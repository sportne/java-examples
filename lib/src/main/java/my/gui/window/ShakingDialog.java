package my.gui.window;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.Timer;

public class ShakingDialog {

	private class ActionTime implements ActionListener {

		private int count = 0;

		// every interval the timer ticks, this is performed
		@Override
		public void actionPerformed(ActionEvent e) {
			// get elapsed time(running time)
			long elapsedTime = System.currentTimeMillis() - startTime;

			count++;
			int offset = (count % MAX_MOVEMENT) - MAX_MOVEMENT / 2;

			EventQueue.invokeLater(
					() -> dialog.setLocation(primaryLocation.x + offset, primaryLocation.y));

			// elapsedTime exceed DURATION, so stop now
			if (elapsedTime > DURATION) {
				stopShake();
			}
		}
	}

	private static final int MAX_MOVEMENT = 6;

	public static final int UPDATE_TIME = 2;
	public static final int DURATION = 300;
	private final JDialog dialog;

	private Point primaryLocation;
	private long startTime;
	private Timer time;

	// listener/instance of ActionTime
	private ActionTime timeListener = new ActionTime();

	public ShakingDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	public void startShake() {
		primaryLocation = dialog.getLocation();
		startTime = System.currentTimeMillis();
		time = new Timer(UPDATE_TIME, timeListener);
		time.start();
	}

	// stops shake/puts back in original place
	public void stopShake() {
		// code to stop the screen shaking
		time.stop();
		time.removeActionListener(timeListener);
		EventQueue.invokeLater(() -> dialog.setLocation(primaryLocation));
	}

}
