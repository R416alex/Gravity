//******************************************************
// Class: Vector
// Author: Alex Ephraim
// Created: Oct 27, 2021
// Modified: Dec 6, 2021
//
// Purpose: A class representing the vector object that shows a planets speed and direction.
//
// Attributes: planet: Planet, endX: double, endY: double 
// Methods: Vector(double x, double y, Planet p), update(): void
//
//******************************************************

package main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class Vector extends RenderedObject {

	private Planet planet;
	private double endX;
	private double endY;

	public Vector(double x, double y, Planet p) {
		super(x, y, Color.RED, new Line());
		planet = p;
		update();
	}

	public void update() {
		if (planet.isLocked() || (planet.getVx() == 0 && planet.getVy() == 0)) {
			shape.setOpacity(0);
		} else {
			shape.setOpacity(1);
		}
		endX = (planet.getVx() / 20);
		endY = (planet.getVy() / 20);
		double r = 1.0 - planet.getColor().getRed();
		double g = 1.0 - planet.getColor().getGreen();
		double b = 1.0 - planet.getColor().getBlue();
		double a = planet.getColor().getOpacity();
		color = new Color(r, g, b, a);
		((Line) shape).setStrokeLineCap(StrokeLineCap.ROUND);
		((Line) shape).setLayoutX(getScreenX());
		((Line) shape).setLayoutY(getScreenY());
		((Line) shape).setEndX(endX);
		((Line) shape).setEndY(endY);
		((Line) shape).setStrokeWidth(5);
		((Line) shape).setStroke(color);
		((Line) shape).setFill(color);
	}

}
