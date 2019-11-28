package com.b96software.schoolplannerapp.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.b96software.schoolplannerapp.widgetadapters.NoteRemoteViewsFactory;

public class NoteWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NoteRemoteViewsFactory(getApplicationContext());
    }
}
