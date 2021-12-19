package ru.spbu.mas;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class App {

    public static ArrayList<ArrayList<Float>> Connections;
    public static int AGENT_NUMBERS = 5;
    public static int MAX_STEPS = 100;
    public static double NOISE = 1e-3;

    //// Поля ниже отвечают за вывод информации(для удобства проверки)
    ///                              показывать инфу про
    public static boolean showInterferences = true; ///  помехи
    public static boolean showNoises = true;        ///  шум
    public static boolean showMessageInfo = true;   ///  передачу сообщений

    public static void main(String[] args) {

        Connections = new ArrayList<ArrayList<Float>>();

        /// инициализируем граф
        App.initGraph();

        System.out.println(App.Connections);

        MainController mc = new MainController(App.AGENT_NUMBERS, false);
        mc.initAgents();
    }

    private static void initGraph(){
        ArrayList<Float> con1 = new ArrayList<>(Arrays.asList(0f, 0.95f, 1f, 0f, 0f));
        ArrayList<Float> con2 = new ArrayList<>(Arrays.asList(1f, 0f, 0f, 0f, 0f));
        ArrayList<Float> con3 = new ArrayList<>(Arrays.asList(0.93f, 0f, 0f, 1f, 0.83f));
        ArrayList<Float> con4 = new ArrayList<>(Arrays.asList(0f, 0f, 0.81f, 0f, 1f));
        ArrayList<Float> con5 = new ArrayList<>(Arrays.asList(0f, 0f, 1f, 1f, 0f));
        App.Connections.add(con1);
        App.Connections.add(con2);
        App.Connections.add(con3);
        App.Connections.add(con4);
        App.Connections.add(con5);
    }

}

