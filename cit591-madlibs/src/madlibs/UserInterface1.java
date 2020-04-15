package madlibs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * UserInterface1 class implements GUI components using Swing library.
 * 
 * @author Team 42 - Ross Beck-MacNeil, Paul Lysander, and Brenda Moss
 */
public class UserInterface1 extends JPanel implements ActionListener {

    private static final int STANDARD_WIDTH = 640;
    private JTextArea text;
    private CardLayout c1 = new CardLayout();
    private JPanel cards = new JPanel(c1);
    private JPanel firstCard = new JPanel();
    private JPanel secondCard = new JPanel();
    private JPanel thirdCard = new JPanel();
    private JPanel forthCard = new JPanel();
    private JPanel fifthCard = new JPanel();
    private JPanel sixthCard = new JPanel();
    private JPanel seventhCard = new JPanel();
    private JPanel eighthCard = new JPanel();
    private JPanel wordEntry = new JPanel();
    private ArrayList<MenuEntry> childrensMenu;
    private ArrayList<MenuEntry> classicsMenu;
    private Boolean RadioButtonSelected = null;
    private JButton playButton;

    private JButton continueButton;
    private JTextField[] wordField;
    private int cardCounter = 0;

    private String originalText;
    private Passage passage;
    private MadLib m = new MadLib();
    private int numOfWords;
    private String[] replacementWords;
    Integer[] indexesToReplace;
    
    private JTextArea text2;
    private JButton playAgainButton;
    private int firstStart;

    /**
     * This constructor adds several GUI components to the panel and listens for
     * action events from some of them.
     */
    public UserInterface1(ArrayList<MenuEntry> childrensMenu, ArrayList<MenuEntry> classicsMenu) {
        this.childrensMenu = childrensMenu;
        this.classicsMenu = classicsMenu;

        setLayout(c1);
        add(cards, "MadLib");
        cards.add(firstCard, "Menu");
        cards.add(secondCard, "Replace Nouns");
        cards.add(thirdCard, "Replace Plural Nouns");
        cards.add(forthCard, "Replace Adjectives");
        cards.add(fifthCard, "Replace Adverbs");
        cards.add(sixthCard, "Replace -ed Verbs");
        cards.add(seventhCard, "Replace -ing Verbs");
        cards.add(eighthCard, "Result");
        c1.show(cards, "Menu");

        designFirstCard();

    }

    // end of constructor

