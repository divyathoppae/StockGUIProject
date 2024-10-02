import java.time.LocalDate;
import java.util.Map;

/**
 * Interface representing the view in the MVC pattern for a stock application.
 * The view is responsible for displaying information to the user and
 * collecting user input.
 */
public interface View {

  /**
   * Displays the value of a portfolio on a specific date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date for which the portfolio value is calculated.
   * @param value         The calculated value of the portfolio.
   */
  void displayPortfolioValue(String portfolioName, LocalDate date, double value);

  /**
   * Displays an error message to the user.
   *
   * @param message The error message to be displayed.
   */
  void displayError(String message);

  /**
   * Displays a message indicating that the requested stock was not found.
   */
  void displayStockNotFound();

  /**
   * Displays a generic message to the user.
   *
   * @param message The message to be displayed.
   */
  void displayMessage(String message);

  /**
   * Displays the composition of a portfolio on a specific date.
   *
   * @param composition A map of stocks to their quantities.
   * @param date        The date for which the composition is displayed.
   */
  void displayPortfolioComposition(Map<String, Double> composition, LocalDate date);

  /**
   * Displays a message indicating that a portfolio was created.
   *
   * @param s the message to be displayed.
   */
  void displayCreatePortfolio(String s);

  /**
   * Displays a message indicating that a stock was removed from the portfolio.
   *
   * @param quantity     the quantity of stock removed.
   * @param tickerSymbol the ticker symbol of the removed stock.
   */
  void displayFetchAndRemoveStock(int quantity, String tickerSymbol);

  /**
   * Displays a message indicating that a stock was added to the portfolio.
   *
   * @param quantity     the quantity of stock added.
   * @param tickerSymbol the ticker symbol of the removed stock.
   */
  void displayFetchAndAddStock(int quantity, String tickerSymbol);

  /**
   * Displays a message indicating that a portfolio was saved.
   *
   * @param filename the name of the file to which the portfolio was saved.
   */
  void displaySavePortfolio(String filename);

  /**
   * Displays a message indicating that a portfolio was loaded.
   *
   * @param filename the name of the file to which the portfolio was loaded.
   */
  void displayRetrievePortfolio(String filename);
}