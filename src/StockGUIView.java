import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.Box;


/**
 * This represents the view class for a GUI-based stock portfolio manager.
 * Allows users to create, buy, sell, save, load portfolios, and obtain the value/
 * composition of those portfolios.
 * This switches between boxes and screens to navigate.
 */
public class StockGUIView extends JFrame implements GUIView {
  private final JTextField portfolioNameFieldCreate = new JTextField();
  private final JTextField portfolioNameFieldBuy = new JTextField();
  private final JTextField tickerSymbolFieldBuy = new JTextField();
  private final JTextField quantityFieldBuy = new JTextField();
  private final JTextField dateFieldBuy = new JTextField();
  private final JTextField portfolioNameFieldSell = new JTextField();
  private final JTextField tickerSymbolFieldSell = new JTextField();
  private final JTextField quantityFieldSell = new JTextField();
  private final JTextField dateFieldSell = new JTextField();
  private final JTextField portfolioNameFieldComp = new JTextField();
  private final JTextField dateFieldComp = new JTextField();
  private final JTextField portfolioNameFieldTotal = new JTextField();
  private final JTextField dateFieldTotal = new JTextField();
  private final JTextField portfolioNameFieldSave = new JTextField();
  private final JTextField portfolioNameFieldLoad = new JTextField();

  private final JTextArea displayArea;
  private final CardLayout cardLayout;
  private final JPanel cardPanel;
  private JButton sellButton;
  private JButton buyButton;
  private JButton compositionButton;
  private JButton totalValueButton;
  private JButton createButton;
  private JButton saveButton;
  private JButton loadButton;
  private final JLabel messageLabel = new JLabel();
  private final JLabel messageLabelBuy = new JLabel();
  private final JLabel messageLabelSell = new JLabel();
  private final JLabel messageLabelComposition = new JLabel();
  private final JLabel messageLabelValue = new JLabel();
  private final JLabel messageLabelSave = new JLabel();
  private final JLabel messageLabelLoad = new JLabel();


