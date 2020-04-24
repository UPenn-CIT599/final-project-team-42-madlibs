package madlibs;

/**
 * Unit test to compare entries in index.csv file with the number 
 * of entries generated in makeMenu method in MadLib class
 *  
 */

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class MadLibTest {
          
     int childrensBooksCounter = 0;
     int classicsBooksCounter = 0;
     
     // object created from MadLib class
     MadLib m = new MadLib();
     
     @Test
     void makeMenuTest () {
          // call makeMenu method
          m.makeMenu();
          // determine size of menu ArrayList
          int childrensBooksNumber = m.getChildrensMenu().size();
          int classicsBooksNumber = m.getClassicsMenu().size();

          // Reads in the text from index.csv, regular expression pattern matching
          File inputFile = new File("index.csv");
          try {
               Scanner in = new Scanner(inputFile);
               
               while(in.hasNextLine()) {
                    String line = in.nextLine();
                    if (Pattern.matches(".*children[0-9].txt", line)) {
                         childrensBooksCounter ++;
                    }
                    
                    else {
                         classicsBooksCounter ++; 
                         }
                    }
               in.close();
                    
          }
                    
          catch (IOException e ) {
               e.printStackTrace();
               System.out.println("Check that file is being read properly.");
          }
          assertEquals(childrensBooksCounter, childrensBooksNumber);
          assertEquals(classicsBooksCounter, classicsBooksNumber);
     }


}
