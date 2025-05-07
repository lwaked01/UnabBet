package com.leonardowaked.unabbet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun BetScreen() {
    var homeScore by remember { mutableStateOf(2) }
    var awayScore by remember { mutableStateOf(2) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7A1E1E))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            TopBar()

            Spacer(modifier = Modifier.height(16.dp))


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bucaramanga - Santa Fe",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "2 - 1",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "65:29",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFFF5EDDE), shape = RoundedCornerShape(20.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tu apuesta:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ScoreBox(homeScore) { homeScore = it }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "-", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    ScoreBox(awayScore) { awayScore = it }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Este resultado no está disponible",
                    fontSize = 12.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A1E1E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fijar Apuesta", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A1E1E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cantidad A Apostar", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "PAGO POTENCIAL:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 24.dp)
                            .background(Color(0xFF7A1E1E))
                    )
                }
            }
        }


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black)
                .padding(10.dp),
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
fun ScoreBox(score: Int, onScoreChange: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .border(1.dp, Color.Black)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = score.toString(), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5EDDE))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { }) {
            Icon(Icons.Default.Menu, contentDescription = "Menu")
        }

        Image(
            painter = painterResource(id = R.drawable.logo_bet),
            contentDescription = "UnabBet",
            modifier = Modifier.size(40.dp)
        )

        Column(horizontalAlignment = Alignment.End) {
            Text(text = "$100.000", fontWeight = FontWeight.Bold)
            Text(text = "Cajero", fontSize = 12.sp)
        }

        Text(
            text = "Cuenta",
            modifier = Modifier.padding(start = 8.dp),
            fontWeight = FontWeight.Bold
        )
    }
}