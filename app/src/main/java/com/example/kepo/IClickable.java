package com.example.kepo;

import android.view.View;

import com.example.kepo.modal.Todolist;

public interface IClickable {
    void onTimeClick(Todolist todolist);

    void checkBoxClick(Todolist todolist, View view, int position);


}
