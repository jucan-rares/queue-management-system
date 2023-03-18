package controller;

import model.SimulationManager;
import view.MainMenu;

public class MainController {

    private SimulationManager simulationManager;
    private MainMenu mainMenu;

    public MainController() {

        mainMenu = new MainMenu();
        start();
    }

    public void start() {

        mainMenu.getStartBtn().addActionListener(e -> {

            //extragem valorile din GUI
            int N = mainMenu.getN();
            int Q = mainMenu.getQ();
            int simulation = mainMenu.getSimulationTime();
            int minArrival = mainMenu.getMinArrival();
            int maxArrival = mainMenu.getMaxArrival();
            int minService = mainMenu.getMinService();
            int maxService = mainMenu.getMaxService();

            //si le folosim
            simulationManager = new SimulationManager(N, Q, simulation, minArrival, maxArrival, minService, maxService, this);

            //cream un nou thread
            Thread thread = new Thread(simulationManager);
            thread.start();
        });

        mainMenu.getClearBtn().addActionListener(e -> {
            mainMenu.getOutput().setText("");
        });
    }

    public void updateOutput(String output) {
        mainMenu.setTextArea(output);
    }
}