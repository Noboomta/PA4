package sample;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class AllTask {

    public List<Task> allTask = new ArrayList<>();

    public void addTask(Task task){
       allTask.add(task);
    }

    public double getAllProgress(){
        double allProgress = 0;
        for(Task task: allTask){
            allProgress+=task.getProgress();
        }
        return allProgress/allTask.size();
    }

}
