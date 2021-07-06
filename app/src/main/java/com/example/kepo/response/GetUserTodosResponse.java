package com.example.kepo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserTodosResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetUserTodosResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data {
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("listTodo")
        @Expose
        private List<ListTodo> listTodo;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ListTodo> getListTodo() {
            return listTodo;
        }

        public void setListTodo(List<ListTodo> listTodo) {
            this.listTodo = listTodo;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "userId='" + userId + '\'' +
                    ", username='" + username + '\'' +
                    ", name='" + name + '\'' +
                    ", listTodo=" + listTodo +
                    '}';
        }
    }

    public static class ListTodo{
        @SerializedName("todo_id")
        @Expose
        private String todo_id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("last_edited")
        @Expose
        private String last_edited;

        public String getTodo_id() {
            return todo_id;
        }

        public void setTodo_id(String todo_id) {
            this.todo_id = todo_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLast_edited() {
            return last_edited;
        }

        public void setLast_edited(String last_edited) {
            this.last_edited = last_edited;
        }

        @Override
        public String toString() {
            return "ListTodo{" +
                    "todo_id='" + todo_id + '\'' +
                    ", title='" + title + '\'' +
                    ", last_edited='" + last_edited + '\'' +
                    '}';
        }
    }
}
