package com.stilldabomb.Pacman.entities;

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
	private boolean connectToSurroundings;
	
	public EntityWall(double x, double y, WallType wallType, boolean connectToSurroundings) {
		this.collisionType = CollisionType.SOLID;
		this.renderType = RenderType.STATIONARY;
		this.width = this.height = Pacman.pacmanSize;
		this.x = x;
		this.y = y;
		this.cordinateX = (int)(x/Pacman.pacmanSize);
		this.cordinateY = (int)(y/Pacman.pacmanSize);
		this.connectToSurroundings = connectToSurroundings;
	}
	
	public EntityWall(double x, double y, boolean connectToSurroundings) {
		this(x, y, WallType.INSIDE, connectToSurroundings);
	}
	
	public EntityWall(int x, int y) {
		this(x*Pacman.pacmanSize, y*Pacman.pacmanSize, true);
	}

	public void draw(Graphics2D g) {
		List<Direction> directions = new ArrayList<Direction>();
		if (this.connectToSurroundings) {
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
		}
		g.setColor(Color.BLUE);
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
						path.curveTo(this.x, this.y, this.x, this.y, this.x, this.y - this.height / 2);
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
			
			// Upper right corner
			if (!(directions.contains(Direction.EAST) && directions.contains(Direction.NORTH) && this.getWallAtDirection(Direction.NORTH).getWallAtDirection(Direction.EAST) != null)) {
				if (directions.contains(Direction.EAST)) {
					EntityWall eWall = this.getWallAtDirection(Direction.EAST);
					EntityWall neWall = eWall.getWallAtDirection(Direction.NORTH);
					if (neWall == null) {
						path.moveTo(this.x + this.width * 1.5 , this.y);
					} else {
						path.moveTo(neWall.getX(), neWall.getY() + neWall.getHeight() / 2);
					}
				} else {
					path.moveTo(this.x + this.width, this.y + this.height / 2);
				}
				if (directions.contains(Direction.NORTH)) {
					EntityWall nWall = this.getWallAtDirection(Direction.NORTH);
					EntityWall neWall = nWall.getWallAtDirection(Direction.EAST);
					if (neWall == null) {
						path.curveTo(this.x + this.width, this.y, this.x + this.width, this.y, this.x + this.width, this.y - this.height / 2);
					} else {
						path.curveTo(nWall.getX() + nWall.getWidth(), nWall.getY() + nWall.getHeight(), nWall.getX() + nWall.getWidth(), nWall.getY() + nWall.getHeight(), neWall.getX() + neWall.getWidth() / 2, neWall.getY() + neWall.getHeight());
					}
				} else {
					EntityWall eWall = this.getWallAtDirection(Direction.EAST);
					if (directions.contains(Direction.EAST) && eWall.getWallAtDirection(Direction.NORTH) != null) {
						path.curveTo(eWall.getX(), eWall.getY(), eWall.getX(), eWall.getY(), this.x + this.width / 2, this.y);
					} else {
						path.curveTo(this.x + this.width, this.y, this.x + this.width, this.y, this.x + this.width / 2, this.y);
					}
				}
			}
			
			// Bottom left corner
			if (!(directions.contains(Direction.WEST) && directions.contains(Direction.SOUTH) && this.getWallAtDirection(Direction.SOUTH).getWallAtDirection(Direction.WEST) != null)) {
				if (directions.contains(Direction.WEST)) {
					EntityWall wWall = this.getWallAtDirection(Direction.WEST);
					EntityWall swWall = wWall.getWallAtDirection(Direction.SOUTH);
					if (swWall == null) {
						path.moveTo(this.x - this.width / 2, this.y + this.height);
					} else {
						path.moveTo(swWall.getX() + swWall.getWidth(), swWall.getY() + swWall.getHeight() / 2);
					}
				} else {
					path.moveTo(this.x, this.y + this.height / 2);
				}
				if (directions.contains(Direction.SOUTH)) {
					EntityWall sWall = this.getWallAtDirection(Direction.SOUTH);
					EntityWall swWall = sWall.getWallAtDirection(Direction.WEST);
					if (swWall == null) {
						path.curveTo(this.x, this.y + this.height, this.x, this.y + this.height, this.x, this.y + this.height * 1.5);
					} else {
						path.curveTo(sWall.getX(), sWall.getY(), sWall.getX(), sWall.getY(), swWall.getX() + swWall.getWidth() / 2, swWall.getY());
					}
				} else {
					EntityWall wWall = this.getWallAtDirection(Direction.WEST);
					if (directions.contains(Direction.WEST) && wWall.getWallAtDirection(Direction.SOUTH) != null) {
						path.curveTo(wWall.getX() + wWall.getWidth(), wWall.getY() + wWall.getHeight(), wWall.getX() + wWall.getWidth(), wWall.getY() + wWall.getHeight(), this.x + this.width / 2, this.y + this.height);
					} else {
						path.curveTo(this.x, this.y + this.height, this.x, this.y + this.height, this.x + this.width / 2, this.y + this.height);
					}
				}
			}
			
			// Bottom right corner
			if (!(directions.contains(Direction.EAST) && directions.contains(Direction.SOUTH) && this.getWallAtDirection(Direction.SOUTH).getWallAtDirection(Direction.EAST) != null)) {
				if (directions.contains(Direction.EAST)) {
					EntityWall eWall = this.getWallAtDirection(Direction.EAST);
					EntityWall seWall = eWall.getWallAtDirection(Direction.SOUTH);
					if (seWall == null) {
						path.moveTo(this.x + this.width * 1.5 , this.y + this.height);
					} else {
						path.moveTo(seWall.getX(), seWall.getY() + seWall.getHeight() / 2);
					}
				} else {
					path.moveTo(this.x + this.width, this.y + this.height / 2);
				}
				if (directions.contains(Direction.SOUTH)) {
					EntityWall sWall = this.getWallAtDirection(Direction.SOUTH);
					EntityWall seWall = sWall.getWallAtDirection(Direction.EAST);
					if (seWall == null) {
						path.curveTo(this.x + this.width, this.y + this.height, this.x + this.width, this.y + this.height, this.x + this.width, this.y + this.height * 1.5);
					} else {
						path.curveTo(sWall.getX() + sWall.getWidth(), sWall.getY(), sWall.getX() + sWall.getWidth(), sWall.getY(), seWall.getX() + seWall.getWidth() / 2, seWall.getY());
					}
				} else {
					EntityWall eWall = this.getWallAtDirection(Direction.EAST);
					if (directions.contains(Direction.EAST) && eWall.getWallAtDirection(Direction.SOUTH) != null) {
						path.curveTo(eWall.getX(), eWall.getY() + eWall.getHeight(), eWall.getX(), eWall.getY() + eWall.getHeight(), this.x + this.width / 2, this.y + this.height);
					} else {
						path.curveTo(this.x + this.width, this.y + this.height, this.x + this.width, this.y + this.height, this.x + this.width / 2, this.y + this.height);
					}
				}
			}
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
	
	public boolean collidesWith(Entity e) {
		boolean left = e.getX() + e.getWidth() < this.getX();
		boolean right = e.getX() > this.getX() + this.getWidth();
		boolean top = e.getY() + e.getHeight() < this.getY();
		boolean bottom = e.getY() > this.getY() + this.getHeight();
		return (left || right || top || bottom) ? false : super.collidesWith(e);
	}
	
}
