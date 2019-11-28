package com.b96software.schoolplannerapp.adapter;

import android.content.Context;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.interfaces.AdapterListener;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Assignment;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> implements AdapterListener<Assignment> {

    private int layout;
    private Context context;
    private Calendar cal;
    private SimpleDateFormat format;
    private ArrayList<Assignment> assignments;
    private ItemClickedListener<Object> callback;


    public AssignmentAdapter(Context context, int layout, ArrayList<Assignment> assignments, ItemClickedListener<Object> callback) {
        this.context = context;
        this.layout = layout;
        this.assignments = assignments;
        this.callback = callback;
        format = new SimpleDateFormat(Utils.DATE_DAY);
        cal = new GregorianCalendar();
    }

    @Override
    public AssignmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AssignmentAdapter.ViewHolder holder, int position) {
        Assignment assignment = assignments.get(position);

        holder.assignName.setText(assignment.getName());


        if(assignment.getCourseID() == 0) {
            holder.assignCourse.setVisibility(View.VISIBLE);
            holder.assignCourse.setText(assignment.getCourseName());
            holder.assignCourse.setTextColor(assignment.getCourseColor());
        }
        else
            holder.assignCourse.setVisibility(View.GONE);

        holder.assignImage.setColorFilter(assignment.getCourseColor());

        try {
            long dueDate = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).parse(assignment.getDate()).getTime();
            cal.setTimeInMillis(dueDate);

            //Set Date
            holder.assignDueDate.setText(format.format(dueDate));



        } catch (ParseException e) {
            e.printStackTrace();
        }


        //Get Assignment progress
        if (assignment.getAssignProgress() == Utils.ASSIGN_TYPE_NOT_DONE && System.currentTimeMillis() < cal.getTimeInMillis()){
            holder.assignCard.setStrokeColor(context.getResources().getColor(R.color.yellow));
            holder.assignDueDate.setTextColor(context.getResources().getColor(R.color.yellow));
        }
        else if(System.currentTimeMillis() > cal.getTimeInMillis() && assignment.getAssignProgress() != Utils.ASSIGN_TYPE_COMPLETED) {
            holder.assignCard.setStrokeColor(context.getResources().getColor(R.color.red));
            holder.assignDueDate.setTextColor(context.getResources().getColor(R.color.red));
        }
        else {
            holder.assignCard.setStrokeColor(context.getResources().getColor(R.color.colorPrimary));
            holder.assignDueDate.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }


    @Override
    public void setCursor(ArrayList<Assignment> items) {
        assignments.clear();
        assignments.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public Assignment getItem(int position) {
        return assignments.get(position);
    }

    @Override
    public void setItem(Assignment item, int position) {
        assignments.set(position, item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.assignName)
        TextView assignName;

        @BindView(R.id.assignCourse)
        TextView assignCourse;

        @BindView(R.id.assignImage)
        ImageView assignImage;

        @BindView(R.id.assignDueDate)
        TextView assignDueDate;

        @BindView(R.id.assignCard)
        MaterialCardView assignCard;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onItemClicked(assignments.get(getAdapterPosition()));
                }
            });
        }
    }
}
