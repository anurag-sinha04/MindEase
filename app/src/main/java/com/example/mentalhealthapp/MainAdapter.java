package com.example.mentalhealthapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealthapp.model.Cards;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    Context context;
    List<Cards> cardsList;

    int[] cardColors = {
            Color.parseColor("#DCEEFF"), // Stress Test
            Color.parseColor("#FADADD"), // News
            Color.parseColor("#E6D9FF"), // Memes
            Color.parseColor("#FFF1C1"), // Journal
            Color.parseColor("#DFF5EA"), // ToDo
            Color.parseColor("#FFE6D5"), // Statistics
            Color.parseColor("#E0F2F1")  // Anonymous Talk (new)
    };



    String[] subtitles = {
            "Check your stress level",
            "Latest mental wellness updates",
            "Light content to relax your mind",
            "Write and reflect privately",
            "Small tasks for calmness",
            "View stress trends",
            "Talk freely without identity"   // new
    };


    public MainAdapter(Context context, List<Cards> cardsList) {
        this.context = context;
        this.cardsList = cardsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.profileImage.setImageResource(cardsList.get(position).getImageProfile());
        holder.cardtext.setText(cardsList.get(position).getCardtext());
        holder.subtext.setText(subtitles[position]);

        holder.cardView.setCardBackgroundColor(cardColors[position]);

        holder.itemView.setOnClickListener(view -> {
            holder.itemView.animate()
                    .scaleX(0.97f).scaleY(0.97f)
                    .setDuration(80)
                    .withEndAction(() -> holder.itemView.animate()
                            .scaleX(1f).scaleY(1f)
                            .setDuration(80))
                    .start();

            Intent intent = null;
            switch (position) {
                case 0: intent = new Intent(context, QuizActivity.class); break;
                case 1: intent = new Intent(context, NewsActivity.class); break;
                case 2: intent = new Intent(context, MemeActivity.class); break;
                case 3: intent = new Intent(context, JournalActivity.class); break;
                case 4: intent = new Intent(context, ToDoListActivity.class); break;
                case 5: intent = new Intent(context, QuizStatistics.class); break;
                case 6: intent = new Intent(context, AnonymousChat.class); break; // NEW
            }
            if (intent != null) context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView cardtext, subtext;
        ConstraintLayout constraintLayout;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            cardtext = itemView.findViewById(R.id.cardtext);
            subtext = itemView.findViewById(R.id.subtext);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            cardView = itemView.findViewById(R.id.cardViewRoot);
        }
    }
}
