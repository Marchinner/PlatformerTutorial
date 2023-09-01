package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private Game game;

	public GamePanel(Game game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;

		addKeyListener(new KeyboardInputs(this));
		setPanelSize();
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
		System.out.println("size: " + GAME_WIDTH + " : " + GAME_HEIGHT);
	}

	public void updateGame() {
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		game.render(graphics);
	}

	public Game getGame() {
		return game;
	}
}
