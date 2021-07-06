package com.example.kepo.request;

import java.util.List;

public class DeleteTodoRequest {
    private List<String> todos;

    public List<String> getTodos() {
        return todos;
    }

    public void setTodos(List<String> todos) {
        this.todos = todos;
    }
}
