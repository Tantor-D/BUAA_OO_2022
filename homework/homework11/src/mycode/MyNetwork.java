package mycode;
/*
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.RedEnvelopeMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

*/

import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.RedEnvelopeMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> peopleMap;
    private final HashMap<Integer, Group> groupMap;
    private final HashMap<Integer, Message> messageMap;
    private final HashMap<Integer, Integer> emojiMap;
    private static int blockNum = 0;
    
    public MyNetwork() {
        peopleMap = new HashMap<>();
        groupMap = new HashMap<>();
        messageMap = new HashMap<>();
        emojiMap = new HashMap<>();
    }
    
    public /*@ pure @*/ boolean contains(int id) { // for people, not for group
        return peopleMap.containsKey(id);
    }
    
    public /*@ pure @*/ Person getPerson(int id) {
        if (peopleMap.containsKey(id)) {
            return peopleMap.get(id);
        }
        return null;
    }
    
    public void addPerson(/*@ non_null @*/Person person) throws EqualPersonIdException {
        if (peopleMap.containsKey(person.getId())) {
            throw new ExpEqualPersonIdException(person.getId());
        }
        blockNum++;
        peopleMap.put(person.getId(), person);
    }
    
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
    
    public /*@ pure @*/ int queryPeopleSum() {
        return peopleMap.size();
    }
    
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
            thisPerson.renewNextNodeForPrim(nextNodes, finishSet);
        }
        return ret;
    }
    
    public void addGroup(/*@ non_null @*/Group group) throws EqualGroupIdException {
        if (groupMap.containsKey(group.getId())) {
            throw new ExpEqualGroupIdException(group.getId());
        }
        groupMap.put(group.getId(), group);
    }
    
    public /*@ pure @*/ Group getGroup(int id) {
        if (groupMap.containsKey(id)) {
            return groupMap.get(id);
        }
        return null;
    }
    
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
    
    public /*@ pure @*/ int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groupMap.containsKey(id)) {
            throw new ExpGroupIdNotFoundException(id);
        }
        return groupMap.get(id).getSize();
    }
    
    public /*@ pure @*/ int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groupMap.containsKey(id)) {
            throw new ExpGroupIdNotFoundException(id);
        }
        return groupMap.get(id).getValueSum();
    }
    
    public /*@ pure @*/ int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groupMap.containsKey(id)) {
            throw new ExpGroupIdNotFoundException(id);
        }
        return groupMap.get(id).getAgeVar();
    }
    
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
    
    public /*@ pure @*/ boolean containsMessage(int id) {
        return messageMap.containsKey(id);
    }
    
    // 这个是有不同的,已修改
    public void addMessage(Message message) throws
            EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        if (messageMap.containsKey(message.getId())) {
            throw new ExpEqualMessageIdException(message.getId());
        }
        if ((message instanceof EmojiMessage) &&
                !emojiMap.containsKey(((EmojiMessage) message).getEmojiId())) {
            throw new ExpEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        }
        if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new ExpEqualPersonIdException(message.getPerson1().getId());
        }
        
        if (message.getType() == 0 &&
                message.getPerson1().getId() == message.getPerson2().getId()) {
            throw new ExpEqualPersonIdException(message.getPerson1().getId());
        }
        messageMap.put(message.getId(), message);
    }
    
    public /*@ pure @*/ Message getMessage(int id) {
        if (!messageMap.containsKey(id)) {
            return null;
        }
        return messageMap.get(id);
    }
    
    // 有不同，已修改
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
            messageMap.remove(id);
            
            MyPerson person1 = (MyPerson) thisMsg.getPerson1();
            MyPerson person2 = (MyPerson) thisMsg.getPerson2();
            
            person1.addSocialValue(thisMsg.getSocialValue());
            person2.addSocialValue(thisMsg.getSocialValue());
            person2.addMessageAtHead(thisMsg);
            
            if (thisMsg instanceof RedEnvelopeMessage) {
                person1.addMoney(-1 * ((RedEnvelopeMessage) thisMsg).getMoney());
                person2.addMoney(((RedEnvelopeMessage) thisMsg).getMoney());
            }
            if (thisMsg instanceof EmojiMessage) {
                int id1 = ((EmojiMessage) thisMsg).getEmojiId();
                if (emojiMap.containsKey(id1)) { // 应该是不需要的，只是保险加上
                    emojiMap.put(id1, emojiMap.get(id1) + 1);
                }
            }
            
        } else if (messageMap.get(id).getType() == 1) {
            Message thisMsg = messageMap.get(id);
            messageMap.remove(id);
            
            MyGroup thisGroup = (MyGroup) thisMsg.getGroup();
            thisGroup.addSocialValue(thisMsg.getSocialValue());
            
            if (thisMsg instanceof RedEnvelopeMessage) {
                thisGroup.personSendMoney(thisMsg.getPerson1().getId(),
                        ((RedEnvelopeMessage) thisMsg).getMoney());
            }
            if (thisMsg instanceof EmojiMessage) {
                int id1 = ((EmojiMessage) thisMsg).getEmojiId();
                if (emojiMap.containsKey(id1)) { // 应该是不需要的，只是保险加上
                    emojiMap.put(id1, emojiMap.get(id1) + 1);
                }
            }
        }
    }
    
    public /*@ pure @*/ int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!peopleMap.containsKey(id)) {
            throw new ExpPersonIdNotFoundException(id);
        }
        return peopleMap.get(id).getSocialValue();
    }
    
    public /*@ pure @*/ List<Message> queryReceivedMessages(int id)
            throws PersonIdNotFoundException {
        if (!peopleMap.containsKey(id)) {
            throw new ExpPersonIdNotFoundException(id);
        }
        return peopleMap.get(id).getReceivedMessages();
    }
    
    // --------------------------------------------------------新加的都在底下
    public /*@ pure @*/ boolean containsEmojiId(int id) {
        return emojiMap.containsKey(id);
    }
    
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojiMap.containsKey(id)) {
            throw new ExpEqualEmojiIdException(id);
        }
        emojiMap.put(id, 0);
    }
    
    public /*@ pure @*/ int queryMoney(int id) throws PersonIdNotFoundException {
        if (!peopleMap.containsKey(id)) {
            throw new ExpPersonIdNotFoundException(id);
        }
        return peopleMap.get(id).getMoney();
    }
    
    public /*@ pure @*/ int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!emojiMap.containsKey(id)) {
            throw new ExpEmojiIdNotFoundException(id);
        }
        return emojiMap.get(id);
    }
    
    public int deleteColdEmoji(int limit) {
        Iterator<Map.Entry<Integer, Integer>> it1 = emojiMap.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<Integer, Integer> item = it1.next();
            if (item.getValue() < limit) {
                it1.remove();
            }
        }
        
        Iterator<Map.Entry<Integer, Message>> it2 = messageMap.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<Integer, Message> item = it2.next();
            Message thisMessage = item.getValue();
            if (thisMessage instanceof EmojiMessage) {
                if (!emojiMap.containsKey(((EmojiMessage) thisMessage).getEmojiId())) {
                    it2.remove();
                }
            }
        }
        return emojiMap.size();
    }
    
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!peopleMap.containsKey(personId)) {
            throw new ExpPersonIdNotFoundException(personId);
        }
        ((MyPerson) peopleMap.get(personId)).clearNotices();
    }
    
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!messageMap.containsKey(id) ||
                (messageMap.containsKey(id) && messageMap.get(id).getType() == 1)) {
            throw new ExpMessageIdNotFoundException(id);
        }
        
        Message thisMsg = messageMap.get(id);
        
        boolean flagCircle;
        try {
            flagCircle = isCircle(thisMsg.getPerson1().getId(), thisMsg.getPerson2().getId());
        } catch (PersonIdNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (!flagCircle) { // 这种情况下不需要删除
            return -1;
        }
        
        messageMap.remove(id);
        
        final int ret = dijkstra(thisMsg.getPerson1().getId(), thisMsg.getPerson2().getId());
        
        MyPerson person1 = (MyPerson) thisMsg.getPerson1();
        MyPerson person2 = (MyPerson) thisMsg.getPerson2();
        
        person1.addSocialValue(thisMsg.getSocialValue());
        person2.addSocialValue(thisMsg.getSocialValue());
        person2.addMessageAtHead(thisMsg);
        
        if (thisMsg instanceof RedEnvelopeMessage) {
            person1.addMoney(-1 * ((RedEnvelopeMessage) thisMsg).getMoney());
            person2.addMoney(((RedEnvelopeMessage) thisMsg).getMoney());
        }
        if (thisMsg instanceof EmojiMessage) {
            int id1 = ((EmojiMessage) thisMsg).getEmojiId();
            if (emojiMap.containsKey(id1)) { // 应该是不需要的，只是保险加上
                emojiMap.put(id1, emojiMap.get(id1) + 1);
            }
        }
        return ret;
    }
    
    public static void minusBlockNum() {
        blockNum--;
    }
    
    private int dijkstra(int id1, int id2) {
        if (id1 == id2) {
            return 0;
        }
        
        PriorityQueue<NextNode> nextNodes = new PriorityQueue<>();
        HashSet<Integer> finishSet = new HashSet<>();
        nextNodes.add(new NextNode(id1, 0));
        while (!nextNodes.isEmpty()) {
            int nowId = nextNodes.element().getPersonId();
            int nowDis = nextNodes.element().getDst();
            nextNodes.remove();
            
            if (!finishSet.contains(nowId)) {
                finishSet.add(nowId);
                if (nowId == id2) {
                    return nowDis;
                }
                ((MyPerson) peopleMap.get(nowId)).
                        renewNextNodeForDijkstra(nextNodes, finishSet, nowDis);
            }
        }
        return -1;
    }
    
}