  /**
   * Constructs the StockGUIView frame.
   * Initializes GUI components and sets up
   * the screens for navigation.
   */
  public StockGUIView() {
    setTitle("Stock Portfolio Manager");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create the main panel with CardLayout
    cardLayout = new CardLayout();
    cardPanel = new JPanel(cardLayout);

    // Create and add the header panel
    JPanel headerPanel = new JPanel(new BorderLayout());
    JLabel headerLabel = new JLabel("Welcome to the Stock Portfolio Manager!");
    headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
    headerPanel.add(headerLabel, BorderLayout.CENTER);

    // Add space between the header label and the home button
    headerPanel.add(Box.createHorizontalStrut(20), BorderLayout.EAST);
    JButton homeButton = new JButton("Home");
    homeButton.addActionListener(e -> cardLayout.show(cardPanel, "Main Menu"));
    headerPanel.add(homeButton, BorderLayout.EAST);

    add(headerPanel, BorderLayout.NORTH);

    // Create the main menu panel
    JPanel mainMenuPanel = new JPanel(new BorderLayout());
    JLabel subheaderLabel = new JLabel("Start by creating your "
            + "portfolio or load an existing portfolio.");
    subheaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
    subheaderLabel.setFont(new Font("Arial", Font.ITALIC, 16));
    mainMenuPanel.add(subheaderLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(10, 10, 10, 10);
    // Optional: Add spacing around the button
    JButton createPortfolioButton = new JButton("Create Portfolio");
    createPortfolioButton.addActionListener(e -> cardLayout.show(cardPanel,
            "Create Portfolio"));
    buttonPanel.add(createPortfolioButton, gbc);

    // Add the rest of the buttons in a 2x3 grid
    gbc.gridwidth = 1; // Reset gridwidth to 1 for the remaining buttons
    gbc.gridy = 1; // Move to the next row
    JButton buyStockButton = new JButton("Buy Stock");
    buyStockButton.addActionListener(e -> cardLayout.show(cardPanel, "Buy Stock"));
    buttonPanel.add(buyStockButton, gbc);
    gbc.gridx = 1; // Move to the next column
    JButton sellStockButton = new JButton("Sell Stock");
    sellStockButton.addActionListener(e -> cardLayout.show(cardPanel, "Sell Stock"));
    buttonPanel.add(sellStockButton, gbc);
    gbc.gridx = 0; // Move back to the first column
    gbc.gridy = 2; // Move to the next row
    JButton portfolioValueButton = new JButton("Portfolio Value");
    portfolioValueButton.addActionListener(e -> cardLayout.show(cardPanel,
            "Portfolio Value"));
    buttonPanel.add(portfolioValueButton, gbc);

    gbc.gridx = 1; // Move to the next column
    JButton portfolioCompositionButton = new JButton("Portfolio Composition");
    portfolioCompositionButton.addActionListener(e -> cardLayout.show(cardPanel,
            "Portfolio Composition"));
    buttonPanel.add(portfolioCompositionButton, gbc);
    gbc.gridx = 0; // Move back to the first column
    gbc.gridy = 3; // Move to the next row
    JButton savePortfolioButton = new JButton("Save Portfolio");
    savePortfolioButton.addActionListener(e -> cardLayout.show(cardPanel, "Save Portfolio"));
    buttonPanel.add(savePortfolioButton, gbc);
    gbc.gridx = 1; // Move to the next column
    JButton loadPortfolioButton = new JButton("Load Portfolio");
    loadPortfolioButton.addActionListener(e -> cardLayout.show(cardPanel, "Load Portfolio"));
    buttonPanel.add(loadPortfolioButton, gbc);

    mainMenuPanel.add(buttonPanel, BorderLayout.CENTER);

    // Add all panels to the card panel
    cardPanel.add(mainMenuPanel, "Main Menu");
    cardPanel.add(createCreatePortfolioPanel(), "Create Portfolio");
    cardPanel.add(createBuyStockPanel(), "Buy Stock");
    cardPanel.add(createSellStockPanel(), "Sell Stock");
    cardPanel.add(createPortfolioValuePanel(), "Portfolio Value");
    cardPanel.add(createPortfolioCompositionPanel(), "Portfolio Composition");
    cardPanel.add(createSavePortfolioPanel(), "Save Portfolio");
    cardPanel.add(createLoadPortfolioPanel(), "Load Portfolio");

    add(cardPanel, BorderLayout.CENTER);

    displayArea = new JTextArea();
    displayArea.setEditable(false);
    add(new JScrollPane(displayArea), BorderLayout.SOUTH);

    cardLayout.show(cardPanel, "Main Menu"); // Start with the main menu
    setStockActionsEnabled(true);

  }

  /**
   * Creates the panel for creating a new portfolio.
   *
   * @return JPanel for creating a portfolio
   */
  private JPanel createCreatePortfolioPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    // Set preferred size for text fields
    Dimension fieldSize = new Dimension(200, 25);

    portfolioNameFieldCreate.setPreferredSize(fieldSize);

    // Add the labels and fields to the panel
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Enter stock details:"), gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Portfolio Name:"), gbc);
    gbc.gridx = 1;
    panel.add(portfolioNameFieldCreate, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 2; // Make the button span across two columns
    gbc.anchor = GridBagConstraints.CENTER;
    createButton = new JButton("Create");
    createButton.setActionCommand("Create Portfolio");

    panel.add(createButton, gbc);
    panel.add(messageLabel);
    return panel;
  }

  /**
   * Creates the panel for buying stocks.
   *
   * @return JPanel for buying stocks
   */
  private JPanel createBuyStockPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    // Set preferred size for text fields
    Dimension fieldSize = new Dimension(200, 25);
    portfolioNameFieldBuy.setPreferredSize(fieldSize);
    tickerSymbolFieldBuy.setPreferredSize(fieldSize);
    quantityFieldBuy.setPreferredSize(fieldSize);
    dateFieldBuy.setPreferredSize(fieldSize);

    // Add the labels and fields to the panel
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Enter stock details:"), gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Portfolio Name:"), gbc);
    gbc.gridx = 1;
    panel.add(portfolioNameFieldBuy, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Ticker Symbol:"), gbc);
    gbc.gridx = 1;
    panel.add(tickerSymbolFieldBuy, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Quantity:"), gbc);
    gbc.gridx = 1;
    panel.add(quantityFieldBuy, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
    gbc.gridx = 1;
    panel.add(dateFieldBuy, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 2; // Make the button span across two columns
    gbc.anchor = GridBagConstraints.CENTER;
    buyButton = new JButton("Buy");

    panel.add(buyButton, gbc);
    panel.add(messageLabelBuy);

    return panel;
  }


  /**
   * Creates the panel for selling stocks.
   *
   * @return JPanel for selling stocks
   */
  private JPanel createSellStockPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    // Set preferred size for text fields
    Dimension fieldSize = new Dimension(200, 25);

    portfolioNameFieldSell.setPreferredSize(fieldSize);
    tickerSymbolFieldSell.setPreferredSize(fieldSize);
    quantityFieldSell.setPreferredSize(fieldSize);
    dateFieldSell.setPreferredSize(fieldSize);

    // Add the labels and fields to the panel
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Enter stock details:"), gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Portfolio Name:"), gbc);
    gbc.gridx = 1;
    panel.add(portfolioNameFieldSell, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Ticker Symbol:"), gbc);
    gbc.gridx = 1;
    panel.add(tickerSymbolFieldSell, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Quantity:"), gbc);
    gbc.gridx = 1;
    panel.add(quantityFieldSell, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
    gbc.gridx = 1;
    panel.add(dateFieldSell, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 2; // Make the button span across two columns
    gbc.anchor = GridBagConstraints.CENTER;
    sellButton = new JButton("Sell");

    panel.add(sellButton, gbc);
    panel.add(messageLabelSell);

    return panel;
  }

  /**
   * Creates the panel for getting the value of a portfolio
   *
   * @return JPanel for the value of portfolio
   */
  private JPanel createPortfolioValuePanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    // Set preferred size for text fields
    Dimension fieldSize = new Dimension(200, 25);
    portfolioNameFieldTotal.setPreferredSize(fieldSize);
    dateFieldTotal.setPreferredSize(fieldSize);

    // Add the labels and fields to the panel
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Enter stock details:"), gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Portfolio Name:"), gbc);
    gbc.gridx = 1;
    panel.add(portfolioNameFieldTotal, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
    gbc.gridx = 1;
    panel.add(dateFieldTotal, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 2; // Make the button span across two columns
    gbc.anchor = GridBagConstraints.CENTER;

    totalValueButton = new JButton("Total Value");

    panel.add(totalValueButton, gbc);
    panel.add(messageLabelValue);

    return panel;
  }

  /**
   * Creates the panel for getting the composition of a portfolio.
   *
   * @return JPanel for getting composition of portfolio
   */
  private JPanel createPortfolioCompositionPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    // Set preferred size for text fields
    Dimension fieldSize = new Dimension(200, 25);

    portfolioNameFieldComp.setPreferredSize(fieldSize);
    dateFieldComp.setPreferredSize(fieldSize);

    // Add the labels and fields to the panel
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Enter stock details:"), gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Portfolio Name:"), gbc);
    gbc.gridx = 1;
    panel.add(portfolioNameFieldComp, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
    gbc.gridx = 1;
    panel.add(dateFieldComp, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 2; // Make the button span across two columns
    gbc.anchor = GridBagConstraints.CENTER;
    compositionButton = new JButton("Composition");

    panel.add(compositionButton, gbc);
    panel.add(messageLabelComposition);

    return panel;
  }

  /**
   * Creates the panel for saving portfolios.
   *
   * @return JPanel for saving portfolios
   */
  private JPanel createSavePortfolioPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    // Set preferred size for text fields
    Dimension fieldSize = new Dimension(200, 25);
    portfolioNameFieldSave.setPreferredSize(fieldSize);

    // Add the labels and fields to the panel
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Enter stock details:"), gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Portfolio Name:"), gbc);
    gbc.gridx = 1;
    panel.add(portfolioNameFieldSave, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 2; // Make the button span across two columns
    gbc.anchor = GridBagConstraints.CENTER;
    saveButton = new JButton("Save");

    panel.add(saveButton, gbc);
    panel.add(messageLabelSave);

    return panel;
  }

  /**
   * Creates the panel for loading portfolios.
   *
   * @return JPanel for loading portfolios
   */
  private JPanel createLoadPortfolioPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    // Set preferred size for text fields
    Dimension fieldSize = new Dimension(200, 25);
    portfolioNameFieldLoad.setPreferredSize(fieldSize);

    // Add the labels and fields to the panel
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Enter stock details:"), gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    panel.add(new JLabel("Portfolio Name:"), gbc);
    gbc.gridx = 1;
    panel.add(portfolioNameFieldLoad, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 2; // Make the button span across two columns
    gbc.anchor = GridBagConstraints.CENTER;
    loadButton = new JButton("Load");

    panel.add(loadButton, gbc);
    panel.add(messageLabelLoad);

    return panel;
  }

  /**
   * Enables or disables actions related to this program.
   *
   * @param enabled If true, enables the stock-related action,
   *                otherwise, disables them.
   */
  private void setStockActionsEnabled(boolean enabled) {
    buyButton.setEnabled(enabled);
    sellButton.setEnabled(enabled);
    totalValueButton.setEnabled(enabled);
    saveButton.setEnabled(enabled);
    loadButton.setEnabled(enabled);
    compositionButton.setEnabled(enabled);
  }

  /**
   * Displays an error message for the create operation.
   *
   * @param error the error message to display
   */
  public void displayErrorCreate(String error) {
    if (messageLabel != null) {
      messageLabel.setText(error);
    }
  }

  /**
   * Displays an error message for the buy operation.
   *
   * @param error the error message to display
   */
  public void displayErrorBuy(String error) {
    if (messageLabelBuy != null) {
      messageLabelBuy.setText(error);
    }
  }

  /**
   * Displays an error message for the sell operation.
   *
   * @param error the error message to display
   */
  public void displayErrorSell(String error) {
    if (messageLabelSell != null) {
      messageLabelSell.setText(error);
    }
  }

  /**
   * Displays an error message for the composition operation.
   *
   * @param error the error message to display
   */
  public void displayErrorComp(String error) {
    if (messageLabelComposition != null) {
      messageLabelComposition.setText(error);
    }
  }

  /**
   * Displays an error message for the value operation.
   *
   * @param error the error message to display
   */
  public void displayErrorValue(String error) {
    if (messageLabelValue != null) {
      messageLabelValue.setText(error);
    }
  }

  /**
   * Displays an error message for the save operation.
   *
   * @param error the error message to display
   */
  public void displayErrorSave(String error) {
    if (messageLabelSave != null) {
      messageLabelSave.setText(error);
    }
  }

  /**
   * Displays an error message for the load operation.
   *
   * @param error the error message to display
   */
  public void displayErrorLoad(String error) {
    if (messageLabelLoad != null) {
      messageLabelLoad.setText(error);
    }
  }

  /**
   * Displays a message in the display area of the GUI.
   *
   * @param message The message to be displayed.
   */
  @Override
  public void displayMessage(String message) {
    if (messageLabel != null) {
      messageLabel.setText(message);
    }
  }

  /**
   * Displays an error message to the user.
   *
   * @param message The error message to be displayed.
   */
  @Override
  public void displayError(String message) {
    displayArea.append("Error: " + message + "\n");
  }

  /**
   * Displays a message indicating that a portfolio was created.
   *
   * @param portfolioName the message to be displayed.
   */
  @Override
  public void displayCreatePortfolio(String portfolioName) {
    if (messageLabel != null) {
      messageLabel.setText("Portfolio " + portfolioName + " created successfully.");
    }
  }

  /**
   * Displays a message indicating that a stock was added to the portfolio.
   *
   * @param quantity     the quantity of stock added.
   * @param tickerSymbol the ticker symbol of the removed stock.
   */
  @Override
  public void displayFetchAndAddStock(int quantity, String tickerSymbol) {
    if (messageLabelBuy != null) {
      messageLabelBuy.setText(quantity + " shares of stock " + tickerSymbol
              + " added successfully.");
    }
  }

  /**
   * Displays a message indicating that a stock was removed from the portfolio.
   *
   * @param quantity     the quantity of stock removed.
   * @param tickerSymbol the ticker symbol of the removed stock.
   */
  @Override
  public void displayFetchAndRemoveStock(int quantity, String tickerSymbol) {
    if (messageLabelSell != null) {
      messageLabelSell.setText(quantity + " shares of stock " + tickerSymbol
              + " added successfully.");
    }
  }

  /**
   * Displays a message indicating that a portfolio was saved.
   *
   * @param filename the name of the file to which the portfolio was saved.
   */
  @Override
  public void displaySavePortfolio(String filename) {
    if (messageLabelSave != null) {
      messageLabelSave.setText("Portfolio saved successfully to " + filename);
    }
  }

  /**
   * Displays a message indicating that a portfolio was loaded.
   *
   * @param filename the name of the file to which the portfolio was loaded.
   */
  @Override
  public void displayRetrievePortfolio(String filename) {
    if (messageLabelLoad != null) {
      messageLabelLoad.setText("Portfolio " + filename + " retrieved successfully.");
    }
  }


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
  @Override
  public void setFeatures(Features features) {
    createButton.addActionListener(l -> features.handleCreatePortfolio(portfolioNameFieldCreate
            .getText().trim()));

    buyButton.addActionListener(l -> features.handleFetchAndAddStock(portfolioNameFieldBuy
                    .getText().trim(), tickerSymbolFieldBuy.getText().trim(),
            LocalDate.parse(dateFieldBuy.getText().trim()),
            Integer.parseInt(quantityFieldBuy.getText().trim())));

    sellButton.addActionListener(l -> features.handleFetchAndRemoveStock(portfolioNameFieldSell
                    .getText().trim(),
            tickerSymbolFieldSell.getText().trim(), LocalDate.parse(dateFieldSell.getText().trim()),
            Integer.parseInt(quantityFieldSell.getText().trim())));

    compositionButton.addActionListener(l -> features
            .handlePortfolioComposition(portfolioNameFieldComp
                    .getText().trim(), LocalDate.parse(dateFieldComp.getText().trim())));

    totalValueButton.addActionListener(l -> features.handlePortfolioValue(portfolioNameFieldTotal
            .getText().trim(), LocalDate.parse(dateFieldTotal.getText().trim())));

    saveButton.addActionListener(l -> features.handleSavePortfolio(portfolioNameFieldSave
            .getText().trim()));

    loadButton.addActionListener(l -> features.handleLoadPortfolio(portfolioNameFieldLoad
            .getText().trim()));

  }

  /**
   * Sets the boolean true when the message should be displayed.
   */
  @Override
  public void display() {
    setVisible(true);
  }


  /**
   * Displays the value of a portfolio on a specific date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date for which the portfolio value is calculated.
   * @param value         The calculated value of the portfolio.
   */
  @Override
  public void displayPortfolioValue(String portfolioName, LocalDate date, double value) {
    if (messageLabelValue != null) {
      messageLabelValue.setText("Value of portfolio " + portfolioName + " on " + date + ": "
              + value + "\n");
    }
  }

  /**
   * Displays the composition of a portfolio on a specific date.
   *
   * @param composition A map of stocks to their quantities.
   * @param date        The date for which the composition is displayed.
   */
  @Override
  public void displayPortfolioComposition(Map<String, Double> composition, LocalDate date) {
    StringBuilder totalAppended = new StringBuilder();
    totalAppended.append("Composition of portfolio on ").append(date).append(":\n");
    composition.forEach((stock, percentage) -> totalAppended.append(stock).append(": ")
            .append(percentage));

    if (messageLabelComposition != null) {
      messageLabelComposition.setText(totalAppended.toString());
    }
  }

  /**
   * Displays a message indicating that a requested stock was not found.
   */
  @Override
  public void displayStockNotFound() {
    displayArea.append("Stock not found.\n");
  }
}