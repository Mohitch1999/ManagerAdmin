package com.example.managerapp.repository

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
import com.example.managerapp.network.ApiInterface
import retrofit2.Response

class Repository (private val apiInterface: ApiInterface){


    suspend fun fetchData(): List<LeadModel> {
        return apiInterface.getLeadAllData()
    }

    suspend fun getManager(): List<ManagerModel> {
        return apiInterface.getManager()
    }

    suspend fun fetchCityData(): List<CityModel> {
        return apiInterface.getCityData()
    }

    suspend fun fetchTLData(): List<TLModel> {
        return apiInterface.getTeamLeaderData()
    }

    suspend fun fetchImageData(): List<ImageUploadModel> {
        return apiInterface.getImagesData()
    }

    suspend fun fetchDecoratorData(): List<DecoratorModel> {
        return apiInterface.getDecorator()
    }

    suspend fun fetchSpinnerDecoratorData(id : String): List<SpinnerDecoratorModel> {
        return apiInterface.getSpinnerDecorator(id)
    }

//    suspend fun fetchDecoratorLeadByIdData(): List<LeadModel> {
//        return apiInterface.getDecoratorLeadById()
//    }

    suspend fun createLead(leadID : String,forUpdateOrNot : String,name : String,tl_id : String,file : String, mobile : String,whatsapp : String,decor_date : String, decor_time :String,
                           description : String,total_amount : String,recieve_amount : String,
                           location : String,address : String) : Response<LeadResponseModel>{
        return apiInterface.getSignup(leadID,forUpdateOrNot,name,tl_id,file,mobile,whatsapp,decor_date,decor_time,description
                                        ,total_amount,recieve_amount,address,location)
    }

    suspend fun updateLead(leadID : String,forUpdateOrNot : String,name : String,tl_id : String,file : String, mobile : String,whatsapp : String,decor_date : String, decor_time :String,
                           description : String,total_amount : String,recieve_amount : String,
                           location : String,address : String) : Response<LeadResponseModel>{
        return apiInterface.updateLead(leadID,forUpdateOrNot,name,tl_id,file,mobile,whatsapp,decor_date,decor_time,description
            ,total_amount,recieve_amount,address,location)
    }

    suspend fun createTeamLeader(manager_id : String, city_id : String, name : String, mobile : String,email : String,password : String,) : Response<RegisterModel>{
        return apiInterface.createTeamLeader(manager_id,city_id,name,mobile,email,password)
    }

    suspend fun createDecorator(tl_id : String, name : String, mobile : String,email : String,password : String,) : Response<RegisterModel>{
        return apiInterface.createDecorator(tl_id,name,mobile,email,password)
    }

    suspend fun updateManager(tl_id : String,choose_id : String, name : String, mobile : String,email : String,password : String,) : Response<RegisterModel>{
        return apiInterface.updateManager(tl_id,choose_id,name,mobile,email,password)
    }

    suspend fun createDecoratorLead(decorator_id : String, lead_id : String) : Response<SignupModel>{
        return apiInterface.createDecoratorLead(decorator_id,lead_id)
    }

    suspend fun getAttendanceManager(): List<ManagerAttendanceModel> {
        return apiInterface.getAttendanceManager()
    }

    suspend fun getAttendanceTeamLeader(): List<TeamLeaderAttendanceModel> {
        return apiInterface.getAttendanceTeamLeader()
    }
    suspend fun getAttendanceDecorator(): List<DecoratorAttendanceModel> {
        return apiInterface.getAttendanceDecorator()
    }

    suspend fun getDecoratorForHrSpinner(): List<SpinnerDecoratorModel> {
        return apiInterface.getDecoratorForHrSpinner()
    }

    suspend fun getTeamLeaderForSpinner(): List<SpinnerDecoratorModel> {
        return apiInterface.getTeamLeaderForSpinner()
    }

    suspend fun updateHrLoginStatus(id : String,choose_id : String) : Response<RegisterModel>{
        return apiInterface.updateHrLoginStatus(id,choose_id)
    }

