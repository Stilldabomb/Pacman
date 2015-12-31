package com.stilldabomb.Pacman.entities;

import java.awt.Graphics2D;

import lombok.Getter;

public abstract class Entity {
	
	public enum Direction {
		NORTH,
		SOUTH,
		EAST,
		WEST
	}
	
	public enum CollisionType {
		NONE,
		SOLID,
		SEMISOLID,
		ENTITY
	}
	
	@Getter
	protected Direction direction = Direction.EAST;
	@Getter
	protected CollisionType collisionType;
	@Getter
	protected int x,y,width,height;
	@Getter
	protected float speed;
	
	public void update(double delta) {}
	public abstract void draw(Graphics2D g);
	
	public boolean collidesWith(Entity e) {
		if (e == null || e.getCollisionType() == CollisionType.NONE || this.getCollisionType() == CollisionType.NONE) return false;
		for (int x = this.x; x < this.x + this.width; x++) {
			for (int y = this.y; y < this.y + this.height; y++) {
				for (int xc = e.x; xc < e.x + e.width; xc++) {
					for (int yc = e.y; yc < e.y + e.height; yc++) {
						if (x == xc && y == yc) return true;
					}
				}
			}
		}
		return false;
		/*boolean leftX = this.x > e.x && this.x < e.x + e.width;
		boolean topY = this.y > e.y && this.y < e.y + e.height;
		boolean rightX = this.x + this.width > e.x && this.x + this.width < e.x + e.width;
		boolean bottomY = this.y + this.height > e.y && this.y + this.height < e.y + e.height;
		boolean topLeft = leftX && topY;
		boolean topRight = rightX && topY;
		boolean bottomLeft = leftX && bottomY;
		boolean bottomRight = rightX && bottomY;
		return topLeft || topRight || bottomLeft || bottomRight;*/
	}
	
	public static boolean isPacman(Entity e) {
		return (e instanceof EntityPacman);
	}
	
	public static boolean isNotPacman(Entity e) {
		return !isPacman(e);
	}
	
}
