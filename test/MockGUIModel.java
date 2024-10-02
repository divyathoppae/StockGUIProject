public class MockGUIModel implements Model {
  private boolean createPortfolioCalled = false;
  private boolean addStockToPortfolioCalled = false;
  private boolean removeStockFromPortfolioCalled = false;
  private boolean getPortfolioValueCalled = false;
  private boolean getPortfolioCompositionCalled = false;
  private boolean savePortfolioCalled = false;
  private boolean retrievePortfolioCalled = false;

  // Add more fields if you need to capture method arguments or return values

  @Override
  public void createPortfolio(String portfolioName) throws InvalidPortfolioException {
    createPortfolioCalled = true;
    // Add more logic if needed
  }

  @Override
  public void addStockToPortfolio(String tickerSymbol, String portfolioName, int quantity, LocalDate date)
          throws UnknownStockException, InvalidPortfolioException {
    addStockToPortfolioCalled = true;
    // Add more logic if needed
  }

  @Override
  public void removeStockFromPortfolio(String portfolioName, String tickerSymbol, int quantity, LocalDate date)
          throws UnknownStockException, InvalidPortfolioException, CannotSellException {
    removeStockFromPortfolioCalled = true;
    // Add more logic if needed
  }

  @Override
  public double getPortfolioValue(String portfolioName, LocalDate date)
          throws UnknownStockException, InvalidPortfolioException, IOException {
    getPortfolioValueCalled = true;
    // Add more logic if needed
    return 0; // Return a dummy value
  }

  @Override
  public Map<String, Double> getPortfolioComposition(String portfolioName, LocalDate date)
          throws InvalidPortfolioException {
    getPortfolioCompositionCalled = true;
    // Add more logic if needed
    return null; // Return a dummy value
  }

  @Override
  public void savePortfolio(String portfolioName, String filename)
          throws InvalidPortfolioException, IOException {
    savePortfolioCalled = true;
    // Add more logic if needed
  }

  @Override
  public void retrievePortfolio(String portfolioName) throws IOException {
    retrievePortfolioCalled = true;
    // Add more logic if needed
  }

  // Add methods to check if specific methods were called
  public boolean isCreatePortfolioCalled() {
    return createPortfolioCalled;
  }

  public boolean isAddStockToPortfolioCalled() {
    return addStockToPortfolioCalled;
  }

  // Add more getters for other methods if needed
}
