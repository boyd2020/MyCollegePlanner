package com.b96software.schoolplannerapp.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.interfaces.AdapterListener;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Grade;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> implements AdapterListener<Grade> {

    private int layout;
    private Context context;
    private SimpleDateFormat format;
    private ArrayList<Grade> grades;
    private ItemClickedListener<Object> callback;


    public GradeAdapter(Context context, int layout, ArrayList<Grade> grades, ItemClickedListener<Object> callback) {
        this.context = context;
        this.layout = layout;
        this.grades = grades;
        this.callback = callback;
        format = new SimpleDateFormat(Utils.DATE_DAY);
    }

    @Override
    public GradeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(context).inflate(layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GradeAdapter.ViewHolder holder, int position) {
        Grade g = grades.get(position);

        holder.gradeEarned.setText(String.valueOf(g.getEarned()));
        holder.assignName.setText(String.valueOf(g.getAssignmentName()));
        holder.courseName.setText(g.getCourseName());


        holder.courseName.setTextColor(g.getCourseColor());
        holder.gradeDate.setTextColor(g.getCourseColor());

        //Set Grade Earned Text Color
        Drawable circle = context.getResources().getDrawable(R.drawable.circle);
        circle.setColorFilter(g.getCourseColor(), PorterDuff.Mode.SRC_IN);

        holder.gradeEarned.setBackground(circle);
        holder.gradeCard.setStrokeColor(g.getCourseColor());



        try {
            if(g.getDate() != null) {
                long dueDate = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).parse(g.getDate()).getTime();
                holder.gradeDate.setText(format.format(dueDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    @Override
    public void setCursor(ArrayList<Grade> items) {
        grades.clear();
        grades.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public Grade getItem(int position) {
        return grades.get(position);
    }

    @Override
    public void setItem(Grade item, int position) {
        grades.set(position, item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.gradeEarned)
        TextView gradeEarned;

        @BindView(R.id.courseName)
        TextView courseName;

        @BindView(R.id.assignName)
        TextView assignName;

        @BindView(R.id.gradeDate)
        TextView gradeDate;

        @BindView(R.id.gradeCard)
        MaterialCardView gradeCard;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onItemClicked(grades.get(getAdapterPosition()));
                }
            });
        }
    }
}
