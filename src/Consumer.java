import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Consumer extends Agent {

    Long sleepTime;

    List<String> myList = new LinkedList<String>();

    public Consumer() {

    }

    @Override
    protected void setup(){
        Random rand = new Random();
        sleepTime = (long) (rand.nextInt(3000) + 2000);

        addBehaviour(new TickerBehaviour(this,sleepTime) {
            @Override
            protected void onTick() {
                ACLMessage msgSent = new ACLMessage(ACLMessage.INFORM);
                msgSent.setContent("getToken");
                msgSent.addReceiver(new AID("AgentProducer", AID.ISLOCALNAME));
                send(msgSent);
                ACLMessage msgReceived = receive();
                if(msgReceived!=null){
                    if(!msgReceived.getContent().equals("noTokens")&&!msgReceived.getContent().equals("noTokensLeft")){
                        myList.add(msgReceived.getContent());
                        System.out.println("Consumed "+msgReceived.getContent()+", agent "+msgSent.getSender().getLocalName());
                    }
                    else if (msgReceived.getContent().equals("noTokens")){
                        block();
                    }
                    else if (msgReceived.getContent().equals("noTokensLeft")){
                        System.out.println("Agent "+msgSent.getSender().getLocalName()+" pobrał "+myList.size()+" tokenów");
                        stop();
                    }
                }else block();
            }
        });
    }

}