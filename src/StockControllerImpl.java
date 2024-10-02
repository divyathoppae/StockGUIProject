import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


/**
 * Implementation of StockControllerImpl interface,
 * made to focus on handling the inputs
 * Manages the interactions between the model and the view,
 * and handles user input for stock operations.
 */
public class StockControllerImpl implements StockController {
  private final Model model;
  private final TextView view;
  private final Scanner scanner;

  /**
   * Constructs a new StockControllerImpl object
   * with the given model and view.
   *
   * @param model the stock model
   * @param view  the stock view
   * @param in    the input stream
   */
  public StockControllerImpl(Model model, TextView view, InputStream in) {
    this.model = model;
    this.view = view;
    this.scanner = new Scanner(in);
  }

  /**
   * Starts the stock management application, initiating
   * user interaction and handling user inputs.
   * Handles each input accordingly
   */
  @Override
  public void start() {
    boolean running = true;
    while (running) {
      view.printMenu();
      int choice = getUserChoice();
      switch (choice) {
        case 1:
          handleGainOrLoss();
          break;
        case 2:
          handleMovingAverage();
          break;
        case 3:
          handleCrossover();
          break;
        case 4:
          handlePortfolioValue();
          break;
        case 5:
          handleFetchAndAddStock();
          break;
        case 6:
          handleCreatePortfolio();
          break;
        case 7:
          handleFetchAndRemoveStock();
          break;
        case 8:
          handlePortfolioComposition();
          break;
        case 9:
          handlePortfolioDistribution();
          break;
        case 10:
          handleSaveExistingPortfolio();
          break;
        case 11:
          handleRetrieveExistingPortfolio();
          break;
        case 12:
          handleBalancePortfolio();
          break;
        case 13:
          handlePlotChart();
          break;
        case 0:
          running = false;
          break;
        default:
          view.displayError("Invalid choice. Please try again.");
      }
    }
  }


  /**
   * Handles the process of retrieving an existing portfolio.
   */
  private void handleRetrieveExistingPortfolio() {
    String portfolioName = getPortfolioName();
    try {
      model.retrievePortfolio(portfolioName);
      view.displayMessage("Portfolio " + portfolioName + " retrieved successfully.");
    } catch (IOException e) {
      view.displayMessage("Failed to retrieve portfolio.");
    }

  }

  /**
   * Handles the process of fetching a stock and adding it to a portfolio.
   */
  private void handleFetchAndAddStock() {
    try {
      String name = getPortfolioName();
      if (!model.isPortfolio(name)) {
        view.displayMessage("No portfolio with name: " + name + "\nCreate portfolio first.");
        return;
      }
      String tickerSymbol = getTickerSymbol();
      if (model.checkTickerSymbol(tickerSymbol)) {
        try {
          int quantity = getQuantity();
          LocalDate date;
          try {
            date = getDate("the");
          } catch (DateTimeParseException e) {
            return;
          }
          model.addStockToPortfolio(tickerSymbol, name, quantity, date);
          view.displayFetchAndAddStock(quantity, tickerSymbol);
        } catch (InvalidPortfolioException e) {
          view.displayMessage("Error: " + e.getMessage());
        } catch (IOException e) {
          view.displayMessage(e.getMessage());
        }
      } else {
        view.displayMessage("Enter valid Ticker Symbol");
      }
    } catch (UnknownStockException e) {
      view.displayMessage("Unknown stock: " + e.getMessage());
    }
  }

  /**
   * Handles the process of creating a new portfolio.
   */
  private void handleCreatePortfolio() {
    try {
      String portfolioName = getPortfolioName();
      if (model.isPortfolio(portfolioName)) {
        view.displayMessage("Already have a portfolio with name: "
                + portfolioName);
      } else {
        model.createPortfolio(portfolioName);
        view.displayCreatePortfolio(portfolioName);
      }
    } catch (InvalidPortfolioException e) {
      view.displayMessage("No portfolio with that name.");
    }
  }

