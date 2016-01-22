package com.stilldabomb.Pacman.entities;

import lombok.Getter;

public class SpawnPoint {
	@Getter
	private int x,y,player;
	
	public SpawnPoint(int x, int y, int player) {
		this.x = x;
		this.y = y;
	}
	
	public SpawnPoint(int x, int y) {
		this(x, y, -1);
	}
}
