package madlibs;

/**
 * MenuEntry class defines the parameters associated with each literary entry on the menu.  
 * 
 * @author Team 42 - Ross Beck-MacNeil, Paul Lysander, and Brenda Moss  
 */


public class MenuEntry {
    String litTitle;
    String litAuthor;
    String litFileName;
    String litGenre;
    
    public MenuEntry(String litTitle, String litAuthor, String litFileName, String litGenre) {
        this.litTitle = litTitle;
        this.litAuthor = litAuthor;
        this.litFileName = litFileName;
        this.litGenre = litGenre;
        
    }

    public String getLitTitle() {
        return litTitle;
    }

    public String getLitAuthor() {
        return litAuthor;
    }

    public String getLitFileName() {
        return litFileName;
    }
    
    public String getLitGenre() {
        return litGenre;
    }
    
}
