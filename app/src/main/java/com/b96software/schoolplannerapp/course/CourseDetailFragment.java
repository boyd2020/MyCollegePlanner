package com.b96software.schoolplannerapp.course;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.adapter.CourseDetailAdapter;
import com.b96software.schoolplannerapp.handlers.CourseHandler;
import com.b96software.schoolplannerapp.model.Course;
import com.b96software.schoolplannerapp.util.BundleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CourseDetailFragment extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>
{


    private static final int LOADER_ID = 1000045;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;


    private Unbinder unbinder;
    private Course course;
    private CourseHandler handler;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_course_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle(getString(R.string.course_edit));

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

        recyclerView.setAdapter(new CourseDetailAdapter(getContext(), course));

        //Toolbar Info
        collapsingToolbar.setTitle(course.getCourseName());
        collapsingToolbar.setBackgroundColor(course.getCourseColor());
        collapsingToolbar.setContentScrimColor(course.getCourseColor());
        toolbar.setBackgroundColor(course.getCourseColor());

        if(getActivity().findViewById(R.id.toolbar) != null)
            ((Toolbar)getActivity().findViewById(R.id.toolbar)).setBackgroundColor(course.getCourseColor());
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
