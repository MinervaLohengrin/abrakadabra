package exception;

// represents an exception that is thrown when the task passed to deleteTask() as an argument
// is not in the list of tasks
public class NotInListException extends Exception {
    public NotInListException() {
        super();
    }
}

// for phase 4 demo