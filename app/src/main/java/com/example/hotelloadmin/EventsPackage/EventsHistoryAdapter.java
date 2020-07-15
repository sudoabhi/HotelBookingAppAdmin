package com.example.hotelloadmin.EventsPackage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import com.example.hotelloadmin.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Asus on 2/21/2019.
 */

public class EventsHistoryAdapter extends RecyclerView.Adapter<EventsHistoryAdapter.MyViewHolder> implements Filterable {

    private List<EventsHistoryData> posts;
    private List<EventsHistoryData> postsFiltered;
    private Context context;
    public int sizeFlag=0;
    FirebaseDatabase eventsDatabase;
    DatabaseReference eventsReference;
    FirebaseDatabase adminDatabase;
    DatabaseReference adminReference;
    DatabaseReference event_discussion;
    FirebaseStorage storage;
    StorageReference storage_reference;


    int yearI;
    int monthI;
    int dateI;
    String yearS;
    String dayS;
    String am_pm;
    String todays_time;
    String todays_date;
    String monthS;


    int targetWidth;
    int targetHeight;

    FirebaseUser user;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView parentCardView;

        LinearLayout event_details;
        ImageView EventBanner;
        View gradient;
        TextView EventName;
        LinearLayout analytics;
        LinearLayout edit;
        LinearLayout more;
        LinearLayout discussions_ll;
        TextView discussions_count;

        ProgressBar progressBar;


