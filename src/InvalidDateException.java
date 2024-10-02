/**
 * The InvalidDateException is thrown when the date is invalid.
 * This is used throughout the code.
 */
public class InvalidDateException extends Exception {

  /**
   * Constructs an InvalidDateException with the given message.
   *
   * @param message the exception message
   */
  InvalidDateException(String message) {
    super(message);

  }
}
