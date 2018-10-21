package com.jinwoo.dsmjumpup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class SignInActivity : AppCompatActivity() {

    private lateinit var mAuth :FirebaseAuth
    private lateinit var mGoogleApiClient: GoogleSignInClient
    private val RC_SIGN_IN = 1000
    lateinit var sign_in_btn: SignInButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        sign_in_btn = findViewById(R.id.sign_in)

        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleSignIn.getClient(this@SignInActivity, gso)

        sign_in_btn.setOnClickListener { v ->
            var signinIntent = mGoogleApiClient.signInIntent
            startActivityForResult(signinIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN) {
            var result: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            if(result.isSuccess) {
                var account: GoogleSignInAccount = result.signInAccount!!
                firebaseAuthWithGoogle(account)
            } else {
                Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        var credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken,null)

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, OnCompleteListener { task: Task<AuthResult> ->
            if(!task.isSuccessful) {
                Toast.makeText(this@SignInActivity, "Authentication 연동", Toast.LENGTH_SHORT).show()
            } else {
                goMainAcitivty()
            }
        })
    }

    private fun goMainAcitivty() {
        var intent = Intent(this@SignInActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
