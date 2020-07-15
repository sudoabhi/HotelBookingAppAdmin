package com.example.hotelloadmin.CommonPackage;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hotelloadmin.R;

import java.util.List;

public class AppFAQsRecyclerViewAdapter extends RecyclerView.Adapter<AppFAQsRecyclerViewAdapter.ViewHolder> {

    private List<AppFAQsRecyclerData> faqList;
    private LayoutInflater mInflater;


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView question;
        TextView answer;
        LinearLayout expand;
        LinearLayout outer;
        ImageView expandiv;


        public ViewHolder(View itemView) {
            super(itemView);
            question=itemView.findViewById(R.id.ques);
            answer=itemView.findViewById(R.id.ans);
            expand=itemView.findViewById(R.id.expand);
            expandiv=itemView.findViewById(R.id.expandiv);
            outer=itemView.findViewById(R.id.outer);
            answer.setVisibility(View.GONE);
            question.setTypeface(Typeface.DEFAULT_BOLD);

        }

    }


    public AppFAQsRecyclerViewAdapter(List<AppFAQsRecyclerData> faqList){
        this.faqList=faqList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row_app_faqs, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        AppFAQsRecyclerData data=faqList.get(position);
        viewHolder.question.setText(data.getQuestion());
        viewHolder.answer.setText(data.getAnswer());


        viewHolder.expand.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(viewHolder.answer.getVisibility()==View.GONE) {
                    viewHolder.answer.setVisibility(View.VISIBLE);
                    viewHolder.expandiv.setBackgroundResource(R.drawable.ic_collapse);
                }else {
                    viewHolder.answer.setVisibility(View.GONE);
                    viewHolder.expandiv.setBackgroundResource(R.drawable.ic_expand);
                }

            }
        });

        viewHolder.outer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(viewHolder.answer.getVisibility()==View.GONE) {
                    viewHolder.answer.setVisibility(View.VISIBLE);
                    viewHolder.expandiv.setBackgroundResource(R.drawable.ic_collapse);
                }else {
                    viewHolder.answer.setVisibility(View.GONE);
                    viewHolder.expandiv.setBackgroundResource(R.drawable.ic_expand);
                }

            }
        });

        viewHolder.expandiv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(viewHolder.answer.getVisibility()==View.GONE) {
                    viewHolder.answer.setVisibility(View.VISIBLE);
                    viewHolder.expandiv.setBackgroundResource(R.drawable.ic_collapse);
                }else {
                    viewHolder.answer.setVisibility(View.GONE);
                    viewHolder.expandiv.setBackgroundResource(R.drawable.ic_expand);
                }

            }
        });


        viewHolder.question.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(viewHolder.answer.getVisibility()==View.GONE) {
                    viewHolder.answer.setVisibility(View.VISIBLE);
                    viewHolder.expandiv.setBackgroundResource(R.drawable.ic_collapse);
                }else {
                    viewHolder.answer.setVisibility(View.GONE);
                    viewHolder.expandiv.setBackgroundResource(R.drawable.ic_expand);
                }

            }
        });

        if(viewHolder.answer.getVisibility()==View.VISIBLE){
            viewHolder.answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.answer.setVisibility(View.GONE);
                    viewHolder.expandiv.setBackgroundResource(R.drawable.ic_expand);
                }
            });

            viewHolder.outer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.answer.setVisibility(View.GONE);
                    viewHolder.expandiv.setBackgroundResource(R.drawable.ic_expand);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }




}
