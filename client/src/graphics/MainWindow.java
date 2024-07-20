package graphics;

import data.*;
import db.UserManager;
import network.Client;
import network.Request;
import network.Response;
import network.ResponseStatus;
import utility.RequestBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainWindow extends Window {
    private JPanel mainPanel;
    private JPanel commandsPanel;
    private JButton clear;
    private JButton executeScript;
    private JButton filterGreater;
    private JTextPane messagesTextPane;
    private JScrollPane commandsPane;
    private JPanel footerPanel;
    private JPanel headerPanel;
    private JComboBox<String> languageSelector;
    private JButton exit;
    private JLabel user;
    private JPanel currentUserPanel;
    private JLabel currentUserLabel;
    private JButton help;
    private JButton info;
    private JButton insert;
    private JButton removeAll;
    private JButton removeAny;
    private JButton removeGreater;
    private JButton removeKey;
    private JButton removeLower;
    private JButton replaceIfLower;
    private JButton update;
    private JScrollPane messagesScrollPane;
    private JScrollPane tableScrollPane;
    private JTable collectionTable;
    private JButton show;
    private JButton visualiseButton;

    private String unexpectedException;

    private String enterDataText;
    private String dialogWindowTitle;

    private String idColumn;
    private String creationDateColumn;
    private String nameColumn;
    private String coordinatesColumn;
    private String heightColumn;
    private String eyeColorColumn;
    private String hairColorColumn;
    private String countryColumn;
    private String locationColumn;
    private String keyColumn;
    private String creatorColumn;

    private final Client client;

    public MainWindow() {
        client = new Client();
    }

    public JComboBox<String> getLanguageSelector() {
        return languageSelector;
    }

    @Override
    public void init() {
        defaultInit(mainPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        updateTable();

        user.setText(UserManager.getCurrentUser().username());

        languageSelector.addActionListener(e -> updateLanguage());

        info.addActionListener(e -> doStandardAction("info"));
        help.addActionListener(e -> doStandardAction("help"));
        exit.addActionListener(e -> exit());
        insert.addActionListener(e -> insert());
        show.addActionListener(e -> updateTable());
        clear.addActionListener(e -> {
            doStandardAction("clear");
            updateTable();
        });
        removeKey.addActionListener(e -> removeKey());
        removeAny.addActionListener(e -> removeAnyByHairColor());
        removeAll.addActionListener(e -> removeAllByHairColor());
        executeScript.addActionListener(e -> executeScript());
        removeGreater.addActionListener(e -> removeGreater());
        removeLower.addActionListener(e -> removeLower());
        update.addActionListener(e -> updatePerson());
        replaceIfLower.addActionListener(e -> replaceLower());
        filterGreater.addActionListener(e -> filterGreater());
        visualiseButton.addActionListener(e -> visualiseData());
    }

    private Response getResponse(Request request) {
        try {
            return client.sendRequest(request);
        } catch (IOException e) {
            return new Response(unexpectedException, ResponseStatus.ERROR);
        }
    }

    private void doStandardAction(String commandName) {
        Request request = new RequestBuilder.Builder()
                .setCommand(commandName)
                .build();

        Response response = getResponse(request);
        messagesTextPane.setText(response.message());
    }

    private void visualiseData() {
        ArrayList<Person> persons = new ArrayList<>();
        for (String line : getCollection().split(" ")) {
            String[] params = line.split(",");
            Person person = new Person(
                    params[2],
                    new Coordinates(params[3]),
                    Integer.parseInt(params[4]),
                    EyeColor.valueOf(params[5]),
                    HairColor.valueOf(params[6]),
                    Country.valueOf(params[7]),
                    new Location(params[8]));
            person.setId(Integer.parseInt(params[0]));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(params[1], formatter);
            person.setCreationDate(localDate);

            person.setCreator(params[10]);
            persons.add(person);
        }

        WindowChanger.openWindow(new VisualisationWindow(persons));
    }

    private void exit() {
        System.exit(-1);
    }

    private void insert() {
        WindowChanger.openWindow(new InsertPersonWindow(client, this));
    }

    private void updatePerson() {
        WindowChanger.openWindow(new UpdatePersonWindow(client, this));
    }

    private void replaceLower() {
        WindowChanger.openWindow(new ReplaceIfLowerWindow(client, this));
    }

    private void filterGreater() {
        String height = JOptionPane.showInputDialog(mainPanel, enterDataText);
        Request request = new RequestBuilder.Builder()
                .setCommand("filter_greater_than_height")
                .addToCommand(height)
                .build();

        Response response = getResponse(request);
        updateTable(response.message());
    }

    private void removeKey() {
        String key = JOptionPane.showInputDialog(mainPanel, enterDataText);
        Request request = new RequestBuilder.Builder()
                .setCommand("remove_key")
                .addToCommand(key)
                .build();

        Response response = getResponse(request);
        messagesTextPane.setText(response.message());
        updateTable();
    }

    private void removeAnyByHairColor() {
        String[] colors = {"GREEN", "ORANGE", "WHITE"};
        String selectedColor = (String) JOptionPane.showInputDialog(
                mainPanel,
                enterDataText,
                dialogWindowTitle,
                JOptionPane.QUESTION_MESSAGE,
                null,
                colors,
                colors[0]
        );

        Request request = new RequestBuilder.Builder()
                .setCommand("remove_any_by_hair_color")
                .addToCommand(selectedColor)
                .build();

        Response response = getResponse(request);
        messagesTextPane.setText(response.message());
        updateTable();
    }

    private void removeAllByHairColor() {
        String[] colors = {"GREEN", "ORANGE", "WHITE"};
        String selectedColor = (String) JOptionPane.showInputDialog(
                mainPanel,
                enterDataText,
                dialogWindowTitle,
                JOptionPane.QUESTION_MESSAGE,
                null,
                colors,
                colors[0]
        );

        Request request = new RequestBuilder.Builder()
                .setCommand("remove_all_by_hair_color")
                .addToCommand(selectedColor)
                .build();

        Response response = getResponse(request);
        messagesTextPane.setText(response.message());
        updateTable();
    }

    private void executeScript() {
        String filename = JOptionPane.showInputDialog(mainPanel, enterDataText);

        Request request = new RequestBuilder.Builder()
                .setCommand("execute_script")
                .addToCommand(filename)
                .build();

        Response response = getResponse(request);
        messagesTextPane.setText(response.message());
    }

    private void removeGreater() {
        String name = JOptionPane.showInputDialog(mainPanel, enterDataText);

        Person comparingPerson = new Person(name, null, 1, null, null, null, null);

        Request request = new RequestBuilder.Builder()
                .setCommand("remove_greater")
                .setPerson(comparingPerson)
                .build();

        Response response = getResponse(request);
        messagesTextPane.setText(response.message());
        updateTable();
    }

    private void removeLower() {
        String name = JOptionPane.showInputDialog(mainPanel, enterDataText);

        Person comparingPerson = new Person(name, null, 1, null, null, null, null);

        Request request = new RequestBuilder.Builder()
                .setCommand("remove_lower")
                .setPerson(comparingPerson)
                .build();

        Response response = getResponse(request);
        messagesTextPane.setText(response.message());
        updateTable();
    }

    private String getCollection() {
        Request request = new RequestBuilder.Builder()
                .setCommand("show")
                .build();

        return getResponse(request).message();
    }

    public void updateTable(String collection) {
        updateDefault(collection);
    }

    public void updateTable() {
        updateDefault(getCollection());
    }

    private void updateDefault(String collection) {
        String[] columnNames = {idColumn, creationDateColumn, nameColumn, coordinatesColumn, heightColumn, eyeColorColumn, hairColorColumn, countryColumn, locationColumn, keyColumn, creatorColumn};

        String[][] data = {};

        if (!collection.equals("Коллекция пуста")) {
            data = Arrays.stream(collection.split(" "))
                    .map(row -> row.split(","))
                    .toArray(String[][]::new);
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        collectionTable.setModel(model);

        if (WindowChanger.isVisualisationOpened()) {
            visualiseData();
        }
    }

    @Override
    public void updateLanguage() {
        Locale locale = Locale.of(String.valueOf(languageSelector.getSelectedItem()));
        ResourceBundle bundle = ResourceBundle.getBundle("resources.main", locale);

        setTitle(bundle.getString("main_title"));
        unexpectedException = bundle.getString("unexpected_exception");
        enterDataText = bundle.getString("enter_data");
        dialogWindowTitle = bundle.getString("dialog_window_title");
        help.setText(bundle.getString("help_button"));
        info.setText(bundle.getString("info_button"));
        insert.setText(bundle.getString("insert_button"));
        removeAll.setText(bundle.getString("remove_all_hair_button"));
        removeAny.setText(bundle.getString("remove_any_hair_button"));
        removeGreater.setText(bundle.getString("remove_greater_button"));
        removeKey.setText(bundle.getString("remove_key_button"));
        removeLower.setText(bundle.getString("remove_lower_button"));
        replaceIfLower.setText(bundle.getString("replace_lower_button"));
        update.setText(bundle.getString("update_button"));
        exit.setText(bundle.getString("exit"));
        show.setText(bundle.getString("show_button"));
        clear.setText(bundle.getString("clear_button"));
        executeScript.setText(bundle.getString("execute_script_button"));
        filterGreater.setText(bundle.getString("filter_button"));
        currentUserLabel.setText(bundle.getString("current_user"));
        idColumn = bundle.getString("idColumn");
        creationDateColumn = bundle.getString("creationDateColumn");
        nameColumn = bundle.getString("nameColumn");
        coordinatesColumn = bundle.getString("coordinates");
        heightColumn = bundle.getString("heightColumn");
        eyeColorColumn = bundle.getString("eyeColorColumn");
        hairColorColumn = bundle.getString("hairColorColumn");
        countryColumn = bundle.getString("countryColumn");
        locationColumn = bundle.getString("locationColumn");
        keyColumn = bundle.getString("keyColumn");
        creatorColumn = bundle.getString("creatorColumn");
        visualiseButton.setText(bundle.getString("visualise_button"));

        updateTable();
    }

    public JTextPane getMessagesTextPane() {
        return messagesTextPane;
    }
}
