package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 * @author Oksiuta Andrii
 * 30.12.2022
 * 11:38
 */
public class Server {


  public static void main(String[] args) {

    System.out.println("Starting");
    Map<Socket, String> serverConnections = new HashMap<>();
    List<Thread> threadList = new ArrayList<>();

    try (ServerSocket serverSocket = new ServerSocket(8080)) {

      for (int i = 0; i < 2; i++) {

        threadList.add(new Thread(() -> {
          try {
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket.isConnected());
            System.out.println(clientSocket.getPort());
            serverConnections.put(clientSocket, "Client" + clientSocket.getPort());

            //Sending
            for (Entry<Socket, String> socketStringEntry : serverConnections.entrySet()) {
              ObjectOutputStream outStream = new ObjectOutputStream(
                  socketStringEntry.getKey().getOutputStream());
              String oOut = "\n[" + socketStringEntry.getValue() + " is Online...]";
              outStream.writeObject(oOut);
            }

            //Receiving
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());

            while (!clientSocket.isClosed()) {
              Object o = inStream.readObject();
              switch (o.toString()) {
                case "-exit":
                  clientSocket.close();
                  System.out.println(serverConnections.get(clientSocket) + " is out!!!");
                  serverConnections.remove(clientSocket);
                  break;
                case "File receiving...":
                  System.out.println(o);
                  Object f = inStream.readObject();
                  File file = new File(
                      "C:/Users/7not9/IdeaProjects/Server_Client/serverTest/src/main/resources",
                      "file_" + serverConnections.get(clientSocket) + "_" + LocalDate.now());

                  try (FileWriter writer = new FileWriter(file)) {
                    writer.write((char[]) f);
                    break;
                  }
                default:
                  System.out.println("Incorrect command...(");
                  break;
              }
            }

            inStream.close();
          } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
          }
        }));

        threadList.stream().filter(a -> !a.isAlive()).forEach(Thread::start);
      }

      for (Thread thread : threadList) {
        thread.join();
      }


    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }


    System.out.println("finish");
  }
}