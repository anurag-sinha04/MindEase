package com.example.mentalhealthapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnonymousChat extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText messageBox;
    Button sendBtn;

    ChatAdapter adapter;
    List<ChatMessage> messageList;

    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_chat);

        recyclerView = findViewById(R.id.chatRecycler);
        messageBox = findViewById(R.id.messageInput);
        sendBtn = findViewById(R.id.sendBtn);

        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true); // WhatsApp style
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Real-time listener
        db.collection("Messages")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        messageList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            ChatMessage msg = doc.toObject(ChatMessage.class);
                            messageList.add(msg);
                        }
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });

        sendBtn.setOnClickListener(v -> {
            String text = messageBox.getText().toString().trim();
            if (!text.isEmpty()) {

                Map<String, Object> map = new HashMap<>();
                map.put("text", text);
                map.put("time", System.currentTimeMillis());

                db.collection("Messages")
                        .add(map)
                        .addOnSuccessListener(documentReference -> {
                            messageBox.setText("");
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Send failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }
        });

    }
}
