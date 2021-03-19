package com.vaer;

import java.net.*;
import java.io.*;

public class Socketor {

    /**
     * Client server socket console application in Java
     * */

    /* class values */
    private String socketorMode;
    private String host;
    private int portNumber;
    private String operation;
    private int value1;
    private int value2;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedWriter writer;
    private BufferedReader reader;

    /* constructors */

    /*
     * Constructor for Server Mode
     * @param socketorMode -> String value, write "server"
     * @param portNumber -> integer value, write value from 30_000 to 40_000
     * Create ServerSocket and Socket to communicate with client.
     * Server work as calculator and can do simple math operations
     * plus, minus, multiply, division
     *
     */
    public Socketor(String socketorMode, int portNumber){
        try {
            this.socketorMode = socketorMode;
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server running ...");

            while(true) {
                clientSocket = serverSocket.accept();

                //to write message for client
                writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                //to read message from client
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                serverAction();
            }

        }catch(IOException exception) {
            System.out.println("Exception in Server !!!");
            exception.getStackTrace();
        }
    }

    /*
     * Constructor for Client Mode
     * @param socketorMode -> String value, write "client"
     * @param portNumber -> integer value, write value from 30_000 to 40_000
     * @param operation -> String value, "+" "-" "*" "/"
     * Create Socket to make request for server
     *
     */
    public Socketor(String socketorMode, int portNumber, String operation, int value1, int value2) {
        try {
            this.socketorMode = socketorMode;
            this.host = "127.0.0.1";
            clientSocket = new Socket(host, portNumber);
            this.operation = operation;
            this.value1 = value1;
            this.value2 = value2;
            System.out.println(">> Client sending request: operation " + this.operation + ", values: " + this.value1 + ", " + this.value2);

            //to write message for server
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            //to read message from server
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            clientAction();

        }catch(IOException exception) {
            System.out.println("Exception in Client !!!");
            exception.getStackTrace();
        }
    }

    /* methods */

    /* helper method for serverAction() method
     * @param request -> String request from client
     * @param operation -> String that containe plus minus multiple or division symbols
     * method parse from request String plus minus multiple or division symbols
     * */
    private static String operationParser(String request, String operation) {
        String result = "";
        char[] charArray = request.toCharArray();

        for(int i = 0; i < request.length(); i++) {
            if(charArray[i] == operation.charAt(0) || charArray[i] == operation.charAt(1) ||
                    charArray[i] == operation.charAt(2) || charArray[i] == operation.charAt(3) ){
                result += charArray[i];
                break;
            }
        }
        return result;
    }

    /* helper method for serverAction() method
     * @param request -> String request from client
     * method parse from request String first and second operands
     * */
    //helper method for serverAction() method
    private static int[] valuesParser(String request) {
        int[] resultArray = new int[2];
        StringBuffer value1 = new StringBuffer("");
        StringBuffer value2 = new StringBuffer("");
        char[] charArray = request.toCharArray();
        int secondValueIndex = 0;

        //find first value
        for(int i = 0; i < request.length(); i++) {
            if(charArray[i] >= 48 && charArray[i] <= 57) {
                value1.append(charArray[i]);
                secondValueIndex = i+1;
                if(charArray[i+1] < 48 || charArray[i+1] > 57)
                    break;
            }
        }

        //find second value
        for(int i = secondValueIndex; i < request.length(); i++) {
            if(charArray[i] >= 48 && charArray[i] <= 57) {
                value2.append(charArray[i]);
            }
        }
        resultArray[0] = Integer.parseInt(value1.toString());
        resultArray[1] = Integer.parseInt(value2.toString());
        return resultArray;
    }

    /* helper method for serverAction() method
     * @param operation -> String value that contain tokens + * - /
     * @param value1 -> first integer number
     * @param value2 -> second integer number
     * */
    private static int calculate(String operation, int value1, int value2)throws RuntimeException {
        int result = 0;

        if(operation.equals("+"))
            result = value1 + value2;

        if(operation.equals("-"))
            result = value1 - value2;

        if(operation.equals("*"))
            result = value1 * value2;

        if(operation.equals("/")) {
            if(value2 != 0)
                return value1 / value2;

            throw new RuntimeException("Incorrect second value: couldn`t be divided to zero");
        }
        return result;
    }

    /* * * * * * * * * * * * * * * * * * * * *
     * Method that contain all Server logics *
     * * * * * * * * * * * * * * * * * * * * */
    public void serverAction() {
        try {
            String request = reader.readLine();
            this.operation = Socketor.operationParser(request, "+-*/");
            int[] intArray = new int[2];
            intArray = Socketor.valuesParser(request);
            this.value1 = intArray[0];
            this.value2 = intArray[1];
            int result = 0;

            result = Socketor.calculate(this.operation, this.value1, this.value2);
            String response = ">>Accepted client: " + this.value1 +" " + this.operation + " " + this.value2 + " = " + result + "\r\n";

            writer.write(response);
            writer.flush();

            //close all resources
            reader.close();
            writer.close();
            clientSocket.close();

        }catch(IOException exception) {
            exception.getStackTrace();
        }catch(RuntimeException exception) {
            String exMessage = exception.getMessage();
            try {
                writer.write(exMessage);
                writer.flush();

                //close all resources
                reader.close();
                writer.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * *
     * Method that contain all Client logics *
     * * * * * * * * * * * * * * * * * * * * */
    public void clientAction() {
        try {
            String request = "Operation: " + this.operation + ", first value: " + this.value1 + ", second value: " + this.value2 + "\r\n";
            writer.write(request);
            writer.flush();

            String response = reader.readLine();
            System.out.println(response);

            //close all resources
            writer.close();
            reader.close();
            clientSocket.close();

        }catch(IOException exception) {
            exception.getStackTrace();
        }
    }

    /* Object method */
    @Override
    public String toString() {
        return "Socketor{" +
                "socketorMode='" + socketorMode + '\'' +
                ", host='" + host + '\'' +
                ", portNumber=" + portNumber +
                ", operation='" + operation + '\'' +
                ", value1=" + value1 +
                ", value2=" + value2 +
                ", serverSocket=" + serverSocket +
                ", clientSocket=" + clientSocket +
                ", writer=" + writer +
                ", reader=" + reader +
                '}';
    }

}//class ends
