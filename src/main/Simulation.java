//******************************************************
// Class: Simulation
// Author: Alex Ephraim
// Created: Oct 27, 2021
// Modified: Dec 5, 2021
//
// Purpose: A class that holds the planets and updates them each frame.
//
// Attributes: scaler: double, running: boolean, planets: ArrayList<Planet>, initialPlanets: ArrayList<Planet>
// Methods: Simulation(), update(double timestep): void, addPlanet(Planet p): void, getPlanets(): ArrayList<Planet>, removePlanet(double[] loc): void, calculateCollisions(): void, reset(): void, clear(): void, checkInitial(): void, getScaler(): double, isRunning(): boolean, changeRunning(boolean b): void, setPlanets(ArrayList<Planet> p): void, getInitialPlanets(): ArrayList<Planet>
//
//******************************************************

package main;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.paint.Color;

public class Simulation {

	private double scaler = 3E9;

	private boolean running;

	public ArrayList<Planet> planets;
	private ArrayList<Planet> initialPlanets;

	public Simulation() {
		running = false;
		planets = new ArrayList<Planet>();
		initialPlanets = new ArrayList<Planet>();
	}

	public void update(double timestep) {

		for (Planet p : planets) {
			p.calculateForces(planets);
		}
		for (Planet p : planets) {

			p.calculateVelocity(timestep);
			p.calculatePosition(timestep);
		}
		calculateCollisions();
	}

	public void addPlanet(Planet p) {
		planets.add(p);
		checkInitial();
	}

	public ArrayList<Planet> getPlanets() {
		return planets;
	}

	public void removePlanet(double[] loc) {
		Iterator<Planet> iter = planets.iterator();
		while (iter.hasNext()) {
			Planet p = iter.next();
			if (Math.abs(p.getX() - loc[0]) < 100 && Math.abs(p.getY() - loc[1]) < 100) {
				iter.remove();
			}
		}
		checkInitial();
	}

	private void calculateCollisions() {
		ArrayList<Planet> toAdd = new ArrayList<Planet>();
		Iterator<Planet> iter1 = planets.iterator();
		while (iter1.hasNext()) {
			Planet p1 = iter1.next();

			if (!p1.hasCollided()) {
				Iterator<Planet> iter2 = planets.iterator();
				while (iter2.hasNext()) {
					Planet p2 = iter2.next();
					if (!p2.hasCollided() && !p1.equals(p2)) {
						double x1 = p1.getX() / scaler;
						double y1 = p1.getY() / scaler;
						double x2 = p2.getX() / scaler;
						double y2 = p2.getY() / scaler;
						double r1 = ((p1.getMass() - 1E10) / (5E27 - 1E10)) * (20 - 5) + 5;
						double r2 = ((p2.getMass() - 1E10) / (5E27 - 1E10)) * (20 - 5) + 5;

						double distSq = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
						double radSumSq = (r1 + r2) * (r1 + r2);
						if (distSq <= radSumSq) {
							p1.collide();
							p2.collide();

							double x = (((x1) + (x2)) / 2);
							double y = (((y1) + (y2)) / 2);

							double px = (p1.getVx() * p1.getMass()) + (p2.getVx() * p2.getMass());
							double py = (p1.getVy() * p1.getMass()) + (p2.getVy() * p2.getMass());
							double mass = (p1.getMass() + p2.getMass());
							double vx = px / mass;
							double vy = py / mass;

							double rgb1[] = { p1.getColor().getRed(), p1.getColor().getGreen(),
									p1.getColor().getBlue() };
							double rgb2[] = { p2.getColor().getRed(), p2.getColor().getGreen(),
									p2.getColor().getBlue() };

							Color color = new Color((rgb1[0] + rgb2[0]) / 2, (rgb1[1] + rgb2[1]) / 2,
									(rgb1[2] + rgb2[2]) / 2, 1);

							String name;
							String np1 = p1.getName();
							String np2 = p2.getName();
							np1 = np1.replaceAll("-", "");
							np2 = np2.replaceAll("-", "");
							if (p1.getMass() > p2.getMass()) {
								name = np1.substring(0, np1.length() / 2) + "-" + np2.substring(0, np2.length() / 2);
							} else {
								name = np2.substring(0, np2.length() / 2) + "-" + np1.substring(0, np1.length() / 2);
							}
							Planet p3 = new Planet(x * scaler, y * scaler, vx, vy, mass, color, name);
							if (p1.isLocked() || p2.isLocked()) {
								p3.setLocked(true);
							}
							p3.setMergeResult();
							p3.setMergeCount(p1.getMergeCount() + p2.getMergeCount() + 1);
							p3.setDistance(p1.getDistance() + p2.getDistance());
							if (p1.getMax() > p2.getMax()) {
								p3.setMax(p1.getMax());
							} else {
								p3.setMax(p2.getMax());
							}
							toAdd.add(p3);
						}
					}
				}
			}
		}
		Iterator<Planet> iter3 = planets.iterator();
		while (iter3.hasNext()) {
			Planet p = iter3.next();
			if (p.hasCollided()) {
				iter3.remove();
			}
		}
		for (Planet p : toAdd) {
			planets.add(p);
		}
	}

	public void reset() {
		planets.clear();
		for (Planet p : initialPlanets) {
			planets.add(new Planet(p));
		}
		running = false;
	}

	public void clear() {
		planets.clear();
		running = false;
	}

	public void checkInitial() {
		if (!running) {
			initialPlanets.clear();
			for (Planet p : planets) {
				initialPlanets.add(new Planet(p));
			}
		}
	}

	public double getScaler() {
		return scaler;
	}

	public boolean isRunning() {
		return running;
	}

	public void changeRunning(boolean b) {
		running = b;

	}

	public void setPlanets(ArrayList<Planet> p) {
		planets = p;
	}

	public ArrayList<Planet> getInitialPlanets() {
		return initialPlanets;
	}
}
