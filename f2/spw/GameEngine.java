import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import javax.swing.Timer;


public class GameEngine implements KeyListener, GameReporter{
	GamePanel gp;
		
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Boss> bosss = new ArrayList<Boss>();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<Heal> heals = new ArrayList<Heal>();	
	private SpaceShip v;	
	
	private Timer timer;
	
	private long score = 0;
	private double difficulty = 0.3;
	private double difficultyboss = 0.01;
	private double difficultyheal = 0.005;
	private int num = 100;
	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);
		
		timer = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
			}
		});
		timer.setRepeats(true);
		
	}
	
	public void start(){
		timer.start();
	}
	
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*390), 30);
		gp.sprites.add(e);
		enemies.add(e);
	}

	private void generateBoss(){
		Boss z = new Boss((int)(Math.random()*390), 30);
		gp.sprites.add(z);
		bosss.add(z);
	}

	private void generateHeal(){
		Heal h = new Heal((int)(Math.random()*390), 30);
		gp.sprites.add(h);
		heals.add(h);
	}

	private void generateBullet(SpaceShip v){
		Bullet b = new Bullet(v.getX()+(v.getWidth()/2),v.getY());
		gp.sprites.add(b);
		bullets.add(b);
	}
	
	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}

		if(Math.random() < difficultyboss){
			generateBoss();
		}

		if(Math.random() < difficultyheal){
			generateHeal();
		}
		
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
			}
		}

		Iterator<Boss> z_iter = bosss.iterator();
		while(z_iter.hasNext()){
			Boss z = z_iter.next();
			z.proceed();
			
			if(!z.isAlive()){
				z_iter.remove();
				gp.sprites.remove(z);
			}
		}

		Iterator<Heal> h_iter = heals.iterator();
		while(h_iter.hasNext()){
			Heal h = h_iter.next();
			h.proceed();
			
			if(!h.isAlive()){
				h_iter.remove();
				gp.sprites.remove(h);
			}
		}
		
		Iterator<Bullet> b_iter = bullets.iterator();
		while(b_iter.hasNext()){
			Bullet b = b_iter.next();
			b.proceed();
			
			if(!b.isAlive()){
				b_iter.remove();
				gp.sprites.remove(b);
			}
		}
		
		gp.updateGameUI(this);
		gp.bloodSpaceShip(this);
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		Rectangle2D.Double zr;
		Rectangle2D.Double br;
		Rectangle2D.Double hr;
		for(Enemy e : enemies){
			er = e.getRectangle();
			for(Bullet b:bullets){
				br = b.getRectangle();
				if(er.intersects(br)){
					b.die();
					e.die();
					score += 100;
				}
			}
			if(er.intersects(vr)){
				e.die();
				num = num - 10;
				return;
			}
			if(num == 0){
				die();
				gp.bloodSpaceShip(this);
			}
		}
		for(Boss z : bosss){
			zr = z.getRectangle();
			for(Bullet b:bullets){
				br = b.getRectangle();
				if(zr.intersects(br)){
					b.die();
					z.painHP();
					if(z.getHP()==0){
						score += 500;
						z.die();
					}
				}
			}
			if(zr.intersects(vr)){
				z.die();
				num = num - 50;
				return;
			}
			if(num == 0){
				die();
				gp.bloodSpaceShip(this);
			}
		}
		for(Heal h : heals){
			hr = h.getRectangle();
			if(hr.intersects(vr)){
				h.die();
				num = num + 10;
				if(num>100){
					num = 100;
				}
				return;
			}
		}
	}
	
	public void die(){
		gp.end();
		timer.stop();
		showdialog();
	}
	
	void controlVehicle(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			v.move(-1);
			break;
		case KeyEvent.VK_RIGHT:
			v.move(1);
			break;
		case KeyEvent.VK_D:
			difficulty += 0.1;
			break;
		case KeyEvent.VK_UP:
			v.moveup(-1);
			break;
		case KeyEvent.VK_DOWN:
			v.moveup(1);
			break;
		case KeyEvent.VK_SPACE:
			generateBullet(v);
			break;
		}
	}

	public long getScore(){
		return score;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		controlVehicle(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//do nothing
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing		
	}

	public int getNum(){
		return num;
	}

	public void showdialog(){
		JFrame frame = new JFrame("JOptionPane showMessageDialog example");
    	JOptionPane.showMessageDialog(frame,"GameOver \n"+score);
    	System.exit(0);
	}
}