package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HeroSelector extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel top;
	private JPanel p;

	public JButton getStart() {
		return start;
	}

	private JButton start;

	public HeroSelector() {
		
		this.setUndecorated(true);
		this.setBounds(0, 0, 561, 318);
		this.setPreferredSize(new Dimension(567, 323));
		this.setLayout(null);
		
		WindowDestroyer dest = new WindowDestroyer();
		this.addWindowListener(dest);
		
		this.setTitle("Hearthstone");
		
		
		
		
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.setBounds(new Rectangle(571, 324));
		panel.setOpaque(false);
		this.add(panel);

		top = new JPanel(new FlowLayout(FlowLayout.CENTER, 11, 70));
		panel.add(top);
		top.setOpaque(false);
		top.setBackground(null);

		p = new JPanel(new FlowLayout(FlowLayout.CENTER,11, 27));
		panel.add(p);
		p.setOpaque(false);
	

		start = new JButton("Start game");
		start.setText("Start game");
		start.setSize(new Dimension(30, 20));
		JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
		bot.setOpaque(false);
		panel.add(bot);
		bot.add(start);
		bot.setBackground(null);

		JLabel background = new JLabel();
		background.setIcon(new ImageIcon("Images/welcome.jpg"));
		background.setSize(571, 322);
		background.setLayout(new BorderLayout());
		this.add(background);

		this.setLocationRelativeTo(null);
		this.revalidate();
		this.repaint();
		this.setVisible(true);

	}

	public static void main(String[] args) {
		new HeroSelector();
	}

	public JPanel getTop() {
		return top;
	}

	public JPanel getP() {
		return p;
	}

}
