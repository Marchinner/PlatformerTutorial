package main;

import javax.swing.*;

public class GameWindow {
	private JFrame windowFrame;
	public GameWindow(GamePanel gamePanel) {
		windowFrame = new JFrame();
		windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windowFrame.add(gamePanel);
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setResizable(false);
		windowFrame.pack();
		windowFrame.setVisible(true);
	}
}
