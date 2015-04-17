package pokejava;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * The display of the game in an JFrame.
 * 
 * @author PoketronHacker
 * @version 1.0
 *
 */
public class Display extends JFrame {
	private static final long serialVersionUID = -6975353145011224889L;
	
	private int gameWidth, gameHeight;
	private BufferedImage backBuffer;
	
	/**
	 * Creates a new display.
	 * 
	 * @param title The title of this display.
	 * @param width The game width of this display.
	 * @param height The game height of this display.
	 */
	public Display(String title, int width, int height) {
		super(title);
		this.gameWidth = width;
		this.gameHeight = height;
		this.backBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		setSize(width, height);
		setResizable(false);
		setVisible(true);
		setSize(getInsets().left + width + getInsets().right,
				getInsets().top + height + getInsets().bottom);
		setLocationRelativeTo(null);
	}
	
	/**
	 * Paints the back buffer onto the display.
	 */
	public void paintBackBuffer() {
		getGraphics().drawImage(backBuffer, getInsets().left, getInsets().top, null);
	}
	
	/**
	 * @return The game width.
	 */
	public int getGameWidth() {
		return this.gameWidth;
	}
	
	/**
	 * @return The game height.
	 */
	public int getGameHeight() {
		return this.gameHeight;
	}
	
	/**
	 * @return The games back buffer.
	 */
	public BufferedImage getGameBuffer() {
		return this.backBuffer;
	}
	
}
