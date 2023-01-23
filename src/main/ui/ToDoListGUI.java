package ui;

import exception.EmptyNameException;
import exception.NullTaskException;
import model.Status;
import model.Task;
import model.ToDoList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static model.Status.FLEXIBLE;
import static model.Status.HARD;


// source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ComboBoxDemoProject/src/components/ComboBoxDemo.java
// source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
// source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDialogRunnerProject/src/components/ListDialog.java

// Represents the Graphical User Interface for the project
public class ToDoListGUI extends JPanel {
    private static final String JSON_STORE = "./data/todolist.json";
    protected JList list;
    protected ToDoList toDoList;

    protected DefaultListModel listForPanelDisplay;

    protected static final String ADD_STRING = "Add task";
    protected static final String DELETE_STRING = "Delete task";
    protected static final String CHANGE_STATUS = "Change status of a task";
    protected static final String SAVE_APP = "Save application";
    protected static final String LOAD_APP = "Load application";

    protected JButton deleteButton;
    protected JButton addButton;
    protected JButton changeStatusButton;
    protected JButton saveButton;
    protected JButton loadButton;
    protected JTextField taskNameField;

    protected JScrollPane panelOfLists;
    protected JPanel buttonPanel;

    protected static List<Status> statusList;
    protected static List<String> nameList;

    protected JsonWriter jsonWriter;
    protected JsonReader jsonReader;

    // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
    // MODIFIES: this
    // EFFECTS: creates a panel that is divided into a scroll panel at the top and a panel of buttons at the bottom
    public ToDoListGUI() {
        super(new BorderLayout());

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        try {
            toDoList = new ToDoList("A list");
        } catch (EmptyNameException e) {
            System.out.println("\n" + "EmptyNameException: name of to-do list has length of zero" + "\n");
        }

        try {
            listForPanelDisplay = new ToDoList("To be displayed on the scroll panel");
        } catch (EmptyNameException e) {
            System.out.println("\n" + "EmptyNameException: name of to-do list has length of zero" + "\n");
        }

        list = new JList(listForPanelDisplay);

        nameList = new ArrayList<>();
        statusList = new ArrayList<>();

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(30);

        panelOfLists = new JScrollPane(list);

        addButtonsAndTextField();

        createPanel();
    }

    // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
    // MODIFIES: this
    // EFFECTS: puts various buttons and a text field onto a panel
    //          that is divided into a scroll panel at the top and a panel of buttons at the bottom
    private void createPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(addButton);
        buttonPanel.add(taskNameField);
        buttonPanel.add(deleteButton);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(changeStatusButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        // adds scroll panel
        add(panelOfLists, BorderLayout.CENTER);
        // adds panel of buttons
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
    // MODIFIES: this
    // EFFECTS: sets up and adds various buttons onto the panel instantiated by a call to ToDoListGUI;
    protected void addButtonsAndTextField() {
        addButton = new JButton(ADD_STRING);
        addButton.setActionCommand(ADD_STRING);
        addButton.addActionListener(new AddListener());

        deleteButton = new JButton(DELETE_STRING);
        deleteButton.setActionCommand(DELETE_STRING);
        deleteButton.addActionListener(new DeleteListener());

        changeStatusButton = new JButton(CHANGE_STATUS);
        changeStatusButton.setActionCommand(CHANGE_STATUS);
        changeStatusButton.addActionListener(new ChangeStatusListener());

        saveButton = new JButton(SAVE_APP);
        saveButton.setActionCommand(SAVE_APP);
        saveButton.addActionListener(new SaveListener());

        loadButton = new JButton(LOAD_APP);
        loadButton.setActionCommand(LOAD_APP);
        loadButton.addActionListener(new LoadListener());

        taskNameField = new JTextField(10);
    }

    // source: http://ryisnow.net/2018/11/21/how-to-play-audio-files-sound-effect-java-game-development-extra-3/
    // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
    class DeleteListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: specifies how the application responds when the delete button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {

            int index = list.getSelectedIndex();

            // if the user clicked on the delete button without selecting any task on the scroll panel
            if (index == -1) {
                ErrorSound errorSound = new ErrorSound();
                errorSound.setFile(errorSound.getSoundFilePath());
                errorSound.play();
            } else {
                toDoList.getTasks().remove(index);

                listForPanelDisplay.removeElementAt(index);

            }
        }
    }

    // source: http://ryisnow.net/2018/11/21/how-to-play-audio-files-sound-effect-java-game-development-extra-3/
    // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
    class AddListener implements ActionListener {
        private int numberOfSpaces;
        private char[] array;

        // MODIFIES: this
        // EFFECTS: specifies how the application responds when the add button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = taskNameField.getText();

