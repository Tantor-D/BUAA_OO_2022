package mycode.sequence;

import com.oocourse.uml3.models.elements.UmlLifeline;

public class SequenceLifeline {
    private final UmlLifeline umlLifeline;
    private int numOfCreateMessage;
    private int numOfFoundMessage;
    private int numOfLostMessage;
    private UmlLifeline creatorLifeline;
    private boolean isDead;
    
    public SequenceLifeline(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
        numOfCreateMessage = 0;
        numOfFoundMessage = 0;
        numOfLostMessage = 0;
        isDead = false;
        creatorLifeline = null;
    }
    
    public boolean isDead() {
        return isDead;
    }
    
    public void setDead(boolean dead) {
        isDead = dead;
    }
    
    public String getId() {
        return umlLifeline.getId();
    }
    
    public String getName() {
        return umlLifeline.getName();
    }
    
    public int getNumOfCreateMessage() {
        return numOfCreateMessage;
    }
    
    public void setCreator(UmlLifeline umlLifeline) {
        this.creatorLifeline = umlLifeline;
    }
    
    public int getNumOfFoundMessage() {
        return numOfFoundMessage;
    }
    
    public int getNumOfLostMessage() {
        return numOfLostMessage;
    }
    
    public UmlLifeline getUmlLifeline() {
        return umlLifeline;
    }
    
    public UmlLifeline getCreatorLifeline() {
        return creatorLifeline;
    }
    
    public void addNumOfFoundMessage() {
        numOfFoundMessage++;
    }
    
    public void addNumOfLostMessage() {
        numOfLostMessage++;
    }
    
    public void addNumOfCreateMessage() {
        numOfCreateMessage++;
    }

}
