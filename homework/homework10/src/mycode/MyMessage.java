package mycode;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

public class MyMessage implements Message {
    /*@ public instance model non_null int id;
      @ public instance model non_null int socialValue;
      @ public instance model non_null int type;
      @ public instance model non_null Person person1;
      @ public instance model nullable Person person2;
      @ public instance model nullable Group group;
      @*/
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;
    private final Person person2;
    private final Group group;
    
    /*@ ensures type == 0;
      @ ensures group == null;
      @ ensures id == messageId;
      @ ensures socialValue == messageSocialValue;
      @ ensures person1 == messagePerson1;
      @ ensures person2 == messagePerson2;
      @*/
    public MyMessage(
            int messageId, int messageSocialValue, Person messagePerson1, Person messagePerson2) {
        this.type = 0; // receiver is person
        this.group = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
    }
    
    /*@ ensures type == 1;
      @ ensures person2 == null;
      @ ensures id == messageId;
      @ ensures socialValue == messageSocialValue;
      @ ensures person1 == messagePerson1;
      @ ensures group == messageGroup;
      @*/
    public MyMessage(
            int messageId, int messageSocialValue, Person messagePerson1, Group messageGroup) {
        this.type = 1;
        this.person2 = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = messagePerson1;
        this.group = messageGroup;
    }
    
    //@ ensures \result == type;
    public /*@ pure @*/ int getType() {
        return type;
    }
    
    //@ ensures \result == id;
    public /*@ pure @*/ int getId() {
        return id;
    }
    
    //@ ensures \result == socialValue;
    public /*@ pure @*/ int getSocialValue() {
        return socialValue;
    }
    
    //@ ensures \result == person1;
    public /*@ pure @*/ Person getPerson1() {
        return person1;
    }
    
    /*@ requires person2 != null;
      @ ensures \result == person2;
      @*/
    public /*@ pure @*/ Person getPerson2() {
        return person2;
    }
    
    /*@ requires group != null;
      @ ensures \result == group;
      @*/
    public /*@ pure @*/ Group getGroup() {
        return group;
    }
    
    /*@ also
      @ public normal_behavior
      @ requires obj != null && obj instanceof Message;
      @ assignable \nothing;
      @ ensures \result == (((Message) obj).getId() == id);
      @ also
      @ public normal_behavior
      @ requires obj == null || !(obj instanceof Message);
      @ assignable \nothing;
      @ ensures \result == false;
      @*/
    public /*@ pure @*/ boolean equals(Object obj) {
        if (!(obj instanceof Message)) {
            return false;
        }
        return ((Message) obj).getId() == id;
    }
}
