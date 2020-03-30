package madlibs;

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
