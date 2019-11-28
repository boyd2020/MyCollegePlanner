package com.b96software.schoolplannerapp.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.adapter.ProfessorAdapter;
import com.b96software.schoolplannerapp.handlers.ProfessorHandler;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Professor;
import com.b96software.schoolplannerapp.professors.AddProfessorActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddProfessorDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
    ItemClickedListener<Professor>
{

    //Constants
    private static final int LOADER_ID = 2000000;


    @BindView(R.id.dialogTitle)
    TextView title;

    @BindView(R.id.dialogRecyclerView)
    RecyclerView recyclerView;


    //Objects
    private Unbinder unbinder;
    private ProfessorAdapter adapter;
    private ProfessorHandler handler;
    private ItemClickedListener<Professor> callback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = (ItemClickedListener<Professor>) getTargetFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();

        if(d != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_select, container, false);
        unbinder = ButterKnife.bind(this, v);

        //Set Dialog Title
        title.setText(getString(R.string.prof_select));

        //Initialize Adapter and RecyclerView
        adapter = new ProfessorAdapter(getContext(), R.layout.list_item_professor, new ArrayList<Professor>(), this);
        recyclerView.setAdapter(adapter);

        //Initialize Professor Handler
        handler = new ProfessorHandler(getContext());

        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return handler.getAllProfessorsFromDB();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<Professor> professors = handler.getProfessorsFromCursor(cursor);
        adapter.setCursor(professors);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @OnClick({R.id.dialogAdd, R.id.dialogCancel})
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.dialogAdd:
                addProfessor();
                dismiss();
                break;

            case R.id.dialogCancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onItemClicked(Professor item) {
        callback.onItemClicked(item);
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
    }


    private void addProfessor()
    {
        Intent intent = new Intent(getContext(), AddProfessorActivity.class);
        startActivity(intent);
    }
}
