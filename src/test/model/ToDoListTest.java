package model;

import exception.EmptyNameException;
import exception.NotInListException;
import exception.NullTaskException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static model.Status.FLEXIBLE;
import static model.Status.HARD;
import static org.junit.jupiter.api.Assertions.*;


// Unit tests for the methods in the ToDoList class
public class ToDoListTest {
    private ToDoList toDoList;
    private ToDoList secondToDoList;
    private Task firstTask;
    private Task secondTask;
    private Task thirdTask;
    private Task fourthTask;
    private Task fifthTask;


    @BeforeEach
    public void runBefore() {
        try {
            toDoList = new ToDoList("A list of tasks that I will procrastinate on");
            // expected
        } catch (EmptyNameException e) {
            fail("EmptyNameException not expected but was thrown");
        }

        try {
            secondToDoList = new ToDoList("");
            fail("EmptyNameException expected but not thrown");
        } catch (EmptyNameException exception) {
            // expected
        }

        try {
            secondToDoList = new ToDoList("             ");
            fail("EmptyNameException expected but not thrown");
        } catch (EmptyNameException exception) {
            // expected
        }
    }


    @Test
    public void testGetName() {
        assertEquals("A list of tasks that I will procrastinate on", toDoList.getName());
    }


    @Test
    public void testAddTask() {
        firstTask = new Task("Clean my desk", HARD);

        secondTask = new Task("Order Pizza", HARD);

        thirdTask = new Task("Shovel snow in driveway", FLEXIBLE);

        try {
            toDoList.addTask(firstTask);
            toDoList.addTask(secondTask);
            toDoList.addTask(thirdTask);
            // expected
        } catch (NullTaskException e) {
            fail("NullTaskException not expected but thrown");
        }

        assertTrue(toDoList.getTasks().contains(firstTask));
        assertEquals(toDoList.getTasks().get(0).getName(), "Clean my desk");
        assertEquals(toDoList.getTasks().get(0).getStatus(), HARD);

        assertTrue(toDoList.getTasks().contains(secondTask));
        assertEquals(toDoList.getTasks().get(1).getName(), "Order Pizza");
        assertEquals(toDoList.getTasks().get(1).getStatus(), HARD);

        assertTrue(toDoList.getTasks().contains(thirdTask));
        assertEquals(toDoList.getTasks().get(2).getName(), "Shovel snow in driveway");
        assertEquals(toDoList.getTasks().get(2).getStatus(), FLEXIBLE);

        try {
            toDoList.addTask(fourthTask);
            fail("NullTaskException expected but not thrown");
        } catch (NullTaskException e) {
            // expected
        }
    }


    @Test
    public void testDeleteTask() {
        firstTask = new Task("Clean my desk", HARD);
        secondTask = new Task("Order Pizza", HARD);
        thirdTask = new Task("Shovel snow in driveway", FLEXIBLE);


        try {
            toDoList.addTask(firstTask);
            toDoList.addTask(secondTask);
            toDoList.addTask(thirdTask);
            // expected
        } catch (NullTaskException e) {
            fail("NullTaskException not expected but was thrown");
        }

        try {
            toDoList.deleteTask(firstTask);
            // expected
        } catch (NotInListException e) {
            fail("NotInListException not expected but was thrown");
        }
        assertEquals(2, toDoList.numTasks());
        assertFalse(toDoList.getTasks().contains(firstTask));

        try {
            toDoList.deleteTask(secondTask);
            // expected
        } catch (NotInListException e) {
            fail("NotInListException not expected but was thrown");
        }
        assertEquals(1, toDoList.numTasks());
        assertFalse(toDoList.getTasks().contains(secondTask));

        try {
            toDoList.deleteTask(thirdTask);
            // expected
        } catch (NotInListException e) {
            fail("NotInListException not expected but was thrown");
        }
        assertEquals(0, toDoList.numTasks());
        assertFalse(toDoList.getTasks().contains(thirdTask));

        try {
            toDoList.deleteTask(fourthTask);
            fail("NotInListException expected but not thrown");
        } catch (NotInListException e) {
            // expected
        }
    }


    @Test
    public void testGetTasks() {
        firstTask = new Task("Clean my desk", HARD);

        secondTask = new Task("Order Pizza", HARD);

        thirdTask = new Task("Shovel snow in driveway", FLEXIBLE);

        try {
            toDoList.addTask(firstTask);
            toDoList.addTask(secondTask);
            toDoList.addTask(thirdTask);
            // expected
        } catch (NullTaskException e) {
            fail("NullTaskException not expected but was thrown");
        }

        assertTrue(toDoList.getTasks().contains(firstTask));
        assertTrue(toDoList.getTasks().contains(secondTask));
        assertTrue(toDoList.getTasks().contains(thirdTask));
        assertEquals(toDoList.numTasks(), 3);

        try {
            toDoList.deleteTask(secondTask);
            // expected
        } catch (NotInListException e) {
            fail("NotInListException not expected but was thrown");
        }

        assertTrue(toDoList.getTasks().contains(firstTask));
        assertTrue(toDoList.getTasks().contains(thirdTask));
        assertFalse(toDoList.getTasks().contains(secondTask));
        assertEquals(toDoList.numTasks(), 2);
    }


    @Test
    public void testNumTasks() {
        firstTask = new Task("Clean my desk", HARD);
        secondTask = new Task("Order Pizza", HARD);
        thirdTask = new Task("Shovel snow in driveway", FLEXIBLE);

        try {
            toDoList.addTask(firstTask);
            toDoList.addTask(secondTask);
            toDoList.addTask(thirdTask);
            // expected
        } catch (NullTaskException e) {
            fail("NullTaskException not expected but was thrown");
        }

        assertEquals(toDoList.numTasks(), 3);

        try {
            toDoList.deleteTask(secondTask);
            // expected
        } catch (NotInListException e) {
            fail("NotInListException not expected but was thrown");
        }
        assertEquals(toDoList.numTasks(), 2);

        try {
            toDoList.deleteTask(firstTask);
            // expected
        } catch (NotInListException e) {
            fail("NotInListException not expected but was thrown");
        }
        assertEquals(toDoList.numTasks(), 1);

        try {
            toDoList.deleteTask(thirdTask);
            // expected
        } catch (NotInListException e) {
            fail("NotInListException not expected but was thrown");
        }
        assertEquals(toDoList.numTasks(), 0);
    }

    @Test
    public void testIsTaskInListWithSameName() {
        firstTask = new Task("Clean my desk", HARD);
        secondTask = new Task("Order Pizza", HARD);
        thirdTask = new Task("Shovel snow in driveway", FLEXIBLE);

        try {
            toDoList.addTask(firstTask);
            toDoList.addTask(secondTask);
            toDoList.addTask(thirdTask);
            // expected
        } catch (NullTaskException e) {
            fail("NullTaskException not expected but was thrown");
        }

        fourthTask = new Task("Order Pizza", FLEXIBLE);

        fifthTask = new Task("Drink water", HARD);

        assertTrue(toDoList.isTaskInListWithSameName(fourthTask.getName()));
        assertFalse(toDoList.isTaskInListWithSameName(fifthTask.getName()));
    }

}

// for phase 4 demo