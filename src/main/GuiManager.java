//******************************************************
// Class: GuiManager
// Author: Alex Ephraim
// Created: Oct 27, 2021
// Modified: Dec 6, 2021
//
// Purpose: A class that handles all the graphics processing.
//
// Attributes: renderHeight: double, renderWidth: double, initComplete: boolean, simulation: Simulation, stage: Stage, simScreen: SimScreen, scene: Scene, planets: ArrayList<Planet>, particles: ArrayList<Particle>, vectors: ArrayList<Vector>, names: ArrayList<String>
// Methods: GuiManager(Stage primaryStage, Simulation s), update(long time): void, getTimestep(): double, letterbox(final Scene scene, final Pane contentPane): void
//
//******************************************************

package main;

import java.util.ArrayList;
import java.util.Iterator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class GuiManager {

	private double renderHeight = 480;
	private double renderWidth = 640;

	private boolean initComplete;

	private Simulation simulation;

	private Stage stage;
	private SimScreen simScreen;
	private Scene scene;

	private ArrayList<Planet> planets;
	private ArrayList<Particle> particles;
	private ArrayList<Vector> vectors;
	private ArrayList<String> names;

	public GuiManager(Stage primaryStage, Simulation s) {

		initComplete = false;

		names = new FileHandler().loadNames();
		planets = new ArrayList<Planet>();
		particles = new ArrayList<Particle>();
		vectors = new ArrayList<Vector>();

		simulation = s;
		stage = primaryStage;

		stage.setTitle("Gravity");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));

		simScreen = new SimScreen(renderHeight, renderWidth, simulation);

		scene = new Scene(simScreen);
		Scale scale = new Scale(1, 1);
		scale.setPivotX(0);
		scale.setPivotY(0);
		scene.getRoot().getTransforms().setAll(scale);
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();

		letterbox(scene, simScreen);

		scene.setOnMouseReleased(e -> {

			double x = e.getSceneX() / scene.getRoot().getTransforms().get(0).getMxx();
			double y = e.getSceneY() / scene.getRoot().getTransforms().get(0).getMyy();

			if (x < renderWidth - (renderWidth - simScreen.getMainPane().getWidth())
					&& y < renderHeight - (renderHeight - simScreen.getMainPane().getHeight())) {

				if (e.getButton() == MouseButton.PRIMARY) {
					boolean selectThisClick = false;
					for (Planet p : planets) {
						Circle c = (Circle) p.getShape();
						if (Math.abs(Math.hypot(x - c.getLayoutX(), y - c.getLayoutY())) < c.getRadius()) {
							simScreen.setJustSelected();
							simScreen.updateSelected(p);
							selectThisClick = true;
						}
					}

					if (!selectThisClick) {

						Planet p = new Planet(x * simulation.getScaler(), y * simulation.getScaler(), 0, 0,
								(Math.random() * (5E27 - 1E10)) + 1E10, simScreen.getColor(),
								names.get((int) Math.floor(Math.random() * (59 - 0 + 1) + 0)));

						simScreen.setJustSelected();
						p.setColor(new Color(Math.random(), Math.random(), Math.random(), 1));
						simulation.addPlanet(p);
						simScreen.updateSelected(p);
					}
				} else if (e.getButton() == MouseButton.SECONDARY) {

					for (Planet p : planets) {

						Circle c = (Circle) p.getShape();
						if (Math.abs(Math.hypot(x - c.getLayoutX(), y - c.getLayoutY())) < c.getRadius()) {

							simulation.removePlanet(new double[] { c.getLayoutX() * simulation.getScaler(),
									c.getLayoutY() * simulation.getScaler() });
							simScreen.updateSelected(null);
						}
					}
				}
			}

		});

	}

	public void update(long time) {
		if (!initComplete) {
			stage.sizeToScene();
			stage.setMinHeight(stage.getHeight());
			stage.setMinWidth(stage.getWidth());
			initComplete = true;
		}

		if (simScreen.getMainPane().getChildren() != null) {
			simScreen.getMainPane().getChildren().clear();
		}
		if (simulation.isRunning()) {
			simScreen.updateSliders();
		}
		planets.clear();
		vectors.clear();
		planets.addAll(simulation.getPlanets());

		if (simScreen.getTrails() && simulation.isRunning()) {
			for (Planet p : simulation.getPlanets()) {
				particles.add(new Particle(p.getX(), p.getY(), p));

			}
			Iterator<Particle> iter = particles.iterator();
			while (iter.hasNext()) {
				if (iter.next().tick()) {
					iter.remove();
				}
			}
			for (Particle p : particles) {
				simScreen.getMainPane().getChildren().add(p.getShape());
			}
		} else {
			particles.clear();
		}

		for (Planet p : planets) {
			if (simulation.isRunning()) {
				if (p.getMergeResult() && !planets.contains(simScreen.getSelected())) {
					simScreen.updateSelected(p);
				}
			}

			simScreen.getMainPane().getChildren().add(p.getShape());
		}
		if (simScreen.getVectors()) {
			for (Planet p : planets) {
				vectors.add(new Vector(p.getX(), p.getY(), p));
			}
			for (Vector v : vectors) {
				simScreen.getMainPane().getChildren().add(v.getShape());
			}
		}

	}

	public double getTimestep() {
		return simScreen.getTimestep();
	}

	private void letterbox(final Scene scene, final Pane contentPane) {
		final double initWidth = scene.getWidth();
		final double initHeight = scene.getHeight();

		final double ratio = initWidth / initHeight;
		SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(scene, ratio, initHeight, initWidth,
				contentPane);
		scene.widthProperty().addListener(sizeListener);
		scene.heightProperty().addListener(sizeListener);

	}

	private static class SceneSizeChangeListener implements ChangeListener<Number> {
		private final Scene scene;
		private final double ratio;
		private final double initHeight;
		private final double initWidth;
		private final Pane contentPane;

		public SceneSizeChangeListener(Scene scene, double ratio, double initHeight, double initWidth,
				Pane contentPane) {
			this.scene = scene;
			this.ratio = ratio;
			this.initHeight = initHeight;
			this.initWidth = initWidth;
			this.contentPane = contentPane;

		}

		@Override
		public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
			final double newWidth = scene.getWidth();
			final double newHeight = scene.getHeight();
			double scaleFactor = newWidth / newHeight > ratio ? newHeight / initHeight : newWidth / initWidth;
			if (scaleFactor >= 1) {
				Scale scale = new Scale(scaleFactor, scaleFactor);
				scale.setPivotX(0);
				scale.setPivotY(0);
				scene.getRoot().getTransforms().setAll(scale);
				contentPane.setPrefWidth(newWidth / scaleFactor);
				contentPane.setPrefHeight(newHeight / scaleFactor);

			} else {
				contentPane.setPrefWidth(Math.max(initWidth, newWidth));
				contentPane.setPrefHeight(Math.max(initHeight, newHeight));
			}
		}
	}

}
