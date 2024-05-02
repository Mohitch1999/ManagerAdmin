package com.example.managerapp.network

import com.example.managerapp.firebase.model.FirebaseModel
import com.example.managerapp.model.CashImageModel
import com.example.managerapp.model.CityModel
import com.example.managerapp.model.DecoratorAttendanceModel
import com.example.managerapp.model.DecoratorLocationModel
import com.example.managerapp.model.DecoratorModel
import com.example.managerapp.model.ImageUploadModel
import com.example.managerapp.model.LeadImageModelItem
import com.example.managerapp.model.LeadModel
import com.example.managerapp.model.LeadResponseModel
import com.example.managerapp.model.LoginModel
import com.example.managerapp.model.LoginUserImageModel
import com.example.managerapp.model.ManagerAttendanceModel
import com.example.managerapp.model.ManagerModel
import com.example.managerapp.model.MultipleFirebaseApiDecoratorModel
import com.example.managerapp.model.NotificationDecoratorModel
import com.example.managerapp.model.RegisterModel
import com.example.managerapp.model.SignupModel
import com.example.managerapp.model.SpinnerDecoratorModel
import com.example.managerapp.model.TLModel
import com.example.managerapp.model.TeamLeaderAttendanceModel
import com.example.managerapp.model.TokenUpdateModel
import com.example.managerapp.model.VideoLeadModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ApiInterface {


    @POST("conn.php")
    @FormUrlEncoded
    suspend fun getSignup(
        @Field("leadid") leadid: String,
        @Field("forUpdateOrNot") forUpdateOrNot: String,
        @Field("name") name: String,
        @Field("tid") tlid: String,
        @Field("file") a: String,
        @Field("mobile") mobile: String,
        @Field("whatsapp") whatsapp: String,
        @Field("decor_date") decor_date: String,
        @Field("decor_time") decor_time: String,
        @Field("description") description: String,
        @Field("total_amount") total_amount: String,
        @Field("recieve_amount") recieve_amount: String,
        @Field("address") address: String,
        @Field("location") location: String,
    ): Response<LeadResponseModel>

    @POST("UpdateLead.php")
    @FormUrlEncoded
    suspend fun updateLead(
        @Field("leadid") leadid: String,
        @Field("forUpdateOrNot") forUpdateOrNot: String,
        @Field("name") name: String,
        @Field("tid") tlid: String,
        @Field("file") a: String,
        @Field("mobile") mobile: String,
        @Field("whatsapp") whatsapp: String,
        @Field("decor_date") decor_date: String,
        @Field("decor_time") decor_time: String,
        @Field("description") description: String,
        @Field("total_amount") total_amount: String,
        @Field("recieve_amount") recieve_amount: String,
        @Field("address") address: String,
        @Field("location") location: String,
    ): Response<LeadResponseModel>


    @Multipart
    @POST("images.php")
    fun uploadImage(
        @Part file: MultipartBody.Part
    ): Call<SignupModel>


    @POST("createteamleader.php")
    @FormUrlEncoded
    suspend fun createTeamLeader(
        @Field("manager_id") manager_id: String,
        @Field("city_id") city_id: String,
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<RegisterModel>

    @POST("updateManager.php")
    @FormUrlEncoded
    suspend fun updateManager(
        @Field("id") id: String,
        @Field("choose_id") choose_id: String,
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<RegisterModel>

    @POST("createDecorator.php")
    @FormUrlEncoded
    suspend fun createDecorator(
        @Field("tl_id") tl_id: String,
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<RegisterModel>

    @POST("createDecoratorLead.php")
    @FormUrlEncoded
    suspend fun createDecoratorLead(
        @Field("decorator_id") decorator_id: String,
        @Field("lead_id") lead_id: String,
    ) : Response<SignupModel>


    @POST("UpdateDecoratorLeadStatus.php")
    @FormUrlEncoded
    suspend fun updateDecoratorLeadStatus(
        @Field("id") id: String,
        @Field("status") status: String,
    ) : Response<SignupModel>


    @POST("MultipleFirebaseApi.php")
    @FormUrlEncoded
    suspend fun sendMultipleFirebaseApi(
        @Field("id") id: String,
        @Field("title") title: String,
        @Field("body") body: String

    ) : Response<FirebaseModel>

    @POST("MultipleFirebaseApiDecorator.php")
    @FormUrlEncoded
    suspend fun sendMultipleFirebaseApiDecorator(
        @Field("id") id: String,
        @Field("title") title: String,
        @Field("body") body: String
    ) : Response<MultipleFirebaseApiDecoratorModel>

    @POST("loginTeamLeader.php")
    @FormUrlEncoded
    suspend fun loginTeamLeader(
        @Field("email") email: String,
        @Field("password") password: String

    ) : Response<LoginModel>

    @POST("loginAdmin.php")
    @FormUrlEncoded
    suspend fun loginAdmin(
        @Field("email") email: String,
        @Field("password") password: String

    ) : Response<LoginModel>

    @POST("updateTokenManager.php")
    @FormUrlEncoded
    suspend fun updateTokenManager(
        @Field("id") id: String,
        @Field("token") token: String
    ) : Response<TokenUpdateModel>

    @POST("updateTokenTeamLeader.php")
    @FormUrlEncoded
    suspend fun updateTokenTeamLeader(
        @Field("id") id: String,
        @Field("token") token: String
    ) : Response<TokenUpdateModel>

    @POST("updateTokenDecorator.php")
    @FormUrlEncoded
    suspend fun updateTokenDecorator(
        @Field("id") id: String,
        @Field("token") token: String
    ) : Response<TokenUpdateModel>

    @POST("loginManager.php")
    @FormUrlEncoded
    suspend fun loginManager(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<LoginModel>

    @POST("loginDecorator.php")
    @FormUrlEncoded
    suspend fun loginDecorator(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<LoginModel>

    @POST("createDecoratorLocation.php")
    @FormUrlEncoded
    suspend fun createDecoratorLocation(
        @Field("lead_id") lead_id: String,
        @Field("decorator_id") decorator_id: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("lead_name") leadName: String,
        @Field("lead_status") lead_status: Int,
    ) : Response<RegisterModel>

    @POST("checkIsLogin.php")
    @FormUrlEncoded
    suspend fun checkIsLogin(
        @Field("email") email: String,
        @Field("choose_id") choose_id: String
    ) : Response<LoginModel>

    @POST("getDecoratorLeadById.php")
    @FormUrlEncoded
    suspend fun getDecoratorLeadById(
        @Field("id") id: String
    ) : List<LeadModel>

    @POST("getDecoratorById.php")
    @FormUrlEncoded
    suspend fun getDecoratorById(
        @Field("id") id: String
    ) : List<DecoratorModel>

    @POST("getDecoratorLocationById.php")
    @FormUrlEncoded
    suspend fun getDecoratorLocationById(
        @Field("id") id: String
    ) : List<DecoratorLocationModel>


    @POST("getTeamLeaderLeadById.php")
    @FormUrlEncoded
    suspend fun getTeamLeaderLeadById(
        @Field("tid") id: String
    ) : List<LeadModel>

    @POST("getImageByLeadId.php")
    @FormUrlEncoded
    suspend fun getImageLeadById(
        @Field("lead_id") id: String
    ) : List<LeadImageModelItem>

    @POST("deleteLead.php")
    @FormUrlEncoded
    suspend fun deleteLeadById(
        @Field("id") id: String,
        @Field("choose_id") choose_id: String
    ) : Response<LoginModel>

    @POST("getCashImages.php")
    @FormUrlEncoded
    suspend fun getCashImageById(
        @Field("lead_id") id: String
    ) : List<CashImageModel>

    @POST("getVideoById.php")
    @FormUrlEncoded
    suspend fun getVideoById(
        @Field("lead_id") id: String
    ) : List<VideoLeadModel>

    @POST("getNotification.php")
    @FormUrlEncoded
    suspend fun getNotification(
        @Field("id") id: String,
        @Field("choose_id") choose_id: String
    ) : List<NotificationDecoratorModel>

    @POST("getDecoratorLeadStatus.php")
    @FormUrlEncoded
    suspend fun getDecoratorLeadStatusById(
        @Field("id") id: String
    ) : List<LeadModel>

    @POST("FirebaseApi.php")
    @FormUrlEncoded
    suspend fun sendNotification(
        @Field("id") id: String,
        @Field("title") title: String,
        @Field("body") body: String
    ) : Response<FirebaseModel>

    @POST("getDecoratorForSpinner.php")
    @FormUrlEncoded
    suspend fun getSpinnerDecorator(
        @Field("id") id: String,
    ): List<SpinnerDecoratorModel>

    @POST("getAttendanceLoginImage.php")
    @FormUrlEncoded
    suspend fun getAttendanceLoginImage(
        @Field("id") id: String,
        @Field("choose_id") choose_id: String,
    ) : List<LoginUserImageModel>

    @POST("updateHrLoginStatus.php")
    @FormUrlEncoded
    suspend fun updateHrLoginStatus(
        @Field("id") id: String,
        @Field("choose_id") choose_id: String,
    ) : Response<RegisterModel>

    @POST("updateOverTime.php")
    @FormUrlEncoded
    suspend fun updateOverTime(
        @Field("id") id: String,
        @Field("choose_id") choose_id: String,
        @Field("over_time") over_time: String,
    ) : Response<RegisterModel>

    @GET("getAttendanceManager.php")
    suspend fun getAttendanceManager(): List<ManagerAttendanceModel>

    @GET("getAttendanceTeamLeader.php")
    suspend fun getAttendanceTeamLeader(): List<TeamLeaderAttendanceModel>

    @GET("getAttendanceDecorator.php")
    suspend fun getAttendanceDecorator(): List<DecoratorAttendanceModel>

    @GET("getDecoratorForHrSpinner.php")
    suspend fun getDecoratorForHrSpinner(): List<SpinnerDecoratorModel>

    @GET("getTeamLeaderForSpinner.php")
    suspend fun getTeamLeaderForSpinner(): List<SpinnerDecoratorModel>

    @GET("getLead.php")
    suspend fun getLeadAllData(): List<LeadModel>

    @GET("getManager.php")
    suspend fun getManager(): List<ManagerModel>

    @GET("getDecorator.php")
    suspend fun getDecorator(): List<DecoratorModel>

//    @Post("getDecoratorLeadById.php")
//    suspend fun getDecoratorLeadById(): List<LeadModel>

    @GET("getCity.php")
    suspend fun getCityData(): List<CityModel>

    @GET("getTeamLeader.php")
    suspend fun getTeamLeaderData(): List<TLModel>

    @GET("images.php")
    suspend fun getImagesData(): List<ImageUploadModel>

}