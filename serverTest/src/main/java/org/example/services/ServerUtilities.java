package org.example.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Map.Entry;
import java.util.Scanner;
import org.example.model.ClientCard;
import org.example.model.Server;

/*
 * @author Oksiuta Andrii
 * 04.01.2023
 * 15:10
 */
public class ServerUtilities {

  private ServerUtilities() {
    throw new IllegalStateException("Utility class");
  }

  public static void serverConsole(Server server, String command) {
    if (server == null || command == null) {
      return;
    }
    while (!server.getServerSocket().isClosed()) {
      if (command.equals("-shutdown")) {
        serverMassSending(server, "\nSERVER IS SHUTTED DOWN");
        System.exit(111);
      }
    }
  }

  public static void serverMassSending(Server server, String massage) {
    for (Entry<Socket, ClientCard> socketStringEntry : server.getServerConnections().entrySet()) {
      try {
        socketStringEntry.getValue().getObjectOutputStream().writeObject(massage);
      } catch (IOException e) {
        System.out.println("Some problems in massSending!!!");
      }
    }
    System.out.println(massage);
  }

  public static void serverClientLogic(Socket clientSocket, Server server) {
    try {
      ObjectInputStream inStream = server.getServerConnections().get(clientSocket)
          .getObjectInputStream();
      while (!clientSocket.isClosed()) {
        Object o = inStream.readObject();
        switch (o.toString()) {
          case "-exit":
            String exitMessage = "\n[" +
                server.getServerConnections().get(clientSocket).getName() + " is out!!!]";
            clientSocket.close();
            server.getServerConnections().remove(clientSocket);
            ServerUtilities.serverMassSending(server, exitMessage);
            break;
          case "File receiving...":
            System.out.println(o);
            Object f = inStream.readObject();
            File file = new File(
                "serverTest/src/main/resources",
                "file_" + server.getServerConnections().get(clientSocket).getName() + "_"
                    + LocalDate.now());
            try (FileWriter writer = new FileWriter(file)) {
              writer.write((char[]) f);
            }
            System.out.println(file.getName() + " - received");
            break;
          default:
            System.out.println("Incorrect command...(");
            break;
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static String getScanner() {
    Scanner textIn = new Scanner(System.in);
    System.out.print("Input a command... ");
    return textIn.next();
  }
}
