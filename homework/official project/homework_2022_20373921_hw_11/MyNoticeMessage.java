package mycode;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

public class MyNoticeMessage extends MyMessage implements NoticeMessage {
    //@ public instance model non_null String string;
    
    //@ public invariant socialValue == string.length();
    
    //@ ensures \result == string;
    private String string;
    
    public /*@ pure @*/ String getString() {
        return string;
    }
    
    public MyNoticeMessage(int messageId,
                           String noticeString,
                           Person messagePerson1,
                           Person messagePerson2) {
        super(messageId, noticeString.length(), messagePerson1, messagePerson2);
        string = noticeString;
    }
    
    public MyNoticeMessage(int messageId,
                           String noticeString,
                           Person messagePerson1,
                           Group messageGroup) {
        super(messageId, noticeString.length(), messagePerson1, messageGroup);
        string = noticeString;
    }
}
