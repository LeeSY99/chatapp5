package com.example.chatapp5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp5.databinding.ActivityLoginBinding
import com.example.chatapp5.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //인증
        auth= Firebase.auth

        //데이터베이스 초기화
        database = Firebase.database.reference


        binding.signupButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.pwInput.text.toString().trim()
            val name= binding.nameInput.text.toString().trim()
            signUp(email, password,name)
        }
    }
    private fun signUp(email:String,password:String,name:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"회원가입 성공",Toast.LENGTH_SHORT).show()
                    val intent: Intent = Intent(this@SignupActivity,MainActivity::class.java)
                    startActivity(intent)
                    addUserToDB(name,email,auth.currentUser?.uid!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"회원가입 실패",Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun addUserToDB(name: String,email: String,uId:String){
        database.child("user").child(uId).setValue(User(name,email,uId))
    }
}