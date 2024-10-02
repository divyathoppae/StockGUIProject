import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for portfolio class.
 * Testing all the methods in this class
 */
public class PortfolioTest {

  // tests that the constructor works how it supposed to
  // withing getting the name, showing that the there
  // are currently no stocks in the portfolio
  @Test
  public void testConstructor() {
    String portfolioName = "OOD";
    Portfolio portfolio = new Portfolio(portfolioName);
    assertEquals(portfolioName, portfolio.getName());
    assertTrue(portfolio.getStocks().isEmpty());
  }


  // tests that the stocks have been successfully each to each portfolio
  // one stocks
  @Test
  public void testAddStockOne() throws UnknownStockException, IOException {

    Portfolio portfolio = new Portfolio("Test Portfolio");

    Stock testStock = new Stock("AAPL");

    int quantity = 10;
    portfolio.addStock(testStock, quantity);

    Map<String, Integer> stocks = portfolio.getStocks();
    assertTrue(stocks.containsKey("AAPL"));
    assertEquals(quantity, stocks.get("AAPL").intValue());
  }


  // tests that the stocks have been successfully each to each portfolio
  // two stocks
  @Test
  public void testAddStockTwo() throws UnknownStockException, IOException {

    Portfolio portfolio = new Portfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("GOOG");

    int quantity1 = 15;
    int quantity2 = 35;

    portfolio.addStock(stock1, quantity1);
    portfolio.addStock(stock2, quantity2);

    Map<String, Integer> stocks = portfolio.getStocks();
    assertTrue(stocks.containsKey("AAPL"));
    assertTrue(stocks.containsKey("GOOG"));
    assertEquals(quantity1, stocks.get("AAPL").intValue());
    assertEquals(quantity2, stocks.get("GOOG").intValue());

  }

  // tests that the stocks have been successfully each to each portfolio
  // three stocks
  // different portfolios
  @Test
  public void testAddStockThreeStocksDifferentPortfolios() throws UnknownStockException,
          IOException {
    Portfolio portfolio = new Portfolio("OOD");
    Portfolio portfolio2 = new Portfolio("Fundies");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("GOOG");
    Stock stock3 = new Stock("C");

    int quantity1 = 15;
    int quantity2 = 35;
    int quantity3 = 40;

    portfolio.addStock(stock1, quantity1);
    portfolio.addStock(stock2, quantity2);
    portfolio2.addStock(stock3, quantity3);

    Map<String, Integer> stocks = portfolio.getStocks();
    assertTrue(stocks.containsKey("AAPL"));
    assertTrue(stocks.containsKey("GOOG"));
    assertEquals(quantity1, stocks.get("AAPL").intValue());
    assertEquals(quantity2, stocks.get("GOOG").intValue());

    Map<String, Integer> stocks2 = portfolio2.getStocks();
    assertTrue(stocks2.containsKey("C"));
    assertEquals(quantity3, stocks2.get("C").intValue());
  }


  // testing that the getter getName works fine
  @Test
  public void testGetName() {

    String portfolioName = "OOD";
    Portfolio portfolio = new Portfolio(portfolioName);

    String retrievedName = portfolio.getName();

    assertEquals(retrievedName, (portfolioName));
  }


  // testing that the getter getName works fin with 2 portfolios
  @Test
  public void testGetNameTwoPortfolios() {
    // defining portfolios
    String portfolioName1 = "OOD";
    Portfolio portfolio = new Portfolio(portfolioName1);

    String portfolioName2 = "Fundies";
    Portfolio portfolio2 = new Portfolio(portfolioName2);

    // calling getName
    String retrievedName = portfolio2.getName();

    // checking that the two are equal
    assertEquals(retrievedName, (portfolioName2));
  }

  // testing that the getStocks is working good
  // with returning the stocks of a portfolio
  // three stocks
  // also incorporated in other methods
  @Test
  public void testGetStocksThreeStocks() throws UnknownStockException, IOException {
    Portfolio portfolio = new Portfolio("Test Portfolio");

    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("GOOGL");
    Stock stock3 = new Stock("MSFT");


    portfolio.addStock(stock1, 10);
    portfolio.addStock(stock2, 5);
    portfolio.addStock(stock3, 10);

    // Retrieve the stocks from the portfolio
    Map<String, Integer> stocks = portfolio.getStocks();

    // Verify the stocks are stored correctly
    assertTrue(stocks.containsKey("AAPL"));
    assertTrue(stocks.containsKey("GOOGL"));
    assertTrue(stocks.containsKey("MSFT"));

    assertEquals(10, stocks.get("AAPL").intValue());
    assertEquals(5, stocks.get("GOOGL").intValue());
    assertEquals(10, stocks.get("MSFT").intValue());
  }

