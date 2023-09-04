package inputs;

import gamestates.Gamestate;
import gamestates.State;
import main.Game;
import main.GamePanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Objects;

public class MouseInputs implements MouseListener, MouseMotionListener {

	private GamePanel gamePanel;

	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	@Override

	public void mouseClicked(MouseEvent e) {
		if (Objects.requireNonNull(Gamestate.state) == Gamestate.PLAYING) {
			gamePanel.getGame().getPlaying().mouseClicked(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (Gamestate.state) {
			case MENU -> gamePanel.getGame().getMenu().mousePressed(e);
			case PLAYING -> gamePanel.getGame().getPlaying().mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (Gamestate.state) {
			case MENU -> gamePanel.getGame().getMenu().mouseReleased(e);
			case PLAYING -> gamePanel.getGame().getPlaying().mouseReleased(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch (Gamestate.state) {
			case MENU -> gamePanel.getGame().getMenu().mouseMoved(e);
			case PLAYING -> gamePanel.getGame().getPlaying().mouseMoved(e);
		}
	}
}
