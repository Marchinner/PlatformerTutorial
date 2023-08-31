package main;

import java.awt.*;
import java.util.Random;

public class Rectangle {
	private int x;
	private int y;
	private int w;
	private int h;
	private int xDir = 1;
	private int yDir = 1;
	private Color color;
	private Random random = new Random();

	public Rectangle(int x, int y) {
		this.x = x;
		this.y = y;
		w = random.nextInt(50);
		h = w;
		color = newColor();
	}

	private Color newColor() {
		return new Color(
				random.nextInt(255),
				random.nextInt(255),
				random.nextInt(255)
		);
	}

	public void updateRectangle() {
		this.x += xDir;
		if ((x + w) > 300 || x < 0) {
			xDir *= -1;
			color = newColor();
		}
		this.y += yDir;
		if ((y + h) > 300 || y < 0) {
			yDir *= -1;
			color = newColor();
		}
	}

	public void drawRectangle(Graphics graphics) {
		graphics.setColor(color);
		graphics.fillRect(x, y, w, h);
	}
}
