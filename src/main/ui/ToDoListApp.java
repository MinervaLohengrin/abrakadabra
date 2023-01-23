package ui;

import exception.EmptyNameException;
import exception.NotInListException;
import exception.NullTaskException;
import model.Status;
import model.Task;
import model.ToDoList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static model.Status.FLEXIBLE;
import static model.Status.HARD;


// source: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
// source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents the to-do list application
public class ToDoListApp {
    private static final String JSON_STORE = "./data/todolist.json";
    private Scanner input;
    private ToDoList toDoList;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // source: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
    // EFFECTS: constructs to-do list and runs application
    public ToDoListApp() {
        input = new Scanner(System.in);
        try {
            toDoList = new ToDoList("A list of tasks that I am likely to procrastinate on");
        } catch (EmptyNameException e) {
            System.out.println("\n" + "EmptyNameException: name of to-do list has length of zero" + "\n");
        }
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runToDoList();
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
    // MODIFIES: this
    // EFFECTS: processes user input
    private void runToDoList() {
        boolean keepGoing = true;
        String command = null;
        input = new Scanner(System.in);
        do {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();


            if (!(command.equals("q"))) {
                processCommand(command);
            } else if (command.equals("q")) {
                keepGoing = false;
            }

        } while (keepGoing);

        System.out.println("\nTill next time");
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("Select from:");
        System.out.println("\ta -> add task");
        System.out.println("\td -> delete task");
        System.out.println("\tp -> print tasks");
        System.out.println("\tcs -> change the status of a task");
        System.out.println("\ts -> save to-do list to file");
        System.out.println("\tl -> load to-do list from file");
        System.out.println("\tq -> quit \n");
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("a")) {
            doAddTask();
        } else if (command.equals("d")) {
            doDeleteTask();
        } else if (command.equals("p")) {
            printTasks();
        } else if (command.equals("cs")) {
            doChangeStatus();
        } else if (command.equals("s")) {
            saveToDoList();
        } else if (command.equals("l")) {
            loadToDoList();
        } else {
            System.out.println("Selection not valid...");
        }

    }

    // source: https://stackoverflow.com/questions/12524243/how-do-i-make-java-register-a-string-input-with-spaces/12524285
    // source: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
    // REQUIRES: name of task entered by user has non-zero length, and when the user is asked whether or not he/she
    //           would like to add another task, the response entered by the user is either 'yes' or 'no'
    //
    //           a task that has the exact same name (same spelling and spacing) as
    //           a task that is already in the to-do list
    //           cannot be added to the to-do list
    // MODIFIES: this
    // EFFECTS: prompt user for name and status of task and adds it to the to-do list
    private void doAddTask() {
        boolean continueMethod = true;
        System.out.println("\n" + "Please enter name of task:" + "\n");

        String name = getInitialCommand();

        if (toDoList.isNameLengthZero(name)) {
            System.out.println("\n" + "Invalid input - the name of the task has a length of zero" + "\n");
            return;
        }

        Status status = readStatus();
        if (status == null) {
            System.out.println("\n" + "invalid selection - status of task should either be 'hard' or 'flexible'");
            System.out.println("\n");
            return;
        }
        createAndAddTask(name, status);

        addAnotherTask(continueMethod);
        printTasks();
    }

    // EFFECTS: asks user if he/she would like to add another task
    private void addAnotherTask(boolean continueMethod) {
        String name;
        Status status;
        while (continueMethod) {
            System.out.println("\n" + "Would you like to add another task ?\n");
            System.out.println("\t enter 'yes' or 'no', not including the quotation marks \n");
            String subsequentCommand = input.next();
            if (subsequentCommand.equalsIgnoreCase("yes")) {
                System.out.println("\n" + "Please enter name of task: \n");
                input.useDelimiter("\n");
                name = input.next();
                status = readStatus();
                createAndAddTask(name, status);
            } else if (subsequentCommand.equalsIgnoreCase("no")) {
                continueMethod = false;
            } else {
                System.out.println("\n" + "invalid selection.\n");
            }
        }
    }

    // EFFECTS: creates a new task according to the arguments passed to the method
    //          and adds it to the to-do list
    private void createAndAddTask(String name, Status status) {
        try {
            toDoList.addTask(new Task(name, status));
        } catch (NullTaskException e) {
            System.out.println("\n" + "NullTaskException: the task has not been initialized" + "\n");
        }
    }

    // NOTE: this is a helper method for doAddTask, doDeleteTask() and doChangeStatus
    // EFFECTS: records the user's initial command
    private String getInitialCommand() {
        input.useDelimiter("\n");
        return input.next();
    }


