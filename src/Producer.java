import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Producer extends Agent {

    Long miliseconds = (long) 1200;
    int maxTokens = 40;
    boolean reachedMaxTokens = false;

    public Producer() {

    }

    Queue<String> myQueue = new LinkedList<String>();

    private void generateTokens(Long sleepTime) throws InterruptedException {
        myQueue.offer("token1");
        System.out.println("Generated token 1");
        myQueue.offer("token2");
        System.out.println("Generated token 2");
        myQueue.offer("token3");
        System.out.println("Generated token 3");
        int i = 4;
        String s;
        while (i <= maxTokens) {
            myQueue.offer("token" + i);
            // = myQueue.peek();
            System.out.println("Generated token "+i);
            i++;
            Thread.sleep(miliseconds);
        }
        reachedMaxTokens = true;
    }

    @Override
    protected void setup() {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    generateTokens(miliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();


        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msgReceived = receive();
                if(msgReceived!=null){
                    if(msgReceived.getContent().equals("getToken")) {
                        ACLMessage msgSent = new ACLMessage(ACLMessage.INFORM);
                        if(!myQueue.isEmpty()){
                            msgSent.setContent(myQueue.poll());
                        }
                        else if (myQueue.isEmpty()&&reachedMaxTokens==false){
                            msgSent.setContent("noTokens");
                        }
                        else if (myQueue.isEmpty()&&reachedMaxTokens==true){
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
