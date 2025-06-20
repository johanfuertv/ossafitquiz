package com.example.fitquiz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitquiz.R;
import com.example.fitquiz.models.Achievement;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {
    
    private List<Achievement> achievements;
    private int lastPosition = -1;
    
    public AchievementAdapter(List<Achievement> achievements) {
        this.achievements = achievements;
    }
    
    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);
        
        holder.titleText.setText(achievement.getTitle());
        holder.descriptionText.setText(achievement.getDescription());
        holder.progressText.setText(achievement.getProgress());
        holder.iconImage.setImageResource(achievement.getIconResource());
        
        // Cambiar apariencia según si está desbloqueado o no
        if (achievement.isUnlocked()) {
            holder.itemView.setAlpha(1.0f);
            holder.lockIcon.setVisibility(View.GONE);
        } else {
            holder.itemView.setAlpha(0.6f);
            holder.lockIcon.setVisibility(View.VISIBLE);
        }
        
        // Animar la entrada de los elementos
        setAnimation(holder.itemView, position);
    }
    
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    
    @Override
    public int getItemCount() {
        return achievements.size();
    }
    
    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView descriptionText;
        TextView progressText;
        ImageView iconImage;
        ImageView lockIcon;
        
        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.achievement_title);
            descriptionText = itemView.findViewById(R.id.achievement_description);
            progressText = itemView.findViewById(R.id.achievement_progress);
            iconImage = itemView.findViewById(R.id.achievement_icon);
            lockIcon = itemView.findViewById(R.id.lock_icon);
        }
    }
}
