import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This class contains test cases for the StockControllerImpl class.
 * It uses JUnit to verify the functionality of the stock management application.
 * The test cases cover various scenarios including:
 * - Creating a portfolio
 * - Adding stocks to a portfolio
 * - Fetching and handling portfolio values
 * - Handling edge cases and error scenarios
 * The mockModel and mockView are used to simulate the model and view
 * components of the application, allowing for isolated
 * testing of the controller logic.
 * Input streams are used to simulate user input.
 */
public class StockControllerImplTest {

  private MockModel mockModel;
  private MockView mockView;
  private StockControllerImpl controller;


  // setting up the scenario for the mock
  // initialize each test
  @Before
  public void setUp() {
    mockModel = new MockModel();
    mockView = new MockView();
    InputStream input = new ByteArrayInputStream("0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
  }

  // testing the display menu when it starts
  @Test
  public void testStartDisplaysMenu() {
    controller.start();
    assertTrue(mockView.getMessages().contains("Menu displayed"));
  }

  // tests case 1
  // this tests that the correct loss is calculated
  // over long period of time (11 months)
  @Test
  public void testLossOfStockLongPeriod() {
    InputStream input = new ByteArrayInputStream("1\nAAPL\n2022-01-03\n2022-12-28\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Gain/Loss from 2022-01-03 to 2022-12-28: -55.97"));
  }

