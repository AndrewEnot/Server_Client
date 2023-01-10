package org.example.controllers;

import java.io.IOException;
import java.net.Socket;
import org.example.model.ClientCard;
import org.example.services.ServerUtilities;
import org.example.model.Server;

/*
 * @author Oksiuta Andrii
 * 04.01.2023
 * 14:56
 */
public class ServerController {

  private ServerController() {
    throw new IllegalStateException("Controller class");
  }

  public static void work(int port) {
    try {
      Server server = new Server(port);

      Thread trServer = new Thread(() -> {
        String scanner = ServerUtilities.getScanner();
        ServerUtilities.serverConsole(server,scanner);
      });
      trServer.start();

      while (!server.getServerSocket().isClosed()) {

        //Socket accepting and adding to Map
        Socket clientSocket = server.getServerSocket().accept();
        server.getServerConnections().put(clientSocket, new ClientCard(clientSocket));

        //Sending
        ServerUtilities.serverMassSending(server,
            "\n[Client" + clientSocket.getPort() + " is Online...]");

        //Receiving
        Thread tr = new Thread(() -> ServerUtilities.serverClientLogic(clientSocket, server));
        tr.start();
        server.getThreadList().add(tr);

      }

      for (Thread thread : server.getThreadList()) {
        thread.join();
      }

    } catch (InterruptedException |IOException e) {
      e.printStackTrace();
      // Restore interrupted state...
      Thread.currentThread().interrupt();
    }
  }
}
