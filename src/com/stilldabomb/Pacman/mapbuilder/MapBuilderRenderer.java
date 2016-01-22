package com.stilldabomb.Pacman.mapbuilder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.stilldabomb.Pacman.Pacman;
import com.stilldabomb.Pacman.entities.Entity;
import com.stilldabomb.Pacman.entities.EntityWall;

import lombok.Getter;
import lombok.Setter;

public class MapBuilderRenderer extends JPanel {
	
	@Getter
	private static MapBuilderRenderer instance;
	private int yPadding = 7, xPadding = 0;
	@Getter @Setter
	private Entity currentlySelected;
	@Getter
	private List<Entity> entities = new ArrayList<Entity>() {{
		add(new EntityWall(5, yPadding, false));
		add(new EntityWall(5, yPadding*2 + Pacman.pacmanSize, false));
	}};
	
	public MapBuilderRenderer() {
		instance = this;
		currentlySelected = entities.get(0);
		this.setPreferredSize(new Dimension(Pacman.pacmanSize + yPadding*2, (Pacman.pacmanSize + yPadding)*entities.size()+yPadding));
		this.xPadding = (int) (Math.floor(this.getPreferredSize().getWidth()/2)-(Pacman.pacmanSize/2))-1;
		entities.stream().forEach(e -> e.setX(xPadding));
	}
	
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		g.setBackground(Color.BLACK);
		g.clearRect(0, 0, (int)this.getPreferredSize().getWidth(), (int)this.getPreferredSize().getHeight());
		g.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.entities.forEach(e -> {
			e.draw(g);
			if (e == currentlySelected) {
				g.setColor(Color.RED);
				g.drawRect((int)e.getX() - 2, (int)e.getY() - 2, e.getWidth() + 4, e.getHeight() + 4);
			}
		});
	}
	
}
