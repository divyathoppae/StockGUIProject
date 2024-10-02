import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * The view class for displaying stock and portfolio information to the user.
 * This class simply displays what the controller processes and requests.
 */
public class StockView implements TextView {

  /**
   * Displays the main menu to the user.
   */
  @Override
  public void printMenu() {
    System.out.println("1. Examine the gain or loss of a stock over a specified period.");
    System.out.println("2. Examine the x-day moving average of a stock for a specified date"
            + " and value of x.");
    System.out.println("3. Determine x-day crossovers for a specified stock over a date"
            + " range and value of x.");
    System.out.println("4. Find the value of a portfolio on a specific date.");
    System.out.println("5. Buy a new stock and add to a portfolio on a specific date.");
    System.out.println("6. Create a new portfolio.");
    System.out.println("7. Sell a stock and remove from portfolio on a specific date.");
    System.out.println("8. Get the composition of a portfolio.");
    System.out.println("9. Get the distribution of a portfolio.");
    System.out.println("10. Save an existing portfolio.");
    System.out.println("11. Retrieve an existing portfolio.");
    System.out.println("12. Rebalance a portfolio.");
    System.out.println("13. Create a graph of Performance over time of a portfolio.");
    System.out.println("0. Exit.");
    System.out.print("Enter your choice: ");
  }

  /**
   * Displays the gain or loss of a stock over a specified period.
   *
   * @param gainOrLoss the gain or loss
   * @param startDate  the start date
   * @param endDate    the end date
   */
  @Override
  public void displayGainOrLoss(double gainOrLoss, LocalDate startDate, LocalDate endDate) {
    System.out.printf("Gain/Loss from %s to %s: %.2f%n", startDate, endDate, gainOrLoss);
  }

  /**
   * Displays the moving average of a stock.
   *
   * @param movingAverage the moving average
   * @param days          the number of days
   * @param date          the date
   */
  @Override
  public void displayMovingAverage(double movingAverage, int days, LocalDate date) {
    System.out.printf("%d-day moving average on %s: %.2f%n", days, date, movingAverage);
  }

  /**
   * Displays a crossover on a specified date.
   *
   * @param date the date of the crossover
   */
  @Override
  public void displayCrossover(LocalDate date) {
    System.out.printf("Crossover on %s%n", date);
  }

  /**
   * Displays the value of a portfolio on a specific date.
   *
   * @param portfolioName the portfolio name
   * @param date          the date
   * @param value         the value of the portfolio
   */
  @Override
  public void displayPortfolioValue(String portfolioName, LocalDate date, double value) {
    System.out.printf("Value of portfolio '%s' on %s: %.2f%n", portfolioName, date, value);
  }

  /**
   * Displays an error message.
   *
   * @param message the error message
   */
  @Override
  public void displayError(String message) {
    System.out.println("Error: " + message);
  }

  /**
   * Displays a message indicating that the stock was not found.
   */
  @Override
  public void displayStockNotFound() {
    System.out.println("Stock not found.");
  }

  /**
   * Displays a general message to the user.
   *
   * @param message the message to display
   */
  @Override
  public void displayMessage(String message) {
    System.out.println(message);
  }

  /**
   * Adjusts the dates to ensure the graph has at least 5 lines but no more than 30 lines.
   *
   * @param startDate       the start date.
   * @param endDate         the end date.
   * @param portfolioValues represents all the portfolio values
   * @param scale           represents the scale that is used
   */

  @Override
  public void displayTextChart(String portfolioName, LocalDate startDate, LocalDate endDate,
                               TreeMap<LocalDate, Double> portfolioValues,
                               double scale, String granularity) {
    StringBuilder chart = new StringBuilder();
    DateTimeFormatter dateFormatter;

    switch (granularity) {
      case "day":
        dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        break;
      case "week":
        dateFormatter = DateTimeFormatter.ofPattern("W MMM yyyy"); // ISO week date format
        break;
      case "month":
      case "bi-year":
        dateFormatter = DateTimeFormatter.ofPattern("MMM yyyy");
        break;
      case "year":
        dateFormatter = DateTimeFormatter.ofPattern("yyyy");
        break;
      default:
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    chart.append("Performance of portfolio ").append(portfolioName)
            .append(" from ").append(startDate).append(" to ").append(endDate).append("\n\n");

    for (Map.Entry<LocalDate, Double> entry : portfolioValues.entrySet()) {
      String dateLabel = entry.getKey().format(dateFormatter);
      double value = entry.getValue();
      int asteriskCount = (int) (value / scale);
      chart.append(dateLabel).append(": ").append("*".repeat(asteriskCount)).append("\n");
    }

    chart.append("\nScale: * = ").append(scale).append(" units");

    displayMessage(chart.toString());
  }


  /**
   * Displays the composition of a portfolio on a specific date.
   *
   * @param composition A map of stocks ticker symbols to their quantities.
   * @param date        The date for which the composition is displayed.
   */
  public void displayPortfolioComposition(Map<String, Double> composition, LocalDate date) {
    System.out.println("Portfolio Composition on " + date + ":");
    for (Map.Entry<String, Double> entry : composition.entrySet()) {
      String stock = entry.getKey();
      double quantity = entry.getValue();
      System.out.println(stock + ": " + quantity);
    }
  }

  /**
   * Displays the value distribution of a portfolio on a specific date.
   *
   * @param distribution A map of stock names to their value percentages.
   * @param date         The date for which the distribution is displayed.
   */
  @Override
  public void displayPortfolioDistribution(Map<String, Double> distribution, LocalDate date) {
    System.out.println("Portfolio Distribution on " + date + ":");
    for (Map.Entry<String, Double> entry : distribution.entrySet()) {
      String stockSymbol = entry.getKey();
      double value = entry.getValue();
      System.out.println(stockSymbol + ": " + value);
    }
  }

  /**
   * Displays a message indicating that a portfolio was created.
   *
   * @param s the message to be displayed.
   */
  @Override
  public void displayCreatePortfolio(String s) {
    System.out.println("Portfolio " + s + " created successfully.");
  }

  /**
   * Displays a message indicating that a stock was removed from the portfolio.
   *
   * @param quantity     the quantity of stock removed.
   * @param tickerSymbol the ticker symbol of the removed stock.
   */
  @Override
  public void displayFetchAndRemoveStock(int quantity, String tickerSymbol) {
    System.out.println(quantity + " shares of stock " + tickerSymbol
            + " removed successfully.");
  }

  /**
   * Displays a message indicating that a stock was added to the portfolio.
   *
   * @param quantity     the quantity of stock added.
   * @param tickerSymbol the ticker symbol of the removed stock.
   */
  @Override
  public void displayFetchAndAddStock(int quantity, String tickerSymbol) {
    System.out.println(quantity + " shares of stock " + tickerSymbol
            + " added successfully.");
  }

  /**
   * Displays a message indicating that a portfolio was saved.
   *
   * @param filename the name of the file to which the portfolio was saved.
   */
  @Override
  public void displaySavePortfolio(String filename) {
    System.out.println("Portfolio saved successfully to " + filename);
  }

  /**
   * Displays a message indicating that a portfolio was loaded.
   *
   * @param filename the name of the file to which the portfolio was loaded.
   */
  @Override
  public void displayRetrievePortfolio(String filename) {
    System.out.println("Portfolio " + filename + " retrieved successfully.");
  }
}