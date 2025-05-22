package com.leonardowaked.unabbet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leonardowaked.unabbet.util.BetHistoryManager // Importa el gestor
import com.leonardowaked.unabbet.util.UserBet // <-- IMPORTANTE: Ahora importamos UserBet

// Preview para visualizar la pantalla en el editor
@Preview(showBackground = true)
@Composable
fun BetHistoryScreenPreview() {
    BetHistoryScreen(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetHistoryScreen(navController: NavController) {
    // Observa la lista de apuestas (UserBet) del BetHistoryManager.
    val bets by BetHistoryManager.bets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Apuestas", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF7A1E1E))
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF7A1E1E))
        ) {
            if (bets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No tienes apuestas realizadas aún.",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(bets) { userBet -> // <-- Usa userBet aquí en el iterador
                        BetHistoryCard(userBet = userBet) // <-- Pasa userBet a la tarjeta
                    }
                }
            }
        }
    }
}

// Composable para mostrar los detalles de una apuesta en una tarjeta
@Composable
fun BetHistoryCard(userBet: UserBet) { // <-- CAMBIO AQUÍ: de bet: Bet a userBet: UserBet
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "${userBet.match.teams.home.name} vs ${userBet.match.teams.away.name}", // <-- Usa userBet
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Marcador pronosticado: ${userBet.homeScore} - ${userBet.awayScore}", // <-- Usa userBet
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Apostado: $%.2f".format(userBet.amount), // <-- Usa userBet
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ganancia Potencial: $%.2f".format(userBet.potentialPayout), // <-- Usa userBet
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Green
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Liga: ${userBet.match.league.name}", // <-- Usa userBet
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = "Estado del partido: ${userBet.match.fixture.status.long}", // <-- Usa userBet
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}