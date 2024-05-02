package com.example.managerapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.databinding.ActivityAddLeadImagesBinding
import com.example.managerapp.model.SignupModel
import com.example.managerapp.network.MyApi
import com.example.managerapp.view.fragment.manager.UploadRequestBody
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AddLeadImagesActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {


    lateinit var binding: ActivityAddLeadImagesBinding

    private var selectedImageUri: Uri? = null
    var id = ""
    var name = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddLeadImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.progressbar.visibility = View.GONE

        id = intent.getStringExtra("id").toString()
//        name = intent.getStringExtra("name").toString()

        Toast.makeText(
            this@AddLeadImagesActivity,
            id.toString(),
            Toast.LENGTH_LONG
        ).show()



        binding.getImageSubmitButton.setOnClickListener {
            opeinImageChooser()
        }

        binding.submitButton.setOnClickListener {

            if(selectedImageUri == null){
                Toast.makeText(
                    this@AddLeadImagesActivity,
                    "Please Select Image",
                    Toast.LENGTH_LONG
                ).show()
            }else{
                binding.progressbar.visibility = View.VISIBLE
                uploadImage()
            }

        }


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


        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            selectedImageUri!!, "r", null
        ) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir,contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file,"image",this)
//        binding.progressbar.progress = 0

            MyApi().uploadImage(
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
                        this@AddLeadImagesActivity,
                        "Successfull",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                    binding.progressbar.visibility = View.GONE

                    Log.d("response", "submitted successfully")
                }

                override fun onFailure(call: Call<SignupModel>, t: Throwable) {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(
                        this@AddLeadImagesActivity,
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


}