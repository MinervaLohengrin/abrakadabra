package persistence;

import exception.EmptyNameException;
import exception.NullTaskException;
import model.Status;
import model.Task;
import model.ToDoList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// unit tests for the JsonReader class
public class JsonReaderTest extends JsonTest {

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // tests that
    // when read() is invoked upon a JsonReader
    // and a non-existent file name is assigned to the source field of the said JsonReader,
    //  an IOException is thrown
    //
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ToDoList toDoList = reader.read();
            fail("IOException expected but not thrown");
        } catch (IOException e) {
            // pass
        } catch (EmptyNameException e) {
            System.out.println("\n" + "name of to-do list has length of zero" + "\n");
        } catch (NullTaskException e) {
            System.out.println("\n" + "the task has not been initialized" + "\n");
        }
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // tests that
    // when read() is invoked upon a JsonReader
    // and the name of
    // the source file that was passed to the JsonReader as an argument during its instantiation
    // is the JSON representation of an empty ToDoList object,
    // the JsonReader will read in an empty to-do list
    @Test
    void testReaderEmptyToDoList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyToDoList.json");
        try {
            ToDoList toDoList = reader.read();
            assertEquals("A list with zero tasks", toDoList.getName());
            assertEquals(0, toDoList.numTasks());
        } catch (IOException e) {
            fail("Couldn't read from file; IOException thrown but not expected");
        } catch (EmptyNameException e) {
            System.out.println("\n" + "name of to-do list has length of zero" + "\n");
        } catch (NullTaskException e) {
            System.out.println("\n" + "the task has not been initialized" + "\n");
        }
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // tests that
    // when read() is invoked upon a JsonReader
    // and the name of
    // the source file that was passed to the JsonReader as an argument during its instantiation
    // is the JSON representation of a non-empty ToDoList object,
    // the JsonReader will read in a to-do list that is identical to
    // the source file that was passed to the JsonReader as an argument during its instantiation
    @Test
    void testReaderTypicalToDoList() {
        JsonReader reader = new JsonReader("./data/testReaderTypicalToDoList.json");
        try {
            ToDoList toDoList = reader.read();
            assertEquals("A list of urgent tasks", toDoList.getName());
            List<Task> tasks = toDoList.getTasks();
            assertEquals(3, tasks.size());
            checkTask("finish MATH 101 Homework", Status.HARD, tasks.get(0));
            checkTask("print tomorrow's BIOL 200 slides", Status.HARD, tasks.get(1));
            checkTask("browse through reddit UBC for a good laugh", Status.HARD, tasks.get(2));
        } catch (IOException e) {
            fail("Couldn't read from file; IOException thrown but not expected");
        } catch (EmptyNameException e) {
            System.out.println("\n" + "name of to-do list has length of zero" + "\n");
        } catch (NullTaskException e) {
            System.out.println("\n" + "the task has not been initialized" + "\n");
        }
    }
}

// for phase 4 demo