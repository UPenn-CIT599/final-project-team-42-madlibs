package madlibs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
  * UserInterface1 class implements GUI components using Swing library.
 * 
 * @author Team 42 - Ross Beck-MacNeil, Paul Lysander, and Brenda Moss  
 */
public class UserInterface1 extends JPanel implements ActionListener {

    private static final int STANDARD_WIDTH = 640;
    private JTextArea text; // a message will be posted to this text area
                            // each time an event is generated by some
                            // user action
    private CardLayout c1 = new CardLayout();
    private JPanel cards = new JPanel(c1);
    private JPanel firstCard = new JPanel();
    private JPanel secondCard = new JPanel();
    private JPanel thirdCard = new JPanel();
    private ArrayList<MenuEntry> childrensMenu;
    private ArrayList<MenuEntry> classicsMenu;
    private Boolean RadioButtonSelected = null;
    private String originalText;
    private Passage passage;
 
    
    /**
     * This constructor adds several GUI components to the panel and listens
     * for action events from some of them.
     */
    public UserInterface1(ArrayList<MenuEntry> childrensMenu, ArrayList<MenuEntry> classicsMenu) {
        this.childrensMenu = childrensMenu;
        this.classicsMenu = classicsMenu;
        
        setLayout(c1);
        add(cards, "MadLib");
        cards.add(firstCard, "Menu");
        cards.add(secondCard, "Replace Words");
        cards.add(thirdCard, "Result");
        c1.show(cards, "Menu");

        designFirstCard(); 
        designThirdCard();

        

    }
    
    // end of constructor

    /**
     * designFirstCard method sets up the first window shown in the Literature Mad-Lib game using
     * a Swing BorderLayout.  The layout includes a dynamic menu of literature passage options based 
     * on the contents index.csv file (represented in the childrensMenu & classicsMenu array lists).
     * When each menu option is selected that passage is displayed in the text area to the right.  Once
     * the player decided on the passage they want to use to play Literature Mad-Libs, they will select
     * the button at the bottom of the window to continue playing the game.  
     */
    private void designFirstCard() {
        //Sets BorderLayout as layout for firstCard and inserts title in the top (NORTH) cell
        firstCard.setLayout(new BorderLayout());
        formatTitle(firstCard, "Welcome to Literature Mad-Libs");
        
        
        //Nests GridLayout into the left-middle (WEST) cell of the overall BorderLayout
        //This area will include radio buttons to select the literature menu
        JPanel litMenu = new JPanel(); 
        //Calculates numberMenuRows for GridLayout based on how many passage options are available 
        //plus three to compensate for the instructions and "Children's Literature" & "Classic Literature" labels
        int numMenuRows = (childrensMenu.size() + classicsMenu.size() + 3);        
        litMenu.setLayout(new GridLayout(numMenuRows, 1, 3, 10));
        litMenu.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        firstCard.add(litMenu, BorderLayout.WEST);
        
        //Populates the left-middle (WEST) cell with instructions and radio buttons for each menu option
        JLabel instructions = new JLabel("Please select a literature passage you would like to Mad-Lib: ");
        litMenu.add(instructions);
        JRadioButton[] radioButton = new JRadioButton[(childrensMenu.size() + classicsMenu.size() + 1)];
        ButtonGroup group = new ButtonGroup();
     
        
        //Displays the Children's Literature menu options
        JLabel childrenLabel = new JLabel("Children's Literature:");
        litMenu.add(childrenLabel);
        for (int i = 1; i <= childrensMenu.size(); i++){
            radioButton[i] = new JRadioButton(childrensMenu.get(i-1).getLitTitle() + " by" + childrensMenu.get(i-1).getLitAuthor());
            litMenu.add(radioButton[i]); 
            group.add(radioButton[i]);
            radioButton[i].setName(childrensMenu.get(i-1).getLitFileName());
        }
              
        //Displays the Classic Literature menu options
        JLabel classicLabel = new JLabel("Classic Literature:");
        litMenu.add(classicLabel);
        for (int j = childrensMenu.size() + 1; j <= (childrensMenu.size() + classicsMenu.size()); j++) {
            radioButton[j] = new JRadioButton(classicsMenu.get(j - childrensMenu.size() - 1).getLitTitle() + " by" + classicsMenu.get(j - childrensMenu.size() - 1).getLitAuthor());
            litMenu.add(radioButton[j]);
            group.add(radioButton[j]);
            radioButton[j].setName(classicsMenu.get(j - childrensMenu.size() - 1).getLitFileName());
        }
        
        //Adds action Listener to radioButtons
        for (int k = 1; k <= (childrensMenu.size() + classicsMenu.size()); k++) {
            radioButton[k].addActionListener(this);
            
            
        }
        
        //Places scrolling text area into the right-middle (EAST) cell of the overall BorderLayout
        text = new JTextArea();
        text.setEditable(false);
        text.setMargin(new Insets(4, 4, 4, 4));
        firstCard.add(new JScrollPane(text), BorderLayout.CENTER);        
        
        //Places a "PLAY MAD-LIBS button into the bottom cell of the overall BorderLayout and
        //nests another BorderLayout within the button in order to display a 2-line button
        JButton playButton = new JButton();        
        playButton.setLayout(new BorderLayout());
        JLabel label1 = new JLabel("PLAY MAD-LIBS");
        JLabel label2 = new JLabel("with selected passage");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        playButton.add(BorderLayout.NORTH,label1);
        playButton.add(BorderLayout.SOUTH,label2);
        
        //Adds action listener to the playButton
        playButton.addActionListener(this);
        firstCard.add(playButton, BorderLayout.SOUTH);
        
    }
    
