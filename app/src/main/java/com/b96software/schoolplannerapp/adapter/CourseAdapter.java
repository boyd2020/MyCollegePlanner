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
import com.b96software.schoolplannerapp.model.Course;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> implements AdapterListener<Course> {

    private int layout;
    private Context context;
    private ArrayList<Course> courses;
    private ItemClickedListener<Object> callback;

    public CourseAdapter(Context context, int layout, ArrayList<Course> courses, ItemClickedListener<Object> callback) {
        this.context = context;
        this.layout = layout;
        this.courses = courses;
        this.callback = callback;
    }


    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(context).inflate(layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CourseAdapter.ViewHolder holder, int position) {
        Course c = courses.get(position);

        holder.profName.setText(c.getProfName());
        holder.courseName.setText(c.getCourseName());

        holder.courseName.setTextColor(c.getCourseColor());
        holder.cardView.setStrokeColor(c.getCourseColor());
        holder.courseImage.setColorFilter(c.getCourseColor());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }


    @Override
    public void setCursor(ArrayList<Course> items) {
        courses.clear();
        courses.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public Course getItem(int position) {
        return courses.get(position);
    }

    @Override
    public void setItem(Course item, int position) {
        courses.set(position, item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.courseName)
        TextView courseName;

        @BindView(R.id.profName)
        TextView profName;

        @BindView(R.id.courseImage)
        ImageView courseImage;

        @BindView(R.id.courseCard)
        MaterialCardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onItemClicked(courses.get(getAdapterPosition()));
                }
            });
        }
    }
}
