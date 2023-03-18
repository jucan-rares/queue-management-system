package model;

import controller.MainController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SimulationManager implements Runnable {

    private int N;
    private int Q;
    private int simulationTime;
    private int minArrival;
    private int maxArrival;
    private int minService;
    private int maxService;

    private SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    private Scheduler scheduler;
    private MainController controller;

    private List<Task> generatedTasks; //in lista asta vom stoca task-urile ce le vom genera aleatoriu

    private int peakHour;
    private int totalTasks = 0;

    private boolean stop = false; //switch-ul ce dicteaza daca simularea continua

    public static int sumServiceTime = 0;
    public static int completedTasks = 0;
    public static int sumWaitingTime = 0;

    //constructor with parameters
    public SimulationManager(int N, int Q, int simulationTime, int minArrival, int maxArrival, int minService, int maxService, MainController controller) {

        this.N = N;
        this.Q = Q;
        this.simulationTime = simulationTime;
        this.minArrival = minArrival;
        this.maxArrival = maxArrival;
        this.minService = minService;
        this.maxService = maxService;
        this.controller = controller;

        scheduler = new Scheduler(N, Q);
        scheduler.changeStrategy(selectionPolicy); //schimbam strategia

        generatedTasks = new ArrayList<>();
        generateRandomTasks(); //generam task-urile
    }

    //method
    private void generateRandomTasks() { //functie ce genereaza N task-uri cu arrival si service time aleatori

        int ID = 0;
        int arrivalTime;
        int serviceTime;

        for (int i = 0; i < N; i++) {

            ID++;
            arrivalTime = (int) Math.floor(Math.random() * (maxArrival - minArrival + 1) + minArrival);
            serviceTime = (int) Math.floor(Math.random() * (maxService - minService + 1) + minService);
            generatedTasks.add(new Task(ID, arrivalTime, serviceTime));
        }

        Collections.sort(generatedTasks); // de asemenea sortam task-urile dupa arrival time-ul lor
    }

    @Override
    public void run() {

        int currentTime = 0; //timpul curent
        String output = ""; //string-ul ce va fi afisat in GUI

        try {

            while (currentTime < simulationTime && !this.stop) { //daca timpul trece de timpul de simulare sau nu mai sunt clienti atunci simularea se va opri

                int nrOfTasks = 0; //un contor ce indica cate task-uri sunt active (in procesare)
                boolean allQueuesEmpty = true; //un switch ce indica daca toate cozile sunt inchise (fara task-uri)

                output = "\n";

                //basically peste tot unde trebuie sa afisam va trebui sa afisam de doua ori acelasi lucru: o data pentru fisierul text si  o data pentru GUI
                scheduler.writer.write("Time " + currentTime + "\nWaiting clients: ");
                output = output + "  Time " + currentTime + "\n  Waiting clients: ";

                //parcurgem task-urile
                Iterator<Task> i = generatedTasks.iterator();
                while (i.hasNext()) { //parcurgem lista task-urilor in asteptare sa verificam daca le putem repartiza

                    Task task = i.next();
                    if (task.getArrivalTime() == currentTime) { //daca arrival time-ul unui task este egal cu timpul curent atunci il plasam intr-o coada

                        scheduler.dispatchTask(task); //plasam task-ul
                        i.remove(); //stergem task-ul din lista de asteptare
                    }
                }

                for (Task task : generatedTasks) { //afisam atributele fiecarui task din lista de asteptare

                    //afisam
                    scheduler.writer.write("(" + task.getID() + ", " + task.getArrivalTime() + ", " + task.getServiceTime() + "); ");
                    output = output + "(" + task.getID() + ", " + task.getArrivalTime() + ", " + task.getServiceTime() + "); ";
                }

                //afisam
                scheduler.writer.write('\n');
                output = output + '\n';

                for (Queue queue : scheduler.getQueues()) { //parcurgem cozile

                    //afisam
                    scheduler.writer.write("Queue " + (scheduler.getQueues().indexOf(queue) + 1) + ": ");
                    output = output + "  Queue " + (scheduler.getQueues().indexOf(queue) + 1) + ": ";

                    nrOfTasks += queue.getQueue().size();

                    if (queue.getCurrentTask() == null) { //daca coada e goala

                        //afisam
                        scheduler.writer.write(" closed\n");
                        output = output + " closed\n";
                    } else {

                        allQueuesEmpty = false;
                        nrOfTasks++;

                        //afisam
                        scheduler.writer.write("(" + queue.getCurrentTask().getID() + ", " + queue.getCurrentTask().getArrivalTime() + ", " + queue.getCurrentTask().getServiceTime() + "); ");
                        output = output + "(" + queue.getCurrentTask().getID() + ", " + queue.getCurrentTask().getArrivalTime() + ", " + queue.getCurrentTask().getServiceTime() + "); ";

                        queue.getCurrentTask().setServiceTime(queue.getCurrentTask().getServiceTime() - 1); //decrementam service time-ul task-ului curent

                        for (Task task : queue.getQueue()) { //afisam si restul task-urilor din coada

                            //afisam
                            scheduler.writer.write("(" + task.getID() + " ," + task.getArrivalTime() + " ," + task.getServiceTime() + "); ");
                            output = output + "(" + task.getID() + " ," + task.getArrivalTime() + " ," + task.getServiceTime() + "); ";
                        }

                        //afisam
                        scheduler.writer.write('\n');
                        output = output + '\n';

                        queue.getWaitingTime().getAndDecrement(); //decrementam waiting time-ul cozii curente
                    }
                }

                if (totalTasks < nrOfTasks) {

                    totalTasks = nrOfTasks;
                    peakHour = currentTime; //stocam cand a fost ora de varf
                }

                //afisam
                scheduler.writer.write('\n');
                output = output + '\n';

                controller.updateOutput(output); //updatam ce se afiseaza in GUI

                Thread.sleep(1000); //se vor face schimbari o data la o secunda

                currentTime++; //incrementam timpul curent

                if (allQueuesEmpty && generatedTasks.isEmpty()) //simularea se opreste daca cozile sunt goale si lista de task-uri in asteptare este goala
                    this.stop = true;
            }

            this.stop = true;

            for (Queue queue : scheduler.getQueues()) //oprim thread-urile pentru fiecare coada
                queue.setStop(true);

            //calculam timpul mediu de asteptare si de service
            double averageWaiting = 0, averageService = 0;
            if (completedTasks != 0) { //daca a fost cel putin un task procesat

                averageService = Math.round((sumServiceTime / (completedTasks * 1.0)) * 100) / 100.0;
                averageWaiting = Math.round((sumWaitingTime / (completedTasks * 1.0)) * 100) / 100.0;
            }

            //afisam---------------------------------------------------------------------
            output = "\n";

            scheduler.writer.write("Average waiting time: " + averageWaiting + '\n');
            output = output + "  Average waiting time: " + averageWaiting + '\n';

            scheduler.writer.write("Average service time: " + averageService + '\n');
            output = output + "  Average service time: " + averageService + '\n';

            scheduler.writer.write("Peak hour: " + peakHour);
            output = output + "  Peak hour: " + peakHour;

            controller.updateOutput(output); ////updatam ce se afiseaza in GUI
            //---------------------------------------------------------------------------

            scheduler.writer.close(); //inchidem fisierul text

        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}