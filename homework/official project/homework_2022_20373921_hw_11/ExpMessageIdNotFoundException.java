package mycode;

import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class ExpMessageIdNotFoundException extends MessageIdNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> idToCountMap = new HashMap<>();
    private int id;
    
    public ExpMessageIdNotFoundException(int id) {
        this.id = id;
        count++;
        if (idToCountMap.containsKey(id)) {
            int prev = idToCountMap.get(id);
            idToCountMap.put(id, prev + 1);
        } else {
            idToCountMap.put(id, 1);
        }
    }
    
    @Override
    public void print() {
        System.out.println("minf-" + count + ", " + id + "-" + idToCountMap.get(id));
    }
}
