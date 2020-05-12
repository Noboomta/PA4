package sample;
import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.List;

/**
 * AllTask class, class of the Task.
 */
public class AllTask {

    public List<Task> allTask = new ArrayList<>();

    /**
     * To add task.
     */
    public void addTask(Task task) {
        allTask.add(task);
    }

    /**
     * To return all task progress
     */
    public double getAllProgress() {
        double allProgress = 0;
        for (Task task : allTask) {
            allProgress += task.getProgress();
        }
        return allProgress / allTask.size();
    }

}
