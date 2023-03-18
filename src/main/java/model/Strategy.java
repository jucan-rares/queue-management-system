package model;

import java.util.List;

public interface Strategy { //interfata pentru a adauga un task in functie de o strategie

    void addTask(List<Queue> queues, Task task);
}
