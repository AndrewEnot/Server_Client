package org.example.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
 * @author Oksiuta Andrii
 * 04.01.2023
 * 14:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Server {

  private ServerSocket serverSocket;
  private Map<Socket, ClientCard> serverConnections;
  private List<Thread> threadList;

  public Server(int port) throws IOException {
    this.serverSocket = new ServerSocket(port);
    this.serverConnections = new HashMap<>();
    this.threadList = new ArrayList<>();
  }
}
