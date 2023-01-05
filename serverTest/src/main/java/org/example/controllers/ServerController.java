package org.example.controllers;

import java.net.Socket;
import org.example.services.ServerUtilities;
import org.example.model.Server;

/*
 * @author Oksiuta Andrii
 * 04.01.2023
 * 14:56
 */
public class ServerController {

  public static void work(int port) {
    try {
      Server test = new Server(port);

      Thread trServer = new Thread(() -> ServerUtilities.serverConsole(test));
      trServer.start();

      while (!test.getServerSocket().isClosed()) {
        Socket clientSocket = ServerUtilities.acceptClientSocket(test);
        //Sending
        ServerUtilities.serverMassSending(test,
            "\n[Client" + clientSocket.getPort() + " is Online...]");
        //Receiving
        Thread tr = new Thread(() -> ServerUtilities.serverClientLogic(clientSocket, test));
        tr.start();
        test.getThreadList().add(tr);
      }

      for (Thread thread : test.getThreadList()) {
        thread.join();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      // Restore interrupted state...
      Thread.currentThread().interrupt();
    }
  }
}
