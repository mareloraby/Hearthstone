package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOver extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JButton getStart() {
		return start;
	}

	private JButton start;

	public GameOver(String Text) {

		this.setBounds(0, 0, 430, 340);
		this.setPreferredSize(new Dimension(430, 340));
		this.setLayout(null);

		WindowDestroyer dest = new WindowDestroyer();
		this.addWindowListener(dest);

		this.setTitle("Hearthstone");

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 111));
		panel.setBounds(new Rectangle(430, 310));
		panel.setOpaque(false);
		this.add(panel);

		JLabel text = new JLabel(Text);
		text.setForeground(Color.WHITE.brighter());
		text.setFont(new Font("Serif", Font.BOLD + Font.ITALIC, 41));

		panel.add(text);

		JLabel background = new JLabel();
		background.setIcon(new ImageIcon("Images/hearthstoneEnd.jpg"));
		background.setSize(571, 322);
		background.setLayout(new BorderLayout());
		this.add(background);

		this.setLocationRelativeTo(null);
		this.revalidate();
		this.repaint();
		this.setVisible(true);

	}

	public static void main(String[] args) {
		new GameOver("FirstHero Win!");
	}

}
