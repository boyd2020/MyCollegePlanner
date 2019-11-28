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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.adapter.EventAdapter;
import com.b96software.schoolplannerapp.adapter.WeeklyAdapter;
import com.b96software.schoolplannerapp.agenda.NoteFragment;
import com.b96software.schoolplannerapp.handlers.EventHandler;
import com.b96software.schoolplannerapp.model.Event;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WeeklyPagerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //Constants
    private static final int WEEK_DAYS = 7;

    //Views
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.bottomSheetLayout)
    RelativeLayout bottomSheetLayout;

    @BindView(R.id.emptyText)
    TextView emptyText;

    //Objects
    private int pagerPosition;
    private Unbinder unbinder;
    private EventHandler handler;
    private SimpleDateFormat format;
    private WeeklyAdapter adapter;
    private ArrayList<Long> dates;
    private ArrayList<EventAdapter> eventAdapters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment_agenda, container, false);
        unbinder = ButterKnife.bind(this, v);

        format = new SimpleDateFormat(Utils.SQL_DATE_FORMAT);
        eventAdapters = new ArrayList<>();
        handler = new EventHandler(getContext());

        pagerPosition = getArguments().getInt(Utils.KEY_LOADER_ID);
        dates = (ArrayList<Long>)getArguments().getSerializable(Utils.KEY_DATES);

        adapter = new WeeklyAdapter(getContext(), dates, eventAdapters);
        recyclerView.setAdapter(adapter);
        emptyText.setVisibility(View.GONE);


        //Iterate through a seven day period
        int i = pagerPosition;

        //Iterate through a seven day period to initialize loaders
        for(; i < pagerPosition + WEEK_DAYS; i++)
        {
            int pos = i % pagerPosition;    //Get Current Position
            eventAdapters.add(pos, new EventAdapter(getContext(), new ArrayList<Event>()));
            getActivity().getSupportLoaderManager().destroyLoader(i);
            getActivity().getSupportLoaderManager().initLoader(i, null, this);
        }

        //Initialize Note Fragment
        addNoteFragment(i);

        //Add BottomSheet
        addBottomSheet();

        return v;
    }

    private void addNoteFragment(int loaderID)
    {
        //Get Start and End Dates
        String startDate = format.format(dates.get(0));
        String endDate = format.format(dates.get(dates.size() - 1));

        //Create Bundle
        Bundle bundle = new Bundle();
        bundle.putString(Utils.KEY_START_DATE, startDate);
        bundle.putString(Utils.KEY_END_DATE, endDate);
        bundle.putInt(Utils.KEY_LOADER_ID, loaderID);

        //Initialize Note Fragment
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(bundle);

        //Add Note Fragment to the frame
        getChildFragmentManager().beginTransaction().replace(R.id.noteFrame, fragment).commit();

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_slide_top);
        recyclerView.setLayoutAnimation(controller);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int pos = id % pagerPosition;
        String date = format.format(dates.get(pos));
        return handler.getEventsByDate(date);
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int pos = loader.getId() % pagerPosition;
        ArrayList<Event> events = handler.getEventsFromCursor(data);
        adapter.setEventCursor(events, pos);
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Log.e("Loader", "Reset");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        if(eventAdapters != null && !eventAdapters.isEmpty())
        {
            int i = pagerPosition;

            for(; i < pagerPosition + WEEK_DAYS; i++)
            {
                getActivity().getSupportLoaderManager().destroyLoader(i);
            }

            getActivity().getSupportLoaderManager().destroyLoader(i);
        }
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
