import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the StockDataProvider interface that fetches historical stock prices
 * from the AlphaVantage API.
 */
public class AlphaVantageStockDataProvider implements StockDataProvider {
  private static final String apiKey = "TIKUGQGX5SGSNYAE";


  /**
   * Fetches historical stock prices for a given ticker symbol.
   *
   * @param tickerSymbol the ticker symbol of the stock
   * @return a map where the keys are dates and the values are the stock prices on those dates
   * @throws IOException if an I/O error occurs
   */
  @Override
  public Map<LocalDate, StockPrice> fetchHistoricalPrices(String tickerSymbol) throws IOException {
    URL url;
    try {
      url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&outputsize"
              + "=full&symbol=" + tickerSymbol + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("The AlphaVantage API has either changed or no longer works");
    }

    InputStream in;
    StringBuilder output = new StringBuilder();
    String fileOutput = "";

    try {
      in = url.openStream();
      int b;
      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (UnknownHostException e) {
      String currentDir = System.getProperty("user.dir");
      String relativePath = tickerSymbol + ".csv";
      String absolutePath = currentDir + "/" + relativePath;
      File file = new File(absolutePath);
      if (file.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
          fileOutput = br.lines().collect(Collectors.joining("\r\n"));
        } catch (IOException f) {
          throw new IOException("Couldn't read from file.");
        }
      }
    }

    String[] lines;
    if (fileOutput.isEmpty()) {
      lines = output.toString().split("\r\n");
      try {
        writeCsvFile(tickerSymbol + ".csv", lines);
      } catch (IOException e) {
        throw new IOException("Unable to write csv file for stock with ticker symbol: "
                + tickerSymbol);
      }
    } else {
      lines = fileOutput.split("\r\n");
    }

    Map<LocalDate, StockPrice> historicalPrices = new HashMap<>();
    for (int i = 1; i < lines.length; i++) {
      String[] stockOutputLine = lines[i].split(",");
      StockPrice stockPrice = new StockPrice(
              LocalDate.parse(stockOutputLine[0]),
              Double.parseDouble(stockOutputLine[1]),
              Double.parseDouble(stockOutputLine[2]),
              Double.parseDouble(stockOutputLine[3]),
              Double.parseDouble(stockOutputLine[4]),
              Long.parseLong(stockOutputLine[5])
      );
      historicalPrices.put(LocalDate.parse(stockOutputLine[0]), stockPrice);
    }
    return historicalPrices;
  }

  /**
   * Writes the fetched stock prices to a CSV file.
   *
   * @param fileName the name of the file to write
   * @param lines    the lines of stock data to write
   * @throws IOException if an I/O error occurs
   */
  private void writeCsvFile(String fileName, String[] lines) throws IOException {
    try (FileWriter writer = new FileWriter(fileName)) {
      for (String line : lines) {
        writer.append(line).append("\n");
      }
    }
  }
}
