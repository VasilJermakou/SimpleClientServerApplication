package com.vaer;

import java.util.Scanner;

public class SocketorRunner {
    public static void main(String[] args) {

        /**
         * Client server socket console application in Java
         * */

        /* class fields */
        String socketorMode = "";
        String portNumber = "";
        String operation = "";
        String value1 = "";
        String value2 = "";

        /* main logic */
        try {
            System.out.println("SocketorRunner starting ... \n\r");

            Scanner scanner = new Scanner(System.in);
            System.out.println(">>Enter socketor mode, please(Server/Client): ");
            socketorMode = scanner.next();

            System.out.println(">>Enter port number, please(integer value from 30000 to 40000): ");
            portNumber = scanner.next();

            if(socketorMode.equalsIgnoreCase("Client")) {
                System.out.println(">>Enter operation, please( + - * / ): ");
                operation = scanner.next();

                System.out.println(">>Enter first value, please(integer number): ");
                value1 = scanner.next();

                System.out.println(">>Enter second value, please(integer number): ");
                value2 = scanner.next();

                int num1 = Integer.valueOf(value1);
                int num2 = Integer.valueOf(value2);
                int port = Integer.valueOf(portNumber);
                Socketor clientSocketor = new Socketor(socketorMode, port, operation, num1, num2);
                Thread.sleep(3000);
            }

            if(socketorMode.equalsIgnoreCase("Server")){
                int port = Integer.valueOf(portNumber);
                Socketor serverSocketor = new Socketor(socketorMode, port);
                Thread.sleep(3000);
            }

            scanner.close();

        }catch(InterruptedException exception) {
            System.out.println("InterruptedException in SocketorRunner !!!");
            exception.printStackTrace();
        }

    }//main ends
}//class ends
