/**
 * The InvalidWeightException is thrown when stock data cannot be fetched.
 * This is used throughout the code.
 */
public class InvalidWeightException extends Exception {

  /**
   * Constructs an InvalidWeightException with the given message.
   *
   * @param message the exception message
   */
  InvalidWeightException(String message) {
    super(message);

  }
}