package mycode;

import com.oocourse.spec3.exceptions.EqualEmojiIdException;

import java.util.HashMap;

public class ExpEqualEmojiIdException extends EqualEmojiIdException {
    private static int count = 0;
    private static HashMap<Integer, Integer> idToCountMap = new HashMap<>();
    private final int id;
    
    public ExpEqualEmojiIdException(int id) {
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
        System.out.println("eei-" + count + ", " + id + "-" + idToCountMap.get(id));
    }
}