  /**
   * Handles the process of calculating and displaying the
   * gain or loss of a stock over a specified period.
   */
  public void handleGainOrLoss() {
    String tickerSymbol = getTickerSymbol();
    try {
      if (model.checkTickerSymbol(tickerSymbol)) {
        try {
          LocalDate startDate;
          try {
            startDate = getDate("start");
          } catch (DateTimeParseException e) {
            return;
          }
          LocalDate endDate;
          try {
            endDate = getDate("end");
          } catch (DateTimeParseException e) {
            return;
          }
          double gainOrLoss = model.getGainOrLoss(tickerSymbol, startDate, endDate);
          if (model.isStock(tickerSymbol)) {
            view.displayGainOrLoss(gainOrLoss, startDate, endDate);
          } else {
            view.displayStockNotFound();
          }
        } catch (InvalidDateException e) {
          view.displayMessage("Price data not available for the given dates.");
        }
      } else {
        view.displayMessage("Enter a valid Ticker Symbol");
      }
    } catch (UnknownStockException | IOException e) {
      view.displayMessage("Unknown stock: " + tickerSymbol);
    }
  }

  /**
   * Handles the process of calculating and
   * displaying the moving average of a stock.
   */
  private void handleMovingAverage() {
    try {
      String tickerSymbol = getTickerSymbol();
      if (model.checkTickerSymbol(tickerSymbol)) {
        try {
          LocalDate date;
          try {
            date = getDate("the");
          } catch (DateTimeParseException e) {
            return;
          }
          int days;
          try {
            days = getDays();
          } catch (InputMismatchException e) {
            return;
          }
          double movingAverage = 0;
          try {
            movingAverage = model.getMovingAverage(tickerSymbol, date, days);
          } catch (IllegalArgumentException | IOException e) {
            view.displayMessage(e.getMessage());
          }
          if (model.isStock(tickerSymbol)) {
            view.displayMovingAverage(movingAverage, days, date);
          } else {
            view.displayStockNotFound();
          }
        } catch (InvalidDateException e) {
          view.displayMessage("Price data not available for the given dates.");
        }
      } else {
        view.displayMessage("Enter a valid Ticker Symbol.");
      }
    } catch (UnknownStockException e) {
      view.displayMessage("Unknown stock: " + e.getMessage());
    }
  }

  /**
   * Handles the process of checking and displaying crossovers for a stock over a date range.
   */
  private void handleCrossover() {
    try {
      String tickerSymbol = getTickerSymbol();
      if (model.checkTickerSymbol(tickerSymbol)) {
        LocalDate startDate;
        try {
          startDate = getDate("start");
        } catch (DateTimeParseException e) {
          view.displayMessage("Invalid start date format. Please try again.");
          return;
        }

        LocalDate endDate;
        try {
          endDate = getDate("end");
        } catch (DateTimeParseException e) {
          view.displayMessage("Invalid end date format. Please try again.");
          return;
        }

        int days;
        try {
          days = getDays();
        } catch (InputMismatchException e) {
          view.displayMessage("Invalid input for days. Please enter a valid number.");
          return;
        }

        try {
          if (model.checkTickerSymbol(tickerSymbol)) {
            LocalDate date = startDate;
            while (!date.isAfter(endDate)) {
              if (model.isCrossover(tickerSymbol, date, days)) {
                view.displayCrossover(date);
              }
              date = date.plusDays(1);
            }
          } else {
            view.displayStockNotFound();
          }
        } catch (IllegalArgumentException e) {
          view.displayMessage("Invalid argument: " + e.getMessage());
        } catch (IOException e) {
          view.displayMessage(e.getMessage());
        }

      } else {
        view.displayMessage("Enter a valid Ticker Symbol.");
      }
    } catch (UnknownStockException e) {
      view.displayMessage("Unknown stock: " + e.getMessage());
    } catch (InvalidDateException e) {
      view.displayMessage(e.getMessage());
    }
  }


  /**
   * Handles the process of calculating and displaying
   * the value of a portfolio on a specific date.
   */
  private void handlePortfolioValue() {
    String portfolioName = getPortfolioName();
    if (!model.isPortfolio(portfolioName)) {
      view.displayMessage("No portfolio with name: " + portfolioName
              + "\n Create portfolio first.");
      return;
    }
    LocalDate date;
    try {
      date = getDate("the");
    } catch (DateTimeParseException e) {
      return;
    }
    try {
      double value = model.getPortfolioValue(portfolioName, date);
      view.displayPortfolioValue(portfolioName, date, value);
    } catch (UnknownStockException e) {
      view.displayError(e.getMessage());
    } catch (InvalidPortfolioException e) {
      view.displayMessage("Portfolio does not exist");
    } catch (IOException e) {
      view.displayMessage("File is not readable.");
    }
  }

