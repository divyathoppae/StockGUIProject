/**
 * The CannotSellException is thrown when the user sells more
 * stocks than bought. This is used throughout the code.
 */
public class CannotSellException extends Throwable {
  /**
   * Constructs an UnknownStockException with the given message.
   *
   * @param message the exception message
   */
  CannotSellException(String message) {
    super(message);

  }
}