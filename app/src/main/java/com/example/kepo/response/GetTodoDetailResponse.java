package com.example.kepo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTodoDetailResponse {
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

    public static class Data {
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("todo_id")
        @Expose
        private String todo_id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("last_edited")
        @Expose
        private String last_edited;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLast_edited() {
            return last_edited;
        }

        public void setLast_edited(String last_edited) {
            this.last_edited = last_edited;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "username='" + username + '\'' +
                    ", todo_id='" + todo_id + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", last_edited='" + last_edited + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetTodoDetailResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
