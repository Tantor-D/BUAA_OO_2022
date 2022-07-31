package mycode;

import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

/*
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.HashSet;
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.HashSet;

public class MyPerson implements Person {
    /*@ public instance model int id;
      @ public instance model non_null String name;
      @ public instance model int age;
      @ public instance model non_null Person[] acquaintance;
      @ public instance model non_null int[] value;
      @ public instance model int money;
      @ public instance model int socialValue;
      @ public instance model non_null Message[] messages;
      @*/
    private final int id;
    private final String name;
    private final int age;
    private int father;
    private int money;
    private int socialValue;
    private final HashMap<Integer, Person> acquaintance;
    private final HashMap<Integer, Integer> values;
    private final LinkedList<Message> messages;
    
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.father = id;
        this.money = 0;
        this.socialValue = 0;
        acquaintance = new HashMap<>();
        values = new HashMap<>();
        messages = new LinkedList<>();
    }
    
    //@ ensures \result == id;
    public /*@ pure @*/ int getId() {
        return id;
    }
    
    //@ ensures \result.equals(name);
    public /*@ pure @*/ String getName() {
        return name;
    }
    
    //@ ensures \result == age;
    public /*@ pure @*/ int getAge() {
        return age;
    }
    
    /*@ also
      @ public normal_behavior
      @ requires obj != null && obj instanceof Person;
      @ assignable \nothing;
      @ ensures \result == (((Person) obj).getId() == id);
      @ also
      @ public normal_behavior
      @ requires obj == null || !(obj instanceof Person);
      @ assignable \nothing;
      @ ensures \result == false;
      @*/
    @Override
    public /*@ pure @*/ boolean equals(Object obj) {
        if (!(obj instanceof Person)) {
            return false;
        }
        return ((Person) obj).getId() == id;
    }
    
    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == (\exists int i; 0 <= i && i < acquaintance.length;
      @                     acquaintance[i].getId() == person.getId()) || person.getId() == id;
      @*/
    public /*@ pure @*/ boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        }
        return acquaintance.containsKey(person.getId());
    }
    
    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < acquaintance.length;
      @          acquaintance[i].getId() == person.getId());
      @ assignable \nothing;
      @ ensures (\exists int i; 0 <= i && i < acquaintance.length;
      @         acquaintance[i].getId() == person.getId() && \result == value[i]);
      @ also
      @ public normal_behavior
      @ requires (\forall int i; 0 <= i && i < acquaintance.length;
      @          acquaintance[i].getId() != person.getId());
      @ ensures \result == 0;
      @*/
    public /*@ pure @*/ int queryValue(Person person) {
        if (values.containsKey(person.getId())) {
            return values.get(person.getId());
        }
        return 0;
    }
    
    //@ also ensures \result == name.compareTo(p2.getName());
    public /*@ pure @*/ int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
    
    public void addAcquainted(Person person, int value, HashMap<Integer, Person> people) {
        acquaintance.put(person.getId(), person);
        values.put(person.getId(), value);
        
        MyPerson person2 = (MyPerson) person;
        int root1 = this.findFather(people);
        int root2 = person2.findFather(people);
        if (root1 != root2) {
            MyPerson fatherOfPerson2 = (MyPerson) people.get(root2);
            fatherOfPerson2.setFather(root1);
            MyNetwork.minusBlockNum();
        }
    }
    
    public int findFather(HashMap<Integer, Person> people) {
        if (father != id) {
            MyPerson myPerson = (MyPerson) people.get(father);
            father = myPerson.findFather(people);
        }
        return father;
    }
    
    /*@ public normal_behavior
      @ assignable socialValue;
      @ ensures socialValue == \old(socialValue) + num;
      @*/
    public void addSocialValue(int num) {
        socialValue += num;
    }
    
    //@ ensures \result == socialValue;
    public /*@ pure @*/ int getSocialValue() {
        return socialValue;
    }
    
    /*@ ensures (\result.size() == messages.length) &&
      @           (\forall int i; 0 <= i && i < messages.length;
      @             messages[i] == \result.get(i));
      @*/
    public /*@ pure @*/ List<Message> getMessages() {
        return messages; // 甚至不需要类型转换
    }
    
    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures (\forall int i; 0 <= i && i < messages.length && i <= 3;
      @           \result.contains(messages[i]) && \result.get(i) == messages[i]);
      @ ensures \result.size() == ((messages.length < 4)? messages.length: 4);
      @*/
    public /*@ pure @*/ List<Message> getReceivedMessages() {
        List<Message> list = new ArrayList<>();
        for (int i = 0; i < Math.min(messages.size(), 4); i++) {
            list.add(messages.get(i));
        }
        return list;
    }
    
    /*@ public normal_behavior
      @ assignable money;
      @ ensures money == \old(money) + num;
      @*/
    public void addMoney(int num) {
        money += num;
    }
    
    //@ ensures \result == money;
    public /*@ pure @*/ int getMoney() {
        return money;
    }
    
    public void setFather(int father) {
        this.father = father;
    }
    
    public void addMessageAtHead(Message msg) {
        messages.addFirst(msg);
    }
    
    public void renewNextNode(PriorityQueue<NextNode> nextNodes, HashSet<Integer> finishSet) {
        for (int personId: values.keySet()) {
            if (finishSet.contains(personId)) {
                continue;
            }
            nextNodes.add(new NextNode(personId,values.get(personId)));
        }
    }
}
