package com.b96software.schoolplannerapp.classdate;

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
import com.b96software.schoolplannerapp.model.ClassDate;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ClassDateOverviewFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1000012;

    @BindView(R.id.courseStartTime)
    TextView courseStartTime;

    @BindView(R.id.courseEndTime)
    TextView courseEndTime;

    @BindView(R.id.courseDate)
    TextView courseDate;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //Variables/ Objects
    private Unbinder unbinder;
    private ClassDate classDate;
    private CourseHandler handler;
    private SimpleDateFormat timeFormat, dateFormat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.overview_class_date, container, false);
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
            classDate = getArguments().getParcelable(BundleUtils.BUNDLE_CLASS_DATE);
        else
            classDate = savedInstanceState.getParcelable(BundleUtils.BUNDLE_CLASS_DATE);


        handler = new CourseHandler(getContext());


        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleUtils.BUNDLE_CLASS_DATE, classDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return handler.getCourseDateFromDB(classDate.getClassID());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        classDate = handler.getClassDateFromCursor(cursor);

        if(classDate != null)
        {
            timeFormat = new SimpleDateFormat(Utils.DATE_TIME);
            dateFormat = new SimpleDateFormat(Utils.SQL_DATETIME_FORMAT);


            //Toolbar Info
            collapsingToolbar.setTitle(classDate.getCourseName());
            collapsingToolbar.setBackgroundColor(classDate.getCourseColor());
            collapsingToolbar.setContentScrimColor(classDate.getCourseColor());
            toolbar.setBackgroundColor(classDate.getCourseColor());

            if(getActivity().findViewById(R.id.toolbar) != null)
                ((Toolbar)getActivity().findViewById(R.id.toolbar)).setBackgroundColor(classDate.getCourseColor());


            try {
                if(classDate.getClassDate() != null) {
                    long date = dateFormat.parse(classDate.getClassDate()).getTime();
                    courseDate.setText(new SimpleDateFormat(Utils.DISPLAY_DATE).format(date));
                }


                if(classDate.getClassStart() != null) {
                    long startTime = dateFormat.parse(classDate.getClassStart()).getTime();
                    courseStartTime.setText(timeFormat.format(startTime));

                }

                if(classDate.getClassEnd() != null) {
                    long endTime = dateFormat.parse(classDate.getClassEnd()).getTime();
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
                editClassDate();
                break;
        }
    }


    private void editClassDate()
    {
        Intent intent = new Intent(getContext(), EditClassDateActivity.class);
        intent.putExtra(BundleUtils.BUNDLE_CLASS_DATE, classDate);
        startActivity(intent);
    }
}