  /**
   * Handles the process of fetching and removing a stock from a portfolio.
   */
  private void handleFetchAndRemoveStock() {
    try {
      String name = getPortfolioName();
      if (!model.isPortfolio(name)) {
        view.displayMessage("No portfolio with name: " + name + "\nCreate portfolio first.");
        return;
      }
      String tickerSymbol = getTickerSymbol();
      if (model.checkTickerSymbol(tickerSymbol)) {
        int quantity = getQuantity();
        LocalDate date;
        try {
          date = getDate("the");
        } catch (DateTimeParseException e) {
          return;
        }
        model.removeStockFromPortfolio(name, tickerSymbol, quantity, date);
        view.displayFetchAndRemoveStock(quantity, tickerSymbol);
      } else {
        view.displayMessage("Enter valid Ticker Symbol");
      }
    } catch (UnknownStockException e) {
      view.displayMessage("Unknown stock: " + e.getMessage());
    } catch (InvalidPortfolioException | IOException | CannotSellException e) {
      view.displayError(e.getMessage());
    }
  }

  /**
   * Handles the process of displaying the composition of a portfolio.
   */
  private void handlePortfolioComposition() {
    try {
      String portfolioName = getPortfolioName();
      LocalDate date;
      try {
        date = getDate("the");
      } catch (DateTimeParseException e) {
        return;
      }
      Map<String, Double> composition = model.getPortfolioComposition(portfolioName, date);
      view.displayPortfolioComposition(composition, date);
    } catch (InvalidPortfolioException e) {
      view.displayMessage("Error: " + e.getMessage());
    }
  }

  /**
   * Handles the process of displaying the value distribution of a portfolio.
   */
  private void handlePortfolioDistribution() {
    try {
      String portfolioName = getPortfolioName();
      LocalDate date;
      try {
        date = getDate("the");
      } catch (DateTimeParseException e) {
        return;
      }
      Map<String, Double> distribution = model.getPortfolioDistribution(portfolioName, date);
      view.displayPortfolioDistribution(distribution, date);
    } catch (InvalidPortfolioException e) {
      view.displayMessage("Error: " + e.getMessage());
    } catch (UnknownStockException | IOException e) {
      view.displayMessage("Not a real stock");
    }
  }

  /**
   * Handles the process of saving an existing portfolio to a file.
   */
  private void handleSaveExistingPortfolio() {
    try {
      String portfolioName = getPortfolioName();
      String filename = portfolioName + ".txt";
      model.savePortfolio(portfolioName, filename);
      view.displaySavePortfolio(filename);
    } catch (IOException e) {
      view.displayMessage("Error saving portfolio: " + e.getMessage());
    } catch (InvalidPortfolioException e) {
      view.displayMessage("Error: " + e.getMessage());
    }
  }

  /**
   * Handles the process of rebalancing a portfolio.
   */
  private void handleBalancePortfolio() {
    try {
      String portfolioName = getPortfolioName();
      if (!model.isPortfolio(portfolioName)) {
        view.displayMessage("No portfolio with name: " + portfolioName
                + "\nCreate portfolio first.");
        return;
      }

      LocalDate rebalanceDate;

      try {
        rebalanceDate = getDate("rebalance");
      } catch (DateTimeParseException e) {
        return;
      }
      Map<String, Double> newWeights = new HashMap<>();

      while (true) {
        String tickerSymbol = getTickerSymbol();
        if (tickerSymbol.isEmpty()) {
          break;
        }
        double weight = getWeight();
        newWeights.put(tickerSymbol, weight);
      }

      try {
        model.rebalancePortfolio(portfolioName, rebalanceDate, newWeights);
        view.displayMessage("Portfolio rebalanced successfully.");
      } catch (InvalidPortfolioException | InvalidWeightException | IOException e) {
        view.displayMessage("Error: " + e.getMessage());
      }
    } catch (UnknownStockException e) {
      view.displayMessage("Unknown stock: " + e.getMessage());
    }
  }

  private static final int maxAsterisks = 50;

