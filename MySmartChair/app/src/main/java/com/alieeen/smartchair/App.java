package com.alieeen.smartchair;

import com.alieeen.smartchair.model.Message;

import java.util.ArrayList;

/**
 * Created by alinekborges on 18/05/15.
 */
public class App {
    private static App ourInstance = new App();

    public static App getInstance() {
        return ourInstance;
    }

    private App() {

    }

    private ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<Message> receivedMessages = new ArrayList<>();


    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(ArrayList<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public void addReceivedMessage(String m) {
        Message message = new Message();
        message.setReceived(m);
        messages.add(message);
        receivedMessages.add(message);
    }

    public void addSentMessage(String m) {
        Message message = new Message();
        message.setSent(m);
        messages.add(message);
    }
}