    // MODIFIES: this
    // EFFECTS: prompts user for name and status of task and deletes it from the to-do list
    private void doDeleteTask() {
        Task taskToBeDeleted = null;
        System.out.println("\n" + "Please enter name of task you wish to delete:");
        System.out.println("\n");
        String name = getInitialCommand();
        System.out.println("\n" + "Please enter the current status of task you wish to delete:");
        System.out.println("\n\t NOTE: enter either 'hard' or 'flexible' into the console,");
        System.out.println("\t not including the quotation marks \n");
        String status = input.next();
        for (Task t : toDoList.getTasks()) {
            if (t.getName().equalsIgnoreCase(name) && t.getStatus().toString().equalsIgnoreCase(status)) {
                // I can't make a call to deleteTask() here because this is a for-each loop and
                // it would result in ConcurrentModificationException
                taskToBeDeleted = t;
            }
        }

        // if there are no tasks in the list that match the name and status specified by user input,
        // the value assigned to taskToBeDeleted will still be null,
        // and NotInListException will definitely be thrown
        // otherwise, NotInListException will not be thrown
        try {
            toDoList.deleteTask(taskToBeDeleted);
        } catch (NotInListException e) {
            System.out.println("\n" + "NotInListException: the task is not in the to-do list" + "\n");
            return;
        }

        System.out.println("\n\t the task '" + name + "'" + " whose status is " + "'" + status + "'");
        System.out.println("\t has been deleted. \n");

        printTasks();
    }


    // REQUIRES: name of task entered by user has non-zero length, and when the user is asked to enter
    //           the status of the task, the response entered by the user is either 'hard' or 'flexible'
    // MODIFIES: this
    // EFFECTS: prompts user for name and status of task and changes the status of the said task
    private void doChangeStatus() {
        Task taskToBeChanged = null;
        System.out.println("\n" + "Please enter name of task you wish to modify: \n");
        String name = getInitialCommand();
        System.out.println("\n" + "Please enter the current status of task you wish to modify:");
        System.out.println("\n\t" + "enter either 'hard' or 'flexible' into the console \n");
        System.out.println("\t not including the quotation marks \n");
        String status = input.next();
        for (Task t : toDoList.getTasks()) {
            if (t.getName().equalsIgnoreCase(name) && t.getStatus().toString().equalsIgnoreCase(status)) {
                // I can't make a call to changeStatus() here because this is a for-each loop and
                // it would result in ConcurrentModificationException
                taskToBeChanged = t;
            }
        }
        if (taskToBeChanged == null) {
            System.out.println("\n" + "invalid input: ");
            System.out.println("\t" + "there is no such task with name: " + name + " and status: " + status);
        } else {
            taskToBeChanged.changeStatus();
            String newStatus = taskToBeChanged.getStatus().toString().toLowerCase();
            System.out.println("\n\t the status of the task '" + name + "'");
            System.out.println("\t has been changed from " + "'" + status + "'" + " to '" + newStatus + "' \n");
        }
        printTasks();
    }

    // EFFECTS: prints all the tasks in to-do list to the console
    private void printTasks() {
        List<Task> tasks = toDoList.getTasks();

        System.out.println("\n" + "Here is the most up-to-date list: \n");
        for (Task t : tasks) {
            System.out.println("\t" + t);
        }
        System.out.println("\n");
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
    // REQUIRES: user input is either '1' or '2'
    // EFFECTS: prompts user to select status and returns it
    private Status readStatus() {
        System.out.println("\n" + "Please select a status for your task \n");

        System.out.println("1  --->  Hard deadline \n");
        System.out.println("2  --->  Flexible deadline \n");


        int selection = input.nextInt();

        if (selection == 1) {
            return HARD;
        } else if (selection == 2) {
            return FLEXIBLE;
        } else {
            return null;
        }
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: saves the to-do list to file
    private void saveToDoList() {
        try {
            jsonWriter.open();
            jsonWriter.write(toDoList);
            jsonWriter.close();
            System.out.println("\n" + "Saved " + toDoList.getName() + " to " + JSON_STORE + "\n");
        } catch (FileNotFoundException e) {
            System.out.println("\n" + "Unable to write to file: " + JSON_STORE + "\n");
        }
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: loads to-do list from file
    private void loadToDoList() {
        try {
            toDoList = jsonReader.read();
            System.out.println("\n" + "Loaded " + toDoList.getName() + " from " + JSON_STORE + "\n");
        } catch (IOException e) {
            System.out.println("\n" + "Unable to read from file: " + JSON_STORE + "\n");
        } catch (EmptyNameException emptyNameException) {
            System.out.println("\n" + "EmptyNameException: name of to-do list has length of zero" + "\n");
        } catch (NullTaskException nullTaskException) {
            System.out.println("\n" + "NullTaskException: the task has not been initialized" + "\n");
        }
    }

}

