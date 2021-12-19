package ru.spbu.mas;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.lang.Math;

import java.util.ArrayList;
import java.util.Random;

public class Behaviour_receive extends CyclicBehaviour {

    private final DefaultAgent agent;

    Behaviour_receive(DefaultAgent agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void action() {
        ACLMessage msg = this.agent.receive();
        if (App.showMessageInfo) log(msg);

        if (msg != null) {
            Integer neighbour_id = Integer.parseInt(msg.getSender().getLocalName());
            /// берем значение от соседа и добавляем сюда шум (имитация помех в каналах передачи)
            double noise = ((2 * new Random().nextDouble()) - 1) * App.NOISE; /// вычисляем случайный шум
            if (App.showNoises)
                System.out.println(String.format("Шум сообщения от агента %d к агенту %d = %f", neighbour_id, this.agent.getId(), noise));
            double neighbour_val = Double.parseDouble(msg.getContent());
            double new_tmp_number = this.agent.getTmpNumber() + this.agent.alpha * (neighbour_val + noise - this.agent.getNumber());
            this.agent.setTmpNumber(new_tmp_number);

            this.saveBuffer(neighbour_id, neighbour_val);
        }
        else {
            block();
        }
    }

    private void log(ACLMessage msg){
        if (msg == null) return;
        String txt = "Агент #" + this.agent.getId() +" получил сообщение типа ";
        if (msg.getPerformative() == ACLMessage.REQUEST) txt += "REQUEST ";
        else if (msg.getPerformative() == ACLMessage.INFORM) txt += "INFORM ";
        else if (msg.getPerformative() == ACLMessage.REFUSE) txt += "REFUSE ";
        else if (msg.getPerformative() == ACLMessage.PROPOSE) txt += "PROPOSE ";
        else txt += "Неизвестного типа!";
        txt += "с содержанием: " + msg.getContent() + " от пользователя " + msg.getSender().getLocalName();
        System.out.println(txt);
    }

    private void saveBuffer(Integer id, double value){
        ArrayList<Pair> buffer = this.agent.getBuffer();
        for (int i = 0; i < buffer.size(); ++i){
            if (buffer.get(i).getId() == id){
                buffer.get(i).set(true, value);
                return;
            }
        }
        /// не нашли в буфере. Значит нужно добавить.
        buffer.add(new Pair(id, value));
    }
}