    suspend fun updateOverTime(id : String,choose_id : String,over_time : String) : Response<RegisterModel>{
        return apiInterface.updateOverTime(id,choose_id,over_time)
    }

    suspend fun getAttendanceLoginImage(id : String,choose_id : String) : List<LoginUserImageModel>{
        return apiInterface.getAttendanceLoginImage(id,choose_id)
    }

    suspend fun updateDecoratorLeadStatus(id : String, status : String) : Response<SignupModel>{
        return apiInterface.updateDecoratorLeadStatus(id,status)
    }

    suspend fun sendMultipleFirebaseApi(decorator_id : String,title : String,body : String) : Response<FirebaseModel>{
        return apiInterface.sendMultipleFirebaseApi(decorator_id,title, body)
    }

    suspend fun sendMultipleFirebaseApiDecorator(decorator_id : String,title : String,body : String) : Response<MultipleFirebaseApiDecoratorModel>{
        return apiInterface.sendMultipleFirebaseApiDecorator(decorator_id,title, body)
    }

    suspend fun loginTeamLeader(email : String,password : String) : Response<LoginModel>{
        return apiInterface.loginTeamLeader(email,password)
    }

    suspend fun loginAdmin(email : String,password : String) : Response<LoginModel>{
        return apiInterface.loginAdmin(email,password)
    }

    suspend fun updateTokenManager(id : String,token : String) : Response<TokenUpdateModel>{
        return apiInterface.updateTokenManager(id,token)
    }

    suspend fun updateTokenTeamLeader(id : String,token : String) : Response<TokenUpdateModel>{
        return apiInterface.updateTokenTeamLeader(id,token)
    }

    suspend fun updateTokenDecorator(id : String,token : String) : Response<TokenUpdateModel>{
        return apiInterface.updateTokenDecorator(id,token)
    }

    suspend fun loginDecorator(email : String,password : String,) : Response<LoginModel>{
        return apiInterface.loginDecorator(email,password)
    }

    suspend fun createDecoratorLocation(lead_id : String,decorator_id : String,latitude : String,longitude : String,leadName : String,leadStatus : Int) : Response<RegisterModel>{
        return apiInterface.createDecoratorLocation(lead_id, decorator_id, latitude, longitude,leadName,leadStatus)
    }

    suspend fun checkIsLogin(email : String,choose_id : String,) : Response<LoginModel>{
        return apiInterface.checkIsLogin(email,choose_id)
    }

    suspend fun loginManager(email : String,password : String,) : Response<LoginModel>{
        return apiInterface.loginManager(email,password)
    }

    suspend fun getDecoratorLeadById(id : String) : List<LeadModel>{
        return apiInterface.getDecoratorLeadById(id)
    }

    suspend fun getDecoratorById(id : String) : List<DecoratorModel>{
        return apiInterface.getDecoratorById(id)
    }

    suspend fun getDecoratorLocationById(id : String) : List<DecoratorLocationModel>{
        return apiInterface.getDecoratorLocationById(id)
    }

    suspend fun getTeamLeaderLeadById(id : String) : List<LeadModel>{
        return apiInterface.getTeamLeaderLeadById(id)
    }

    suspend fun getDecoratorLeadStatusById(id : String) : List<LeadModel>{
        return apiInterface.getDecoratorLeadStatusById(id)
    }

    suspend fun getImageLeadById(id : String) : List<LeadImageModelItem>{
        return apiInterface.getImageLeadById(id)
    }

    suspend fun deleteLeadById(id : String,choose_id : String) : Response<LoginModel>{
        return apiInterface.deleteLeadById(id,choose_id)
    }

    suspend fun getCashImageById(id : String) : List<CashImageModel>{
        return apiInterface.getCashImageById(id)
    }

    suspend fun getVideoById(id : String) : List<VideoLeadModel>{
        return apiInterface.getVideoById(id)
    }

    suspend fun getNotification(id : String,choose_id : String) : List<NotificationDecoratorModel>{
        return apiInterface.getNotification(id,choose_id)
    }

    suspend fun sendNotification(id : String,title : String,body : String) :Response<FirebaseModel>{
        return apiInterface.sendNotification(id,title, body)
    }

}