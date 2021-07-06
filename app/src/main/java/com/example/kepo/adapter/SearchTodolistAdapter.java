package com.example.kepo.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kepo.IClickableSearchTodo;
import com.example.kepo.R;
import com.example.kepo.SearchTodoActivity;
import com.example.kepo.modal.SearchTodolistUserDetail;
import com.example.kepo.modal.UserTodolistDetail;

import java.util.ArrayList;

public class SearchTodolistAdapter extends RecyclerView.Adapter<SearchTodolistAdapter.SearchTodolistViewHolder>{

    private ArrayList<SearchTodolistUserDetail> todolists;
    private IClickableSearchTodo listener;
    private SearchTodoActivity searchTodoActivity;

    public SearchTodolistAdapter(ArrayList<SearchTodolistUserDetail> todolists, IClickableSearchTodo listener, SearchTodoActivity searchTodoActivity) {
        this.todolists = todolists;
        this.listener = listener;
        this.searchTodoActivity = searchTodoActivity;
    }

    @NonNull
    @Override
    public SearchTodolistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.userdetail_todolist_item_layout,parent,false);

        return new SearchTodolistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTodolistViewHolder holder, int position) {
        SearchTodolistUserDetail todolist = todolists.get(position);
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

    public void addTodolist(SearchTodolistUserDetail todolist){
        todolists.add(todolist);
        notifyDataSetChanged();
    }

    class SearchTodolistViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle,tvEdit,tvName;
        private LinearLayout llBoxitem;

        public SearchTodolistViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvEdit = itemView.findViewById(R.id.tv_edited);
            llBoxitem = itemView.findViewById(R.id.ll_boxItem);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }

    public void RemoveItem(){
        todolists.clear();
        notifyDataSetChanged();
    }

}
