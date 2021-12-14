//******************************************************
// Class: RenderedObject
// Author: Alex Ephraim
// Created: Oct 27, 2021
// Modified: Dec 5, 2021
//
// Purpose: A class that represents any object that needs to be rendered in the simulation.
//
// Attributes: scaler: double, x: double, y: double, shape: Shape, color: Color,
// Methods: RenderedObject(double x, double y, Color color, Shape shape), getScreenX(): double, getScreenY(): double, getScaler(): double, getX(): double, setX(double x): void, getY(): double, setY(double y): void, getShape(): Shape, getColor(): Color, setColor(Color color): void
//******************************************************

package main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public abstract class RenderedObject {

	protected double scaler = 3E9;

	protected double x;
	protected double y;

	protected Shape shape;
	protected Color color;

	public RenderedObject(double x, double y, Color color, Shape shape) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.shape = shape;
		shape.setFill(color);
		shape.setLayoutX(getScreenX());
		shape.setLayoutY(getScreenY());
	}

	public double getScreenX() {
		return x / scaler;
	}

	public double getScreenY() {
		return y / scaler;
	}

	public double getScaler() {
		return scaler;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Shape getShape() {
		shape.setLayoutX(getScreenX());
		shape.setLayoutY(getScreenY());
		return shape;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
