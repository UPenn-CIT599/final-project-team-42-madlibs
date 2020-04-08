package madlibs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * MadLib class is the primary class of the Literature Mad-Lib game.  It will call on 
 * all the other methods and classes to run the game.  
 * 
 * @author Team 42 - Ross Beck-MacNeil, Paul Lysander, and Brenda Moss  
 * 
 * Design Progress Notes:  At this point we have designed a good amount of the logic for display
 * to the console and will methodically start transferring it to a more user friendly GUI using 
 * swing.  In the meantime, parts of the program will display on console while others display in 
 * the GUI.
 */

public class MadLib {
	//constants that will parameterize the sampling of the different parts of speech
	private final int MIN_SINGULAR_NOUNS = 2;
	private final int MAX_SINGULAR_NOUNS = 10;
	private final double PERCENT_SINGULAR_NOUNS = 0.25;
	
	private final int MIN_PLURAL_NOUNS = 2;
	private final int MAX_PLURAL_NOUNS = 10;
	private final double PERCENT_PLURAL_NOUNS = 0.25;
	
	private final int MIN_ADJECTIVES = 2;
	private final int MAX_ADJECTIVES = 10;
	private final double PERCENT_ADJECTIVES = 0.25;
	
	private final int MIN_ADVERBS = 2;
	private final int MAX_ADVERBS = 10;
	private final double PERCENT_ADVERBS = 0.25;
	
	private final int MIN_EDVERBS = 2;
	private final int MAX_EDVERBS = 10;
	private final double PERCENT_EDVERBS = 0.25;
	
	private final int MIN_INGVERBS = 2;
	private final int MAX_INGVERBS = 10;
	private final double PERCENT_INGVERBS = 0.25;
	

    private ArrayList<MenuEntry> classicsMenu;
    private ArrayList<MenuEntry> childrensMenu;
    private int firstStart;
    private int invalidCount;
    private int menuSize;
   

    
    public MadLib() {
        classicsMenu = new ArrayList<MenuEntry>();
        childrensMenu = new ArrayList<MenuEntry>();
        
    }
    
    /**
     * madLibRunner method is used to run the Mad-Lib game.  It will call on all the other methods and classes
     * to run the game.
     * 
     */
    public void madLibRunner() {
        
        // Program greeting and menu prompt. Loop prevents introduction from being
        // presented each time madLibRunner() method is called.

        
        //while (firstStart == 0) {
//            System.out.println();
//            System.out.println("Welcome to Literature Mad-Libs");
//            System.out.println();
//            System.out.println("Please select a literature passage you would like to Mad-Lib: ");
            makeMenu();
            openUserInterface();
//            firstStart++;
        //}
        
     
        //Reads in the player's choice of literature based on menu, verifies valid input, identifies passage file name.
        Scanner myScanner = new Scanner(System.in);
        
        int litChoice;
        String passageFileName;
        Passage passage;
        
        if (myScanner.hasNextInt()) {
            litChoice = myScanner.nextInt();
            if (litChoice <= menuSize) {
               if (litChoice <= childrensMenu.size()) {
                   passageFileName = childrensMenu.get(litChoice - 1).getLitFileName();
                   //System.out.println(passageFileName);
               }
               else {
                   passageFileName = classicsMenu.get(litChoice - childrensMenu.size() - 1).getLitFileName();
                   //System.out.println(passageFileName);
               }
               
               String originalText = litReader(passageFileName);
               //This is where we can pass the original text String to the Passage class methods
               passage = new Passage(originalText);
               // Prompt user to supply replacement words
               for (Passage.PartOfSpeech partOfSpeech: Passage.PartOfSpeech.values()) {
            	   promptForReplacement(passage, partOfSpeech);
               }
               //passage.passageRun();
            }
            else {
                
                invalidCount++;
                String invalidEntry = Integer.toString(litChoice);
                invalidStart(invalidCount, invalidEntry);
            }
        }
        
        else {
            invalidCount++;
            String invalidEntry = myScanner.next();
            invalidStart(invalidCount, invalidEntry);
        }
        
   
        myScanner.close();
    }
    
