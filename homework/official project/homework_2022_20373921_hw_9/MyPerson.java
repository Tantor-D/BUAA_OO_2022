package com.mycode;

import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    /*@ public instance model int id;
      @ public instance model non_null String name;
      @ public instance model int age;
      @ public instance model non_null Person[] acquaintance;
      @ public instance model non_null int[] value;
      @*/
    private int id;
    private String name;
    private int age;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Integer, Integer> values;
    private int father;
    //private ArrayList<Person> acquaintance;
    //private ArrayList<Integer> value;
    
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.father = id;
        //acquaintance = new ArrayList<>();
        //value = new ArrayList<>();
        acquaintance = new HashMap<>();
        values = new HashMap<>();
    }
    
    //@ ensures \result == id;
    public /*@ pure @*/ int getId() {
        return id;
    }
    
    //@ ensures \result.equals(name);
    public /*@ pure @*/ String getName() {
        // todo 最后看看有没有深拷贝的必要
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
    @Override // todo 这个需不需要写
    public /*@ pure @*/ boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Person)) {
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
    
    public void setFather(int father) {
        this.father = father;
    }
    
    public int acquaintanceSize() {
        return acquaintance.size();
    }
}
