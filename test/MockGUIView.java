import java.time.LocalDate;
import java.util.Map;

public class MockGUIView implements View, GUIView{
  @Override
  public void display() {

  }

  @Override
  public void setFeatures(Features features) {

  }

  @Override
  public void displayErrorCreate(String error) {

  }

  @Override
  public void displayErrorBuy(String error) {

  }

  @Override
  public void displayErrorSell(String error) {

  }

  @Override
  public void displayErrorComp(String error) {

  }

  @Override
  public void displayErrorValue(String error) {

  }

  @Override
  public void displayErrorSave(String error) {

  }

  @Override
  public void displayErrorLoad(String error) {

  }

  @Override
  public void displayPortfolioValue(String portfolioName, LocalDate date, double value) {

  }

  @Override
  public void displayError(String message) {

  }

  @Override
  public void displayStockNotFound() {

  }

  @Override
  public void displayMessage(String message) {

  }

  @Override
  public void displayPortfolioComposition(Map<String, Double> composition, LocalDate date) {

  }

  @Override
  public void displayCreatePortfolio(String s) {

  }

  @Override
  public void displayFetchAndRemoveStock(int quantity, String tickerSymbol) {

  }

  @Override
  public void displayFetchAndAddStock(int quantity, String tickerSymbol) {

  }

  @Override
  public void displaySavePortfolio(String filename) {

  }

  @Override
  public void displayRetrievePortfolio(String filename) {

  }
}
