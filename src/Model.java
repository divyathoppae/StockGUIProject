import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Interface representing the model in the MVC pattern for a stock application.
 * The model is responsible for managing the data of the application.
 */
public interface Model {


  /**
   * Calculates the gain or loss of the stock between two dates.
   *
   * @param tickerSymbol the tickerSymbol being used in the calculation
   * @param startDate    the start date
   * @param endDate      the end date
   * @return the calculated gain or loss of that stock
   * @throws UnknownStockException if the stock symbol is not recognized
   * @throws InvalidDateException  if the dates are invalid
   * @throws IOException           if an I/O error occurs
   */
  double getGainOrLoss(String tickerSymbol, LocalDate startDate, LocalDate endDate)
          throws UnknownStockException, InvalidDateException, IOException;


  /**
   * Calculates the moving average of the stock over a specified number of days
   * ending on a given date.
   *
   * @param tickerSymbol the tickerSymbol being used in the calculation
   * @param date         the start date
   * @param days         the number of days that the calculation is solving for
   * @return the moving average of that stock
   * @throws UnknownStockException if the stock symbol is not recognized
   * @throws InvalidDateException  if the date is invalid
   * @throws IOException           if an I/O error occurs
   */
  double getMovingAverage(String tickerSymbol, LocalDate date, int days)
          throws UnknownStockException, InvalidDateException, IllegalArgumentException, IOException;

  /**
   * Checks if the stock's price has crossed over its moving average on a given date.
   *
   * @param tickerSymbol the tickerSymbol being used in the calculation
   * @param date         the date to check
   * @param days         the number of days that the crossover is checking for
   * @return true if the price has crossed over the moving average, false otherwise
   * @throws UnknownStockException if the stock symbol is not recognized
   * @throws InvalidDateException  if the date is not valid
   * @throws IOException           if an I/O error occurs
   */
  boolean isCrossover(String tickerSymbol, LocalDate date, int days)
          throws UnknownStockException, InvalidDateException, IOException;

  /**
   * Creates a new portfolio with the given name and adds it to the model.
   *
   * @param portfolioName the name of the portfolio to create
   * @throws InvalidPortfolioException if the portfolio name is invalid
   */
  void createPortfolio(String portfolioName) throws InvalidPortfolioException;

  /**
   * Adds a stock to a portfolio.
   *
   * @param tickerSymbol  the ticker symbol of the stock to be added
   * @param portfolioName the name of the portfolio
   * @param quantity      the quantity of the stock to be added
   * @param date          the date the stock is added
   * @throws UnknownStockException     if the stock symbol is not recognized
   * @throws InvalidPortfolioException if the portfolio name is invalid
   * @throws IOException               if an I/O error occurs
   */
  void addStockToPortfolio(String tickerSymbol, String portfolioName,
                           int quantity, LocalDate date)
          throws UnknownStockException, InvalidPortfolioException, IOException;


  /**
   * Calculates the total value of the specified portfolio on a give date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date for which the portfolio value is to be calculated
   *                      specified by the user
   * @return the total value of the portfolio on that date
   * @throws UnknownStockException     if the stock is not a a valid stock input
   * @throws InvalidPortfolioException if the portfolio name is invalid
   * @throws IOException               if an I/O error occurs
   */
  double getPortfolioValue(String portfolioName, LocalDate date) throws UnknownStockException,
          InvalidPortfolioException, IOException;

  /**
   * Checks if a portfolio exists from the map of portfolios.
   *
   * @param portfolioName the name of the portfolio
   * @return true if the portfolio exists, false otherwise
   */
  boolean isPortfolio(String portfolioName);

  /**
   * Getter for retrieving a portfolio.
   *
   * @param portfolioName the name of the portfolio
   * @return the portfolio object
   */
  Portfolio getPortfolio(String portfolioName);

  /**
   * Retrieves a stock by its ticker symbol.
   *
   * @param tickerSymbol the ticker symbol of the stock
   * @return the stock with the given ticker symbol, or null if not found
   */
  Stock getStock(String tickerSymbol);

  /**
   * Checks if a stock exists.
   *
   * @param tickerSymbol the ticker symbol of the stock
   * @return true if the stock exists, false otherwise
   */
  boolean isStock(String tickerSymbol);

