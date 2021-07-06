package com.example.kepo.request;

public class SearchTodoRequest {
    private String searchQuery;
    private int filterUser;
    private int filterTodo;

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public int getFilterUser() {
        return filterUser;
    }

    public void setFilterUser(int filterUser) {
        this.filterUser = filterUser;
    }

    public int getFilterTodo() {
        return filterTodo;
    }

    public void setFilterTodo(int filterTodo) {
        this.filterTodo = filterTodo;
    }
}
