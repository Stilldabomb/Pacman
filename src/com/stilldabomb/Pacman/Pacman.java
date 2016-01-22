package com.stilldabomb.Pacman;

import java.awt.Point;

import javax.swing.JFrame;

import lombok.Getter;

import com.stilldabomb.Pacman.events.Keyboard;
import com.stilldabomb.Pacman.events.Mouse;
import com.stilldabomb.Pacman.mapbuilder.MapBuilder;

/**
 * 
 * @author Stilldabomb
 * @disclaimer This "game" (app) uses a lot of memory, so using it on a low
 * end machine probably isn't the best idea, and although it should run smoothly
 * I can make no promises, so do as you please and don't expect your CPU usage not
 * to go up to 100%. The problem is AWT, it is a terrible framework for building
 * games, as it wasn't designed for that, so using it now probably isn't the best
 * idea but I needed to get it done before the semester ended and wasn't up for learning
 * OpenGL. Java is a shite language and you should switch to a different one if you're
 * reading this.
 * 
 */

public class Pacman extends JFrame {
	
	public static final int pacmanSize = 15;
	@Getter
	private static Pacman instance;
	private PacmanRenderer pacmanRenderer;
	@Getter
	private static boolean mapBuilderEnabled;
	private static MapBuilder mapBuilder;
	
	public static void main(String args[]) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-mapbuilder")) {
				mapBuilderEnabled = true;
				mapBuilder = new MapBuilder();
			}
		}
		new Pacman();
	}
	
	public Pacman() {
		super("Pacman");
		
		instance = this;
		pacmanRenderer = new PacmanRenderer();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(pacmanRenderer);
		this.addKeyListener(Keyboard.getInstance());
		this.addMouseListener(Mouse.getInstance());
		this.addMouseMotionListener(Mouse.getInstance());
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
		if (mapBuilder != null) {
			new Thread(() -> {
				while (true) {
					Point p = new Point((int)this.getSize().getWidth() + (int)this.getLocation().getX() + 1, (int)this.getLocation().getY() + (int)(this.getSize().getHeight() - this.getContentPane().getHeight()));
					mapBuilder.setLocation(p);
				}
			}).start();
		}
	}

}
