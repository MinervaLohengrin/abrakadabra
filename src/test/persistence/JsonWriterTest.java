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
// unit tests for methods in the JsonWriter class
public class JsonWriterTest extends JsonTest {

    private String nameOfList;

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //tests that open() throws a FileNotFoundException when it is invoked upon
    //by a JsonWriter whose destination file has an invalid file name
    @Test
    void testWriterInvalidFile() {
        try {
            ToDoList toDoList = new ToDoList("A list of tasks that I will likely procrastinate on");
            JsonWriter jsonWriter = new JsonWriter("./data/my\0illegal:fileName.json");
            jsonWriter.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        } catch (EmptyNameException exception) {
            fail("EmptyNameException thrown but not expected");
        }
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // tests that,
    //
    // when write() is invoked with
    // an empty to-do list passed as an argument,
    //
    // and read() is invoked upon
    // a JsonReader whose source file has the same name as the destination file of the JsonWriter upon which write() was previously invoked,
    //
    // the JsonReader will read in an empty to-do list
    @Test
    void testWriterEmptyToDoList() {
        try {
            ToDoList toDoList = new ToDoList("A list that does not contain any tasks");
            JsonWriter jsonWriter = new JsonWriter("./data/testWriterEmptyToDoList.json");
            jsonWriter.open();
            jsonWriter.write(toDoList);
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/testWriterEmptyToDoList.json");
            toDoList = jsonReader.read();
            nameOfList = "A list that does not contain any tasks";
            assertTrue(toDoList.getName().equalsIgnoreCase(nameOfList));
            assertEquals("A list that does not contain any tasks", toDoList.getName());
            assertEquals(0, toDoList.numTasks());
            // expected
        } catch (IOException e) {
            fail("IOException not expected but was thrown");
        } catch (EmptyNameException e) {
            System.out.println("\n" + "name of to-do list has length of zero" + "\n");
        } catch (NullTaskException e) {
            System.out.println("\n" + "the task has not been initialized" + "\n");
        }
    }

    // source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // tests that,
    //
    // when write() is invoked with
    // a non-empty to-do list passed as an argument,
    //
    // and read() is invoked upon
    // a JsonReader whose source file has the same name as the destination file of the JsonWriter upon which write() was previously invoked,
    //
    // the JsonReader will read in a to-do list that is identical to the non-empty to-do list that was passed to write() as an argument
    @Test
    void testWriterGeneralToDoList() {
        try {
            ToDoList toDoList = new ToDoList("A list of tasks for which I will probably procrastinate");
            try {
                toDoList.addTask(new Task("print tomorrow's BIOL 200 slides", Status.HARD));
                toDoList.addTask(new Task("finish writing my research paper", Status.HARD));
                toDoList.addTask(new Task("do the pre-readings for all of my courses", Status.HARD));
                toDoList.addTask(new Task("clean up the mess in my room", Status.HARD));
            } catch (NullTaskException e) {
                System.out.println("\n" + "task has not been initialized" + "\n");
            }
            JsonWriter writer = new JsonWriter("./data/testWriterTypicalToDoList.json");
            writer.open();
            writer.write(toDoList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterTypicalToDoList.json");
            toDoList = reader.read();
            nameOfList = "A list of tasks for which I will probably procrastinate";
            assertTrue(toDoList.getName().equalsIgnoreCase(nameOfList));
            // assertEquals("A list of tasks for which I will probably procrastinate", toDoList.getName());
            List<Task> tasks = toDoList.getTasks();
            assertEquals(4, tasks.size());
            checkTask("print tomorrow's BIOL 200 slides", Status.HARD, tasks.get(0));
            checkTask("finish writing my research paper", Status.HARD, tasks.get(1));
            checkTask("do the pre-readings for all of my courses", Status.HARD, tasks.get(2));
            checkTask("clean up the mess in my room", Status.HARD, tasks.get(3));

        } catch (IOException e) {
            fail("IOException not expected but was thrown");
        } catch (EmptyNameException e) {
            System.out.println("\n" + "name of to-do list has length of zero" + "\n");
        } catch (NullTaskException e) {
            System.out.println("\n" + "the task has not been initialized" + "\n");
        }
    }
}

// for phase 4 demo
