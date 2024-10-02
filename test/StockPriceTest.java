import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * Test class for StockPrice Class.
 * This essentially just tests a series of getters
 * to confirm that it is getting the right value.
 */

public class StockPriceTest {

  private static final double DELTA = 0.00000001;


  // In these tests below, the basic getters are being tested.
  // Note: In the actual program: the data is being retrieved
  // from the csv file in which the values are never negative,
  // therefore, it is not being tested here.

  // testing that the correct data is fetched from the data
  @Test
  public void testGetDate() {
    LocalDate date = LocalDate.of(2023, 6, 5);
    StockPrice stockPrice = new StockPrice(date, 100.0, 110.0,
            95.0, 105.0, 1000000);
    assertEquals(date, stockPrice.getDate());
  }

  // testing that the right opening price is fetched from the data
  @Test
  public void testGetOpen() {
    double open = 100.0;
    StockPrice stockPrice = new StockPrice(LocalDate.of(2023, 6, 5),
            open, 110.0, 95.0, 105.0, 1000000);
    assertEquals(open, stockPrice.getOpen(), DELTA);
  }

  // testing that the right high price is fetched from the data
  @Test
  public void testGetHigh() {
    double high = 110.0;
    StockPrice stockPrice = new StockPrice(LocalDate.of(2023, 6, 5),
            100.0, high, 95.0, 105.0, 1000000);
    assertEquals(high, stockPrice.getHigh(), DELTA);
  }

  // testing that the right low price is fetched from the data

  @Test
  public void testGetLow() {
    double low = 95.0;
    StockPrice stockPrice = new StockPrice(LocalDate.of(2023, 6, 5),
            100.0, 110.0, low, 105.0, 1000000);
    assertEquals(low, stockPrice.getLow(), DELTA);
  }

  // testing that the right closing price is fetched from the data

  @Test
  public void testGetClose() {
    double close = 105.0;
    StockPrice stockPrice = new StockPrice(LocalDate.of(2023, 6, 5),
            100.0, 110.0, 95.0, close, 1000000);
    assertEquals(close, stockPrice.getClose(), DELTA);
  }

  // testing that the right volume is fetched from the data
  @Test
  public void testGetVolume() {
    long volume = 1000000;
    StockPrice stockPrice = new StockPrice(LocalDate.of(2023, 6, 5),
            100.0, 110.0, 95.0, 105.0, volume);
    assertEquals(volume, stockPrice.getVolume(), DELTA);
  }

  // testing that the correct data is fetched from the data
  // different date
  @Test
  public void testGetDateDifferent() {
    LocalDate date = LocalDate.of(2010, 6, 5);
    StockPrice stockPrice = new StockPrice(date, 100.0, 110.0, 95.0,
            105.0, 1000000);
    assertEquals(date, stockPrice.getDate());
  }

  // testing that the right opening price is fetched from the data
  // at boundary value
  @Test
  public void testGetOpenBoundary() {
    double open = Double.MAX_VALUE;
    StockPrice stockPrice = new StockPrice(LocalDate.of(2019, 3, 1),
            open, 110.0, 95.0, 105.0, 1000000);
    assertEquals(open, stockPrice.getOpen(), DELTA);
  }

  // testing that the right high price is fetched from the data
  // at boundary
  @Test
  public void testGetHighBoundary() {
    double high = Double.MAX_VALUE;
    StockPrice stockPrice = new StockPrice(LocalDate.of(2023, 6, 5),
            100.0, high, 95.0, 105.0, 1000000);
    assertEquals(high, stockPrice.getHigh(), DELTA);
  }

  // testing that the right low price is fetched from the data
  // at boundary
  @Test
  public void testGetLowBoundary() {
    double low = Double.MIN_VALUE;
    StockPrice stockPrice = new StockPrice(LocalDate.of(2023, 6, 5),
            100.0, 110.0, low, 105.0, 1000000);
    assertEquals(low, stockPrice.getLow(), DELTA);
  }

  // testing that the right closing price is fetched from the data
  // at boundary
  @Test
  public void testGetCloseBoundary() {
    double close = Double.MIN_VALUE;
    StockPrice stockPrice = new StockPrice(LocalDate.of(2023, 6, 5),
            100.0, 110.0, 95.0, close, 1000000);
    assertEquals(close, stockPrice.getClose(), DELTA);
  }

  // testing that the right volume is fetched from the data
  // at high boundary
  @Test
  public void testGetVolumeHighBoundary() {
    long volume = Long.MAX_VALUE;
    StockPrice stockPrice = new StockPrice(LocalDate.of(2023, 6, 5),
            100.0, 110.0, 95.0, 105.0, volume);
    assertEquals(volume, stockPrice.getVolume(), DELTA);
  }

  // testing that the right volume is fetched from the data
  // at low boundary
  @Test
  public void testGetVolumeLowBoundary() {
    long volume = Long.MIN_VALUE;
    StockPrice stockPrice = new StockPrice(LocalDate.of(2023, 6, 5),
            100.0, 110.0, 95.0, 105.0, volume);
    assertEquals(volume, stockPrice.getVolume(), DELTA);
  }
}