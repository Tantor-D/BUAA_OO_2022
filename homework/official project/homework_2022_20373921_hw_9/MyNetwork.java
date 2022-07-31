package com.mycode;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;

import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyNetwork implements Network {
    /*@ public instance model non_null Person[] people;
      @ public instance model non_null Group[] groups;
      @*/
    private HashMap<Integer, Person> people;
    private HashMap<Integer, Group> groups;
    private static int blockNum = 0;
    
    public MyNetwork() {
        people = new HashMap<>();
        groups = new HashMap<>();
    }
    
    //@ ensures \result == (\exists int i; 0 <= i && i < people.length; people[i].getId() == id);
    public /*@ pure @*/ boolean contains(int id) { // for people, not for group
        return people.containsKey(id);
    }
    
    /*@ public normal_behavior
      @ requires contains(id);
      @ ensures (\exists int i; 0 <= i && i < people.length; people[i].getId() == id &&
      @         \result == people[i]);
      @ also
      @ public normal_behavior
      @ requires !contains(id);
      @ ensures \result == null;
      @*/
    public /*@ pure @*/ Person getPerson(int id) {
        if (people.containsKey(id)) {
            return people.get(id);
        }
        return null;
    }
    
    /*@ public normal_behavior
      @ requires !(\exists int i; 0 <= i && i < people.length; people[i].equals(person));
      @ assignable people;
      @ ensures people.length == \old(people.length) + 1;
      @ ensures (\forall int i; 0 <= i && i < \old(people.length);
      @          (\exists int j; 0 <= j && j < people.length; people[j] == (\old(people[i]))));
      @ ensures (\exists int i; 0 <= i && i < people.length; people[i] == person);
      @ also
      @ public exceptional_behavior
      @ signals (EqualPersonIdException e) (\exists int i; 0 <= i && i < people.length;
      @                                     people[i].equals(person));
      @*/
    public void addPerson(/*@ non_null @*/Person person) throws EqualPersonIdException {
        if (people.containsKey(person.getId())) {
            throw new ExpEqualPersonIdException(person.getId());
        }
        blockNum++;
        people.put(person.getId(), person);
    }
    
    /*@ public normal_behavior
      @ requires contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2));
      @ assignable people;
      @ ensures people.length == \old(people.length);
      @ ensures (\forall int i; 0 <= i && i < \old(people.length);
      @          (\exists int j; 0 <= j && j < people.length; people[j] == \old(people[i])));
      @ ensures (\forall int i; 0 <= i && i < people.length && \old(people[i].getId()) != id1 &&
      @     \old(people[i].getId()) != id2; \not_assigned(people[i]));
      @ ensures getPerson(id1).isLinked(getPerson(id2)) && getPerson(id2).isLinked(getPerson(id1));
      @ ensures getPerson(id1).queryValue(getPerson(id2)) == value;
      @ ensures getPerson(id2).queryValue(getPerson(id1)) == value;
      @ ensures (\forall int i; 0 <= i && i < \old(getPerson(id1).acquaintance.length);
      @         \old(getPerson(id1).acquaintance[i]) == getPerson(id1).acquaintance[i] &&
      @          \old(getPerson(id1).value[i]) == getPerson(id1).value[i]);
      @ ensures (\forall int i; 0 <= i && i < \old(getPerson(id2).acquaintance.length);
      @         \old(getPerson(id2).acquaintance[i]) == getPerson(id2).acquaintance[i] &&
      @          \old(getPerson(id2).value[i]) == getPerson(id2).value[i]);
      @ ensures getPerson(id1).value.length == getPerson(id1).acquaintance.length;
      @ ensures getPerson(id2).value.length == getPerson(id2).acquaintance.length;
      @ ensures \old(getPerson(id1).value.length) == getPerson(id1).acquaintance.length - 1;
      @ ensures \old(getPerson(id2).value.length) == getPerson(id2).acquaintance.length - 1;
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ requires !contains(id1) || !contains(id2) || getPerson(id1).isLinked(getPerson(id2));
      @ signals (PersonIdNotFoundException e) !contains(id1);
      @ signals (PersonIdNotFoundException e) contains(id1) && !contains(id2);
      @ signals (EqualRelationException e) contains(id1) && contains(id2) &&
      @         getPerson(id1).isLinked(getPerson(id2));
      @*/
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (!people.containsKey(id1)) {
            throw new ExpPersonIdNotFoundException(id1);
        }
        if (!people.containsKey(id2)) {
            throw new ExpPersonIdNotFoundException(id2);
        }
        if (people.get(id1).isLinked(people.get(id2))) {
            throw new ExpEqualRelationException(id1, id2);
        }
        MyPerson person1 = (MyPerson) people.get(id1);
        MyPerson person2 = (MyPerson) people.get(id2);
        person1.addAcquainted(person2, value, people);
        person2.addAcquainted(person1, value, people);
    }
    
    /*@ public normal_behavior
      @ requires contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2));
      @ ensures \result == getPerson(id1).queryValue(getPerson(id2));
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(id1);
      @ signals (PersonIdNotFoundException e) contains(id1) && !contains(id2);
      @ signals (RelationNotFoundException e) contains(id1) && contains(id2) &&
      @         !getPerson(id1).isLinked(getPerson(id2));
      @*/
    public /*@ pure @*/ int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (!people.containsKey(id1)) {
            throw new ExpPersonIdNotFoundException(id1);
        }
        if (!people.containsKey(id2)) {
            throw new ExpPersonIdNotFoundException(id2);
        }
        if (!people.get(id1).isLinked(people.get(id2))) {
            throw new ExpRelationNotFoundException(id1, id2);
        }
        return people.get(id1).queryValue(people.get(id2));
    }
    
    //@ ensures \result == people.length;
    public /*@ pure @*/ int queryPeopleSum() {
        return people.size();
    }
    
    /*@ public normal_behavior
      @ requires contains(id1) && contains(id2);
      @ ensures \result == (\exists Person[] array; array.length >= 2;
      @                     array[0].equals(getPerson(id1)) &&
      @                     array[array.length - 1].equals(getPerson(id2)) &&
      @                      (\forall int i; 0 <= i && i < array.length - 1;
      @                      array[i].isLinked(array[i + 1]) == true));
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(id1);
      @ signals (PersonIdNotFoundException e) contains(id1) && !contains(id2);
      @*/
    public /*@ pure @*/ boolean isCircle(int id1, int id2) throws PersonIdNotFoundException { // 并查集
        if (!people.containsKey(id1)) {
            throw new ExpPersonIdNotFoundException(id1);
        }
        if (!people.containsKey(id2)) {
            throw new ExpPersonIdNotFoundException(id2);
        }
        if (id1 == id2) {
            return true;
        }
        MyPerson person1 = (MyPerson) people.get(id1);
        MyPerson person2 = (MyPerson) people.get(id2);
        return person1.findFather(people) == person2.findFather(people);
        /*int fa1 = person1.findFather(people);
        int fa2 = person2.findFather(people);
        System.out.println("id = " + person1.getId() + " ,  fa = " + fa1);
        System.out.println("id = " + person2.getId() + " ,  fa = " + fa2);
        System.out.println("return = " + (fa1 == fa2));
        return fa1 == fa2;*/
    }
    
    /*@ ensures \result ==
      @         (\sum int i; 0 <= i && i < people.length &&
      @         (\forall int j; 0 <= j && j < i; !isCircle(people[i].getId(), people[j].getId()));
      @         1);
      @*/
    public /*@ pure @*/ int queryBlockSum() {
        return blockNum;
    }
    
    /*@ public normal_behavior
      @ requires !(\exists int i; 0 <= i && i < groups.length; groups[i].equals(group));
      @ assignable groups;
      @ ensures groups.length == \old(groups.length) + 1;
      @ ensures (\forall int i; 0 <= i && i < \old(groups.length);
      @          (\exists int j; 0 <= j && j < groups.length; groups[j] == (\old(groups[i]))));
      @ ensures (\exists int i; 0 <= i && i < groups.length; groups[i] == group);
      @ also
      @ public exceptional_behavior
      @ signals (EqualGroupIdException e) (\exists int i; 0 <= i && i < groups.length;
      @                                     groups[i].equals(group));
      @*/
    public void addGroup(/*@ non_null @*/Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new ExpEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
    }
    
    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id);
      @ ensures (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id &&
      @         \result == groups[i]);
      @ also
      @ public normal_behavior
      @ requires (\forall int i; 0 <= i && i < groups.length; groups[i].getId() != id);
      @ ensures \result == null;
      @*/
    public /*@ pure @*/ Group getGroup(int id) {
        if (groups.containsKey(id)) {
            return groups.get(id);
        }
        return null;
    }
    
    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id2) &&
      @           (\exists int i; 0 <= i && i < people.length; people[i].getId() == id1) &&
      @            getGroup(id2).hasPerson(getPerson(id1)) == false &&
      @             getGroup(id2).people.length < 1111;
      @ assignable getGroup(id2).people;
      @ ensures (\forall Person i; \old(getGroup(id2).hasPerson(i));
      @          getGroup(id2).hasPerson(i));
      @ ensures \old(getGroup(id2).people.length) == getGroup(id2).people.length - 1;
      @ ensures getGroup(id2).hasPerson(getPerson(id1));
      @ also
      @ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id2) &&
      @           (\exists int i; 0 <= i && i < people.length; people[i].getId() == id1) &&
      @            getGroup(id2).hasPerson(getPerson(id1)) == false &&
      @             getGroup(id2).people.length >= 1111;
      @ assignable \nothing;
      @ also
      @ public exceptional_behavior
      @ signals (GroupIdNotFoundException e) !(\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2);
      @ signals (PersonIdNotFoundException e) (\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2) && !(\exists int i; 0 <= i && i < people.length;
      @           people[i].getId() == id1);
      @ signals (EqualPersonIdException e) (\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2) && (\exists int i; 0 <= i && i < people.length;
      @           people[i].getId() == id1) && getGroup(id2).hasPerson(getPerson(id1));
      @*/
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new ExpGroupIdNotFoundException(id2);
        }
        if (!people.containsKey(id1)) {
            throw new ExpPersonIdNotFoundException(id1);
        }
        if (groups.get(id2).hasPerson(people.get(id1))) {
            throw new ExpEqualPersonIdException(id1);
        }
        if (groups.get(id2).getSize() >= 1111) {
            return;
        }
        groups.get(id2).addPerson(people.get(id1));
    }
    
    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id2) &&
      @           (\exists int i; 0 <= i && i < people.length; people[i].getId() == id1) &&
      @            getGroup(id2).hasPerson(getPerson(id1)) == true;
      @ assignable getGroup(id2).people;
      @ ensures (\forall Person i; getGroup(id2).hasPerson(i);
      @          \old(getGroup(id2).hasPerson(i)));
      @ ensures \old(getGroup(id2).people.length) == getGroup(id2).people.length + 1;
      @ ensures getGroup(id2).hasPerson(getPerson(id1)) == false;
      @ also
      @ public exceptional_behavior
      @ signals (GroupIdNotFoundException e) !(\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2);
      @ signals (PersonIdNotFoundException e) (\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2) && !(\exists int i; 0 <= i && i < people.length;
      @           people[i].getId() == id1);
      @ signals (EqualPersonIdException e) (\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2) && (\exists int i; 0 <= i && i < people.length;
      @           people[i].getId() == id1) && !getGroup(id2).hasPerson(getPerson(id1));
      @*/
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new ExpGroupIdNotFoundException(id2);
        }
        if (!people.containsKey(id1)) {
            throw new ExpPersonIdNotFoundException(id1);
        }
        if (!groups.get(id2).hasPerson(people.get(id1))) {
            throw new ExpEqualPersonIdException(id1);
        }
        groups.get(id2).delPerson(people.get(id1));
    }
    
    public static void minusBlockNum() {
        blockNum--;
    }
}
