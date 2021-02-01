package view;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Popup extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Popup(String msg , int w , int h) {

        JLabel l = new JLabel();
       l.setText(msg);
        
        this.setVisible(false);
		this.toFront();
		final JDialog dialog = new JDialog(this, "Error", true);
		
		 JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 77));
	        p.setSize(w,h );
	        p.add(l,BorderLayout.CENTER);
	        
		//dialog.setLayout(new FlowLayout());
        
	     dialog.add(p);
        dialog.setSize(w, h);
        
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