        public MyViewHolder(View v) {
            super(v);
            parentCardView=(CardView)v.findViewById(R.id.parent_card_view) ;

           // parentCardView.setVisibility(View.INVISIBLE);
            event_details=v.findViewById(R.id.event_lay);
            gradient=v.findViewById(R.id.gradient);
            EventBanner=(ImageView)v.findViewById(R.id.event_banner);
            discussions_count=(TextView) v.findViewById(R.id.discussions_count);

            //  update_date=(TextView)v.findViewById(R.id.update_day);
            EventName=(TextView)v.findViewById(R.id.event_name);
            analytics=v.findViewById(R.id.analytic_lay);
            edit=v.findViewById(R.id.edit_lay);
            more=v.findViewById(R.id.more_lay);
            discussions_ll=v.findViewById(R.id.discussions);
            discussions_count=v.findViewById(R.id.discussions_count);

            progressBar=v.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    public EventsHistoryAdapter(Context context,List<EventsHistoryData> posts) {
        this.context=context;
        this.posts=posts;
        this.postsFiltered=posts;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_events_history,parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        user= FirebaseAuth.getInstance().getCurrentUser();
        adminDatabase = FirebaseDatabase.getInstance();
        event_discussion=FirebaseDatabase.getInstance().getReference().child("EventBlogs");
        adminReference = adminDatabase.getReference().child("HotelLoAdmin");
        adminReference= adminReference.child(user.getUid()).child("Events");

        eventsDatabase = FirebaseDatabase.getInstance();
        eventsReference = eventsDatabase.getReference().child("Hotels");

        storage = FirebaseStorage.getInstance();
        storage_reference = storage.getReference();
        storage_reference=storage_reference.child("admin_app/");


        final EventsHistoryData c = postsFiltered.get(position);

        event_discussion.child(c.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if( dataSnapshot.getChildrenCount()==0){
                   holder.discussions_count.setText("0 ");
               }
               else{
                   long x=dataSnapshot.getChildrenCount()-1;
                   holder.discussions_count.setText(x+"+ ");
               }
                event_discussion.child(c.getKey()).removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        final int width = metrics.widthPixels;
        int height = metrics.heightPixels;



        Picasso.get()
                .load(c.getEventBannerUrl())
                .error(android.R.drawable.stat_notify_error)
                .resize(width,500)
                .centerCrop()

                .into(holder.EventBanner, new Callback() {
                    @Override
                    public void onSuccess() {

                        holder.gradient.setLayoutParams(new FrameLayout.LayoutParams(width,490));
                        holder.EventName.setText(c.getEventName());
                        //   holder.update_date.setText(c.getUpdate_date());
                        holder.parentCardView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                        Toast.makeText(context, "Failed to load new events", Toast.LENGTH_SHORT).show();

                    }


                });

        holder.discussions_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),EventDiscussions.class);
                intent.putExtra("EventKey",c.getKey());
                intent.putExtra("EventName",c.getEventName());
                view.getContext().startActivity(intent);

            }
        });


        holder.event_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),EventDetails.class);
                intent.putExtra("Key",c.getKey());
                intent.putExtra("position",position);
                view.getContext().startActivity(intent);
                //((EventsHistoryActivity)context).finish();
            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final String EventKey=c.getKey();
                PopupMenu popup = new PopupMenu(view.getContext(),holder.edit);
                popup.getMenuInflater().inflate(R.menu.edit_event, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();


                        //noinspection SimplifiableIfStatement
                        if (id == R.id.action_edit) {
                            Intent i=new Intent(view.getContext(),EditEventDetails.class);
                            i.putExtra("EventKey",EventKey);
                            view.getContext().startActivity(i);
                            return true;

                        } else if (id == R.id.action_edit_faq) {

                            Intent i=new Intent(view.getContext(),EditEventFAQs.class);
                            i.putExtra("EventKey",EventKey);
                            view.getContext().startActivity(i);
                            return true;

                        } else if (id == R.id.action_edit_org) {

                            Intent i=new Intent(view.getContext(),EditEventOrganisers.class);
                            i.putExtra("EventKey",EventKey);
                            view.getContext().startActivity(i);
                            return true;

                        } else if (id == R.id.action_edit_ticket) {
                            Intent i=new Intent(view.getContext(),EditEventTickets.class);
                            i.putExtra("EventKey",EventKey);
                            view.getContext().startActivity(i);
                            return true;

                        } else if (id == R.id.action_edit_banner) {
                            Intent i=new Intent(view.getContext(),EditEventBanner.class);
                            i.putExtra("EventKey",EventKey);
                            view.getContext().startActivity(i);
                            return true;
                        }
                        return true;
                    }
                });

                popup.show();



            }
        });


        holder.analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),EventAnalytics.class);
                Log.e("EventKey",""+c.getKey());
                intent.putExtra("Key",c.getKey());
                intent.putExtra("EventName",c.getEventName());
                intent.putExtra("position",position);
                view.getContext().startActivity(intent);
            }
        });






        final String key=c.getKey();
        final PopupMenu popup = new PopupMenu(context, holder.more);
        popup.getMenuInflater().inflate(R.menu.event_more, popup.getMenu());

        final MenuItem item1=popup.getMenu().findItem(R.id.action_ticketing);
        eventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(key).child("Ticketing").getValue(String.class).equals("Enabled")){
                    item1.setTitle("Disable Ticketing");
                }
                else{
                    item1.setTitle("Enable Ticketing");
                }

                eventsReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final MenuItem item2=popup.getMenu().findItem(R.id.action_notification);


        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(final MenuItem item) {

                        int id = item.getItemId();
                        //noinspection SimplifiableIfStatement
                        if (id == R.id.action_delete) {

                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder
                                    (view.getContext(),R.style.AlertDialogTheme);

                            alertDialogBuilder
                                    .setTitle("Sure to delete this event?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            holder.progressBar.setVisibility(View.VISIBLE);
                                            holder.parentCardView.setForeground(new ColorDrawable(Color.parseColor("#60000000")));
                                            final String key=c.getKey();

                                            eventsReference.child(key).child("Tickets").addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                   if(dataSnapshot.hasChild("BookedTickets")){
                                                       holder.parentCardView.setForeground(null);
                                                       holder.progressBar.setVisibility(View.INVISIBLE);
                                                       Toast.makeText(context,"Events with booked tickets cannot be deleted. Try disabling ticket.",Toast.LENGTH_LONG).show();
                                                   }
                                                   else{
                                                       holder.progressBar.setVisibility(View.INVISIBLE);
                                                       holder.parentCardView.setForeground(null);
                                                       adminReference.child(key).removeValue();
                                                       eventsReference.child(key).removeValue();
                                                       postsFiltered.remove(position);
                                                       notifyItemRemoved(position);// notify the adapter about the removed item
                                                       notifyItemRangeChanged(position,postsFiltered.size());
                                                       Toast.makeText(context,"Event deleted",Toast.LENGTH_LONG).show();
                                                   }

                                                    eventsReference.child(key).child("Tickets").removeEventListener(this);

                                                }

                                                @Override
                                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                }

                                                @Override
                                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                                }

                                                @Override
                                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });




                                        }
                                    })
                                    .setNegativeButton(Html.fromHtml("<font color='#3BB0E1'>No</font>"),null);

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                            return true;

                        } else if (id == R.id.action_ticketing) {

                            final String key=c.getKey();

                            final MenuItem item1=popup.getMenu().findItem(R.id.action_ticketing);
                            eventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.child(key).child("Ticketing").getValue(String.class).equals("Enabled")){

                                        item1.setTitle("Disable Ticketing");
                                        adminReference.child(key).child("Ticketing").setValue("Disabled");
                                        eventsReference.child(key).child("Ticketing").setValue("Disabled");
                                        Toast.makeText(context,"Ticketing Disabled. Henceforth no further tickets can be booked for this event",Toast.LENGTH_LONG).show();

                                    }
                                    else{
                                        item1.setTitle("Enable Ticketing");
                                        adminReference.child(key).child("Ticketing").setValue("Enabled");
                                        eventsReference.child(key).child("Ticketing").setValue("Enabled");
                                        Toast.makeText(context,"Ticketing Enabled",Toast.LENGTH_LONG).show();
                                    }
                                    eventsReference.removeEventListener(this);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                            return true;
                        }
                        else if(id == R.id.action_notification){


                            return true;
                        }

                        return true;
                    }
                });

                popup.show();




            }
        });




    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return postsFiltered.size();
    }




    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    postsFiltered = posts;
                }
                else {
                    List<EventsHistoryData> filteredList = new ArrayList<>();
                    for (EventsHistoryData row : posts) {

                        if (row.getEventName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }

                    }
                    postsFiltered = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = postsFiltered;
                filterResults.count=postsFiltered.size();


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                postsFiltered = (ArrayList<EventsHistoryData>)filterResults.values;
                if(filterResults.count==0){
                    sizeFlag=0;
                }else{
                    sizeFlag=1;
                }
                filteredCount(sizeFlag);
                Log.e("SizeF1",""+sizeFlag);
                notifyDataSetChanged();
            }

        };
    }

    public void filteredCount(int flag){

        Intent intent=new Intent("Flag");
        intent.putExtra("FlagValue", flag);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        Log.e("SizeF2",""+flag);

    }


    private void getTodaysTime() {

        Calendar calendar = Calendar.getInstance();


        int hourS = calendar.get(Calendar.HOUR_OF_DAY);
        yearI=calendar.get(Calendar.YEAR);

        yearS = Integer.toString(calendar.get(Calendar.YEAR));
        int monthofYear = calendar.get(Calendar.MONTH);
        monthI=monthofYear+1;
        dayS = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        dateI=calendar.get(Calendar.DAY_OF_MONTH);
        int minuteS = calendar.get(Calendar.MINUTE);
        //    Toast.makeText(getActivity(),hourS+minutesS,Toast.LENGTH_SHORT).show();

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourS);
        datetime.set(Calendar.MINUTE, minuteS);

        String minute;
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        if (datetime.get(Calendar.MINUTE) < 10) {

            minute = "0" + datetime.get(Calendar.MINUTE);
        } else minute = "" + datetime.get(Calendar.MINUTE);

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
        todays_time = strHrsToShow + ":" + minute + " " + am_pm;

        switch (monthofYear + 1) {
            case 1:
                monthS = "Jan";
                break;
            case 2:
                monthS = "Feb";
                break;
            case 3:
                monthS = "March";
                break;
            case 4:
                monthS = "April";
                break;
            case 5:
                monthS = "May";
                break;
            case 6:
                monthS = "June";
                break;
            case 7:
                monthS = "July";
                break;
            case 8:
                monthS = "August";
                break;
            case 9:
                monthS = "Sept";
                break;
            case 10:
                monthS = "Oct";
                break;
            case 11:
                monthS = "Nov";
                break;
            case 12:
                monthS = "Dec";
                break;
        }

        todays_date = "" + dayS + " " + monthS + ", " + yearS;


    }


}

