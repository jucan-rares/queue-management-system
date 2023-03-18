package model;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {

    @Override
    public void addTask(List<Queue> queues, Task task) { //adaugam task-ul la coada cu cele mai putine task-uri

        int index = 0, min = Integer.MAX_VALUE;

        for (Queue q : queues) { //parcurgem  cozile

            if (q.getQueue().size() < min) { //si cautam cea cu cele mai putine task-uri

                min = q.getQueue().size();
                index = queues.indexOf(q);
            }
        }

        queues.get(index).addTask(task); //si adaugam task-ul in coada gasita
    }
}