package com.example.managerapp.view.fragment.attendance

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.managerapp.databinding.ActivityLoginUserImageBinding
import com.example.managerapp.model.SignupModel
import com.example.managerapp.network.MyApi
import com.example.managerapp.utils.Prefs
import com.example.managerapp.view.fragment.decorator.CashImageActivity
import com.example.managerapp.view.fragment.manager.UploadRequestBody
import com.example.managerapp.viewModel.RegisterViewModel
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class LoginUserImageActivity : AppCompatActivity(), UploadRequestBody.UploadCallback  {

    lateinit var binding: ActivityLoginUserImageBinding

    private var selectedImageUri: Uri? = null
    lateinit var viewModel: RegisterViewModel

    private lateinit var imageCapture: ImageCapture

    var id = ""
    var name = ""
    var choose_id = ""
    var login_status = ""

    val REQUEST_CODE_PERMISSIONS = 121

    var managerId = 0
    var loginStatusId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUserImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.progressbar.visibility = View.GONE


        choose_id = intent.getStringExtra("choose_id").toString()
        login_status = intent.getStringExtra("login_status").toString()

        if(choose_id.toInt() == 1){
            managerId = Prefs.getManagerId()
            loginStatusId = Prefs.getManagerLoginStatusId()
        }
        else if(choose_id.toInt() == 2){
            managerId = Prefs.getTeamLeaderId()
            loginStatusId = Prefs.getTeamLeaderLoginStatusId()
        }
        else if(choose_id.toInt() == 3){
            managerId = Prefs.getDecoratorId()
            loginStatusId = Prefs.getDecoratorLoginStatusId()
        }

        if (allPermissionsGranted()) {

        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS
            )
        }


        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.getImageSubmitButton.setOnClickListener {
            openCamera()
        }

        binding.submitButton.setOnClickListener {
            if(selectedImageUri == null){
                Toast.makeText(
                    this@LoginUserImageActivity,
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


    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        selectedImageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri)
        cameraActivityResultLauncher.launch(cameraIntent)
    }

    private var cameraActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            if (it.resultCode === RESULT_OK) {
                binding.imgpreview.setImageURI(selectedImageUri);
            }
        }
    )


    @SuppressLint("SuspiciousIndentation")
    private fun uploadImage() {

        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            selectedImageUri!!, "r", null
        ) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image", this)
        if(login_status.toInt() == 1){
            MyApi().uploadLoginStatusImage(
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    body
                ),
                RequestBody.create(MediaType.parse("text/plain"), managerId.toString()),
                RequestBody.create(MediaType.parse("text/plain"), loginStatusId.toString()),
                RequestBody.create(MediaType.parse("text/plain"), choose_id),
                RequestBody.create(MediaType.parse("text/plain"), "1"),
                RequestBody.create(MediaType.parse("text/plain"), "0")
            ).enqueue(object : Callback<SignupModel> {
                override fun onResponse(
                    call: Call<SignupModel>,
                    response: Response<SignupModel>
                ) {
                    Toast.makeText(
                        this@LoginUserImageActivity,
                        "Successfull",
                        Toast.LENGTH_LONG
                    ).show()

                    binding.progressbar.visibility = View.GONE

                    response.body()?.message?.toInt()?.let { Prefs.setManagerLoginImageId(it) }
                    finish()


                    Log.d("response", "submitted successfully")
                }

                override fun onFailure(call: Call<SignupModel>, t: Throwable) {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(
                        this@LoginUserImageActivity,
                        t.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
        }else if(login_status.toInt() == 0){
            var updateId = Prefs.getManagerLoginImageId()
            MyApi().logoutStatusImage(
                MultipartBody.Part.createFormData(
                    "image2",
                    file.name,
                    body
                ),
                RequestBody.create(MediaType.parse("text/plain"), choose_id),
                RequestBody.create(MediaType.parse("text/plain"), updateId.toString()),
                RequestBody.create(MediaType.parse("text/plain"), "22"),
            ).enqueue(object : Callback<SignupModel> {
                override fun onResponse(
                    call: Call<SignupModel>,
                    response: Response<SignupModel>
                ) {
                    Toast.makeText(
                        this@LoginUserImageActivity,
                        "Successfull",
                        Toast.LENGTH_LONG
                    ).show()


                    binding.progressbar.visibility = View.GONE
                    finish()

                    Log.d("response", "submitted successfully")
                }

                override fun onFailure(call: Call<SignupModel>, t: Throwable) {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(
                        this@LoginUserImageActivity,
                        t.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
        }



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

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        applicationContext, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    override fun onProgressUpdate(percentage: Int) {
        binding.progressbar.progress = percentage
    }




}