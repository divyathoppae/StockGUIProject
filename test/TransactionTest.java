import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * test class for the Transaction Class.
 */
public class TransactionTest {

  // testing that all the getters are working
  @Test
  public void testTransactionConstructorAndGetters() {
    // Create a transaction
    String tickerSymbol = "AAPL";
    int shares = 10;
    LocalDate date = LocalDate.of(2023, 6, 1);
    TransactionType type = TransactionType.BUY;

    Transaction transaction = new Transaction(tickerSymbol, shares, date, type);

    // Test getters
    assertEquals(tickerSymbol, transaction.getTickerSymbol());
    assertEquals(shares, transaction.getShares(), 0.00001);
    assertEquals(date, transaction.getDate());
    assertEquals(type, transaction.getType());
  }

  // confirming that the enum works for the transaction type within that class
  @Test
  public void testTransactionType() {
    // Create a buy transaction
    Transaction buyTransaction = new Transaction("AAPL", 10,
            LocalDate.of(2023, 6, 1), TransactionType.BUY);
    assertEquals(TransactionType.BUY, buyTransaction.getType());

    // Create a sell transaction
    Transaction sellTransaction = new Transaction("AAPL", 5,
            LocalDate.of(2023, 6, 2), TransactionType.SELL);
    assertEquals(TransactionType.SELL, sellTransaction.getType());
  }
}