//******************************************************
// Author: Alex Ephraim
// Created: Oct 27, 2021
// Modified: Dec 1, 2021
//
// Purpose: A class that configures how the GUI will look.
//
// Attributes: height: double, width: double, timestep: double, trailEffect: boolean, musicToggle: boolean, selectedPlanet: Planet, name: TextField, locked: CheckBox, simulation: Simulation, musicPlayer: MediaPlayer,colorPicker: ColorPicker, velYSlider: Slider, velXSlider: Slider, massSlider: Slider, borderPane: BorderPane, mainPane: Pane, rightPane: Pane, bottomPane: Pane, massText: Text, velXText: Text, velYText: Text, mergeText: Text, maxText: Text, maxText2: Text, distanceText: Text, distanceText2: Text, left: Button, right: Button, up: Button, down: Button, vectorEffect: boolean, justSelected: boolean
// Methods: SimScreen(double h, double w, Simulation s), updateSelected(Planet p): void, getColor(): Color, initNodes(): void, changeVel(): void, initRight(): void, initBottom(): void, initMain(): void, getTimestep(): double, getMainPane(): Pane, getSelected(): Planet, getVectors(): boolean, getTrails(): boolean, setJustSelected(): void, updateSliders(): void
//******************************************************

package main;

import java.io.File;
import java.text.DecimalFormat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class SimScreen extends Pane {

	private double height;
	private double width;

	private double timestep;
	private boolean trailEffect;
	private boolean musicToggle;

	private Planet selectedPlanet;
	private TextField name;
	private CheckBox locked;

	private Simulation simulation;

	private MediaPlayer musicPlayer;
	private ColorPicker colorPicker;
	private Slider velYSlider;
	private Slider velXSlider;
	private Slider massSlider;

	private BorderPane borderPane;
	private Pane mainPane;
	private Pane rightPane;
	private Pane bottomPane;
	private Text massText;
	private Text velXText;
	private Text velYText;
	private Text mergeText;
	private Text maxText;
	private Text maxText2;
	private Text distanceText;
	private Text distanceText2;
	private Button left;
	private Button right;
	private Button up;
	private Button down;
	private boolean vectorEffect;
	private boolean justSelected;

	public SimScreen(double h, double w, Simulation s) {
		height = h;
		width = w;
		simulation = s;
		musicToggle = true;
		vectorEffect = true;
		justSelected = false;
		trailEffect = true;
		timestep = 5E6;

		Media media = new Media(getClass().getResource("backgroundmusic.mp3").toString());
		musicPlayer = new MediaPlayer(media);

		musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		musicPlayer.setVolume(0.5);

		musicPlayer.play();

		initNodes();

	}

	public void updateSelected(Planet p) {
		selectedPlanet = p;
		updateSliders();
		if (p != null) {
			locked.setSelected(selectedPlanet.isLocked());
			name.setText(selectedPlanet.getName());
			name.setAlignment(Pos.CENTER);
			colorPicker.setValue(selectedPlanet.getColor());
		} else {
			name.setText("Nothing");
		}
		justSelected = false;
		name.setLayoutX(rightPane.getPrefWidth() / 2 - (name.getLayoutBounds().getWidth() / 2));
	}

	public Color getColor() {
		return colorPicker.getValue();
	}

	private void initNodes() {
		borderPane = new BorderPane();

		initRight();
		initBottom();
		initMain();

		borderPane.setCenter(mainPane);
		borderPane.setRight(rightPane);
		borderPane.setBottom(bottomPane);
		borderPane.setPrefSize(width, height);

		this.getChildren().add(borderPane);

	}

	private void changeVel() {
		if (selectedPlanet != null) {
			selectedPlanet.setVx(velXSlider.getValue());
			selectedPlanet.setVy(velYSlider.getValue());
			simulation.checkInitial();
		}

	}

	private void initRight() {
		rightPane = new Pane();
		rightPane.setPrefSize(width / 5, height);
		rightPane.setStyle("-fx-background-color: #34eb34; ");

		distanceText = new Text();

		distanceText.setText("Distance:");
		distanceText.setTextAlignment(TextAlignment.CENTER);
		distanceText.autosize();
		distanceText.setLayoutX(((rightPane.getPrefWidth()) / 2) - (distanceText.getLayoutBounds().getWidth() / 2));
		distanceText.setLayoutY(((rightPane.getPrefHeight() * 6) / 27) - (height / 100));

		distanceText2 = new Text();

		distanceText2.setText("0.0e+00");
		distanceText2.setTextAlignment(TextAlignment.CENTER);
		distanceText2.autosize();
		distanceText2.setLayoutX(((rightPane.getPrefWidth()) / 2) - (distanceText2.getLayoutBounds().getWidth() / 2));
		distanceText2.setLayoutY(((rightPane.getPrefHeight() * 7) / 27) - (height / 70));

		maxText = new Text();
		maxText.setText("Max Speed:");
		maxText.setTextAlignment(TextAlignment.CENTER);
		maxText.autosize();
		maxText.setLayoutX(((rightPane.getPrefWidth()) / 2) - (maxText.getLayoutBounds().getWidth() / 2));
		maxText.setLayoutY(((rightPane.getPrefHeight() * 8) / 28) - (height / 100));

		maxText2 = new Text();
		maxText2.setText("0.0e+00");
		maxText2.setTextAlignment(TextAlignment.CENTER);
		maxText2.autosize();
		maxText2.setLayoutX(((rightPane.getPrefWidth()) / 2) - (maxText2.getLayoutBounds().getWidth() / 2));
		maxText2.setLayoutY(((rightPane.getPrefHeight() * 9) / 28) - (height / 70));

		mergeText = new Text("Merge Count: 0");
		mergeText.autosize();
		mergeText.setLayoutX(((rightPane.getPrefWidth()) / 2) - (mergeText.getLayoutBounds().getWidth() / 2));
		mergeText.setLayoutY(((rightPane.getPrefHeight() * 34) / 100));

		name = new TextField("Nothing");
		name.setPrefSize(rightPane.getPrefWidth() - (rightPane.getPrefWidth() / 10), rightPane.getPrefHeight() / 12);
		name.setAlignment(Pos.CENTER);
		name.setLayoutX(rightPane.getPrefWidth() / 2 - (name.getPrefWidth() / 2));
		name.setLayoutY(rightPane.getPrefHeight() / 45);
		name.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				selectedPlanet.setName(name.getText());
			} catch (Exception b) {
			}
		});

		massText = new Text("Mass: 5.0e+27");
		massText.autosize();
		massText.setLayoutX(((rightPane.getPrefWidth()) / 2) - (massText.getLayoutBounds().getWidth() / 2));
		massText.setLayoutY(((rightPane.getPrefHeight() * 10) / 26) - (height / 100));

		massSlider = new Slider(1E10, 1E28, (1E10 + 1E28) / 2);
		massSlider.setPrefSize(rightPane.getPrefWidth(), rightPane.getPrefHeight() / 24);
		massSlider.setLayoutX((rightPane.getWidth() / 2));
		massSlider.setLayoutY(((rightPane.getPrefHeight() * 5) / 13));
		massSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				if (!simulation.isRunning()) {
					try {
						selectedPlanet.setMass(massSlider.getValue());
						simulation.checkInitial();
					} catch (Exception e) {
					}
					massText.setText("Mass: " + String.format("%6.1e", massSlider.getValue()));
				}
			}
		});

		velXText = new Text("Vel X: 0000.0");
		velXText.autosize();
		velXText.setLayoutX(((rightPane.getPrefWidth()) / 2) - (velXText.getLayoutBounds().getWidth() / 2));
		velXText.setLayoutY(((rightPane.getPrefHeight() * 12) / 26) - (height / 100));

		velXSlider = new Slider(-2000, 2000, 0);
		velXSlider.setPrefSize(rightPane.getPrefWidth(), rightPane.getPrefHeight() / 24);
		velXSlider.setLayoutX((rightPane.getWidth() / 2));
		velXSlider.setLayoutY(((rightPane.getPrefHeight() * 6) / 13));
		velXSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				if (!simulation.isRunning() && !justSelected) {
					changeVel();
				}
				DecimalFormat df = new DecimalFormat("0000.0");
				velXText.setText("Vel X: " + df.format(velXSlider.getValue()));
			}
		});
		velYText = new Text("Vel Y: 0000.0");
		velYText.autosize();
		velYText.setLayoutX(((rightPane.getPrefWidth()) / 2) - (velYText.getLayoutBounds().getWidth() / 2));
		velYText.setLayoutY(((rightPane.getPrefHeight() * 7) / 13) - (height / 100));

		velYSlider = new Slider(-2000, 2000, 0);
		velYSlider.setPrefSize(rightPane.getPrefWidth(), rightPane.getPrefHeight() / 24);
		velYSlider.setLayoutX((rightPane.getWidth() / 2));
		velYSlider.setLayoutY(((rightPane.getPrefHeight() * 7) / 13));
		velYSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				if (!simulation.isRunning() && !justSelected) {
					changeVel();
				}
				DecimalFormat df = new DecimalFormat("0000.0");
				velYText.setText("Vel Y: " + df.format(velYSlider.getValue()));
			}
		});

		colorPicker = new ColorPicker();
		colorPicker.setPrefSize((rightPane.getPrefWidth() * 9) / 10, height / 20);
		colorPicker.setLayoutX((rightPane.getPrefWidth() / 2) - (colorPicker.getPrefWidth() / 2));
		colorPicker.setLayoutY(height / 8);
		colorPicker.setValue(new Color(Math.random(), Math.random(), Math.random(), 1));
		colorPicker.setStyle("-fx-font-size: 10;");
		colorPicker.setOnAction(e -> {
			try {
				selectedPlanet.setColor(colorPicker.getValue());
				simulation.checkInitial();
			} catch (Exception e1) {

			}
		});

		locked = new CheckBox("Locked");
		locked.setPrefSize((rightPane.getPrefWidth() * 11) / 18, height / 20);
		locked.setLayoutX((rightPane.getPrefWidth() / 2) - (locked.getPrefWidth() / 2));
		locked.setLayoutY(((height * 5) / 7) + (height / 50));
		locked.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
					Boolean newValue) {
				if (selectedPlanet != null) {
					selectedPlanet.setLocked(newValue);
					simulation.checkInitial();
				}
			}
		});

		up = new Button("↑");
		up.setPrefSize(width / 17.7, height / 32);
		up.setLayoutX((rightPane.getPrefWidth() / 2) - (up.getPrefWidth() / 2));
		up.setLayoutY(((rightPane.getPrefHeight() * 4) / 7) + (height / 50));
		up.setOnAction(e -> {
			if (selectedPlanet != null && !simulation.isRunning()) {
				selectedPlanet.setY(selectedPlanet.getY() - 1E10);
				simulation.checkInitial();
			}
		});
		down = new Button("↓");
		down.setPrefSize(width / 17.7, height / 32);
		down.setLayoutX((rightPane.getPrefWidth() / 2) - (down.getPrefWidth() / 2));
		down.setLayoutY(((rightPane.getPrefHeight() * 4) / 7) + (rightPane.getPrefHeight() / 15) + (height / 50));
		down.setOnAction(e -> {
			if (selectedPlanet != null && !simulation.isRunning()) {
				selectedPlanet.setY(selectedPlanet.getY() + 1E10);
				simulation.checkInitial();
			}
		});
		left = new Button("←");
		left.setPrefSize(height / 14.25, width / 17.7);
		left.setLayoutX((rightPane.getPrefWidth() / 2) - (left.getPrefWidth() / 2) - (rightPane.getPrefWidth() / 3.5));
		left.setLayoutY(((rightPane.getPrefHeight() * 4) / 7) + (rightPane.getPrefHeight() / 15)
				- (left.getPrefHeight() / 2) + (height / 50));
		left.setOnAction(e -> {
			if (selectedPlanet != null && !simulation.isRunning()) {
				selectedPlanet.setX(selectedPlanet.getX() - 1E10);
				simulation.checkInitial();
			}
		});
		right = new Button("→");
		right.setPrefSize(height / 14.25, width / 17.7);
		right.setLayoutX(
				(rightPane.getPrefWidth() / 2) - (right.getPrefWidth() / 2) + (rightPane.getPrefWidth() / 3.5));
		right.setLayoutY(((rightPane.getPrefHeight() * 4) / 7) + (rightPane.getPrefHeight() / 15)
				- (right.getPrefHeight() / 2) + (height / 50));
		right.setOnAction(e -> {
			if (selectedPlanet != null && !simulation.isRunning()) {
				selectedPlanet.setX(selectedPlanet.getX() + 1E10);
				simulation.checkInitial();
			}
		});

		rightPane.getChildren().addAll(name, colorPicker, locked, up, down, left, right, velYSlider, velXSlider,
				massSlider, velYText, velXText, massText, distanceText, distanceText2, maxText, maxText2, mergeText);

	}

	private void initBottom() {
		bottomPane = new Pane();
		bottomPane.setPrefSize(width, height / 5);
		bottomPane.setStyle("-fx-background-color: #0084ff; ");

		Button play = new Button("Play");
		play.setPrefSize(100, 50);
		play.setLayoutX((width / 2) - (play.getPrefWidth() / 2));
		play.setLayoutY(bottomPane.getPrefHeight() / 2.25);
		play.setOnAction(e -> {
			if (simulation.isRunning()) {
				play.setText("Play");
			} else {
				play.setText("Pause");
			}
			simulation.changeRunning(!simulation.isRunning());
		});

		// Needs to be before slider but size changed after all other buttons.
		Text time = new Text("1.000x");

		Slider slider = new Slider(5E6 * 0.1, 5E6 * 5, 5E6);
		slider.setPrefSize(width / 4.25, height / 24);
		slider.setLayoutX(((width) / 2) - (slider.getPrefWidth() / 2));
		slider.setLayoutY(bottomPane.getPrefHeight() / 8);
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				timestep = slider.getValue();

				double ratio = timestep / 5E6;
				DecimalFormat df = new DecimalFormat("0.000");
				time.setText(df.format(ratio) + "x");

			}
		});

		Button reset = new Button("Reset");
		reset.setPrefSize(width / 8, height / 13);
		reset.setLayoutX((rightPane.getPrefWidth() / 2) - (reset.getPrefWidth() / 2));
		reset.setLayoutY((bottomPane.getPrefHeight() / 2) - (reset.getPrefHeight() / 2) - (height / 21));
		reset.setOnAction(e -> {
			simulation.reset();
			play.setText("Play");
			mainPane.getChildren().clear();
			updateSelected(null);
			colorPicker.setValue(new Color(Math.random(), Math.random(), Math.random(), 1));
			timestep = 5E6;
			slider.setValue(5E6);
		});

		Button clear = new Button("Clear");
		clear.setPrefSize(width / 8, height / 13);
		clear.setLayoutX((rightPane.getPrefWidth() / 2) - (clear.getPrefWidth() / 2));
		clear.setLayoutY((bottomPane.getPrefHeight() / 2 - (clear.getPrefHeight() / 2)) + (height / 21));
		clear.setOnAction(e -> {
			play.setText("Play");
			locked.setSelected(false);
			simulation.changeRunning(false);
			simulation.clear();
			mainPane.getChildren().clear();
			colorPicker.setValue(new Color(Math.random(), Math.random(), Math.random(), 1));
			updateSelected(null);
			timestep = 5E6;
			slider.setValue(5E6);

		});

		Button loadButton = new Button();
		loadButton.setPrefSize(width / 8, height / 13);
		loadButton.setText("Load");
		loadButton.setLayoutX(width - (rightPane.getPrefWidth() / 2) - (loadButton.getPrefWidth() / 2));

		loadButton.setLayoutY((bottomPane.getPrefHeight() / 2) - (loadButton.getPrefHeight() / 2) + (height / 21));

		loadButton.setOnAction(e -> {
			simulation.changeRunning(false);
			play.setText("Play");
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(System.getProperty("user.home")));
			fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
			File selectedFile = fc.showOpenDialog(borderPane.getScene().getWindow());
			if (selectedFile != null) {

				simulation.setPlanets(new FileHandler().load(selectedFile));
				selectedPlanet = null;
				updateSliders();
			}
			simulation.checkInitial();
		});

		Button saveButton = new Button();
		saveButton.setPrefSize(width / 8, height / 13);
		saveButton.setText("Save");
		saveButton.setLayoutX(width - (rightPane.getPrefWidth() / 2) - (saveButton.getPrefWidth() / 2));
		saveButton.setLayoutY((bottomPane.getPrefHeight() / 2) - (saveButton.getPrefHeight() / 2) - (height / 21));
		saveButton.setOnAction(e -> {
			simulation.checkInitial();
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(System.getProperty("user.home")));
			fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
			File file = fc.showSaveDialog(borderPane.getScene().getWindow());
			if (file != null) {
				new FileHandler().save(file, simulation.getInitialPlanets());
			}
		});

		// relies on other buttons to be in place.
		time.autosize();
		time.setLayoutX((clear.getLayoutX() + clear.getPrefWidth())
				+ ((play.getLayoutX() - (clear.getLayoutX() + clear.getPrefWidth())) / 2)
				- (time.getLayoutBounds().getWidth() / 2));
		time.setLayoutY(bottomPane.getPrefHeight() / 3.5);

		Button resetTime = new Button("Reset Speed");
		resetTime.setPrefSize(width / 6.3, height / 24);
		resetTime.setLayoutX(loadButton.getLayoutX()
				- ((loadButton.getLayoutX() - (slider.getLayoutX() + slider.getPrefWidth())) / 2)
				- (resetTime.getPrefWidth() / 2));
		resetTime.setLayoutY(slider.getLayoutY());
		resetTime.setOnAction(e -> {
			timestep = 5E6;
			slider.setValue(5E6);
		});

		CheckBox trails = new CheckBox("Trails");
		trails.setSelected(true);
		trails.setPrefSize(width / 10.25, height / 19.2);
		trails.setLayoutX((clear.getLayoutX() + clear.getPrefWidth())
				+ ((play.getLayoutX() - (clear.getLayoutX() + clear.getPrefWidth())) / 2)
				- (trails.getPrefWidth() / 2));
		trails.setLayoutY((bottomPane.getPrefHeight() * 7) / 10);
		trails.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
					Boolean newValue) {
				trailEffect = trails.isSelected();

			}
		});

		CheckBox vectors = new CheckBox("Vectors");
		vectors.setSelected(false);

		vectors.setPrefSize(width / 8.25, height / 19.2);
		vectors.setLayoutX(trails.getLayoutX());
		vectors.setSelected(true);
		vectors.setLayoutY((bottomPane.getPrefHeight() * 4) / 10);
		vectors.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
					Boolean newValue) {
				vectorEffect = vectors.isSelected();

			}
		});

		CheckBox music = new CheckBox("Music");
		music.setSelected(true);
		music.setPrefSize(width / 9.5, height / 19.2);
		music.setLayoutX(
				loadButton.getLayoutX() - ((loadButton.getLayoutX() - (play.getLayoutX() + play.getPrefWidth())) / 2)
						- (music.getPrefWidth() / 2));
		music.setLayoutY((bottomPane.getPrefHeight() * 3) / 5);
		music.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
					Boolean newValue) {
				musicToggle = music.isSelected();
				if (!musicToggle) {
					musicPlayer.stop();
				} else {
					musicPlayer.play();
				}
			}
		});

		bottomPane.getChildren().addAll(slider, reset, clear, play, trails, music, loadButton, saveButton, time,
				resetTime, vectors);
	}

	private void initMain() {
		mainPane = new Pane();
		mainPane.setPrefSize(width - rightPane.getPrefWidth(), height - bottomPane.getPrefHeight());

		BackgroundImage BI = new BackgroundImage(new Image(getClass().getResourceAsStream("background.jpg")),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(height - bottomPane.getPrefHeight(), width - rightPane.getPrefWidth(), false, false,
						true, true));
		mainPane.setBackground(new Background(BI));

	}

	public double getTimestep() {
		return timestep;
	}

	public Pane getMainPane() {
		return mainPane;
	}

	public Planet getSelected() {
		return selectedPlanet;
	}

	public boolean getVectors() {
		return vectorEffect;
	}

	public boolean getTrails() {
		return trailEffect;
	}

	public void setJustSelected() {
		justSelected = true;
	}

	public void updateSliders() {
		if (selectedPlanet != null) {
			distanceText2.setText(String.format("%6.1e", selectedPlanet.getDistance()));
			maxText2.setText(String.format("%6.1e", selectedPlanet.getMax()));
			mergeText.setText("Merge Count: " + selectedPlanet.getMergeCount());
			massSlider.setValue(selectedPlanet.getMass());
			velXSlider.setValue(selectedPlanet.getVx());
			velYSlider.setValue(selectedPlanet.getVy());
			DecimalFormat df = new DecimalFormat("0000.0");
			massText.setText("Mass: " + String.format("%6.1e", selectedPlanet.getMass()));
			velXText.setText("Vel X: " + df.format(selectedPlanet.getVx()));
			velYText.setText("Vel Y: " + df.format(selectedPlanet.getVy()));
		} else {
			name.setText("Nothing");
			distanceText2.setText("0.0e+00");
			maxText2.setText("0.0e+00");
			mergeText.setText("Merge Count: 0");
			massSlider.setValue((1E10 + 1E28) / 2);
			velXSlider.setValue(0);
			velYSlider.setValue(0);
			massText.setText("Mass: 5.0e+27");
			velXText.setText("Vel X: 0000.0");
			velYText.setText("Vel Y: 0000.0");
		}

	}

}
