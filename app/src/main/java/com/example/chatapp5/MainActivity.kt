package com.example.chatapp5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp5.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object FirebaseManager {
    private var auth: FirebaseAuth? = null
    private var database: DatabaseReference? = null

    fun initialize() {
        auth = Firebase.auth
        database = Firebase.database.reference
    }

    fun getAuth(): FirebaseAuth {
        return auth ?: throw IllegalStateException("FirebaseManager is not initialized.")
    }

    fun getDatabase(): DatabaseReference {
        return database ?: throw IllegalStateException("FirebaseManager is not initialized.")
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var userList: ArrayList<User>

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private fun initializeFirebase() {
        FirebaseManager.initialize()
        auth = FirebaseManager.getAuth()
        database = FirebaseManager.getDatabase()
    }

    private fun setupRecyclerView() {
        userList = ArrayList()
        adapter = UserAdapter(this, userList)
        binding.userRecycleView.layoutManager = LinearLayoutManager(this)
        binding.userRecycleView.adapter = adapter
    }

    private fun loadUsersFromDatabase() {
        val currentUserUid = auth.currentUser?.uid

        database.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val cUser = postSnapshot.getValue(User::class.java)
                    if (currentUserUid != cUser?.uid) {
                        Log.d(
                            "UID",
                            "Current User UID: $currentUserUid, cUser UID: ${cUser?.uid}"
                        )
                        userList.add(cUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 오류 발생시
            }
        })
    }

    private fun logout() {
        val auth = FirebaseManager.getAuth()
        auth.signOut()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupOptionsMenu(menu: Menu?) {
        menuInflater.inflate(R.menu.menu, menu)
    }

    private fun handleOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.log_out -> {
                logout()
                true
            }
            else -> true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeFirebase()
        setupRecyclerView()
        loadUsersFromDatabase()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setupOptionsMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return handleOptionsItemSelected(item)
    }
}
