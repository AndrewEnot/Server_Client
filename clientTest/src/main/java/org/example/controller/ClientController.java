package org.example.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.example.model.Client;
import org.example.services.ClientUtilities;

/*
 * @author Oksiuta Andrii
 * 05.01.2023
 * 14:45
 */
public class ClientController {
  private ClientController() {
    throw new IllegalStateException("Controller class");
  }

  public static void work(int port) {
    try {
      Client client = new Client(port);

      Thread intThread = new Thread(() -> ClientUtilities.clientServerLogic(client));
      intThread.start();

      while (client.getClientSocket().isConnected()) {
        String in = (String)client.getObjectInputStream().readObject();
        if (in.equals("\nSERVER IS SHUTTED DOWN")) {
          System.out.println(in);
          System.exit(105);
        }
        System.out.println(in);
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
