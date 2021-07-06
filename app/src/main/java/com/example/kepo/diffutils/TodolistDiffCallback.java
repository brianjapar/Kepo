package com.example.kepo.diffutils;

import androidx.recyclerview.widget.DiffUtil;

import com.example.kepo.modal.Todolist;

import java.util.List;

public class TodolistDiffCallback extends DiffUtil.Callback {
    List<Todolist> oldTodo;
    List<Todolist> newTodo;

    public TodolistDiffCallback(List<Todolist> oldTodo, List<Todolist> newTodo) {
        this.oldTodo = oldTodo;
        this.newTodo = newTodo;
    }

    @Override
    public int getOldListSize() {
        return oldTodo.size();
    }

    @Override
    public int getNewListSize() {
        return newTodo.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldTodo.get(oldItemPosition).getTodo_id() == newTodo.get(newItemPosition).getTodo_id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldTodo.get(oldItemPosition).equals(newTodo.get(newItemPosition));
    }


}
