package com.b96software.schoolplannerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.interfaces.AdapterListener;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.model.Professor;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorAdapter.ViewHolder> implements AdapterListener<Professor> {

    private int layout;
    private Context context;
    private ArrayList<Professor> professors;
    private ItemClickedListener<Professor> callback;

    public ProfessorAdapter(Context context, int layout, ArrayList<Professor> professors, ItemClickedListener<Professor> callback) {
        this.context = context;
        this.layout = layout;
        this.professors = professors;
        this.callback = callback;
    }

    @Override
    public ProfessorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProfessorAdapter.ViewHolder holder, int position) {
        Professor professor = professors.get(position);

        holder.name.setText(professor.getName());
        holder.email.setText(professor.getEmail());

        if(holder.email.getText().toString().isEmpty())
            holder.email.setVisibility(View.GONE);
        else
            holder.email.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return professors.size();
    }


    @Override
    public void setCursor(ArrayList<Professor> items) {
        professors.clear();
        professors.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public Professor getItem(int position) {
        return professors.get(position);
    }

    @Override
    public void setItem(Professor item, int position) {
        professors.set(position, item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.contactImage)
        ImageView image;

        @BindView(R.id.contactName)
        TextView name;

        @BindView(R.id.contactEmail)
        TextView email;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Professor professor = professors.get(getAdapterPosition());
                    callback.onItemClicked(professor);
                }
            });
        }
    }
}
