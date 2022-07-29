package com.example.beavervolunteerappjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustumViewVolunteerHolder> {
    private Context context;
    private VolunteerOpportunity[] listOfOpportunity;
    private SelectListener listener;

    public CustomAdapter(Context context, VolunteerOpportunity[] listOfOpportunity, SelectListener listener) {
        this.context = context;
        this.listOfOpportunity = listOfOpportunity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustumViewVolunteerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustumViewVolunteerHolder
                (LayoutInflater.from(context).inflate(R.layout.single_opportunity, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustumViewVolunteerHolder holder, int position) {

        holder.opportunityName.setText(listOfOpportunity[position].getOpportunityName());
        holder.opportunityDescription.setText(listOfOpportunity[position].getShortDescription());
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                listener.onItemClicked(listOfOpportunity[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfOpportunity.length;
    }
}
