package ru.rma.apps.carrotquest.example

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.carrotquest_sdk.android.Carrot
import io.carrotquest_sdk.android.util.UserProperty
import kotlinx.android.synthetic.main.activity_main.*

private const val APP_ID = "Enter your APP_ID"
private const val API_KEY = "Enter your API_KEY"
private const val USER_AUTH_KEY = "Enter your USER_AUTH_KEY"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View?) {
        when (v?.id ?: 0) {
            R.id.buttonLogIn -> logIn()
            R.id.buttonLogOut -> logOut()
        }
    }

    private fun logIn() {
        val userId = inputUserId.text?.toString()?.trim() ?: ""
        val userName = inputUserName.text?.toString()?.trim() ?: ""

        layoutUserId.error = null
        layoutUserName.error = null

        if (userId.isEmpty()) {
            layoutUserId.error = getString(R.string.all_error_required_field)
            return
        }
        if (userName.isEmpty()) {
            layoutUserName.error = getString(R.string.all_error_required_field)
            return
        }

        Carrot.setup(applicationContext, API_KEY, APP_ID)
        Carrot.auth(userId, USER_AUTH_KEY)
        Carrot.setUserProperty(UserProperty("\$name", userName))
    }

    private fun logOut() {
        Carrot.deInit()

        inputUserId.setText("")
        inputUserName.setText("")
    }
}
