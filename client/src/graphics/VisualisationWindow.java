package graphics;

import data.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VisualisationWindow extends Window {
    private final ArrayList<Person> people;

    public VisualisationWindow(ArrayList<Person> allPersons) {
        people = new ArrayList<>(allPersons);
    }

    @Override
    public void init() {
        setLocationRelativeTo(null);
        setContentPane(new VisualisationPanel());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Dimension windowSize = new Dimension(500, 500);
        setBounds((screenSize.width - windowSize.width) / 2,
                (screenSize.height - windowSize.height) / 2,
                windowSize.width,
                windowSize.height);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                WindowChanger.removeActiveWindow(VisualisationWindow.this);
            }
        });
    }

    @Override
    public void updateLanguage() {
    }

    public class VisualisationPanel extends JPanel {
        private Map<String, Color> creatorColors;
        private Map<Person, Point> currentPositions;
        private final Timer animationTimer;
        private final int animationDelay = 30;

        public VisualisationPanel() {
            assignColorsToCreators();
            initializeCurrentPositions();

            animationTimer = new Timer(animationDelay, e -> {
                updatePositions();
                repaint();
            });
            animationTimer.start();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleMouseClick(e.getX(), e.getY());
                }
            });
        }

        private void initializeCurrentPositions() {
            currentPositions = new HashMap<>();
            for (Person person : people) {
                currentPositions.put(person, new Point(-person.getHeight(), (int)person.getCoordinates().getY()));
            }
        }

        private void updatePositions() {
            for (Person person : people) {
                Point currentPos = currentPositions.get(person);
                int targetX = person.getCoordinates().getX().intValue();
                int targetY = (int) person.getCoordinates().getY();

                if (currentPos.x < targetX) {
                    currentPos.x = Math.min(currentPos.x + 5, targetX);
                }
                if (currentPos.y < targetY) {
                    currentPos.y = Math.min(currentPos.y + 5, targetY);
                } else if (currentPos.y > targetY) {
                    currentPos.y = Math.max(currentPos.y - 5, targetY);
                }

                currentPositions.put(person, currentPos);
            }
        }

        private void handleMouseClick(int x, int y) {
            for (Person person : people) {
                int size = person.getHeight();
                Point position = currentPositions.get(person);

                if (x >= position.x && x <= position.x + size && y >= position.y && y <= position.y + size) {
                    String info = "ID: %s\nИмя: %s\nДата создания: %s\nРост: %s\nЦвет глаз: %s\nЦвет волос: %s\nСтрана: %s\nЛокация: %s".formatted(
                            person.getId(),
                            person.getName(),
                            person.getCreationDate(),
                            person.getHeight(),
                            person.getEyeColor(),
                            person.getHairColor(),
                            person.getNationality(),
                            person.getLocation()
                    );
                    JOptionPane.showMessageDialog(VisualisationWindow.this, info);
                    break;
                }
            }
        }

        private void assignColorsToCreators() {
            creatorColors = new HashMap<>();
            for (Person person : people) {
                if (!creatorColors.containsKey(person.getCreator())) {
                    creatorColors.put(person.getCreator(), generateRandomColor());
                }
            }
        }

        private Color generateRandomColor() {
            Random random = new Random();
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);

            return new Color(red, green, blue);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (Person person : people) {
                Point position = currentPositions.get(person);
                int size = person.getHeight();
                Color color = creatorColors.get(person.getCreator());

                g.setColor(color);
                g.fillRect(position.x, position.y, size, size);
            }
        }
    }
}
