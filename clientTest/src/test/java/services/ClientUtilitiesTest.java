package services;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import java.io.ObjectOutputStream;
import org.example.services.ClientUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/*
 * @author Oksiuta Andrii
 * 05.01.2023
 * 14:45
 */
class ClientUtilitiesTest {


  @Test
  void testSwitcher() {
    String test = "";
    ClientUtilities.switcher(null, test);
  }

  @Test
  void testExitClient() {
    String textExpected = "test";

    ObjectOutputStream testStream = Mockito.mock(ObjectOutputStream.class);

    //test for System.exit
    try {
      SystemLambda.catchSystemExit(() -> {
        ClientUtilities.exitClient(testStream, textExpected);
      });
    } catch (Exception e) {
      e.printStackTrace();
    }

    Assertions.assertThrows(NullPointerException.class,
        () -> ClientUtilities.exitClient(testStream, textExpected));
  }

  @Test
  void testIsFileSend() {
    String pathEmpty = "";
    String pathReal = "C:/Users/7not9/IdeaProjects/Server_Client/clientTest/src/test/java/resources/test.txt";

    ObjectOutputStream testStream = Mockito.mock(ObjectOutputStream.class);
    Mockito.doNothing().when(testStream);

    Assertions.assertFalse(ClientUtilities.isFileSend(testStream, pathEmpty));

    ClientUtilities.isFileSend(null, pathReal);
    ClientUtilities.isFileSend(testStream, null);
  }
}
