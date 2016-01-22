package com.stilldabomb.Pacman.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;

import com.stilldabomb.Pacman.Pacman;
import com.stilldabomb.Pacman.PacmanRenderer;
import com.stilldabomb.Pacman.events.Keyboard;

import lombok.Getter;

public class EntityPacman extends Entity {
	
	private Arc2D.Double arc;
	private int arcStart, gap, size = Pacman.pacmanSize;
	private boolean opening;
	@Getter
	private int lives = 3;
	long lastMove = 0;
	
	public EntityPacman(int x, int y) {
		this.arc = new Arc2D.Double(0,0,size,size,0,0,Arc2D.PIE);
		this.collisionType = CollisionType.ENTITY;
		this.width = this.height = this.size = Pacman.pacmanSize;
		this.speed = 70;
		this.x = x*Pacman.pacmanSize;
		this.y = y*Pacman.pacmanSize;
	}
	
	public void update(double delta) {
		if (Pacman.isMapBuilderEnabled())
			return;
		// Set direction based on key down
		if (Keyboard.isKeyDown(KeyEvent.VK_W))
			this.setDirection(Direction.NORTH);
		if (Keyboard.isKeyDown(KeyEvent.VK_S))
			this.setDirection(Direction.SOUTH);
		if (Keyboard.isKeyDown(KeyEvent.VK_D))
			this.setDirection(Direction.EAST);
		if (Keyboard.isKeyDown(KeyEvent.VK_A))
			this.setDirection(Direction.WEST);
		if (Keyboard.isKeyDown(KeyEvent.VK_SHIFT))
			this.speed = 130;
		else
			this.speed = 90;
		
		// Move Pacman based on direction
		double displacement = Pacman.pacmanSize*delta;
		long now = System.currentTimeMillis();
		if (lastMove == 0) lastMove = now;
		switch (direction) {
			case NORTH:
				this.y -= displacement;
				if (this.y + this.size < 0)
					this.y = PacmanRenderer.getWindowHeight() - this.size;
				break;
			case SOUTH:
				this.y += displacement;
				if (this.y > PacmanRenderer.getWindowHeight())
					this.y = 0;
				break;
			case EAST:
				this.x += displacement;
				if (this.x > PacmanRenderer.getWindowWidth())
					this.x = 0;
				break;
			case WEST:
				this.x -= displacement;
				if (this.x + this.size < 0)
					this.x = PacmanRenderer.getWindowWidth() - this.size;
				break;
		}

		// Set the gap of the mouth
		double gps = 250*delta;
		int maxGap = 100;
		if (this.opening) {
			this.gap += gps;
			if (this.gap >= maxGap) {
				this.gap = maxGap;
				this.opening = false;
			}
		} else {
			this.gap -= gps;
			if (this.gap <= 0) {
				this.gap = 0;
				this.opening = true;
			}
		}
		
		PacmanRenderer.getInstance().getEntities().stream().filter(e -> e instanceof EntityWall).filter(e -> ((EntityWall)e).collidesWith(this)).forEach(w -> {
			if (this.direction == Direction.NORTH) {
				this.y = w.y + w.height;
			}
			if (this.direction == Direction.SOUTH) {
				this.y = w.y - this.height;
			}
			if (this.direction == Direction.EAST) {
				this.x = w.x - this.width;
			}
			if (this.direction == Direction.WEST) {
				this.x = w.x + w.width;
			}
		});
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		this.arc.setAngleStart(arcStart + (gap / 2));
		this.arc.setAngleExtent(360 - gap);
		this.arc.x = x;
		this.arc.y = y;
		g.setColor(Color.YELLOW);
		g.fill(this.arc);
		if (this.x < 0) {
			this.arc.x += PacmanRenderer.getWindowWidth();
		}
		if (this.x + this.size > PacmanRenderer.getWindowHeight()) {
			this.arc.x -= PacmanRenderer.getWindowWidth();
		}
		if (this.y < 0) {
			this.arc.y += PacmanRenderer.getWindowHeight();
		}
		if (this.y + this.size > PacmanRenderer.getWindowHeight()) {
			this.arc.y -= PacmanRenderer.getWindowHeight();
		}
		g.fill(this.arc);
	}
	
	public void setDirection(Direction direction) {
		switch (this.direction) {
		case NORTH:
			if (this.y - Math.floor(this.y) > 0.02)return;
			break;
		case SOUTH:
			break;
		case EAST:
			break;
		case WEST:
			break;
		}
		this.direction = direction;
		switch (this.direction) {
		case NORTH:
			arcStart = 90;
			break;
		case SOUTH:
			arcStart = -90;
			break;
		case EAST:
			arcStart = 0;
			break;
		case WEST:
			arcStart = -180;
			break;
		}
	}
	
}
