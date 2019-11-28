package com.b96software.schoolplannerapp.widgetadapters;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.b96software.schoolplannerapp.R;

import com.b96software.schoolplannerapp.handlers.NoteHandler;
import com.b96software.schoolplannerapp.managers.PreferenceManager;
import com.b96software.schoolplannerapp.model.Note;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class NoteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private NoteHandler handler;
    private SimpleDateFormat format;
    private ArrayList<Note> notes;
    private PreferenceManager preferenceManager;

    public NoteRemoteViewsFactory(Context context) {
        this.context = context;
        handler = new NoteHandler(context);
        notes = new ArrayList<>();
        format = new SimpleDateFormat(Utils.SQL_DATE_FORMAT);
        preferenceManager = new PreferenceManager(context);
    }



    @Override
    public void onCreate() {
        getNotes();
    }

    @Override
    public void onDataSetChanged() {
        long identityToken = Binder.clearCallingIdentity();
        getNotes();
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if(notes.isEmpty())
            return null;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_note);

        Calendar cal = new GregorianCalendar();
        Note n = notes.get(position);

        views.setTextViewText(R.id.noteNameTextView, n.getNoteName());
        views.setTextViewText(R.id.noteDescTextView, n.getNoteText());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(BundleUtils.BUNDLE_NOTE, n);
        views.setOnClickFillInIntent(R.id.noteCard, fillInIntent);

        try {
            cal.setTimeInMillis(format.parse(n.getNoteDate()).getTime());
            views.setTextViewText(R.id.noteDateTextView, context.getString(R.string.note_date, new SimpleDateFormat(Utils.DISPLAY_DATE).format(cal.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return  notes.get(position).getNoteID();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void getNotes()
    {
        String date = preferenceManager.getNoteDate();

        notes.clear();
        notes.addAll(handler.getNotesByDate(date));
    }
}
