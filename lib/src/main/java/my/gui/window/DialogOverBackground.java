package my.gui.window;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This is just demonstrating combining a centered dialog with a full screen
 * borderless translucent background.
 */
public class DialogOverBackground {

	public static void main(String[] args) throws InterruptedException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		FullScreenBorderlessTranslucentWindow background = new FullScreenBorderlessTranslucentWindow(
				0.9f, Color.BLACK);
		CenteredOnTopQuestionDialog dialog = new CenteredOnTopQuestionDialog(background);

		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				EventQueue.invokeLater(() -> background.dispose());
			}
		});

		background.setVisible(true);
		dialog.setVisible(true);
	}

}
