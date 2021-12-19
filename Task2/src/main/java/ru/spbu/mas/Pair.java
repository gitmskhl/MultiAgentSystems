package ru.spbu.mas;

public class Pair {

    private boolean flag;
    private double value;
    private Integer id;

    Pair(Integer id, double value){
        this(true, id, value);
    }

    Pair(boolean flag, Integer id, double value){
        this.flag = flag;
        this.value = value;
        this.id = id;
    }

    boolean getFlag(){ return this.flag; }

    double getValue(){ return value; }

    Integer getId(){ return this.id; }

    void set(boolean flag, double value){
        this.flag = flag;
        this.value = value;
    }

    void setValue(double value){
        this.set(true, value);
    }

    void setFlag(boolean flag){
        this.flag = flag;
    }

}
