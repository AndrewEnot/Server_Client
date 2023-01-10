package org.example.services;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import org.example.model.Client;

/*
 * @author Oksiuta Andrii
 * 05.01.2023
 * 14:45
 */
public class ClientUtilities {

  private ClientUtilities() {
    throw new IllegalStateException("Utility class");
  }

  private static String getScanner(String message) {
    String text = "";
    if (message != null) {
      Scanner textIn = new Scanner(System.in);
      System.out.print(message);
      text = textIn.next();
    }
    return text;
  }

  public static void clientServerLogic(Client client) {
    if (client == null) {
      return;
    }
    while (client.getClientSocket().isConnected()) {
      String text = getScanner("Input a command:");
      switcher(client.getObjectOutputStream(), text);
    }
  }

  public static void switcher(ObjectOutputStream clientStream, String text) {
    if (clientStream == null || text == null) {
      return;
    }
    switch (text) {
      case "-exit":
        exitClient(clientStream, text);
        break;
      case "-file":
        String path = getScanner(
            "Please, input path of file you are sending: ");
        if (!isFileSend(clientStream, path)) {
          System.out.println("File didn't send!!!");
        }
        break;
      default:
        System.out.println("Incorrect command...(");
        break;
    }
  }

  public static boolean isFileSend(ObjectOutputStream clientStream, String path) {
    if (clientStream == null || path == null) {
      return false;
    }
    File file = new File(path);
    if (file.exists()) {
      System.out.println("File sending");
      try {
        clientStream.writeObject("File receiving...");
        char[] chars = new char[(int) file.length()];
        try (FileReader reader = new FileReader(file)) {
          int read = reader.read(chars);
          if (read == -1) {
            return true;
          }
          clientStream.writeObject(chars);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      return true;
    }
    return false;
  }

  public static void exitClient(ObjectOutputStream clientStream, String text) {
    if (clientStream == null || text == null) {
      return;
    }
    try {
      clientStream.writeObject(text);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("You ending session. Have a nice day!!!");
    System.exit(111);
  }
}
