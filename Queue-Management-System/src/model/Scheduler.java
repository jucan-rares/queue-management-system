package model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private List<Queue> queues;
    private int maxClientsPerQueue;
    private int maxNoQueues;
    private Strategy strategy;

    public FileWriter writer;

    //constructor with parameters
    public Scheduler(int maxClientsPerQueue, int maxNoQueues) {

        this.queues = new ArrayList<>();
        this.maxClientsPerQueue = maxClientsPerQueue;
        this.maxNoQueues = maxNoQueues;
        this.strategy = new ConcreteStrategyQueue();

        Thread[] threads = new Thread[maxNoQueues];
        for (int i = 0; i < maxNoQueues; i++) { //creem si pornim cate un thread pentru fiecare coada

            Queue queue = new Queue();
            threads[i] = new Thread(queue);
            queues.add(queue);
            threads[i].start();
        }

        try {
            writer = new FileWriter("LogOfEvents.txt"); //setam si fisierul text in care vom afisa rezultatele
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //getter
    public List<Queue> getQueues() {
        return queues;
    }

    //methods
    public void dispatchTask(Task task) {
        strategy.addTask(queues, task);
    }

    public void changeStrategy(SelectionPolicy policy) { //functie ce schimba strategia

        if (policy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ConcreteStrategyQueue();
        else
            strategy = new ConcreteStrategyTime();
    }
}