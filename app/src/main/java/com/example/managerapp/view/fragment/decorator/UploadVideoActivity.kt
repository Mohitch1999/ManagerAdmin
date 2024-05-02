package com.example.managerapp.view.fragment.decorator

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.databinding.ActivityUploadVideoBinding
import com.example.managerapp.model.SignupModel
import com.example.managerapp.network.MyApi
import com.example.managerapp.utils.Prefs
import com.example.managerapp.view.fragment.manager.UploadRequestBody
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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


class UploadVideoActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private lateinit var binding: ActivityUploadVideoBinding

    val PERMISSION_CODE = 100
    private val REQUEST_TAKE_GALLERY_VIDEO = 1000;
    private var selectedImageUri: Uri? = null
    lateinit var viewModel: RegisterViewModel
    var id =""
    var btnId = ""
    var leadname = ""
    var decoratorId = 0
    var latitude = 0.0
    var longitude = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        if (extras != null) {
          id = extras.getString("id").toString()
          leadname = extras.getString("leadname").toString()
          btnId = extras.getString("btn").toString()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        decoratorId = Prefs.getDecoratorId()

        binding.video.setOnClickListener {
            takePermission()
        }

        binding.headerLayout.headerTitle.text = "Upload Video"

        binding.submitbtn.setOnClickListener {
            if(selectedImageUri!=null){
                uploadImage()

            }else{
                Toast.makeText(this@UploadVideoActivity,"Please select videos",Toast.LENGTH_LONG).show()
            }

        }
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)


    }

    fun takePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED && checkSelfPermission(
                    Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                chooseImageGallery()
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                chooseImageGallery()
            }
        }

    }


    @SuppressLint("SuspiciousIndentation")
    private fun uploadImage() {

//        if (selectedImageUri == null) {
//            layout_root .snackbar("Select an Image First")
//            return
//        }
        binding.video.isEnabled = false
        getCurrentLocation()
        binding.progressbar.visibility = View.VISIBLE

        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            selectedImageUri!!, "r", null
        ) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir,contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        binding.progressbar.progress = 0
        val body = UploadRequestBody(file,"video",this)

        if(btnId.toInt() == 1){
            MyApi().uploadVideo(
                MultipartBody.Part.createFormData(
                    "before_decoration",
                    file.name,
                    body
                ),
                RequestBody.create(MediaType.parse("text/plain"), id),
                RequestBody.create(MediaType.parse("text/plain"), btnId),
            ).enqueue(object : Callback<SignupModel> {
                override fun onResponse(
                    call: Call<SignupModel>,
                    response: Response<SignupModel>
                ) {
                    binding.video.isEnabled = true

                    Toast.makeText(
                        this@UploadVideoActivity,
                        "Successfull",
                        Toast.LENGTH_LONG
                    ).show()

                    sendLocation(id!!,decoratorId.toString(),latitude.toString(),longitude.toString(),leadname,3)

                   lifecycleScope.launch {
                      val resStatus =  viewModel.updateDecoratorLeadStatus(id!!,"3")
                       if(resStatus){
                           val res =  viewModel.sendMultipleFirebaseApiDecorator(id,"Lead Status","Decorator upload video before Decoration")
                           if(res.toInt() == 1){
                               binding.progressbar.visibility = View.GONE
                               finish()
                           }
                       }
                   }

                    Log.d("response", "submitted successfully")
                }

                override fun onFailure(call: Call<SignupModel>, t: Throwable) {
                    binding.progressbar.visibility = View.GONE
                    binding.video.isEnabled = true

                    Toast.makeText(
                        this@UploadVideoActivity,
                        "Network Error",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.progressbar.visibility = View.GONE
                    Log.d("response", t.toString())
                }

            })
        }else{
            MyApi().uploadVideo(
                MultipartBody.Part.createFormData(
                    "after_decoration",
                    file.name,
                    body
                ),
                RequestBody.create(MediaType.parse("text/plain"), id),
                RequestBody.create(MediaType.parse("text/plain"), btnId),
            ).enqueue(object : Callback<SignupModel> {
                override fun onResponse(
                    call: Call<SignupModel>,
                    response: Response<SignupModel>
                ) {
                    binding.video.isEnabled = true

                    Toast.makeText(
                        this@UploadVideoActivity,
                        "Successfull",
                        Toast.LENGTH_LONG
                    ).show()

                    sendLocation(id!!,decoratorId.toString(),latitude.toString(),longitude.toString(),leadname,4)

                    lifecycleScope.launch {
                        val resStatus =  viewModel.updateDecoratorLeadStatus(id!!,"4")
                        if(resStatus){
                            val res =  viewModel.sendMultipleFirebaseApiDecorator(id,"Lead Status","Decorator upload video after decoration")
                            if(res.toInt() == 1){
                                binding.progressbar.visibility = View.GONE
                                finish()
                            }
                        }
                    }

                    Log.d("response", "submitted successfully")
                }

                override fun onFailure(call: Call<SignupModel>, t: Throwable) {
                    binding.progressbar.visibility = View.GONE
                    binding.video.isEnabled = true

                    Toast.makeText(
                        this@UploadVideoActivity,
                        "Network Error",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.progressbar.visibility = View.GONE
                    Log.d("response", t.toString())
                }

            })
        }




    }

    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri,null,null,null,null)
        if (returnCursor!=null){
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }



    fun chooseImageGallery() {
        val intent = Intent()
        intent.setType("video/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),
            REQUEST_TAKE_GALLERY_VIDEO
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                selectedImageUri = data?.data
                binding.sizeoffile.text = selectedImageUri.toString()
//            val path = getPath(selectedImageUri)
                // OI FILE Manager
//               binding.video.setVideoURI(data?.data)
//                binding.video.start()
//                val videoCompressor = VideoCompressor()
//                videoCompressor.compressVideo(path!!, outputFilePath, outputWidth, outputHeight, bitRate, frameRate)
//                    uploadImage()
                // MEDIA GALLERY
//                var selectedImagePath = getPath(selectedImageUri)
//                if (selectedImagePath != null) {
//
//                }
            }
        }
    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery()
                }else{
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressbar.progress = percentage
    }

    fun sendLocation(id : String,decoratorId : String,lat : String ,long :String,leadname :String,leadStatus : Int){
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
                Toast.makeText(this@UploadVideoActivity,"Location id $latitude and $longitude",Toast.LENGTH_LONG).show()
                Log.d("locations","$latitude $longitude is the location")

            }
    }

}