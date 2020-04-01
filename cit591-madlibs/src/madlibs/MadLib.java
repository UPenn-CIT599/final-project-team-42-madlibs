package madlibs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MadLib {
    private ArrayList<MenuEntry> classicsMenu;
    private ArrayList<MenuEntry> childrensMenu;
    private int firstStart;
    private int invalidCount;
    private int menuSize;

    public MadLib() {
        
    }
    
    /**
     * madLibRunner method is the used to run the Mad-Lib game.  It will call on all the other methods and classes
     * to run the game.
     * 
     * @author Brenda Moss
     */
    public void madLibRunner() {
        

        // Program greeting and menu prompt. Loop prevents introduction from being
        // presented each time madLibRunner() method is called.

        
        
        while (firstStart == 0) {
            System.out.println();
            System.out.println("Welcome to Literature Mad-Libs");
            System.out.println();
            System.out.println("Please select a literature passage you would like to Mad-Lib: ");
            makeMenu();         
            firstStart++;
        }
        
        Scanner myScanner = new Scanner(System.in);
        
        //Reads in the player's choice of literature based on menu, verifies valid input, identifies passage file name.
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
     * @author Brenda Moss
     */
    public void makeMenu () {
        File indexFile = new File("index.csv");
        classicsMenu = new ArrayList<MenuEntry>();
        childrensMenu = new ArrayList<MenuEntry>();
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
        
        
        int i;
        int j;
        
        
        //Displays the Children's Literature menu
        System.out.println();
        System.out.println("Children's Literature");

        for (i = 1; i <= childrensMenu.size(); i++){
            System.out.println(i+ " " + childrensMenu.get(i-1).getLitTitle() + " by " + childrensMenu.get(i-1).getLitAuthor());            
        }
        System.out.println();
        
        //Displays the Classic Literature menu
        System.out.println("Classic Literature");
        for (j = childrensMenu.size() + 1; j <= childrensMenu.size() + classicsMenu.size(); j++) {
            System.out.println(j+ " " + classicsMenu.get(j - childrensMenu.size() - 1).getLitTitle() + " by" + childrensMenu.get(j - childrensMenu.size() - 1).getLitAuthor());
        }
        
        System.out.println();      
    }
    
    /**
     * litReader method takes in the file name of the literary passage the player selects from the Mad-Lib menu, reads the file and 
     * returns a string containing the passage.
     * @param litFileName
     * @return originalText
     * 
     * @author - Brenda Moss
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
     * @author Brenda Moss
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
