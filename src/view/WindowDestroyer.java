package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class WindowDestroyer extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	public static void main(String[] args) {
		JFrame x = new JFrame();
		x.setSize(100, 400);
		x.setVisible(true);
		JLabel y = new JLabel("Hello");
		x.add(y);
		x.setTitle("MET");
		WindowDestroyer z = new WindowDestroyer();
		x.addWindowListener(z);
	}
}
