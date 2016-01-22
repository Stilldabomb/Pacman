package com.stilldabomb.Pacman.entities;

import java.awt.Graphics2D;

import lombok.Getter;
import lombok.Setter;

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
	
	public enum RenderType {
		STATIONARY,
		MOVING
	}
	
	@Getter
	protected Direction direction = Direction.EAST;
	@Getter
	protected CollisionType collisionType;
	@Getter
	protected RenderType renderType = RenderType.MOVING;
	@Getter @Setter
	protected double x,y;
	@Getter
	protected int width,height;
	@Getter
	protected float speed;
	
	public void update(double delta) {}
	public abstract void draw(Graphics2D g);
	
	public boolean collidesWith(Entity e) {
		if (e == null || e.getCollisionType() == CollisionType.NONE || this.getCollisionType() == CollisionType.NONE) return false;
		for (double x = this.x; x < this.x + this.width; x++) {
			for (double y = this.y; y < this.y + this.height; y++) {
				for (double xc = e.x; xc < e.x + e.width; xc++) {
					for (double yc = e.y; yc < e.y + e.height; yc++) {
						if (Math.floor(x) == Math.floor(xc) && Math.floor(y) == Math.floor(yc)) return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean collidesWith(double d, double e) {
		for (double tx = this.x; tx < this.x + this.width; tx++) {
			for (double ty = this.y; ty < this.y + this.height; ty++) {
				if (Math.floor(tx) == Math.floor(d) && Math.floor(ty) == Math.floor(e)) return true;
			}
		}
		return false;
	}
	
	public static boolean isPacman(Entity e) {
		return (e instanceof EntityPacman);
	}
	
	public static boolean isNotPacman(Entity e) {
		return !isPacman(e);
	}
	
	public static boolean isStationary(Entity e) {
		return e.renderType == RenderType.STATIONARY;
	}
	
	public static boolean isNotStationary(Entity e) {
		return !isStationary(e);
	}
	
}
