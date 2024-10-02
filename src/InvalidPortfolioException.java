/**
 * The InvalidPortfolioException is thrown when Portfolio is has an invalid name.
 * This is used throughout the code.
 */
public class InvalidPortfolioException extends Exception {

  /**
   * Constructs an InvalidPortfolioException with the given message.
   *
   * @param message the exception message
   */
  InvalidPortfolioException(String message) {
    super(message);

  }
}
