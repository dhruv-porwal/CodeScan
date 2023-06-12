package com.example.codescanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Enter : AppCompatActivity() {
    private var sEmail = "MainActivity"
    private var sName = "MainActivity"
    private var sPassword = "MainActivity"


    private lateinit var tfEmail: TextView
    private lateinit var tfName: TextView
    private lateinit var tfpassword: TextView
    private lateinit var btnSignUp: Button
    private lateinit var btnSignIn: Button

    private lateinit var btnRecord: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_enter)
        var auth: FirebaseAuth
        tfName = findViewById(R.id.idEdtUserNameo)
        tfEmail = findViewById(R.id.emalob)
        tfpassword = findViewById(R.id.passo)
        btnSignIn = findViewById(R.id.btn_signin)
        // btnSignUp = findViewById(R.id.button_signup)
        btnRecord = findViewById(R.id.btnViewRecords)

// Initialize Firebase Auth



        btnSignIn.setOnClickListener {


            sEmail = tfEmail.text.toString().trim();
             sName = tfName.text.toString().trim();
            sPassword = tfpassword.text.toString().trim();

if(!sEmail.isEmpty() && !sName.isEmpty() && !sPassword.isEmpty()){
    auth = Firebase.auth
    auth.createUserWithEmailAndPassword(sEmail, sPassword)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                //     Log.d(TAG, "createUserWithEmail:success")
                val user = auth.currentUser
                updateUI(user)


            } else {
                // If sign in fails, display a message to the user.
                // Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()


                //updateUI(null)
            }
        }
}else{

    Toast.makeText(this,"All fields haven't been filled properly",  Toast.LENGTH_SHORT,).show()

}


        }


    }

    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(this,"Successfully added",  Toast.LENGTH_SHORT,).show()
val intent=Intent(this,MainActivity::class.java)
        intent.putExtra("username",tfName.text.toString())
        intent.putExtra("userEmail",tfEmail.text.toString())
        intent.putExtra("userPassword",tfpassword.text.toString())
startActivity(intent)







    }

}

