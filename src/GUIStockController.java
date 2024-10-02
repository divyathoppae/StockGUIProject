import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * The GUIStockController class implements the StockController
 * and ActionListener interfaces.
 * It handles certain actions in order to the GUI to work in the desired way.
 */
public class GUIStockController implements StockController, Features {
  private final Model model;
  private final GUIView view;

  /**
   * Constructs a new GUIStockController object
   * with the given model, view, and input stream.
   *
   * @param model the stock model
   * @param view  the stock view
   */
  public GUIStockController(Model model, GUIView view) {
    this.model = model;
    this.view = view;
  }

  /**
   * Starts the GUI view and sets this
   * controller as the command listener.
   */
  public void start() {
    view.setFeatures(this);
    view.display();
  }

  /**
   * Handles the creation of a new portfolio.
   *
   * @param portfolioName the name of the portfolio.
   */
  @Override
  public void handleCreatePortfolio(String portfolioName) {
    if (portfolioName.isEmpty()) {
      return;
    }
    try {
      model.createPortfolio(portfolioName);
      view.displayCreatePortfolio(portfolioName);
    } catch (InvalidPortfolioException e) {
      view.displayErrorCreate("Error: " + e.getMessage());
    }
  }

  /**
   * Handles fetching and adding a stock to a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param tickerSymbol  the tickerSymbol of the stock.
   * @param date          the date that the stock is added.
   * @param quantity      the quantity added.
   */
  @Override
  public void handleFetchAndAddStock(String portfolioName, String tickerSymbol, LocalDate date,
                                     int quantity) {
    if (portfolioName.isEmpty()) {
      return;
    }
    if (quantity <= 0) {
      view.displayErrorBuy("Error: Quantity must be greater than zero.");
      return;
    }
    if (date == null){
      return;
    }
    try {
      model.addStockToPortfolio(tickerSymbol, portfolioName, quantity, date);
      view.displayFetchAndAddStock(quantity, tickerSymbol);
    } catch (UnknownStockException | InvalidPortfolioException e) {
      view.displayErrorBuy("Error: " + e.getMessage());
    } catch (IOException e) {
      view.displayMessage(e.getMessage());
    }
  }

  /**
   * Handles fetching and removing a stock to a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param tickerSymbol  the tickerSymbol of the stock.
   * @param date          the date that the stock is removed.
   * @param quantity      the quantity removed.
   */
  @Override
  public void handleFetchAndRemoveStock(String portfolioName, String tickerSymbol, LocalDate date,
                                        int quantity) {
    if (portfolioName.isEmpty()) {
      return;
    }
    if (quantity <= 0) {
      view.displayErrorSell("Error: Quantity must be greater than zero.");
      return;
    }
    if (date == null){
      return;
    }

    try {
      model.removeStockFromPortfolio(portfolioName, tickerSymbol, quantity, date);
      view.displayFetchAndRemoveStock(quantity, tickerSymbol);
    } catch (UnknownStockException | InvalidPortfolioException | CannotSellException
             | IOException e) {
      view.displayErrorSell("Error: " + e.getMessage());
    }
  }

  /**
   * Handles calculating the value of a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param date          the date inputted by the user.
   */
  @Override
  public void handlePortfolioValue(String portfolioName, LocalDate date) {
    if (portfolioName == null) {
      return;
    }
    if (date == null){
      return;
    }

    try {
      double value = model.getPortfolioValue(portfolioName, date);
      view.displayPortfolioValue(portfolioName, date, value);
    } catch (UnknownStockException | InvalidPortfolioException | IOException e) {
      view.displayErrorValue("Error: " + e.getMessage());
    }
  }

  /**
   * Handles calculating the composition of a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param date          the date inputted by the user.
   */
  @Override
  public void handlePortfolioComposition(String portfolioName, LocalDate date) {
    try {
      Map<String, Double> composition = model.getPortfolioComposition(portfolioName, date);
      view.displayPortfolioComposition(composition, date);
    } catch (InvalidPortfolioException e) {
      view.displayErrorComp("Error: " + e.getMessage());
    }
  }

  /**
   * Handles saving an existing portfolio.
   *
   * @param portfolioName the name of the portfolio.
   */
  @Override
  public void handleSavePortfolio(String portfolioName) {
    if (portfolioName == null) {
      return;
    }
    String filename = portfolioName + ".txt";
    try {
      model.savePortfolio(portfolioName, filename);
      view.displaySavePortfolio(filename);
    } catch (InvalidPortfolioException | IOException e) {
      view.displayErrorSave("Error: " + e.getMessage());
    }
  }

  /**
   * Handles loading an existing portfolio.
   *
   * @param portfolioName the name of the portfolio.
   */
  @Override
  public void handleLoadPortfolio(String portfolioName) {
    if (portfolioName == null) {
      return;
    }
    try {
      model.retrievePortfolio(portfolioName);
      view.displayRetrievePortfolio(portfolioName);
    } catch (IOException e) {
      view.displayErrorLoad("Failed to retrieve portfolio.");
    }
  }
}