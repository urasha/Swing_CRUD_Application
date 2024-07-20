package utility;

/**
 * Text editing/design
 */
public class Text {

    /**
     * Make given text italic
     * @param text Any text
     * @return Italic style text
     */
    public static String makeItalic(String text) {
        return "\033[3m%s\033[0m".formatted(text);
    }

    /**
     * Make given text bold
     * @param text Any text
     * @return Bold style text
     */
    public static String makeBold(String text) {
        return "\033[1m%s\033[0m".formatted(text);
    }
}
