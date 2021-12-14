//******************************************************
// Class: FileHandler
// Author: Alex Ephraim
// Created: Oct 27, 2021
// Modified: Dec 1, 2021
//
// Purpose: A class that handles saving and loading start states.
//
// Attributes: 
// Methods: FileHandler(): loadNames(): ArrayList<String>, load(File selectedFile): ArrayList<Planet>, save(File file, ArrayList<Planet> planets): void
//
//******************************************************

package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javafx.scene.paint.Color;

public class FileHandler {

	public FileHandler() {

	}

	public ArrayList<String> loadNames() {
		try {
			ArrayList<String> names = new ArrayList<String>();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(this.getClass().getResourceAsStream("names.txt")));
			String line;
			while ((line = br.readLine()) != null) {
				names.add(line);
			}
			br.close();
			return names;
		} catch (Exception e) {
			System.out.println("cant load names");
		}
		return null;

	}

	public ArrayList<Planet> load(File selectedFile) {

		try {
			ArrayList<Planet> planets = new ArrayList<Planet>();
			FileReader fr = new FileReader(selectedFile);
			BufferedReader br = new BufferedReader(fr);
			String line;

			while ((line = br.readLine()) != null) {
				String[] properties = line.split(",");

				String name = properties[0];
				double mass = Double.valueOf(properties[1]);
				double x = Double.valueOf(properties[2]);
				double y = Double.valueOf(properties[3]);
				double vx = Double.valueOf(properties[4]);
				double vy = Double.valueOf(properties[5]);
				Color color = Color.valueOf(properties[6]);
				boolean locked = Boolean.valueOf(properties[7]);
				Planet p = new Planet(x, y, vx, vy, mass, color, name);
				if (locked) {
					p.setLocked(true);
				}
				planets.add(p);

			}
			br.close();
			return planets;
		} catch (Exception e) {
			System.out.println("Error loading file!");
		}

		return null;
	}

	public void save(File file, ArrayList<Planet> planets) {
		try {
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (Planet p : planets) {
				String s = p.getName() + "," + p.getMass() + "," + p.getX() + "," + p.getY() + "," + p.getVx() + ","
						+ p.getVy() + "," + p.getColor().toString() + "," + p.isLocked();
				bufferedWriter.write(s);
				bufferedWriter.newLine();

			}
			bufferedWriter.close();
		} catch (Exception e) {
			System.out.println("Error writting file! Does it already exist?");
		}

	}
}
