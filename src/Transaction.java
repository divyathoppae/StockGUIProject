import java.time.LocalDate;

/**
 * Represents a stock transaction.
 * This class represents the details of a transaction,
 * including the ticker symbol, the number of shares,
 * the date of the transaction, and the type of the transaction.
 */
public class Transaction {
  private final String tickerSymbol;
  private final double shares;
  private final LocalDate date;
  private final TransactionType type;

  /**
   * Constructs a new Transaction object with the specified details.
   *
   * @param tickerSymbol the ticker symbol of the stock
   * @param shares       the number of shares involved in the transaction
   * @param date         the date of the transaction
   * @param type         the type of the transaction (BUY or SELL)
   */
  Transaction(String tickerSymbol, double shares, LocalDate date,
              TransactionType type) {
    this.tickerSymbol = tickerSymbol;
    this.shares = shares;
    this.date = date;
    this.type = type;
  }

  /**
   * Returns the date of the transaction.
   *
   * @return the date of the transaction
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Returns the type of the transaction.
   *
   * @return the type of the transaction (BUY or SELL)
   */
  public TransactionType getType() {
    return type;
  }

  /**
   * Returns the number of shares involved in the transaction.
   *
   * @return the number of shares
   */
  public double getShares() {
    return shares;
  }

  /**
   * Returns the ticker symbol of the stock.
   *
   * @return the ticker symbol
   */
  public String getTickerSymbol() {
    return tickerSymbol;
  }

}

