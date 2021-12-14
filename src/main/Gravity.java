//******************************************************
// Class: Gravity
// Author: Alex Ephraim
// Created: Oct 27, 2021
// Modified: Dec 6, 2021
//
// Purpose: The main class that creates the GuiManager and Simulation.
//
// Attributes: -guiManager: GuiManager, simulation: Simulation, last: double, count: int
// Methods: main(String[] args): void, start(Stage primaryStage): void
//
//******************************************************

package main;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Gravity extends Application {

	private GuiManager guiManager;
	private Simulation simulation;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);
		primaryStage.setTitle("Gravity");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
		Pane splash = new Pane();
		splash.setPrefSize(640, 480);
		BackgroundImage BI = new BackgroundImage(new Image(getClass().getResourceAsStream("icon.png")),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize((splash.getPrefHeight() * 3) / 4, (splash.getPrefHeight() * 3) / 4, false, false,
						false, false));
		splash.setBackground(new Background(BI));
		Text title = new Text();
		title.setText("Gravity by Alex Ephraim");
		title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 22));
		title.setTextAlignment(TextAlignment.CENTER);
		title.autosize();
		title.setLayoutX((splash.getPrefWidth() / 2) - (title.getLayoutBounds().getWidth() / 2));
		title.setLayoutY(splash.getPrefHeight() / 14);

		Text help = new Text();
		help.setText(
				"Left click on empty space to place a planet. Left click on an existing planet to edit it.\nRight click on an existing planet to delete it.");
		help.setTextAlignment(TextAlignment.CENTER);
		help.autosize();
		help.setLayoutX((splash.getPrefWidth() / 2) - (help.getLayoutBounds().getWidth() / 2));
		help.setLayoutY((splash.getPrefHeight() * 23) / 25);
		splash.getChildren().addAll(help, title);

		FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), splash);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		fadeIn.setCycleCount(1);

		// Finish splash with fade out effect
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), splash);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.setCycleCount(1);

		primaryStage.setScene(new Scene(splash));
		primaryStage.show();
		AnimationTimer timer = new timer(fadeOut);
		fadeIn.play();

		// After fade in, start fade out
		fadeIn.setOnFinished((e) -> {

			timer.start();
		});

		// After fade out, load actual content
		fadeOut.setOnFinished((e) -> {
			timer.stop();
			simulation = new Simulation();

			guiManager = new GuiManager(primaryStage, simulation);

			AnimationTimer updater = new Updater();
			updater.start();
		});

	}

	private double last = 0;
	private int count = 0;

	private class timer extends AnimationTimer {
		FadeTransition fadeOut;

		public timer(FadeTransition f) {
			fadeOut = f;
		}

		@Override
		public void handle(long now) {
			if (now - last > 16666666.66) {
				last = now;
				count++;
				if (count >= 60) {
					fadeOut.play();
				}
			}
		}
	}

	private class Updater extends AnimationTimer {
		@Override
		public void handle(long now) {
			if (now - last > 16666666.66) {
				if (simulation.isRunning()) {
					// Update all planets.
					simulation.update(guiManager.getTimestep());
					last = now;
				}
				guiManager.update(now);
			}
		}
	}
}
