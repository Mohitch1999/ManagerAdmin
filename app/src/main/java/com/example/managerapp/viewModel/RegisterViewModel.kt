package com.example.managerapp.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.managerapp.model.NotificationDecoratorModel
import com.example.managerapp.model.RegisterModel
import com.example.managerapp.model.SpinnerDecoratorModel
import com.example.managerapp.model.TLModel
import com.example.managerapp.model.TeamLeaderAttendanceModel
import com.example.managerapp.model.VideoLeadModel
import com.example.managerapp.network.ApiController
import com.example.managerapp.network.ApiInterface
import com.example.managerapp.repository.Repository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class RegisterViewModel : ViewModel() {

     var repository: Repository


    private val _leadData = MutableLiveData<List<LeadModel>>()
    val leadData: LiveData<List<LeadModel>> get() = _leadData


    private val _cityData = MutableLiveData<List<CityModel>>()
    val cityData : LiveData<List<CityModel>> = _cityData

    private val _manager = MutableLiveData<List<ManagerModel>>()
    val manager : LiveData<List<ManagerModel>> = _manager

    private val _managerAttendanceData = MutableLiveData<List<ManagerAttendanceModel>>()
    val managerAttendanceData : LiveData<List<ManagerAttendanceModel>> = _managerAttendanceData

    private val _teamLeaderAttendanceData = MutableLiveData<List<TeamLeaderAttendanceModel>>()
    val teamLeaderAttendanceData : LiveData<List<TeamLeaderAttendanceModel>> = _teamLeaderAttendanceData

    private val _decoratorAttendanceData = MutableLiveData<List<DecoratorAttendanceModel>>()
    val decoratorAttendanceData : LiveData<List<DecoratorAttendanceModel>> = _decoratorAttendanceData


    private val _getDecoratorForHrSpinner = MutableLiveData<List<SpinnerDecoratorModel>>()
    val getDecoratorForHrSpinner: LiveData<List<SpinnerDecoratorModel>> get() = _getDecoratorForHrSpinner

    private val _getTeamLeaderForHrSpinner = MutableLiveData<List<SpinnerDecoratorModel>>()
    val getTeamLeaderForHrSpinner: LiveData<List<SpinnerDecoratorModel>> get() = _getTeamLeaderForHrSpinner


    private val _tlData = MutableLiveData<List<TLModel>>()
    val tlData : LiveData<List<TLModel>> = _tlData


    private val _decoratorData = MutableLiveData<List<DecoratorModel>>()
    val decoratorData : LiveData<List<DecoratorModel>> = _decoratorData


    private val _decoratorLeadByIdData = MutableLiveData<List<LeadModel>>()
    val decoratorLeadByIdData : LiveData<List<LeadModel>> = _decoratorLeadByIdData


    private val _spinnerDecoratorData = MutableLiveData<List<SpinnerDecoratorModel>>()
    val spinnerDecoratorData : LiveData<List<SpinnerDecoratorModel>> = _spinnerDecoratorData

    private val _image  = MutableLiveData<List<ImageUploadModel>>()
    val imageUpload : LiveData<List<ImageUploadModel>> = _image


    init {
        val apiInterface = ApiController.getInstance().create(ApiInterface::class.java)
        repository = Repository(apiInterface)

    }

    fun fetchLeadData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = repository.fetchData()
                Log.d("response", response.toString())
                _leadData.postValue(response)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }


    fun fetchCityData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.fetchCityData()
                Log.d("response", response.toString())
                _cityData.postValue(response)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchManager() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getManager()
                Log.d("response", response.toString())
                _manager.postValue(response)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchTLData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.fetchTLData()
                Log.d("response", response.toString())
                _tlData.postValue(response)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchDecoratorData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.fetchDecoratorData()
                Log.d("response", response.toString())
                _decoratorData.postValue(response)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }



    @SuppressLint("SuspiciousIndentation")
    suspend fun fetchSpinnerDecoratorData(id : String) : List<SpinnerDecoratorModel>? {
        var res : List<SpinnerDecoratorModel>? = null
        val job =  viewModelScope.launch(Dispatchers.IO){
            val response =   repository.fetchSpinnerDecoratorData(id)
            withContext(Dispatchers.Main){
                res = response
            }
        }
        job.join()
        return res
    }

