//******************************************************
// Class: Particle
// Author: Alex Ephraim
// Created: Oct 27, 2021
// Modified: Dec 6, 2021
//
// Purpose: A class that extends the renderedobject class and represents a part of the trail drawn behind moving planets..
//
// Attributes: time = 50: double, max = 5: double, planet: Planet
// Methods: +Particle(double x, double y, Planet p), calculateColor(): Color, tick(): boolean, getPlanet(): Planet
//
//******************************************************

package main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Particle extends RenderedObject {

	private double time = 50;

	private double max = 5;

	private Planet planet;

	public Particle(double x, double y, Planet p) {
		super(x, y, Color.RED, new Circle());
		planet = p;
		shape.setLayoutX(getScreenX());
		shape.setLayoutY(getScreenY());
		((Circle) shape).setRadius(10);
		Circle circ = new Circle(getScreenX(), getScreenY(), 5 * (time / 50));
		circ.setFill(getColor());
		circ.setOpacity(time / 50);

	}

	public Color calculateColor() {
		return Color.hsb(360 * (time / 50), 1, 1);
	}

	public boolean tick() {
		time--;
		shape.setFill(calculateColor());
		((Circle) shape).setRadius(max * (time / 50));
		if (time <= 0) {
			return true;
		}
		return false;
	}

	public Planet getPlanet() {
		return planet;
	}

}
