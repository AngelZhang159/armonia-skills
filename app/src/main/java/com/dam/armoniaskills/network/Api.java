package com.dam.armoniaskills.network;

import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.model.User;

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

public interface Api {
	@POST("api/v1/user/login")
	Call<ResponseBody> loginUser(@Body User user);

	@POST("api/v1/user/register")
	Call<ResponseBody> registerUser(@Body User user);

	@POST("api/v1/user/loginJWT")
	Call<ResponseBody> loginJWT(@Header("Authorization") String jwt);

	@GET("api/v1/user/data")
	Call<ResponseBody> getUserData(@Header("Authorization") String jwt);

	@PATCH("api/v1/user/updateUser")
	Call<ResponseBody> updateUser(@Header("Authorization") String jwt, @Body User user);

	@Multipart
	@POST("api/v1/file/upload")
	Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);


	//Crear nueva Skill
	@POST("api/v1/skill")
	Call<ResponseBody> postSkill(@Header("Authorization") String jwt, @Body Skill skill);
}