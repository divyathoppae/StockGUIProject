/**
 * Interface representing the view of the GUI interface. This extends
 * the view interface used in the text-based interface, and adds
 * two methods, specific to the GUI in order to display the right messages.
 */
public interface GUIView extends View {
  /**
   * Sets the boolean true when the message should be displayed.
   */
  void display();

  /**
   * Sets the features for handling various portfolio actions.
   * This method assigns action listeners to various buttons in order to perform certain tasks.
   * 1. Create a portfolio based on the name provided in the create portfolio text field.
   * 2. Fetch and add a stock to a portfolio based on the provided portfolio
   * name, ticker symbol, date, and quantity in the respective text fields.
   * 3. Fetch and remove a stock from a portfolio based on the provided portfolio
   * name, ticker symbol, date, and quantity in the respective text fields.
   * 4. Get the composition of a portfolio based on the provided portfolio name and
   * date in the respective text fields.
   * 5. Get the total value of a portfolio based on the provided portfolio name
   * and date in the respective text fields.
   * 6. Save a portfolio based on the provided portfolio name in the
   * save portfolio text field.
   * 7. Load a portfolio based on the provided portfolio name in
   * the load portfolio text field.
   *
   * @param features defined by the behavior for handling the portfolio actions
   */
  void setFeatures(Features features);

  /**
   * Displays an error message for the create operation.
   *
   * @param error the error message to display
   */
  void displayErrorCreate(String error);

  /**
   * Displays an error message for the buy operation.
   *
   * @param error the error message to display
   */
  void displayErrorBuy(String error);

  /**
   * Displays an error message for the sell operation.
   *
   * @param error the error message to display
   */
  void displayErrorSell(String error);

  /**
   * Displays an error message for the composition operation.
   *
   * @param error the error message to display
   */
  void displayErrorComp(String error);

  /**
   * Displays an error message for the value operation.
   *
   * @param error the error message to display
   */
  void displayErrorValue(String error);

  /**
   * Displays an error message for the save operation.
   *
   * @param error the error message to display
   */
  void displayErrorSave(String error);

  /**
   * Displays an error message for the load operation.
   *
   * @param error the error message to display
   */
  void displayErrorLoad(String error);

}
