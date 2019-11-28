package com.b96software.schoolplannerapp.professors;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class EditProfessorFragment extends Fragment implements View.OnClickListener {

    //Constants
    private static final int IMAGE_REQUEST = 1;

    @BindView(R.id.profName)
    TextView profName;

    @BindView(R.id.profPhone)
    TextView profPhone;

    @BindView(R.id.profEmail)
    TextView profEmail;

    @BindView(R.id.profOffice)
    TextView profOffice;

    @BindView(R.id.profImage)
    ImageView profImage;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //Objects/Variables
    private byte[] image;
    private Professor professor;
    private Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_professor_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle(getString(R.string.prof_edit));

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        if(savedInstanceState == null) {
            professor = getArguments().getParcelable(BundleUtils.BUNDLE_PROFESSOR);
            image = new ProfessorHandler(getContext()).getProfessorImage(professor.getId());
        }
        else {
            professor = savedInstanceState.getParcelable(BundleUtils.BUNDLE_PROFESSOR);
            image = savedInstanceState.getByteArray(BundleUtils.BUNDLE_PROF_IMAGE);
        }


        //Add Professor Information
        profName.setText(professor.getName());
        profEmail.setText(professor.getEmail());
        profOffice.setText(professor.getOffice());
        profPhone.setText(professor.getPhone());


        //Add Image if available
        if(image != null)
            addImage();

        return v;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        professor.setName(profName.getText().toString());
        professor.setEmail(profEmail.getText().toString());
        professor.setPhone(profPhone.getText().toString());
        professor.setOffice(profOffice.getText().toString());

        outState.putByteArray(BundleUtils.BUNDLE_PROF_IMAGE, image);
        outState.putParcelable(BundleUtils.BUNDLE_PROFESSOR, professor);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fab, R.id.profImage})
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab:
                addProfessor();
                break;

            case R.id.profImage:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), IMAGE_REQUEST);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri imageUri = data.getData();

            try {
                //Get Bytes from Image Stream
                image = ImageUtils.getImageData(getContext(), imageUri);
                addImage();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void addProfessor()
    {
        if(profName.getText().toString().isEmpty())
            Snackbar.make(getActivity().findViewById(R.id.frameLayout), getString(R.string.error_prof_name), Snackbar.LENGTH_LONG).show();
        else
        {
            //Get Professor info from layout
            String name = profName.getText().toString();
            String phone = profPhone.getText().toString();
            String email = profEmail.getText().toString();
            String office = profOffice.getText().toString();

            professor.setName(name);
            professor.setPhone(phone);
            professor.setEmail(email);
            professor.setOffice(office);
            professor.setImage(image);

            new ProfessorHandler(getContext()).updateProfessor(professor);
            getActivity().finish();
        }
    }

    private void addImage()
    {
        //Add the image to the imageView
        Bitmap bitmap = ImageUtils.getImage(image);
        profImage.setImageBitmap(bitmap);
        profImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

}
