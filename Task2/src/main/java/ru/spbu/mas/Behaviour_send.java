package ru.spbu.mas;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Random;

public class Behaviour_send extends TickerBehaviour {


    private final DefaultAgent agent;
    private int currentStep;

    Behaviour_send(DefaultAgent agent, long period) {
        super(agent, period);
        this.setFixedPeriod(true);
        this.agent = agent;
        this.currentStep = 0;
    }

    @Override
    protected void onTick() {
        if (currentStep < App.MAX_STEPS) {

            this.checkBuffer();

            if (this.agent.getTmpNumber() != 0){
                this.agent.setNumber(this.agent.getNumber() + this.agent.getTmpNumber());
                this.agent.setTmpNumber(0);
            }
            broadcast(Double.toString(this.agent.getNumber()), ACLMessage.INFORM);
            this.currentStep++;
        }
        else{
            if (this.agent.getId() == 1){
                System.out.println("Результат: " + this.agent.getNumber());
            }
            this.stop();
        }
    }

    private void broadcast(String content, int performative){
        ACLMessage broadcastMessage = new ACLMessage(performative);
        broadcastMessage.setContent(content);
        for (Integer neighourId : this.agent.getNeighbours()){
            if (new Random().nextDouble() <= App.Connections.get(this.agent.getId() - 1).get(neighourId - 1)){
                broadcastMessage.addReceiver(new AID(Integer.toString(neighourId), AID.ISLOCALNAME));
            }
            else{
                if (App.showInterferences)
                    System.err.println("ДАННЫЕ по каналу от агента " + this.agent.getId() + " к агенту " + neighourId + " не переданы!");
            }

        }
        this.agent.send(broadcastMessage);
    }


    /*
        Эта функция проверяет, получили ли мы все сообщения от тех, кто когда-то уже отправлял их нам.
        Если не получили на итерации, это значит, что в канале возникла задержка.
        Тогда мы обращаемся к буфферу и делаем поправку в соответствии с последним отправленным
        значением, как это описано в формуле для протокола локального голосования.
    */
    private void checkBuffer(){
        ArrayList<Pair> buffer = this.agent.getBuffer();
        for (int i = 0; i < buffer.size(); ++i){
            if (!buffer.get(i).getFlag()){
                double noise = ((2 * new Random().nextDouble()) - 1) * App.NOISE; /// вычисляем случайный шум
                double neighbour_val = buffer.get(i).getValue() + noise;
                double new_tmp_number = this.agent.getTmpNumber() + this.agent.alpha * (neighbour_val - this.agent.getNumber());
                this.agent.setTmpNumber(new_tmp_number);
            }
            else{
                buffer.get(i).setFlag(false);
            }
        }
    }

}
