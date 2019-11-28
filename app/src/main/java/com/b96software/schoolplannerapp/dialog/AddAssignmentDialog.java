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
import com.b96software.schoolplannerapp.adapter.AssignmentAdapter;
import com.b96software.schoolplannerapp.assignments.AddAssignmentActivity;
import com.b96software.schoolplannerapp.handlers.AssignmentHandler;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Assignment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddAssignmentDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
        ItemClickedListener<Object>
{

    //Constants
    private static final int LOADER_ID = 2000000;


    @BindView(R.id.dialogTitle)
    TextView title;

    @BindView(R.id.dialogRecyclerView)
    RecyclerView recyclerView;


    //Objects
    private Unbinder unbinder;
    private AssignmentAdapter adapter;
    private AssignmentHandler handler;
    private ItemClickedListener<Object> callback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = (ItemClickedListener<Object>) getTargetFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();

        int deviceWidth = (int) (getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density);

        if(d != null && deviceWidth < 600)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_select, container, false);
        unbinder = ButterKnife.bind(this, v);

        //Set Dialog Title
        title.setText(getString(R.string.assignment_select));

        //Initialize Adapter and RecyclerView
        adapter = new AssignmentAdapter(getContext(), R.layout.card_assignment, new ArrayList<Assignment>(), this);
        recyclerView.setAdapter(adapter);

        //Initialize Professor Handler
        handler = new AssignmentHandler(getContext());

        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return handler.getAllAssignments();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<Assignment> assignments = handler.getAssignmentsFromCursor(cursor);
        adapter.setCursor(assignments);
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
    public void onItemClicked(Object object) {
        callback.onItemClicked(object);
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
        Intent intent = new Intent(getContext(), AddAssignmentActivity.class);
        startActivity(intent);
    }


}
