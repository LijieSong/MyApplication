package com.example.user.myapplication

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.user.utils.permission.MPermission
import com.example.user.utils.sys.Validator
import com.tencent.smtt.sdk.QbSdk
import com.example.user.utils.permission.annotation.OnMPermissionDenied
import com.example.user.utils.permission.annotation.OnMPermissionGranted


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private var mEmailView: AutoCompleteTextView? = null
    private var mPasswordView: EditText? = null
    private var mProgressView: View? = null
    private var mLoginFormView: View? = null
    private val BASIC_PERMISSION_REQUEST_CODE = 100

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        requestBasicPermission()
        QbSdk.initX5Environment(applicationContext,null)
        // Set up the login form.
        mEmailView = findViewById(R.id.email) as AutoCompleteTextView
        mPasswordView = findViewById(R.id.password) as EditText
        val mEmailSignInButton = findViewById(R.id.email_sign_in_button) as Button
        mEmailSignInButton.setOnClickListener { attemptLogin() }

        mLoginFormView = findViewById(R.id.login_form)
        mProgressView = findViewById(R.id.login_progress)

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        // Reset errors.
        mEmailView!!.error = null
        mPasswordView!!.error = null

        // Store values at the time of the login attempt.
        val email = mEmailView!!.text.toString()
        val password = mPasswordView!!.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView!!.error = getString(R.string.error_invalid_password)
            focusView = mPasswordView
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView!!.error = getString(R.string.error_field_required)
            focusView = mEmailView
            cancel = true
        } else if (!isEmailValid(email)) {
            mEmailView!!.error = getString(R.string.error_invalid_email)
            focusView = mEmailView
            cancel = true
        }

        if (cancel) {
            focusView!!.requestFocus()
        } else {
            showCheckDialog()
        }
    }

    private fun showCheckDialog() {
        val view = View.inflate(this, R.layout.login_check, null)
        val group = view.findViewById(R.id.rg_all) as RadioGroup
        val tv_ok = view.findViewById(R.id.tv_ok) as TextView
        val tv_cancel = view.findViewById(R.id.tv_cancel) as TextView
        var type: Int? = 1
        group.check(R.id.rb_image)
        group.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_image) {
                type = 1
            } else {
                type = 2
            }
        }
        val builder = AlertDialog.Builder(LoginActivity@ this)
        builder.setView(view)
        val dialog = builder.show()
        tv_ok.setOnClickListener {
            dialog.dismiss()
            if (type == 1) {
                startActivity(Intent(LoginActivity@ this, MainActivity::class.java))
            } else {
                startActivity(Intent(LoginActivity@ this, VideoActivity::class.java))
            }
            finish()
        }
        tv_cancel.setOnClickListener {
            dialog.dismiss()
            type = 1
        }
    }


    private fun isEmailValid(email: String): Boolean {
        return Validator.isEmailStr(email)
    }

    private fun isPasswordValid(password: String): Boolean {
        return Validator.isPassword(password)
    }

    /**
     * 基本权限管理
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun requestBasicPermission() {
        /**
         * Manifest.permission.RECORD_AUDIO,
         * Manifest.permission.ACCESS_COARSE_LOCATION,
         * Manifest.permission.ACCESS_FINE_LOCATION
         */
        MPermission.with(this@LoginActivity)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.READ_PHONE_STATE
                )
                .request()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }
}

