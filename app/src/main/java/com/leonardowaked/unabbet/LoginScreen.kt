package com.leonardowaked.unabbet

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth


@Composable
fun LoginScreen(onClickRegister : ()->Unit = {},  onSuccessfulLogin: ()->Unit = {}) {

    var inputEmail by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("")}
    var passwordError by remember { mutableStateOf("")}


    val auth = Firebase.auth
    val activity = LocalView.current.context as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5EDDE))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF7A1E1E))
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "18+ Apuesta con responsabilidad",
                        color = Color(0xFFF5EDDE),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(R.drawable.logo_bet),
                    contentDescription = "Logo Unab",
                    modifier = Modifier.size(180.dp),

                )

                Spacer(modifier = Modifier.height(80.dp))


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .background(Color(0xFF7A1E1E), shape = RoundedCornerShape(8.dp))
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Iniciar sesion",
                        fontSize = 20.sp,
                        color = Color(0xFFF5EDDE),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = inputEmail,
                        onValueChange = {inputEmail = it},
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "Correo Electronico", color = Color(0xFFF5EDDE)) },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFFFFFFFF))
                        },
                        shape = RoundedCornerShape(12.dp),
                        supportingText = {
                            if (emailError.isNotEmpty()){
                                Text(
                                    text = emailError,
                                    color = Color.Red
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email
                    )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = inputPassword,
                        onValueChange = {inputPassword = it},
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Contraseña", color = Color(0xFFF5EDDE)) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFFFFFFF))
                        },
                        shape = RoundedCornerShape(12.dp),
                        supportingText = {
                            if (passwordError.isNotEmpty()){
                                Text(
                                    text = passwordError,
                                    color = Color.Red
                                )
                            }
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Password
                        )

                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (loginError.isNotEmpty()){
                        Text(
                            loginError,
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            val isValidEmail: Boolean = validateEmail(inputEmail).first

                            val isValidPassword: Boolean = validatePassword(inputPassword).first

                            emailError = validateEmail(inputEmail).second
                            passwordError = validatePassword(inputPassword).second

                            if (isValidEmail && isValidPassword) {
                                auth.signInWithEmailAndPassword(inputEmail, inputPassword)
                                    .addOnCompleteListener(activity) { task ->
                                        if (task.isSuccessful) {
                                            onSuccessfulLogin()
                                        } else {

                                            loginError = when (task.exception) {
                                                is FirebaseAuthInvalidCredentialsException -> "Correo o contrasea incorrecta"
                                                is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo"
                                                else -> "Error al iniciar sesion. Intenta de nuevo"
                                            }
                                        }
                                    }
                            } else {
                                1f
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF5EDDE),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Entrar")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = onClickRegister) {
                        Text("¿No tienes cuenta?", color = Color(0xFFF5EDDE))
                    }
                }
            }
        }
    }
}