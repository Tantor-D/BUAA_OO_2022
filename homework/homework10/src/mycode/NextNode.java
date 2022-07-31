package mycode;

public class NextNode implements Comparable<NextNode> {
    private int dst;
    private int personId;
    
    public NextNode(int personId, int dst) {
        this.dst = dst;
        this.personId = personId;
    }
    
    public int getDst() {
        return dst;
    }
    
    public int getPersonId() {
        return personId;
    }
    
    @Override
    public int compareTo(NextNode o) {
        if (this.getDst() < o.getDst()) {
            return -1;
        }
        return 1;
    }
}
