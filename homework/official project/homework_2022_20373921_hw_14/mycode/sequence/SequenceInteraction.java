package mycode.sequence;

import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;
import mycode.Tools;

import java.util.HashMap;
import java.util.HashSet;

public class SequenceInteraction {
    private final UmlInteraction umlInteraction;
    
    private final HashMap<String, String> name2LifelineId;
    private final HashMap<String, SequenceLifeline> id2Lifeline;
    private final HashSet<String> endpointIdSet;
    private int numOfLifeline;
    
    public SequenceInteraction(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
        name2LifelineId = new HashMap<>();
        id2Lifeline = new HashMap<>();
        endpointIdSet = new HashSet<>();
        numOfLifeline = 0;
    }
    
    public void dealMessage(UmlMessage umlMessage) {
        // 发送方是lifeline，接收方是endpoint
        if (id2Lifeline.containsKey(umlMessage.getSource()) &&
                endpointIdSet.contains(umlMessage.getTarget())) {
            id2Lifeline.get(umlMessage.getSource()).addNumOfLostMessage();
        }
        // 接收方是lifeline
        if (id2Lifeline.containsKey(umlMessage.getTarget())) {
            if (endpointIdSet.contains(umlMessage.getSource())) { // 发送方是endpoint
                id2Lifeline.get(umlMessage.getTarget()).addNumOfFoundMessage();
            } else { // 发送方是lifeline，只有此时才可能发送createMessage
                if (umlMessage.getMessageSort() == MessageSort.CREATE_MESSAGE) {
                    UmlLifeline creator = id2Lifeline.get(umlMessage.getSource()).getUmlLifeline();
                    id2Lifeline.get(umlMessage.getTarget()).addNumOfCreateMessage();
                    id2Lifeline.get(umlMessage.getTarget()).setCreator(creator);
                }
            }
        }
    }
    
    public void addLifeline(SequenceLifeline sequenceLifeline) {
        numOfLifeline++;
        id2Lifeline.put(sequenceLifeline.getId(), sequenceLifeline);
        if (name2LifelineId.containsKey(sequenceLifeline.getName())) {
            name2LifelineId.put(sequenceLifeline.getName(), Tools.getDuplicated());
        } else {
            name2LifelineId.put(sequenceLifeline.getName(), sequenceLifeline.getId());
        }
    }
    
    public boolean isLifelineExist(String lifelineName) {
        return name2LifelineId.containsKey(lifelineName);
    }
    
    public boolean isLifelineDuplicate(String lifelineName) {
        if (!name2LifelineId.containsKey(lifelineName)) {
            return false;
        }
        return name2LifelineId.get(lifelineName).equals(Tools.getDuplicated());
    }
    
    public SequenceLifeline getLifeline(String lifelineName) {
        return id2Lifeline.get(name2LifelineId.get(lifelineName));
    }
    
    public void addEndpointId(String endpointId) {
        endpointIdSet.add(endpointId);
    }
    
    public int getNumOfLifeline() {
        return numOfLifeline;
    }
    
}
