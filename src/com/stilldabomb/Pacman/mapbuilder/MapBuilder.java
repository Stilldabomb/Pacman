package com.stilldabomb.Pacman.mapbuilder;

import javax.swing.JFrame;

import lombok.Getter;

public class MapBuilder extends JFrame {
	
	@Getter
	private static MapBuilder instance;
	private static MapBuilderRenderer mapBuilderRenderer;
	
	public MapBuilder() {
		super("");
		
		instance = this;
		mapBuilderRenderer = new MapBuilderRenderer();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(mapBuilderRenderer);
		this.addKeyListener(Keyboard.getInstance());
		this.addMouseListener(Mouse.getInstance());
		this.addMouseMotionListener(Mouse.getInstance());
		this.setUndecorated(true);
		this.pack();
		this.setVisible(true);
	}

	
}
