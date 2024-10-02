import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The model class for managing stocks and portfolios.
 * This class stores and retrieves stock and portfolio data.
 * Implements all the methods from the model interface
 */
public class StockModel implements Model {
  private final Map<String, Stock> stocks;
  private final Map<String, Portfolio> portfolios;

  /**
   * Constructs a new Model.
   * StockModel object with empty collections of stocks and portfolios.
   */
  public StockModel() {
    this.stocks = new HashMap<>();
    this.portfolios = new HashMap<>();
  }

  /**
   * Calculates the gain or loss of the stock between two dates.
   *
   * @param tickerSymbol the tickerSymbol being used in the calculation
   * @param startDate    the start date
   * @param endDate      the end date
   * @return the calculated gain or loss of that stock
   * @throws UnknownStockException if the stock symbol is not recognized
   * @throws InvalidDateException  if the dates are invalid
   */
  public double getGainOrLoss(String tickerSymbol, LocalDate startDate, LocalDate endDate) throws
          UnknownStockException, InvalidDateException {
    Stock stock = newStock(tickerSymbol);
    StockPrice startPrice = stock.getPrice(startDate);
    StockPrice endPrice = stock.getPrice(endDate);
    if (startPrice == null || endPrice == null) {
      throw new InvalidDateException("Price data not available for the given dates");
    }
    return endPrice.getClose() - startPrice.getClose();
  }

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
   */
  public double getMovingAverage(String tickerSymbol, LocalDate date, int days) throws
          UnknownStockException, InvalidDateException, IllegalArgumentException {
    double total = 0;
    int count = 0;
    Stock stock = newStock(tickerSymbol);
    for (int i = 0; i < days; i++) {
      LocalDate currentDate = date.minusDays(i);
      StockPrice price = stock.getPrice(currentDate);
      if (price != null) {
        total += price.getClose();
        count++;
      }
    }
    if (count == 0) {
      throw new InvalidDateException("Not enough data to calculate moving average");
    }
    return total / count;
  }

  /**
   * Checks if the stock's price has crossed over its moving average on a given date.
   *
   * @param tickerSymbol the tickerSymbol being used in the calculation
   * @param date         the date to check
   * @param days         the number of days that the crossover is checking for
   * @return true if the price has crossed over the moving average, false otherwise
   * @throws UnknownStockException if the stock symbol is not recognized
   * @throws InvalidDateException  if the date is not valid
   */
  public boolean isCrossover(String tickerSymbol, LocalDate date, int days) throws
          UnknownStockException, InvalidDateException {
    Stock stock = newStock(tickerSymbol);
    double movingAverage = 0;
    try {
      movingAverage = getMovingAverage(tickerSymbol, date, days);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException();
    }
    StockPrice price = stock.getPrice(date);
    return price != null && price.getClose() > movingAverage;
  }

  /**
   * Creates a new portfolio with the given name and adds it to the model.
   *
   * @param portfolioName the name of the portfolio to create
   * @throws InvalidPortfolioException if the portfolio name is invalid
   */
  public void createPortfolio(String portfolioName) throws InvalidPortfolioException {
    if (portfolios.containsKey(portfolioName)) {
      throw new InvalidPortfolioException("Portfolio name already exists.");
    }
    portfolios.putIfAbsent(portfolioName, new Portfolio(portfolioName));
  }

  /**
   * Adds a stock to a portfolio.
   *
   * @param tickerSymbol  the ticker symbol of the stock to be added
   * @param portfolioName the name of the portfolio
   * @param quantity      the quantity of the stock to be added
   * @param date          the date the stock is added
   * @throws UnknownStockException     if the stock symbol is not recognized
   * @throws InvalidPortfolioException if the portfolio name is invalid
   */
  @Override
  public void addStockToPortfolio(String tickerSymbol, String portfolioName,
                                  int quantity, LocalDate date)
          throws UnknownStockException, InvalidPortfolioException {
    // Check if the portfolio exists
    Portfolio portfolio = portfolios.get(portfolioName);
    if (portfolio == null) {
      throw new InvalidPortfolioException("Portfolio " + portfolioName + " does not exist.");
    }

    // Check if the ticker symbol is valid
    if (!checkTickerSymbol(tickerSymbol)) {
      throw new UnknownStockException("Stock " + tickerSymbol + " is not valid.");
    }

    newStock(tickerSymbol);
    // Add the stock to the portfolio
    portfolio.buyStock(getStock(tickerSymbol), quantity, date);
  }


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
  public double getPortfolioValue(String portfolioName, LocalDate date) throws
          UnknownStockException, InvalidPortfolioException, IOException {
    Portfolio portfolio = portfolios.get(portfolioName);
    if (portfolio != null) {
      return portfolio.getPortfolioValue(date);
    }
    throw new InvalidPortfolioException("Portfolio not found: " + portfolioName);
  }

