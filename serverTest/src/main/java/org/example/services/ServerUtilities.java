package org.example.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.Scanner;
import org.example.model.Client;
import org.example.model.Server;

/*
 * @author Oksiuta Andrii
 * 04.01.2023
 * 15:10
 */
public class ServerUtilities {
  public static void serverConsole(Server server) {
    while (!server.getServerSocket().isClosed()) {
      Scanner textIn = new Scanner(System.in);
      System.out.print("Server: Input a command... ");
      String text = textIn.next();
      System.out.println("You have entered: " + text);
      if (text.equals("-shutdown")) {
        for (int i = 5; i >= 0; i--) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            // Restore interrupted state...
            Thread.currentThread().interrupt();
          }
          System.out.println(i);
        }
        serverMassSending(server,"\nSERVER IS SHUTTED DOWN");
        System.exit(111);
      }
    }
  }

  public static void serverMassSending(Server server, String  massage) {
    for (Entry<Socket, Client> socketStringEntry : server.getServerConnections().entrySet()) {
      String oOut = massage;
      try {
        socketStringEntry.getValue().getObjectOutputStream().writeObject(oOut);
      } catch (IOException e) {
        System.out.println("Some problems in massSending ");
      }
    }
  }

  public static Socket acceptClientSocket(Server server) {
    Socket clientSocket = null;
    try {
      clientSocket = server.getServerSocket().accept();
      System.out.println(clientSocket.isConnected());
      System.out.println(clientSocket.getPort());
      server.getServerConnections().put(clientSocket,
          new Client("Client" + clientSocket.getPort(), clientSocket,
              new ObjectOutputStream(clientSocket.getOutputStream()),
              new ObjectInputStream(clientSocket.getInputStream()),
              LocalDateTime.now()));

    } catch (IOException e) {
      e.printStackTrace();
    }
    return clientSocket;
  }
  public static void serverClientLogic(Socket clientSocket, Server server) {
    try {
      ObjectInputStream inStream = server.getServerConnections().get(clientSocket)
          .getObjectInputStream();
      while (!clientSocket.isClosed()) {
        Object o = inStream.readObject();
        switch (o.toString()) {
          case "-exit":
            clientSocket.close();
            System.out.println(server.getServerConnections().get(clientSocket) + " is out!!!");
            server.getServerConnections().remove(clientSocket);
            break;
          case "File receiving...":
            System.out.println(o);
            Object f = inStream.readObject();
            File file = new File(
                "C:/Users/7not9/IdeaProjects/Server_Client/serverTest/src/main/resources",
                "file_" + server.getServerConnections().get(clientSocket) + "_" + LocalDate.now());

            try (FileWriter writer = new FileWriter(file)) {
              writer.write((char[]) f);
              break;
            }
          default:
            System.out.println("Incorrect command...(");
            break;
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
