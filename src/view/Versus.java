package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Versus extends JFrame{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Versus(String path1,String path2) {
		
		
		

        ImageIcon ic1 = new ImageIcon(path1);
        ImageIcon ic2 = new ImageIcon(path2);
		
		JLabel l1 = new JLabel();
		
		JLabel l2 = new JLabel();
		l2.setText("VS. ");
		l2.setFont(new Font("Serif", Font.BOLD + Font.ITALIC, 40));
		
		JLabel l3 = new JLabel();
		
		
		
     
		this.setVisible(false);
		
		final JDialog dialog = new JDialog(this, "Heros Selected", true);
		dialog.setUndecorated(true);
		dialog.setBackground(new Color(0, 0, 0, 0));
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));	
		
		p.setSize(ic1.getIconWidth()*3, ic1.getIconHeight()+50);

		p.setBackground(new Color(0,0,0,0));
		
		
		p.add(l1);
		l1.setIcon(ic1);
		
		p.add(l2);
		
		p.add(l3);
		l3.setIcon(ic2);
		
		
		 
       dialog.add(p);
       dialog.setSize(ic1.getIconWidth()*3, ic1.getIconHeight()+50);
       dialog.setLocationRelativeTo(null);  
        Timer timer = new Timer(1900, new ActionListener() {
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
	
	public static void main(String [] args) {
		
		
		new Versus("Images/Rexxar.png","Images/Gul'dan.png"
				+ "");
		
	}

	
	
}