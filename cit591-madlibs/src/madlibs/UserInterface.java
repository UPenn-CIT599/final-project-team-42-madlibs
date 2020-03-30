package madlibs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UserInterface extends JPanel {
     private ArrayList<JTextField> fieldList; // List of JTextFields for the blank spots
     private JPanel inputPanel; // panel for user word input
     private JPanel resultPanel; // panel to display the completed mad libs game result
     private JButton openButton, saveButton;; // buttons for user to open file, save game

     /**
      * constructor for creating mad libs game user interface
      * 
      */
     public UserInterface() {
          JFileChooser selectTextFile = new JFileChooser();
          selectTextFile.setDialogTitle("Please select a text file to begin.");
          File originalText = null;

     }

     /**
      * Method that builds a panel that allows a user to insert text for filling in
      * the mad libs blanks.
      */
     private void buildInputPanel() {
          inputPanel = new JPanel();
          saveButton = new JButton("Fill in the blanks.");
          saveButton.addActionListener(new ActionListener() {

          }
     
          /**
           * Method for displaying the final results for the game
           */
          private void buildFinalResult() {
               resultPanel = new JPanel();
               resultPanel.setLayout(new BorderLayout());
               
          }

          /**
           * method that adds the user input to the game and creates a JEditorPane
           * to display it.
           */
          private void finishStory() {
                  String fieldText;
                  // Pane to display the story board
                  JEditorPane storyPane = new JEditorPane();

          }
          
          public static void main(String args[])
          {
               JFrame gameFrame = new JFrame("Play Mad Libs");
               
          }


     }