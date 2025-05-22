package com.leonardowaked.unabbet

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onClickBack: () -> Unit = {}, onSuccessfulRegister: () -> Unit = {}) {
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
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onClickBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFF5EDDE),
                    )
                )
            }
        ) { innerPadding ->
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

                // Nombre
                OutlinedTextField(
                    value = inputName,
                    onValueChange = { inputName = it },
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
                        if (nameError.isNotEmpty()) {
                            Text(nameError, color = Color.Red)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Correo
                OutlinedTextField(
                    value = inputEmail,
                    onValueChange = { inputEmail = it },
                    modifier = fieldModifier,
                    label = { Text("Correo Electrónico", style = labelTextStyle) },
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
                            Text(emailError, color = Color.Red)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contraseña
                OutlinedTextField(
                    value = inputPassword,
                    onValueChange = { inputPassword = it },
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
                        if (passwordError.isNotEmpty()) {
                            Text(passwordError, color = Color.Red)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Repetir Contraseña
                OutlinedTextField(
                    value = inputPasswordConfirmation,
                    onValueChange = { inputPasswordConfirmation = it },
                    modifier = fieldModifier,
                    label = { Text("Repite la contraseña", style = labelTextStyle) },
                    shape = RoundedCornerShape(6.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    supportingText = {
                        if (passwordConfirmationError.isNotEmpty()) {
                            Text(passwordConfirmationError, color = Color.Red)
                        }
                    }
                )

                if (registerError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(registerError, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val isValidName = validateName(inputName).first
                        val isValidEmail = validateEmail(inputEmail).first
                        val isValidPassword = validatePassword(inputPassword).first
                        val isValidConfirmPassword = validateConfirmPassword(inputPassword, inputPasswordConfirmation).first

                        nameError = validateName(inputName).second
                        emailError = validateEmail(inputEmail).second
                        passwordError = validatePassword(inputPassword).second
                        passwordConfirmationError = validateConfirmPassword(inputPassword, inputPasswordConfirmation).second

                        if (isValidName && isValidPassword && isValidConfirmPassword && isValidEmail) {
                            auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                                .addOnCompleteListener(activity) { task ->
                                    if (task.isSuccessful) {
                                        val user = auth.currentUser
                                        val profileUpdates = userProfileChangeRequest {
                                            displayName = inputName
                                        }

                                        user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
                                            if (updateTask.isSuccessful) {
                                                println(" Nombre actualizado correctamente: ${inputName}")
                                                user.reload().addOnCompleteListener { reloadTask ->
                                                    if (reloadTask.isSuccessful) {
                                                        println(" Usuario recargado. Nombre actual: ${auth.currentUser?.displayName}")
                                                        onSuccessfulRegister()
                                                    } else {
                                                        registerError = "Error al recargar el usuario"
                                                        println(" Error al hacer reload: ${reloadTask.exception}")
                                                    }
                                                }
                                            } else {
                                                registerError = "Error al guardar el nombre"
                                                println(" Error en updateProfile: ${updateTask.exception}")
                                            }
                                        }
                                    } else {
                                        registerError = when (task.exception) {
                                            is FirebaseAuthInvalidCredentialsException -> "Correo inválido"
                                            is FirebaseAuthUserCollisionException -> "Correo ya registrado"
                                            else -> "Error al registrarse"
                                        }
                                        println(" Error en createUser: ${task.exception}")
                                    }
                                }
                        } else {
                            registerError = "Verifica los campos resaltados"
                        }
                    },
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

        // Footer
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
