package my.gui.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The purpose of this class is to provide a full screen borderless window with
 * transparency.
 */
public class FullScreenBorderlessTranslucentWindow extends JFrame {

	private static final long serialVersionUID = 5427440720614489808L;

	public static void main(String[] args) throws InterruptedException {
		FullScreenBorderlessTranslucentWindow window = new FullScreenBorderlessTranslucentWindow(
				Color.GRAY);
		window.setVisible(true);

		Thread.sleep(2000);
		window.dispose();
	}

	FullScreenBorderlessTranslucentWindow() {
		this(0.5f, new Color(238, 238, 238));
	}

	FullScreenBorderlessTranslucentWindow(Color color) {
		this(0.5f, color);
	}

	FullScreenBorderlessTranslucentWindow(float opacity, Color color) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		final int screen_Width = dim.width;
		final int screen_Height = dim.height;

		setSize(screen_Width, screen_Height);

		// set properties for the JFrame
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setLayout(null);

		setOpacity(opacity);
		setBackground(color);
		getContentPane().setBackground(color);
	}

}
