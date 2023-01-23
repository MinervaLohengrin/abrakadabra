package persistence;

import exception.EmptyNameException;
import exception.NullTaskException;
import model.Status;
import model.Task;
import model.ToDoList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a reader that reads to-do list from JSON data stored in file
public class JsonReader {
    private String source;

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: reads to-do list from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ToDoList read() throws IOException, EmptyNameException, NullTaskException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseToDoList(jsonObject);
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: parses to-do list from JSON object and returns it
    private ToDoList parseToDoList(JSONObject jsonObject) throws EmptyNameException, NullTaskException {
        String name = jsonObject.getString("name");
        ToDoList toDoList = null;
        toDoList = new ToDoList(name);
        addTasks(toDoList, jsonObject);
        return toDoList;
    }

    // NOTE: this is a helper method for parseToDoList
    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: toDoList
    // EFFECTS: parses multiple tasks from JSON object and adds them to to-do list
    private void addTasks(ToDoList toDoList, JSONObject jsonObject) throws NullTaskException {
        JSONArray jsonArray = jsonObject.getJSONArray("tasks");
        for (Object json : jsonArray) {
            JSONObject nextTask = (JSONObject) json;
            addTask(toDoList, nextTask);
        }
    }

    // NOTE: this is a helper method for addTasks()
    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: toDoList
    // EFFECTS: parses a single task from JSON object and adds it to to-do list
    private void addTask(ToDoList toDoList, JSONObject jsonObject) throws NullTaskException {
        String name = jsonObject.getString("name");
        Status status = Status.valueOf(jsonObject.getString("status"));
        Task task = new Task(name, status);
        toDoList.addTask(task);
    }
}

// for phase 4 demo