package org.example;

import org.example.controllers.ServerController;

/*
 * @author Oksiuta Andrii
 * 30.12.2022
 * 11:38
 */
public class Main {

  public static void main(String[] args) {

    System.out.println("Starting");
    ServerController.work(8080);
    System.out.println("finish");
  }
}