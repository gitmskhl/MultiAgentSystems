package ru.spbu.mas;

import jade.core.Agent;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DefaultAgent extends Agent {

    private ArrayList<Integer> linkedAgents;
    private ArrayList<Pair> buffer; /// необходим для хранения информации при сильных помехах в каналах связи
    private double number;
    private double tmpNumber;
    private int id;
    public double alpha;

    @Override
    protected void setup() {

        this.id = Integer.parseInt(getAID().getLocalName());
        this.number = new Random().nextInt(10);
        this.tmpNumber = 0;
        this.alpha = 1.0 / (App.AGENT_NUMBERS + 1);

        System.out.println("Агент #" + this.id + " с загаданным числом " + this.number);

        // инициализация соседей
        initLinked();

        this.buffer = new ArrayList<>();

        addBehaviour(new Behaviour_receive(this));
        addBehaviour(new Behaviour_send(this, TimeUnit.SECONDS.toMillis(1)));
    }

    private void initLinked() {
        this.linkedAgents = new ArrayList<>();
        ArrayList<Float> prob_neighbours = App.Connections.get(this.id - 1);

        for (int i = 0; i < App.AGENT_NUMBERS; ++i){
            if (prob_neighbours.get(i) != 0){
                this.linkedAgents.add(i + 1);
            }
        }

        System.out.println(this.linkedAgents);

    }

    @Override
    protected  void takeDown(){
        System.out.println("Agent #" + id + " terminating");
    }

    public int getId() {
        return this.id;
    }

    public double getNumber(){ return this.number; }

    public void setNumber(double new_number) { this.number = new_number; }

    public ArrayList<Integer> getNeighbours(){
        return this.linkedAgents;
    }

    public int number_of_neighbours(){
        return this.linkedAgents.size();
    }

    public double getTmpNumber(){ return this.tmpNumber; }

    public void setTmpNumber(double new_num) { this.tmpNumber = new_num; }

    public ArrayList<Pair> getBuffer() { return this.buffer; }

}
