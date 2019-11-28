package com.b96software.schoolplannerapp.pagerfragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CalendarView;
import android.widget.RelativeLayout;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.adapter.EventAdapter;
import com.b96software.schoolplannerapp.agenda.DailyNoteFragment;
import com.b96software.schoolplannerapp.handlers.EventHandler;
import com.b96software.schoolplannerapp.model.Event;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MonthlyPagerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.calendarView)
    CalendarView calendarView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.bottomSheetLayout)
    RelativeLayout bottomSheetLayout;

    //Objects
    int loaderID;
    long date;
    Unbinder unbinder;
    EventAdapter adapter;
    EventHandler handler;
    SimpleDateFormat format;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.agenda_fragment_month, container,false);
        unbinder = ButterKnife.bind(this, v);

        handler = new EventHandler(getContext());
        adapter = new EventAdapter(getContext(), new ArrayList<Event>());
        format = new SimpleDateFormat(Utils.SQL_DATE_FORMAT);

        date = getArguments().getLong(Utils.KEY_DATE);
        loaderID = getArguments().getInt(Utils.KEY_LOADER_ID);

        recyclerView.setAdapter(adapter);

        getActivity().getSupportLoaderManager().destroyLoader(loaderID);
        getActivity().getSupportLoaderManager().initLoader(loaderID, null, this);

        //Add Note Fragment
        addNoteFragment(loaderID + 1);

        //Add BottomSheet
        addBottomSheet();

        //Add Calendar
        addCalendar();

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_slide_top);
        recyclerView.setLayoutAnimation(controller);

        return v;
    }

    private void addCalendar()
    {
        final Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(date);

        calendarView.setDate(cal.getTimeInMillis());

        //Set Min to first day of the month
        cal.set(Calendar.DAY_OF_MONTH, 1);
        calendarView.setMinDate(cal.getTimeInMillis());


        //Get Max Date
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        calendarView.setMaxDate(cal.getTimeInMillis());


        //Reset calendar time
        cal.setTimeInMillis(date);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                cal.set(year, month, dayOfMonth);
                date = cal.getTimeInMillis();

                getActivity().getSupportLoaderManager().destroyLoader(loaderID);
                getActivity().getSupportLoaderManager().initLoader(loaderID, null, MonthlyPagerFragment.this);
            }
        });
    }

    private void addNoteFragment(int loaderID)
    {
        //Create Bundle for the fragment
        Bundle bundle = new Bundle();
        bundle.putString(Utils.KEY_DATE, format.format(date));
        bundle.putInt(Utils.KEY_LOADER_ID, loaderID);

        //Initialize Note Fragment
        Fragment fragment = new DailyNoteFragment();
        fragment.setArguments(bundle);

        //Add Note Fragment to the frame
        getChildFragmentManager().beginTransaction().replace(R.id.noteFrame, fragment).commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return handler.getEventsByDate(format.format(date));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Event> events = handler.getEventsFromCursor(data);
        adapter.setCursor(events);
        recyclerView.scheduleLayoutAnimation();

        //Update Note Fragment
        addNoteFragment(loaderID + 1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().getSupportLoaderManager().destroyLoader(loaderID);
    }

    private void addBottomSheet()
    {
        if(bottomSheetLayout != null)
        {
            final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
            bottomSheetLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    else
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
        }
    }
}
