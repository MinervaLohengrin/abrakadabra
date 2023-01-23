package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

import static model.Status.FLEXIBLE;
import static model.Status.HARD;

// source: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
// source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a Task having a name and a status
public class Task implements Writable {
    private String name;                // this is the name of a task
    private Status status;

    // REQUIRES: name has non-zero length and status is either HARD or FLEXIBLE
    //           before they get passed to the constructor as arguments
    // MODIFIES: this
    // EFFECTS: constructs a task with a name and status
    public Task(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    // EFFECTS: returns name of this task
    public String getName() {
        return name;
    }

    // EFFECTS: returns status of this task
    public Status getStatus() {
        return status;
    }

    // MODIFIES: this
    // EFFECTS: changes status of task
    public void changeStatus() {
        if (this.status == HARD) {
            this.status = FLEXIBLE;
        } else {
            this.status = HARD;
        }
    }


    // source: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
    // EFFECTS: returns the string representation of this task
    @Override
    public String toString() {
        if (this.status == HARD) {
            return name + "  --->  hard deadline";
        }
        return name + "  --->  flexible deadline";
    }


    @Override
    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: returns this task as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("status", status);
        return json;
    }
}


