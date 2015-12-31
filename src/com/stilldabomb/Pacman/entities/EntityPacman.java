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
	
	public EntityPacman() {
		this.arc = new Arc2D.Double(0,0,size,size,0,0,Arc2D.PIE);
		this.collisionType = CollisionType.ENTITY;
		this.width = this.height = this.size = Pacman.pacmanSize;
		this.speed = 70;
		this.x = PacmanRenderer.getWindowSize() / 2 - this.width / 2;
		this.y = PacmanRenderer.getWindowSize() / 2 - this.height / 2;
	}
	
	public void update(double delta) {
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
		int displacement = (int)(this.speed*delta);
		switch (direction) {
		case NORTH:
			this.y -= displacement;
			if (this.y + this.size < 0)
				this.y = PacmanRenderer.getWindowSize() - this.size;
			break;
		case SOUTH:
			this.y += displacement;
			if (this.y > PacmanRenderer.getWindowSize())
				this.y = 0;
			break;
		case EAST:
			this.x += displacement;
			if (this.x > PacmanRenderer.getWindowSize())
				this.x = 0;
			break;
		case WEST:
			this.x -= displacement;
			if (this.x + this.size < 0)
				this.x = PacmanRenderer.getWindowSize() - this.size;
			break;
		}
		
		PacmanRenderer.getInstance().getEntities().stream().filter(e -> e instanceof EntityWall).filter(this::collidesWith).forEach(w -> {
			if (this.direction == Direction.NORTH || this.direction == Direction.SOUTH) {
				if (this.y < w.y) {
					this.y = w.y - this.height;
				} else {
					this.y = w.y + w.height;
				}
			}
			if (this.direction == Direction.EAST || this.direction == Direction.WEST) {
				if (this.x < w.x) {
					this.x = w.x - this.width;
				} else {
					this.x = w.x + w.height;
				}
			}
		});;
		
		// Set the gap of the mouth
		int gps = 300;
		int maxGap = 100;
		if (this.opening) {
			this.gap += gps*delta;
			if (this.gap >= maxGap) {
				this.gap = maxGap;
				this.opening = false;
			}
		} else {
			this.gap -= gps*delta;
			if (this.gap <= 0) {
				this.gap = 0;
				this.opening = true;
			}
		}
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
			this.arc.x += PacmanRenderer.getWindowSize();
		}
		if (this.x + this.size > PacmanRenderer.getWindowSize()) {
			this.arc.x -= PacmanRenderer.getWindowSize();
		}
		if (this.y < 0) {
			this.arc.y += PacmanRenderer.getWindowSize();
		}
		if (this.y + this.size > PacmanRenderer.getWindowSize()) {
			this.arc.y -= PacmanRenderer.getWindowSize();
		}
		g.fill(this.arc);
	}
	
	public void setDirection(Direction direction) {
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
