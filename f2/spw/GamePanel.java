import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Image;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
	private BufferedImage bi;
	private Image imgBackground;	
	Graphics2D big;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	public GamePanel() {
		imgBackground = Toolkit.getDefaultToolkit().getImage("bg.jpg");
		bi = new BufferedImage(400, 600, BufferedImage.TYPE_INT_ARGB);
		big = (Graphics2D) bi.getGraphics();
		big.drawImage(imgBackground, 0, 0, 400, 600,null);
		//big.setBackground(Color.BLACK);
	}

	public void updateGameUI(GameReporter reporter){
		//big.clearRect(0, 0, 400, 600);
		big.drawImage(imgBackground, 0, 0, 400, 600,null);
		big.setColor(Color.WHITE);		
		big.drawString(String.format("%08d", reporter.getScore()), 300, 20);
		for(Sprite s : sprites){
			s.draw(big);
		}
		
		repaint();
	}

	public void end(){
		big.clearRect(0, 0, 400, 600);
		
		big.setColor(Color.GREEN);	
		big.drawString(String.format("END"),300,20);
		for(Sprite s : sprites){
			s.draw(big);
		}
		
		repaint();
	}
	
	public void bloodSpaceShip(GameReporter reporter){
		
		big.setColor(Color.RED);
		big.fillRect( 0, 0, 38*(reporter.getNum()/10), 10 );
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, 0, 0);
	}

}