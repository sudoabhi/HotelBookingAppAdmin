package com.example.hotelloadmin.EventsPackage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hotelloadmin.NewEventPackage.EventTicketsRecyclerData;
import com.example.hotelloadmin.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EditEventTicketsRecyclerViewAdapter extends RecyclerView.Adapter<EditEventTicketsRecyclerViewAdapter.ViewHolder> {

    private List<EventTicketsRecyclerData> eventsList;
    private Context context;
    private LayoutInflater mInflater;
    FirebaseDatabase eventsDatabase;
    DatabaseReference eventsReference;
    ChildEventListener cel;

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextInputEditText ticket_type;

        TextInputEditText ticket_name;
        TextInputEditText ticket_category;
        TextInputEditText ticket_desc;
        TextInputEditText ticket_price;
        TextInputEditText fees_setting;
        Button remove_ticket;



        public ViewHolder(View itemView) {
            super(itemView);
            ticket_type = itemView.findViewById(R.id.ticket_type);
            ticket_name = itemView.findViewById(R.id.ticket_name);
            ticket_category = itemView.findViewById(R.id.ticket_category);
            ticket_desc = itemView.findViewById(R.id.ticket_description);
            ticket_price = itemView.findViewById(R.id.ticket_price);
            fees_setting = itemView.findViewById(R.id.fees_setting);

            remove_ticket=itemView.findViewById(R.id.remove_tick_button);
            ticket_price.setEnabled(false);
            ticket_desc.setEnabled(false);
            ticket_category.setEnabled(false);
            ticket_name.setEnabled(false);
            ticket_type.setEnabled(false);
            fees_setting.setEnabled(false);

        }

    }


    public EditEventTicketsRecyclerViewAdapter(Context context,List<EventTicketsRecyclerData> eventsList){
        this.context=context;
        this.eventsList=eventsList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row_new_event_tickets, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        final EventTicketsRecyclerData data=eventsList.get(position);
        final String key=data.getTicket_key();
        viewHolder.ticket_type.setText(data.getTicket_type());
        viewHolder.ticket_name.setText(data.getTicket_name());
        viewHolder.ticket_category.setText(data.getTicket_cat());
        viewHolder.ticket_desc.setText(data.getTicket_desc());
        viewHolder.ticket_price.setText(data.getTicket_price());
        viewHolder.fees_setting.setText(data.getFees_setting());



        viewHolder.remove_ticket.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent=new Intent("TicketKey");
                intent.putExtra("TicketKey", key);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

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
