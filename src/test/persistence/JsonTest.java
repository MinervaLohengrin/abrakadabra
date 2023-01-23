package persistence;

import model.Status;
import model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

// source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// represents a class that is the superclass of JsonReaderTest and JsonWriterTest
public class JsonTest {
    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // checks that the name that was passed to checkTask() as an argument
    // is identical to the name field of the task that is passed to checkTask() as an argument
    // and that the status that was passed to checkTask() as an argument
    // is identical to the status field of the task that is passed to checkTask() as an argument
    protected void checkTask(String name, Status status, Task task) {
        assertEquals(name, task.getName());
        assertEquals(status, task.getStatus());
    }
}

// for phase 4 demo