    /**
     * designSecondCard method sets up the second window shown in the Literature Mad-Lib game using
     * a Swing ......  
     */
    private void designSecondCard(Passage passage) {
        //Sets BorderLayout as layout for secondCard and inserts title in the top (NORTH) cell
        secondCard.setLayout(new BorderLayout());
        formatTitle(secondCard, "On Our Way to Literature Mad-Lib Fun!");
        
        //Nests GridLayouts into the middle (CENTER) cell of the overall BorderLayout
        //This area will include text boxes to enter words of various parts of speech
        JPanel wordEntry = new JPanel(); 
        
        System.out.println("start of designSecondCard" + originalText);
       
        //Identifies number of input words needed for each part of speech
        MadLib m = new MadLib();
        int i = 0;
        Integer[] numOfWordsNeeded = new Integer[6];
        for (PartOfSpeech partOfSpeech: PartOfSpeech.values()) {
            numOfWordsNeeded[i] = m.promptForReplacement(passage, partOfSpeech); 
            i++;
        }
        
        //Calculates numRows needed for GridLayout based on how many words needed for each part of speech 
        //and placing the nouns in the first column, adjectives & adverbs in the second column, and
        //verbs in the final column
        int numRows = numOfWordsNeeded[0] + numOfWordsNeeded[1];    //adds number of singular and plural nouns needed
        if (numOfWordsNeeded[2] + numOfWordsNeeded[3] > numRows) {  //adds number of adjectives and adverbs needed
            numRows = numOfWordsNeeded[2] + numOfWordsNeeded[3];
        }
        if (numOfWordsNeeded[4] + numOfWordsNeeded[5] > numRows) {  //adds number of "ed" and "ing" verbs needed
            numRows = numOfWordsNeeded[4] + numOfWordsNeeded[5];
        }
        //Adds two rows to accommodate the part of speech label
        numRows = numRows + 2;
        
        //Sets the nested GridLayout and places it in the CENTER cell of the BorderLayout
        wordEntry.setLayout(new GridLayout(numRows, 3, 3, 10));
        wordEntry.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        secondCard.add(wordEntry, BorderLayout.CENTER);
        
        
        JPanel[][] panelHolder = new JPanel[numRows][3]; 
        
        
        for(int s = 0; s < numRows; s++) {
            for(int t = 0; t < 3; t++) {
               panelHolder[s][t] = new JPanel();
               add(panelHolder[s][t]);
            }
         }
        
        int v = 0;
        if (numOfWordsNeeded[0] > 0) {
            panelHolder[0][0].add(new JLabel("Enter Singular Nouns"));
            for (v = 1; v < numOfWordsNeeded[0]; v++) {
                panelHolder[v][0].add(new JTextField(20));
            }
        }
        v++;
        if (numOfWordsNeeded[1] > 0 ) {
            panelHolder[v][0].add(new JLabel("Enter Plural Nouns"));
            for (int w = v + 1; w < (w + numOfWordsNeeded[1]); w++) {
                panelHolder[w][0].add(new JTextField(20));
            }
        }
        
        
        
    }
    
    
    
    /**
     * designThirdCard method sets up the second window shown in the Literature Mad-Lib game using
     * a Swing ......  
     */
    private void designThirdCard() {
        
    }
    
    private void formatTitle(JPanel card, String titleString) {
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
        JLabel title = new JLabel(titleString);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setPreferredSize(new Dimension(STANDARD_WIDTH, 50));
        card.add(title, BorderLayout.NORTH);
    }
    
    private void post(String message) { // add a message and line feed to the text
        text.append(message + "\n\n");
    }

    /**
     * Respond to an ActionEvent from one of the GUI components in the panel.
     * In each case, a message about the event is posted to the text area.
     * This method is part of the ActionListener interface.
     */
    public void actionPerformed(ActionEvent evt) {
        MadLib m = new MadLib();
        try {
            JRadioButton passageSelected;
            Object target = evt.getSource(); 
            //Manages actions when "PLAY MAD-LIB" button is clicked and doesn't
            //let the player continue unless a radio button (passage) is selected
            if (target instanceof JButton) {
                if (RadioButtonSelected == true) {
                    
                    passage = new Passage(originalText);
                    designSecondCard(passage);
                    c1.show(cards, "Replace Words");
                    
                }
                else {
                    RadioButtonSelected = null;
                }
                
            }
            //Manages display of passage in right text panel when each radio button
            //(passage) is selected
            
            else if (target instanceof JRadioButton) {
                if (((JRadioButton) target).isSelected()) {
                    passageSelected = (JRadioButton) target;
                    
                    originalText = m.litReader(passageSelected.getName());
                    text.setText(originalText);
                    RadioButtonSelected = true;
                   
                  
                }
            
            }
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            
        }
        
    }
    
    /**
     * promptForReplacement is a temporary method that demonstrates how to use the passage object
     * to determine which indexes should be replaced, prompt the user for the 
     * right number of replacement words, and then updated the passage with the
     * supplied words.
     * @param passage
     * @param partOfSpeech
     */
     public int promptForReplacement(Passage passage, PartOfSpeech partOfSpeech) {
         Integer[] indexesToSample = passage.getIndexes(partOfSpeech);   
         Integer[] indexesToReplace = Passage.sample(
             indexesToSample,
             partOfSpeech.getPercent(),
             partOfSpeech.getMinN(),
             partOfSpeech.getMaxN()
         );
         int numberOfWordsToReplace = indexesToReplace.length;
         return numberOfWordsToReplace;

     }
   
}


