package com.example.managerapp.network

import com.example.managerapp.model.SignupModel
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit


interface MyApi {

    @Multipart
    @POST("images.php")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("lead_id") desc: RequestBody
    ): Call<SignupModel>


    @Multipart
    @POST("loginStatusImage.php")
    fun uploadLoginStatusImage(
        @Part image: MultipartBody.Part,
        @Part("manager_id") manager_id: RequestBody,
        @Part("login_status_id") login_status_id: RequestBody,
        @Part("choose_id") choose_id: RequestBody,
        @Part("in_or_out") in_or_out: RequestBody,
        @Part("update_id") update_id: RequestBody
    ): Call<SignupModel>

    @Multipart
    @POST("logoutStatusImage.php")
    fun logoutStatusImage(
        @Part image: MultipartBody.Part,
        @Part("choose_id") choose_id: RequestBody,
        @Part("login_status_id") login_status_id: RequestBody,
        @Part("login_id") login_id: RequestBody
    ): Call<SignupModel>


    @Multipart
    @POST("cashImages.php")
    fun uploadCashImage(
        @Part image: MultipartBody.Part,
        @Part("lead_id") desc: RequestBody
    ): Call<SignupModel>

    @Multipart
    @POST("videos.php")
    fun uploadVideo(
        @Part video: MultipartBody.Part,
        @Part("lead_id") desc: RequestBody,
        @Part("btn_id") btn_id: RequestBody
    ): Call<SignupModel>



    companion object {
        var okHttpClient: OkHttpClient
            get() = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
            set(value) = TODO()
        operator fun invoke(): MyApi{


            return Retrofit.Builder()
                .baseUrl("https://corporatelife.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(MyApi::class.java)
        }
    }
}