    /**
     * makeMenu method is the used to create the Mad-Lib game menu.  The menu entries are drawn from a file named index.csv.  The 
     * index.csv file contains a line for each entry of the menu.  Each line contains the title of the literature in which the passage
     * was drawn, the author, and the .txt file that contains the passage.  The naming convention of the passage files is children#.txt or
     * classic#.txt depending on if the passage is a drawn from classic or children's literature.  This method is designed to automatically
     * expand if additional pieces of literature are added to the library.  
     * 
     */
    public void makeMenu () {
        File indexFile = new File("index.csv");        
        try {
            Scanner in = new Scanner(indexFile);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String [] lineColumns = line.split(",");
                String litTitle = lineColumns[0];
                String litAuthor = lineColumns[1];
                String litFileName = lineColumns[2].trim();
                String litGenre = "";
                
                //Adds an entry to the childrens or classics menu depending on the genre of literature
                if (litFileName.contains("children")) {
                    litGenre = "childrens";
                    MenuEntry entry = new MenuEntry(litTitle, litAuthor, litFileName, litGenre);
                    childrensMenu.add(entry);
                }
                else if (litFileName.contains("classic")) {
                    litGenre = "classic";
                    MenuEntry entry = new MenuEntry(litTitle, litAuthor, litFileName, litGenre);
                    classicsMenu.add(entry);
                }            
            }
            //menuSize is used in both makeMenu and madLibRunner methods
            menuSize = childrensMenu.size() + classicsMenu.size();
            in.close();
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
      //Displays the Children's Literature menu to console
        int i;
        int j;
        System.out.println();
        System.out.println("Children's Literature");

        for (i = 1; i <= childrensMenu.size(); i++){
            System.out.println(i+ " " + childrensMenu.get(i-1).getLitTitle() + " by " + childrensMenu.get(i-1).getLitAuthor());            
        }
        System.out.println();
        
        //Displays the Classic Literature menu to console
        System.out.println("Classic Literature");
        for (j = childrensMenu.size() + 1; j <= childrensMenu.size() + classicsMenu.size(); j++) {
            System.out.println(j+ " " + classicsMenu.get(j - childrensMenu.size() - 1).getLitTitle() + " by" + classicsMenu.get(j - childrensMenu.size() - 1).getLitAuthor());
        }
        
        System.out.println();   
       
    }
    
    /**
     * openUserInterface method opens the user interface window and passes childrensMenu and classicMenu 
     * to UserInterface1 to display menu
     */
    public void openUserInterface() {
        JFrame window = new JFrame("Literature Mad-Libs Game");
        window.setContentPane(new UserInterface1(childrensMenu, classicsMenu));
            //.setSize sets the frame size -- without it the frame would be tiny
        window.setSize(1100, 600);
        //window.pack();
            //exits the program when the window is closed
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setLocation(150, 100);
            //.setVisible allows frame to be visible
        window.setVisible(true);
    }
    
    public ArrayList<MenuEntry> getClassicsMenu() {
        return classicsMenu;
    }

    public ArrayList<MenuEntry> getChildrensMenu() {
        return childrensMenu;
    }
    
    
    
   /**
    * promptForReplacement is a temporary method that demonstrates how to use the passage object
    * to determine which indexes should be replaced, prompt the user for the 
    * right number of replacement words, and then updated the passage with the
    * supplied words.
    * @param passage
    * @param partOfSpeech
    */
    private void promptForReplacement(Passage passage, Passage.PartOfSpeech partOfSpeech) {
    	Integer[] indexesToSample = passage.getIndexes(partOfSpeech);
    	/* TODO: Figure out proper place to store different parameters for sampling
    	* depending on part of speech, rather than hardcoding here
    	*/
    	Scanner in = new Scanner(System.in);
    	Integer[] indexesToReplace = Passage.sample(indexesToSample, 0.33, 2, 10);
    	int numberOfWordsToReplace = indexesToReplace.length;

    	System.out.println("Please provide " + indexesToReplace.length  + 
    			" of the following: " + partOfSpeech.getPrintableForm());
    	String[] replacementWords = new String[numberOfWordsToReplace];
    	for (int i = 0; i < numberOfWordsToReplace; i++) {
    		System.out.println((i + 1) + "/" + numberOfWordsToReplace + ":");
    		replacementWords[i] = in.nextLine();
    	}
    	passage.replaceWords(replacementWords, indexesToReplace);
    }
    
    /**
     * litReader method takes in the file name of the literary passage the player selects from the Mad-Lib menu, reads the file and 
     * returns a string containing the passage.
     * @param litFileName
     * @return originalText
     * 
     */
    public String litReader(String litFileName) {
        String originalText = null;
        File litFile = new File(litFileName);

        try {
            Scanner myScanner = new Scanner(litFile);
            originalText = myScanner.useDelimiter("\\Z").next(); 
            System.out.println(originalText);
            myScanner.close();
        } 
        
        catch (FileNotFoundException e) {
            System.out.println("Literature passage file could not be found.  Game ended");
            System.exit(0);
            //e.printStackTrace();
        }
        
        return originalText;
    }
    
    
    /**
     * invalidStart method manages invalid entries to for menu selection and terminates the game after three invalid entries.
     * @param invalidCount
     * @param notInteger
     * 
     */
    public void invalidStart(int invalidCount, String notInteger) {
        while (invalidCount <= 2) {            
            System.out.printf("\"%s\" is not a menu choice. \n", notInteger);
            System.out.println("Invalid selection. Please enter a menu number between 1 and " + menuSize + " to indicate your literature selection. ");
            madLibRunner();
        }

        System.out.println(
                "You have entered several selections incorrectly. \nPlease rerun the Mad-Lib game again when you are ready to play.");
        System.exit(0);

    }
    
	public static void main(String[] args) {
	    
		MadLib m = new MadLib();
		m.madLibRunner();
		
		

	}

}
