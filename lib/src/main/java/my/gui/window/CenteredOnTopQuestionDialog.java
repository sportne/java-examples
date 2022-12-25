package my.gui.window;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * The purpose of this JDialog is to ask a question while remaining always on
 * top and be centered in the screen.
 */
public class CenteredOnTopQuestionDialog extends JDialog {

	private class SwingAction extends AbstractAction {

		private static final long serialVersionUID = -2441004953368436131L;

		private final CenteredOnTopQuestionDialog parent;

		public SwingAction(CenteredOnTopQuestionDialog parent) {
			this.parent = parent;
			putValue(NAME, "OK");
			putValue(SHORT_DESCRIPTION, "Submits the requested text");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (parent.isCorrectAnswer()) {
				EventQueue.invokeLater(() -> {
					parent.dispose();
					parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
				});
			} else {
				parent.handleIncorrectAnswer();
				if (parent.attemptCount >= parent.maximumAttempts) {
					EventQueue.invokeLater(() -> {
						parent.dispose();
						parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
					});
				}
			}
		}
	}

	private static final long serialVersionUID = 2462578690020488356L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CenteredOnTopQuestionDialog dialog = new CenteredOnTopQuestionDialog();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final JPanel contentPanel = new JPanel();
	private final JPasswordField answerField;
	private final JLabel lblQuestion;
	private final JLabel lblStatus;

	private int attemptCount = 0;
	private final int maximumAttempts = 3;

	private final Action action;

	/**
	 * Create the dialog.
	 */
	public CenteredOnTopQuestionDialog() {
		this(null);
	}

	public CenteredOnTopQuestionDialog(JFrame owner) {
		super(owner);
		action = new SwingAction(this);

		setTitle("Enter Input");
		setAlwaysOnTop(true);
		setModalityType(ModalityType.TOOLKIT_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			lblQuestion = new JLabel("Answer the question:");
			contentPanel.add(lblQuestion, BorderLayout.NORTH);
		}
		{
			answerField = new JPasswordField();
			contentPanel.add(answerField, BorderLayout.SOUTH);
			answerField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				lblStatus = new JLabel();
				lblStatus.setFont(lblStatus.getFont().deriveFont(8));
				lblStatus.setText(String.format("%d attempts remaining",
						maximumAttempts - attemptCount, maximumAttempts));
				buttonPane.add(lblStatus);
			}
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setAction(action);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		lblStatus.setText("");
	}

	public void handleIncorrectAnswer() {
		attemptCount++;
		EventQueue.invokeLater(() -> {
			lblStatus.setText(String.format("%d attempts remaining", maximumAttempts - attemptCount,
					maximumAttempts));
			pack();
			ShakingDialog dialog = new ShakingDialog(this);
			dialog.startShake();
		});

	}

	public boolean isCorrectAnswer() {
		char[] answer = answerField.getPassword();
		EventQueue.invokeLater(() -> {
			answerField.setText("");
		});
		return Arrays.equals("42".toCharArray(), answer);
	}
}
