package org.example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/*
 * @author Oksiuta Andrii
 * 30.12.2022
 * 11:53
 */
public class Client {

  public static void main(String[] args) {
    int port = 8080;

    try (Socket clientSocket = new Socket("localhost", port);
        ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream())) {

      Thread intThread = new Thread(() -> {
        try {
          Scanner textIn = new Scanner(System.in);

          while (clientSocket.isConnected()) {
            System.out.print("This is client 2: Input a command... ");
            String text = textIn.next();
            System.out.println("You have entered: " + text);

            switch (text) {
              case "-exit":
                outStream.writeObject(text);
                System.out.println("You ending session. Have a nice day!!!");
                System.exit(111);
              case "-file":
                System.out.print("Please, input path of file you are sending: ");
                String path = textIn.next();
                System.out.println("File sending");
                outStream.writeObject("File receiving...");
                File file = new File(path);
                char[] chars = new char[(int) file.length()];
                try (FileReader reader = new FileReader(file)) {
                  int read = reader.read(chars);
                  if (read == -1) {
                    return;
                  }
                  outStream.writeObject(chars);
                }
                break;
              default:
                System.out.println("Incorrect command...(");
                break;
            }
          }

        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      intThread.start();

      while (clientSocket.isConnected()) {
        String in = (String)inStream.readObject();
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
