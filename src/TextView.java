import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/**
 * Interface that holds all the methods for the text based interface that the StockView will need
 * to implement.
 */
public interface TextView extends View {
  /**
   * Prints the menu options for the user.
   */
  void printMenu();

  /**
   * Displays the gain or loss of a stock over a specified period.
   *
   * @param gainOrLoss The gain or loss amount.
   * @param startDate  The start date of the period.
   * @param endDate    The end date of the period.
   */
  void displayGainOrLoss(double gainOrLoss, LocalDate startDate, LocalDate endDate);

  /**
   * Displays the moving average of a stock for a specified number of days.
   *
   * @param movingAverage The calculated moving average.
   * @param days          The number of days over which the moving average is calculated.
   * @param date          The date for which the moving average is calculated.
   */
  void displayMovingAverage(double movingAverage, int days, LocalDate date);

  /**
   * Displays a crossover event for a stock on a specified date.
   *
   * @param date The date of the crossover event.
   */
  void displayCrossover(LocalDate date);

  /**
   * Displays a graph showing the portfolio value over time.
   *
   * @param portfolioName   The name of the portfolio.
   * @param startDate       The start date for the graph.
   * @param endDate         The end date for the graph.
   * @param portfolioValues A map of dates to portfolio values.
   * @param scale           The scale of the graph.
   */
  void displayTextChart(String portfolioName, LocalDate startDate, LocalDate endDate,
                        TreeMap<LocalDate, Double> portfolioValues,
                        double scale, String granularity);

  /**
   * Displays the value distribution of a portfolio on a specific date.
   *
   * @param distribution A map of stock names to their value percentages.
   * @param date         The date for which the distribution is displayed.
   */
  void displayPortfolioDistribution(Map<String, Double> distribution, LocalDate date);

}