  /**
   * Removes a stock from a portfolio.
   *
   * @param portfolioName the name of the portfolio
   * @param tickerSymbol  the ticker symbol of the stock to be removed
   * @param quantity      the quantity of the stock to be removed
   * @param date          the date the stock is removed
   * @throws UnknownStockException if the stock symbol is not recognized
   */
  @Override
  public void removeStockFromPortfolio(String portfolioName, String tickerSymbol, int quantity,
                                       LocalDate date) throws UnknownStockException,
          InvalidPortfolioException, CannotSellException {
    Portfolio portfolio = portfolios.get(portfolioName);
    if (portfolio == null) {
      throw new InvalidPortfolioException("Portfolio not found: " + portfolioName);
    }
    if (portfolio.getComposition(date).getOrDefault(tickerSymbol, 0.0) <= quantity) {
      throw new CannotSellException("You are selling more stocks than bought. Buy stocks first.");
    }
    newStock(tickerSymbol);
    portfolio.sellShares(getStock(tickerSymbol), quantity, date);
  }

  /**
   * Retrieves the composition of a portfolio on a specific date.
   *
   * @param portfolioName the name of the portfolio
   * @param date          the date for which the composition is to be retrieved
   * @return a map of stocks Ticker Symbol Name and their quantities in the portfolio
   * @throws InvalidPortfolioException if the portfolio name is invalid
   */
  public Map<String, Double> getPortfolioComposition(String portfolioName, LocalDate date) throws
          InvalidPortfolioException {
    Portfolio portfolio = portfolios.get(portfolioName);
    if (portfolio == null) {
      throw new InvalidPortfolioException("Portfolio not found: " + portfolioName);
    }

    return portfolio.getComposition(date);
  }

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
  public void rebalancePortfolio(String portfolioName, LocalDate rebalanceDate, Map<String,
          Double> newWeights) throws InvalidPortfolioException, InvalidWeightException,
          UnknownStockException, IOException {
    Portfolio portfolio = portfolios.get(portfolioName);
    if (portfolio == null) {
      throw new InvalidPortfolioException("Portfolio not found: " + portfolioName);
    }

    double totalWeight = newWeights.values().stream().mapToDouble(Double::doubleValue).sum();
    if (Math.abs(totalWeight - 1.0) > 1e-9) {
      throw new InvalidWeightException("Weights must sum to 1 (100%).");
    }

    portfolio.rebalance(rebalanceDate, newWeights);
  }

  /**
   * Saves a portfolio to a file.
   *
   * @param portfolioName the name of the portfolio
   * @param filename      the name of the file to save the portfolio to
   * @throws IOException               if an I/O error occurs
   * @throws InvalidPortfolioException if the portfolio name is invalid
   * @throws IOException               if an I/O error occurs
   */
  public void savePortfolio(String portfolioName, String filename) throws IOException,
          InvalidPortfolioException {
    Portfolio portfolio = portfolios.get(portfolioName);
    if (portfolio == null) {
      throw new InvalidPortfolioException("Portfolio not found: " + portfolioName);
    }

    portfolio.savePortfolio(filename);
  }

  /**
   * Retrieves a portfolio.
   *
   * @param portfolioName the name of the portfolio
   * @throws IOException if an I/O error occurs
   */
  public void retrievePortfolio(String portfolioName) throws IOException {
    Portfolio portfolio = new Portfolio(portfolioName);
    portfolio.loadPortfolio(portfolioName + ".txt");
    portfolios.putIfAbsent(portfolioName, portfolio);
    if (portfolioName == null) {
      throw new IOException("Portfolio not found: " + portfolioName);
    }
  }

  /**
   * Retrieves a stock by its ticker symbol.
   *
   * @param tickerSymbol the ticker symbol of the stock
   * @return the stock with the given ticker symbol, or null if not found
   */
  public Stock getStock(String tickerSymbol) {
    return stocks.get(tickerSymbol);
  }

  /**
   * Getter for retrieving a portfolio.
   *
   * @param portfolioName the name of the portfolio
   * @return the portfolio object
   */
  public Portfolio getPortfolio(String portfolioName) {
    return portfolios.get(portfolioName);
  }


  /**
   * Checks if a portfolio exists.
   *
   * @param portfolioName the name of the portfolio
   * @return true if the portfolio exists, false otherwise
   */
  public boolean isPortfolio(String portfolioName) {
    Portfolio portfolio = portfolios.get(portfolioName);
    return portfolio != null;
  }

