import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class Consumer extends Agent {

    Long sleepTime;

    public Consumer() {

    }

    @Override
    public void doWait(long millis) {
        super.doWait(millis);
    }

    @Override
    protected void setup(){
        Random rand = new Random();
        sleepTime = (long) (rand.nextInt(10000) + 1000);

        addBehaviour(new TickerBehaviour(this,sleepTime) {
            @Override
            protected void onTick() {
                ACLMessage msgSent = new ACLMessage(ACLMessage.INFORM);
                msgSent.setContent("getToken");
                msgSent.addReceiver(new AID("AgentProducer", AID.ISLOCALNAME));
                send(msgSent);
                ACLMessage msgReceived = receive();
                if(msgReceived!=null){
                    System.out.println(msgReceived.getContent());
                }else block();
            }
        });
    }

}