package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GameView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel side;
	private JPanel secondH;
	private JPanel firstH;
	private JPanel firstField;
	private JPanel secondField;

	private JButton firstHeroButton;
	private JButton firstHeroPower;
	private JButton secondHeroButton;
	private JButton secondHeroPower;
	private JButton endTurn;

	
	private JButton firstHeroUseCard;
	private JButton secondHeroUseCard;

	private JTextArea firstHeroDetails;
	private JTextArea secondHeroDetails;

	public GameView() {
		this.setTitle("Hearthstone");
		this.setPreferredSize(new Dimension(960, 540));
		this.setSize(new Dimension(960, 540));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());

		WindowDestroyer dest = new WindowDestroyer();
		this.addWindowListener(dest);

		side = new JPanel(new GridLayout(0, 1));
		side.setPreferredSize(new Dimension(124, 838));
		side.setBackground(new Color(13,29,47));

		firstHeroButton = new JButton();
		secondHeroButton = new JButton();
		firstHeroPower= new JButton();
		firstHeroPower.setActionCommand("fPower");
		secondHeroPower = new JButton();
		secondHeroPower.setActionCommand("sPower");
		endTurn = new JButton("EndTurn");
		
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("Images/EndTurnn.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image dimg = img.getScaledInstance((int) (img.getWidth()*0.6), (int) (img.getHeight()*0.6),Image.SCALE_SMOOTH);
		
		ImageIcon icon = new ImageIcon(dimg);
		endTurn.setIcon(icon);
		endTurn.setBorderPainted(false);
		endTurn.setContentAreaFilled(false);
		

		secondHeroDetails = new JTextArea("");
		secondHeroDetails.setBackground(new Color(13,29,47));
		secondHeroDetails.setForeground(new Color(239,239,217));
		secondHeroDetails.setEditable(false);
		
		firstHeroDetails = new JTextArea("");
		firstHeroDetails.setBackground(new Color(13,29,47));
		firstHeroDetails.setForeground(new Color(239,239,217));
		firstHeroDetails.setEditable(false);

		
		firstHeroButton.setActionCommand("firsthero");
		secondHeroButton.setActionCommand("secondhero");
		
		side.add(firstHeroButton);
		side.add(firstHeroDetails);
		side.add(firstHeroPower);
		side.add(endTurn);
		side.add(secondHeroPower);
		side.add(secondHeroDetails);
		side.add(secondHeroButton);

		
		JLabel sh = new JLabel(" Second Hero Hand:");
		sh.setFont(new Font("Serif", Font.PLAIN, 15));
		sh.setForeground(new Color(239,239,217));
		
		secondH = new JPanel(new GridLayout(0, 11));
		secondH.add(sh);
		secondH.setBackground(new Color(13,29,47));
		
		JPanel s1 = new JPanel(new GridLayout(3, 0));
		s1.setBackground(new Color(13,29,47));
		JLabel s11 = new JLabel(" Second Hero Field:");
		s11.setFont(new Font("Serif", Font.PLAIN, 20));
		s11.setForeground(Color.WHITE);
		s1.add(s11);
		
		secondField = new JPanel(new GridLayout(0, 8));
		secondField.setBackground(new Color(225, 202, 132));
		secondField.add(s1);
		
		secondHeroUseCard = new JButton("Select Card");
		secondHeroUseCard.setEnabled(false);
		secondHeroUseCard.setActionCommand("susecard");
		s1.add(secondHeroUseCard);
	
		firstHeroUseCard = new JButton("Select Card");
		firstHeroUseCard.setEnabled(false);
		firstHeroUseCard.setActionCommand("fusecard");		
		
		JLabel fh = new JLabel(" First Hero Hand:");
		fh.setFont(new Font("Serif", Font.PLAIN, 15));
		fh.setForeground(Color.WHITE);
		JPanel f1 = new JPanel(new GridLayout(3, 0));
		f1.add(new JLabel());
		f1.add(firstHeroUseCard);
		f1.setBackground(new Color(13,29,47));
		JLabel f11 = new JLabel(" First Hero Field:");
		f11.setFont(new Font("Serif", Font.PLAIN, 20));
		f11.setForeground(Color.WHITE);
		f1.add(f11);
		
		firstH = new JPanel(new GridLayout(0,11));
		firstH.add(fh);
		firstH.setBackground(new Color(13,29,47));
		
		firstField = new JPanel(new GridLayout(0, 8));
		firstField.add(f1);
		firstField.setBackground(new Color(225, 202, 132));
		
		GridLayout g = (new GridLayout(0, 1));
		g.setVgap(5);
		JPanel p = new JPanel(g);
		
		p.setBackground(Color.BLACK);
		p.add(firstH);
		p.add(firstField);
		p.add(secondField);
		p.add(secondH);
		
		this.getContentPane().add(side, BorderLayout.EAST);
		this.getContentPane().add(p, BorderLayout.CENTER);

		this.revalidate();
		this.repaint();
		this.setVisible(true);

	}


	public JPanel getSecondH() {
		return secondH;
	}

	public JPanel getFirstH() {
		return firstH;
	}

	public JPanel getFirstField() {
		return firstField;
	}

	public JPanel getSecondField() {
		return secondField;
	}

	public JButton getFirstHeroButton() {
		return firstHeroButton;
	}

	public JButton getFirstHeroPower() {
		return firstHeroPower;
	}

	public JButton getSecondHeroButton() {
		return secondHeroButton;
	}

	public JButton getSecondHeroPower() {
		return secondHeroPower;
	}

	public JButton getEndTurn() {
		return endTurn;
	}

	public JTextArea getFirstHeroDetails() {
		return firstHeroDetails;
	}

	public JTextArea getSecondHeroDetails() {
		return secondHeroDetails;
	}
	
	public JButton getFirstHeroUseCard() {
		return firstHeroUseCard;
	}
	public JButton getSecondHeroUseCard() {
		return secondHeroUseCard;
	}public static void main(String[] args) {
		new GameView();
	}

}