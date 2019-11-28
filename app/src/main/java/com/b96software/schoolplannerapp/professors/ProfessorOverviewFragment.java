package com.b96software.schoolplannerapp.professors;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.handlers.ProfessorHandler;
import com.b96software.schoolplannerapp.model.Professor;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfessorOverviewFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 100002;

    @BindView(R.id.profPhone)
    TextView profPhone;

    @BindView(R.id.profEmail)
    TextView profEmail;

    @BindView(R.id.profOffice)
    TextView profOffice;

    @BindView(R.id.profImage)
    ImageView profImage;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    //Objects
    private Professor professor;
    private Unbinder unbinder;
    private ProfessorHandler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.overview_professor_fragment, container, false);
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
            professor = getArguments().getParcelable(BundleUtils.BUNDLE_PROFESSOR);
        else
            professor = savedInstanceState.getParcelable(BundleUtils.BUNDLE_PROFESSOR);

        handler = new ProfessorHandler(getContext());


        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleUtils.BUNDLE_PROFESSOR, professor);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return handler.getProfessorFromDB(professor.getId());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        professor = handler.getProfessorFromCursor(data);

        if(professor != null)
        {
            profEmail.setText(professor.getEmail());
            profPhone.setText(professor.getPhone());
            profOffice.setText(professor.getOffice());

            //Professor Name
            collapsingToolbar.setTitle(professor.getName());

            //Add Image if available
            if(professor.getImage() != null)
                addImage();

            professor.setImage(null);
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
                editProfessor();
                break;
        }
    }


    private void editProfessor()
    {
        Intent intent = new Intent(getContext(), EditProfessorActivity.class);
        intent.putExtra(BundleUtils.BUNDLE_PROFESSOR, professor);
        startActivity(intent);
    }

    private void addImage()
    {
        //Add the image to the imageView
        Bitmap bitmap = ImageUtils.getImage(professor.getImage());
        profImage.setImageBitmap(bitmap);
        profImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}
