package gui;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.*;
import java.awt.event.*;

public class TheDesktop extends JFrame implements ActionListener {
	static JDesktopPane desktop;
	
	public MainInternalFrame find_frame;
	public JMenuBar menuBar;
	public JMenu file_menu;
	public JMenu credential_menu;
	public JMenu help_menu;

	public TheDesktop(String mytitle) {
		//Make the big window be indented 200 pixels from each edge
		//of the screen.
		int inset = 200;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset,
				screenSize.width  - inset*5,
				screenSize.height - inset*2);
		//Set up the GUI
		desktop = new JDesktopPane(); //a specialized layered pane
		setContentPane(desktop);
		setJMenuBar(createMenuBar());

		desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
		this.setTitle(mytitle);
		loadGUI();//start the GUI to process user actions
	}

	protected JMenuBar createMenuBar() {
		menuBar = new JMenuBar();

		//Set up the File menu.
		file_menu=addToMenuBar(menuBar, "File", KeyEvent.VK_F);
		addToMenu(file_menu, "Quit", "quit");

		return menuBar;
	}

	private JMenu addToMenuBar(JMenuBar menuBar, String title, int t_event) {
		JMenu t_menu = new JMenu(title);
		t_menu.setMnemonic(t_event);
		menuBar.add(t_menu);
		return t_menu;
	}

	private void addToMenu(JMenu t_menu, String title, String act_cmd) {
		//This method creates a menuItem with "title" adds it to the t_menu and sets up a shortcut
		//to the action based on the KeyEvent int passed in.
		JMenuItem menuItem = new JMenuItem(title);
		menuItem.setActionCommand(act_cmd);
		menuItem.addActionListener(this);
		t_menu.add(menuItem);
	}

	//React to menu selections.
	public void actionPerformed(ActionEvent e) {
		String cmd=e.getActionCommand();
		if (cmd.equals("quit")) {
			System.exit(0);
		}
	}

	protected void loadGUI() {
		setCursorWait(true); 
		
		find_frame = new MainInternalFrame(desktop);
        find_frame.setVisible(true);
        desktop.add(find_frame);
        
        try {
            find_frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
		
		//FileChooser fc = new FileChooser(desktop);
		setCursorWait(false); 
	}

	public static void setCursorWait(Boolean state){
		if (state)
			desktop.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		else
			desktop.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); 
	}

	private static void createAndShowGUI(String myTitle) {
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
		//Create and set up the window.
		TheDesktop setupDesktop = new TheDesktop(myTitle);
		setupDesktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setupDesktop.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				//Close out any settings frames that were open
				System.exit(0); 
			}
		});  

		//Display the window.
		setupDesktop.setVisible(true);
	}

	public static void drawPage(final String myTitle) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(myTitle);
			}
		});
	}
}
