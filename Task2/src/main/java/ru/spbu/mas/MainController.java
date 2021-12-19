package ru.spbu.mas;

import com.sun.tools.javac.Main;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class MainController {

    public static int numberOfAgents; /// private
    private static boolean showGUI;

    public MainController(int numberOfAgents, boolean showGUI){
        this.numberOfAgents = numberOfAgents;
        this.showGUI = showGUI;
    }

    void initAgents() {

        // Retrieve the singleton instance of the JADE Runtime.
        Runtime rt = Runtime.instance();

        // Create a container to host the default Agent.
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "10098");
        p.setParameter(Profile.GUI, Boolean.toString(this.showGUI));
        ContainerController cc = rt.createMainContainer(p);

        try {
            for (int i = 1; i <= MainController.numberOfAgents; i++) {
                AgentController agent = cc.createNewAgent(Integer.toString(i), "ru.spbu.mas.DefaultAgent", null);
                agent.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}