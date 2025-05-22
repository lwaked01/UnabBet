package com.leonardowaked.unabbet

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leonardowaked.unabbet.data.models.MatchResponse // Importación principal
import com.leonardowaked.unabbet.util.BalanceManager
import com.leonardowaked.unabbet.util.UserBet
import com.leonardowaked.unabbet.util.BetHistoryManager
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

// ¡NUEVAS IMPORTACIONES NECESARIAS PARA EL PREVIEW!
// Como Fixture, League, Teams, etc. no son internas de MatchResponse,
// debes importarlas directamente desde su paquete.
import com.leonardowaked.unabbet.data.models.Fixture
import com.leonardowaked.unabbet.data.models.League
import com.leonardowaked.unabbet.data.models.Teams
import com.leonardowaked.unabbet.data.models.Team
import com.leonardowaked.unabbet.data.models.Goals
import com.leonardowaked.unabbet.data.models.Score
import com.leonardowaked.unabbet.data.models.Periods
import com.leonardowaked.unabbet.data.models.Venue
import com.leonardowaked.unabbet.data.models.Status


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetScreen(navController: NavController, matchJson: String?) {
    val match = remember(matchJson) {
        matchJson?.let {
            try {
                val decodedJson = URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                Gson().fromJson(decodedJson, MatchResponse::class.java)
            } catch (e: Exception) {
                Log.e("BetScreen", "Error deserializando matchJson: ${e.message}")
                null
            }
        }
    }

    if (match == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF7A1E1E)), contentAlignment = Alignment.Center) {
            Text("Error: Partido no disponible.", color = Color.White, fontSize = 20.sp)
        }
        return
    }

    var homeScorePrediction by remember { mutableStateOf(0) }
    var awayScorePrediction by remember { mutableStateOf(0) }
    var betAmountString by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userBalance by BalanceManager.userBalance.collectAsState()

    val potentialPayout = remember(betAmountString) {
        val amount = betAmountString.toDoubleOrNull() ?: 0.0
        if (amount > 0) amount * 1.8
        else 0.0
    }

    val isBetButtonEnabled = betAmountString.toDoubleOrNull() != null &&
            (betAmountString.toDoubleOrNull() ?: 0.0) > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Apuesta", color = Color.White) },
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.width(48.dp))
                Button(
                    onClick = { /* No hay acción aquí, es solo un logo */ },
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

                Button(
                    onClick = { navController.navigate("account") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "$%.2f".format(userBalance), fontWeight = FontWeight.Bold)
                        Text(text = "Cajero", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${match.teams.home.name} - ${match.teams.away.name}",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${match.goals.home ?: "0"} - ${match.goals.away ?: "0"}",
                    color = Color.Yellow,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                val displayStatus = when (match.fixture.status.short) {
                    "FT", "AET", "PEN" -> "Finalizado"
                    "HT" -> "Descanso"
                    "1H", "2H", "ET", "BT" -> "${match.fixture.status.elapsed?.toString() ?: ""}´"
                    "NS" -> "No Iniciado"
                    "PST" -> "Pospuesto"
                    "CANC" -> "Cancelado"
                    "ABD" -> "Abandonado"
                    else -> match.fixture.status.long
                }
                Text(
                    text = displayStatus,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5EDDE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Pronostica el marcador:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ScoreInputField(
                            score = homeScorePrediction,
                            onValueChange = { homeScorePrediction = it }
                        )
                        Text(
                            text = "-",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        ScoreInputField(
                            score = awayScorePrediction,
                            onValueChange = { awayScorePrediction = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Resultado disponible",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = betAmountString,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        betAmountString = newValue
                    }
                },
                label = { Text("Cantidad a apostar") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val amount = betAmountString.toDoubleOrNull() ?: 0.0

                    if (amount <= 0) {
                        Toast.makeText(context, "Ingresa una cantidad válida para apostar.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (homeScorePrediction < 0 || awayScorePrediction < 0) {
                        Toast.makeText(context, "El marcador no puede ser negativo.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (amount > userBalance) {
                        Toast.makeText(context, "Saldo insuficiente. Tienes: $%.2f".format(userBalance), Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    Log.d("Apuesta", "Apuesta Confirmada:")
                    Log.d("Apuesta", "Partido: ${match.teams.home.name} vs ${match.teams.away.name}")
                    Log.d("Apuesta", "Marcador Pronosticado: $homeScorePrediction - $awayScorePrediction")
                    Log.d("Apuesta", "Monto Apostado: $%.2f".format(amount))
                    Log.d("Apuesta", "Ganancia Potencial: $%.2f".format(potentialPayout))

                    BalanceManager.deductBalance(amount)
                    Log.d("Apuesta", "Saldo después de la deducción: $%.2f".format(BalanceManager.userBalance.value))

                    val newUserBet = UserBet(
                        match = match,
                        homeScore = homeScorePrediction,
                        awayScore = awayScorePrediction,
                        amount = amount,
                        potentialPayout = potentialPayout
                    )
                    BetHistoryManager.addBet(newUserBet)

                    Log.d("DEBUG_BET_MANAGER", "Apuesta añadida a BetHistoryManager: $newUserBet. Tamaño del historial: ${BetHistoryManager.bets.value.size}")

                    Toast.makeText(context, "¡Apuesta realizada! Saldo restante: $%.2f".format(BalanceManager.userBalance.value), Toast.LENGTH_LONG).show()

                    betAmountString = ""
                    homeScorePrediction = 0
                    awayScorePrediction = 0

                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDC800)),
                shape = RoundedCornerShape(8.dp),
                enabled = isBetButtonEnabled
            ) {
                Text(text = "Fijar Apuesta", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "PAGO POTENCIAL:",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$%.2f".format(potentialPayout),
                color = Color.Green,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun ScoreInputField(score: Int, onValueChange: (Int) -> Unit) {
    OutlinedTextField(
        value = score.toString(),
        onValueChange = { newValue ->
            val newScore = newValue.toIntOrNull() ?: 0
            onValueChange(newScore.coerceAtLeast(0))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.width(80.dp),
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7A1E1E),
            unfocusedBorderColor = Color.Gray,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = Color.Black
        ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun BetScreenPreview() {
    val sampleMatchJson = Gson().toJson(
        MatchResponse(
            fixture = Fixture( // <-- ¡CORRECCIÓN AQUÍ! Ahora es solo 'Fixture'
                id = 12345,
                date = "2025-05-22T12:00:00+00:00",
                timestamp = 1678886400,
                timezone = "UTC",
                status = Status( // <-- ¡CORRECCIÓN AQUÍ! Ahora es solo 'Status'
                    long = "Match Finished",
                    short = "FT",
                    elapsed = 90
                ),
                referee = null, // Añadido para que coincida con tu Fixture en MatchResponse.kt
                periods = Periods(null, null), // Añadido para que coincida con tu Fixture
                venue = Venue(1, "Sample Venue", "Sample City") // Añadido para que coincida
            ),
            league = League( // <-- ¡CORRECCIÓN AQUÍ! Ahora es solo 'League'
                id = 1,
                name = "Sample League",
                country = "World",
                season = 2024,
                round = "Regular Season - 1",
                logo = "https://example.com/logo.png", // Añadido para que coincida
                flag = null // Añadido para que coincida
            ),
            teams = Teams( // <-- ¡CORRECCIÓN AQUÍ! Ahora es solo 'Teams'
                home = Team(id = 1, name = "Home Team", winner = true, logo = "https://example.com/home.png"), // <-- Ahora solo 'Team'
                away = Team(id = 2, name = "Away Team", winner = false, logo = "https://example.com/away.png")  // <-- Ahora solo 'Team'
            ),
            goals = Goals(home = 2, away = 1), // <-- ¡CORRECCIÓN AQUÍ! Ahora es solo 'Goals'
            score = Score( // <-- ¡CORRECCIÓN AQUÍ! Ahora es solo 'Score'
                halftime = Goals(home = 1, away = 0), // <-- ¡CORRECCIÓN AQUÍ! Ahora es solo 'Goals'
                fulltime = Goals(home = 2, away = 1), // <-- ¡CORRECCIÓN AQUÍ! Ahora es solo 'Goals'
                extratime = Goals(home = null, away = null), // <-- ¡CORRECCIÓN AQUÍ! Ahora es solo 'Goals'
                penalty = Goals(home = null, away = null) // <-- ¡CORRECCIÓN AQUÍ! Ahora es solo 'Goals'
            )
        )
    )
    BetScreen(navController = rememberNavController(), matchJson = sampleMatchJson)
}