//******************************************************
// Class: Planet
// Author: Alex Ephraim
// Created: Oct 27, 2021
// Modified: Dec 6, 2021
//
// Purpose: A class representing a planet object.
//
// Attributes: -name: String, mass: double, fx: double, fy: double, vx: double, vy: double, collided: boolean, locked: boolean, mergeResult: boolean, G: double, distance: double, max: double, mergeCount: int
// Methods: Planet(double x, double y, double vx, double vy, double m, Color c, String n), Planet(Planet p), getMergeCount(): int, setMergeCount(int i): void, getMax(): double, getDistance(): double, setDistance(double d): void, setMax(double m): void, calculatePosition(double timestep): void, calculateForces(ArrayList<Planet> planets): void, calculateVelocity(double timestep): void, updateShape(): void, setMergeResult(): void, getMergeResult(): boolean, collide(): void, isLocked(): boolean, setLocked(boolean b): void, hasCollided(): boolean, getMass(): double, setMass(double m): void, getName(): String, setName(String name): void, getVx(): double, setVx(double vx): void, getVy(): double, setVy(double vy): void
//
//******************************************************

package main;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Planet extends RenderedObject {

	private String name;
	private double mass;
	private double fx;
	private double fy;
	private double vx;
	private double vy;
	private boolean collided;
	private boolean locked;
	private boolean mergeResult;
	private double G = 6.673e-11;
	private double distance;
	private double max;
	private int mergeCount;

	public Planet(double x, double y, double vx, double vy, double m, Color c, String n) {
		super(x, y, c, new Circle(0, 0, ((m - 1E10) / (5E27 - 1E10)) * (20 - 5) + 5));
		mergeResult = false;
		locked = false;
		collided = false;
		mass = m;
		this.vx = vx;
		this.vy = vy;
		distance = 0;
		mergeCount = 0;
		max = 0;
		name = n;
	}

	public Planet(Planet p) {
		super(p.getX(), p.getY(), p.getColor(), p.getShape());
		setLocked(p.isLocked());
		collided = false;
		this.distance = p.getDistance();
		this.mergeCount = 0;
		this.max = 0;
		this.name = p.getName();
		this.mass = p.getMass();
		this.vx = p.getVx();
		this.vy = p.getVy();
	}

	public int getMergeCount() {
		return mergeCount;
	}

	public void setMergeCount(int i) {
		mergeCount = i;
	}

	public double getMax() {
		return max;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double d) {
		distance = d;
	}

	public void setMax(double m) {
		max = m;
	}

	public void calculatePosition(double timestep) {
		if (!locked) {
			x += timestep * vx;
			y += timestep * vy;
			distance += Math.abs(timestep * vy) + Math.abs(timestep * vx);
		}
	}

	public void calculateForces(ArrayList<Planet> planets) {
		fx = fy = 0;
		for (Planet p : planets) {
			if (p != this) {
				double angle = Math.atan2(p.getY() - y, p.getX() - x);
				double dist = Math.abs(Math.hypot(x - p.getX(), y - p.getY()));
				double f = (G * mass * p.getMass()) / Math.pow(dist, 2);
				fy += Math.sin(angle) * f;
				fx += Math.cos(angle) * f;
			}
		}
	}

	public void calculateVelocity(double timestep) {

		vx += timestep * (fx / mass);
		vy += timestep * (fy / mass);
		double speed = Math.abs(Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2)));
		if (speed > max) {
			max = speed;
		}
	}

	public void updateShape() {
		// shape = new Circle()
	}

	public void setMergeResult() {
		mergeResult = true;
	}

	public boolean getMergeResult() {
		return mergeResult;
	}

	public void collide() {
		collided = true;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean b) {
		locked = b;
	}

	public boolean hasCollided() {
		return collided;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double m) {
		mass = m;
		Circle c = new Circle(0, 0, ((mass - 1E10) / (5E27 - 1E10)) * (20 - 5) + 5);
		c.setLayoutX(getScreenX());
		c.setLayoutY(getScreenY());
		c.setFill(color);
		shape = c;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getVx() {
		if (Double.isNaN(vx)) {
			return 0;
		}
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		if (Double.isNaN(vy)) {
			return 0;
		}
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}
}
