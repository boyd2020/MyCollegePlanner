package com.b96software.schoolplannerapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.interfaces.GradeAddedListener;
import com.b96software.schoolplannerapp.model.Grade;
import com.b96software.schoolplannerapp.util.BundleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddGradeDialog extends DialogFragment implements View.OnClickListener {

    //Views
    @BindView(R.id.gradeEarned)
    TextView gradeEarnedTextView;

    @BindView(R.id.gradeWorth)
    TextView gradeWorthTextView;


    @BindView(R.id.gradeDialog)
    LinearLayout gradeDialog;

    //Variables/Objects
    private Grade grade;
    private Unbinder unbinder;
    private GradeAddedListener callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (GradeAddedListener) getTargetFragment();
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
        View v = inflater.inflate(R.layout.dialog_grade, container, false);
        unbinder = ButterKnife.bind(this, v);

        grade = getArguments().getParcelable(BundleUtils.BUNDLE_GRADE);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.dialogAdd, R.id.dialogCancel})
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.dialogAdd:
                addGrade();
                dismiss();
                break;

            case R.id.dialogCancel:
                dismiss();
                break;
        }
    }

    private void addGrade()
    {

        if(gradeEarnedTextView.getText().toString().isEmpty())
            Snackbar.make(gradeDialog, getString(R.string.error_grade_earned), Snackbar.LENGTH_LONG).show();
        else if (gradeWorthTextView.getText().toString().isEmpty())
            Snackbar.make(gradeDialog, getString(R.string.error_grade_worth), Snackbar.LENGTH_LONG).show();
        else
        {
            double earned = Double.parseDouble(gradeEarnedTextView.getText().toString());
            double worth = Double.parseDouble(gradeWorthTextView.getText().toString());

            grade.setEarned(earned);
            grade.setWorth(worth);

            callback.onGradeAdded(grade);
        }
    }
}
