package com.b96software.schoolplannerapp.agenda;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.adapter.NoteAdapter;
import com.b96software.schoolplannerapp.handlers.NoteHandler;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.interfaces.NoteListener;
import com.b96software.schoolplannerapp.model.Note;
import com.b96software.schoolplannerapp.notes.NoteOverviewActivity;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DailyNoteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemClickedListener<Note>,
        NoteListener
{

    //Views
    @BindView(R.id.noteRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyText)
    TextView emptyText;

    //Objects
    private int loaderID;
    private String date;
    private Unbinder unbinder;
    private NoteAdapter adapter;
    private NoteHandler handler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.note_fragment, container, false);

        unbinder = ButterKnife.bind(this, v);

        handler = new NoteHandler(getContext());
        adapter = new NoteAdapter(getContext(), new ArrayList<Note>(), this, this);
        recyclerView.setAdapter(adapter);
        emptyText.setText(getString(R.string.note_empty));


        loaderID = getArguments().getInt(Utils.KEY_LOADER_ID);
        date = getArguments().getString(Utils.KEY_DATE);

        getActivity().getSupportLoaderManager().destroyLoader(loaderID);
        getActivity().getSupportLoaderManager().initLoader(loaderID, null, this);

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_slide_top);
        recyclerView.setLayoutAnimation(controller);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return handler.getNotesByDateFromDB(date);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Note> notes = handler.getNotesFromCursor(data);
        adapter.setCursor(notes);
        recyclerView.scheduleLayoutAnimation();


        if(data.getCount() == 0)
            emptyText.setVisibility(View.VISIBLE);
        else
            emptyText.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(new ArrayList<Note>());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().getSupportLoaderManager().destroyLoader(loaderID);
    }

    @Override
    public void onItemClicked(Note note) {
        Intent intent = new Intent(getContext(), NoteOverviewActivity.class);
        intent.putExtra(BundleUtils.BUNDLE_NOTE, note);
        startActivity(intent);
    }

    @Override
    public void onShareNoteClicked(Note n) {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText(n.getNoteText())
                .setSubject(n.getNoteName())
                .getIntent(), getString(R.string.action_share)));
    }
}
