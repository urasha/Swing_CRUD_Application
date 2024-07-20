package graphics;

import utility.person.StandardPersonCreator;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class PersonIncludeWindow extends Window {
    private JTextField nameTextField;
    private JLabel nameLabel;
    private JLabel coordinatesLabel;
    private JTextField coordinatesTextField;
    private JLabel heightLabel;
    private JTextField heightTextField;
    private JLabel eyeColorLabel;
    private JComboBox<String> eyeColorSelector;
    private JComboBox<String> hairColorSelector;
    private JLabel hairColorLabel;
    private JLabel countryLabel;
    private JComboBox<String> countrySelector;
    private JLabel locationLabel;
    private JTextField locationTextField;
    private JButton submitButton;
    private JLabel keyLabel;

    private String invalidArgumentsText;
    private String unexpectedException;

    private MainWindow mainWindow;

    @Override
    public void init() {
        throw new RuntimeException("init method isn't overriding");
    }

    protected boolean checkArguments() {
        StandardPersonCreator personCreator = new StandardPersonCreator();
        return personCreator.validateName(nameTextField.getText()) &&
                personCreator.validateCoordinates(coordinatesTextField.getText()) &&
                personCreator.validateHeight(heightTextField.getText()) &&
                personCreator.validateEyeColor((String) eyeColorSelector.getSelectedItem()) &&
                personCreator.validateHairColor((String) hairColorSelector.getSelectedItem()) &&
                personCreator.validateNationality((String) countrySelector.getSelectedItem()) &&
                personCreator.validateLocation(locationTextField.getText());
    }

    @Override
    public void updateLanguage() {
        Locale locale = Locale.of(String.valueOf(mainWindow.getLanguageSelector().getSelectedItem()));
        ResourceBundle bundle = ResourceBundle.getBundle("resources.main", locale);

        setTitle(bundle.getString("another_title"));
        invalidArgumentsText = bundle.getString("invalidArgumentsText");
        unexpectedException = bundle.getString("unexpected_exception");
        nameLabel.setText(bundle.getString("name_label"));
        coordinatesLabel.setText(bundle.getString("coordinates_label"));
        heightLabel.setText(bundle.getString("height_label"));
        eyeColorLabel.setText(bundle.getString("eye_color_label"));
        hairColorLabel.setText(bundle.getString("hair_color_label"));
        countryLabel.setText(bundle.getString("country_label"));
        locationLabel.setText(bundle.getString("location_label"));
        keyLabel.setText(bundle.getString("key_label"));
        submitButton.setText(bundle.getString("replace_person_button"));
    }
}
