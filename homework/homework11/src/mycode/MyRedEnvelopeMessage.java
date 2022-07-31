package mycode;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

public class MyRedEnvelopeMessage extends MyMessage implements RedEnvelopeMessage {
    //@ public instance model int money;
    //@ public invariant socialValue == money * 5;
    //@ ensures \result == money;
    
    private final int money;
    
    public /*@ pure @*/ int getMoney() {
        return money;
    }
    
    public MyRedEnvelopeMessage(int messageId,
                                int luckyMoney,
                                Person messagePerson1,
                                Person messagePerson2) {
        super(messageId, luckyMoney * 5, messagePerson1, messagePerson2);
        money = luckyMoney;
    }
    
    public MyRedEnvelopeMessage(int messageId,
                                int luckyMoney,
                                Person messagePerson1,
                                Group messageGroup) {
        super(messageId, luckyMoney * 5, messagePerson1, messageGroup);
        money = luckyMoney;
    }
}
