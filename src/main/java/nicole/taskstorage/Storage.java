package nicole.taskstorage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Scanner;

import nicole.nicoleexceptions.NicoleException;
import nicole.task.Task;

public class Storage {
    public Storage() {
    }

    /**
     * Saves user tasks to "./data/tasks.txt"
     *
     * @throws NicoleException if there are write issues to tasks.txt
     */
    protected void saveTasksToFile() throws NicoleException {
        try {
            FileWriter taskFileWriter = new FileWriter("tasks.txt");
            for (int i = 0; i < TaskList.TASKS.size(); i++) {
                taskFileWriter.write(TaskList.TASKS.get(i) + "\n");
            }
            taskFileWriter.close();
        } catch (IOException e) {
            throw new NicoleException("I couldn't save the task >< try again plss");
        }
    }

    public void loadTasksFromFile(File file) throws NicoleException {
        try {
            int numTasksInFile = 0;
            BufferedReader numTasksReader = new BufferedReader(new FileReader(file));
            while (numTasksReader.readLine() != null) {
                numTasksInFile++;
            }
            numTasksReader.close();
            Scanner userTaskFileReader = new Scanner(file);
            while (userTaskFileReader.hasNextLine()) {
                String task = userTaskFileReader.nextLine();
                /*
                    Checking for size < numTasks prevents us from creating duplicate tasks since we call the
                    loadTasksFromFile method both at initialisation when the file is empty and during when
                    running operations on tasks (add, update, delete, etc)
                 */
                if (TaskList.TASKS.size() < numTasksInFile) {
                    char taskType = task.charAt(1);
                    char taskCompleted = task.charAt(4);
                    String taskDescription = task.substring(7);
                    Task recreatedTask = Task.taskFactory(taskDescription, taskType);
                    if (taskCompleted == 'C') {
                        assert recreatedTask != null;
                        recreatedTask.markDone();
                    }
                    TaskList.TASKS.add(recreatedTask);
                }
            }
            userTaskFileReader.close();
        } catch (FileNotFoundException e) {
            throw new NicoleException("I have no past data with you, let's start something ;)");
        } catch (IOException e) {
            throw new NicoleException("Sorry sorry I have trouble loading your tasks from storage....");
        }
    }
}
