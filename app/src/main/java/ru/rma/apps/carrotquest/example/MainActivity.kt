package ru.rma.apps.carrotquest.example

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.carrotquest_sdk.android.Carrot
import io.carrotquest_sdk.android.constants.SharedPreferenceKeys
import io.carrotquest_sdk.android.util.UserProperty
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View?) {
        when (v?.id ?: 0) {
            R.id.buttonLogIn -> logIn()
            R.id.buttonLogOut -> logOut()
            R.id.buttonLog -> printUserFromPreferences()
        }
    }

    private fun logIn() {
        val userId = inputUserId.text?.toString()?.trim() ?: ""
        val userName = inputUserName.text?.toString()?.trim() ?: ""

        layoutUserId.error = null
        layoutUserName.error = null

        if (userId.isEmpty()) {
            layoutUserId.error = getString(R.string.all_error_required_field)
            inputUserId.requestFocus()
            return
        }
        if (userName.isEmpty()) {
            layoutUserName.error = getString(R.string.all_error_required_field)
            inputUserName.requestFocus()
            return
        }

        Log.v(TAG, "logIn: $userId $userName")
        printUserFromPreferences()
        Log.v(TAG, "setup")
        Carrot.setup(applicationContext, API_KEY, APP_ID)
        printUserFromPreferences()
        Log.v(TAG, "auth")
        Carrot.auth(userId, USER_AUTH_KEY)
        printUserFromPreferences()
        Carrot.setUserProperty(UserProperty("\$name", userName))
    }

    private fun logOut() {
        Log.v(TAG, "logOut")
        Carrot.deInit()

        inputUserId.setText("")
        inputUserName.setText("")
    }

    private fun carrotPreferences() = applicationContext.getSharedPreferences(SharedPreferenceKeys.CARROT_PREF_NAME, Context.MODE_PRIVATE)

    private fun printUserFromPreferences() {
        carrotPreferences().let { pref ->
            Log.v(TAG, "CARROT_TOKEN: " + pref.getString(SharedPreferenceKeys.CARROT_TOKEN, ""))
            Log.v(TAG, "CARROT_USER_ID: " + pref.getString(SharedPreferenceKeys.CARROT_USER_ID, ""))
        }
    }
}
