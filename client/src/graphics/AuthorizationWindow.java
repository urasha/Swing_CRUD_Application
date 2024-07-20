package graphics;

import db.User;
import db.UserManager;
import network.Client;
import network.Request;
import network.Response;
import utility.RequestBuilder;

import javax.swing.*;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AuthorizationWindow extends Window {
    private JPanel mainPanel;
    private JPanel usernamePanel;
    private JPanel passwordPanel;
    private JPanel header;
    private JPanel buttonsPanel;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel headerText;
    private JLabel smallText;
    private JButton loginButton;
    private JButton registerButton;
    private JComboBox<String> languageSelector;
    private String successRegisterText;
    private String incorrectDataText;
    private String userExistsText;
    private String unexpectedException;

    private final Client client;

    public AuthorizationWindow() {
        client = new Client();
    }

    @Override
    public void init() {
        defaultInit(mainPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());
        languageSelector.addActionListener(e -> updateLanguage());
    }

    private void login() {
        try {
            Response response = makeRequest("login");

            switch (response.status()) {
                case ERROR -> JOptionPane.showMessageDialog(null, incorrectDataText);
                case AUTH -> {
                    UserManager.setCurrentUser(UserManager.getPossibleUser());
                    WindowChanger.changeWindow(new MainWindow());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, unexpectedException);
        }
    }

    private void register() {
        try {
            Response response = makeRequest("register");
            switch (response.status()) {
                case ERROR -> JOptionPane.showMessageDialog(null, userExistsText);
                case AUTH -> JOptionPane.showMessageDialog(null, successRegisterText);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, unexpectedException);
        }
    }

    private Response makeRequest(String commandType) throws IOException {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        UserManager.setPossibleUser(new User(username, password));

        Request request = new RequestBuilder.Builder()
                .setCommand(commandType)
                .addToCommand(username + " " + password)
                .build();

        return client.sendRequest(request);
    }

    @Override
    public void updateLanguage() {
        Locale locale = Locale.of(String.valueOf(languageSelector.getSelectedItem()));
        ResourceBundle bundle = ResourceBundle.getBundle("resources.auth", locale);

        headerText.setText(bundle.getString("welcome"));
        smallText.setText(bundle.getString("small_text"));
        usernameLabel.setText(bundle.getString("login"));
        passwordLabel.setText(bundle.getString("password"));
        loginButton.setText(bundle.getString("log_in"));
        registerButton.setText(bundle.getString("register"));
        successRegisterText = bundle.getString("success_register");
        incorrectDataText = bundle.getString("incorrect_data");
        userExistsText = bundle.getString("user_exists");
        unexpectedException = bundle.getString("unexpected_exception");

        setTitle(bundle.getString("title"));
    }
}
