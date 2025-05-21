package com.leonardowaked.unabbet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5EDDE))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* Abre el Drawer si lo usas */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menú")
                }

                // Botón para el LOGO
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_bet),
                        contentDescription = "UnabBet",
                        modifier = Modifier.size(40.dp)
                    )
                }

                // Botón para el dinero + Cajero
                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "$100.000", fontWeight = FontWeight.Bold)
                        Text(text = "Cajero", fontSize = 12.sp)
                    }
                }

                // Botón para el texto "Cuenta"
                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Cuenta",
                        fontWeight = FontWeight.Bold
                    )
                }
            }






            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF7A1E1E))
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "En Vivo",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        println("En Vivo clickeado")
                        // Acción de En Vivo
                    }
                )

                Text(
                    text = "Próximos",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        println("Próximos clickeado")
                        // Acción de Próximos
                    }
                )

                Text(
                    text = "Competiciones",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        println("Competiciones clickeado")
                        // Acción de Competiciones
                    }
                )
            }



            Image(
                painter = painterResource(id = R.drawable.banner),
                contentDescription = "Football Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(46.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFF7A1E1E), shape = RoundedCornerShape(20.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Partidos en vivo",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                val partidos = listOf(
                    Triple("Bucaramanga\nSanta Fe", "65'", "Apostar"),
                    Triple("Nacional\nEnvigado", "20'", "Apostar"),
                    Triple("Real Madrid\nMillonarios", "60'", "Apostar"),
                    Triple("Juventus\nMilan", "83'", "Apostar")
                )

                partidos.forEach { (equipos, minuto, boton) ->
                    PartidoCard(equipos, minuto, boton)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }


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
                Text("18+", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Apuesta con responsabilidad",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

        }
    }
}


@Composable
fun PartidoCard(equipos: String, minuto: String, botonTexto: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5EDDE), shape = RoundedCornerShape(6.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = equipos,
            color = Color.Black,
            fontSize = 14.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = minuto, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A1E1E)),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = botonTexto, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}