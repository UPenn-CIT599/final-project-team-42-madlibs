package madlibs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * MadLib class is the primary class of the Literature Mad-Lib game. It will
 * call on all the other methods and classes to run the game.
 * 
 * @author Team 42 - Ross Beck-MacNeil, Paul Lysander, and Brenda Moss
 * 
 */

public class MadLib {
     private ArrayList<MenuEntry> classicsMenu;
     private ArrayList<MenuEntry> childrensMenu;
     /*
      * This global constant is used to determine the maximum number of words that
      * the user will be prompted for each part of speech
      */
     public static final int MAX_WORDS_PER_PART_OF_SPEECH = 5;

     public MadLib() {
          classicsMenu = new ArrayList<MenuEntry>();
          childrensMenu = new ArrayList<MenuEntry>();

     }

     /**
      * madLibRunner method is used to run the Mad-Lib game. It will call on all the
      * other methods and classes to run the game.
      */
     public void madLibRunner() {

          // Manages the initial run of the game by directing the construction of the menu
          // and
          // opening the graphical user interface.

          makeMenu();
          openUserInterface();
     }

     /**
      * makeMenu method is the used to create the Mad-Lib game menu. The menu entries
      * are drawn from a file named index.csv. The index.csv file contains a line for
      * each entry of the menu. Each line contains the title of the literature in
      * which the passage was drawn, the author, and the .txt file that contains the
      * passage. The naming convention of the passage files is children#.txt or
      * classic#.txt depending on if the passage is a drawn from classic or
      * children's literature. This method is designed to automatically expand if
      * additional pieces of literature are added to the library.
      * 
      */
     public void makeMenu() {
          File indexFile = new File("index.csv");
          try {
               Scanner in = new Scanner(indexFile, "utf-8");
               while (in.hasNextLine()) {
                    String line = in.nextLine();
                    String[] lineColumns = line.split(",");
                    String litTitle = lineColumns[0];
                    String litAuthor = lineColumns[1];
                    String litFileName = lineColumns[2].trim();
                    String litGenre = "";

                    // Adds an entry to the children's or classics menu depending on the genre of
                    // literature
                    if (litFileName.contains("children")) {
                         litGenre = "childrens";
                         MenuEntry entry = new MenuEntry(litTitle, litAuthor, litFileName, litGenre);
                         childrensMenu.add(entry);
                    } else if (litFileName.contains("classic")) {
                         litGenre = "classic";
                         MenuEntry entry = new MenuEntry(litTitle, litAuthor, litFileName, litGenre);
                         classicsMenu.add(entry);
                    }
               }
               in.close();

          } catch (FileNotFoundException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }
     }

     /**
      * openUserInterface method opens the user interface window and passes
      * childrensMenu and classicMenu to UserInterface to display menu
      */
     public void openUserInterface() {
          SwingUtilities.invokeLater(new Runnable() {
               public void run() {

                    JFrame window = new JFrame("Literature Mad-Libs Game");
                    window.setContentPane(new UserInterface(childrensMenu, classicsMenu));
                    // .setSize sets the frame size -- without it the frame would be tiny
                    window.setSize(1100, 600);
                    // .setDefaultCloseOperation exits the program when the window is closed
                    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    window.setResizable(false);
                    window.setLocation(150, 100);
                    // .setVisible allows frame to be visible
                    window.setVisible(true);
               }

          });
     }

     /**
      * litReader method takes in the file name of the literary passage the player
      * selects from the Mad-Lib menu, reads the file and returns a string containing
      * the passage.
      * 
      * @param litFileName
      * @return originalText
      * 
      */
     public String litReader(String litFileName) {
          String originalText = null;
          File litFile = new File(litFileName);

          try {
               Scanner myScanner = new Scanner(litFile, "utf-8");
               originalText = myScanner.useDelimiter("\\Z").next();
               myScanner.close();
          }

          catch (FileNotFoundException e) {
               System.out.println("Literature passage file could not be found.  Game ended");
               System.exit(0);
               // e.printStackTrace();
          }

          return originalText;
     }

     public ArrayList<MenuEntry> getClassicsMenu() {
          return classicsMenu;
     }

     public ArrayList<MenuEntry> getChildrensMenu() {
          return childrensMenu;
     }

     public static void main(String[] args) {

          MadLib m = new MadLib();
          m.madLibRunner();

     }

}