package com.stilldabomb.Pacman.maps;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.stilldabomb.Pacman.Pacman;
import com.stilldabomb.Pacman.PacmanRenderer;
import com.stilldabomb.Pacman.entities.Entity;
import com.stilldabomb.Pacman.entities.EntityWall;
import com.stilldabomb.Pacman.entities.SpawnPoint;

public class MapRenderer {
	
	public MapRenderer(List<Entity> entities, List<SpawnPoint> ghostSpawns) throws FileNotFoundException {
		this(entities, ghostSpawns, "map");
	}
	
	public MapRenderer(List<Entity> entities, List<SpawnPoint> ghostSpawns, String mapName) throws FileNotFoundException {
		this(entities, ghostSpawns, new BufferedReader(new FileReader(MapRenderer.class.getResource("/resources/maps/" + mapName + ".pcm").getFile())));
	}
	
	public MapRenderer(List<Entity> entities, List<SpawnPoint> ghostSpawns, BufferedReader map) {
		String line;
		int width = 0, height = 0, x = -1, y = -1;
		try {
			while ((line = map.readLine()) != null) {
				for (String e : line.split(" ")) {
					switch (e.toLowerCase()) {
					case "x":
						entities.add(new EntityWall(x, y));
						break;
					case "g":
						ghostSpawns.add(new SpawnPoint(x, y));
						break;
					}
					if (e.toLowerCase().equals("x")) {
						entities.add(new EntityWall(x, y));
					}
					x++;
					if (x > width) width = x;
					if (y > height) height = y;
				}
				y++;
				x = -1;
			}
			PacmanRenderer.setWidth(width - 1);
			PacmanRenderer.setHeight(height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPCMFromMap(List<Entity> entities) {
		int width = 0;
		int height = 0;
		for (Entity e : entities) {
			int x = ((int)e.getX()/Pacman.pacmanSize) + 1;
			int y = ((int)e.getY()/Pacman.pacmanSize) + 1;
			if (x > width) width = x;
			if (y > height) height = y;
		}
		String[][] temp = new String[height + 1][width + 1];
		entities.stream().filter(Entity::isNotPacman).forEach(e -> {
			int x = ((int)e.getX()/Pacman.pacmanSize) + 1;
			int y = ((int)e.getY()/Pacman.pacmanSize) + 1;
			String type = "o";
			if (e instanceof EntityWall) {
				type = "x";
			}
			String[] xL = temp[y];
			xL[x] = type;
			temp[y] = xL;
		});
		String pcm = "";
		for (String[] y : temp) {
			String yline = "";
			for (String x : y) {
				yline += (x == null ? "o" : x) + " ";
			}
			pcm += yline.trim() + "\n";
		}
		return pcm;
	}
	
}
