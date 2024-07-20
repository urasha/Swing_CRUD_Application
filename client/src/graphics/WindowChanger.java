package graphics;

import java.awt.event.WindowAdapter;
import java.util.ArrayList;

public class WindowChanger {
    private static ArrayList<Window> activeWindows = new ArrayList<>();

    private static Window currentWindow;

    public static void changeWindow(Window window) {
        for (var win : activeWindows) {
            win.dispose();
        }

        currentWindow = window;
        activeWindows.add(currentWindow);
        window.init();
    }

    public static void openWindow(Window window) {
        if (activeWindows.stream().map(Object::getClass).toList().contains(window.getClass())) {
            for (var win : activeWindows) {
                if (win.getClass().equals(window.getClass())) {
                    win.dispose();
                    activeWindows.remove(win);
                    break;
                }
            }
        }

        activeWindows.add(window);
        window.init();
    }

    public static boolean isVisualisationOpened() {
        for (var win : activeWindows) {
            if (win.getClass().equals(VisualisationWindow.class)) {
                return true;
            }
        }

        return false;
    }

    public static void removeActiveWindow(Window window) {
        activeWindows.remove(window);
    }
}
