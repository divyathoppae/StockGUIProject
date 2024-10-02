/**
 * The UnknownStockException is thrown when stock data cannot be fetched.
 * This is used throughout the code.
 */
public class UnknownStockException extends Exception {

  /**
   * Constructs an UnknownStockException with the given message.
   *
   * @param message the exception message
   */
  UnknownStockException(String message) {
    super(message);

  }
}
