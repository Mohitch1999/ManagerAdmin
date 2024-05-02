package com.example.managerapp.view.fragment.decorator

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivityAddLeadImagesBinding
import com.example.managerapp.databinding.ActivityCashImageBinding
import com.example.managerapp.model.SignupModel
import com.example.managerapp.network.MyApi
import com.example.managerapp.utils.Prefs
import com.example.managerapp.view.fragment.manager.UploadRequestBody
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class CashImageActivity: AppCompatActivity(), UploadRequestBody.UploadCallback {


    lateinit var binding: ActivityCashImageBinding

    private var selectedImageUri: Uri? = null
    lateinit var viewModel: RegisterViewModel

    var id = ""
    var name = ""

    var decoratorId = 0
    var latitude = 0.0
    var longitude = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCashImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.progressbar.visibility = View.GONE

        id = intent.getStringExtra("id").toString()
        name = intent.getStringExtra("leadname").toString()
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        decoratorId = Prefs.getDecoratorId()


        binding.getImageSubmitButton.setOnClickListener {
            opeinImageChooser()
        }

        binding.submitButton.setOnClickListener {
            if(selectedImageUri == null){
                Toast.makeText(
                    this@CashImageActivity,
                    "Please Select Image",
                    Toast.LENGTH_LONG
                ).show()
            }else{
                binding.progressbar.visibility = View.VISIBLE
                uploadImage()
            }

        }
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    binding.imgpreview.setImageURI(selectedImageUri)
                }
            }
        }
    }

    fun opeinImageChooser() {

        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun uploadImage() {

        getCurrentLocation()
        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            selectedImageUri!!, "r", null
        ) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image", this)
//        binding.progressbar.progress = 0

        MyApi().uploadCashImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("text/plain"), id)
        ).enqueue(object : Callback<SignupModel> {
            override fun onResponse(
                call: Call<SignupModel>,
                response: Response<SignupModel>
            ) {
                Toast.makeText(
                    this@CashImageActivity,
                    "Successfull",
                    Toast.LENGTH_LONG
                ).show()

                sendLocation(id!!,decoratorId.toString(),latitude.toString(),longitude.toString(),name,5)

                lifecycleScope.launch {
                    val resStatus =  viewModel.updateDecoratorLeadStatus(id!!,"5")
                    if(resStatus){
                        val res =  viewModel.sendMultipleFirebaseApiDecorator(id,"Lead Status","Decorator upload payment")
                        if(res.toInt() == 1){
                            binding.progressbar.visibility = View.GONE
                            finish()
                        }
                    }
                }



//                finish()
                binding.progressbar.visibility = View.GONE

                Log.d("response", "submitted successfully")
            }

            override fun onFailure(call: Call<SignupModel>, t: Throwable) {
                binding.progressbar.visibility = View.GONE
                Toast.makeText(
                    this@CashImageActivity,
                    t.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }

        })


    }

    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }

    private fun View.snackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction("OK") {
                snackbar.dismiss()
            }
        }.show()

    }

    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressbar.progress = percentage
    }

    fun sendLocation(id : String,decoratorId : String,lat : String ,long :String,leadname : String,leadStatus : Int){
        lifecycleScope.launch(Dispatchers.IO ) {
            var response = viewModel.createDecoratorLocation(id,decoratorId,lat,long,leadname,leadStatus)
            if(response.toInt()==1){
                withContext(Dispatchers.Main){
                    Log.d("locations",response + " Location update")
                }

            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                latitude = location?.latitude!!
                longitude = location?.longitude!!
                Toast.makeText(this@CashImageActivity,"Location id $latitude and $longitude",Toast.LENGTH_LONG).show()
                Log.d("locations","$latitude $longitude is the location")

            }
    }

}
