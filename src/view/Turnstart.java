package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Turnstart extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Turnstart(String msg) {

		JLabel l = new JLabel();
		l.setFont(new Font("Serif", Font.PLAIN, 50));
		l.setText(msg);

		this.setVisible(false);
		this.toFront();
		final JDialog dialog = new JDialog(this, true);
		dialog.setUndecorated(true);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 100));
		p.setSize(200, 100);
		p.add(l, BorderLayout.CENTER);
		p.setBackground(new Color(225, 202, 132));

		// dialog.setLayout(new FlowLayout());

		dialog.add(p);
		dialog.setSize(610, 255);

		dialog.setLocationRelativeTo(null);

		Timer timer = new Timer(1700, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		});
		timer.setRepeats(false);
		timer.start();
		dialog.setVisible(true);

		this.revalidate();
		this.repaint();


	}

	

}