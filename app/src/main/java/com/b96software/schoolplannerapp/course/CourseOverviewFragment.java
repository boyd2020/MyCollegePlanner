package com.b96software.schoolplannerapp.course;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.handlers.CourseHandler;
import com.b96software.schoolplannerapp.model.Course;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CourseOverviewFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 100003;


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

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //Variables/ Objects
    private Unbinder unbinder;
    private Course course;
    private CourseHandler handler;
    private SimpleDateFormat timeFormat, dateFormat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.overview_course_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        if(getActivity().findViewById(R.id.contentFrame) == null)
        {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

        if(savedInstanceState == null)
            course = getArguments().getParcelable(BundleUtils.BUNDLE_COURSE);
        else
            course = savedInstanceState.getParcelable(BundleUtils.BUNDLE_COURSE);


        handler = new CourseHandler(getContext());


        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleUtils.BUNDLE_COURSE, course);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return handler.getCourseFromDB(course.getCourseID());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        course = handler.getCourseFromCursor(cursor);

        if(course != null) {

            timeFormat = new SimpleDateFormat(Utils.DATE_TIME);
            dateFormat = new SimpleDateFormat(Utils.SQL_DATETIME_FORMAT);

            profName.setText(course.getProfName());
            courseLocation.setText(course.getLocation());
            courseDays.setText(course.getCourseDays());

            //Toolbar Info
            collapsingToolbar.setTitle(course.getCourseName());
            collapsingToolbar.setBackgroundColor(course.getCourseColor());
            collapsingToolbar.setContentScrimColor(course.getCourseColor());
            toolbar.setBackgroundColor(course.getCourseColor());

            if(getActivity().findViewById(R.id.toolbar) != null)
                ((Toolbar)getActivity().findViewById(R.id.toolbar)).setBackgroundColor(course.getCourseColor());

            try {
                if(course.getCourseStart() != null) {
                    long startTime = dateFormat.parse(course.getCourseStart()).getTime();
                    courseStartTime.setText(timeFormat.format(startTime));

                }

                if(course.getCourseEnd() != null) {
                    long endTime = dateFormat.parse(course.getCourseEnd()).getTime();
                    courseEndTime.setText(timeFormat.format(endTime));
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @OnClick(R.id.fab)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab:
                editCourse();
                break;
        }
    }

    private void editCourse()
    {
        Intent intent = new Intent(getContext(), EditCourseActivity.class);
        intent.putExtra(BundleUtils.BUNDLE_COURSE, course);
        startActivity(intent);
    }
}
