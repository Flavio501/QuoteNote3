package com.example.stark.QuoteNote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;

public class ConnectionHandler {

    String ip = "10.12.33.143";//CETYS
    int port = 5555;
    Socket socket;
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Gson gson = new Gson();

    public ConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        String clientAddress = clientSocket.getInetAddress().toString()
                .substring(1);
        System.out.println("Connected to " + clientAddress);
        try {
            //oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ConnectionHandler(Socket clientSocket, ObjectInputStream oi, ObjectOutputStream oo) {
        this.clientSocket = clientSocket;
        this.ois = oi;
        this.oos = oo;
        String clientAddress = clientSocket.getInetAddress().toString()
                .substring(1);
        System.out.println("Connected to " + clientAddress);
    }

    public void stopInput() {
        try {
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stopConnection() {
        try {
            clientSocket.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendObject(Object o) {
        try {
            //oos = (ObjectOutputStream) clientSocket.getOutputStream();
            oos.writeObject(o);
            oos.flush();
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ClienteFree receiveClient() throws Exception{
        try {
            //ois = (ObjectInputStream) clientSocket.getInputStream();
            ClienteFree cliente = gson.fromJson((String)(ois.readObject()), ClienteFree.class);

            ois.close();
            return cliente;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Quote> receiveQuoteGson() throws Exception{
        try {
            //ois = new ObjectInputStream(socket.getInputStream());
            Type quoteListType = new TypeToken<List<Quote>>(){}.getType();
            List<Quote> newQuotes = gson.fromJson((String)(ois.readObject()), quoteListType);
            ois.close();
            return newQuotes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void stopOutput() {
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}