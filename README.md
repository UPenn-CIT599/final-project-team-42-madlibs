# CIT591-madlibs

**Mad Libs** is a collaborative project by Ross Beck-MacNeil, Brenda Moss, and Paul Lysander for their final project for MCIT 591 at UPenn.

**project category:** Natural Language Processing

**data source (external):** plain text files from Project Gutenberg (https://www.gutenberg.org/)


**project software components/libraries:**  
- JDK 10+  
- Stanford CoreNLP  
- Swing (user interface)  
**Please note:** attempting to load and run the program with an older JDK version may result in various error conditions.

**Game Flow**  
Player is given the opportunity to select a passage from an offering of well-known literature or children’s book passages. Passage is read in by the program and analysis/Natural Language Processing is executed on the passage to "tag" words with their part-of-speech (POS). The program ramdomly selects a number of words in the passage. For each selected word, the player is prompted to provide a replacement word that corresponds to the same part-of-speech (e.g. verb, proper noun, etc.) as the original word.
The player does not know the specific word they are replacing in the original text - only the part-of-speech. The program substitutes the player-provided words into the passage in place of the original words and displays the updated passage with the replacement words highlighted.  Also displayed is the original literature passage for reference. Players are then given the option to play the game again.

**To run program:**   
Clone the repository and run *MadLib.java*. The entire set of files including text files, index file (lists text files being used) and pom.xml file are in the GitHub repository.  
[https://github.com/UPenn-CIT599/final-project-team-42-madlibs/tree/master/cit591-madlibs](https://github.com/UPenn-CIT599/final-project-team-42-madlibs/tree/master/cit591-madlibs)  
Maven is the build automation tool and uses pom.xml file which provides the configuration details for the project.

To set up project and run program in Eclipse:  
1) First need to clone GitHub repository on local machine. From terminal window, run the following command  
git remote set-url origin https://github.com/UPenn-CIT599/final-project-team-42-madlibs.git
2) To add project to Eclipse, create new project  
File > Open Projects from File System  
<img src="https://github.com/UPenn-CIT599/final-project-team-42-madlibs/blob/master/screenshots/Eclipse_-_screenshot_1.jpg" width="270"/>
3) From within Eclipse, navigate to and select project directory location  
<img src="https://github.com/UPenn-CIT599/final-project-team-42-madlibs/blob/master/screenshots/Eclipse_-_screenshot_2.jpg" height="120" />  
4) This will result in the following folders and file structure. The Mad Lib application is executed by running MadLib.java.  
<img src="https://github.com/UPenn-CIT599/final-project-team-42-madlibs/blob/master/screenshots/Eclipse_-_screenshot_3.jpg" width="270" />








