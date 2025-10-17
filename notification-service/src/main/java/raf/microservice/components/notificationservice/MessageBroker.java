package raf.microservice.components.notificationservice;

import org.apache.activemq.broker.BrokerService;

import java.util.Scanner;

public class MessageBroker {
    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();
        // configure the broker
        broker.addConnector("tcp://localhost:61616");
        broker.start();
        Scanner myObj = new Scanner(System.in);  // Create a Scanner objec
        String exit = myObj.nextLine();
        System.out.println(exit);
    }
}
