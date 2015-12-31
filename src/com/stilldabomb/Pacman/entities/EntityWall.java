package com.stilldabomb.Pacman.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import com.stilldabomb.Pacman.Pacman;
import com.stilldabomb.Pacman.PacmanRenderer;

/**
 * 
 * @author Stilldabomb
 * Honestly, this is the worst class in the world, the amount of work that has to be put into it
 * just so that the walls will each line up correctly. Kill me now, I've put too much work into
 * this class.
 *
 */

public class EntityWall extends Entity {
	
	public enum WallType {
		INSIDE,
		BORDER
	}
	
	@Getter
	private int cordinateX,cordinateY;
	@Getter
	private WallType wallType;
	
	public EntityWall(int x, int y, WallType wallType) {
		super.collisionType = CollisionType.SOLID;
		super.width = super.height = Pacman.pacmanSize;
		super.x = x*Pacman.pacmanSize;
		super.y = y*Pacman.pacmanSize;
		this.cordinateX = x;
		this.cordinateY = y;
	}
	
	public EntityWall(int x, int y) {
		this(x, y, WallType.INSIDE);
	}

	public void draw(Graphics2D g) {
		List<Direction> directions = new ArrayList<Direction>();
		PacmanRenderer.getInstance().getEntities().stream().filter(e -> (e instanceof EntityWall)).forEach(e -> {
			EntityWall w = (EntityWall)e;
			if (this.getCordinateX() == w.getCordinateX()) { // Up & Down
				if (this.getCordinateY() - 1 == w.getCordinateY()) { // Up
					directions.add(Direction.NORTH);
				} else if (this.getCordinateY() + 1 == w.getCordinateY()) { // Down
					directions.add(Direction.SOUTH);
				}
			} else if (this.getCordinateY() == w.getCordinateY()) { // Left & Right
				if (this.getCordinateX() - 1 == w.getCordinateX()) { // Left
					directions.add(Direction.WEST);
				} else if (this.getCordinateX() + 1 == w.getCordinateX()) { // Right
					directions.add(Direction.EAST);
				}
			}
		});
		g.setColor(Color.BLUE);
		//g.setStroke(new BasicStroke(this.wallType == WallType.BORDER ? 2.5f : 2.75f));
		GeneralPath path = new GeneralPath();
		if (directions.isEmpty()) {
			path.moveTo(this.x, this.y + this.height / 2);
			path.curveTo(this.x, this.y, this.x, this.y, this.x + this.width / 2, this.y);
			path.curveTo(this.x + this.width, this.y, this.x + this.width, this.y, this.x + this.width, this.y + this.height/2);
			path.curveTo(this.x + this.width, this.y + this.height, this.x + this.width, this.y + this.height, this.x + this.width/2, this.y + this.width);
			path.curveTo(this.x, this.y + this.height, this.x, this.y + this.height, this.x, this.y + this.height/2);
		} else {
			if (directions.size() == 4) return;
			// Upper left corner
			if (!(directions.contains(Direction.WEST) && directions.contains(Direction.NORTH) && this.getWallAtDirection(Direction.NORTH).getWallAtDirection(Direction.WEST) != null)) {
				if (directions.contains(Direction.WEST)) {
					EntityWall wWall = this.getWallAtDirection(Direction.WEST);
					EntityWall nwWall = wWall.getWallAtDirection(Direction.NORTH);
					if (nwWall == null) {
						path.moveTo(this.x - this.width / 2, this.y);
					} else {
						path.moveTo(nwWall.getX() + nwWall.getWidth(), nwWall.getY() + nwWall.getHeight() / 2);
					}
				} else {
					path.moveTo(this.x, this.y + this.height / 2);
				}
				if (directions.contains(Direction.NORTH)) {
					EntityWall nWall = this.getWallAtDirection(Direction.NORTH);
					EntityWall nwWall = nWall.getWallAtDirection(Direction.WEST);
					if (nwWall == null) {
						path.curveTo(this.x, this.y, this.x, this.y, this.x, this.y - this.width / 2);
					} else {
						path.curveTo(nWall.getX(), nWall.getY() + nWall.getHeight(), nWall.getX(), nWall.getY() + nWall.getHeight(), nwWall.getX() + nwWall.getWidth() / 2, nwWall.getY() + nwWall.getHeight());
					}
				} else {
					EntityWall wWall = this.getWallAtDirection(Direction.WEST);
					if (directions.contains(Direction.WEST) && wWall.getWallAtDirection(Direction.NORTH) != null) {
						path.curveTo(wWall.getX() + wWall.getWidth(), wWall.getY(), wWall.getX() + wWall.getWidth(), wWall.getY(), this.x + this.width / 2, this.y);
					} else {
						path.curveTo(this.x, this.y, this.x, this.y, this.x + this.width / 2, this.y);
					}
				}
			}
			
			
			this.x += 100;
			path.moveTo(this.x, this.y + this.height / 2);
			path.curveTo(this.x, this.y, this.x, this.y, this.x + this.width / 2, this.y);
			path.curveTo(this.x + this.width, this.y, this.x + this.width, this.y, this.x + this.width, this.y + this.height/2);
			path.curveTo(this.x + this.width, this.y + this.height, this.x + this.width, this.y + this.height, this.x + this.width/2, this.y + this.width);
			path.curveTo(this.x, this.y + this.height, this.x, this.y + this.height, this.x, this.y + this.height/2);
			this.x -= 100;
		}
		for (int i = 0; i < 3; i++)
		g.draw(path);
	}
	
	public EntityWall getWallAtDirection(Direction direction) {
		EntityWall[] wall = new EntityWall[1]; // Hacky way to get around block scope garble
		PacmanRenderer.getInstance().getEntities().stream().filter(e -> (e instanceof EntityWall)).forEach(e -> {
			EntityWall w = (EntityWall)e;
			if (this.getCordinateX() == w.getCordinateX()) { // Up & Down
				if (this.getCordinateY() - 1 == w.getCordinateY() && direction == Direction.NORTH) { // Up
					wall[0] = w;
				} else if (this.getCordinateY() + 1 == w.getCordinateY() && direction == Direction.SOUTH) { // Down
					wall[0] = w;
				}
			} else if (this.getCordinateY() == w.getCordinateY()) { // Left & Right
				if (this.getCordinateX() - 1 == w.getCordinateX() && direction == Direction.WEST) { // Left
					wall[0] = w;
				} else if (this.getCordinateX() + 1 == w.getCordinateX() && direction == Direction.EAST) { // Right
					wall[0] = w;
				}
			}
		});
		return wall[0];
	}
	
}