    /**
     * designFirstCard method sets up the first window shown in the Literature
     * Mad-Lib game using a Swing BorderLayout. The layout includes a dynamic menu
     * of literature passage options based on the contents index.csv file
     * (represented in the childrensMenu & classicsMenu array lists). When each menu
     * option is selected that passage is displayed in the text area to the right.
     * Once the player decided on the passage they want to use to play Literature
     * Mad-Libs, they will select the button at the bottom of the window to continue
     * playing the game.
     */
    private void designFirstCard() {
        // Sets BorderLayout as layout for firstCard and inserts title in the top
        // (NORTH) cell
        firstCard.setLayout(new BorderLayout());
        formatTitle(firstCard, "Welcome to Literature Mad-Libs");

        // Nests GridLayout into the left-middle (WEST) cell of the overall BorderLayout
        // This area will include radio buttons to select the literature menu
        JPanel litMenu = new JPanel();
        // Calculates numberMenuRows for GridLayout based on how many passage options
        // are available
        // plus three to compensate for the instructions and "Children's Literature" &
        // "Classic Literature" labels
        int numMenuRows = (childrensMenu.size() + classicsMenu.size() + 3);
        litMenu.setLayout(new GridLayout(numMenuRows, 1, 3, 10));
        litMenu.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        firstCard.add(litMenu, BorderLayout.WEST);

        // Populates the left-middle (WEST) cell with instructions and radio buttons for
        // each menu option
        JLabel instructions = new JLabel("Please select a literature passage you would like to Mad-Lib: ");
        litMenu.add(instructions);
        JRadioButton[] radioButton = new JRadioButton[(childrensMenu.size() + classicsMenu.size() + 1)];
        ButtonGroup group = new ButtonGroup();

        // Displays the Children's Literature menu options
        JLabel childrenLabel = new JLabel("Children's Literature:");
        litMenu.add(childrenLabel);
        for (int i = 1; i <= childrensMenu.size(); i++) {
            radioButton[i] = new JRadioButton(
                    childrensMenu.get(i - 1).getLitTitle() + " by" + childrensMenu.get(i - 1).getLitAuthor());
            litMenu.add(radioButton[i]);
            group.add(radioButton[i]);
            radioButton[i].setName(childrensMenu.get(i - 1).getLitFileName());
        }

        // Displays the Classic Literature menu options
        JLabel classicLabel = new JLabel("Classic Literature:");
        litMenu.add(classicLabel);
        for (int j = childrensMenu.size() + 1; j <= (childrensMenu.size() + classicsMenu.size()); j++) {
            radioButton[j] = new JRadioButton(classicsMenu.get(j - childrensMenu.size() - 1).getLitTitle() + " by"
                    + classicsMenu.get(j - childrensMenu.size() - 1).getLitAuthor());
            litMenu.add(radioButton[j]);
            group.add(radioButton[j]);
            radioButton[j].setName(classicsMenu.get(j - childrensMenu.size() - 1).getLitFileName());
        }

        // Adds action Listener to radioButtons
        for (int k = 1; k <= (childrensMenu.size() + classicsMenu.size()); k++) {
            radioButton[k].addActionListener(this);

        }

        // Places scrolling text area into the right-middle (EAST) cell of the overall
        // BorderLayout
        text = new JTextArea();
        text.setEditable(false);
        text.setMargin(new Insets(4, 4, 4, 4));
        firstCard.add(new JScrollPane(text), BorderLayout.CENTER);

        // Places a "PLAY MAD-LIBS button into the bottom cell of the overall
        // BorderLayout and
        // nests another BorderLayout within the button in order to display a 2-line
        // button
        playButton = new JButton();
        playButton.setLayout(new BorderLayout());
        JLabel label1 = new JLabel("PLAY MAD-LIBS");
        JLabel label2 = new JLabel("with selected passage");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        playButton.add(BorderLayout.NORTH, label1);
        playButton.add(BorderLayout.SOUTH, label2);

        // Adds action listener to the playButton
        playButton.addActionListener(this);
        firstCard.add(playButton, BorderLayout.SOUTH);

    }

    /**
     * designWordRequestCards method sets the design for the secondCard thru seventhCard.  Each card collects
     * replacement words for a different part of speech.  If there are no replacement words required for a particular
     * part of speech then that window (card) is not displayed.
     */
    private void designWordRequestCards() {

        if (cardCounter == 0) {
            c1.show(cards, "Replace Nouns");
            setWordRequestCardLayout(secondCard);
        } else if (cardCounter == 1) {
            c1.show(cards, "Replace Plural Nouns");
            setWordRequestCardLayout(thirdCard);
        } else if (cardCounter == 2) {
            c1.show(cards, "Replace Adjectives");
            setWordRequestCardLayout(forthCard);
        } else if (cardCounter == 3) {
            c1.show(cards, "Replace Adverbs");
            setWordRequestCardLayout(fifthCard);
        } else if (cardCounter == 4) {
            c1.show(cards, "Replace -ed Verbs");
            setWordRequestCardLayout(sixthCard);
        } else if (cardCounter == 5) {
            c1.show(cards, "Replace -ing Verbs");
            setWordRequestCardLayout(seventhCard);
        }

        PartOfSpeech[] pos = PartOfSpeech.values();
        requestWords(pos[cardCounter]);
        cardCounter++;

    }
    
    /**
     * setWordRequestCardLayout sets the layout for all the word request cards
     * @param wordCard
     */
    private void setWordRequestCardLayout(JPanel wordCard) {
        wordEntry.removeAll();
        // Sets BorderLayout as layout for secondCard and inserts title in the top
        // (NORTH) cell
        wordCard.setLayout(new BorderLayout());
        formatTitle(wordCard, "On Our Way to Literature Mad-Lib Fun!");

        // Nests GridLayouts into the middle (CENTER) cell of the overall BorderLayout
        // This area will include text boxes to enter words of various parts of speech

        // Sets the nested wordEntry GridLayout and places it in the CENTER cell of the
        // BorderLayout
        wordEntry.setLayout(new GridLayout(10, 1, 3, 10));
        wordEntry.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        wordCard.add(wordEntry, BorderLayout.CENTER);
    }

