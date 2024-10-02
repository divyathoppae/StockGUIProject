import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * MockView is a mock implementation of the View.View interface.
 * It stores messages that would be displayed to the user,
 * which can thenbe tested if it is the right output.
 */
public class MockView implements View, TextView {
  private final List<String> messages = new ArrayList<>();

  /**
   * Gets the list of messages that have
   * been used, based on the input.
   *
   * @return the list of messages
   */
  public List<String> getMessages() {
    return messages;
  }

  /**
   * Displaying the menu by
   * adding a message to the list.
   */
  @Override
  public void printMenu() {
    messages.add("Menu displayed");
  }

  /**
   * Simulates displaying the gain or loss
   * over a specified period.
   *
   * @param gainOrLoss the gain or loss amount
   * @param startDate  the start date of the period
   * @param endDate    the end date of the period
   */
  @Override
  public void displayGainOrLoss(double gainOrLoss, LocalDate startDate, LocalDate endDate) {
    messages.add(String.format("Gain/Loss from %s to %s: %.2f", startDate, endDate, gainOrLoss));
  }

  /**
   * Simulates displaying the moving average
   * for a specified number of days.
   *
   * @param movingAverage the moving average value
   * @param days          the number of days over which the moving average is calculated
   * @param date          the date for which the moving average is calculated
   */
  @Override
  public void displayMovingAverage(double movingAverage, int days, LocalDate date) {
    messages.add(String.format("%d-day moving average on %s: %.2f", days, date, movingAverage));
  }


  /**
   * Simulates displaying when a crossover has occured.
   *
   * @param date the to which the crossover occurred on
   */
  @Override
  public void displayCrossover(LocalDate date) {
    messages.add(String.format("Crossover on %s", date));
  }

  /**
   * Displaying the value of a portfolio on a specific date.
   *
   * @param portfolioName the name of the portfolio
   * @param date          the date for which the portfolio value is displayed
   * @param value         the value of the portfolio
   */
  @Override
  public void displayPortfolioValue(String portfolioName, LocalDate date, double value) {
    messages.add(String.format("Value of portfolio '%s' on %s: %.2f", portfolioName, date, value));
  }

  /**
   * Displays an error message, if necessary.
   *
   * @param message the error message to be displayed
   */
  @Override
  public void displayError(String message) {
    messages.add("Error: " + message);
  }


  /**
   * Displays a stock not found message,
   * if needed.
   */
  @Override
  public void displayStockNotFound() {
    messages.add("Stock not found.");
  }

  /**
   * Displays the appropiate message, depending
   * on the scenario.
   *
   * @param message the message to be displayed
   */
  @Override
  public void displayMessage(String message) {
    messages.add(message);
  }

  @Override
  public void displayTextChart(String portfolioName, LocalDate startDate, LocalDate endDate,
                               TreeMap<LocalDate, Double> portfolioValues, double scale,
                               String granularity) {
    StringBuilder chart = new StringBuilder();
    DateTimeFormatter dateFormatter;

    switch (granularity) {
      case "day":
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        break;
      case "week":
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // ISO week date format
        break;
      case "month":
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
   * Simulates displaying the composition of a portfolio on a specific date.
   *
   * @param composition a map of stocks to their quantities
   * @param date        the date for which the composition is displayed
   */
  @Override
  public void displayPortfolioComposition(Map<String, Double> composition, LocalDate date) {
    messages.add("Portfolio Composition on " + date + ":");
    String compositionString = composition.entrySet().stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining("\n"));
    messages.add(compositionString);
  }

  /**
   * Simulates displaying the value distribution of a portfolio on a specific date.
   *
   * @param distribution a map of stock names to their value percentages
   * @param date         the date for which the distribution is displayed
   */
  @Override
  public void displayPortfolioDistribution(Map<String, Double> distribution, LocalDate date) {
    messages.add("Portfolio Distribution on " + date + ":");
    String distributionString = distribution.entrySet().stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining("\n"));
    messages.add(distributionString);
  }

  /**
   * Displays a message indicating that a portfolio was loaded.
   *
   * @param filename the name of the file to which the portfolio was loaded.
   */
  @Override
  public void displayRetrievePortfolio(String filename) {
    messages.add("Portfolio " + filename + " retrieved successfully.");
  }

  /**
   * Displays a message indicating that a portfolio was saved.
   *
   * @param filename the name of the file to which the portfolio was saved.
   */
  @Override
  public void displaySavePortfolio(String filename) {
    messages.add("Portfolio saved successfully to " + filename);
  }

  /**
   * Displays a message indicating that a stock was added to the portfolio.
   *
   * @param quantity     the quantity of stock added.
   * @param tickerSymbol the ticker symbol of the removed stock.
   */
  @Override
  public void displayFetchAndAddStock(int quantity, String tickerSymbol) {
    messages.add(quantity + " shares of stock " + tickerSymbol + " added successfully.");
  }

  /**
   * Displays a message indicating that a stock was removed from the portfolio.
   *
   * @param quantity     the quantity of stock removed.
   * @param tickerSymbol the ticker symbol of the removed stock.
   */
  @Override
  public void displayFetchAndRemoveStock(int quantity, String tickerSymbol) {
    messages.add(quantity + " shares of stock " + tickerSymbol + " removed successfully.");
  }

  /**
   * Displays a message indicating that a portfolio was created.
   *
   * @param s the message to be displayed.
   */
  @Override
  public void displayCreatePortfolio(String s) {
    messages.add("Portfolio " + s + " created successfully.");
  }
}