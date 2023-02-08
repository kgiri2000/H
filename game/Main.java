/*
 * Main.java
 * Author: Sebastian Colwell
 * Date Last Modified: Feb 06 2023
 * Class: Software Design COSC 3011
 * Program: 01
 * 
 * The Main file for Program 01
 * A few things have been removed from the example Main.java given by Kim Buckner
 * Namely, the GameWindow sets its own size, it sets the color palette, and the EXIT_ON_CLOSE
 * Reasoning behind these changes are included in comments inside GameWindow.java
 */
import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		
		// Instantiate a GameWindow object, pass in the window title
		GameWindow game = new GameWindow("Hotel Group Maze Game");
		
		// Show ui makes the gui visible to the user
		game.show_ui();
		
		// Try to set the look and feel of the UI
		// Catch exceptions
		// Personal preference is Nimbus, so all other commented feels have been removed
	    try {
	        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	      } 
	      catch (UnsupportedLookAndFeelException e) {
	    	  System.out.println("Cannot set Look and Feel to Nimbus, using default");
	      }
	      catch (ClassNotFoundException e) {
	    	  System.out.println("UIManager class not found, import javax.swing.*");
	      }
	      catch (InstantiationException e) {
	    	  System.out.println("Cannot Instantiate UIManager class, using default");
	      }
	      catch (IllegalAccessException e) {
	    	  System.out.println("UIManager cannot set look at feel, using default");
	      }
		return;
	}
}