  /**
   * Checks if the provided ticker symbol exists in the "tickerSymbols.csv" file.
   * This method reads the "tickerSymbols.csv" file located in the current working directory
   * and checks if the given ticker symbol is present. The file is expected to have
   * ticker symbols in the first column, with fields separated by commas.
   *
   * @param tickerSymbol The ticker symbol to check for existence in the file.
   * @return true if the ticker symbol exists in the file, false otherwise.
   */
  boolean checkTickerSymbol(String tickerSymbol);

  /**
   * Removes a stock from a portfolio.
   *
   * @param portfolioName the name of the portfolio
   * @param tickerSymbol  the ticker symbol of the stock to be removed
   * @param quantity      the quantity of the stock to be removed
   * @param date          the date the stock is removed
   * @throws UnknownStockException     if the stock symbol is not recognized
   * @throws InvalidPortfolioException if the portfolio name is invalid
   * @throws CannotSellException       if the user sells more stocks than bought
   * @throws IOException               if an I/O error occurs
   */
  void removeStockFromPortfolio(String portfolioName, String tickerSymbol, int quantity,
                                LocalDate date) throws UnknownStockException,
          InvalidPortfolioException, CannotSellException, IOException;

  /**
   * Retrieves the composition of a portfolio on a specific date.
   *
   * @param portfolioName the name of the portfolio
   * @param date          the date for which the composition is to be retrieved
   * @return a map of stocks Ticker Symbol Name and their quantities in the portfolio
   * @throws InvalidPortfolioException if the portfolio name is invalid
   */
  Map<String, Double> getPortfolioComposition(String portfolioName, LocalDate date)
          throws InvalidPortfolioException;

  /**
   * Retrieves the distribution of a portfolio on a specific date.
   *
   * @param portfolioName the name of the portfolio
   * @param date          the date for which the distribution is to be retrieved
   * @return a map of stock ticker symbols and their distribution percentages
   * @throws InvalidPortfolioException if the portfolio name is invalid
   * @throws UnknownStockException     if the stock symbol is not valid
   * @throws IOException               if an I/O error occurs
   */
  Map<String, Double> getPortfolioDistribution(String portfolioName, LocalDate date)
          throws InvalidPortfolioException, UnknownStockException, IOException;

  /**
   * Saves a portfolio to a file.
   *
   * @param portfolioName the name of the portfolio
   * @param filename      the name of the file to save the portfolio to
   * @throws IOException               if an I/O error occurs
   * @throws InvalidPortfolioException if the portfolio name is invalid
   * @throws IOException               if an I/O error occurs
   */
  void savePortfolio(String portfolioName, String filename)
          throws IOException, InvalidPortfolioException;

  /**
   * Rebalances a portfolio on a specific date based on new weights.
   *
   * @param portfolioName the name of the portfolio
   * @param rebalanceDate the date on which to rebalance the portfolio
   * @param newWeights    a map of stock ticker symbols and their new target weights
   * @throws InvalidPortfolioException if the portfolio name is invalid
   * @throws InvalidWeightException    if the weights are invalid
   * @throws UnknownStockException     if a stock symbol in the new weights is not recognized
   * @throws IOException               if an I/O error occurs
   */
  void rebalancePortfolio(String portfolioName, LocalDate rebalanceDate, Map<String,
          Double> newWeights) throws InvalidPortfolioException, InvalidWeightException,
          UnknownStockException, IOException;

  /**
   * Retrieves a portfolio.
   *
   * @param portfolioName the name of the portfolio
   * @throws IOException if an I/O error occurs
   */
  void retrievePortfolio(String portfolioName) throws IOException;

  /**
   * Determines the intervals for the bar chart.
   *
   * @param startDate the start date
   * @param endDate   the end date
   * @return a list of dates
   */
  List<LocalDate> getAdjustedDates(LocalDate startDate, LocalDate endDate);

  /**
   * Determines the date frequency and returns a string.
   *
   * @param startDate the local start date
   * @param endDate   the local end date
   * @return a String that represents either day, week, month, bi year or year
   */
  String determineDateFrequency(LocalDate startDate, LocalDate endDate);

}
