package utility.person;

import data.*;

import java.util.*;
import java.util.function.Function;

/**
 * Standard implementation of Person Creator
 */
public class StandardPersonCreator implements PersonCreator {
    private final LinkedHashMap<String, Function<String, Boolean>> fieldToValidation = new LinkedHashMap<>();
    private boolean isInteractiveInput = true;
    private ArrayList<String> personParams = new ArrayList<>();

    public StandardPersonCreator() {
        // name, coordinates, height, eyeColor, hairColor, nationality, location
        fieldToValidation.put("Введите имя: ", this::validateName);
        fieldToValidation.put("Введите координаты (%s): ".formatted("x>-868;y>-230"), this::validateCoordinates);
        fieldToValidation.put("Введите рост (h>0): ", this::validateHeight);
        fieldToValidation.put("Введите цвет глаз (RED, ORANGE, WHITE, BROWN): ", this::validateEyeColor);
        fieldToValidation.put("Введите цвет волос (GREEN, ORANGE, WHITE): ", this::validateHairColor);
        fieldToValidation.put("Введите страну (USA, FRANCE, VATICAN, ITALY, JAPAN): ", this::validateNationality);
        fieldToValidation.put("Введите локацию (x;y;z): ", this::validateLocation);
    }

    /**
     * Begins entering fields for a new Person object
     * @return List of person's fields
     */
    private ArrayList<String> inputPersonData() {
        Scanner in = new Scanner(System.in);

        ArrayList<String> params = new ArrayList<>();
        for (String phrase : fieldToValidation.keySet()) {
            while (true) {
                System.out.print(phrase);
                String input = in.nextLine().trim();

                if (fieldToValidation.get(phrase).apply(input)) {
                    params.add(input);
                    break;
                }

                System.out.println("Недопустимое значение! Попробуйте ещё раз!");
            }
        }

        return params;
    }

    /**
     * Read fields for Person object from file input
     * @return List of person's fields
     */
    private ArrayList<String> readPersonData() {
        ArrayList<String> params = new ArrayList<>();
        int paramCounter = 0;
        for (String phrase : fieldToValidation.keySet()) {
            String input = personParams.get(paramCounter);

            if (fieldToValidation.get(phrase).apply(input)) {
                params.add(input);
                paramCounter++;
                continue;
            }

            throw new NoSuchElementException();
        }

        return params;
    }

    /**
     * Set all Person fields
     * @param personParams all Person fields
     */
    public void setPersonParams(ArrayList<String> personParams) {
        this.personParams = personParams;
    }

    /**
     * Set input mode
     * @param isInteractiveInput input by file or console
     */
    public void setIsInteractiveInput(boolean isInteractiveInput) {
        this.isInteractiveInput = isInteractiveInput;
    }

    @Override
    public Person create() {
        ArrayList<String> params = isInteractiveInput ? inputPersonData() : readPersonData();
        return new Person(
                params.get(0),
                new Coordinates(params.get(1)),
                Integer.parseInt(params.get(2)),
                EyeColor.valueOf(params.get(3)),
                HairColor.valueOf(params.get(4)),
                Country.valueOf(params.get(5)),
                new Location(params.get(6))
        );
    }

    @Override
    public boolean validateName(String arg) {
        return !arg.isEmpty();
    }

    @Override
    public boolean validateCoordinates(String arg) {
        try {
            String[] xy = arg.split(";");
            double x = Double.parseDouble(xy[0]);
            double y = Double.parseDouble(xy[1]);
            return x > -868 && y > -230;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean validateHeight(String arg) {
        try {
            return Integer.parseInt(arg) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean validateEyeColor(String arg) {
        try {
            EyeColor.valueOf(arg);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean validateHairColor(String arg) {
        try {
            HairColor.valueOf(arg);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean validateNationality(String arg) {
        try {
            Country.valueOf(arg);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean validateLocation(String arg) {
        try {
            String[] xyz = arg.split(";");
            Integer.parseInt(xyz[0]);
            Double.parseDouble(xyz[1]);
            Float.parseFloat(xyz[2]);
            return true;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return false;
        }

    }
}