            // if the user input is an empty string
            // or if there's already a task on the panel with the same name as the user input
            if (isInputEmpty(name) || toDoList.isTaskInListWithSameName(name)) {
                taskNameField.selectAll();
                ErrorSound errorSound = new ErrorSound();
                errorSound.setFile(errorSound.getSoundFilePath());
                errorSound.play();
            } else {
                nameList.add(name);
                StatusInput statusInput = new StatusInput();
                statusInput.createAndShowGUI();
            }

            // Reset the text field.
            taskNameField.setText("");
        }

        // EFFECTS: checks whether or not the String passed to this method as an argument
        //          is an empty string, or a string comprised of a bunch of spaces
        public boolean isInputEmpty(String input) {
            numberOfSpaces = 0;
            array = input.toCharArray();

            if (input.equalsIgnoreCase("")) {
                return true;
            }

            // checking to see if the user just entered a bunch of spaces
            for (char c : array) {
                if (Character.isSpaceChar(c)) {
                    numberOfSpaces++;
                }
            }

            // if numberOfSpaces is equal to the length of the user input, it means that the user
            // had just entered a bunch of spaces
            if (numberOfSpaces == input.length()) {
                return true;
            }

            return false;
        }


    }

    // source: http://ryisnow.net/2018/11/21/how-to-play-audio-files-sound-effect-java-game-development-extra-3/
    // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
    class ChangeStatusListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: specifies how the application responds when the change status button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            String updatedTaskOnPanel;
            String nameOfTaskToBeUpdated;
            String updatedStatus;

            int index = list.getSelectedIndex();

            // if the user clicked on the delete button without selecting a task in the scroll panel beforehand
            if (index == -1) {
                ErrorSound errorSound = new ErrorSound();
                errorSound.setFile(errorSound.getSoundFilePath());
                errorSound.play();
            } else {
                toDoList.getTasks().get(index).changeStatus();

                // store the name of the task (whose status is to be updated) in a variable
                nameOfTaskToBeUpdated = toDoList.getTasks().get(index).getName();
                // store the new status of the task (whose status is to be updated) in a variable
                updatedStatus = toDoList.getTasks().get(index).getStatus().toString();

                // delete the task from the scroll panel
                listForPanelDisplay.removeElementAt(index);

                // constructing the String representation of the updated task that is to be shown on the scroll panel
                updatedTaskOnPanel = nameOfTaskToBeUpdated.concat(" ---> " + updatedStatus + " deadline");

                // put the String representation of the updated task
                // onto the scroll panel
                // (without changing its location)
                listForPanelDisplay.insertElementAt(updatedTaskOnPanel, index);

                // make sure that the task whose status got updated remains selected after the update
                list.setSelectedIndex(index);
            }
        }
    }

    // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
    class SaveListener implements ActionListener {
        // MODIFIES: this
        // EFFECTS: specifies how the application responds when the save button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            // saves to-do list to file
            try {
                jsonWriter.open();
                jsonWriter.write(toDoList);
                jsonWriter.close();
                System.out.println("\n" + "Saved " + toDoList.getName() + " to " + JSON_STORE + "\n");
            } catch (FileNotFoundException fileNotFoundException) {
                getToolkit().beep();
                System.out.println("\n" + "Unable to write to file: " + JSON_STORE + "\n");
            }
        }
    }

    // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
    class LoadListener implements ActionListener {
        // MODIFIES: this
        // EFFECTS: specifies how the application responds when the load button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            // if the user has already added tasks to the current list
            if (!(toDoList.numTasks() == 0)) {
                ErrorSound errorSound = new ErrorSound();
                errorSound.setFile(errorSound.getSoundFilePath());
                errorSound.play();
            } else if (toDoList.numTasks() == 0) {
                // loads to-do list from file
                try {
                    toDoList = jsonReader.read();
                    System.out.println("\n" + "Loaded " + toDoList.getName() + " from " + JSON_STORE + "\n");
                } catch (IOException ioException) {
                    getToolkit().beep();
                    System.out.println("\n" + "Unable to read from file: " + JSON_STORE + "\n");
                } catch (EmptyNameException emptyNameException) {
                    System.out.println("\n" + "EmptyNameException: name of to-do list has length of zero" + "\n");
                } catch (NullTaskException nullTaskException) {
                    System.out.println("\n" + "NullTaskException: the task has not been initialized" + "\n");
                }
                reconfigureScrollPanel();
            }
        }

        // MODIFIES: this
        // EFFECTS: copies the contents of the to-do list that was just loaded from file
        //          into a temporary list,
        //          clears out the to-do list that was just loaded from file,
        //          takes the contents of the temporary list, and then adds them onto the Scroll Panel
        private void reconfigureScrollPanel() {
            String name;
            Status status;
            List<Task> temporaryList = new ArrayList<>();

            // copying the contents of the to-do list that was just loaded from file
            // into a temporary list, and then repopulating the nameList and statusList
            for (int i = 0; i < toDoList.numTasks(); i++) {
                temporaryList.add(toDoList.getTasks().get(i));
                nameList.add(toDoList.getTasks().get(i).getName());
                statusList.add(toDoList.getTasks().get(i).getStatus());
            }

            // clearing out the to-do list that was just loaded from file
            toDoList.clear();

            // taking each of the tasks in the temporary list
            // and adding them onto the Scroll Panel
            for (int i = 0; i < temporaryList.size(); i++) {
                name = temporaryList.get(i).getName();
                status = temporaryList.get(i).getStatus();
                listForPanelDisplay.addElement(name.concat(" ---> " + status.toString() + " deadline"));
            }

        }

    }

    // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
    // MODIFIES: this
    // EFFECTS: creates the window that displays the tasks that have been added to the to-do list
    //          and also displays various buttons
    private static void createAndShowGUI() {
        //Create and set up window
        JFrame frame = new JFrame("To Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the panel
        JComponent newContentPane = new ToDoListGUI();
        frame.setContentPane(newContentPane);

        //Display the window
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        createAndShowGUI();
    }

    // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ComboBoxDemoProject/src/components/ComboBoxDemo.java
    // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDialogRunnerProject/src/components/ListDialog.java
    class StatusInput extends JPanel implements ActionListener {
        protected JTextField statusField;
        protected JPanel buttonPanel;
        protected JButton confirmStatusButton;

        // MODIFIES: this
        // EFFECTS: constructs a panel that prompts user to enter the status of a task that is to be added
        public StatusInput() {
            super(new GridBagLayout());
            statusField = new JTextField(8);
            confirmStatusButton = new JButton("click on this after entering status");

            confirmStatusButton.setActionCommand("set status");

            statusField.addActionListener(this);
            confirmStatusButton.addActionListener(this);

            buttonPanel = new JPanel(new GridLayout(1, 1));

            buttonPanel.add(confirmStatusButton);

            this.add(statusField);
            this.add(buttonPanel);
        }

        // source: http://ryisnow.net/2018/11/21/how-to-play-audio-files-sound-effect-java-game-development-extra-3/
        // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
        // MODIFIES: this
        // EFFECTS: specifies how the application responds
        // when the user enters the status for the task that is to be added
        public void actionPerformed(ActionEvent e) {

            String statusInput = statusField.getText();

            String command = e.getActionCommand();

            if (firstTimeEnteringStatus(command) && statusInput.equalsIgnoreCase("HARD")) {
                statusList.add(HARD);
                updateListOfTasks(HARD);
            } else if (firstTimeEnteringStatus(command) && statusInput.equalsIgnoreCase("FLEXIBLE")) {
                statusList.add(FLEXIBLE);
                updateListOfTasks(FLEXIBLE);
            } else {
                ErrorSound errorSound = new ErrorSound();
                errorSound.setFile(errorSound.getSoundFilePath());
                errorSound.play();
            }

            // reset the text field
            statusField.setText("");
        }

        // EFFECTS: checks if this is the first time the user is setting the status of the task that is to be added
        private boolean firstTimeEnteringStatus(String command) {

            return (nameList.size() - statusList.size() == 1) && command.equalsIgnoreCase("set status");
        }

        // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
        // MODIFIES: this
        // EFFECTS: sets the status of the task to be added according to the inputs of the user
        private void updateListOfTasks(Status status) {

            int index = statusList.size() - 1;

            String newName = nameList.get(index);
            Status newStatus = statusList.get(index);
            try {
                toDoList.addTask(new Task(newName, newStatus));
            } catch (NullTaskException e) {
                System.out.println("\n" + "NullTaskException: the task has not been initialized" + "\n");
            }

            listForPanelDisplay.insertElementAt(newName.concat(" ---> " + newStatus.toString() + " deadline"), index);
        }

        // source: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ComboBoxDemoProject/src/components/ComboBoxDemo.java
        // EFFECTS: Creates the window that prompts user for the status of a task that is to be added
        protected void createAndShowGUI() {
            //Create and set up the window.
            JFrame frame = new JFrame("Designate a status for this new task as either 'hard' or 'flexible'");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            //Add contents to the window.
            frame.add(new StatusInput());
            //Display the window.
            frame.setMinimumSize(new Dimension(600, 350));
            frame.setMaximumSize(new Dimension(600, 350));
            frame.setVisible(true);
        }

    }

}


