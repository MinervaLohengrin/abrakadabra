package exception;

// represents an exception that is thrown when the task passed to addTask() as an argument
// is null
public class NullTaskException extends Exception {

    public NullTaskException() {
        super();
    }
}

// for phase 4 demo