  // tests that an error is given if the dates are not valid
  @Test
  public void testGainOfStockShortPeriod() {
    InputStream input = new ByteArrayInputStream("1\nGOOG\n2024-02-01\n2024-02-05\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Gain/Loss from 2024-02-01 to 2024-02-05: 2.22"));
  }


  // this tests that the correct gain is calculated
  // over short period of time
  // 2018-09-01 is not a valid date
  @Test
  public void testInvalidDatesForGainLossStock() {
    InputStream input = new ByteArrayInputStream("1\nAAPL\n2018-08-28\n2018-09-01\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();
    assertTrue(mockView.getMessages().contains("Price data not available for the given dates."));
  }

  // tests gain for a different company
  // earlier date
  @Test
  public void testGainDiffStock() {
    InputStream input = new ByteArrayInputStream("1\nC\n2002-07-31\n2003-01-09\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Gain/Loss from 2002-07-31 to 2003-01-09: 3.49"));
  }


  // testing handle moving average for medium period of time
  @Test
  public void testHandleMovingAverageMediumTime() {
    InputStream input = new ByteArrayInputStream("2\nAAPL\n2022-12-31\n50\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("50-day moving average on 2022-12-31: 141.68"));
  }

  // testing handle moving average for short period of time
  // and a diff company
  // not enough time, therefore throws an exception
  @Test
  public void testInvalidAmountOfTimeForAverage() {
    InputStream input = new ByteArrayInputStream("2\nGOOG\n2004-06-19\n0\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();
    assertTrue(mockView.getMessages()
            .contains("Not enough data to calculate moving average"));
  }

  // testing handle moving average for short period of time
  // and a diff company
  @Test
  public void testHandleMovingAverageShortTime() {
    InputStream input = new ByteArrayInputStream("2\nGOOG\n2014-06-19\n5\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("5-day moving average on 2014-06-19: 548.89"));
  }

  // testing handle moving average for very long period of time
  // and a diff company
  @Test
  public void testHandleMovingAverageLongTime() {
    InputStream input = new ByteArrayInputStream("2\nMSFT\n2016-11-29\n300\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("300-day moving average on 2016-11-29: 54.83"));
  }

  // testing handle moving average for over a year
  @Test
  public void testHandleMovingAverageOverYear() {
    InputStream input = new ByteArrayInputStream("2\nMSFT\n2016-11-29\n1000\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("1000-day moving average on 2016-11-29: 48.40"));
  }

  // testing crossover dates for normal period of time
  @Test
  public void testCrossoverNormalPeriodOfTime() {
    InputStream input = new ByteArrayInputStream("3\nAAPL\n2022-01-01\n2022-02-15\n10\n0\n"
            .getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();
    List<String> actualMessages = mockView.getMessages();

    List<String> expectedMessages = Arrays.asList(
            "Crossover on 2022-01-03",
            "Crossover on 2022-01-04",
            "Crossover on 2022-01-12",
            "Crossover on 2022-01-28",
            "Crossover on 2022-01-31",
            "Crossover on 2022-02-01",
            "Crossover on 2022-02-02",
            "Crossover on 2022-02-03",
            "Crossover on 2022-02-04",
            "Crossover on 2022-02-08",
            "Crossover on 2022-02-09",
            "Crossover on 2022-02-15"
    );

    assertTrue(actualMessages.containsAll(expectedMessages));
  }

  // testing crossover dates for 30 days - average
  // number of days is shorter than the period
  @Test
  public void testCrossoverNormal30Days() {
    InputStream input = new ByteArrayInputStream("3\nGOOG\n2017-07-18\n2017-08-30\n30\n0\n"
            .getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    List<String> actualMessages = mockView.getMessages();

    List<String> expectedMessages = Arrays.asList(
            "Crossover on 2017-07-18",
            "Crossover on 2017-07-19",
            "Crossover on 2017-07-20",
            "Crossover on 2017-07-21",
            "Crossover on 2017-07-24",
            "Crossover on 2017-07-25",
            "Crossover on 2017-07-26",
            "Crossover on 2017-07-28",
            "Crossover on 2017-08-23",
            "Crossover on 2017-08-29",
            "Crossover on 2017-08-30"
    );
    assertTrue(actualMessages.containsAll(expectedMessages));
  }

  // testing crossover dates for long period of time
  // confirming that smaller company stocks are still added - not just major
  // the number of days is longer than the period
  @Test
  public void testCrossoverLongPeriod() {
    InputStream input = new ByteArrayInputStream("3\nASR\n2022-04-01\n2022-05-02\n60\n0\n"
            .getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    List<String> actualMessages = mockView.getMessages();

    List<String> expectedMessages = Arrays.asList(
            "Crossover on 2022-04-01",
            "Crossover on 2022-04-04",
            "Crossover on 2022-04-05",
            "Crossover on 2022-04-19",
            "Crossover on 2022-04-26",
            "Crossover on 2022-04-27",
            "Crossover on 2022-04-28",
            "Crossover on 2022-04-29",
            "Crossover on 2022-05-02"
    );

    assertTrue(actualMessages.containsAll(expectedMessages));
  }

  // Confirming that when an option is selected the right messages come up
  // start date
  // crossover
  @Test
  public void testCrossoverPartialMessagesStartDate() {
    InputStream input = new ByteArrayInputStream("3\nAAPL\n2022-01-01\n2022-02-15\n10\n0\n"
            .getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Enter start date (yyyy-mm-dd): "));
  }

  // Confirming that when an option is selected the right messages come up
  // end date
  // crossover
  @Test
  public void testCrossoverPartialMessagesEndDate() {
    InputStream input = new ByteArrayInputStream(("3\nAAPL\n2022-01-01\n"
            + "2022-02-15\n10\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Enter end date (yyyy-mm-dd): "));
  }

  // Confirming that when an option is selected the right messages come up
  // start date
  // gain-loss stock
  @Test
  public void testGainLossPartialMessagesStartDate() {
    InputStream input = new ByteArrayInputStream("1\nGOOG\n2016-11-09\n2016-11-30\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Enter start date (yyyy-mm-dd): "));
  }

  // Confirming that when an option is selected the right messages come up
  // end date
  // gain-loss stock
  @Test
  public void testGainLossPartialMessagesEndDate() {
    InputStream input = new ByteArrayInputStream("1\nGOOG\n2016-11-09\n2016-11-30\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Enter end date (yyyy-mm-dd): "));
  }

  // Confirming that when an option is selected the right messages come up
  // start date
  // average moving day
  @Test
  public void testAverageMovingDayPartialMessagesStartDate() {
    InputStream input = new ByteArrayInputStream("2\nC\n2018-03-11\n10\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Enter the date (yyyy-mm-dd): "));
  }

  // Confirming that when an option is selected the right messages come up
  // number of days
  // average moving day
  @Test
  public void testAverageMovingDayPartialMessagesNumberOfDays() {
    InputStream input = new ByteArrayInputStream("2\nC\n2018-03-11\n10\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Enter number of days: "));
  }

  // Test case 4 in start
  // Test case for creating a portfolio and checking its value when no stock is added
  @Test
  public void testHandlePortfolioValueWhenNoStockAdded() {
    InputStream input = new ByteArrayInputStream(("6\nMyPortfolio\n4\nMyPortfolio"
            + "\n2022-12-31\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio 'MyPortfolio'"
            + " on 2022-12-31: 0.00"));
  }

  // Test case for checking the value of a non-existent portfolio
  @Test
  public void testHandlePortfolioValueNonExistingPortfolio() {
    InputStream input = new ByteArrayInputStream("4\nNonExistingPortfolio\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("No portfolio with name:"
            + " NonExistingPortfolio\n Create portfolio first."));
  }

  // Test case for checking the value of a portfolio with an invalid date
  @Test
  public void testHandlePortfolioValueInvalidDate() throws InvalidPortfolioException {
    mockModel.createPortfolio("MyPortfolio");
    InputStream input = new ByteArrayInputStream("4\nMyPortfolio\n2022-12-32\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Invalid day."));
  }

  // Test case for checking the value of a portfolio that doesn't exist
  @Test
  public void testHandlePortfolioValuePortfolioNotFound() {
    InputStream input = new ByteArrayInputStream("4\nUnknownPortfolio\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("No portfolio with name:"
            + " UnknownPortfolio\n Create portfolio first."));
  }

  // Test case for checking the value of a portfolio successfully
  // this would be 0 since it is before the stock was bought
  @Test
  public void testHandlePortfolioValueValueOfZero() throws UnknownStockException,
          InvalidPortfolioException, IOException {
    mockModel.createPortfolio("MyPortfolio");
    LocalDate date = LocalDate.of(2023, 6, 5);
    mockModel.addStockToPortfolio("AAPL", "MyPortfolio", 10, date);
    mockModel.addStockToPortfolio("GOOGL", "MyPortfolio", 5, date);


    InputStream input = new ByteArrayInputStream("4\nMyPortfolio\n2022-11-11\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio"
            + " 'MyPortfolio' on 2022-11-11: 0.00"));
  }

  // Test case for checking the value of a portfolio on the same date multiple times
  @Test
  public void testHandlePortfolioValueSameDate() throws UnknownStockException,
          InvalidPortfolioException, IOException {
    mockModel.createPortfolio("SameDatePortfolio");
    mockModel.newStock("AAPL");
    LocalDate date = LocalDate.of(2023, 6, 5);
    mockModel.addStockToPortfolio("AAPL", "SameDatePortfolio",
            10, date);

    InputStream input = new ByteArrayInputStream("4\nSameDatePortfolio\n2024-04-11\n0\n"
            .getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio"
            + " 'SameDatePortfolio' on 2024-04-11: 1750.40"));
  }

  // Test case for checking the value of a portfolio with multiple stocks
  @Test
  public void testHandlePortfolioValueMultipleStocks() throws UnknownStockException,
          InvalidPortfolioException, IOException {
    mockModel.createPortfolio("MultipleStocksPortfolio");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("GOOGL");
    LocalDate date = LocalDate.of(2023, 6, 5);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "MultipleStocksPortfolio",
            10, date);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "MultipleStocksPortfolio",
            5, date);


    InputStream input = new ByteArrayInputStream("4\nMultipleStocksPortfolio\n2024-04-11\n0\n"
            .getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio 'MultipleStocksPortfolio'"
            + " on 2024-04-11: 2547.45"));
  }

  // Test case for checking the value of multiple portfolios
  @Test
  public void testHandlePortfolioValueMultiplePortfolios() throws UnknownStockException,
          InvalidPortfolioException, IOException {
    mockModel.createPortfolio("Portfolio1");
    Stock stock1 = new Stock("AAPL");
    LocalDate date = LocalDate.of(2018, 4, 1);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "Portfolio1",
            10, date);


    mockModel.createPortfolio("Portfolio2");
    Stock stock2 = new Stock("GOOGL");
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "Portfolio2",
            5, date);

    InputStream input = new ByteArrayInputStream(("4\nPortfolio1\n2022-12-28\n4"
            + "\nPortfolio2\n2022-12-28\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio 'Portfolio1'"
            + " on 2022-12-28: 1260.40"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'Portfolio2'"
            + " on 2022-12-28: 430.10"));
  }

  // Test case for checking the value of a portfolio with multiple stocks
  @Test
  public void testHandlePortfolioValueMultipleStocksInPortfolio() throws UnknownStockException,
          InvalidPortfolioException, IOException {
    mockModel.createPortfolio("MultipleStocksPortfolio");
    mockModel.newStock("AAPL");
    mockModel.newStock("GOOGL");
    LocalDate date = LocalDate.of(2018, 4, 1);
    mockModel.addStockToPortfolio("AAPL", "MultipleStocksPortfolio",
            10, date);
    mockModel.addStockToPortfolio("GOOGL", "MultipleStocksPortfolio",
            5, date);

    InputStream input = new ByteArrayInputStream("4\nMultipleStocksPortfolio\n2022-12-28\n0\n"
            .getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio 'MultipleStocksPortfolio'"
            + " on 2022-12-28: 1690.50"));
  }

  // Test case for checking the value of multiple portfolios each with multiple stocks
  @Test
  public void testHandlePortfolioValueMultipleStocksInMultiplePortfolios()
          throws UnknownStockException, InvalidPortfolioException, IOException {
    mockModel.createPortfolio("Portfolio1");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("GOOGL");
    LocalDate date = LocalDate.of(2018, 6, 5);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "Portfolio1",
            10, date);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "Portfolio1",
            5, date);


    mockModel.createPortfolio("Portfolio2");
    LocalDate date2 = LocalDate.of(2022, 6, 5);
    Stock stock3 = new Stock("MSFT");
    Stock stock4 = new Stock("AMZN");
    mockModel.addStockToPortfolio(stock3.getTickerSymbol(), "Portfolio2",
            8, date2);
    mockModel.addStockToPortfolio(stock4.getTickerSymbol(), "Portfolio2",
            3, date2);

    InputStream input = new ByteArrayInputStream(("4\nPortfolio1\n2022-12-28\n4\n"
            + "Portfolio2\n2022-12-28\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio 'Portfolio1' on 2022-12-28:"
            + " 1690.50"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'Portfolio2'"
            + " on 2022-12-28: 2121.70"));
  }

  // Test case for creating a new portfolio
  @Test
  public void testHandleCreatePortfolio() {
    InputStream input = new ByteArrayInputStream("6\nMyPortfolio\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio MyPortfolio created successfully."));
    assertNotNull(mockModel.getPortfolio("MyPortfolio"));
  }

  // Test case for creating multiple portfolios
  @Test
  public void testHandleCreateMultiplePortfolios() {
    InputStream input = new ByteArrayInputStream("6\nPortfolio1\n6\nPortfolio2\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio Portfolio1 created successfully."));
    assertTrue(mockView.getMessages().contains("Portfolio Portfolio2 created successfully."));
    assertNotNull(mockModel.getPortfolio("Portfolio1"));
    assertNotNull(mockModel.getPortfolio("Portfolio2"));
  }

  // Test case for adding a stock to a portfolio
  @Test
  public void testHandleFetchAndAddStock() {
    InputStream input = new ByteArrayInputStream(("6\nMyPortfolio\n5\nMyPortfolio\nAAPL\n10"
            + "\n2023-02-01\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio MyPortfolio created successfully."));
    assertTrue(mockView.getMessages().contains("10 shares of stock AAPL added successfully."));
    Portfolio portfolio = mockModel.getPortfolio("MyPortfolio");
    assertNotNull(portfolio);
  }

  // Test case for attempting to add a stock to a non-existent portfolio
  @Test
  public void testHandleFetchAndAddStockNonExistentPortfolio() {
    InputStream input = new ByteArrayInputStream("5\nNonExistentPortfolio\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("No portfolio with name: NonExistentPortfolio\n"
            + "Create portfolio first."));
  }

  // Test case for adding an invalid stock ticker to a portfolio
  @Test
  public void testHandleFetchAndAddStockInvalidTicker() {
    InputStream input = new ByteArrayInputStream("6\nMyPortfolio\n5\nMyPortfolio\nINVALID\n0\n"
            .getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();
    assertTrue(mockView.getMessages().contains("Portfolio MyPortfolio created successfully."));
    assertTrue(mockView.getMessages().contains("Enter valid Ticker Symbol"));
    Portfolio portfolio = mockModel.getPortfolio("MyPortfolio");
  }

  // Test case for adding a stock to a portfolio with zero quantity
  @Test
  public void testHandleFetchAndAddStockZeroQuantity() {
    InputStream input = new ByteArrayInputStream(("6\nMyPortfolio\n5\nMyPortfolio"
            + "\nAAPL\n0\n10\n2023-05-19\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio MyPortfolio created successfully."));
    assertTrue(mockView.getMessages().contains("Error: Quantity must be greater than zero."));
    assertTrue(mockView.getMessages().contains("Enter quantity: "));

    Portfolio portfolio = mockModel.getPortfolio("MyPortfolio");
    assertNotNull(portfolio);
  }

  // Test case for adding a stock to a portfolio with a negative quantity
  @Test
  public void testHandleFetchAndAddStockNegativeQuantity() {
    InputStream input = new ByteArrayInputStream(("6\nMyPortfolio\n5\nMyPortfolio\n"
            + "AAPL\n-10\n10\n2018-10-10\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio MyPortfolio created successfully."));
    assertTrue(mockView.getMessages().contains("Error: Quantity must be greater than zero."));
    assertTrue(mockView.getMessages().contains("Enter quantity: "));
    Portfolio portfolio = mockModel.getPortfolio("MyPortfolio");
    assertNotNull(portfolio);
  }

  // Test case for adding multiple stocks to a portfolio
  @Test
  public void testHandleFetchAndAddStockMultipleStocks() {
    InputStream input = new ByteArrayInputStream(("6\nMyPortfolio\n5\nMyPortfolio\nAAPL\n10\n"
            + "2023-02-01\n5\nMyPortfolio\nGOOGL\n15\n2023-02-01\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio MyPortfolio created successfully."));
    assertTrue(mockView.getMessages().contains("10 shares of stock AAPL added successfully."));
    assertTrue(mockView.getMessages().contains("15 shares of stock GOOGL added successfully."));
    Portfolio portfolio = mockModel.getPortfolio("MyPortfolio");
    assertNotNull(portfolio);
  }

  // Test case for adding a duplicate stock to a portfolio
  @Test
  public void testHandleFetchAndAddStockDuplicateStock() {
    InputStream input = new ByteArrayInputStream(("6\nMyPortfolio\n5\nMyPortfolio\nAAPL\n10"
            + "\n2023-02-01\n5\nMyPortfolio\nAAPL\n5\n2023-02-01\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio MyPortfolio created successfully."));
    assertTrue(mockView.getMessages().contains("5 shares of stock AAPL added successfully."));
    assertTrue(mockView.getMessages().contains("5 shares of stock AAPL added successfully."));
    Portfolio portfolio = mockModel.getPortfolio("MyPortfolio");
    assertNotNull(portfolio);
  }

  //Test cases for case 6
  // Test case for creating a portfolio with a name
  @Test
  public void testHandleCreatePortfolioOne() {
    InputStream input = new ByteArrayInputStream("6\nMyPortfolio\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio MyPortfolio created successfully."));
    assertNotNull(mockModel.getPortfolio("MyPortfolio"));
  }

  // Test case for attempting to create a portfolio that already exists
  @Test
  public void testHandleCreateExistingPortfolio() throws InvalidPortfolioException {
    Portfolio existingPortfolio = new Portfolio("ExistingPortfolio");
    mockModel.createPortfolio("ExistingPortfolio");

    InputStream input = new ByteArrayInputStream("6\nExistingPortfolio\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages()
            .contains("Already have a portfolio with name: ExistingPortfolio"));
    assertNotNull(mockModel.getPortfolio("ExistingPortfolio"));
  }

  // Test case for attempting to create a portfolio with an empty name
  @Test
  public void testHandleCreatePortfolioEmptyName() {
    InputStream input = new ByteArrayInputStream("6\n\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Error: Portfolio name cannot be empty."));
  }

  // Test case for creating a portfolio with a name containing special characters
  @Test
  public void testHandleCreatePortfolioSpecialCharacters() {
    InputStream input = new ByteArrayInputStream("6\n@!#Portfolio\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio @!#Portfolio created successfully."));
    assertNotNull(mockModel.getPortfolio("@!#Portfolio"));
  }

  // Test case for creating a portfolio with a name containing numbers
  @Test
  public void testHandleCreatePortfolioWithNumbers() {
    InputStream input = new ByteArrayInputStream("6\nPortfolio123\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio Portfolio123 created successfully."));
    assertNotNull(mockModel.getPortfolio("Portfolio123"));
  }


  // Test case for creating a portfolio with a very long name
  @Test
  public void testHandleCreatePortfolioLongName() {
    String longName = "Portfolio" + "A".repeat(100);
    InputStream input = new ByteArrayInputStream(("6\n" + longName + "\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio " + longName + " created successfully."));
    assertNotNull(mockModel.getPortfolio(longName));
  }

  // testing the exit option
  @Test
  public void testExitOption() {
    InputStream input = new ByteArrayInputStream("0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();
    List<String> messages = mockView.getMessages();

    // Check that only the menu was displayed and the program exited
    assertEquals(1, messages.size());
    assertTrue(messages.contains("Menu displayed"));
  }

  // testing when no portfolio is created and stock is trying to be added
  @Test
  public void testMustMakePortfolioFirst() {
    InputStream input = new ByteArrayInputStream("5\nOOD\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("No portfolio with name: OOD\n"
            + "Create portfolio first."));
  }

  // test for invalid choice
  @Test
  public void testHandleInvalidChoice() {
    InputStream input = new ByteArrayInputStream("6\nOOD\n20\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Error: Invalid choice. Please try again."));
  }

  // test for purchasing stock without making portfolio
  @Test
  public void testPurchaseStockMakePortfolioFirst() {
    InputStream input = new ByteArrayInputStream("5\nFundies\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("No portfolio with name: Fundies\n"
            + "Create portfolio first."));
  }

  // test for buying multiple stocks and displaying the composition
  @Test
  public void testBuyMultipleStocksWithComposition() {
    InputStream input = new ByteArrayInputStream(("6\nOOD\n5\nOOD\nMSFT\n20\n2024-02-01\n5"
            + "\nOOD\nAAPL\n10\n2024-02-01\n8\nOOD\n2024-05-01\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("20 shares of stock MSFT added successfully."));
    assertTrue(mockView.getMessages().contains("10 shares of stock AAPL added successfully."));
    assertTrue(mockView.getMessages().contains("Portfolio Composition on 2024-05-01:"));
    assertTrue(mockView.getMessages().contains("MSFT: 20.0\nAAPL: 10.0"));
  }

  // saving a portfolio to the files and retrieving it
  @Test
  public void testSavingPortfolio() throws IOException {
    InputStream input = new ByteArrayInputStream(("6\nOOD\n10\nOOD\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio saved successfully to OOD.txt"));
  }

  // buy stock with multiple portfolios
  @Test
  public void testBuyStockMultiplePortfolios()
          throws UnknownStockException, InvalidPortfolioException, IOException {
    mockModel.createPortfolio("Portfolio1");
    Stock stock1 = new Stock("AAPL");
    LocalDate date = LocalDate.of(2018, 4, 1);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "Portfolio1",
            10, date);

    mockModel.createPortfolio("Portfolio2");
    Stock stock2 = new Stock("MSFT");
    LocalDate date2 = LocalDate.of(2020, 6, 17);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "Portfolio2",
            30, date2);


    InputStream input = new ByteArrayInputStream(("8\nPortfolio1\n2018-04-01\n"
            + "8\nPortfolio2\n2021-02-02\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("AAPL: 10.0"));
    assertTrue(mockView.getMessages().contains("MSFT: 30.0"));
  }

  // buy stock and sell stock with multiple portfolios
  @Test
  public void testBuyAndRemoveGetComposition()
          throws UnknownStockException, InvalidPortfolioException, CannotSellException,
          IOException {

    mockModel.createPortfolio("Portfolio2");
    Stock stock2 = new Stock("MSFT");
    LocalDate date2 = LocalDate.of(2020, 6, 17);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "Portfolio2",
            30, date2);

    LocalDate date3 = LocalDate.of(2022, 6, 17);
    mockModel.removeStockFromPortfolio("Portfolio2", stock2.getTickerSymbol(),
            20, date3);


    InputStream input = new ByteArrayInputStream(("8\nPortfolio2\n2021-02-01\n"
            + "8\nPortfolio2\n2022-08-02\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("MSFT: 30.0"));
    assertTrue(mockView.getMessages().contains("MSFT: 10.0"));
  }

  // composition of one stock
  // testing that it is 0 before the bought date
  @Test
  public void testGetCompositionOfOneStock()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    Stock stock2 = new Stock("MSFT");
    LocalDate date2 = LocalDate.of(2020, 6, 17);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "OOD", 30, date2);

    InputStream input = new ByteArrayInputStream(("8\nOOD\n2019-02-01\n8\nOOD\n"
            + "2022-02-01\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio Composition on 2019-02-01:"));
    assertTrue(mockView.getMessages().contains("MSFT: 30.0"));
  }

  // value of one stock
  @Test
  public void testValueOfPortfolioOneStock()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    LocalDate date = LocalDate.of(2023, 9, 1);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date);

    InputStream input = new ByteArrayInputStream(("4\nOOD\n2019-02-01\n4\nOOD"
            + "\n2023-09-01\n4\nOOD\n2023-10-06\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2019-02-01: 0.00"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2023-09-01: 3789.20"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2023-10-06: 3549.80"));
  }

  // test for purchasing stock
  @Test
  public void testSimplePurchaseStock() {
    InputStream input = new ByteArrayInputStream("6\nOOD\n5\nOOD\nMSFT\n30\n2020-02-01\n0\n"
            .getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio OOD created successfully."));
    assertTrue(mockView.getMessages().contains("30 shares of stock MSFT added successfully."));
  }

  // test for selling stock
  @Test
  public void testSimpleSellStock() {
    InputStream input = new ByteArrayInputStream(("6\nOOD\n5\nOOD\nGOOG\n" +
            "10\n2023-02-01\n7\nOOD\nGOOG\n5\n2024-02-01\n0\n")
            .getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio OOD created successfully."));
    assertTrue(mockView.getMessages().contains("5 shares of stock GOOG removed successfully."));
  }

  // test for buying and selling in one test
  @Test
  public void testBuyAndSellStock() {
    InputStream input = new ByteArrayInputStream(("6\nOOD\n5\nOOD\nMSFT\n20\n2024-02-01\n7"
            + "\nOOD\nMSFT\n5\n2024-02-01\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("20 shares of stock MSFT added successfully."));
    assertTrue(mockView.getMessages().contains("5 shares of stock MSFT removed successfully."));
  }

  // test for buying and selling in one test with composition
  @Test
  public void testBuyAndSellStockWithComposition() {
    InputStream input = new ByteArrayInputStream(("6\nOOD\n5\nOOD\nMSFT\n20\n2024-02-01\n7"
            + "\nOOD\nMSFT\n5\n2024-02-01\n8\nOOD\n2024-02-01\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("20 shares of stock MSFT added successfully."));
    assertTrue(mockView.getMessages().contains("5 shares of stock MSFT removed successfully."));
    assertTrue(mockView.getMessages().contains("MSFT: 15.0"));
  }

  // test for buying and selling in one test with composition
  // different date
  @Test
  public void testBuyAndSellStockWithCompositionDifferentDate() {
    InputStream input = new ByteArrayInputStream(("6\nOOD\n5\nOOD\nMSFT\n20\n2024-02-01\n7"
            + "\nOOD\nMSFT\n5\n2024-02-01\n8\nOOD\n2024-03-01\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("20 shares of stock MSFT added successfully."));
    assertTrue(mockView.getMessages().contains("5 shares of stock MSFT removed successfully."));
    assertTrue(mockView.getMessages().contains("Portfolio Composition on 2024-03-01:"));
    assertTrue(mockView.getMessages().contains("MSFT: 15.0"));
  }

  // test for buying and selling in one test with composition
  // different date prior to buying and selling
  @Test
  public void testBuyAndSellStockWithCompositionDatePriorToBuy() {
    InputStream input = new ByteArrayInputStream(("6\nOOD\n5\nOOD\nMSFT\n20\n2024-02-01\n7"
            + "\nOOD\nMSFT\n5\n2024-02-01\n8\nOOD\n2019-02-01\n0\n").getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("20 shares of stock MSFT added successfully."));
    assertTrue(mockView.getMessages().contains("5 shares of stock MSFT removed successfully."));
    assertTrue(mockView.getMessages().contains("Portfolio Composition on 2019-02-01:"));
  }

  // value of two stocks in one portfolio
  @Test
  public void testValueOfPortfolioTwoStock()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("MSFT");
    LocalDate date = LocalDate.of(2023, 9, 1);
    LocalDate date2 = LocalDate.of(2022, 10, 2);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "OOD", 40, date2);

    InputStream input = new ByteArrayInputStream(("4\nOOD\n2019-02-01\n4\nOOD"
            + "\n2022-10-05\n4\nOOD\n2023-10-06\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2019-02-01: 0.00"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2022-10-05: 9968.00"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2023-10-06: 16640.20"));
  }

  // value of three stocks, two portfolios
  @Test
  public void testValueofThreeStocksTwoPortfolios()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("MSFT");
    LocalDate date = LocalDate.of(2023, 9, 1);
    LocalDate date2 = LocalDate.of(2022, 10, 2);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "OOD", 40, date2);

    InputStream input = new ByteArrayInputStream(("4\nOOD\n2019-02-01\n4\nOOD"
            + "\n2022-10-05\n4\nOOD\n2023-10-06\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2019-02-01: 0.00"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2022-10-05: 9968.00"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2023-10-06: 16640.20"));
  }

  // Test for saving with an invalid portfolio name
  @Test
  public void testSavingInvalidPortfolioName() {
    InputStream input = new ByteArrayInputStream("10\nINVALID_PORTFOLIO\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Error: Portfolio not found: INVALID_PORTFOLIO"));
  }

  // Test for IOException during save
  @Test
  public void testIOExceptionWhileSavingPortfolio() {
    InputStream input = new ByteArrayInputStream("10\nOOD\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Error: Portfolio not found: OOD"));
  }

  // Test for missing portfolio name input
  @Test
  public void testMissingPortfolioNameInput() {
    InputStream input = new ByteArrayInputStream("10\n\n0\n".getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Enter portfolio name: "));
    assertFalse(mockView.getMessages().contains("Portfolio saved successfully to .txt"));
  }

  //saving two portfolios
  @Test
  public void testSavingMultiplePortfolios() {
    InputStream input = new ByteArrayInputStream(("6\nOOD\n10\nOOD\n6\nXYZ\n10\nXYZ\n0\n")
            .getBytes());
    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio saved successfully to OOD.txt"));
    assertTrue(mockView.getMessages().contains("Portfolio saved successfully to XYZ.txt"));
  }

  // tests saving multiple portfolios with added stocks
  @Test
  public void testSavingMultiplePortfoliosWithAddedStocks() {
    // Prepare input stream with commands to create portfolios and add stocks
    InputStream input = new ByteArrayInputStream(("6\nOOD\n5\nOOD\nAAPL\n10\n2022-01-01\n"
            + "6\nXYZ\n5\nOOD\nMSFT\n20\n2022-01-01\n"
            + "10\nOOD\n10\nXYZ\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    // Check if portfolios were created and stocks were added
    assertTrue(mockView.getMessages().contains("Portfolio OOD created successfully."));
    assertTrue(mockView.getMessages().contains("Portfolio XYZ created successfully."));
    assertTrue(mockView.getMessages().contains("10 shares of stock AAPL added successfully."));
    assertTrue(mockView.getMessages().contains("20 shares of stock MSFT added successfully."));

    // Check if portfolios were saved
    assertTrue(mockView.getMessages().contains("Portfolio saved successfully to OOD.txt"));
    assertTrue(mockView.getMessages().contains("Portfolio saved successfully to XYZ.txt"));
  }

  // value of three stocks, two portfolios
  @Test
  public void testValueofThreeStocksTwoPortfolios1()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    mockModel.createPortfolio("Fundies");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("MSFT");
    Stock stock3 = new Stock("GOOG");
    LocalDate date = LocalDate.of(2023, 9, 1);
    LocalDate date2 = LocalDate.of(2022, 10, 2);
    LocalDate date3 = LocalDate.of(2019, 11, 14);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "OOD", 40, date2);
    mockModel.addStockToPortfolio(stock3.getTickerSymbol(), "Fundies", 60,
            date3);

    InputStream input = new ByteArrayInputStream(("4\nOOD\n2019-02-01\n4\nOOD"
            + "\n2022-10-05\n4\nOOD\n2023-10-06\n4\nFundies\n2019-11-15\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2019-02-01: 0.00"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2022-10-05: 9968.00"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2023-10-06: 16640.20"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'Fundies' on 2019-11-15: "
            + "80092.20"));
  }

  // value of three stocks, two portfolios
  // invalid day
  @Test
  public void testValueofThreeStocksTwoPortfoliosInvalidDay()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    mockModel.createPortfolio("Fundies");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("MSFT");
    Stock stock3 = new Stock("GOOG");
    LocalDate date = LocalDate.of(2023, 9, 1);
    LocalDate date2 = LocalDate.of(2022, 10, 2);
    LocalDate date3 = LocalDate.of(2019, 11, 14);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "OOD", 40, date2);
    mockModel.addStockToPortfolio(stock3.getTickerSymbol(), "Fundies", 60,
            date3);

    InputStream input = new ByteArrayInputStream(("4\nOOD\n2019-02-01\n4\nOOD"
            + "\n2022-10-05\n4\nOOD\n2023-10-06\n4\nFundies\n201911-15\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2019-02-01: 0.00"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2022-10-05: 9968.00"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2023-10-06: 16640.20"));
    assertTrue(mockView.getMessages().contains("Invalid day."));
  }

  // test distribution for one stock
  @Test
  public void testDistributionForOneStock()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    LocalDate date = LocalDate.of(2023, 9, 1);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date);


    InputStream input = new ByteArrayInputStream(("9\nOOD\n2024-02-01\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio Distribution on 2024-02-01:"));
    assertTrue(mockView.getMessages().contains("AAPL: 3737.2000000000003"));
  }

  // test distribution for one stock, checking with value
  @Test
  public void testDistributionForOneStockCheckValue()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    LocalDate date = LocalDate.of(2023, 9, 1);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date);


    InputStream input = new ByteArrayInputStream(("9\nOOD\n2024-02-01\n4\nOOD\n2024-02-01"
            + "\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio Distribution on 2024-02-01:"));
    assertTrue(mockView.getMessages().contains("AAPL: 3737.2000000000003"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2024-02-01: 3737.20"));

  }


  // test distribution for two stocks
  @Test
  public void testDistributionForTwoStocks()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("AMZN");
    LocalDate date = LocalDate.of(2023, 9, 1);
    LocalDate date2 = LocalDate.of(2022, 10, 2);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "OOD", 40, date2);


    InputStream input = new ByteArrayInputStream(("9\nOOD\n2024-02-01\n4\nOOD\n2024-02-01"
            + "\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio Distribution on 2024-02-01:"));
    assertTrue(mockView.getMessages().contains("AAPL: 3737.2000000000003\n" + "AMZN: 6371.2"));
    assertTrue(mockView.getMessages().contains("Value of portfolio 'OOD' on 2024-02-01: 10108.40"));
  }

  // load for a portfolio
  // failing to retrieve
  @Test
  public void testFailToRetrievePortfolio()
          throws UnknownStockException, InvalidPortfolioException {

    InputStream input = new ByteArrayInputStream(("11\nFundies\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Failed to retrieve portfolio."));
  }

  // load for a portfolio
  // failing to retrieve
  @Test
  public void testRetrievingPortfolio()
          throws InvalidPortfolioException, IOException {

    mockModel.createPortfolio("Algo");
    mockModel.savePortfolio("Algo", "Algo.txt");

    InputStream input = new ByteArrayInputStream(("11\nAlgo\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio Algo retrieved successfully."));
  }


  // rebalancing this portfolio - two stocks
  @Test
  public void testRebalancePortfolioTwoStocks()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("AMZN");
    LocalDate date = LocalDate.of(2023, 9, 1);
    LocalDate date2 = LocalDate.of(2022, 10, 2);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "OOD", 40, date2);


    InputStream input = new ByteArrayInputStream(("12\nOOD\n2024-02-01\nAAPL\n"
            + "0.5\nAMZN\n0.5\n\n9\nOOD\n2024-02-01\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio rebalanced successfully."));
    assertTrue(mockView.getMessages().contains("Portfolio Distribution on 2024-02-01:"));
    assertTrue(mockView.getMessages().contains("AAPL: 5054.2\n"
            + "AMZN: 5054.200000000001"));
  }


  // rebalancing this portfolio - error message
  @Test
  public void testRebalancePortfolioTwoStocksErrorMessage()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("AMZN");
    LocalDate date = LocalDate.of(2023, 9, 1);
    LocalDate date2 = LocalDate.of(2022, 10, 2);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "OOD", 40, date2);


    InputStream input = new ByteArrayInputStream(("12\nOOD\n2024-02-01\nAAPL\n"
            + "0.5\nAMZN\n0.7\n\n0\nOOD\n2024-02-01\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Error: Weights must sum to 1 (100%)."));
  }

  // rebalancing this portfolio - four stocks
  @Test
  public void testRebalancePortfolioFourStocks()
          throws UnknownStockException, InvalidPortfolioException, IOException {

    mockModel.createPortfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    Stock stock2 = new Stock("AMZN");
    Stock stock3 = new Stock("MSFT");
    Stock stock4 = new Stock("GOOG");
    LocalDate date1 = LocalDate.of(2023, 9, 1);
    LocalDate date2 = LocalDate.of(2022, 10, 2);
    LocalDate date3 = LocalDate.of(2019, 4, 15);
    LocalDate date4 = LocalDate.of(2020, 5, 20);

    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date1);
    mockModel.addStockToPortfolio(stock2.getTickerSymbol(), "OOD", 40, date2);
    mockModel.addStockToPortfolio(stock3.getTickerSymbol(), "OOD", 15, date3);
    mockModel.addStockToPortfolio(stock4.getTickerSymbol(), "OOD", 5, date4);


    InputStream input = new ByteArrayInputStream(("9\nOOD\n2024-02-01\n12\nOOD"
            + "\n2024-03-01\nMSFT\n0.2\nGOOG\n0.4\nAAPL\n0.2\nAMZN\n0.2\n\n"
            + "9\nOOD\n2024-03-01\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Portfolio Distribution on 2024-02-01:"));
    assertTrue(mockView.getMessages().contains("MSFT: 6056.7\n"
            + "GOOG: 713.5500000000001\n"
            + "AAPL: 3737.2000000000003\n" + "AMZN: 6371.2"));
    assertTrue(mockView.getMessages().contains("Portfolio rebalanced successfully."));
    assertTrue(mockView.getMessages().contains("Portfolio Distribution on 2024-03-01:"));
    assertTrue(mockView.getMessages().contains("MSFT: 3528.9799999999996\n"
            + "GOOG: 7057.959999999999\n" + "AAPL: 3528.979999999999\n"
            + "AMZN: 3528.9799999999987"));
  }

  // buying stocks
  // invalid day
  // at the time of buying the stock
  @Test
  public void testInvalidDayWhenBuyingStocks()
          throws UnknownStockException, InvalidPortfolioException {

    mockModel.createPortfolio("OOD");

    InputStream input = new ByteArrayInputStream(("5\nOOD\nGOOG\n20\n2024-0420\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Invalid day."));
  }

  // selling stocks
  // invalid day
  // at the time of selling the stock
  @Test
  public void testInvalidDayWhenSellingStocks()
          throws UnknownStockException, InvalidPortfolioException {

    mockModel.createPortfolio("OOD");

    InputStream input = new ByteArrayInputStream(("7\nOOD\nAAPL\n30\n204-20\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Invalid day."));
  }

  // testing that the right message shows up when more
  // stocks than bought is shown
  @Test
  public void testSoldMoreThanBought()
          throws UnknownStockException, InvalidPortfolioException, CannotSellException,
          IOException {

    mockModel.createPortfolio("OOD");
    Stock stock1 = new Stock("AAPL");
    LocalDate date = LocalDate.of(2023, 9, 1);
    mockModel.addStockToPortfolio(stock1.getTickerSymbol(), "OOD", 20, date);

    InputStream input = new ByteArrayInputStream(("7\nOOD\nAAPL\n40\n2023-10-02\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    // fix to the right message
    assertTrue(mockView.getMessages().contains("Error: You are selling more stocks than bought. "
            + "Buy stocks first."));
  }

  // testing the righ message shows up when
  // user trys to sell stocks when none have been added
  @Test
  public void testSellingStocksWhenZero()
          throws UnknownStockException, InvalidPortfolioException, CannotSellException,
          IOException {

    mockModel.createPortfolio("OOD");
    Stock stock1 = new Stock("AAPL");

    InputStream input = new ByteArrayInputStream(("7\nOOD\nAAPL\n40\n2023-10-02\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    // fix to the right message
    assertTrue(mockView.getMessages().contains("Error: You are selling more stocks than bought. "
            + "Buy stocks first."));
  }

  //handles graph output if the dates are invalid
  @Test
  public void testHandlePlotChartWithInvalidDates() {
    // Prepare input stream with commands to plot chart with invalid scale
    InputStream input = new ByteArrayInputStream(("6\nMyPortfolio\n13\nMyPortfolio\n"
            + "2022-01-01\n2022-12-31\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    assertTrue(mockView.getMessages().contains("Performance of portfolio MyPortfolio from"
            + " 2022-01-01 to 2022-12-31\n" + "\n" + "Jan 2022: \n" + "Feb 2022: \n"
            + "Mar 2022: \n"
            + "Apr 2022: \n" + "May 2022: \n" + "Jun 2022: \n" + "Jul 2022: \n" + "Aug 2022: \n"
            + "Sep 2022: \n" + "Oct 2022: \n" + "Nov 2022: \n" + "Dec 2022: \n"
            + "Dec 2022: \n" + "\n" + "Scale: * = 500.0 units"));
  }

  //tests monthly graph when stocks are added to a portfolio
  @Test
  public void testAddStocksAndGraphPortfolioMonth() {
    // Prepare input stream with commands to add stocks
    InputStream input = new ByteArrayInputStream(("6\nMyPortfolio\n5\nMyPortfolio\n"
            + "NFLX\n25\n2024-02-01\n"
            + "\n5\nMyPortfolio\nGOOG\n10\n2024-02-01\n"
            + "13\nMyPortfolio\n2023-11-01\n2024-05-01\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    // Check if stocks were added
    assertTrue(mockView.getMessages().contains("25 shares of stock NFLX added successfully."));
    assertTrue(mockView.getMessages().contains("10 shares of stock GOOG added successfully."));

    // Check if chart was displayed
    assertTrue(mockView.getMessages().contains("Performance of portfolio MyPortfolio"
            + " from 2023-11-01 to 2024-05-01\n"
            + "\n" + "Nov 2023: \n" + "Dec 2023: \n" + "Jan 2024: \n"
            + "Feb 2024: *******************************\n"
            + "Mar 2024: *********************************\n"
            + "Apr 2024: *********************************\n"
            + "May 2024: ******************************\n"
            + "\n"
            + "Scale: * = 500.0 units"));

  }

  //tests yearly frequency graph
  @Test
  public void testAddStocksAndGraphPortfolioYear() {
    // Prepare input stream with commands to add stocks
    InputStream input = new ByteArrayInputStream(("6\nMyPortfolio\n5\nMyPortfolio\n"
            + "NFLX\n25\n2024-02-01\n"
            + "\n5\nMyPortfolio\nGOOG\n10\n2024-02-01\n"
            + "13\nMyPortfolio\n2018-11-01\n2024-05-01\n0\n").getBytes());

    controller = new StockControllerImpl(mockModel, mockView, input);
    controller.start();

    // Check if stocks were added
    assertTrue(mockView.getMessages().contains("25 shares of stock NFLX added successfully."));
    assertTrue(mockView.getMessages().contains("10 shares of stock GOOG added successfully."));

    // Check if chart was displayed
    assertTrue(mockView.getMessages().contains("Performance of portfolio MyPortfolio"
            + " from 2018-11-01 to 2024-05-01\n"
            + "\n" + "2018: \n" + "2019: \n" + "2020: \n" + "2021: \n" + "2022: \n" + "2023: \n"
            + "2024: ******************************\n" + "\n" + "Scale: * = 500.0 units"));

  }
}