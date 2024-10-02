import java.time.LocalDate;

/**
 * This interface is representing the specific features
 * that the GUI can handle. It is implemented into another class
 * where the class defines the methods and can execute on an action
 * depending on what the user asks.
 */
public interface Features {

  /**
   * Handles the creation of a new portfolio.
   *
   * @param portfolioName the name of the portfolio.
   */
  void handleCreatePortfolio(String portfolioName);

  /**
   * Handles fetching and adding a stock to a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param tickerSymbol  the tickerSymbol of the stock.
   * @param date          the date that the stock is added.
   * @param quantity      the quantity added.
   */
  void handleFetchAndAddStock(String portfolioName, String tickerSymbol, LocalDate date,
                              int quantity);

  /**
   * Handles fetching and removing a stock to a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param tickerSymbol  the tickerSymbol of the stock.
   * @param date          the date that the stock is removed.
   * @param quantity      the quantity removed.
   */
  void handleFetchAndRemoveStock(String portfolioName, String tickerSymbol, LocalDate date,
                                 int quantity);

  /**
   * Handles calculating the value of a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param date          the date inputted by the user.
   */
  void handlePortfolioValue(String portfolioName, LocalDate date);

  /**
   * Handles calculating the composition of a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param date          the date inputted by the user.
   */
  void handlePortfolioComposition(String portfolioName, LocalDate date);

  /**
   * Handles saving an existing portfolio.
   *
   * @param portfolioName the name of the portfolio.
   */
  void handleSavePortfolio(String portfolioName);

  /**
   * Handles loading an existing portfolio.
   *
   * @param portfolioName the name of the portfolio.
   */
  void handleLoadPortfolio(String portfolioName);
}
