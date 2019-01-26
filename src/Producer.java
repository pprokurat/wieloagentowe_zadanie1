import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Producer extends Agent {

    Long miliseconds = (long) 20;
    int maxTokens = 30;

    public Producer() {

    }

    Queue<String> myQueue = new LinkedList<String>();

    private void generateTokens(Long sleepTime) throws InterruptedException {
        myQueue.offer("token1");
        myQueue.offer("token2");
        myQueue.offer("token3");
        int i = 4;
        String s;
        while (i <= maxTokens) {
            myQueue.offer("token" + i);
            s = myQueue.peek();
            System.out.println("Generated "+s);
            i++;
            Thread.sleep(miliseconds);
        }
    }

    @Override
    protected void setup() {
        try {
            generateTokens(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msgReceived = receive();
                if(msgReceived!=null){
                    if(msgReceived.getContent().equals("getToken")) {
                        ACLMessage msgSent = new ACLMessage(ACLMessage.INFORM);
                        if(!myQueue.isEmpty()){
                            msgSent.setContent(myQueue.poll());                                                    }
                        else{
                            msgSent.setContent("noTokensLeft");
                        }
                        AID sender = msgReceived.getSender();
                        String senderString = sender.getLocalName();
                        senderString = senderString.split("@")[0];
                        msgSent.addReceiver(new AID(senderString,AID.ISLOCALNAME));
                        send(msgSent);
                    }
                }else block();
            }
        });

    }

}
