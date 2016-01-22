package com.stilldabomb.Pacman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import lombok.Getter;
import lombok.Setter;

import com.stilldabomb.Pacman.entities.*;
import com.stilldabomb.Pacman.events.Keyboard;
import com.stilldabomb.Pacman.events.Mouse;
import com.stilldabomb.Pacman.maps.MapRenderer;

public class PacmanRenderer extends JPanel {
	
	@Getter
	private static PacmanRenderer instance;
	@Setter
	private static int width, height;
	@Getter
	private static int windowWidth, windowHeight;
	private long lastUpdate;
	@Getter
	private MapRenderer mapRenderer;
	@Getter
	private List<Entity> entities = new ArrayList<Entity>() {{
		add(new EntityPacman(5, 5));
	}};
	@Getter
	private List<SpawnPoint> ghostSpawnPoints = new ArrayList<SpawnPoint>();
	private int stationaryRenders = 0;
	private BufferedImage stationaryRenderedImage;
	
	public PacmanRenderer() {
		instance = this;
		this.setBackground(Color.BLACK);
		try {
			this.mapRenderer = new MapRenderer(this.entities, this.ghostSpawnPoints);
			windowWidth = width*Pacman.pacmanSize;
			windowHeight = height*Pacman.pacmanSize;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.setPreferredSize(new Dimension(windowWidth, windowHeight));
	}
	
	public void update(double delta) {
		if (Keyboard.isKeyDown(KeyEvent.VK_P)) {
			System.out.println(this.mapRenderer.getPCMFromMap(this.entities));
		}
		if (Pacman.isMapBuilderEnabled()) {
			if (Mouse.isLeftMouseDown()) {
				if (!entities.stream().anyMatch(e -> {
					int myX = (int)(e.getX()/Pacman.pacmanSize);
					int myY = (int)(e.getY()/Pacman.pacmanSize);
					int moX = Mouse.getX()/Pacman.pacmanSize;
					int moY = Mouse.getY()/Pacman.pacmanSize-2;
					return myX == moX && myY == moY;
				})) {
					entities.add(new EntityWall(Mouse.getX()/Pacman.pacmanSize, Mouse.getY()/Pacman.pacmanSize-2));
				}
			}
			if (Mouse.isRightMouseDown()) {
				List<Entity> temp = new ArrayList<Entity>();
				entities.stream().filter(Entity::isStationary).filter(e -> {
					int myX = (int)(e.getX()/Pacman.pacmanSize);
					int myY = (int)(e.getY()/Pacman.pacmanSize);
					int moX = Mouse.getX()/Pacman.pacmanSize;
					int moY = Mouse.getY()/Pacman.pacmanSize-2;
					return myX == moX && myY == moY;
				}).forEach(e -> temp.add(e));
				temp.stream().forEach(e -> entities.remove(e));
			}
		}
		for (Entity entity : entities) {
			entity.update(delta);
		}
	}
	
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		g.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		long now = System.currentTimeMillis();
		if (lastUpdate == 0) lastUpdate = now;
		if (Math.abs(now - lastUpdate) > 25) {
			update((now - lastUpdate)/1000D);
			lastUpdate = now;
		}
		
		/* Render stationary items to a BufferedImage so they don't have to re-render every
		 * single redraw, so you can add more stationary items without making the game any
		 * slower. Also allowing more dynamic items (players, ghosts, etc.). This is only
		 * added because AWT is trash and doesn't know how to render objects properly.
		 * (hint: it's not meant for games, it's meant for utilities)
		 */
		int stationaryRenders = (int)this.entities.stream().filter(Entity::isStationary).count();
		if (stationaryRenders != this.stationaryRenders) {
			this.stationaryRenderedImage = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D ig = this.stationaryRenderedImage.createGraphics();
			ig.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			entities.stream().filter(Entity::isStationary).forEach(e -> e.draw(ig));
			ig.dispose();
			this.stationaryRenders = stationaryRenders;
		}
		if (this.stationaryRenderedImage != null) {
			g.drawImage(this.stationaryRenderedImage, null, 0, 0);
		}
		
		
		entities.stream().filter(Entity::isNotPacman).filter(Entity::isNotStationary).forEach(e -> e.draw(g));
		entities.stream().filter(Entity::isPacman).forEach(e -> e.draw(g));
		
		try {Thread.sleep(0);}catch(Exception e){e.printStackTrace();}
		repaint();
	}
	
}
