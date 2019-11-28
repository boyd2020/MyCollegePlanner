package com.b96software.schoolplannerapp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.interfaces.ItemClickedListener;
import com.b96software.schoolplannerapp.interfaces.NoteListener;
import com.b96software.schoolplannerapp.model.Note;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    //Objects
    private ArrayList<Note> notes;
    private Context context;
    private SimpleDateFormat format;
    private GregorianCalendar cal;
    private ItemClickedListener<Note> callback;
    private NoteListener noteCallback;



    public NoteAdapter(Context context, ArrayList<Note> notes, ItemClickedListener<Note> callback,
                       NoteListener noteCallback) {
        this.context = context;
        this.notes = notes;
        this.callback = callback;
        this.noteCallback = noteCallback;

        format = new SimpleDateFormat(Utils.DISPLAY_DATE);
        cal = new GregorianCalendar();
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_note, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        Note n = notes.get(position);

        holder.noteName.setText(n.getNoteName());
        holder.noteDesc.setText(n.getNoteText());

        try {
            cal.setTimeInMillis(new SimpleDateFormat(Utils.SQL_DATE_FORMAT).parse(n.getNoteDate()).getTime());
            holder.noteDate.setText(context.getString(R.string.note_date, format.format(cal.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(n.getNoteName().isEmpty())
            holder.noteName.setVisibility(View.GONE);
        else
            holder.noteName.setVisibility(View.VISIBLE);


        if(n.getNoteText().isEmpty())
            holder.noteDesc.setVisibility(View.GONE);
        else
            holder.noteDesc.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public void setCursor(ArrayList<Note> n)
    {
        notes.clear();
        notes.addAll(n);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.noteNameTextView)
        TextView noteName;

        @BindView(R.id.noteDateTextView)
        TextView noteDate;

        @BindView(R.id.noteDescTextView)
        TextView noteDesc;

        @BindView(R.id.noteShareImageView)
        ImageView shareNote;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onItemClicked(notes.get(getAdapterPosition()));
                }
            });

            shareNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noteCallback.onShareNoteClicked(notes.get(getAdapterPosition()));
                }
            });


        }
    }
}
