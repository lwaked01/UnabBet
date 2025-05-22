package com.leonardowaked.unabbet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Preferimos esta por accesibilidad
// import androidx.compose.material.icons.filled.ArrowBack // ¡Esta importación es duplicada y se puede quitar!
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leonardowaked.unabbet.util.BalanceManager // ¡IMPORTANTE: Importar BalanceManager!

@Preview
@Composable
fun AccountScreen(
    onClickBack: () -> Unit = {},
    onClickLogOut: () -> Unit = {}
) {
    val auth = Firebase.auth
    var user by remember { mutableStateOf(auth.currentUser) }

    // Observar el saldo del usuario del BalanceManager
    val userBalance by BalanceManager.userBalance.collectAsState()

    LaunchedEffect(Unit) {
        auth.currentUser?.reload()?.addOnCompleteListener {
            if (it.isSuccessful) {
                user = auth.currentUser
            }
        }
    }

    val displayName = user?.displayName ?: ""
    val email = user?.email ?: ""


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5EDDE))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Usamos la versión de autocierre
                    contentDescription = "Volver",
                    tint = Color(0xFF7A1E1E),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {onClickBack()}
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Mi Perfil",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7A1E1E)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pasar el saldo dinámico a ProfileCard
            ProfileCard(displayName, email, userBalance)

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    onClickLogOut()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Cerrar sesión", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileCard(nombre: String, correo: String, saldo: Double) { // ¡saldo como parámetro!
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF7A1E1E), shape = RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {
        Text(
            text = "Nombre de usuario:",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = nombre,
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Correo electrónico:",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = correo,
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Saldo disponible:",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = "$%.2f".format(saldo), // ¡Ahora muestra el saldo dinámico!
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}