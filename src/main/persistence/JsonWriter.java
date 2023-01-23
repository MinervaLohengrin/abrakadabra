package persistence;


import model.ToDoList;
import org.json.JSONObject;


import java.io.*;

// source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a writer that writes JSON representation of to-do list to file
public class JsonWriter {
    // the field "SPACES" denotes the number of spaces from the bracket of a JSON representation of a task
    // to the left most " symbol of the task
    // look at the json files in the data folder for clarification
    private static final int SPACES = 4;
    private PrintWriter printWriter;
    private String destinationFile;

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destinationFile) {
        this.destinationFile = destinationFile;
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: opens printWriter; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(new File(destinationFile));
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: writes JSON representation of to-do list to file
    public void write(ToDoList toDoList) {
        JSONObject json = toDoList.toJson();
        saveToFile(json.toString(SPACES));
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: closes printWriter
    public void close() {
        printWriter.close();
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        printWriter.print(json);
    }
}

// for phase 4 demo