  /**
   * Handles the input of the information in order to make a graph.
   */
  public void handlePlotChart() {
    try {
      String portfolioName = getPortfolioName();
      if (!model.isPortfolio(portfolioName)) {
        view.displayMessage("No portfolio with name: " + portfolioName
                + "\nCreate portfolio first.");
        return;
      }

      LocalDate startDate;
      try {
        startDate = getDate("start");
      } catch (DateTimeParseException e) {
        view.displayMessage("Invalid start date format."
                + " Please enter the date in yyyy-MM-dd format.");
        return;
      }
      LocalDate endDate;
      try {
        endDate = getDate("end");
      } catch (DateTimeParseException e) {
        view.displayMessage("Invalid end date format. Please enter the date in yyyy-MM-dd format.");
        return;
      }

      List<LocalDate> dates = model.getAdjustedDates(startDate, endDate);
      TreeMap<LocalDate, Double> portfolioValues = new TreeMap<>();
      Double lastKnownValue = null;
      double maxValue = Double.MIN_VALUE;
      for (LocalDate date : dates) {
        double value = model.getPortfolioValue(portfolioName, date);
        if (value != 0) {
          lastKnownValue = value;
        } else if (lastKnownValue != null) {
          value = lastKnownValue;
        }
        if (value > maxValue) {
          maxValue = value;
        }
        portfolioValues.put(date, value);
      }


      // Scale can be 500, 1000, 2000
      final double scale;
      if ((int) maxValue / 500 <= maxAsterisks) {
        scale = 500;
      } else if ((int) maxValue / 1000 <= maxAsterisks) {
        scale = 1000;
      } else if ((int) maxValue / 2000 <= maxAsterisks) {
        scale = 2000;
      } else {
        scale = maxValue / maxAsterisks;
      }

      view.displayTextChart(portfolioName, startDate, endDate, portfolioValues,
              scale, model.determineDateFrequency(startDate, endDate));

    } catch (UnknownStockException e) {
      view.displayMessage("An error occurred: Unknown stock."
              + " Please check the portfolio and try again.");
    } catch (InvalidPortfolioException | IOException e) {
      view.displayMessage(e.getMessage());
    }
  }


  /**
   * Gets the user's menu choice.
   *
   * @return the user's choice as an integer
   */
  private int getUserChoice() {
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline
    return choice;
  }

  /**
   * Gets the stock ticker symbol from the user.
   *
   * @return the stock ticker symbol
   */
  private String getTickerSymbol() {
    view.displayMessage("Enter stock ticker symbol: ");
    return scanner.nextLine();
  }

  /**
   * Gets the quantity of stock from the user.
   *
   * @return the quantity of stock
   */
  private int getQuantity() {
    view.displayMessage("Enter quantity: ");
    int quantity = 0;

    try {
      quantity = scanner.nextInt();
      scanner.nextLine(); // Consume newline
      if (quantity <= 0) {
        view.displayMessage("Error: Quantity must be greater than zero.");
        getQuantity();
      }
    } catch (InputMismatchException e) {
      scanner.nextLine(); // Consume the invalid input
      view.displayMessage("Error: Invalid input. Please enter an integer.");
      getQuantity();
    }

    return quantity;
  }


  /**
   * Gets the date properly formatted in yyyy-mm-dd from the user.
   *
   * @return the number of days
   */
  private LocalDate getDate(String prompt) throws DateTimeParseException {
    try {
      view.displayMessage("Enter " + prompt + " date (yyyy-mm-dd): ");
      return LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (DateTimeParseException e) {
      view.displayMessage("Invalid day.");
      throw e;
    }

  }

  /**
   * Gets the number of days from the user.
   *
   * @return the number of days
   * @throws InputMismatchException when an invalid number of days is entered
   */
  private int getDays() throws InputMismatchException {
    view.displayMessage("Enter number of days: ");
    try {
      int days = scanner.nextInt();
      scanner.nextLine(); // Consume newline
      return days;
    } catch (InputMismatchException e) {
      view.displayMessage("Invalid number of days.");
      throw e;
    }

  }

  /**
   * Gets the filename from the user.
   *
   * @return the filename entered by the user
   */
  private String getFilename() {
    view.displayMessage("Enter file name: ");
    String fileName = scanner.nextLine();
    fileName = fileName + ".txt";
    scanner.nextLine();
    return fileName;
  }

  /**
   * Gets the weight for a stock from the user.
   *
   * @return the weight entered by the user
   */
  private double getWeight() {
    view.displayMessage("Enter weight for this stock (as a decimal, e.g., 0.25 for 25%): ");
    double weight = scanner.nextDouble();
    scanner.nextLine();
    return weight;
  }

  /**
   * Gets the portfolio name from the user.
   *
   * @return the portfolio name
   */
  private String getPortfolioName() {
    view.displayMessage("Enter portfolio name: ");
    String string = scanner.nextLine();
    if (string.isEmpty()) {
      view.displayMessage("Error: Portfolio name cannot be empty.");
      return null;
    } else {
      return string;
    }
  }

}