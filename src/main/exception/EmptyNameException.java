package exception;

// represents an exception that is thrown when the string passed to the ToDoList constructor as an argument
// is either an empty string or a string comprised of a bunch of spaces
public class EmptyNameException extends Exception {
    public EmptyNameException() {
        super();
    }
}

// for phase 4 demo

