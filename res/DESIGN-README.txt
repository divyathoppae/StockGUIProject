1. For this assignment, we threw certain exceptions where we may had missed them. Moreover,
there was one exception that we had neglected to check, which was the possibility of selling more
stocks than had bought. So that was another exception we had added, and includeed in our code. We
decided to add this, as it would be beneifical to the user to know if they have a negative number
of stocks and that they needed to buy more. Hence, the user is more aware of the actions they are
taking, and is more user-friendly. Additionally, we realized in the previous assignments that we had
been hardcoding the API key and accessing that data. We made an interface as suggested in our
self evaluation and a class that holds the alphavantage stock data provider. This way we are not
hardcoding this and as a result out Stock Object class is so much cleaner. We also added a GUIView
interface and a StockGUIView that implements GUIView and View while the StockView implements View
and TextView. This way we contained the common view methods in the interface View and made sure
of proper inheritance and limited duplication. Finally, we now have a Features interface that the
GUIController implements in order to use an effective callback design.

The design of this program is based on the Model, View, and Controller Design. In our file, we have
three interfaces: an interface for the View, Model, and StockController. These hold the public
methods that are implemented in all instances of classes that implement these interfaces. For this
assignment we made a class that implements StockController called StockControllerImpl that holds
the public method called start that executes the program. The rest of the methods in this class are
private helper methods that delegate to the View and the Model for each of the different options in
the start method. Moreover, the StockModel class implements Model and holds two maps for portfolios
and stocks as well as methods with operations. The Portfolio, Stock, and StockPrice classes are all
classes that represent objects that the StockModel accesses. Stock holds the data in a Map with the
stock ticker symbol and stock price object. The StockPrice class has all the fields, date, double
open, high, low, close, volume that are accessible. In order to test our StockControllerImpl class
we created a MockView and a MockModel in order to isolate the StockControllerImpl class. We
recorded output in the MockView and checked if StockControllerImpl tests contained the correct
output. Additionally, we designed other simple test classes to test StockPrice, StockView and
StockView. Finally, we run the program from the StockMainProgram that runs the start method in
controller. Moreover, we have a second controller for the GUI, so that when the program runs,
it access that controller, in order to know what operations are available, since this is different
from the text base interface. Moreover, a new view was created specifically for the GUI since new
code has to be provided for creating the images. With all of this combined, the program can
essentially allow for two different types of programs: text-based and GUI. Within the main
controller that starts the program, based on the command-line, it will open the right program in
the terminal.