  // testing that the getStocks is working good
  // with returning the stocks of a portfolio
  // four stocks
  // multiple portfolios
  // ensuring that all the portfolios are saved even
  // if multiple are made
  @Test
  public void testGetStocksFourStocksMultiplePortfolios() throws UnknownStockException,
          IOException {
    // Create Portfolio objects
    Portfolio portfolio1 = new Portfolio("OOD");
    Portfolio portfolio2 = new Portfolio("Algo");

    // Create some stocks
    Stock stock1 = new Stock("AMZN");
    Stock stock2 = new Stock("GOOG");
    Stock stock3 = new Stock("MSFT");
    Stock stock4 = new Stock("AAPL");

    // Add stocks to the portfolios
    portfolio1.addStock(stock1, 10);
    portfolio1.addStock(stock2, 5);
    portfolio1.addStock(stock3, 20);

    portfolio2.addStock(stock2, 50);
    portfolio2.addStock(stock4, 20);

    // Get the map of stocks from each portfolio
    Map<String, Integer> retrievedStocks1 = portfolio1.getStocks();
    Map<String, Integer> retrievedStocks2 = portfolio2.getStocks();

    // Expected maps
    Map<String, Integer> expectedStocks1 = new HashMap<>();
    expectedStocks1.put("AMZN", 10);
    expectedStocks1.put("GOOG", 5);
    expectedStocks1.put("MSFT", 20);

    Map<String, Integer> expectedStocks2 = new HashMap<>();
    expectedStocks2.put("GOOG", 50);
    expectedStocks2.put("AAPL", 20);

    // Check if the retrieved map matches the expected map
    assertEquals(expectedStocks1, retrievedStocks1);
    assertEquals(expectedStocks2, retrievedStocks2);
  }

  // Test buyStock and getComposition methods
  @Test
  public void testBuyStockAndGetComposition() throws UnknownStockException, IOException {

    Portfolio portfolio = new Portfolio("OOD");

    Stock testStock = new Stock("AAPL");

    LocalDate date = LocalDate.of(2023, 1, 1);
    double quantity = 10.0;
    portfolio.buyStock(testStock, quantity, date);

    Map<String, Double> composition = portfolio.getComposition(date);
    assertTrue(composition.containsKey("AAPL"));
    assertEquals(quantity, composition.get("AAPL").doubleValue(), 0.0001);
  }

  // Test sellShares after buying stock
  @Test
  public void testSellShares() throws UnknownStockException, IOException {

    Portfolio portfolio = new Portfolio("Fundies");

    Stock testStock = new Stock("AAPL");

    LocalDate buyDate = LocalDate.of(2023, 1, 1);
    double buyQuantity = 10.0;
    portfolio.buyStock(testStock, buyQuantity, buyDate);

    Map<String, Double> compositionAfterBuy = portfolio.getComposition(buyDate);
    assertTrue(compositionAfterBuy.containsKey("AAPL"));
    assertEquals(buyQuantity, compositionAfterBuy.get("AAPL").doubleValue(), 0.0001);

    LocalDate sellDate = LocalDate.of(2023, 2, 1);
    double sellQuantity = 5.0;
    portfolio.sellShares(testStock, sellQuantity, sellDate);

    Map<String, Double> compositionAfterSell = portfolio.getComposition(sellDate);
    assertTrue(compositionAfterSell.containsKey("AAPL"));
    assertEquals(buyQuantity - sellQuantity, compositionAfterSell.get("AAPL")
            .doubleValue(), 0.0001);
  }

  // Test savePortfolio and loadPortfolio methods
  @Test
  public void testSaveAndLoadPortfolio() throws IOException, UnknownStockException {
    Portfolio portfolio = new Portfolio("TestPortfolio");
    Stock stockAAPL = new Stock("AAPL");
    Stock stockGOOGL = new Stock("GOOGL");

    portfolio.addStock(stockAAPL, 10);
    portfolio.addStock(stockGOOGL, 5);

    String filename = "testPortfolio.txt";
    portfolio.savePortfolio(filename);

    Portfolio loadedPortfolio = new Portfolio("LoadedPortfolio");
    loadedPortfolio.loadPortfolio(filename);

    // shows that the file was saved and retrieved, with the dating being the same
    assertEquals(portfolio.getComposition(LocalDate.now()).size(),
            loadedPortfolio.getComposition(LocalDate.now()).size());
  }


  // Test getValueDistribution method
  @Test
  public void testGetValueDistribution() throws UnknownStockException, IOException {
    Portfolio portfolio = new Portfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("GOOG");

    LocalDate date = LocalDate.of(2023, 10, 4);

    portfolio.buyStock(stock1, 10, date);
    portfolio.buyStock(stock2, 5, date);

    Map<String, Double> expectedDistribution = new HashMap<>();
    expectedDistribution.put("AAPL", 1736.6);
    expectedDistribution.put("GOOG", 681.35);

    assertEquals(expectedDistribution, portfolio.getValueDistribution(date));
  }

