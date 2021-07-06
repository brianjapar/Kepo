package com.example.kepo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.GetTodoActivity;
import com.example.kepo.IClickable;
import com.example.kepo.R;
import com.example.kepo.diffutils.TodolistDiffCallback;
import com.example.kepo.modal.Todolist;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.TodolistViewHolder> {

    private ArrayList<Todolist> todolists;
    private IClickable listener;
    private GetTodoActivity getTodoActivity;

    public TodolistAdapter(ArrayList<Todolist> todolists, IClickable listener, GetTodoActivity getTodoActivity) {
        this.todolists = todolists;
        this.listener = listener;
        this.getTodoActivity = getTodoActivity;
    }

    @NonNull
    @Override
    public TodolistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.todolist_item_layout,parent,false);

        return new TodolistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodolistViewHolder holder, int position) {
        Todolist todolist = todolists.get(position);
        holder.tvTitle.setText(todolist.getTitle());
        holder.tvEdit.setText(todolist.getLastEdited());
        if(getTodoActivity.isActionMode){
            holder.cbCheckbox.setChecked(false);
        }
        holder.cbCheckbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.checkBoxClick(todolist,v,position);
            }
        });
        holder.llBoxitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTimeClick(todolist);
            }
        });

    }

    @Override
    public int getItemCount() {
        return todolists.size();
    }

    public void addTodolist(Todolist todolist){
        todolists.add(todolist);
        notifyDataSetChanged();
    }


    class TodolistViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle,tvEdit;
        private LinearLayout llBoxitem;
        private CheckBox cbCheckbox;
        private View view;

        public TodolistViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvEdit = itemView.findViewById(R.id.tv_edited);
            llBoxitem = itemView.findViewById(R.id.ll_boxItem);
            cbCheckbox = itemView.findViewById(R.id.cb_checkbox);
            view=itemView;
            view.setOnLongClickListener(getTodoActivity);
            getTodoActivity.isActionMode = !cbCheckbox.isChecked();
        }
    }

    public void RemoveItem(ArrayList<Todolist> selectionTodolist){
        for(int i=0;i<selectionTodolist.size();i++){
            todolists.remove(selectionTodolist.get(i));
            notifyDataSetChanged();
        }
    }

    public void updateList(ArrayList<Todolist> newTodo){
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TodolistDiffCallback(this.todolists,newTodo));
        diffResult.dispatchUpdatesTo(this);
    }

}
