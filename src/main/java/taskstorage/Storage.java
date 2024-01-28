package taskstorage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Scanner;

import nicoleexceptions.NicoleException;

import task.Task;

public class Storage {
    File tasksFile;
    FileWriter taskFileWriter;

    /**
     * Initialises Storage by creating a directory "./data" and a file "tasks.txt" inside.
     *
     * @throws NicoleException if there are issues with creating a writer to the task file.
     */
    public Storage() throws NicoleException {
        new File("./data").mkdirs();
        this.tasksFile = new File("./data/tasks.txt");

        try {
            this.taskFileWriter = new FileWriter("./data/tasks.txt");
        } catch (IOException e) {
            throw new NicoleException("Sorry sorry I couldn't create a file to save your tasks...");
        }

        this.loadTasksFromFile();
    }

    /**
     * Saves user tasks to "./data/tasks.txt"
     *
     * @throws NicoleException if there are write issues to tasks.txt
     */
    protected void saveTasksToFile() throws NicoleException {
        try {
            for (int i = 0; i < TaskList.taskList.size(); i++) {
                this.taskFileWriter.write(TaskList.taskList.get(i) + "\n");
            }
            this.taskFileWriter.close();
        } catch (IOException e) {
            throw new NicoleException("I couldn't save the task >< try again plss");
        }
    }

    private void loadTasksFromFile() throws NicoleException {
        File tasksFile = new File("./data/tasks.txt");
        try {
            int numTasksInFile = 0;
            BufferedReader reader = new BufferedReader(new FileReader(tasksFile));
            while (reader.readLine() != null) {
                numTasksInFile++;
            }

            Scanner userTaskFileReader = new Scanner(tasksFile);
            while (userTaskFileReader.hasNextLine()) {
                String task = userTaskFileReader.nextLine();
                if (TaskList.taskList.size() < numTasksInFile) {
                    char taskType = task.charAt(1);
                    char taskCompleted = task.charAt(4);
                    String taskDescription = task.substring(7);
                    Task recreatedTask = Task.taskFactory(taskDescription, taskType);
                    if (taskCompleted == 'C') {
                        recreatedTask.markDone();
                    }
                    TaskList.taskList.add(recreatedTask);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nicole: I have no past data with you, let's start something ;)");
        } catch (IOException e) {
            throw new NicoleException("Sorry sorry I have trouble loading your tasks from storage....");
        }
    }
}
