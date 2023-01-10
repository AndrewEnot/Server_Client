package example.services;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.Scanner;
import org.example.model.Server;
import org.example.services.ServerUtilities;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/*
 * @author Oksiuta Andrii
 * 04.01.2023
 * 15:10
 */
class ServerUtilitiesTest {
  protected static ByteArrayOutputStream output;
  private static PrintStream old;
  private static InputStream oldIn;



  @BeforeAll
  static void setUpStreams() {
    old = System.out;
    oldIn = System.in;
    output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

  }
  @Test
  void testServerConsole() {
    Server testServer = Mockito.mock(Server.class);

    ServerSocket serverSocket = Mockito.mock(ServerSocket.class);
    Mockito.when(serverSocket.isClosed()).thenReturn(false);

    String textShut = "-shutdown";

    //test for System.exit
    try {
      SystemLambda.catchSystemExit(() -> {
        ServerUtilities.serverConsole(testServer,textShut);
      });
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  @AfterAll
  static void cleanUpStreams() {
    System.setOut(old);
  }
}
