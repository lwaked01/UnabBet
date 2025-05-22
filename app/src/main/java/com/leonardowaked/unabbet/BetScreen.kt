package com.leonardowaked.unabbet

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leonardowaked.unabbet.data.models.Bet // Asegúrate de que tu modelo Bet esté definido
import com.leonardowaked.unabbet.data.models.MatchResponse
import com.leonardowaked.unabbet.util.BalanceManager // IMPORTANTE: Importar BalanceManager

// Preview de BetScreen
@Preview(showBackground = true)
@Composable
fun BetScreenPreview() {
    val sampleMatch = MatchResponse(
        fixture = com.leonardowaked.unabbet.data.models.Fixture(
            id = 123, referee = null, timezone = "UTC", date = "2025-05-20T19:00:00+00:00",
            timestamp = 0, periods = com.leonardowaked.unabbet.data.models.Periods(null, null),
            venue = com.leonardowaked.unabbet.data.models.Venue(0, "Estadio de Prueba", "Ciudad Ficticia"),
            status = com.leonardowaked.unabbet.data.models.Status("Live", "65", 65)
        ),
        league = com.leonardowaked.unabbet.data.models.League(
            id = 1, name = "Liga de Prueba", country = "País Ejemplo", logo = "", flag = null, season = 2025, round = ""
        ),
        teams = com.leonardowaked.unabbet.data.models.Teams(
            home = com.leonardowaked.unabbet.data.models.Team(id = 1, name = "Equipo Local", logo = "", winner = null),
            away = com.leonardowaked.unabbet.data.models.Team(id = 2, name = "Equipo Visitante", logo = "", winner = null)
        ),
        goals = com.leonardowaked.unabbet.data.models.Goals(home = 2, away = 1),
        score = com.leonardowaked.unabbet.data.models.Score(
            halftime = com.leonardowaked.unabbet.data.models.Goals(0,0),
            fulltime = com.leonardowaked.unabbet.data.models.Goals(0,0),
            extratime = com.leonardowaked.unabbet.data.models.Goals(0,0),
            penalty = com.leonardowaked.unabbet.data.models.Goals(0,0)
        )
    )
    BetScreen(navController = rememberNavController(), match = sampleMatch)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetScreen(navController: NavController, match: MatchResponse) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Observa el saldo del BalanceManager
    val userBalance by BalanceManager.userBalance.collectAsState()

    var homeScorePrediction by remember { mutableStateOf(0) }
    var awayScorePrediction by remember { mutableStateOf(0) }

    var betAmountString by remember { mutableStateOf("") }
    var potentialPayout by remember { mutableStateOf(0.0) }

    // Mapa para controlar cuántas veces se ha apostado a un marcador específico
    val stakedScoresCount = remember { mutableStateMapOf<String, Int>() }
    // Lista para almacenar las apuestas realizadas y mostrarlas
    val placedBets = remember { mutableStateListOf<Bet>() }


    LaunchedEffect(betAmountString) {
        val amount = betAmountString.toDoubleOrNull() ?: 0.0
        // Cuota fija para simplificar, como en tu código original
        val fixedMultiplier = 1.8
        potentialPayout = amount * fixedMultiplier
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* El título se manejará dentro de la Row */ },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.White)
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
            // Sección de encabezado (logo, saldo, cuenta)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.width(48.dp)) // Ajuste para alinear con el botón de regreso

                Button(
                    onClick = { /* Acción para el logo si la hay */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
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
                    onClick = { /* Acción para el cajero (no hay aquí por ahora) */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "$%.2f".format(userBalance), fontWeight = FontWeight.Bold) // Saldo real
                        Text(text = "Cajero", fontSize = 12.sp)
                    }
                }

                Button(
                    onClick = { /* Acción para la cuenta (no hay aquí por ahora) */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
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

            Spacer(modifier = Modifier.height(16.dp))

            // Detalles del Partido
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${match.teams.home.name} - ${match.teams.away.name}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${match.goals.home ?: "-"} - ${match.goals.away ?: "-"}",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${match.fixture.status.elapsed?.toString() ?: "No iniciado"}´",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECCIÓN DE APUESTA PRINCIPAL
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(Color(0xFFF5EDDE), shape = RoundedCornerShape(20.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pronostica el marcador:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ScoreBox(homeScorePrediction) { homeScorePrediction = it }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "-", fontSize = 18.sp, color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    ScoreBox(awayScorePrediction) { awayScorePrediction = it }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val currentScoreString = "$homeScorePrediction-$awayScorePrediction"
                val isScoreLimitReached = remember(homeScorePrediction, awayScorePrediction, stakedScoresCount) {
                    stakedScoresCount[currentScoreString] ?: 0 >= 2
                }

                val isScoreValidByRules = remember(homeScorePrediction, awayScorePrediction) {
                    !(homeScorePrediction > 2 || awayScorePrediction > 2)
                }

                val displayMessage = when {
                    !isScoreValidByRules -> "Marcador no permitido (máx. 2 goles por equipo)"
                    isScoreLimitReached -> "Ya has apostado este marcador 2 veces. No disponible."
                    else -> "Resultado disponible"
                }

                val messageColor = when {
                    !isScoreValidByRules || isScoreLimitReached -> Color.Red
                    else -> Color.DarkGray
                }

                Text(
                    text = displayMessage,
                    fontSize = 12.sp,
                    color = messageColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = betAmountString,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            betAmountString = newValue
                        }
                    },
                    label = { Text("Cantidad a apostar") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7A1E1E),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF7A1E1E),
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color(0xFF7A1E1E),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()

                        val amount = betAmountString.toDoubleOrNull() ?: 0.0

                        val isScoreLimitReachedOnSubmit = stakedScoresCount[currentScoreString] ?: 0 >= 2
                        val isScoreValidByRulesOnSubmit = !(homeScorePrediction > 2 || awayScorePrediction > 2)

                        // --- Validaciones ---
                        if (!isScoreValidByRulesOnSubmit) {
                            Toast.makeText(context, "Este marcador no está permitido (máx. 2 goles por equipo).", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (isScoreLimitReachedOnSubmit) {
                            Toast.makeText(context, "Ya has apostado este marcador dos veces. No puedes apostar de nuevo.", Toast.LENGTH_LONG).show()
                            return@Button
                        }
                        if (amount <= 0) {
                            Toast.makeText(context, "Ingresa una cantidad válida para apostar.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (homeScorePrediction < 0 || awayScorePrediction < 0) {
                            Toast.makeText(context, "Los marcadores no pueden ser negativos.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (amount > userBalance) {
                            Toast.makeText(context, "Saldo insuficiente para realizar esta apuesta.", Toast.LENGTH_LONG).show()
                            return@Button
                        }
                        // --- Fin Validaciones ---

                        Log.d("Apuesta", "--- Apuesta Confirmada ---")
                        Log.d("Apuesta", "Partido: ${match.teams.home.name} vs ${match.teams.away.name}")
                        Log.d("Apuesta", "Marcador Apostado: $homeScorePrediction - $awayScorePrediction")
                        Log.d("Apuesta", "Monto Apostado: $%.2f".format(amount))
                        Log.d("Apuesta", "Pago Potencial: $%.2f".format(potentialPayout))
                        Log.d("Apuesta", "Saldo antes: $%.2f".format(userBalance))
                        Log.d("Apuesta", "-------------------------")

                        // Deducir saldo usando BalanceManager
                        BalanceManager.deductBalance(amount)
                        Log.d("Apuesta", "Saldo después: $%.2f".format(BalanceManager.userBalance.value)) // Log el saldo actual del manager

                        // Actualizar el contador de apuestas por marcador
                        stakedScoresCount[currentScoreString] = (stakedScoresCount[currentScoreString] ?: 0) + 1

                        // Crear y añadir la nueva apuesta a la lista
                        val newBet = Bet(
                            matchName = "${match.teams.home.name} vs ${match.teams.away.name}",
                            homeScorePrediction = homeScorePrediction,
                            awayScorePrediction = awayScorePrediction,
                            amountStaked = amount,
                            potentialPayout = potentialPayout
                        )
                        placedBets.add(newBet)

                        Log.d("DEBUG_BET_LIST", "Apuesta añadida: $newBet. Tamaño de la lista: ${placedBets.size}")

                        Toast.makeText(context, "¡Apuesta realizada! Saldo restante: $%.2f".format(BalanceManager.userBalance.value), Toast.LENGTH_LONG).show()

                        // Limpiar campos después de la apuesta exitosa
                        betAmountString = ""
                        homeScorePrediction = 0 // Restablecer a 0 o a "" para que el usuario ingrese de nuevo
                        awayScorePrediction = 0 // Restablecer a 0 o a ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A1E1E)),
                    modifier = Modifier.fillMaxWidth(),
                    // Habilitar el botón solo si la cantidad es válida y las reglas de marcador lo permiten
                    enabled = (betAmountString.toDoubleOrNull() ?: 0.0) > 0 && isScoreValidByRules && !isScoreLimitReached
                ) {
                    Text("Fijar Apuesta", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth() // Asegúrate de que el Row ocupe el ancho completo
                ) {
                    Text(text = "PAGO POTENCIAL:", fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = String.format("$%.2f", potentialPayout),
                        fontWeight = FontWeight.Bold,
                        color = Color.Green,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // SECCIÓN DE APUESTAS REALIZADAS
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 32.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tus Apuestas Realizadas:",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Ocupa todo el espacio vertical restante
                    .padding(horizontal = 32.dp)
            ) {
                if (placedBets.isEmpty()) {
                    item {
                        Text(
                            text = "No hay apuestas realizadas aún.",
                            color = Color.LightGray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(placedBets) { bet ->
                        BetItem(bet = bet)
                    }
                }
            }
        }
    }
}

// Composable para mostrar un solo ítem de apuesta
@Composable
fun BetItem(bet: Bet) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5EDDE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = bet.matchName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Marcador: ${bet.homeScorePrediction} - ${bet.awayScorePrediction}",
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(
                text = "Apostado: $%.2f".format(bet.amountStaked),
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(
                text = "Ganancia Potencial: $%.2f".format(bet.potentialPayout),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Green
            )
        }
    }
}

// Función ScoreBox
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreBox(score: Int, onScoreChange: (Int) -> Unit) {
    // Usamos `remember` para que el texto de entrada se mantenga y solo se actualice
    // cuando el `score` externo cambie (ej. al restablecer).
    // `takeIf { it != "0" } ?: ""` para que no muestre "0" por defecto, sino vacío.
    var scoreText by remember(score) { mutableStateOf(score.toString().takeIf { it != "0" } ?: "") }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = scoreText,
        onValueChange = { newValue ->
            val filteredValue = newValue.filter { it.isDigit() }
            if (filteredValue.length <= 2) { // Limita a 2 dígitos el marcador
                scoreText = filteredValue
                onScoreChange(filteredValue.toIntOrNull() ?: 0) // Actualiza el score Int
            }
        },
        modifier = Modifier
            .width(70.dp)
            .height(56.dp)
            .background(Color.White, RoundedCornerShape(4.dp)),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() } // Oculta el teclado al presionar "Done"
        ),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7A1E1E),
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color.Transparent, // No label
            unfocusedLabelColor = Color.Transparent, // No label
            cursorColor = Color(0xFF7A1E1E),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}