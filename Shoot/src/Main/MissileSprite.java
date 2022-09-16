package Main;

import java.awt.Image;

import Main.Main;

public class MissileSprite extends Sprite {
	
	Main m;
	
	
	public MissileSprite(Main m, Image image, int x, int y) {
		super(image, x, y);
		dy = -15;
		this.m = m;
	}
	
	public MissileSprite(Main m, Image image, int x, int y, int rl) {
		super(image, x, y);
		dy = -15;
		this.m = m;
		
		//left missile
		if(rl == 1) {
			dx = 10;
		}
		
		//right missile
		if(rl == 2) {
			dx = -10;
		}
	}
	
	public void setDx(double dx) {
		this.dx = dx;
	}
	
	public void setDy(double dy) {
		this.dy = dy;
	}
	
	public double getDx() {
		return dx;
	}
	
	public double getDy() {
		return dy;
	}
	
	public void move() {
		super.move();
		if(y < -100) {
			m.removeSprite(this);
		}
	}
	
	@Override 
	public void handleCollision(Sprite other) {
		if(other instanceof SpaceshipSprite) {
			m.plusScore();
			m.removeSprite(this);
		} 
	}
}