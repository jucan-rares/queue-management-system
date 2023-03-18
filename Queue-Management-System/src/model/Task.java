package model;

public class Task implements Comparable<Task> {

    private int ID;
    private int arrivalTime;
    private int serviceTime;
    private int waitingTime = 0;

    //constructor with parameters
    public Task(int ID, int arrivalTime, int serviceTime) {

        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    //getters
    public int getID() {
        return ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    //setters
    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    //method
    @Override
    public int compareTo(Task task) { //functie pentru determinarea task-ului cu arrival time-ul mai mic

        if (task.getArrivalTime() == this.getArrivalTime())
            return 0;
        else
        if (task.getArrivalTime() < this.getArrivalTime())
            return 1;
        else
            return -1;
    }
}

