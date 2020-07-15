package com.example.hotelloadmin.NewEventPackage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelloadmin.EventsPackage.EventDetailsOrganisersData;
import com.example.hotelloadmin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventOrgainsersRecyclerViewAdapter extends RecyclerView.Adapter<EventOrgainsersRecyclerViewAdapter.ViewHolder> {

    private List<EventDetailsOrganisersData> eventsList;
    private LayoutInflater mInflater;
    Context context;


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView org_name;
        TextView org_role;
        TextView org_email;
        TextView org_phone;
        ImageView org_pic;
        Button remove_ticket;
        LinearLayout lay1;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            org_name = itemView.findViewById(R.id.org_name);
            org_email = itemView.findViewById(R.id.org_email);
            org_phone = itemView.findViewById(R.id.org_phone);
            org_pic = itemView.findViewById(R.id.org_pic);
            org_role = itemView.findViewById(R.id.org_role);
            lay1=itemView.findViewById(R.id.lay1);

            remove_ticket=itemView.findViewById(R.id.remove_tick_button);
            progressBar=itemView.findViewById(R.id.progressBar);
        }

    }


    public EventOrgainsersRecyclerViewAdapter(Context context,List<EventDetailsOrganisersData> eventsList){
        this.eventsList=eventsList;
        this.context=context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row_new_event_organisers, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        EventDetailsOrganisersData data=eventsList.get(position);
        viewHolder.org_name.setText(data.getOrganiserName());
        viewHolder.org_role.setText(data.getOrganiserRole());
        viewHolder.org_email.setText(data.getOrganiserEmail());
        viewHolder.org_phone.setText("+(91)"+data.getOrganiserPhone());

        Picasso.get()
                .load(data.getOrganiserPic())
                .error(android.R.drawable.stat_notify_error)
                //.resize(65,80)
                //.centerCrop()
                .into(viewHolder.org_pic, new Callback() {
                    @Override
                    public void onSuccess() {

                        viewHolder.progressBar.setVisibility(View.GONE);
                        viewHolder.lay1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                        Toast.makeText(context, "Failed to load organisers", Toast.LENGTH_SHORT).show();

                    }


                });




        viewHolder.remove_ticket.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                eventsList.remove(position);  // remove the item from list
                notifyItemRemoved(position);// notify the adapter about the removed item
                notifyItemRangeChanged(position, eventsList.size());

            }
        });

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }




}
