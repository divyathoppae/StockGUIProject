import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This is the stock test class.
 * This tests all the methods in the stock.
 */
public class StockTest {
  private Stock stock;

  // sets up in the initial conditions for this tests
  @Before
  public void setUp() throws UnknownStockException, IOException {
    stock = new Stock("AAPL");
  }

  // tests the addPrice method for the stocks
  @Test
  public void testAddPriceGetPrice() throws UnknownStockException {
    stock.addPrice(LocalDate.now(), 100.0, 110.0, 90.0,
            105.0, 1000000);
    assertNotNull(stock.getPrice(LocalDate.now()));
  }

  // tests the getter for the get ticker symbol
  @Test
  public void testGetTickerSymbol() {
    assertEquals("AAPL", stock.getTickerSymbol());
  }

  // tests that the addPrice method works with the duplicate date
  @Test
  public void testAddPriceGetPriceDuplicateDate() {
    LocalDate date = LocalDate.now();
    stock.addPrice(date, 100.0, 110.0, 90.0, 105.0, 1000000);
    stock.addPrice(date, 95.0, 105.0, 85.0, 100.0, 900000);
    StockPrice price = stock.getPrice(date);
    assertEquals(95.0, price.getOpen(), 0.001);
    assertEquals(105.0, price.getHigh(), 0.001);
    assertEquals(85.0, price.getLow(), 0.001);
    assertEquals(100.0, price.getClose(), 0.001);
    assertEquals(900000, price.getVolume());
  }

}