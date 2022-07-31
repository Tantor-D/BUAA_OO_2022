package mycode;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.math.BigInteger;
import java.util.HashMap;

public class MyGroup implements Group {
    /*@ public instance model int id;
      @ public instance model non_null Person[] people;
      @*/
    private final int id;
    private final HashMap<Integer, Person> people;
    private int retForQgvs;
    
    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
        this.retForQgvs = 0;
    }
    
    //@ ensures \result == id;
    public /*@ pure @*/ int getId() {
        return id;
    }
    
    /*@ also
      @ public normal_behavior
      @ requires obj != null && obj instanceof Group;
      @ assignable \nothing;
      @ ensures \result == (((Group) obj).getId() == id);
      @ also
      @ public normal_behavior
      @ requires obj == null || !(obj instanceof Group);
      @ assignable \nothing;
      @ ensures \result == false;
      @*/
    public /*@ pure @*/ boolean equals(Object obj) {
        if (!(obj instanceof Group)) {
            return false;
        }
        return ((Group) obj).getId() == id; // id是每个group唯一的
    }
    
    /*@ public normal_behavior
      @ requires !hasPerson(person);
      @ assignable people;
      @ ensures (\forall Person p; \old(hasPerson(p)); hasPerson(p));
      @ ensures \old(people.length) == people.length - 1;
      @ ensures hasPerson(person);
      @*/
    public void addPerson(/*@ non_null @*/Person person) { // JML已经要求了以前不存在这个person
        for (Person person1 : people.values()) {
            retForQgvs += 2 * person1.queryValue(person);
        }
        people.put(person.getId(), person);
    }
    
    //@ ensures \result == (\exists int i; 0 <= i && i < people.length; people[i].equals(person));
    public /*@ pure @*/ boolean hasPerson(Person person) {
        return people.containsValue(person);
    }
    
    /*@ ensures \result == (\sum int i; 0 <= i && i < people.length;
      @          (\sum int j; 0 <= j && j < people.length &&
      @           people[i].isLinked(people[j]); people[i].queryValue(people[j])));
      @*/
    public /*@ pure @*/ int getValueSum() {
        return this.retForQgvs;
    }
    
    /*@ ensures \result == (people.length == 0? 0:
      @          ((\sum int i; 0 <= i && i < people.length; people[i].getAge()) / people.length));
      @*/
    public /*@ pure @*/ int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        BigInteger ret = new BigInteger("0");
        for (Person person : people.values()) {
            ret = ret.add(BigInteger.valueOf(person.getAge()));
        }
        return ret.divide(BigInteger.valueOf(people.size())).intValue();
    }
    
    /*@ ensures \result == (people.length == 0? 0 : ((\sum int i; 0 <= i && i < people.length;
      @          (people[i].getAge() - getAgeMean()) * (people[i].getAge() - getAgeMean())) /
      @           people.length));
      @*/
    public /*@ pure @*/ int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        BigInteger ret = new BigInteger("0");
        int ave = getAgeMean();
        BigInteger now;
        for (Person person : people.values()) {
            now = new BigInteger(String.valueOf(person.getAge() - ave));
            now = now.multiply(now);
            ret = ret.add(now);
        }
        return ret.divide(BigInteger.valueOf(people.size())).intValue();
    }
    
    /*@ public normal_behavior
      @ requires hasPerson(person) == true;
      @ assignable people;
      @ ensures (\forall Person p; hasPerson(p); \old(hasPerson(p)));
      @ ensures \old(people.length) == people.length + 1;
      @ ensures hasPerson(person) == false;
      @*/
    public void delPerson(/*@ non_null @*/Person person) {
        people.remove(person.getId());
        for (Person person1 : people.values()) {
            retForQgvs -= 2 * person.queryValue(person1);
        }
    }
    
    //@ ensures \result == people.length;
    public /*@ pure @*/ int getSize() {
        return people.size();
    }
    
    public void addSocialValue(int num) {
        for (Person person : people.values()) {
            person.addSocialValue(num);
        }
    }
    
    public void renewQgvsBecauseAp(int id1, int id2) {
        if (!people.containsKey(id1) || !people.containsKey(id2) || id1 == id2) {
            return;
        }
        retForQgvs += 2 * people.get(id1).queryValue(people.get(id2));
    }
}
