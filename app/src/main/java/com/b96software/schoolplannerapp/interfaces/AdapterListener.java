package com.b96software.schoolplannerapp.interfaces;

import java.util.ArrayList;

public interface AdapterListener<T> {

    void setCursor(ArrayList<T> items);
    T getItem(int position);
    void setItem(T item, int position);

}
