package model;

import exception.EmptyNameException;
import exception.NotInListException;
import exception.NullTaskException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;



// source: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
// source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a to-do list having a collection of tasks
public class ToDoList extends DefaultListModel implements Writable {
    private String name;             // this is the name of a to-do list
    private List<Task> tasks;
    private int numberOfSpaces;
    private char[] array;


    // MODIFIES: this
    // EFFECTS: if name has a length of zero, throws EmptyNameException
    //          otherwise constructs to-do list with a name and empty list of tasks
    public ToDoList(String name) throws EmptyNameException {
        if (isNameLengthZero(name)) {
            throw new EmptyNameException();
        }
        this.name = name;
        tasks = new ArrayList<>();

    }

    // EFFECTS: checks whether or not the String passed to this method as an argument
    //          is an empty string, or a string comprised of a bunch of spaces
    //          if the String is an empty string or a string comprised of a bunch of spaces,
    //          returns true.
    //          otherwise, returns false
    public boolean isNameLengthZero(String input) {
        numberOfSpaces = 0;
        array = input.toCharArray();

        // if the user entered an empty string
        if (input.equalsIgnoreCase("")) {
            return true;
        }

        // checking to see if the user just entered a bunch of spaces
        for (char c : array) {
            if (Character.isSpaceChar(c)) {
                numberOfSpaces++;
            }
        }

        // if numberOfSpaces is equal to the length of the user input, it means that the user
        // had just entered a bunch of spaces
        if (numberOfSpaces == input.length()) {
            return true;
        }

        return false;
    }

    // EFFECTS: returns name of this to-do list
    public String getName() {
        return name;
    }


    // MODIFIES: this
    // EFFECTS: if task is null, throws NullTaskException
    //          otherwise task is added
    public void addTask(Task task) throws NullTaskException {
        if (isTaskNull(task)) {
            throw new NullTaskException();
        }
        tasks.add(task);
    }

    // EFFECTS: if task is null, returns true
    //          otherwise, returns false
    public boolean isTaskNull(Task task) {
        if (task == null) {
            return true;
        }
        return false;
    }


    // MODIFIES: this
    // EFFECTS: if task is not in the list of tasks, throws NotInListException
    //          otherwise task is deleted
    public void deleteTask(Task task) throws NotInListException {
        if (isTaskNotInList(task)) {
            throw new NotInListException();
        }
        tasks.remove(task);
    }

    // EFFECTS: if task is not in the list of tasks, returns true
    //          otherwise returns false
    public boolean isTaskNotInList(Task task) {
        if (!tasks.contains(task)) {
            return true;
        }
        return false;
    }

    // EFFECTS: returns the list of tasks in this to-do list
    public List<Task> getTasks() {
        return tasks;
    }


    // EFFECTS: returns number of tasks in this to-do list
    public int numTasks() {
        return tasks.size();
    }


    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    // EFFECTS: returns this to-do list as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("tasks", tasksToJson());
        return json;
    }

    // NOTE: this is a helper method for the implementation of toJson() in the ToDoList class
    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: returns tasks in this to-do list as a JSON array
    private JSONArray tasksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Task t : tasks) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: checks if the name of the new task to be added
    //          (as specified by the user) is already added to the list
    public boolean isTaskInListWithSameName(String name) {
        for (int i = 0; i < this.numTasks(); i++) {
            if (this.getTasks().get(i).getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}

