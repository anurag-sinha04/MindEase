package com.example.mentalhealthapp;

public class ChatMessage {
    private String text;
    private long time;

    public ChatMessage() {} // Firestore needs empty constructor

    public ChatMessage(String text, long time) {
        this.text = text;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public long getTime() {
        return time;
    }
}
