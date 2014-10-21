package martinlarka;

import javax.swing.JFrame;

public class GestureGUI {
	private JFrame frame;
	
	public GestureGUI() {	
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
    /**
     * Method for displaying the view
     */
    public void setVisible() {
	frame.pack();
	frame.setLocationRelativeTo(null); 
	frame.setVisible(true);	
    }
}
