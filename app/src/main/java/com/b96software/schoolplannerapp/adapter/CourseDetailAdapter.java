package com.b96software.schoolplannerapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.assignments.AssignmentOverviewActivity;
import com.b96software.schoolplannerapp.grades.GradeOverviewActivity;
import com.b96software.schoolplannerapp.handlers.AssignmentHandler;
import com.b96software.schoolplannerapp.handlers.CourseHandler;
import com.b96software.schoolplannerapp.handlers.GradeHandler;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Assignment;
import com.b96software.schoolplannerapp.model.Course;
import com.b96software.schoolplannerapp.model.Grade;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickedListener<Object>
{

    private static final int COUNT = 4, COURSE_OVERVIEW = 0, COURSE_GRADES = 1, COURSE_ASSIGN = 2,
    COURSE_EXAMS = 3;

    private Course course;
    private Context context;

    public CourseDetailAdapter(Context context, Course course) {
        this.course = course;
        this.context = context;
        this.course.setCourseDays(new CourseHandler(context).getCourseDays(course.getCourseID()));
    }

    @Override
    public int getItemViewType(int position) {
        switch (position)
        {
            case 0:
                return COURSE_OVERVIEW;

            case 1:
                return COURSE_GRADES;

            case 2:
                return COURSE_ASSIGN;

            case 3:
                return COURSE_EXAMS;

            default:
                return COURSE_OVERVIEW;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        switch (viewType)
        {
            case COURSE_OVERVIEW:
                v = LayoutInflater.from(context).inflate(R.layout.card_course_overview, viewGroup, false);
                return new OverviewHolder(v);

            case COURSE_ASSIGN:
            case COURSE_EXAMS:
            case COURSE_GRADES:
                v = LayoutInflater.from(context).inflate(R.layout.card_course_info, viewGroup, false);
                return new InfoHolder(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType())
        {
            case COURSE_OVERVIEW:
                addOverviewCard((OverviewHolder)viewHolder);
                break;

            case COURSE_ASSIGN:
                addAssignCard((InfoHolder) viewHolder);
                break;

            case COURSE_EXAMS:
                addExamCard((InfoHolder) viewHolder);
                break;

            case COURSE_GRADES:
                addGradeCard((InfoHolder)viewHolder);
               break;
        }
    }

    @Override
    public int getItemCount() {
        return COUNT;
    }


    private void addAssignCard(InfoHolder holder)
    {
        holder.emptyText.setText(context.getString(R.string.assignment_empty));
        holder.infoTitle.setText(context.getString(R.string.menu_assignment));
        holder.recyclerView.setAdapter(new AssignmentAdapter(context, R.layout.card_assignment,
            new AssignmentHandler(context).getCourseAssignmentByType(Utils.ASSIGN_TYPE_HOMEWORK, course.getCourseID()), this));

        if(holder.recyclerView.getAdapter().getItemCount() == 0)
            holder.emptyText.setVisibility(View.VISIBLE);
        else
            holder.emptyText.setVisibility(View.GONE);
    }

    private void addExamCard(InfoHolder holder)
    {
        holder.emptyText.setText(context.getString(R.string.exam_empty));
        holder.infoTitle.setText(context.getString(R.string.menu_exam));
        holder.recyclerView.setAdapter(new AssignmentAdapter(context, R.layout.card_assignment,
                new AssignmentHandler(context).getCourseAssignmentByType(Utils.ASSIGN_TYPE_EXAM, course.getCourseID()), this));

        if(holder.recyclerView.getAdapter().getItemCount() == 0)
            holder.emptyText.setVisibility(View.VISIBLE);
        else
            holder.emptyText.setVisibility(View.GONE);


    }


    private void addGradeCard(InfoHolder holder)
    {
        holder.emptyText.setText(context.getString(R.string.grade_empty));
        holder.infoTitle.setText(context.getString(R.string.menu_grades));
        holder.recyclerView.setAdapter(new GradeAdapter(context, R.layout.card_grade,
                new GradeHandler(context).getCourseGrades(course.getCourseID()), this));

        if(holder.recyclerView.getAdapter().getItemCount() == 0)
            holder.emptyText.setVisibility(View.VISIBLE);
        else
            holder.emptyText.setVisibility(View.GONE);
    }


    private void addOverviewCard(OverviewHolder holder)
    {
        //Add Course Information
        holder.courseLocation.setText(course.getLocation());
        holder.profName.setText(course.getProfName());
        holder.courseDays.setText(course.getCourseDays());


        SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.SQL_DATETIME_FORMAT);
        SimpleDateFormat timeFormat = new SimpleDateFormat(Utils.DATE_TIME);

        try {
            if(course.getCourseStart() != null) {
                long startTime = dateFormat.parse(course.getCourseStart()).getTime();
                holder.courseStartTime.setText(timeFormat.format(startTime));
            }

            if(course.getCourseEnd() != null) {
                long endTime = dateFormat.parse(course.getCourseEnd()).getTime();
                holder.courseEndTime.setText(timeFormat.format(endTime));
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(Object item) {
        Intent intent;

        if(item instanceof Assignment) {
            intent = new Intent(context, AssignmentOverviewActivity.class);
            intent.putExtra(BundleUtils.BUNDLE_ASSIGNMENT, (Assignment)item);
            context.startActivity(intent);
        }
        else if (item instanceof Grade)
        {
            intent = new Intent(context, GradeOverviewActivity.class);
            intent.putExtra(BundleUtils.BUNDLE_GRADE, (Grade)item);
            context.startActivity(intent);
        }
    }

    public class InfoHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.infoTitle)
        TextView infoTitle;

        @BindView(R.id.emptyText)
        TextView emptyText;

        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        public InfoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public class OverviewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.profName)
        TextView profName;

        @BindView(R.id.courseLocation)
        TextView courseLocation;

        @BindView(R.id.courseDays)
        TextView courseDays;

        @BindView(R.id.courseStartTime)
        TextView courseStartTime;

        @BindView(R.id.courseEndTime)
        TextView courseEndTime;

        public OverviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
