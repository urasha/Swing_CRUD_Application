package utility;

/**
 * Object whose fields may be validating
 */
public interface Validatable {
    /**
     * Checks an object against specified criteria
     * @return Object validation result
     */
    boolean validate();
}
