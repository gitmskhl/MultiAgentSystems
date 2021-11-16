package ru.spbu.mas;

public class Pair {

    private float sum;
    private int num;

    public Pair(float sum, int num){
        this.sum = sum;
        this.num = num;
    }

    public boolean less(Pair p){
        return (this.sum < p.getSum()) || ((this.sum == p.getSum()) && (this.num < p.getNum()));
    }

    public boolean more(Pair p){
        return p.less(this);
    }

    public boolean more_or_equal(Pair p){
        return !this.less(p);
    }

    public boolean custom_equal(Pair p){
        return (!this.less(p)) && (!p.less(this));
    }

    public float getSum(){
        return this.sum;
    }

    public int getNum(){
        return this.num;
    }

}
