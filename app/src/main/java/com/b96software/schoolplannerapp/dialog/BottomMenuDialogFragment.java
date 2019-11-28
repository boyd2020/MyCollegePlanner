package com.b96software.schoolplannerapp.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.adapter.MenuItemAdapter;
import com.b96software.schoolplannerapp.assignments.AddAssignmentActivity;
import com.b96software.schoolplannerapp.course.AddCourseActivity;
import com.b96software.schoolplannerapp.grades.AddGradeActivity;
import com.b96software.schoolplannerapp.interfaces.MenuItemClickedListener;
import com.b96software.schoolplannerapp.model.MenuItem;
import com.b96software.schoolplannerapp.notes.AddNoteActivity;
import com.b96software.schoolplannerapp.professors.AddProfessorActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BottomMenuDialogFragment extends BottomSheetDialogFragment implements MenuItemClickedListener {

    //Constants
    private static final int MENU_PROFESSOR = 0, MENU_COURSE = 1, MENU_ASSIGNMENT = 2, MENU_EXAM = 3,
    MENU_GRADE = 4, MENU_NOTE = 5;

    @BindView(R.id.menuRecyclerView)
    RecyclerView recyclerView;

    //Objects/Variables
    private Unbinder unbinder;
    private MenuItemAdapter adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_fab_layout, container, false);
        unbinder = ButterKnife.bind(this, v);

        adapter = new MenuItemAdapter(getContext(), getMenuItems(), this);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onMenuItemClicked(int position) {
        Intent intent;

        switch (position)
        {
            case MENU_PROFESSOR:
                startActivity(new Intent(getContext(), AddProfessorActivity.class));
                break;

            case MENU_COURSE:
                startActivity(new Intent(getContext(), AddCourseActivity.class));
                break;

            case MENU_ASSIGNMENT:
            case MENU_EXAM:
                startActivity(new Intent(getContext(), AddAssignmentActivity.class));
                break;

            case MENU_GRADE:
                startActivity(new Intent(getContext(), AddGradeActivity.class));
                break;

            case MENU_NOTE:
                startActivity(new Intent(getContext(), AddNoteActivity.class));
                break;

            default:
                startActivity(new Intent(getContext(), AddAssignmentActivity.class));
                break;
        }

        dismiss();
    }

    //Add MenuItems to adapter
    private ArrayList<MenuItem> getMenuItems()
    {
        int[] icons = new int[]{R.drawable.ic_person_white_48dp, R.drawable.baseline_class_black_48dp,
                R.drawable.ic_assignment_black_48dp, R.drawable.ic_assessment_black_48dp, R.drawable.baseline_star_rate_black_48dp,
                R.drawable.baseline_note_black_48dp};

        String[] names = new String[]{getString(R.string.menu_professor), getString(R.string.menu_courses),
                getString(R.string.menu_assignment), getString(R.string.menu_exam), getString(R.string.menu_grades),
                getString(R.string.menu_notes)};

        ArrayList<MenuItem> items = new ArrayList<>();

        for(int i = 0; i < icons.length; i++)
        {
            MenuItem item = new MenuItem(icons[i], names[i]);
            items.add(item);
        }

        return items;
    }
}
