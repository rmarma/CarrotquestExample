package ru.rma.apps.carrotquest.example

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.carrotquest_sdk.android.Carrot
import io.carrotquest_sdk.android.constants.SharedPreferenceKeys
import io.carrotquest_sdk.android.models.UserProperty
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
        Log.v(TAG, "setup")
        Carrot.setup(applicationContext, API_KEY, APP_ID, object : Carrot.Callback<Boolean> {

            override fun onResponse(result: Boolean?) {
                Log.v(TAG, "setup: onResponse: $result")
                if (result == true) {
                    Log.v(TAG, "auth")
                    Carrot.auth(userId, USER_AUTH_KEY, object : Carrot.Callback<Boolean> {
                        override fun onResponse(result: Boolean?) {
                            Log.v(TAG, "auth: onResponse: $result")
                            if (result == true) {
                                Carrot.setUserProperty(UserProperty("\$name", userName))
                            }
                        }

                        override fun onFailure(t: Throwable?) {
                            t?.printStackTrace()
                        }
                    })
                }
            }

            override fun onFailure(t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }

    private fun logOut() {
        Log.v(TAG, "logOut")
        Carrot.deInit()

        inputUserId.setText("")
        inputUserName.setText("")
    }

    private fun carrotPreferences() =
        applicationContext.getSharedPreferences(SharedPreferenceKeys.CARROT_PREF_NAME, Context.MODE_PRIVATE)

    private fun printUserFromPreferences() {
        with(carrotPreferences()) {
            Log.v(TAG, "CARROT_TOKEN: " + getString(SharedPreferenceKeys.CARROT_TOKEN, ""))
            Log.v(TAG, "CARROT_USER_ID: " + getString(SharedPreferenceKeys.CARROT_USER_ID, ""))
        }
    }
}