  // Test getPortfolioValue with transactions
  @Test
  public void testGetPortfolioValueWithTransactions() throws UnknownStockException, IOException {
    Portfolio portfolio = new Portfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("GOOG");

    LocalDate date = LocalDate.of(2023, 10, 4);

    stock1.addPrice(date, 150.0, 160.0, 145.0, 155.0, 200000);
    stock2.addPrice(date, 2500.0, 2550.0, 2480.0, 2530.0, 200000);

    portfolio.buyStock(stock1, 10, date);
    portfolio.buyStock(stock2, 5, date);
    portfolio.sellShares(stock1, 3, date);

    double expectedValue = 1896.9699999999998;

    assertEquals(expectedValue, portfolio.getPortfolioValue(date), 0.0001);
  }

  // Test getPortfolioValue method
  @Test
  public void testGetPortfolioValue() throws UnknownStockException, IOException {
    Portfolio portfolio = new Portfolio("OOD");
    Stock stockAAPL = new Stock("AAPL");
    Stock stockGOOGL = new Stock("GOOGL");

    LocalDate purchaseDate1 = LocalDate.of(2023, 1, 1);
    portfolio.buyStock(stockAAPL, 10, purchaseDate1);

    LocalDate purchaseDate2 = LocalDate.of(2023, 1, 2);
    portfolio.buyStock(stockGOOGL, 5, purchaseDate2);

    double value = portfolio.getPortfolioValue(LocalDate.of(2023, 1, 3));
    assertEquals(value, 1696.3, 0.000001);
  }

  // Test buyStock and getComposition methods
  // Multiple Stocks in two portfolio
  // another version
  @Test
  public void testBuyStockMultipleStocksInTwoPortfolio() throws UnknownStockException, IOException {
    // Create portfolios
    Portfolio portfolio1 = new Portfolio("Fundies");
    Portfolio portfolio2 = new Portfolio("OOD");

    Stock stock1 = new Stock("MSFT");
    Stock stock2 = new Stock("AMZN");
    Stock stock3 = new Stock("AAPL");

    LocalDate date = LocalDate.of(2015, 2, 14);
    portfolio1.buyStock(stock1, 15.0, date);
    portfolio1.buyStock(stock2, 20.0, date);
    portfolio2.buyStock(stock3, 30.0, date);

    Map<String, Double> compositionAfterBuy1 = portfolio1.getComposition(date);
    assertTrue(compositionAfterBuy1.containsKey("MSFT"));
    assertTrue(compositionAfterBuy1.containsKey("AMZN"));
    assertEquals(15.0, compositionAfterBuy1.get("MSFT"), 0.001);
    assertEquals(20.0, compositionAfterBuy1.get("AMZN"), 0.001);

    Map<String, Double> compositionAfterBuy2 = portfolio2.getComposition(date);
    assertTrue(compositionAfterBuy2.containsKey("AAPL"));
    assertEquals(30.0, compositionAfterBuy2.get("AAPL"), 0.001);

  }

  // Test rebalance method
  @Test
  public void testRebalance() throws UnknownStockException, IOException {
    Portfolio portfolio = new Portfolio("OOD");

    Stock appleStock = new Stock("AAPL");
    Stock googleStock = new Stock("GOOGL");
    Stock amazonStock = new Stock("AMZN");

    LocalDate initialDate = LocalDate.of(2023, 1, 1);
    portfolio.buyStock(appleStock, 10.0, initialDate);
    portfolio.buyStock(googleStock, 5.0, initialDate);
    portfolio.buyStock(amazonStock, 15.0, initialDate);

    Map<String, Double> initialComposition = portfolio.getComposition(initialDate);
    assertEquals(10.0, initialComposition.get("AAPL").doubleValue(), 0.001);
    assertEquals(5.0, initialComposition.get("GOOGL").doubleValue(), 0.001);
    assertEquals(15.0, initialComposition.get("AMZN").doubleValue(), 0.001);

    Map<String, Double> targetDistribution = new HashMap<>();
    targetDistribution.put("AAPL", 0.4);
    targetDistribution.put("GOOGL", 0.3);
    targetDistribution.put("AMZN", 0.3);

    LocalDate rebalanceDate = LocalDate.of(2023, 2, 1);
    portfolio.rebalance(rebalanceDate, targetDistribution);

    Map<String, Double> rebalancedComposition = portfolio.getComposition(rebalanceDate);

    double totalValue = portfolio.getPortfolioValue(rebalanceDate);
    double expectedAppleQuantity = (totalValue * 0.4) / appleStock.getPrice(rebalanceDate)
            .getClose();
    double expectedGoogleQuantity = (totalValue * 0.3) / googleStock.getPrice(rebalanceDate)
            .getClose();
    double expectedAmazonQuantity = (totalValue * 0.3) / amazonStock.getPrice(rebalanceDate)
            .getClose();

    assertEquals(expectedAppleQuantity, rebalancedComposition.get("AAPL").doubleValue(),
            0.001);
    assertEquals(expectedGoogleQuantity, rebalancedComposition.get("GOOGL").doubleValue(),
            0.001);
    assertEquals(expectedAmazonQuantity, rebalancedComposition.get("AMZN").doubleValue(),
            0.001);

  }
}