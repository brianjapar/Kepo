package com.example.kepo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.IClickable;
import com.example.kepo.IClickableUser;
import com.example.kepo.R;
import com.example.kepo.SearchUserActivity;
import com.example.kepo.modal.SearchTodolistUserDetail;
import com.example.kepo.modal.Todolist;
import com.example.kepo.modal.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<User> users;
    private IClickableUser listener;
    private SearchUserActivity searchUserActivity;

    public UserAdapter(ArrayList<User> users, IClickableUser listener, SearchUserActivity searchUserActivity) {
        this.users = users;
        this.listener = listener;
        this.searchUserActivity = searchUserActivity;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_item_layout,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvUsername.setText(user.getUsername());
        holder.tvName.setText(user.getName());
        holder.llBoxitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickUser(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addUser(User user){
        users.add(user);
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUsername,tvName;
        private LinearLayout llBoxitem;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvName = itemView.findViewById(R.id.tv_name);
            llBoxitem = itemView.findViewById(R.id.ll_boxItem);
        }
    }
    public void RemoveItem(){
        users.clear();
        notifyDataSetChanged();
    }
}