//    fun fetchDecoratorLeadByIdData() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = repository.fetchDecoratorLeadByIdData()
//                Log.d("response", response.toString())
//                _decoratorLeadByIdData.postValue(response)
//            } catch (e: Exception) {
//                // Handle error
//            }
//        }
//    }




    suspend fun insertLead(leadID : String,forUpdateOrNot : String,name : String,tl_id : String, file : String, mobile : String, whatsapp : String, decor_date : String, decor_time :String,
                   description : String, total_amount : String, recieve_amount : String,
                   location : String, address : String) : Response<LeadResponseModel>? {
        var responses : Response<LeadResponseModel>? = null
        val job = viewModelScope.launch(Dispatchers.IO){
             val res = repository.createLead(leadID,forUpdateOrNot,name,tl_id,file,mobile,whatsapp,decor_date,decor_time,description
                 ,total_amount,recieve_amount,address,location)
            responses = res
        }
        job.join()
        return responses
    }

    suspend fun updateLead(leadID : String,forUpdateOrNot : String,name : String,tl_id : String, file : String, mobile : String, whatsapp : String, decor_date : String, decor_time :String,
                           description : String, total_amount : String, recieve_amount : String,
                           location : String, address : String) : Response<LeadResponseModel>? {
        var responses : Response<LeadResponseModel>? = null
        val job = viewModelScope.launch(Dispatchers.IO){
            val res = repository.updateLead(leadID,forUpdateOrNot,name,tl_id,file,mobile,whatsapp,decor_date,decor_time,description
                ,total_amount,recieve_amount,address,location)
            responses = res
        }
        job.join()
        return responses
    }

    suspend fun createTeamLeader(manager_id : String, city_id : String,name : String, mobile : String, email : String, password : String) : Response<RegisterModel>?{
        var res : Response<RegisterModel>? = null
        val job = viewModelScope.launch(Dispatchers.IO){
            val response =repository.createTeamLeader(manager_id,city_id,name,mobile,email,password)
            withContext(Dispatchers.Main){
                res = response
            }
        }
        job.join()
        return res
    }


    @SuppressLint("SuspiciousIndentation")
    suspend fun createDecorator(city_id : String, name : String, mobile : String, email : String, password : String) : Response<RegisterModel>?{
       var res :Response<RegisterModel>? = null
        val job = viewModelScope.launch(Dispatchers.IO){
            val response = repository.createDecorator(city_id,name,mobile,email,password)
                withContext(Dispatchers.Main){
                    res = response
                }
        }
        job.join()
        return res
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun updateManager(id : String,choose_id : String, name : String, mobile : String, email : String, password : String) : Response<RegisterModel>?{
        var res :Response<RegisterModel>? = null
        val job = viewModelScope.launch(Dispatchers.IO){
            val response = repository.updateManager(id,choose_id,name,mobile,email,password)
            withContext(Dispatchers.Main){
                res = response
            }
        }
        job.join()
        return res
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getDecoratorLeadById(id : String) : List<LeadModel>? {
        var res : List<LeadModel>? = null
      val job =  viewModelScope.launch(Dispatchers.IO){
         val response =   repository.getDecoratorLeadById(id)
            withContext(Dispatchers.Main){
                res = response
            }
        }
        job.join()
        return res
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getDecoratorById(id : String) : List<DecoratorModel>? {
        var res : List<DecoratorModel>? = null
        val job =  viewModelScope.launch(Dispatchers.IO){
            val response =   repository.getDecoratorById(id)
            withContext(Dispatchers.Main){
                res = response
            }
        }
        job.join()
        return res
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getDecoratorLocationById(id : String) : List<DecoratorLocationModel>? {
        var res : List<DecoratorLocationModel>? = null
        val job =  viewModelScope.launch(Dispatchers.IO){
            val response =   repository.getDecoratorLocationById(id)
            withContext(Dispatchers.Main){
                res = response
            }
        }
        job.join()
        return res
    }

    suspend fun getTeamLeaderLeadById(id : String) : List<LeadModel>? {
        var res : List<LeadModel>? = null
        val job =  viewModelScope.launch(Dispatchers.IO){
            val response =   repository.getTeamLeaderLeadById(id)
            withContext(Dispatchers.Main){
                res = response
            }
        }
        job.join()
        return res
    }

    suspend fun getDecoratorLeadStatusById(id : String) : List<LeadModel>? {
        var res : List<LeadModel>? = null
        val job =  viewModelScope.launch(Dispatchers.IO){
            val response =   repository.getDecoratorLeadStatusById(id)
            withContext(Dispatchers.Main){
                res = response
            }
        }
        job.join()
        return res
    }

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }

    suspend fun getImageLeadById(id : String) : List<LeadImageModelItem>? {
        var res : List<LeadImageModelItem>? = null
        try {
            val job =  viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler){
                val response = repository.getImageLeadById(id)
                withContext(Dispatchers.Main){
                    res = response
                }
            }
            job.join()
        }catch (e: Exception){
            Log.d("response_getImage",e.toString())
        }

        return res
    }

    suspend fun deleteLeadById(id : String,choose_id : String) : Response<LoginModel>? {
        var res : Response<LoginModel>? = null
        try {
            val job =  viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler){
                val response = repository.deleteLeadById(id,choose_id)
                withContext(Dispatchers.Main){
                    res = response
                }
            }
            job.join()
        }catch (e: Exception){
            Log.d("response_getImage",e.toString())
        }

        return res
    }

    suspend fun getCashImageById(id : String) : List<CashImageModel>? {
        var res : List<CashImageModel>? = null
        try {
            val job =  viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler){
                val response = repository.getCashImageById(id)
                withContext(Dispatchers.Main){
                    res = response
                }
            }
            job.join()
        }catch (e: Exception){
            Log.d("response_getImage",e.toString())
        }

        return res
    }

    suspend fun getVideoById(id : String) : List<VideoLeadModel>? {
        var res : List<VideoLeadModel>? = null
        try {
            val job =  viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler){
                val response = repository.getVideoById(id)
                withContext(Dispatchers.Main){
                    res = response
                }
            }
            job.join()
        }catch (e: Exception){
            Log.d("response_getImage",e.toString())
        }

        return res
    }

    suspend fun getNotification(id : String,choose_id : String,) : List<NotificationDecoratorModel>? {
        var res : List<NotificationDecoratorModel>? = null
        try {
            val job =  viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler){
                val response = repository.getNotification(id,choose_id)
                withContext(Dispatchers.Main){
                    res = response
                }
            }
            job.join()
        }catch (e: Exception){
            Log.d("response_getNotification",e.toString())
        }

        return res
    }

    suspend fun createDecoratorLead(city_id : String, name : String) : Boolean{
        var res = false
        val job = viewModelScope.launch(Dispatchers.IO){
            val response = repository.createDecoratorLead(city_id,name)
            withContext(Dispatchers.Main){
                res = response.isSuccessful
            }
        }
        job.join()
        return res
    }

    fun getAttendanceManager() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getAttendanceManager()
                Log.d("response", response.toString())
                _managerAttendanceData.postValue(response)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    suspend fun getAttendanceLoginImage(id : String,choose_id : String,) : List<LoginUserImageModel>? {
        var res : List<LoginUserImageModel>? = null
        try {
            val job =  viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler){
                val response = repository.getAttendanceLoginImage(id,choose_id)
                withContext(Dispatchers.Main){
                    res = response
                }
            }
            job.join()
        }catch (e: Exception){
            Log.d("response_getNotification",e.toString())
        }

        return res
    }

    fun getAttendanceTeamLeader() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getAttendanceTeamLeader()
                Log.d("response", response.toString())
                _teamLeaderAttendanceData.postValue(response)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun getAttendanceDecorator() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getAttendanceDecorator()
                Log.d("response", response.toString())
                _decoratorAttendanceData.postValue(response)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun getDecoratorForHrSpinner() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = repository.getDecoratorForHrSpinner()
                Log.d("response", response.toString())
                _getDecoratorForHrSpinner.postValue(response)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun getTeamLeaderForSpinner() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = repository.getTeamLeaderForSpinner()
                Log.d("response", response.toString())
                _getTeamLeaderForHrSpinner.postValue(response)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    suspend fun updateHrLoginStatus(id : String,choose_id : String,) : Response<RegisterModel>? {
        var res : Response<RegisterModel>? = null
        try {
            val job =  viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler){
                val response = repository.updateHrLoginStatus(id,choose_id)
                withContext(Dispatchers.Main){
                    res = response
                }
            }
            job.join()
        }catch (e: Exception){
            Log.d("response_getNotification",e.toString())
        }

        return res
    }


    suspend fun updateOverTime(id : String,choose_id : String,over_time : String,) : Response<RegisterModel>? {
        var res : Response<RegisterModel>? = null
        try {
            val job =  viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler){
                val response = repository.updateOverTime(id,choose_id,over_time)
                withContext(Dispatchers.Main){
                    res = response
                }
            }
            job.join()
        }catch (e: Exception){
            Log.d("response_getNotification",e.toString())
        }

        return res
    }

    suspend fun updateDecoratorLeadStatus(id : String, status : String) : Boolean{
        var res = false
        val job = viewModelScope.launch(Dispatchers.IO){
            val response = repository.updateDecoratorLeadStatus(id, status)
            withContext(Dispatchers.Main){
                res = response.isSuccessful
            }
        }
        job.join()
        return res
    }

    suspend fun sendMultipleFirebaseApi(decorator_id : String,title : String,body : String) {
        repository.sendMultipleFirebaseApi(decorator_id,title, body)
//        var res = false
//        val job = viewModelScope.launch(Dispatchers.IO){
//            val response = repository.sendMultipleFirebaseApi(decorator_id,title, body)
//            withContext(Dispatchers.Main){
//                res = response.isSuccessful
//            }
//        }
//        job.join()
//        return res
    }

    suspend fun sendMultipleFirebaseApiDecorator(lead_id : String,title : String,body : String) : String{
//        repository.sendMultipleFirebaseApiDecorator(lead_id)
        var res = ""
        val job = viewModelScope.launch(Dispatchers.IO){
            val response = repository.sendMultipleFirebaseApiDecorator(lead_id,title, body)
            withContext(Dispatchers.Main){
                res = response.body()!!.success.toString()
            }
        }
        job.join()
        return res
    }

    suspend fun loginTeamLeader(  email : String, password : String) :  String{
         var message = "done"
       val job =  viewModelScope.launch(Dispatchers.IO){
           val messages = repository.loginTeamLeader(email,password)
            withContext(Dispatchers.Main){
              message = messages.body()!!.id
            }
        }
        job.join()
        return message
    }

    suspend fun loginAdmin(  email : String, password : String) :  String{
        var message = "done"
        val job =  viewModelScope.launch(Dispatchers.IO){
            val messages = repository.loginAdmin(email,password)
            withContext(Dispatchers.Main){
                message = messages.body()!!.id
            }
        }
        job.join()
        return message
    }

    suspend fun updateTokenManager(  id : String, token : String) :  String{
        var message = "done"
        try {
            val job =  viewModelScope.launch(Dispatchers.IO){
                val messages = repository.updateTokenManager(id,token)
                withContext(Dispatchers.Main){
                    message = messages.body()!!.message
                }
            }
            job.join()
        }catch (e : Exception){
            Log.d("mnagertokenupdate",e.toString())
        }

        return message
    }

    suspend fun updateTokenTeamLeader(  id : String, token : String) :  String{
        var message = "done"
        try {
            val job =  viewModelScope.launch(Dispatchers.IO){
                val messages = repository.updateTokenTeamLeader(id,token)
                withContext(Dispatchers.Main){
                    message = messages.body()!!.message
                }
            }
            job.join()
        }catch (e :Exception){
            Log.d("updatetokenteamleader",e.toString())
        }

        return message
    }

    suspend fun updateTokenDecorator(  id : String, token : String) :  String{
        var message = "done"
        try {
            val job =  viewModelScope.launch(Dispatchers.IO){
                val messages = repository.updateTokenDecorator(id,token)
                withContext(Dispatchers.Main){
                    message = messages.body()!!.message
                }
            }
            job.join()
        }catch (e : Exception){
            Log.d("updatetokendecorator",e.toString())
        }
        return message
    }

    suspend fun loginDecorator(email : String, password : String): String{
        var message  = "done"
        val job =  viewModelScope.launch(Dispatchers.IO){
            val messages = repository.loginDecorator(email,password)
            withContext(Dispatchers.Main){
                message = messages.body()!!.id
            }
        }
        job.join()
        return message
    }

    suspend fun createDecoratorLocation(lead_id : String,decorator_id : String,latitude : String,longitude : String,leadName : String,leadStatus : Int): String{
        var message  = "done"
        val job =  viewModelScope.launch(Dispatchers.IO){
            val messages = repository.createDecoratorLocation(lead_id, decorator_id, latitude, longitude,leadName,leadStatus)
            withContext(Dispatchers.Main){
                message = messages.body()!!.status.toString()
            }
        }
        job.join()
        return message
    }

    suspend fun checkIsLogin(  email : String, choose_id : String): String{
        var message  = "done"
        val job =  viewModelScope.launch(Dispatchers.IO){
            val messages = repository.checkIsLogin(email,choose_id)
            withContext(Dispatchers.Main){
                message = messages.body()!!.id
            }
        }
        job.join()
        return message
    }

    suspend fun loginManager(  email : String, password : String): String{
        var message  = "done"
        val job =  viewModelScope.launch(Dispatchers.IO){
            val messages = repository.loginManager(email,password)
            withContext(Dispatchers.Main){
                message = messages.body()!!.id
            }
        }
        job.join()
        return message
    }

    suspend fun sendNotification(id : String,title : String,body : String) : Response<FirebaseModel>?{
        var res : Response<FirebaseModel>? = null
        val join = viewModelScope.launch(Dispatchers.IO) {
            val message = repository.sendNotification(id,title, body)
            withContext(Dispatchers.Main){
                res = message
            }
        }
        join.join()
        return res
    }
}