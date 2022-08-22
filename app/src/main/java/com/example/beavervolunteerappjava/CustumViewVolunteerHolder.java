package com.example.beavervolunteerappjava;
/**
 *
 * @author  Nina Zhang
 * @version 1.0
 * @since   2022/07/
 */
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CustumViewVolunteerHolder extends RecyclerView.ViewHolder {
    public TextView opportunityName, opportunityDescription;
    public CardView cardView;

    public CustumViewVolunteerHolder(@NonNull View itemView) {
        super(itemView);
        opportunityName = itemView.findViewById(R.id.opportunityName);
        opportunityDescription = itemView.findViewById(R.id.opportunityDescription);
        cardView = itemView.findViewById(R.id.main_container);
    }
}
