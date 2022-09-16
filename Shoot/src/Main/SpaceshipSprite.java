package Main;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class SpaceshipSprite extends Sprite{
	private Main m;
	
	public SpaceshipSprite(Main m, Image image, int x, int y) {
		super(image, x, y);
		this.m = m;
		dy = 5;
	}

	public void move() {
		super.move();
		if(y > 800) {
			m.removeSprite(this);
		}
	}
	
	public void handleCollision(Sprite other) {
		if(other instanceof MissileSprite) {
			m.removeSprite(this);
			m.initItem(this);
		}
	}
}