package com.b96software.schoolplannerapp.notes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.handlers.NoteHandler;
import com.b96software.schoolplannerapp.model.Note;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NoteOverviewFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1000001;

    @BindView(R.id.noteCreated)
    TextView noteCreated;

    @BindView(R.id.noteDesc)
    TextView noteDesc;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //Object
    private Note note;
    private Unbinder unbinder;
    private NoteHandler handler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.overview_note_fragment, container, false);
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

        fab.setImageResource(R.drawable.ic_create_white_48dp);


        if(savedInstanceState == null)
            note = getArguments().getParcelable(BundleUtils.BUNDLE_NOTE);
        else
            note = savedInstanceState.getParcelable(BundleUtils.BUNDLE_NOTE);

        handler = new NoteHandler(getContext());

        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleUtils.BUNDLE_NOTE, note);
    }

    @OnClick(R.id.fab)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab:
                editNote();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return handler.getNoteFromDB(note.getNoteID());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        note = handler.getNoteFromCursor(cursor);

        if(note != null)
        {
            collapsingToolbar.setTitle(note.getNoteName());
            noteDesc.setText(note.getNoteText());

            //Retrieve and display the note date
            try {
                long date = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).parse(note.getNoteDate()).getTime();
                noteCreated.setText(new SimpleDateFormat(Utils.DISPLAY_DATE).format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void editNote()
    {
        Intent intent = new Intent(getContext(), EditNoteActivity.class);
        intent.putExtra(BundleUtils.BUNDLE_NOTE, note);
        startActivity(intent);
    }
}
