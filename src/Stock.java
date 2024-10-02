import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * The Stock class represents a stock with its historical prices.
 */
public class Stock {
  private final String tickerSymbol;
  private final Map<LocalDate, StockPrice> historicalPrices;
  private static final StockDataProvider dataProvider = new AlphaVantageStockDataProvider();

  /**
   * Constructs a Stock object with the
   * given ticker symbol and then with that
   * information, can get its data.
   *
   * @param tickerSymbol the ticker symbol of the stock
   * @throws UnknownStockException if the stock symbol is not recognized
   * @throws IOException           if an I/O error occurs
   */
  public Stock(String tickerSymbol) throws UnknownStockException, IOException {
    this.tickerSymbol = tickerSymbol;
    this.historicalPrices = dataProvider.fetchHistoricalPrices(tickerSymbol);
  }

  /**
   * Gets the ticker symbol of the stock.
   *
   * @return the ticker symbol of the stock
   */
  public String getTickerSymbol() {
    return tickerSymbol;
  }

  /**
   * Adds a price entry to the stock's historical prices.
   *
   * @param date   the date of the price entry
   * @param open   the opening price
   * @param high   the highest price
   * @param low    the lowest price
   * @param close  the closing price
   * @param volume the trading volume
   */
  public void addPrice(LocalDate date, double open, double high, double low, double close,
                       long volume) {
    historicalPrices.put(date, new StockPrice(date, open, high, low, close, volume));
  }

  /**
   * Gets the price of the stock on a specific date.
   *
   * @param date the date of the price entry
   * @return the stock price on the given date, or null if not available
   */
  public StockPrice getPrice(LocalDate date) {
    return historicalPrices.get(date);
  }
}
