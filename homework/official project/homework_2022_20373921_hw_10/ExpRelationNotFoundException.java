package mycode;

import com.oocourse.spec2.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class ExpRelationNotFoundException extends RelationNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> idToCountMap = new HashMap<>();
    private int id1;
    private int id2;
    
    public ExpRelationNotFoundException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        count++;
        if (idToCountMap.containsKey(id1)) {
            int prev = idToCountMap.get(id1);
            idToCountMap.put(id1, prev + 1);
        } else {
            idToCountMap.put(id1, 1);
        }
        
        if (id1 != id2) {
            if (idToCountMap.containsKey(id2)) {
                int prev = idToCountMap.get(id2);
                idToCountMap.put(id2, prev + 1);
            } else {
                idToCountMap.put(id2, 1);
            }
        }
    }
    
    @Override
    public void print() {
        if (id1 < id2) {
            System.out.println("rnf-" + count + ", " + id1 + '-' +
                    idToCountMap.get(id1) + ", " + id2 + '-' + idToCountMap.get(id2));
        } else {
            System.out.println("rnf-" + count + ", " + id2 + '-' +
                    idToCountMap.get(id2) + ", " + id1 + '-' + idToCountMap.get(id1));
        }
        
    }
}
