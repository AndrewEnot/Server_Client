package org.example.model;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author Oksiuta Andrii
 * 30.12.2022
 * 11:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

  private Socket clientSocket;
  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;

  public Client(int port) {
    try {
      clientSocket = new Socket("localhost", port);
      objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
      objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
