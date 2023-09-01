package main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

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

		windowFrame.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
			}
		});
	}
}
