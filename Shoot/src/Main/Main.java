package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main extends JPanel implements ActionListener {

	private static boolean running;
	
	private BufferedImage fighterImg;
	private BufferedImage missileImg;
	private BufferedImage missileImg2;
	private BufferedImage explosionImg;
	private BufferedImage spaceshipImg;
	private BufferedImage spaceImg;
	private BufferedImage beamImg;
	private BufferedImage itemImg;
	private BufferedImage itemImg2;
	
	private double score;
	
	private static int life;
	
	private JFrame frame;
	
	private ArrayList<Sprite> sprites1; // fighter
	private ArrayList<Sprite> sprites2; // spaceship
	private ArrayList<Sprite> sprites3; // missile
	private ArrayList<Sprite> sprites4; // beam
	private ArrayList<itemSprite> sprites5; // item
	private ArrayList<Integer> itemNumArr;
	private boolean fireFlag;
	
	private int fireSpeed;
	
	private Sprite fighter;
	private Sprite missile;
	private Sprite spaceship;
	private Sprite beam;
	private itemSprite item;
	
	private JMenuItem newGame;
	private JMenuItem closeGame;
	
	private static Main m;
	
	private static boolean lrTrigger;
	
	private static boolean lockon;
	
	private int itemNum = 0;
	
	public Main() {
		// 새 게임 시작시 전부 초기화해야해서 만든 코드입니다.
		addKeyListener(new KeyHandler());
		frame = new JFrame();
		running = true;
		sprites1 = new ArrayList<Sprite>();
		sprites2 = new ArrayList<Sprite>();
		sprites3 = new ArrayList<Sprite>();
		sprites4 = new ArrayList<Sprite>();
		sprites5 = new ArrayList<itemSprite>();
		itemNumArr = new ArrayList<Integer>();
		life = 5;
		fireFlag = false;
		fireSpeed = 500;
		lrTrigger = false;
		lockon = false;
		score = 0;
		newGame = new JMenuItem("새 게임");
		closeGame = new JMenuItem("끝내기");
		JMenuBar mb = new JMenuBar();
		JMenu gameMenu = new JMenu("메뉴");
		gameMenu.add(newGame);
		gameMenu.add(closeGame);
		newGame.addActionListener(this);
		closeGame.addActionListener(this);
		mb.add(gameMenu);
		
		frame.setTitle("Shooting Game");
		frame.setSize(700, 800);// 창 크기 설정
		frame.setBounds(0,0,700,800);
		frame.add(this);
		frame.setJMenuBar(mb);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {

			// 이미지 불러오기
			fighterImg = ImageIO.read(new File("img/fighter.png"));
			missileImg = ImageIO.read(new File("img/missile.png"));
			explosionImg = ImageIO.read(new File("img/explosion.png"));
			spaceshipImg = ImageIO.read(new File("img/spaceship.png"));
			spaceImg = ImageIO.read(new File("img/space.png"));
			beamImg = ImageIO.read(new File("img/beam.png"));
			itemImg = ImageIO.read(new File("img/item.png"));
			itemImg2 = ImageIO.read(new File("img/item2.png"));
			missileImg2 = ImageIO.read(new File("img/missile2.png"));
			
		} catch (Exception e) {

		}
		
		this.requestFocus();
		this.initFighter();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newGame) {
			// 새 게임 버튼 누를 시
			running = false;
			frame.dispose();
			
			m = new Main();
			
			m.threadFire();
			m.threadInitSpaceship();
			m.threadFire2();
			m.threadGameLoop();
		} else if (e.getSource() == closeGame) {
			System.exit(0);
		}
	}
	
	public void initFighter() {
		try { // 내 전투기 만드는 메소드
			fighter = new FighterSprite(this, fighterImg, 370, 500);
			sprites1.add(fighter);
		} catch(Exception e) {
			
		}
		
	}
	
	public void initSpaceship() {
		try { // 상대 적기 만드는 메소드
			int n = (int)(Math.random()*500);
			spaceship = new SpaceshipSprite(this, spaceshipImg, n, 50);
			sprites2.add(spaceship);
		} catch(Exception e) {
			
		}
	}
	
	public void threadMove1() { // 나의 비행기 움직일 시 스레드로 동시작업 통해 위치 옮겨서 다시그리기
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < sprites1.size(); i++) {
					try {
						Sprite sprite = (Sprite) sprites1.get(i);
						sprite.move();
					} catch(Exception e) {
						
					}
				}
			}
		});
		
		thread.start();
	}
	
	public void threadMove2() { //threadMove2와 비슷. 적기에게 적용
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < sprites2.size(); i++) {
					try {
						Sprite sprite = (Sprite) sprites2.get(i);
						sprite.move();
					} catch(Exception e) {
						
					}
				}
			}
		});
		
		thread.start();
	}
	
	public void threadMove3() { // 내 비행기 공격 빔 움직임
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < sprites3.size(); i++) {
					try {
						Sprite sprite = (Sprite) sprites3.get(i);
						sprite.move();
					} catch(Exception e) {
						
					}
				}
			}
		});
		
		thread.start();
	}
	
	public void threadMove4() { // 상대 비행기 공격 빔 
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < sprites4.size(); i++) {
					try {
						Sprite sprite = (Sprite) sprites4.get(i);
						sprite.move();
					} catch(Exception e) {
						
					}
				}
			}
		});
		
		thread.start();
	}
	
	public void moveAll() { // 스레드 묶어뒀습니다.
		threadMove1();
		threadMove2();
		threadMove3();
		threadMove4();
	}
	
	public boolean checkLife() { // 라이프 판별
		if(life == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public void adjustLife(boolean b) {
		if(b) {
			life += 1;
		} else {
			life -= 1;
		}
	}
	
	public void gameLoop() {
		try {
			
			while(running) { // 당연히 running값이 true일 동안
				
				moveAll(); // 스레드 움직임
				
				for(int p = 0; p < sprites2.size(); p++) {
					for(int s = 0; s < sprites3.size(); s++) {
						
						try {
							Sprite me = (Sprite) sprites2.get(p);
							Sprite other = (Sprite) sprites3.get(s);
							
							if(me.collision(other)) { // 내 빔과 상대 적기 충돌 시 처리
								me.handleCollision(other);
								other.handleCollision(me);
							}
							
						} catch(Exception e) {
							
						}
					}
				}
				
				for(int s = 0; s < sprites4.size(); s++) {
					
					try {
						Sprite me = (Sprite) sprites4.get(s);
						Sprite other = (Sprite) sprites1.get(0);
						
						if(me.collision(other)) { // 내 비행기와 상대 빔 충돌 시 처리
							me.handleCollision(other);
							other.handleCollision(me);
						}
						
					} catch(Exception e) {
						
					}
				}
				
				for(int s = 0; s < sprites5.size(); s++) {
					
					try {
						Sprite me = (Sprite) sprites5.get(s);
						Sprite other = (Sprite) sprites1.get(0);
						
						if(me.collision(other)) { // 아이템 먹었을 시
							me.handleCollision(other);
							other.handleCollision(me);
						}
						
					} catch(Exception e) {
						
					}
				}
				
				repaint();
				
				try {
					
					Thread.sleep(10);
				} catch (Exception e) {
					
				}
			} 
			
		} catch(Exception e) {
		
		}

	}
	
	public void fire() {
		try {
			if(fireFlag == false) {
				missile = new MissileSprite(this, missileImg, fighter.getX(), fighter.getY()-20);
				sprites3.add(missile); // 미사일 객체 만들어서 arraylist에 저장
			}
		} catch(Exception e) {
			
		}
	}
	
	public void stopFire() {
		fireFlag = true;
		sprites3.clear();
	}
	
	public void fire2() { //적기가 날리는 빔 생성입니다.
		try {
			beam = new BeamSprite(this, beamImg, spaceship.getX()+20, spaceship.getY()+15);
			if(beam == null) {
				while(beam != null) {
					beam = new BeamSprite(this, beamImg, spaceship.getX()+20, spaceship.getY()+15);
				}
			}
			sprites4.add(beam);
		} catch(Exception e) {
			
		}
	}
	
	public void plusScore() {
		score+=10;
		
		if(score % 100 == 0 && score > 0 && fireSpeed > 100) {
			fireSpeed = fireSpeed - 50;
			
			if(life + 3 <= 5) { // 100점 단위로 보상. 라이프는 3회복 최대 5까지.  속도도 상승
				life += 3;
			} else {
				life = 5;
			}
			
		}
	}
	
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		g.drawImage(spaceImg, 0, 0, getBackground(), this);
		g.setFont(new Font("궁서", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("점수 : "+ (int)score, 500, 50);
		g.drawString("발사 속도 : "+ (51-fireSpeed/10), 500, 110);
		g.drawString("라이프 : ", 500, 80);
		for(int i = 0; i < life; i++) {
			g.drawString("★", 580+i*15, 80);
		}
		
		paintFighter(g);
		paintSpaceship(g);
		paintMissile(g);
		paintBeam(g);
		paintItem(g);
		
		if(sprites1.size() == 0) {
			g.clearRect(0, 0, 700, 800);
			g.drawImage(null, 0, 0, getBackground(), this);
			g.setFont(new Font("궁서", Font.BOLD, 30));
			g.setColor(Color.black);
			g.drawString("게임종료", 280, 350);
			g.setFont(new Font("궁서", Font.BOLD, 20));
			g.drawString("점수 : "+(int)score, 280, 400);
		}
		
		if(score==100) {
			Sprite sprite = (Sprite) sprites1.get(0);
			g.setFont(new Font("돋움", Font.BOLD, 20));
			g.drawString("속도 상승!", sprite.getX(), sprite.getY()-50);
			g.drawString("라이프 상승!", sprite.getX(), sprite.getY()-70);
		}
	}
	
	public void paintFighter(Graphics g) {
		try {
			
			if(sprites1.size() == 0) {
				stopFire();
				return;
			}
			
			Sprite sprite = (Sprite) sprites1.get(0);
			sprite.draw(g);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void paintMissile(Graphics g) {
		try {
			
			if(sprites1.size() == 0) {
				return;
			}
			
			for(int i = 0; i < sprites3.size(); i++) {
				Sprite sprite = (Sprite) sprites3.get(i);
				sprite.draw(g);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paintSpaceship(Graphics g) {
		try {
			
			if(sprites1.size() == 0) {
				return;
			}
			
			for(int i = 0; i < sprites2.size(); i++) {
				Sprite sprite = (Sprite) sprites2.get(i);
				if(sprite!=null)
					sprite.draw(g);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void paintItem(Graphics g) {
		try {
			
			if(sprites5 == null) {
				return;
			}
			
			for(int i = 0; i < sprites5.size(); i++) {
				Sprite sprite = (Sprite) sprites5.get(i);
				sprite.draw(g);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void paintBeam(Graphics g) {
		try {
			
			if(sprites1.size() == 0) {
				return;
			}
			
			for(int i = 0; i < sprites4.size(); i++) {
				Sprite sprite = (Sprite) sprites4.get(i);
				sprite.draw(g);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 혹시 몰라 이미지 조절 메소드 만들어둠.
	public static void resizeFile(String imagePathToRead, String imagePathToWrite, int resizeWidth, int resizeHeight) throws IOException {

		File fileToRead = new File(imagePathToRead);
		
		BufferedImage bufferedImageInput = ImageIO.read(fileToRead);

		BufferedImage bufferedImageOutput = new BufferedImage(resizeWidth, resizeHeight, bufferedImageInput.getType());

		Graphics2D g2d = bufferedImageOutput.createGraphics();
		g2d.drawImage(bufferedImageInput, 0, 0, resizeWidth, resizeHeight, null);
		g2d.dispose();

		String formatName = imagePathToWrite.substring(imagePathToWrite.lastIndexOf(".") + 1);

		ImageIO.write(bufferedImageOutput, formatName, new File(imagePathToWrite));
	}

	public void removeSprite(Sprite sprite) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				if(sprite instanceof SpaceshipSprite) {
					sprite.image = explosionImg;
				} else if(sprite instanceof MissileSprite) {
					sprites3.remove(sprite);
				} else if(sprite instanceof BeamSprite) {
					sprites4.remove(sprite);
				}
				
				if(sprite instanceof SpaceshipSprite && sprites2 != null) {
					sprites2.remove(sprite);
				}
				
				if(sprite instanceof itemSprite) {
					sprites5.remove(sprite);
				}
			}
		});
		
		thread.start();
	}
	
	public void endGame(Sprite sprite) {
		
		sprite.image = explosionImg;
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		sprites1.remove(sprite);
		
		running = false;
	}
	
	public void threadFire() {
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(running) {
					
					if(lrTrigger) {
						initLeftMissile();
						initRightMissile();
					}
					
					if(lockon) {
						initLockon();
					}
					
					fire();
					
					try {
						Thread.sleep(fireSpeed);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}	
		});
			
		thread.start();
	}
	
	public void threadFire2() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(running) {
					fire2();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}	
		});
			
		thread.start();
	}
	
	public void threadInitSpaceship() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(running) {
					
					initSpaceship();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		thread.start();
	}
	
	private void initLeftMissile() {
		try {
			if(fireFlag == false) {
				Sprite sprite = (Sprite) sprites1.get(0);
				missile = new MissileSprite(this, ro2(30,missileImg, 1), sprite.getX(), sprite.getY(), 1);
				sprites3.add(missile);
			}
		} catch(Exception e) {
			
		}
	}
	
	private void initRightMissile() {
		try {
			if(fireFlag == false) {
				Sprite sprite = (Sprite) sprites1.get(0);
				missile = new MissileSprite(this, ro2(-30,missileImg, 2), sprite.getX(), sprite.getY(), 2);
				sprites3.add(missile);
			}
		} catch(Exception e) {
			
		}
	}
	
	private void initLockon() { // 파란 아이템 먹었으면 사방으로 
		try {
			Sprite sprite = (Sprite) sprites1.get(0);
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(10);
			missile.setDy(10);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(5);
			missile.setDy(10);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(10);
			missile.setDy(5);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(-5);
			missile.setDy(10);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(10);
			missile.setDy(-5);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(-10);
			missile.setDy(5);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(-5);
			missile.setDy(-10);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(5);
			missile.setDy(-10);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(-10);
			missile.setDy(-5);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(10);
			missile.setDy(-10);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(-10);
			missile.setDy(-10);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(-10);
			missile.setDy(10);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(-10);
			missile.setDy(0);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(0);
			missile.setDy(-10);
			sprites3.add(missile);
			
			missile = new MissileSprite(this, missileImg2, sprite.getX(), sprite.getY(), 0);
			missile.setDx(10);
			missile.setDy(0);
			sprites3.add(missile);
			
			lockon =false;
			
		} catch(Exception e) {
			
		}
	}

	public void threadGameLoop() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(running) {
					gameLoop();
				}
			}
		});
		
		thread.start();
	}
	
	public void threadRemoveItemAuto(int itemNum) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(running) {
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					removeItemAuto(itemNum);
				}
			}
		});
		
		thread.start();
	}
	
	public void removeItemAuto(int itemNum) {
		try {
			if(itemNumArr.contains(itemNum)) {
				sprites5.remove(itemNum);
			}
		} catch(Exception e) {
			
		}
	}

	public void initItem(SpaceshipSprite sprite) {
		
		try {
			
			int n = (int)(Math.random()*100);
			
			if(n < 10) {
				item = new itemSprite(this, itemImg, sprite.getX(), sprite.getY(), itemNum);
				sprites5.add(item);
				itemNumArr.add(itemNum);
				m.threadRemoveItemAuto(itemNum);
				itemNum++;
			} else if(n < 20 && n >=10) {
				item = new itemSprite(this, itemImg2, sprite.getX(), sprite.getY(), itemNum, 1);
				sprites5.add(item);
				m.threadRemoveItemAuto(itemNum);
				itemNum++;
			}
		} catch(Exception e) {
			
		}
	}

	public void upgradeMissile() {
		lrTrigger = true;
		lockon = false;
	}
	
	public void upgradeMissile2() {
		lockon = true;
		lrTrigger = false;
	}
	
	private static BufferedImage ro2(int rotate, BufferedImage image, int rl) throws Exception {
	 
	    BufferedImage oldImage = image;
	 
	    BufferedImage newImage = null;
	 
	    if(180 == rotate) {
	        newImage = new BufferedImage(oldImage.getWidth(),oldImage.getHeight(), oldImage.getType());
	    }
	    else {
	        newImage = new BufferedImage(oldImage.getHeight(),oldImage.getWidth(), oldImage.getType());
	    }
	 
	    Graphics2D graphics = (Graphics2D) newImage.getGraphics();
	 
	    graphics.rotate(Math.toRadians(rotate), newImage.getWidth() / 2, newImage.getHeight() / 2);
	 
	    if(180 != rotate) {
	        graphics.translate((newImage.getWidth() - oldImage.getWidth()) / 2, (newImage.getHeight() - oldImage.getHeight()) / 2);        // 90, 270도일때만 사용
	    }
	 
	    graphics.drawImage(oldImage, 0, 0, oldImage.getWidth(), oldImage.getHeight(), null);
	 
	    if(rl == 1)
	    	ImageIO.write(newImage, "PNG", new FileOutputStream(new File("img/missile"+rl)));
	    
	    if(rl == 2)
	    	ImageIO.write(newImage, "PNG", new FileOutputStream(new File("img/missile"+rl)));
	    
	    BufferedImage img = ImageIO.read(new File("img/missile"+rl));
	    
	    return img;
	}
	
	public class KeyHandler implements KeyListener{

	    /** 
	    * 출처: https://micropilot.tistory.com/2953 [Programming:티스토리]
	    * keyPressed()에서 발생한 키코드를 아래의 HashSet에 저장하면 타이머 이벤트 핸들러에서 
	    * 키코드를 확인하여 화면을 갱신하는 코드를 실행한다
	    * 여기서 컬렉션 중에서 Set을 선택한 이유는 키코드가 중복되어 저장하는 것을 막고 키를 뗄 때
	    * HashSet에서 해당 키코드를 한개만 제거해주면 즉시 이벤트 효과가 제거되므로 이벤트에 즉시 반응하는 효과를 낼 수 있다
	    */
		
	    // 도저히 키 이벤트만으로는 반응속도가 너무 늦어서 항 카페에서 퍼왔습니다.
	    HashSet<Integer> pressedKeys = new HashSet<Integer>();
	    Timer timer;

	    public KeyHandler()
	    {
	        timer = new Timer(10, new ActionListener(){ // 10ms마다 액션 이벤트 발생
	            @Override
	            public void actionPerformed(ActionEvent arg0) // 10ms마다 발생한 액션 이벤트 처리
	            {	
	                if(!pressedKeys.isEmpty()){
	                    Iterator<Integer> i = pressedKeys.iterator();
	                    int n = 0;
	                    
	                    while(i.hasNext()){
	                    	
	                    	n = i.next();
	                    	
	                    	if(n == KeyEvent.VK_A)
	                			fighter.setDx(-5);
	                		
	                    	else if(n == KeyEvent.VK_D)
	                			fighter.setDx(+5);
	                		
	                    	else if(n == KeyEvent.VK_W)
	                			fighter.setDy(-5);
	                		
	                    	else if(n == KeyEvent.VK_S)
	                			fighter.setDy(+5);
	                		
	                    	repaint();
	                    }
	                }else {
	                	timer.stop();
	                }
	            }
	        });
	    }

	    @Override
	    public void keyPressed(KeyEvent keyEvent){
	        //발생한 키코드를 HsshSet에 저장한다
	        int keyCode = keyEvent.getKeyCode();
	        pressedKeys.add(keyCode);
	        if(!timer.isRunning()) timer.start();
	    }
	    
	    @Override
	    public void keyReleased(KeyEvent keyEvent){
	        //HashSet에서 키코드를 제거한다
	        int keyCode = keyEvent.getKeyCode();
	        fighter.setDx(0);
	        fighter.setDy(0);
	        pressedKeys.remove(keyCode);
	    }
	    
	    @Override
	    public void keyTyped(KeyEvent keyEvent){}
	}
	
	public static void main(String args[]) {
		m = new Main();
		m.threadFire();
		m.threadInitSpaceship();
		m.threadFire2();
		m.threadGameLoop();
	}
}

