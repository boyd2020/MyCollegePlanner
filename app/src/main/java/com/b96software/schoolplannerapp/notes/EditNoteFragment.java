package com.b96software.schoolplannerapp.notes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.handlers.NoteHandler;
import com.b96software.schoolplannerapp.model.Note;
import com.b96software.schoolplannerapp.util.BundleUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class EditNoteFragment extends Fragment implements View.OnClickListener {

    //Constants
    private static final int REQ_CODE_SPEECH_INPUT = 1;


    @BindView(R.id.noteTitle)
    EditText noteTitle;

    @BindView(R.id.noteDesc)
    EditText noteDesc;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //Objects
    private Note note;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_note_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle(getString(R.string.note_edit));

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        if(savedInstanceState == null)
            note = getArguments().getParcelable(BundleUtils.BUNDLE_NOTE);
        else
            note = savedInstanceState.getParcelable(BundleUtils.BUNDLE_NOTE);


        //Add Note Information
        noteTitle.setText(note.getNoteName());
        noteDesc.setText(note.getNoteText());

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        note.setNoteName(noteTitle.getText().toString());
        note.setNoteText(noteDesc.getText().toString());

        outState.putParcelable(BundleUtils.BUNDLE_NOTE, note);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fab, R.id.noteMic})
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab:
                saveNote();
                break;

            case R.id.noteMic:
                askSpeechInput();
                break;
        }
    }

    public void saveNote()
    {
        String title = noteTitle.getText().toString();
        String desc = noteDesc.getText().toString();

        note.setNoteName(title);
        note.setNoteText(desc);

        new NoteHandler(getContext()).updateNote(note);
        getActivity().finish();
    }


    private void askSpeechInput()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.mic_note_desc));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            //Snackbar.make(searchView, getString(R.string.speech_supported), Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQ_CODE_SPEECH_INPUT:

                if(resultCode == RESULT_OK) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    noteDesc.setText(result.get(0));
                }
                break;
        }
    }
}
