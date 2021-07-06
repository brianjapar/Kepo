package com.example.kepo.apihelper;

import com.example.kepo.request.CreateTodoRequest;
import com.example.kepo.request.DeleteTodoRequest;
import com.example.kepo.request.LoginRequest;
import com.example.kepo.request.SearchTodoRequest;
import com.example.kepo.request.SearchUserRequest;
import com.example.kepo.request.UpdateTodoRequest;
import com.example.kepo.response.CreateTodoResponse;
import com.example.kepo.response.DeleteTodoResponse;
import com.example.kepo.response.GetTodoDetailResponse;
import com.example.kepo.response.GetUserTodosResponse;
import com.example.kepo.response.LoginResponse;
import com.example.kepo.response.SearchTodoResponse;
import com.example.kepo.response.SearchUserResponse;
import com.example.kepo.response.UpdateTodoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @POST("login/")
    Call<LoginResponse> userLogin(@Body LoginRequest user);

    @POST("user/{user_id}/todo")
    Call<CreateTodoResponse> createTodo(@Body CreateTodoRequest createTodoRequest,
                                        @Path("user_id") String user_id);

    @GET("user/{user_id}/todo")
    Call<GetUserTodosResponse> getTodo(@Path("user_id") String user_id);

    @GET("user/{user_id}/todo/{todo_id}")
    Call<GetTodoDetailResponse> getTodoDetail(@Path("user_id") String user_id,
                                              @Path("todo_id") String todo_id);

    @PUT("user/{user_id}/todo/{todo_id}")
    Call<UpdateTodoResponse> updateTodo(@Body UpdateTodoRequest updateTodoRequest,
                                        @Path("user_id") String user_id,
                                        @Path("todo_id") String todo_id);

//    @HTTP(method = "DELETE", path = "user/{user_id}/todo", hasBody = true)
//    Call<DeleteTodoResponse> deleteTodo(@Body DeleteTodoRequest deleteTodoRequest,
//                                        @Path("user_id") String user_id);

    @POST("user/{user_id}/deleteTodo")
    Call<DeleteTodoResponse> deleteTodo(@Body DeleteTodoRequest deleteTodoRequest,
                                        @Path("user_id") String user_id);

    @POST("searchUser/")
    Call<SearchUserResponse> searchUser(@Body SearchUserRequest searchUserRequest);

    @POST("searchTodos/")
    Call<SearchTodoResponse> searchTodo(@Body SearchTodoRequest searchTodoRequest);

}