  /**
   * Checks if a stock exists.
   *
   * @param tickerSymbol the ticker symbol of the stock
   * @return true if the stock exists, false otherwise
   */
  public boolean isStock(String tickerSymbol) {
    Stock stock = stocks.get(tickerSymbol);
    return stock != null;
  }

  /**
   * Fetches a stock by its ticker symbol,
   * adds it to the model,
   * and displays a success message.
   *
   * @param tickerSymbol the ticker symbol of the stock to fetch
   * @return the fetched stock, or null if the stock could not be fetched
   */
  private Stock newStock(String tickerSymbol) throws UnknownStockException {
    try {
      Stock stock = new Stock(tickerSymbol);
      stocks.putIfAbsent(tickerSymbol, stock);
      return stock;
    } catch (UnknownStockException | IOException e) {
      throw new UnknownStockException("Unknown Stock.");
    }

  }

  /**
   * Checks if the provided ticker symbol exists in the "tickerSymbols.csv" file.
   * This method reads the "tickerSymbols.csv" file located in the current working directory
   * and checks if the given ticker symbol is present. The file is expected to have
   * ticker symbols in the first column, with fields separated by commas.
   *
   * @param tickerSymbol The ticker symbol to check for existence in the file.
   * @return true if the ticker symbol exists in the file, false otherwise.
   */
  public boolean checkTickerSymbol(String tickerSymbol) {

    String currentDir = System.getProperty("user.dir");
    String relativePath = "tickerSymbols.csv";

    // Construct the absolute file path
    String filePath = currentDir + "/" + relativePath;

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        // Split the line into fields using the comma as a delimiter
        String[] fields = line.split(",");
        if (fields.length > 0) {
          // Assuming the ticker symbol is in the first column
          String tickerSymbolPoss = fields[0].trim();
          if (tickerSymbolPoss.equalsIgnoreCase(tickerSymbol)) {
            return true;
          }

        }
      }
    } catch (IOException e) {
      return false;
    }
    return false;
  }

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
  public Map<String, Double> getPortfolioDistribution(String portfolioName, LocalDate date)
          throws InvalidPortfolioException, UnknownStockException, IOException {
    Portfolio portfolio = portfolios.get(portfolioName);
    if (portfolio == null) {
      throw new InvalidPortfolioException("Portfolio not found: " + portfolioName);
    }


    return portfolio.getValueDistribution(date);
  }

  /**
   * Determines the appropriate date granularity (day, week, month, or year) based on the time span.
   *
   * @param startDate The start date of the time span.
   * @param endDate   The end date of the time span.
   * @return A string indicating the date granularity ("day", "week", "month", or "year").
   */
  public String determineDateFrequency(LocalDate startDate, LocalDate endDate) {
    long totalDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

    if (totalDays >= 30 * 6 * 30) {
      return "year";
    } else if (totalDays >= 30 * 30) {
      return "bi-year";
    } else if (totalDays >= 30 * 7) {
      return "month";
    } else if (totalDays >= 30) {
      return "week";
    } else {
      return "day";
    }

  }

  /**
   * Creates a list of dates that are adjusted based off a particular frequency of time such as
   * daily, weekly, monthly, biyearly and yearly.
   *
   * @param startDate the start date
   * @param endDate   the end date
   * @return a list that holds all the adjusted dates
   */
  public List<LocalDate> getAdjustedDates(LocalDate startDate, LocalDate endDate) {
    List<LocalDate> dates = new ArrayList<>();
    String granularity = determineDateFrequency(startDate, endDate);

    LocalDate currentDate = startDate;
    switch (granularity) {
      case "day":
        while (!currentDate.isAfter(endDate)) {
          dates.add(currentDate);
          currentDate = currentDate.plusDays(1);
        }
        break;
      case "week":
        while (!currentDate.isAfter(endDate)) {
          dates.add(currentDate);
          currentDate = currentDate.plusWeeks(1);
        }
        break;
      case "month":
        while (!currentDate.isAfter(endDate)) {
          dates.add(currentDate);
          currentDate = currentDate.plusMonths(1);
        }
        break;
      case "bi-year":
        while (!currentDate.isAfter(endDate)) {
          dates.add(currentDate);
          currentDate = currentDate.plusMonths(6);
        }
        break;
      case "year":
        while (!currentDate.isAfter(endDate)) {
          dates.add(currentDate);
          currentDate = currentDate.plusYears(1);
        }
        break;
      default:
        break;
    }
    return dates;
  }
}