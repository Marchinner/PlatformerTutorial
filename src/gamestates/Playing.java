package gamestates;

import entities.Player;
import levels.LevelManager;
import main.Game;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements Statemethods {

	private Player player;
	private LevelManager levelManager;

	public Playing(Game game) {
		super(game);
		initializeClasses();
	}

	private void initializeClasses() {
		levelManager = new LevelManager(game);
		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE));
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
	}

	public void windowFocusLost() {
		player.resetDirectionsBooleans();
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public void update() {
		levelManager.update();
		player.update();
	}

	@Override
	public void draw(Graphics graphics) {
		levelManager.draw(graphics);
		player.render(graphics);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
			player.setPlayerIsAttacking(true);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_A -> {
				player.setLeft(true);
			}
			case KeyEvent.VK_D -> {
				player.setRight(true);
			}
			case KeyEvent.VK_SPACE -> {
				player.setJump(true);
			}
			case KeyEvent.VK_BACK_SPACE -> {
				Gamestate.state = Gamestate.MENU;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_A -> {
				player.setLeft(false);
			}
			case KeyEvent.VK_D -> {
				player.setRight(false);
			}
			case KeyEvent.VK_SPACE -> {
				player.setJump(false);
			}
		}
	}
}
