package com.example.managerapp.view.fragment.manager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.managerapp.AddLeadImagesActivity
import com.example.managerapp.R
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ViewLeadImagesFragment : Fragment() {

    lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_view_lead_images, container, false)

        val headerTitle = view.findViewById<TextView>(R.id.header_title)
        headerTitle.text = "View Lead Image"

        val img1 = view.findViewById<ImageView>(R.id.img1)
        val img2 = view.findViewById<ImageView>(R.id.img2)
        val img3 = view.findViewById<ImageView>(R.id.img3)
        val img4 = view.findViewById<ImageView>(R.id.img4)
        val nodata = view.findViewById<TextView>(R.id.nodata)
        val addImagebtn = view.findViewById<MaterialButton>(R.id.add_image)
        val progresbar = view.findViewById<ProgressBar>(R.id.progressbar)

        val id = arguments?.getString("id")
        val verify = arguments?.getString("verify")
//        Toast.makeText(requireContext(),id, Toast.LENGTH_LONG).show()


        progresbar.visibility = View.VISIBLE
        img1.visibility = View.GONE
        img2.visibility = View.GONE
        img3.visibility = View.GONE
        img4.visibility = View.GONE
        nodata.visibility = View.GONE
        addImagebtn.visibility = View.GONE



        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            var imagesdata = id?.let { viewModel.getImageLeadById(it) }

            withContext(Dispatchers.Main){
                if(imagesdata?.size!! > 0){
                    progresbar.visibility = View.GONE



                    if(imagesdata?.size!! == 1 ){

                        img1.visibility = View.VISIBLE
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/images/"+ imagesdata?.get(0)?.image).into(img1);
                        addImagebtn.visibility = View.VISIBLE
                    }else if(imagesdata?.size!! == 2 ){

                        img1.visibility = View.VISIBLE
                        img2.visibility = View.VISIBLE
                        addImagebtn.visibility = View.VISIBLE

                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/images/"+ imagesdata?.get(0)?.image).into(img1);
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/images/"+ imagesdata?.get(1)?.image).into(img2);

                    }else if(imagesdata?.size!! == 3 ){

                        img1.visibility = View.VISIBLE
                        img2.visibility = View.VISIBLE
                        img3.visibility = View.VISIBLE
                        addImagebtn.visibility = View.VISIBLE

                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/images/"+ imagesdata?.get(0)?.image).into(img1);
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/images/"+ imagesdata?.get(1)?.image).into(img2);
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/images/"+ imagesdata?.get(2)?.image).into(img3);

                    }else if(imagesdata?.size!! == 4 ){

                        img1.visibility = View.VISIBLE
                        img2.visibility = View.VISIBLE
                        img3.visibility = View.VISIBLE
                        img4.visibility = View.VISIBLE
                        addImagebtn.visibility = View.GONE
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/images/"+ imagesdata?.get(0)?.image).into(img1);
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/images/"+ imagesdata?.get(1)?.image).into(img2);
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/images/"+ imagesdata?.get(2)?.image).into(img3);
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/images/"+ imagesdata?.get(3)?.image).into(img4);

                    }

//                    Toast.makeText(requireContext(),imagesdata.size.toString() + "ok now", Toast.LENGTH_LONG).show()

                }else{
                    nodata.visibility = View.VISIBLE
                    progresbar.visibility = View.GONE
                    addImagebtn.visibility = View.VISIBLE
                    Toast.makeText(requireContext(),"No Data Available", Toast.LENGTH_LONG).show()
                }
                if(verify!!.toInt() == 1){
                    addImagebtn.visibility = View.VISIBLE
                }else{
                    addImagebtn.visibility = View.GONE
                }
            }
        }



        addImagebtn.setOnClickListener {
            val intent = Intent(requireContext(),AddLeadImagesActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("id",id)
            startActivity(intent)
        }





        return view
    }

}