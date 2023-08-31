package inputs;

import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static utils.Constants.Directions.*;

public class KeyboardInputs implements KeyListener {
	private GamePanel gamePanel;
	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	@Override

	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W -> {
				gamePanel.setPlayerDirection(UP);
			}
			case KeyEvent.VK_A -> {
				gamePanel.setPlayerDirection(LEFT);
			}
			case KeyEvent.VK_S -> {
				gamePanel.setPlayerDirection(DOWN);
			}
			case KeyEvent.VK_D -> {
				gamePanel.setPlayerDirection(RIGHT);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D -> {
				gamePanel.setPlayerIsMoving(false);
			}
		}
	}
}
