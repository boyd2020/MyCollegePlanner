package com.b96software.schoolplannerapp.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.b96software.schoolplannerapp.widgetadapters.EventRemoteViewsFactory;

public class EventWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new EventRemoteViewsFactory(getApplicationContext());
    }
}
