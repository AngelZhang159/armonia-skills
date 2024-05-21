package com.dam.armoniaskills.network;

import com.dam.armoniaskills.model.ChatDTO;
import com.dam.armoniaskills.model.ChatMessage;
import com.dam.armoniaskills.model.ChatRoom;
import com.dam.armoniaskills.model.Review;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.model.User;

import java.util.List;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
	@POST("api/v1/user/login")
	Call<ResponseBody> loginUser(@Body User user);

	@POST("api/v1/user/register")
	Call<ResponseBody> registerUser(@Body User user);

	@POST("api/v1/user/loginJWT")
	Call<ResponseBody> loginJWT(@Header("Authorization") String jwt);

	@GET("api/v1/user/data")
	Call<ResponseBody> getUserData(@Header("Authorization") String jwt);

	@GET("api/v1/user")
	Call<User> getUserDataFromUUID(@Query("id") UUID id);

	@PATCH("api/v1/user/updateUser")
	Call<ResponseBody> updateUser(@Header("Authorization") String jwt, @Body User user);

	@Multipart
	@POST("api/v1/file/upload")
	Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

    @PATCH("api/v1/user/addReview")
    Call<ResponseBody> addReview(@Header("Authorization") String jwt, @Body Review review);


	//Crear nueva Skill
	@POST("api/v1/skill")
	Call<ResponseBody> postSkill(@Header("Authorization") String jwt, @Body Skill skill);

	@GET("api/v1/skill")
	Call<List<Skill>> getSkills();

	@GET("api/v1/chat")
	Call<List<ChatDTO>> getChats(@Header("Authorization") String jwt);

	@GET("api/v1/balance")
	Call<Double> getBalance(@Header("Authorization") String jwt);

	@PATCH("api/v1/balance/deposit")
	Call<ResponseBody> depositarDinero(@Header("Authorization") String token, @Body double cantidadFormateada);

	@PATCH("api/v1/balance/withdraw")
	Call<ResponseBody> retirarDinero(@Header("Authorization") String token, @Body double cantidadFormateada);

	@GET("api/v1/chat/new/{id}/{skillId}")
	Call<ChatRoom> createChat(@Header("Authorization") String token, @Path("id") UUID receiverId, @Path("skillId") UUID skillId);

	@GET("api/v1/messages/{chatId}")
	Call<List<ChatMessage>> getMessages(@Header("Authorization") String token, @Path("chatId") UUID chatId);

	@GET("api/v1/user/id")
	Call<UUID> getUserId(@Header("Authorization") String token);
}