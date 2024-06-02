package com.dam.armoniaskills.network;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUpload {
	public void subirImagen(Uri imageUri, ContentResolver contentResolver, UploadCallback callback) {

		if (imageUri == null) {
			callback.onError(new NullPointerException("URI is null"));
			return;
		}

		MultipartBody.Part filePart = convertUriToMultipartBody(imageUri, contentResolver);

		Call<ResponseBody> call = RetrofitClient
				.getInstance()
				.getApi()
				.uploadImage(filePart);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					try {
						String result = response.body().string();
						callback.onSuccess(result);
					} catch (IOException e) {
						callback.onError(e);
					}
				} else {
					callback.onError(new Exception("Response not successful: " + response));
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable throwable) {
				callback.onError(throwable);
			}
		});
	}

	private MultipartBody.Part convertUriToMultipartBody(Uri uri, ContentResolver contentResolver) {
		// Get the file path from the Uri
		String filePath = getPathFromUri(uri, contentResolver);
		File file = new File(filePath);

		// Create a RequestBody instance from the file
		RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

		// Create MultipartBody.Part using the file
		return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
	}

	private String getPathFromUri(Uri uri, ContentResolver contentResolver) {
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		cursor.moveToFirst();
		String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
		cursor.close();
		return path;
	}
}
