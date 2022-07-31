package mycode;
/*
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;

import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;*/

import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;

import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    /*@ public instance model non_null Person[] people;
      @ public instance model non_null Group[] groups;
      @ public instance model non_null Message[] messages;
      @*/
    private final HashMap<Integer, Person> peopleMap;
    private final HashMap<Integer, Group> groupMap;
    private final HashMap<Integer, Message> messageMap;
    private static int blockNum = 0;
    
    public MyNetwork() {
        peopleMap = new HashMap<>();
        groupMap = new HashMap<>();
        messageMap = new HashMap<>();
    }
    
    //@ ensures \result == (\exists int i; 0 <= i && i < people.length; people[i].getId() == id);
    public /*@ pure @*/ boolean contains(int id) { // for people, not for group
        return peopleMap.containsKey(id);
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
        if (peopleMap.containsKey(id)) {
            return peopleMap.get(id);
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
        if (peopleMap.containsKey(person.getId())) {
            throw new ExpEqualPersonIdException(person.getId());
        }
        blockNum++;
        peopleMap.put(person.getId(), person);
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
        if (!peopleMap.containsKey(id1)) {
            throw new ExpPersonIdNotFoundException(id1);
        }
        if (!peopleMap.containsKey(id2)) {
            throw new ExpPersonIdNotFoundException(id2);
        }
        if (peopleMap.get(id1).isLinked(peopleMap.get(id2))) { // 相同的人不能addRelation
            throw new ExpEqualRelationException(id1, id2);
        }
        MyPerson person1 = (MyPerson) peopleMap.get(id1);
        MyPerson person2 = (MyPerson) peopleMap.get(id2);
        person1.addAcquainted(person2, value, peopleMap);
        person2.addAcquainted(person1, value, peopleMap);
        for (Group group : groupMap.values()) {
            MyGroup thisGroup = (MyGroup) group;
            thisGroup.renewQgvsBecauseAp(id1, id2);
        }
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
        if (!peopleMap.containsKey(id1)) {
            throw new ExpPersonIdNotFoundException(id1);
        }
        if (!peopleMap.containsKey(id2)) {
            throw new ExpPersonIdNotFoundException(id2);
        }
        if (!peopleMap.get(id1).isLinked(peopleMap.get(id2))) {
            throw new ExpRelationNotFoundException(id1, id2);
        }
        return peopleMap.get(id1).queryValue(peopleMap.get(id2));
    }
    
    //@ ensures \result == people.length;
    public /*@ pure @*/ int queryPeopleSum() {
        return peopleMap.size();
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
        if (!peopleMap.containsKey(id1)) {
            throw new ExpPersonIdNotFoundException(id1);
        }
        if (!peopleMap.containsKey(id2)) {
            throw new ExpPersonIdNotFoundException(id2);
        }
        if (id1 == id2) {
            return true;
        }
        MyPerson person1 = (MyPerson) peopleMap.get(id1);
        MyPerson person2 = (MyPerson) peopleMap.get(id2);
        return person1.findFather(peopleMap) == person2.findFather(peopleMap);
    }
    
    /*@ ensures \result ==
      @         (\sum int i; 0 <= i && i < people.length &&
      @         (\forall int j; 0 <= j && j < i; !isCircle(people[i].getId(), people[j].getId()));
      @         1);
      @*/
    public /*@ pure @*/ int queryBlockSum() {
        return blockNum;
    }
    
    // 有JML描述，太丑了
    public /*@ pure @*/ int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!peopleMap.containsKey(id)) { // 最小生成树 prim算法
            throw new ExpPersonIdNotFoundException(id);
        }
        int ret = 0;
        PriorityQueue<NextNode> nextNodes = new PriorityQueue<>();
        HashSet<Integer> finishSet = new HashSet<>(); // false没找到，true找到了
        
        nextNodes.add(new NextNode(id, 0));
        while (!nextNodes.isEmpty()) {
            int thisId = nextNodes.element().getPersonId();
            
            if (finishSet.contains(thisId)) {
                nextNodes.remove();
                continue;
            }
            int thisDst = nextNodes.element().getDst();
            nextNodes.remove();
            
            finishSet.add(thisId);
            ret += thisDst;
            
            MyPerson thisPerson = (MyPerson) peopleMap.get(thisId);
            thisPerson.renewNextNode(nextNodes, finishSet);
        }
        return ret;
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
        if (groupMap.containsKey(group.getId())) {
            throw new ExpEqualGroupIdException(group.getId());
        }
        groupMap.put(group.getId(), group);
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
        if (groupMap.containsKey(id)) {
            return groupMap.get(id);
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
        if (!groupMap.containsKey(id2)) {
            throw new ExpGroupIdNotFoundException(id2);
        }
        if (!peopleMap.containsKey(id1)) {
            throw new ExpPersonIdNotFoundException(id1);
        }
        if (groupMap.get(id2).hasPerson(peopleMap.get(id1))) {
            throw new ExpEqualPersonIdException(id1);
        }
        if (groupMap.get(id2).getSize() >= 1111) {
            return;
        }
        groupMap.get(id2).addPerson(peopleMap.get(id1));
    }
    
    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id);
      @ ensures \result == getGroup(id).people.length;
      @ also
      @ public exceptional_behavior
      @ signals (GroupIdNotFoundException e) !(\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id);
      @*/
    public /*@ pure @*/ int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groupMap.containsKey(id)) {
            throw new ExpGroupIdNotFoundException(id);
        }
        return groupMap.get(id).getSize();
    }
    
    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id);
      @ ensures \result == getGroup(id).getValueSum();
      @ also
      @ public exceptional_behavior
      @ signals (GroupIdNotFoundException e) !(\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id);
      @*/
    public /*@ pure @*/ int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groupMap.containsKey(id)) {
            throw new ExpGroupIdNotFoundException(id);
        }
        return groupMap.get(id).getValueSum();
    }
    
    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id);
      @ ensures \result == getGroup(id).getAgeVar();
      @ also
      @ public exceptional_behavior
      @ signals (GroupIdNotFoundException e) !(\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id);
      @*/
    public /*@ pure @*/ int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groupMap.containsKey(id)) {
            throw new ExpGroupIdNotFoundException(id);
        }
        return groupMap.get(id).getAgeVar();
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
        if (!groupMap.containsKey(id2)) {
            throw new ExpGroupIdNotFoundException(id2);
        }
        if (!peopleMap.containsKey(id1)) {
            throw new ExpPersonIdNotFoundException(id1);
        }
        if (!groupMap.get(id2).hasPerson(peopleMap.get(id1))) {
            throw new ExpEqualPersonIdException(id1);
        }
        groupMap.get(id2).delPerson(peopleMap.get(id1));
    }
    
    //@ ensures \result == (\exists int i;
    // 0 <= i && i < messages.length; messages[i].getId() == id);
    public /*@ pure @*/ boolean containsMessage(int id) {
        return messageMap.containsKey(id);
    }
    
    // 有JML，删了
    public void addMessage(Message message) throws
            EqualMessageIdException, EqualPersonIdException {
        if (messageMap.containsKey(message.getId())) {
            throw new ExpEqualMessageIdException(message.getId());
        }
        if (message.getType() == 0 &&
                message.getPerson1().getId() == message.getPerson2().getId()) {
            throw new ExpEqualPersonIdException(message.getPerson1().getId());
        }
        messageMap.put(message.getId(), message);
    }
    
    /*@ public normal_behavior
      @ requires containsMessage(id);
      @ ensures (\exists int i; 0 <= i && i < messages.length; messages[i].getId() == id &&
      @         \result == messages[i]);
      @ public normal_behavior
      @ requires !containsMessage(id);
      @ ensures \result == null;
      @*/
    public /*@ pure @*/ Message getMessage(int id) {
        if (!messageMap.containsKey(id)) {
            return null;
        }
        return messageMap.get(id);
    }
    
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (!messageMap.containsKey(id)) {
            throw new ExpMessageIdNotFoundException(id);
        }
        if (messageMap.get(id).getType() == 0 &&
                !(messageMap.get(id).getPerson1().isLinked(messageMap.get(id).getPerson2()))) {
            throw new ExpRelationNotFoundException(
                    messageMap.get(id).getPerson1().getId(),
                    messageMap.get(id).getPerson2().getId());
        }
        if (messageMap.get(id).getType() == 1 &&
                !(messageMap.get(id).getGroup().hasPerson(messageMap.get(id).getPerson1()))) {
            throw new ExpPersonIdNotFoundException(messageMap.get(id).getPerson1().getId());
        }
        
        if (messageMap.get(id).getType() == 0) {
            Message thisMsg = messageMap.get(id);
            MyPerson person1 = (MyPerson) thisMsg.getPerson1();
            MyPerson person2 = (MyPerson) thisMsg.getPerson2();
            
            person1.addSocialValue(thisMsg.getSocialValue());
            person2.addSocialValue(thisMsg.getSocialValue());
            person2.addMessageAtHead(thisMsg);
            
            messageMap.remove(id);
        } else if (messageMap.get(id).getType() == 1) {
            MyGroup thisGroup = (MyGroup) messageMap.get(id).getGroup();
            thisGroup.addSocialValue(messageMap.get(id).getSocialValue());
            
            messageMap.remove(id);
        }
    }
    
    /*@ public normal_behavior
      @ requires contains(id);
      @ ensures \result == getPerson(id).getSocialValue();
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(id);
      @*/
    public /*@ pure @*/ int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!peopleMap.containsKey(id)) {
            throw new ExpPersonIdNotFoundException(id);
        }
        return peopleMap.get(id).getSocialValue();
    }
    
    /*@ public normal_behavior
      @ requires contains(id);
      @ ensures \result == getPerson(id).getReceivedMessages();
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(id);
      @*/
    public /*@ pure @*/ List<Message> queryReceivedMessages(int id)
            throws PersonIdNotFoundException {
        if (!peopleMap.containsKey(id)) {
            throw new ExpPersonIdNotFoundException(id);
        }
        return peopleMap.get(id).getReceivedMessages();
    }
    
    public static void minusBlockNum() {
        blockNum--;
    }
    
}
