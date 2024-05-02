package com.example.managerapp.utils

import android.content.Context
import android.content.SharedPreferences

object Prefs {

    private lateinit var prefs: SharedPreferences

    private const val SURPRISE = "SurpriseVille"
    private const val USERNAME = "username"
    private const val PROFILE = "profile"
    private const val LATITUDE = "latitude"
    private const val LONGITUDE = "longitude"
    private const val TEAMlEADERID = "teamleaderId"
    private const val MANAGERID = "managerId"
    private const val DECORATORID = "decoratorId"
    private const val MANAGERTOKEN = "mangerToken"
    private const val TEAMLEADERTOKEN = "teamLeaderToken"
    private const val DECORATORTOKEN = "decoratorToken"
    private const val DECORATOREMAIL = "decoratorEmail"
    private const val MANAGEREMAIL = "managerEmail"
    private const val ADMINEMAIL = "adminEmail"
    private const val TEAMLEADEREMAIL = "teamLeaderEmail"

    private const val MANAGERLOGINSTATUS = "managerLoginStatus"
    private const val TEAMlEADERLOGINSTATUS = "teamleaderLoginStatus"
    private const val DECORATORLOGINSTATUS = "decoratorLoginStatus"

    private const val MANAGERLOGINSTATUSID = "managerLoginStatusId"
    private const val TEAMlEADERLOGINSTATUSID = "teamleaderLoginStatusId"
    private const val DECORATORLOGINSTATUSID = "decoratorLoginStatusId"

    private const val MANAGERLOGINIMAGEID = "managerLoginImageId"
    private const val TEAMlEADERLOGINIMAGEID = "teamleaderLoginImageId"
    private const val DECORATORLOGINIMAGEID = "decoratorLoginImageId"



    fun init(context: Context) {
        prefs = context.getSharedPreferences(SURPRISE, Context.MODE_PRIVATE)

    }


    fun setUsername(username: String) {
        prefs.edit().putString(USERNAME, username).apply()
    }

    fun getUsername(): String {
        return prefs.getString(USERNAME, "") ?: ""
    }


    fun setMangerToken(username: String) {
        prefs.edit().putString(MANAGERTOKEN, username).apply()
    }

    fun getManagerToken(): String {
        return prefs.getString(MANAGERTOKEN, "") ?: ""
    }

    fun setTeamLeaderToken(username: String) {
        prefs.edit().putString(TEAMLEADERTOKEN, username).apply()
    }

    fun getTeamLeaderToken(): String {
        return prefs.getString(TEAMLEADERTOKEN, "") ?: ""
    }

    fun setDecoratorToken(username: String) {
        prefs.edit().putString(DECORATORTOKEN, username).apply()
    }

    fun getDecoratorToken(): String {
        return prefs.getString(DECORATORTOKEN, "") ?: ""
    }


    fun setManagerEmail(username: String) {
        prefs.edit().putString(MANAGEREMAIL, username).apply()
    }

    fun getManagerEmail(): String {
        return prefs.getString(MANAGEREMAIL, "") ?: ""
    }

    fun setAdminEmail(username: String) {
        prefs.edit().putString(ADMINEMAIL, username).apply()
    }

    fun getAdminEmail(): String {
        return prefs.getString(ADMINEMAIL, "") ?: ""
    }

    fun setTeamLeaderEmail(username: String) {
        prefs.edit().putString(TEAMLEADEREMAIL, username).apply()
    }

    fun getTeamLeaderEmail(): String {
        return prefs.getString(TEAMLEADEREMAIL, "") ?: ""
    }

    fun setDecoratorEmail(username: String) {
        prefs.edit().putString(DECORATOREMAIL, username).apply()
    }

    fun getDecoratorEmail(): String {
        return prefs.getString(DECORATOREMAIL, "") ?: ""
    }


    fun setLatitude(latitude: String) {
        prefs.edit().putString(LATITUDE, latitude).apply()
    }

    fun getLatitude(): String {
        return prefs.getString(LATITUDE, "") ?: ""
    }


    fun setLongitude(latitude: String) {
        prefs.edit().putString(LONGITUDE, latitude).apply()
    }

    fun getLongitude(): String {
        return prefs.getString(LONGITUDE, "") ?: ""
    }



    fun setTeamLeaderId(profile: Int) {
        prefs.edit().putInt(TEAMlEADERID, profile).apply()
    }

    fun getTeamLeaderId(): Int {
        return prefs.getInt(TEAMlEADERID, 0)
    }

