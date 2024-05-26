package com.devmasterteam.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.helper.BiometricHelper
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var securityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = ActivityLoginBinding.inflate(layoutInflater)
        securityPreferences = SecurityPreferences(this)

        // Layout
        setContentView(binding.root)

        //Biometric

        //action bar
        supportActionBar?.hide()

        // Eventos
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)

        // Observadores
        observe()

    }

    // Verifica se usuário está logado
    override fun onResume() {
        super.onResume()
        viewModel.getPriorities()
        viewModel.verifyLoggedUser()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            handleLogin()
        } else if (v.id == R.id.text_register) {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun handleLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.ERROR_NOT_BLANK, Toast.LENGTH_LONG).show()

        } else viewModel.doLogin(email, password)
    }

    private fun observe() {
        viewModel.login.observe(this, Observer {
            it?.let {
                if (it.status) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()

                } else {
                    binding.editEmail.text.clear()
                    binding.editPassword.text.clear()
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.islogged.observe(this, Observer {
            if (it.status) {
                handleBiometric()
                binding.editEmail.text.insert(0, it.message)

            } else {
                binding.editEmail.text.insert(0, it.message)
            }
        })
    }


    private fun handleBiometric() {

        biometricAuthentication()

    }

    private fun biometricAuthentication() {
        /*Thread usada*/
        val executor = ContextCompat.getMainExecutor(this)

        /*Logica do prompt*/
        val bio =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                    super.onAuthenticationSucceeded(result)
                }
            })

        /*Layout*/
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticar com biometria")
            .setNegativeButtonText("Digitar a senha")
            .build()

        bio.authenticate(info)
    }

}