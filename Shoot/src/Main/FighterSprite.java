package Main;

import java.awt.Image;

import Main.Main;

public class FighterSprite extends Sprite{
	
	private Main m;
	
	public FighterSprite(Main m, Image image, int x, int y) {
		super(image, x, y);
		this.m = m;
		dx = 0;
		dy = 0;
	}
	
	public void move() {
		if((dx < 0) && (x < 10)) {
			return;
		}
		if((dx > 0) && (x > 750)) {
			return;
		}
		if((dy > 0) && (y > 700)) {
			return;
		}
		if((dy < 0) && (y < 10)) {
			return;
		}
		super.move();
	}
	
	
	@Override 
	public void handleCollision(Sprite other) {
		if(other instanceof BeamSprite) {
			if(m.checkLife()) {
				m.endGame(this);
			} else {
				m.adjustLife(false);
			}
		}
	}
	
}
