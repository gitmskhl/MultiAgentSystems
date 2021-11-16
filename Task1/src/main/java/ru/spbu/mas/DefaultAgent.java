package ru.spbu.mas;

import jade.core.Agent;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DefaultAgent extends Agent {

    private ArrayList<Integer> linkedAgents;
    private float number;
    private int id;
    private boolean isMain;
    private int state;   /// -1 - голосование за лидера,  0 - начальное, 1 - в процессе, 2 - закончил вычисления
    private Pair own;
    private Pair max;

    @Override
    protected void setup() {

        this.id = Integer.parseInt(getAID().getLocalName());
        this.number = new Random().nextInt(10);
        this.isMain = false;
        this.state = -1; /// 0

        System.out.println("Агент #" + this.id + " с загаданным числом " + this.number);
        ///if (this.isMain) System.out.println("Агент " + this.id + " объявлен главным агентом");

        // инициализация соседей
        initLinked();

        // обязательно после инициализации графа
        this.own = new Pair(this.number_of_neighbours(), this.id);
        this.max = this.own;


        /// выбираем лучшего
        System.out.println("Агент #" + this.id + " начинает участие в голосовании");
        addBehaviour(new voteBehaviour(this, App.AGENT_NUMBERS, TimeUnit.SECONDS.toMillis(1)));

        addBehaviour(new AgentBehaviour(this));
    }

    public Pair getOwn(){ return this.own;}

    public Pair getMax() {return this.max;}

    public void setMax(Pair max) {this.max = max;}

    private void initLinked() {
        this.linkedAgents = App.Edges.get(this.id);
    }

    @Override
    protected  void takeDown(){
        System.out.println("Agent #" + id + " terminating");
    }

    public int getId() {
        return this.id;
    }

    public ArrayList<Integer> getNeighbours(){
        return this.linkedAgents;
    }

    public int number_of_neighbours(){
        return this.linkedAgents.size();
    }

    public int getState(){
        return this.state;
    }

    public void setState(int state){
        this.state = state;
    }

    public float getNumber(){
        return this.number;
    }

    public boolean isMain(){
        return this.isMain;
    }

    public void setMain(){
        this.isMain = true;
    }

}
