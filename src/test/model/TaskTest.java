package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.Status.FLEXIBLE;
import static model.Status.HARD;
import static org.junit.jupiter.api.Assertions.*;


// Unit tests for the methods in the Task class
public class TaskTest {
    private Task firstTask;
    private Task secondTask;
    private Task thirdTask;


    @BeforeEach
    public void runBefore() {
        firstTask = new Task("Drink Water", FLEXIBLE);
        secondTask = new Task("Do Laundry", FLEXIBLE);
        thirdTask = new Task("Take out the trash", HARD);
    }


    @Test
    public void testGetName() {
        assertEquals("Drink Water", firstTask.getName());
        assertEquals("Do Laundry", secondTask.getName());
        assertTrue(thirdTask.getName().contentEquals("Take out the trash"));
    }


    @Test
    public void testGetStatus() {
        assertEquals(FLEXIBLE, firstTask.getStatus());
        assertEquals(FLEXIBLE, secondTask.getStatus());
        assertEquals(HARD, thirdTask.getStatus());
    }


    @Test
    public void testChangeStatus() {
        firstTask.changeStatus();
        assertEquals(HARD, firstTask.getStatus());

        secondTask.changeStatus();
        assertEquals(HARD, secondTask.getStatus());

        thirdTask.changeStatus();
        assertEquals(FLEXIBLE, thirdTask.getStatus());
    }


    @Test
    public void testToString() {
        assertTrue(firstTask.toString().contentEquals("Drink Water  --->  flexible deadline"));
        assertTrue(secondTask.toString().contentEquals("Do Laundry  --->  flexible deadline"));
        assertTrue(thirdTask.toString().contentEquals("Take out the trash  --->  hard deadline"));
    }

}

// for phase 4 demo