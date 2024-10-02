import java.time.LocalDate;

/**
 * The StockPrice class represents the price details
 * of a stock on a specific date.
 */
public class StockPrice {
  private final LocalDate date;
  private final double open;
  private final double high;
  private final double low;
  private final double close;
  private final long volume;

  /**
   * Constructs a StockPrice with the specified
   * details.
   *
   * @param date   the date of the price entry
   * @param open   the opening price
   * @param high   the highest price
   * @param low    the lowest price
   * @param close  the closing price
   * @param volume the trading volume
   */
  public StockPrice(LocalDate date, double open, double high, double low, double close,
                    long volume) {
    this.date = date;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
  }

  /**
   * Gets the date of the price entry.
   *
   * @return the date
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Gets the opening price.
   *
   * @return the opening price
   */
  public double getOpen() {
    return open;
  }

  /**
   * Gets the highest price.
   *
   * @return the highest price
   */
  public double getHigh() {
    return high;
  }

  /**
   * Gets the lowest price.
   *
   * @return the lowest price
   */
  public double getLow() {
    return low;
  }

  /**
   * Gets the closing price.
   *
   * @return the closing price
   */
  public double getClose() {
    return close;
  }

  /**
   * Gets the trading volume.
   *
   * @return the trading volume
   */
  public long getVolume() {
    return volume;
  }
}