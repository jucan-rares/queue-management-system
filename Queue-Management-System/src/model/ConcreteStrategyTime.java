package model;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addTask(List<Queue> queues, Task task) { //adaugam task-ul la coada cu timpul de asteptare cel mai scurt

        int index = 0, min = Integer.MAX_VALUE;

        for (Queue q : queues) { //parcurgem  cozile

            if (q.getWaitingTime().get() < min) { //si cautam cea cu cel mai mic timp de asteptare

                min = q.getWaitingTime().get();
                index = queues.indexOf(q);
            }
        }

        queues.get(index).addTask(task); //si adaugam task-ul in coada gasita
    }
}