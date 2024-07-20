package graphics;

import data.*;
import network.Client;
import network.Request;
import network.Response;
import network.ResponseStatus;
import utility.RequestBuilder;

import javax.swing.*;
import java.io.IOException;

public class InsertPersonWindow extends PersonIncludeWindow {
    private JPanel mainPanel;
    private JPanel namePanel;
    private JTextField nameTextField;
    private JLabel nameLabel;
    private JPanel coordinatesPanel;
    private JLabel coordinatesLabel;
    private JTextField coordinatesTextField;
    private JPanel heightPanel;
    private JLabel heightLabel;
    private JTextField heightTextField;
    private JPanel eyeColorPanel;
    private JLabel eyeColorLabel;
    private JComboBox<String> eyeColorSelector;
    private JComboBox<String> hairColorSelector;
    private JLabel hairColorLabel;
    private JPanel hairColorPanel;
    private JPanel countryPanel;
    private JLabel countryLabel;
    private JComboBox<String> countrySelector;
    private JPanel locationPanel;
    private JLabel locationLabel;
    private JTextField locationTextField;
    private JButton submitButton;
    private JPanel keyPanel;
    private JLabel keyLabel;
    private JTextField keyTextField;

    private String invalidArgumentsText;
    private String unexpectedException;

    private final Client client;
    private MainWindow mainWindow;

    public InsertPersonWindow(Client client, MainWindow mainWindow) {
        this.client = client;
        this.mainWindow = mainWindow;
    }

    @Override
    public void init() {
        defaultInit(mainPanel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        submitButton.addActionListener(e -> insert());
    }

    private void insert() {
        if (!checkArguments()) {
            JOptionPane.showMessageDialog(null, invalidArgumentsText);
            return;
        }

        Person person = new Person(
                nameTextField.getText(),
                new Coordinates(coordinatesTextField.getText()),
                Integer.parseInt(heightTextField.getText()),
                EyeColor.valueOf((String) eyeColorSelector.getSelectedItem()),
                HairColor.valueOf((String) hairColorSelector.getSelectedItem()),
                Country.valueOf((String) countrySelector.getSelectedItem()),
                new Location(locationTextField.getText())
        );

        Request request = new RequestBuilder.Builder()
                .setCommand("insert")
                .addToCommand(keyTextField.getText())
                .setPerson(person)
                .build();

        Response response = getResponse(request);

        mainWindow.getMessagesTextPane().setText(response.message());
        mainWindow.updateTable();
    }

    private Response getResponse(Request request) {
        try {
            return client.sendRequest(request);
        } catch (IOException e) {
            return new Response(unexpectedException, ResponseStatus.ERROR);
        }
    }
}
