package graphics;

import javax.swing.*;
import java.awt.*;

public abstract class Window extends JFrame {
    public abstract void init();
    public abstract void updateLanguage();

    protected void defaultInit(JPanel panel) {
        setContentPane(panel);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Dimension windowSize = panel.getPreferredSize();
        setBounds((screenSize.width - windowSize.width) / 2,
                (screenSize.height - windowSize.height) / 2,
                windowSize.width,
                windowSize.height);

        updateLanguage();
    }
}
