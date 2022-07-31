package mycode;

import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

public class MyEmojiMessage extends MyMessage implements EmojiMessage {
    //@ public instance model int emojiId;
    //@ public invariant socialValue == emojiId;
    
    private final int emojiId;
    
    //@ ensures \result == emojiId;
    public /*@ pure @*/ int getEmojiId() {
        return emojiId;
    }
    
    public MyEmojiMessage(
            int messageId, int emojiNumber, Person messagePerson1, Person messagePerson2) {
        super(messageId, emojiNumber, messagePerson1, messagePerson2);
        emojiId = emojiNumber;
    }
    
    public MyEmojiMessage(
            int messageId, int emojiNumber, Person messagePerson1, Group messageGroup) {
        super(messageId,emojiNumber,messagePerson1,messageGroup);
        emojiId = emojiNumber;
    }
}

