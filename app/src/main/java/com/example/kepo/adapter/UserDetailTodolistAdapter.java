package com.example.kepo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.DetailUserScreenActivity;
import com.example.kepo.IClickable;
import com.example.kepo.IClickableUserTodo;
import com.example.kepo.R;
import com.example.kepo.modal.Todolist;
import com.example.kepo.modal.UserTodolistDetail;

import java.util.ArrayList;

public class UserDetailTodolistAdapter extends RecyclerView.Adapter<UserDetailTodolistAdapter.UserDetailTodolistHolder> {

    private ArrayList<UserTodolistDetail> todolists;
    private IClickableUserTodo listener;
    private DetailUserScreenActivity detailUserScreenActivity;

    public UserDetailTodolistAdapter(ArrayList<UserTodolistDetail> todolists, IClickableUserTodo listener, DetailUserScreenActivity detailUserScreenActivity) {
        this.todolists = todolists;
        this.listener = listener;
        this.detailUserScreenActivity = detailUserScreenActivity;
    }

    @NonNull
    @Override
    public UserDetailTodolistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.userdetail_todolist_item_layout,parent,false);

        return new UserDetailTodolistHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailTodolistHolder holder, int position) {
        UserTodolistDetail todolist = todolists.get(position);
        holder.tvTitle.setText(todolist.getTitle());
        holder.tvEdit.setText(todolist.getLastEdited());
        holder.tvName.setText(todolist.getName());
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

    public void addTodolist(UserTodolistDetail todolist){
        todolists.add(todolist);
        notifyDataSetChanged();
    }

    class UserDetailTodolistHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle,tvEdit,tvName;
        private LinearLayout llBoxitem;

        public UserDetailTodolistHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvEdit = itemView.findViewById(R.id.tv_edited);
            llBoxitem = itemView.findViewById(R.id.ll_boxItem);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
