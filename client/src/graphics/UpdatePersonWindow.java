package graphics;

import data.*;
import network.Client;
import network.Request;
import network.Response;
import network.ResponseStatus;
import utility.RequestBuilder;

import javax.swing.*;
import java.io.IOException;

public class UpdatePersonWindow extends PersonIncludeWindow {
    private JPanel namePanel;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JPanel coordinatesPanel;
    private JLabel coordinatesLabel;
    private JTextField coordinatesTextField;
    private JPanel heightPanel;
    private JLabel heightLabel;
    private JTextField heightTextField;
    private JPanel eyeColorPanel;
    private JLabel eyeColorLabel;
    private JComboBox eyeColorSelector;
    private JPanel hairColorPanel;
    private JLabel hairColorLabel;
    private JComboBox hairColorSelector;
    private JPanel countryPanel;
    private JLabel countryLabel;
    private JComboBox countrySelector;
    private JPanel locationPanel;
    private JLabel locationLabel;
    private JTextField locationTextField;
    private JButton submitButton;
    private JPanel idPanel;
    private JLabel idLabel;
    private JTextField idTextField;
    private JPanel mainPanel;

    private String invalidArgumentsText;
    private String unexpectedException;

    private Client client;
    private MainWindow mainWindow;

    public UpdatePersonWindow(Client client, MainWindow mainWindow) {
        this.client = client;
        this.mainWindow = mainWindow;
    }

    @Override
    public void init() {
        defaultInit(mainPanel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        submitButton.addActionListener(e -> updatePerson());
    }

    private void updatePerson() {
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
                .setCommand("update")
                .addToCommand(idTextField.getText())
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