    private void requestWords(PartOfSpeech partOfSpeech) {
        indexesToReplace = m.promptForReplacement(passage, partOfSpeech);
        numOfWords = indexesToReplace.length;
        if (indexesToReplace.length > 0) {
            JLabel instructions = new JLabel("Enter " + partOfSpeech.getDescription());
            wordEntry.add(instructions);

            wordField = new JTextField[indexesToReplace.length];
            for (int i = 0; i < indexesToReplace.length; i++) {
                wordField[i] = new JTextField(10);
                wordEntry.add(wordField[i]);
            }
            continueButton = new JButton("CONTINUE");
            continueButton.addActionListener(this);
            wordEntry.add(continueButton);
        }
        else {
            cardCounter++;
            designWordRequestCards();
        }
        
    }

    private void collectWords() {
        replacementWords = new String[numOfWords];
        for (int i = 0; i < numOfWords; i++) {
            replacementWords[i] = wordField[i].getText();
            //System.out.println(replacementWords[i]);
        }
        passage.replaceWords(replacementWords, indexesToReplace);
    }

    /**
     * designResultCard method sets up the result window (eighthCard) to display the passage
     * with designated words replaced with the words inputed by the player on secondCard thru
     * seventhCard
     */
    private void designResultCard() {
        c1.show(cards, "Result");
        
        while (firstStart == 0) {
            // Sets BorderLayout as layout for firstCard and inserts title in the top
            // (NORTH) cell
            eighthCard.setLayout(new BorderLayout());
            formatTitle(eighthCard, "Literature Mad-Lib Result");
            
            // Places scrolling text area into the middle (CENTER) cell of the overall
            // BorderLayout  (note the EAST cell is empty)
            text2 = new JTextArea();
            text2.setEditable(false);
            text2.setMargin(new Insets(4, 4, 4, 4));
            
            eighthCard.add(new JScrollPane(text2), BorderLayout.CENTER);
            
            System.out.println("finished result page layout setup");
            
            // Places a "PLAY AGAIN button into the bottom cell of the overall
            // BorderLayout and
            // nests another BorderLayout within the button in order to display a 2-line
            // button
            playAgainButton = new JButton();
            playAgainButton.setLayout(new BorderLayout());
            JLabel label1 = new JLabel("PLAY AGAIN");
            label1.setHorizontalAlignment(SwingConstants.CENTER);
            playAgainButton.add(BorderLayout.NORTH, label1);
           
    
            // Adds action listener to the playButton
            playAgainButton.addActionListener(this);
            eighthCard.add(playAgainButton, BorderLayout.SOUTH);
        
            firstStart++;
        }
        
        // Displays Mad-Lib updatedText in the text field 
        String updatedText = passage.getUpdatedText();
        //System.out.println("updated text :" + updatedText);
        text2.setText(updatedText);
        
        
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
     * Respond to an ActionEvent from one of the GUI components in the panel. In
     * each case, a message about the event is posted to the text area. This method
     * is part of the ActionListener interface.
     */
    public void actionPerformed(ActionEvent evt) {
        MadLib m = new MadLib();
        try {
            JRadioButton passageSelected;
            Object target = evt.getSource();
            // Manages actions when "PLAY MAD-LIB" button is clicked and doesn't
            // let the player continue unless a radio button (passage) is selected
            if (target instanceof JButton) {
                if (target == playButton) {
                    if (RadioButtonSelected == true) {

                        passage = new Passage(originalText);
                        designWordRequestCards();

                    } else {
                        RadioButtonSelected = null;
                    }
                }
                if (target == continueButton) {
                    collectWords();
                    if (cardCounter < 6) {
                        designWordRequestCards();
                    } else {
                        cardCounter = 0;
                        designResultCard();
                    }
                        
                }
                if (target == playAgainButton) {
                    c1.show(cards, "Menu");
                }
            }
            // Manages display of passage in right text panel when each radio button
            // (passage) is selected

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

}
