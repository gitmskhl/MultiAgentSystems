package ru.spbu.mas;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AgentBehaviour extends CyclicBehaviour {

    private final DefaultAgent agent;
    private ACLMessage reply;
    private final int number_of_neighbours;
    private int number_of_received_messages;
    private float sum;  /// сумма пришедших от соседей
    private int num;   /// число подсчитанных агентов

    AgentBehaviour(DefaultAgent agent) {
        super(agent);
        this.agent = agent;
        this.reply = null;
        this.number_of_neighbours = agent.number_of_neighbours();
        this.number_of_received_messages = 0;
        this.sum = agent.getNumber();
        this.num = 1;
    }

    public void action(){

        ACLMessage msg = this.agent.receive();

        this.log(msg);

        switch (this.agent.getState()){

            case -1:
                if (msg != null){
                    if (msg.getPerformative() == ACLMessage.PROPOSE){
                        Pair prop = this.getPairFromString(msg.getContent());
                        if (this.agent.getMax().less(prop)) this.agent.setMax(prop);
                    }
                    else{
                        System.err.println("Для агента #" + this.agent.getId() +
                                "с состоянием '-1'(голосование( пришел запрос НЕ PROPOSE!");
                    }
                }
                else block();

                break;

            case 0:
                /*
                if (this.agent.isMain()){
                    this.agent.setState(1);
                    this.broadcast();
                }
                */
                if (msg != null){
                    if (msg.getPerformative() == ACLMessage.REQUEST){
                        this.reply = msg.createReply();
                        this.reply.setPerformative(ACLMessage.INFORM);
                        this.agent.setState(1);
                        this.broadcast();
                    }
                    else{
                        System.err.println("Для агента #" + this.agent.getId() +
                                "с состоянием '0' пришел запрос НЕ REQUEST!");
                    }
                }
                else block();
                break;

            case 1:
                if (msg != null){
                    if (msg.getPerformative() == ACLMessage.REQUEST){
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.REFUSE);
                        reply.setContent("not-available");
                        this.agent.send(reply);
                    }
                    else if (msg.getPerformative() == ACLMessage.INFORM){
                        Pair p = this.getPairFromString(msg.getContent());
                        this.sum += p.getSum();
                        this.num += p.getNum();
                        this.number_of_received_messages += 1;
                    }
                    else {
                        this.number_of_received_messages += 1;
                    }
                    if (this.number_of_received_messages == this.number_of_neighbours){
                        this.agent.setState(2);
                        if (this.agent.isMain()){
                            System.out.println("Программа окончена. Среднее арифметическое = " + (float) this.sum / this.num);
                            System.out.println("Значения sum = " + this.sum + " num = " + this.num);
                        }
                        else{
                            this.reply.setContent("(" + this.sum + "," + this.num + ")");
                            this.agent.send(this.reply);
                        }

                    }
                }
                else block();
                break;

            case 2:
                if (msg != null){
                    if (msg.getPerformative() == ACLMessage.REQUEST){
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.REFUSE);
                        reply.setContent("not-available");
                        this.agent.send(reply);
                    }
                    else{
                        System.err.println("Для агента #" + this.agent.getId() +
                                "с состоянием '2' пришел запрос НЕ REQUEST!");
                    }
                }
                else block();
                break;

            default:
                System.err.println("Неизвестный тип состояния агента #" + this.agent.getId() + "!!!");
                break;
        }
    }

    private void broadcast(){
        ACLMessage broadcastMessage = new ACLMessage(ACLMessage.REQUEST);
        broadcastMessage.setContent("get average");
        for (Integer neighourId : this.agent.getNeighbours()){
            broadcastMessage.addReceiver(new AID(Integer.toString(neighourId), AID.ISLOCALNAME));
        }
        this.agent.send(broadcastMessage);
    }

    private Pair getPairFromString(String str){
        str = str.substring(1, str.length()-1);
        String[] keyValuePairs = str.split(",");
        return new Pair(Float.parseFloat(keyValuePairs[0]), Integer.parseInt(keyValuePairs[1]));
    }

    private void log(ACLMessage msg){
        if (msg == null) return;
        String txt = "Агент #" + this.agent.getId() + " с состоянием " + this.agent.getState() +" получил сообщение типа ";
         if (msg.getPerformative() == ACLMessage.REQUEST) txt += "REQUEST ";
        else if (msg.getPerformative() == ACLMessage.INFORM) txt += "INFORM ";
        else if (msg.getPerformative() == ACLMessage.REFUSE) txt += "REFUSE ";
        else if (msg.getPerformative() == ACLMessage.PROPOSE) txt += "PROPOSE ";
        else txt += "Неизвестного типа!";
        txt += "с содержанием: " + msg.getContent() + " от пользователя " + msg.getSender().getLocalName();
        System.out.println(txt);
    }

}
