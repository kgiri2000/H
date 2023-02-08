/*
 * Main.java
 * Author: Sebastian Colwell
 * Date Last Modified: Feb 06 2023
 * Class: Software Design COSC 3011
 * Program: 01
 * 
 * GameWindow at this point contains all code to display the "game"
 * At this point absolutely nothing is functional other than the quit button
 * This class simply sets up nested JPanels to create a pleasing GUI
 * 
 * This code could have been reduced by a large margin by putting everything in the main content pane
 * The idea is that using nested panels each with their own layout, 
 * resizing will be easier in the future.
 * 
 * Most of the code is splitting the main content pane up into panels
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements ActionListener {
	
	// Default serial UID
	private static final long serialVersionUID = 1L;
	
	// DIMENSIONS
	private static Dimension d_size = new Dimension(1000,1000);   // Default GUI Size
	private static Dimension menu_size = new Dimension(400, 100); // Default Menu Panel Size
	private static Dimension tile_size = new Dimension(50,50);    // Default Tile size (Grid and pieces)
	private static Dimension button_size = new Dimension(100, 70);// Default Button size (within menu)
	
	// COLORS
	private static String background = "#11999E";
	private static String panels     = "#30E3CA";
	private static String elements   = "#E4F9F5";
	private static String lines      = "#40514E";
	
	// FRAMES
	// Quick Note: making this content pane a private variable is just for readability at the moment.
	// This works fine for now since all we are doing is displaying panels.
	private Container main = this.getContentPane();
	private static JPanel menu_panel, game_panel;  // Main Frames (top = menu, bottom = game)
	private static JPanel LPieces, Board, RPieces; // Game Sub-Frames (Player pieces on left, right, and the board)
	
	// BUTTONS
	private static JButton quit, reset, new_game; // Only quit has functionality at the moment

	// GameWindow -- Constructor
	public GameWindow(String window_title){
		super(window_title); // Give the window a title (JFrame)
		
		// Close on exit
		// This was moved inside the object instead of main. The object should set this.
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		main.setLayout(new GridBagLayout()); // Add GridBagLayout to the content pane
		
		// SETUP -- All of this is called on init to setup the GUI
		add_menu_container();                    // Define Menu Panel (Frame)
		new_game = add_button("New Game", 0); // Add Buttons to the menu panel
		reset    = add_button("Reset", 1);
		quit     = add_button("Quit", 2);
		quit.addActionListener(this); // Add the action listener to the quit button
		
		add_game_container();                // Define the Game Area (Frame)
		LPieces = create_game_panel(0, 0.5); // Sub-Frame within the Game Area (Left side)
		Board   = create_game_panel(1, 1);   // Middle area
		RPieces = create_game_panel(2, 0.5); // Right side 
		populate_game_area();                // Add panels to both the board and the pieces sub-frames
	}
	
	// create_main_panel Helper Function
	private JPanel create_main_panel(boolean border) {
		JPanel panel = new JPanel();               // Create a new panel
		panel.setLayout(new GridBagLayout());      // Give it a layout
		panel.setBackground(Color.decode(panels)); // Use the preset panel color
		
		// Give the panel a border if requested
		if(border){
			panel.setBorder(BorderFactory.createLineBorder(Color.decode(lines),3));
		}
		return panel;
	}
	
	// Create the menu panel
	private void add_menu_container() {
		/*
		 * This function creates a nested panel within the content pane of the GUI
		 * The menu panel has a fixed size of 400x100 and will always be in the center top
		 * Insets force the menu to have a little padding so the buttons aren't right on the edge of the GUI
		 * Weight needs to be greater than 0 but less than the game areas weight to create nice spacing
		 */
		menu_panel = create_main_panel(true);   
		menu_panel.setPreferredSize(menu_size); 
		menu_panel.setMinimumSize(menu_size);
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0; 
		c.insets = new Insets(15, 0, 0, 0);
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.NORTH;
		main.add(menu_panel, c);
		return;
	}
	
	// Create the game area panel
	private void add_game_container() {
		/*
		 * This function creates a nested panel within the content pane of the GUI
		 * The game area fills the remainder of the screen below the menu panel
		 * Insets force the game area to have the same padding that the menu does on all sides. 
		 * This creates a 15px background border
		 * Weights are maximized so the panel can take as much space as the layout allows.
		 */
		game_panel = create_main_panel(true);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 15, 15, 15);
		c.weighty = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.BOTH;
		main.add(game_panel, c);
	}
	// create_game_panel -- helper
	private JPanel create_game_panel(int x, double weight) {
		/*
		 * This function creates and adds a panel to the game_area
		 * This is used to create the sub-frames for LPieces RPieces and Board
		 * This utilizes the create_main_panel helper function but without the border
		 * These sub-frames are invisible but it makes the game area easy to populate with tiles
		 * The sub-frames are layed out horizontally so only the gridx and weightx need to be passed in
		 * weightx allows the board to take up more space than the pieces, rather than being equal thirds
		 */
		JPanel panel = create_main_panel(false);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = 0;
		c.weightx = weight;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,5,5,5);
		game_panel.add(panel, c);
		return panel;
	}
	
	private JPanel add_tile(JPanel container, int x, int y, Insets insets) {
		/*
		 * This function adds a 50x50 panel (a tile) to a container panel
		 * This is used to create the grid and all player pieces
		 * This can easily be modified to create and add a Tile() object in the future
		 */
		JPanel tile = new JPanel();
		tile.setPreferredSize(tile_size);
		tile.setMinimumSize(tile_size);
		tile.setBackground(Color.decode(elements));
		tile.setBorder(BorderFactory.createLineBorder(Color.decode(lines), 1));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.insets = insets;
		container.add(tile, c);
		return tile;
	}
	
	private JPanel add_tile(JPanel container, int x, int y, Insets insets, String text) {
		/*
		 * This is an additional add_tile function which creates the same tile
		 * But also takes a text argument to display on the tile
		 * This allows the pieces to be numbered and the grid can stay blank.
		 */
		JPanel tile = add_tile(container, x, y, insets);
		JLabel label = new JLabel(text);
		tile.add(label);
		return tile;
	}
	
	private void populate_game_area() {
		/*
		 * Add 8 tiles to each side of the game area, add them vertically by keeping all gridx = 0
		 * Add 16 tiles arranged in a grid to the board
		 * Simply done through a nested for loop which assigns the gridx and gridy of each tile
		 */
		for(int i = 0; i < 8; i ++) {
			add_tile(LPieces, 0, i, new Insets(10,10,10,10), Integer.toString(i));
			add_tile(RPieces, 0, i, new Insets(10,10,10,10), Integer.toString(i + 8));
		}
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				add_tile(Board, j, i, new Insets(1,1,1,1));
			}
		}
		return;
	}
	
	private JButton add_button(String text, int x) {
		/*
		 * Add a single button to the menu panel
		 * Give this function the text on the button, and the grid location
		 * These are arranged horizontally within the menu panel with padding on left/right
		 * So the buttons appear to be spaced evenly
		 */
		JButton button = new JButton(text);
		button.setPreferredSize(button_size);
		button.setMinimumSize(button_size);
		button.setBackground(Color.decode(elements));
		button.setBorder(BorderFactory.createLineBorder(Color.decode(lines), 2));
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = x;
		menu_panel.add(button, c);
		return button;
	}

	public void show_ui() {
		/*
		 * Very simple public function which sets the default size
		 * Sets background color
		 * And shows the GUI
		 * This could be all within the constructor
		 * However this gives the user the ability when to show the GUI
		 */
		this.setSize(d_size);
		main.setBackground(Color.decode(background));
		this.setVisible(true);
		return;
	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * Only functionality now is the quit button
		 * This just system exits if the quit button is pressed
		 */
		if (e.getSource() == quit) {
			System.exit(0);
		}
		return;
	}
}
