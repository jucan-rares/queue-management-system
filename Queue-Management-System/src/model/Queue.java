package model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue implements Runnable {

    private volatile BlockingQueue<Task> queue;
    private AtomicInteger waitingTime;

    private Task currentTask = null;
    private boolean stop = false;

    //constructor without parameters
    public Queue() { //initilizam valorile

        this.waitingTime = new AtomicInteger(0);
        this.queue = new ArrayBlockingQueue<>(1001);
    }

    //getters
    public BlockingQueue<Task> getQueue() {
        return queue;
    }

    public AtomicInteger getWaitingTime() {
        return waitingTime;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    //setter
    public void setStop(boolean stop) {
        this.stop = stop;
    }

    //method
    public void addTask(Task task) {

        queue.add(task); //adaugam task-ul la coada
        waitingTime.getAndAdd(task.getServiceTime()); //trebuie sa adaugam timpul de realizare a task-ului la timpul de asteptare a cozii
        task.setWaitingTime(waitingTime.get());
    }

    @Override
    public void run() {

        while (!stop) {

            try {
                currentTask = queue.take(); //salvam task-ul curent

                int serviceTime = currentTask.getServiceTime();

                Thread.sleep(currentTask.getServiceTime() * 1000); //asteptam cat dureaza service time-ul task-ului

                SimulationManager.completedTasks++; //numaram cate task-uri au fost completate
                SimulationManager.sumServiceTime += serviceTime; //adaugam service time-ul task-ului curent la suma tuturor
                SimulationManager.sumWaitingTime += currentTask.getWaitingTime(); //adaugam waiting time-ul task-ului curent la suma tuturor

                currentTask = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}