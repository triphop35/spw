import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet extends Sprite{
	public static final int Y_TO_FADE = 100;
	public static final int Y_TO_DIE = 30;
	
	private int step = 10;
	private boolean alive = true;
	
	public Bullet(int x, int y) {
		super(x, y, 5, 5);
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(x, y, width, height);
		
	}

	public void proceed(){
		y -= step;
		if(y < Y_TO_DIE){
			alive = false;
		}
	}
	
	public boolean isAlive(){
		return alive;
	}

	public void die(){
		alive = false;	
	}
}