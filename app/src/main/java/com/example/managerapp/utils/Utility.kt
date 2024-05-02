package com.example.managerapp.utils

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.managerapp.R
import com.example.managerapp.view.fragment.notification.NotificationFragment

object Utility {
    fun changeFragmentWithData(fargment : Fragment,fragmentActivity: FragmentActivity, bundle: Bundle){

        val updateFragment = fargment
        val transaction=fragmentActivity.supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.silde_in, R.anim.slide_out)
        updateFragment.arguments = bundle
        transaction.replace(R.id.fragment_container,updateFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    fun changeFragmentWithNotificationData(fargment : Fragment,fragmentActivity: FragmentActivity, bundle: Bundle){

        val updateFragment = fargment
        val transaction=fragmentActivity.supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_down, R.anim.fade_out)
        updateFragment.arguments = bundle
        transaction.replace(R.id.fragment_container,updateFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }


    fun changeFragment(fragment :Fragment , fragmentActivity: FragmentActivity){
        val addLeadFragment = fragment
        val transaction=fragmentActivity.supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        transaction.replace(R.id.fragment_container,addLeadFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}