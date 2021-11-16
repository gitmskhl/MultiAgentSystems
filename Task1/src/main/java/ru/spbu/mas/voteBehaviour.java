package ru.spbu.mas;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class voteBehaviour extends TickerBehaviour {

    private DefaultAgent agent;
    private long period;
    private int step;
    private int number_of_agents;


    public voteBehaviour(DefaultAgent agent, int number_of_agents, long period){
        super(agent, period);
        this.setFixedPeriod(true);
        this.agent = agent;
        this.step = 0;
        this.number_of_agents = number_of_agents;
    }

    @Override
    protected void onTick(){
        if (this.step <= this.number_of_agents){
            broadcast();
            this.step += 1;
        }
        else{
            if (this.agent.getMax().custom_equal(this.agent.getOwn())) {
                this.agent.setMain();
                System.out.println("Агент #" + this.agent.getId() + " выбран главным");
            }
            this.agent.setState(0);
            this.stop();
            System.out.println("Агент #" + this.agent.getId() + " заканчивает участие в голосовании");
            if(this.agent.isMain()) {
                this.agent.setState(1);
                this.broadcastRequest();
            }
        }
    }


    private void broadcast(){
        ACLMessage broadcastMessage = new ACLMessage(ACLMessage.PROPOSE);
        float deg = this.agent.getMax().getSum();
        int id = this.agent.getMax().getNum();
        broadcastMessage.setContent("(" + deg + "," + id + ")");
        for (Integer neighourId : this.agent.getNeighbours()){
            broadcastMessage.addReceiver(new AID(Integer.toString(neighourId), AID.ISLOCALNAME));
        }
        this.agent.send(broadcastMessage);
    }

    private void broadcastRequest(){
        ACLMessage broadcastMessage = new ACLMessage(ACLMessage.REQUEST);
        broadcastMessage.setContent("get average");
        for (Integer neighourId : this.agent.getNeighbours()){
            broadcastMessage.addReceiver(new AID(Integer.toString(neighourId), AID.ISLOCALNAME));
        }
        this.agent.send(broadcastMessage);
    }

}
