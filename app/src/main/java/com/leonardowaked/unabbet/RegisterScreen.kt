package com.leonardowaked.unabbet

import android.R.attr.contentDescription
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun RegisterScreen(onClickBack :()-> Unit = {}, onSuccessfulRegister:()-> Unit = {}) {

    val auth = Firebase.auth

    val activity = LocalView.current.context as Activity

    var inputName by remember { mutableStateOf("") }
    var inputEmail by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var inputPasswordConfirmation by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var passwordConfirmationError by remember { mutableStateOf("") }

    var registerError by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5EDDE))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(imageVector =  Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "icon register")
                    }
                },  colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5EDDE),
                )
            )},
            modifier = Modifier.fillMaxSize(),
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_bet),
                        contentDescription = "Logo UnabBet",
                        modifier = Modifier.size(180.dp)
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    Text(
                        text = "Registro",
                        fontSize = 24.sp,
                        color = Color(0xFF7A1E1E),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    val fieldModifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(6.dp))

                    val labelTextStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = inputName,
                        onValueChange = {inputName = it},
                        modifier = fieldModifier,
                        label = { Text("Usuario", style = labelTextStyle) },
                        shape = RoundedCornerShape(6.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        supportingText = {
                            if (emailError.isNotEmpty()) {
                                Text(
                                    text = emailError,
                                    color = Color.Red
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = inputEmail,
                        onValueChange = {inputEmail = it},
                        modifier = fieldModifier,
                        label = { Text("Correo Electronico", style = labelTextStyle) },
                        shape = RoundedCornerShape(6.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        supportingText = {
                            if (emailError.isNotEmpty()) {
                                Text(
                                    text = emailError,
                                    color = Color.Red
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = inputPassword,
                        onValueChange = {inputPassword = it},
                        modifier = fieldModifier,
                        label = { Text("Contraseña", style = labelTextStyle) },
                        shape = RoundedCornerShape(6.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        supportingText = {
                            if (emailError.isNotEmpty()) {
                                Text(
                                    text = emailError,
                                    color = Color.Red
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = inputPasswordConfirmation,
                        onValueChange = {inputPasswordConfirmation = it},
                        modifier = fieldModifier,
                        label = { Text("Repita la contraseña", style = labelTextStyle) },
                        shape = RoundedCornerShape(6.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        supportingText = {
                            if (emailError.isNotEmpty()) {
                                Text(
                                    text = emailError,
                                    color = Color.Red
                                )
                            }
                        }
                    )

                    if (registerError.isNotEmpty()){
                        Text(registerError, color = Color.Red)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {val isValidName = validateName(inputName).first
                            val isValidEmail = validateEmail(inputEmail).first
                            val isValidPassword = validatePassword(inputPassword).first
                            val isValidConfirmPassword = validateConfirmPassword(inputPassword, inputPasswordConfirmation).first

                            nameError = validateName(inputName).second
                            emailError = validateEmail(inputEmail).second
                            passwordError = validatePassword(inputPassword).second
                            passwordConfirmationError = validateConfirmPassword(inputPassword, inputPasswordConfirmation).second

                            if (isValidName && isValidPassword && isValidConfirmPassword && isValidEmail){
                                auth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(activity){task ->
                                    if (task.isSuccessful){
                                        onSuccessfulRegister()
                                    }else{
                                        registerError = when(task.isSuccessful){
                                            is FirebaseAuthInvalidCredentialsException -> "Correo invalido"
                                            is FirebaseAuthUserCollisionException -> "Correo ya registrado"
                                            else -> "Error al registrarse"
                                        }
                                    }
                                }

                            }else {
                                registerError = "Hubo un error en el register"
                            } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7A1E1E),
                            contentColor = Color(0xFFF5EDDE)
                        )
                    ) {
                        Text("Continuar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFF7A1E1E))
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(Color.Black, shape = RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("18+", color = Color(0xFFF5EDDE), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Apuesta con responsabilidad",
                color = Color(0xFFF5EDDE),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}