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
 * 05.01.2023
 * 14:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientCard {

  private String name;
  private Socket clientSocket;
  private ObjectOutputStream objectOutputStream;
  private ObjectInputStream objectInputStream;

  public ClientCard(Socket clientSocket) throws IOException {
    this.clientSocket = clientSocket;
    name = "Client_" + clientSocket.getPort();
    objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
  }
}