    fun setDecoratorId(profile: Int) {
        prefs.edit().putInt(DECORATORID, profile).apply()
    }

    fun getDecoratorId(): Int {
        return prefs.getInt(DECORATORID, 0)
    }

    fun setManagerId(profile: Int) {
        prefs.edit().putInt(MANAGERID, profile).apply()
    }

    fun getManagerId(): Int {
        return prefs.getInt(MANAGERID, 0)
    }

    fun managerIsLoggedIn(): Boolean {
        return getManagerToken().isNotEmpty()
    }

    fun teamLeaderIsLoggedIn(): Boolean {
        return getTeamLeaderToken().isNotEmpty()
    }

    fun adminIsLoggedIn(): Boolean {
        return getAdminEmail().isNotEmpty()
    }

    fun decoratorIsLoggedIn(): Boolean {
        return getDecoratorToken().isNotEmpty()
    }



    fun setManagerLoginStatus(profile: Int) {
        prefs.edit().putInt(MANAGERLOGINSTATUS, profile).apply()
    }

    fun getManagerLoginStatus(): Int {
        return prefs.getInt(MANAGERLOGINSTATUS, 0)
    }

    fun setManagerLoginStatusId(profile: Int) {
        prefs.edit().putInt(MANAGERLOGINSTATUSID, profile).apply()
    }

    fun getManagerLoginStatusId(): Int {
        return prefs.getInt(MANAGERLOGINSTATUSID, 0)
    }

    fun setTeamLeaderLoginStatusId(profile: Int) {
        prefs.edit().putInt(TEAMlEADERLOGINSTATUSID, profile).apply()
    }

    fun getTeamLeaderLoginStatusId(): Int {
        return prefs.getInt(TEAMlEADERLOGINSTATUSID, 0)
    }
    fun setDecoratorLoginStatusId(profile: Int) {
        prefs.edit().putInt(DECORATORLOGINSTATUSID, profile).apply()
    }

    fun getDecoratorLoginStatusId(): Int {
        return prefs.getInt(DECORATORLOGINSTATUSID, 0)
    }


    fun setManagerLoginImageId(profile: Int) {
        prefs.edit().putInt(MANAGERLOGINIMAGEID, profile).apply()
    }

    fun getManagerLoginImageId(): Int {
        return prefs.getInt(MANAGERLOGINIMAGEID, 0)
    }

    fun setTeamLeaderLoginStatus(profile: Int) {
        prefs.edit().putInt(TEAMlEADERLOGINSTATUS, profile).apply()
    }

    fun getTeamLeaderLoginStatus(): Int {
        return prefs.getInt(TEAMlEADERLOGINSTATUS, 0)
    }

    fun setDecoratorLoginStatus(profile: Int) {
        prefs.edit().putInt(DECORATORLOGINSTATUS, profile).apply()
    }

    fun getDecoratorLoginStatus(): Int {
        return prefs.getInt(DECORATORLOGINSTATUS, 0)
    }


    fun logoutManager(){
        prefs.edit().remove(MANAGEREMAIL)
        prefs.edit().remove(MANAGERTOKEN)
        prefs.edit().remove(MANAGERID)
        prefs.edit().clear()
        prefs.edit().commit()


    }
    fun logoutTeamLeader(){
        prefs.edit().remove(TEAMLEADEREMAIL)
        prefs.edit().remove(TEAMLEADERTOKEN)
        prefs.edit().remove(TEAMlEADERID)
        prefs.edit().commit()
    }

    fun clearall(context:Context){
        val  sp :SharedPreferences = context.getSharedPreferences(SURPRISE, Context.MODE_PRIVATE)
        val editor=sp.edit()
        editor.clear()
        editor.apply()
        editor.commit()
    }

    fun logoutDecorator(){
        prefs.edit().remove(DECORATOREMAIL)
        prefs.edit().remove(DECORATORTOKEN)
        prefs.edit().remove(DECORATORID)
        prefs.edit().commit()

    }

//    fun addMeterCount(id: String?) {
//        prefs.edit().putInt(id, getMeter(id) + 1).apply()
//    }
//
//    fun getMeter(id: String?): Int {
//        return prefs.getInt(id, 0)
//    }

//    fun resetMeter(id: String?) {
//        prefs.edit().putInt(id, 0).apply()
//    }
//
//    fun setMeterResetDate(id: String) {
//        prefs.edit().putLong("$id/reset", System.currentTimeMillis()).apply()
//    }
//
//    fun getMeterResetDate(id: String): Long {
//        return prefs.getLong("$id/reset", 0)
//    }
}