
/**
 * The main class for the stock management program.
 * This class initializes the MVC components and starts the application.
 */
public class StockMainProgram {

  /**
   * The main method to start the stock management program.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      // No command-line arguments, open GUI
      openGUI();
    } else if (args.length == 1 && args[0].equals("-text")) {
      // Command-line argument is "-text", open text-based interface
      openTextInterface();
    } else {
      // Invalid command-line arguments, display error message
      System.out.println("Invalid command-line arguments. Usage:");
      System.out.println("java -jar Program.jar -text   (for text-based interface)");
      System.out.println("java -jar Program.jar          (for GUI)");
    }
  }

  /**
   * Opens the graphical user interface (GUI)
   * for the stock management program.
   */
  private static void openGUI() {
    GUIView frame = new StockGUIView();
    Model model = new StockModel();
    StockController controller = new GUIStockController(model, frame);
    controller.start();

  }

  /**
   * Opens the text-based interface for
   * the stock management program.
   */
  private static void openTextInterface() {
    TextView view = new StockView();
    Model model = new StockModel();
    StockController controller = new StockControllerImpl(model, view, System.in);
    controller.start();
  }
}
