import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * An interface for fetching historical stock prices.
 * Used in relation to API
 */
public interface StockDataProvider {

  /**
   * Fetches historical stock prices for a given ticker symbol.
   *
   * @param tickerSymbol the ticker symbol of the stock
   * @return a map where the keys are dates and the values are the stock prices on those dates
   * @throws IOException           if an I/O error occurs
   * @throws UnknownStockException if the ticker symbol is not recognized
   */
  Map<LocalDate, StockPrice> fetchHistoricalPrices(String tickerSymbol)
          throws IOException, UnknownStockException;
}
