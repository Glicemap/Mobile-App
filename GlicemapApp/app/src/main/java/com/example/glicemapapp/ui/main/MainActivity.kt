package com.example.glicemapapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.glicemapapp.R
import com.example.glicemapapp.data.Repository
import com.example.glicemapapp.data.Result
import com.example.glicemapapp.data.models.Doctor
import com.example.glicemapapp.data.models.User
import com.example.glicemapapp.data.network.handleException
import com.example.glicemapapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val repository = Repository
    private lateinit var cpf: String
    var user: User? = null
    var doctor: Doctor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val  preferences = getSharedPreferences("login", MODE_PRIVATE)
        cpf = preferences.getString("document", "")!!



        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        binding.navView.setupWithNavController(navController)
        getUser()




    }

    private fun getUser() {
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.bringToFront()
        repository.getUser(cpf!!).observe(this) {
            binding.progressBar.visibility = View.INVISIBLE
            val result = it?.let { result ->
                when (result) {
                    is Result.Success -> {
                        binding.navView.selectedItemId=R.id.navigation_home
                        result.data?.let {
                            user = User(
                                it.user.documentNumber,
                                it.user.name,
                                it.user.lastName,
                                it.user.email,
                                it.user.password,
                                it.user.birthdate,
                                it.user.height,
                                it.user.weight,
                                it.user.sugarMin,
                                it.user.sugarMax
                            )
                            val preferences =
                                getSharedPreferences("userData", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            val gson = Gson()
                            val json = gson.toJson(User(
                                it.user.documentNumber,
                                it.user.name,
                                it.user.lastName,
                                it.user.email,
                                it.user.password,
                                it.user.birthdate,
                                it.user.height,
                                it.user.weight,
                                it.user.sugarMin,
                                it.user.sugarMax
                            ))
                            editor.putString("user", json);
                            editor.apply()
                            doctor = Doctor(
                                it.medic.documentNumber,
                                it.medic.name,
                                it.medic.email
                            )

                            val json2 = gson.toJson(Doctor(
                                it.medic.documentNumber,
                                it.medic.name,
                                it.medic.email
                            ))
                            editor.putString("doctor", json2);
                            editor.apply()
                            true
                        } ?: false

                    }
                    is Result.Error -> {
                        val preferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
                        val gson = Gson()
                        val json = preferences.getString("user", "")
                        user = gson.fromJson(json, User::class.java)
                        val json2 = preferences.getString("doctor", "")
                        doctor = gson.fromJson(json2, Doctor::class.java)
                        Snackbar.make(
                            binding.root,
                            handleException(result.exception.message.toString()),
                            Snackbar.LENGTH_LONG
                        ).show()
                        false
                    }
                }
            }

        }
    }
}