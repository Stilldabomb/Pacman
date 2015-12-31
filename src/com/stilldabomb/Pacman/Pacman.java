package com.stilldabomb.Pacman;

import javax.swing.JFrame;

import lombok.Getter;

import com.stilldabomb.Pacman.events.Keyboard;

public class Pacman extends JFrame {
	
	public static final int pacmanSize = 15;
	@Getter
	private static Pacman instance;
	private PacmanRenderer pacmanRenderer;
	
	public static void main(String args[]) {
		new Pacman();
	}
	
	public Pacman() {
		super("Pacman");
		
		instance = this;
		pacmanRenderer = new PacmanRenderer();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(pacmanRenderer);
		this.addKeyListener(Keyboard.getInstance());
		this.pack();
		this.setVisible(true);
	}

}
