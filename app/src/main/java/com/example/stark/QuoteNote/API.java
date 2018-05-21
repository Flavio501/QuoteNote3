package com.example.stark.QuoteNote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class API {

    List<Quote> quotes;
    static ClienteFree client = new ClienteFree("Null","Null","Null","null");
    static ClienteFree client2 = new ClienteFree("Pancho","Test","Correo@correo.com","MiContra");
    String Test = "Testing this crap again";
    static ServerSocket serverSocket;
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public List<Quote> parseHTML(URL url) throws Exception{
        quotes = new ArrayList<Quote>();
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        Document doc = Jsoup.parse(url, 5000);
        Element table = doc.select("table").get(0); //select the first table.
        Elements rows = table.select("tr");

        for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
            Element row = rows.get(i);
            Elements cols = row.select("td");

            quotes.add(new Quote(parseCol(cols)));
        }
        return quotes;
    }

    public List<Quote> getQuotes(){
        return quotes;
    }

    public void setQuoteParameters(URL url, List<Quote> quotes) throws Exception{
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        Document doc = Jsoup.parse(url, 5000);
        Element table = doc.select("table").get(0); //select the first table.
        Elements rows = table.select("tr");

        int cont = 0;
        for(Quote q : quotes) {
            Elements cols = rows.get(cont).select("td");
            q.setParameters(parseCol(cols));
            cont++;
        }
    }

    public Wrapper parseCol(Elements cols) {
        return new Wrapper(cols.get(0).text().toString(),
                Long.parseLong(cols.get(1).text().toString()),
                new BigDecimal(cols.get(2).text().toString()),
                Integer.parseInt(cols.get(3).text().toString()),
                new BigDecimal(cols.get(4).text().toString()),
                Integer.parseInt(cols.get(5).text().toString()),
                new BigDecimal(cols.get(6).text().toString()),
                new BigDecimal(cols.get(7).text().toString()),
                new BigDecimal(cols.get(8).text().toString()));
    }

    public void Subscribe(Quote q, ClienteFree client) {
        q.addObserver(client);
        client.addQuote(q);
        client.setOldValues(q);
    }

    public void sendObject(Object o) {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(o);
            oos.flush();
            //oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClienteFree receiveClient() throws Exception{
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            ClienteFree cliente = (ClienteFree)(ois.readObject());
            ois.close();
            return cliente;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Quote> receiveQuote() throws Exception{
        try {
            ois = (ObjectInputStream) socket.getInputStream();
            List<Quote> newQuotes = (List<Quote>)(ois.readObject());
            ois.close();
            return newQuotes;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void STOP() {
        stopOutput();
        stopServer();
    }

    public void stopServer() {
        try {
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopOutput() {
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopInput() {
        try {
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}