import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Portfolio class represents a collection of
 * stocks that is owned by a user.
 * This is used to calculate certain statistics about
 * the portfolio.
 */
public class Portfolio {
  private String name;
  private final Map<String, Integer> stocks;
  private final Map<String, List<Transaction>> transactions;

  /**
   * Constructs a Portfolio object with
   * the given name.
   *
   * @param name the name of the portfolio
   */
  public Portfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<>();
    this.transactions = new HashMap<>();
  }

  /**
   * Buys a specific number of shares of a specific stock on a specified date.
   *
   * @param stock    the stock to buy
   * @param quantity the number of shares to buy
   * @param date     the date of purchase
   */
  public void buyStock(Stock stock, double quantity, LocalDate date) {
    transactions.computeIfAbsent(stock.getTickerSymbol(), k -> new ArrayList<>())
            .add(new Transaction(stock.getTickerSymbol(), quantity, date, TransactionType.BUY));
  }

  /**
   * Sells a specific number of shares of a specific stock on a specified date.
   *
   * @param stock  the stock to sell
   * @param shares the number of shares to sell
   * @param date   the date of sale
   */
  public void sellShares(Stock stock, double shares, LocalDate date) {
    transactions.computeIfAbsent(stock.getTickerSymbol(), k -> new ArrayList<>())
            .add(new Transaction(stock.getTickerSymbol(), shares, date, TransactionType.SELL));
    transactions.computeIfAbsent(stock.getTickerSymbol(), k -> new ArrayList<>())
            .remove(new Transaction(stock.getTickerSymbol(), shares, date, TransactionType.BUY));
  }

  /**
   * Gets the composition of the portfolio on a specific date.
   *
   * @param date the date for which the composition is to be retrieved
   * @return a map of stocks ticker symbols and their quantities in the portfolio
   */
  public Map<String, Double> getComposition(LocalDate date) {
    Map<String, Double> composition = new HashMap<>();
    for (Map.Entry<String, List<Transaction>> entry : transactions.entrySet()) {
      double totalShares = 0;
      for (Transaction transaction : entry.getValue()) {
        if (!transaction.getDate().isAfter(date)) {
          totalShares += transaction.getType() == TransactionType.BUY
                  ? transaction.getShares() : -transaction.getShares();
        }
      }
      if (totalShares > 0) {
        composition.put(entry.getKey(), totalShares);
      }
    }
    return composition;
  }


  /**
   * Adds a stock to the portfolio
   * when the user instructs it to do so.
   *
   * @param stock    the stock ticker symbol
   * @param quantity the quantity of the stock (int)
   */
  public void addStock(Stock stock, int quantity) {
    stocks.put(stock.getTickerSymbol(), quantity);
  }

  /**
   * Calculates the total value of the specified portfolio on a give date.
   *
   * @param date the specifed date by the use to which they want the calculation done on
   * @return the total value of the portfolio on that date
   * @throws UnknownStockException if the stock symbol is not recognized
   * @throws IOException           if an I/O error occurs
   */
  public double getPortfolioValue(LocalDate date) throws UnknownStockException, IOException {
    Map<String, Double> composition = getComposition(date);
    double totalValue = 0.0;

    for (Map.Entry<String, Double> entry : composition.entrySet()) {
      String stock = entry.getKey();
      double quantity = entry.getValue();
      StockPrice stockPrice = new Stock(stock).getPrice(date);

      if (stockPrice != null) {
        totalValue += stockPrice.getClose() * quantity;
      }
    }

    return totalValue;
  }

  /**
   * Retrieves the value distribution of the portfolio on a specific date.
   *
   * @param date the date for which the value distribution is to be retrieved
   * @return a map of stock ticker symbols and their distribution percentages
   * @throws UnknownStockException if the stock symbol is not recognized
   * @throws IOException           if an I/O error occurs
   */
  public Map<String, Double> getValueDistribution(LocalDate date)
          throws UnknownStockException, IOException {
    Map<String, Double> composition = getComposition(date);

    Map<String, Double> valueDistribution = new HashMap<>();
    for (Map.Entry<String, Double> entry : composition.entrySet()) {
      String stock = entry.getKey();
      double quantity = entry.getValue();
      StockPrice stockPrice = new Stock(stock).getPrice(date);
      valueDistribution.put(stock, stockPrice.getClose() * quantity);
    }
    return valueDistribution;
  }

  /**
   * Gets the name of the portfolio.
   *
   * @return the name of the portfolio
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the stocks in the portfolio.
   *
   * @return a map of stocks ticker symbols and their quantities
   */
  public Map<String, Integer> getStocks() {
    return stocks;
  }

  /**
   * Saves the portfolio to a file.
   *
   * @param filename the name of the file to save the portfolio to
   * @throws IOException if an I/O error occurs
   */
  public void savePortfolio(String filename) throws IOException {
    try (Writer writer = new FileWriter(filename)) {
      writer.write("Portfolio Name: " + name + "\n");
      writer.write("Number of Stocks: " + transactions.size() + "\n");
      for (Map.Entry<String, List<Transaction>> entry : transactions.entrySet()) {
        String stock = entry.getKey();
        List<Transaction> transactionList = entry.getValue();
        writer.write("Stock: " + stock + "\n");
        writer.write("Transactions: " + transactionList.size() + "\n");
        for (Transaction transaction : transactionList) {
          writer.write(transaction.getType() + "," + transaction.getShares()
                  + "," + transaction.getDate() + "\n");
        }
      }
    }
  }

  /**
   * Loads the portfolio from a file.
   *
   * @param filename the name of the file to load the portfolio from
   * @throws IOException if an I/O error occurs or the file format is invalid
   */
  public void loadPortfolio(String filename) throws IOException {
    Portfolio portfolio = new Portfolio(filename.replace(".txt", ""));
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line = reader.readLine();
      if (line == null || !line.startsWith("Portfolio Name: ")) {
        throw new IOException("Invalid portfolio file");
      }
      this.name = line.substring(16);
      line = reader.readLine();
      if (line == null || !line.startsWith("Number of Stocks: ")) {
        throw new IOException("Invalid portfolio file");
      }
      int numberOfStocks = Integer.parseInt(line.substring(18));
      this.transactions.clear();
      for (int i = 0; i < numberOfStocks; i++) {
        line = reader.readLine();
        if (line == null || !line.startsWith("Stock: ")) {
          throw new IOException("Invalid portfolio file");
        }
        Stock stock = new Stock(line.substring(7));
        line = reader.readLine();
        if (line == null || !line.startsWith("Transactions: ")) {
          throw new IOException("Invalid portfolio file");
        }
        String numberOfTransactions1 = line.substring(14);
        int numberOfTransactions = Integer.parseInt(numberOfTransactions1);
        List<Transaction> transactionList = new ArrayList<>();
        for (int j = 0; j < numberOfTransactions; j++) {
          line = reader.readLine();
          if (line == null) {
            throw new IOException("Invalid portfolio file");
          }
          String[] parts = line.split(",");
          if (parts.length != 3) {
            throw new IOException("Invalid portfolio file");
          }
          TransactionType type = TransactionType.valueOf(parts[0]);
          double shares = Double.parseDouble(parts[1]);
          LocalDate date = LocalDate.parse(parts[2]);
          transactionList.add(new Transaction(stock.getTickerSymbol(), shares, date, type));
          portfolio.addStock(stock, numberOfStocks);
        }
        transactions.put(stock.getTickerSymbol(), transactionList);

      }
    } catch (UnknownStockException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Rebalances the portfolio on a specific date based on new weights.
   *
   * @param date               the date on which to rebalance the portfolio
   * @param targetDistribution a map of stock ticker symbols and their new target weights
   * @throws UnknownStockException if a stock symbol in the new weights is not recognized
   * @throws IOException           if an I/O error occurs
   */
  public void rebalance(LocalDate date, Map<String, Double> targetDistribution)
          throws UnknownStockException, IOException {
    double totalWeight = targetDistribution.values().stream()
            .mapToDouble(Double::doubleValue).sum();
    if (Math.abs(totalWeight - 1.0) > 0.0001) {
      throw new IllegalArgumentException("The target distribution weights must sum to 1.0");
    }
    double totalValue = getPortfolioValue(date);

    Map<Stock, Double> targetValues = new HashMap<>();
    for (Map.Entry<String, Double> entry : targetDistribution.entrySet()) {
      Stock stock = new Stock(entry.getKey());
      targetValues.put(stock, totalValue * entry.getValue());
    }

    Map<Stock, Double> newComposition = new HashMap<>();
    for (Map.Entry<Stock, Double> entry : targetValues.entrySet()) {
      Stock stock = entry.getKey();
      double targetValue = entry.getValue();
      StockPrice stockPrice = stock.getPrice(date);
      if (stockPrice != null) {
        double targetQuantity = targetValue / stockPrice.getClose();
        newComposition.put(stock, targetQuantity);
      } else {
        throw new UnknownStockException("Price for stock "
                + stock.getTickerSymbol() + " is not available on " + date);
      }
    }

    for (Stock stock : newComposition.keySet()) {
      if (!transactions.containsKey(stock.getTickerSymbol())) {
        transactions.put(stock.getTickerSymbol(), new ArrayList<>());
      }
      List<Transaction> stockTransactions = transactions.get(stock.getTickerSymbol());

      stockTransactions.addAll(0, (transactions.get(stock.getTickerSymbol())));

      double currentQuantity = getComposition(date).getOrDefault(stock.getTickerSymbol(),
              0.0);
      double targetQuantity = newComposition.get(stock);

      if (targetQuantity > currentQuantity) {
        double quantityToBuy = targetQuantity - currentQuantity;
        if (quantityToBuy > 0) {
          stockTransactions.add(new Transaction(stock.getTickerSymbol(), quantityToBuy,
                  date, TransactionType.BUY));
        }
      } else if (targetQuantity < currentQuantity) {
        double quantityToSell = currentQuantity - targetQuantity;
        if (quantityToSell > 0) {
          stockTransactions.add(new Transaction(stock.getTickerSymbol(), quantityToSell,
                  date, TransactionType.SELL));
        }
      }
    }
  }
}