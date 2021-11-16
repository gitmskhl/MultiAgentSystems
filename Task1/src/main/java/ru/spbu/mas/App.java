package ru.spbu.mas;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class App {

    public static HashMap<Integer, ArrayList<Integer>> Edges;
    public static int AGENT_NUMBERS = 11;
    public static int mainAgentId = 1;

    public static void main(String[] args) {

        Edges = new HashMap<>();

        App.initTop2();

        System.out.println(App.Edges);

        MainController mc = new MainController(App.AGENT_NUMBERS, App.mainAgentId, false);
        mc.initAgents();
    }

    private static void initStar(){
        for (int agentId = 1; agentId <= App.AGENT_NUMBERS; ++agentId){
            if (agentId == App.mainAgentId){
                ArrayList<Integer> tmp = new ArrayList<>();
                for (int i = 1; i <= App.AGENT_NUMBERS; ++i)
                    if (i != App.mainAgentId) tmp.add(i);

                App.Edges.put(agentId, tmp);
            }
            else{
                App.Edges.put(agentId, new ArrayList<>(Arrays.asList(App.mainAgentId)));
            }
        }
    }

    private static void initTop1(){
        ArrayList<Integer> arr1 = new ArrayList<>();
        arr1.add(2); arr1.add(3);
        ArrayList<Integer> arr2 = new ArrayList<>();
        arr2.add(1); arr2.add(3);
        ArrayList<Integer> arr3 = new ArrayList<>();
        arr3.add(1); arr3.add(2); arr3.add(4);
        ArrayList<Integer> arr4 = new ArrayList<>();
        arr4.add(3); arr4.add(5);
        ArrayList<Integer> arr5 = new ArrayList<>();
        arr5.add(4);
        Edges.put(1, arr1);
        Edges.put(2, arr2);
        Edges.put(3, arr3);
        Edges.put(4, arr4);
        Edges.put(5, arr5);
    }

    private static void initTop2(){
        ArrayList<Integer> arr1 = new ArrayList<>();
        arr1.add(2); arr1.add(3);
        ArrayList<Integer> arr2 = new ArrayList<>();
        arr2.add(1); arr2.add(3);
        ArrayList<Integer> arr3 = new ArrayList<>();
        arr3.add(1); arr3.add(2); arr3.add(4); arr3.add(5);
        ArrayList<Integer> arr4 = new ArrayList<>();
        arr4.add(3); arr4.add(5); arr4.add(6); arr4.add(8);
        ArrayList<Integer> arr5 = new ArrayList<>();
        arr5.add(3); arr5.add(4); arr5.add(7);

        ArrayList<Integer> arr6 = new ArrayList<>();
        arr6.add(4);
        ArrayList<Integer> arr7 = new ArrayList<>();
        arr7.add(5); arr7.add(8); arr7.add(10); arr7.add(11);
        ArrayList<Integer> arr8 = new ArrayList<>();
        arr8.add(4); arr8.add(7); arr8.add(9);
        ArrayList<Integer> arr9 = new ArrayList<>();
        arr9.add(8);
        ArrayList<Integer> arr10 = new ArrayList<>();
        arr10.add(7); arr10.add(11);
        ArrayList<Integer> arr11 = new ArrayList<>();
        arr11.add(10); arr11.add(7);

        Edges.put(1, arr1);
        Edges.put(2, arr2);
        Edges.put(3, arr3);
        Edges.put(4, arr4);
        Edges.put(5, arr5);
        Edges.put(6, arr6);
        Edges.put(7, arr7);
        Edges.put(8, arr8);
        Edges.put(9, arr9);
        Edges.put(10, arr10);
        Edges.put(11, arr11);
    }

}

