package com.leonardowaked.unabbet

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson

import com.leonardowaked.unabbet.data.models.LeagueData
import com.leonardowaked.unabbet.data.models.MatchResponse
import com.leonardowaked.unabbet.network.RetrofitClient
import com.leonardowaked.unabbet.util.BalanceManager // <-- ¡Importa BalanceManager!
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Tu MainViewModel va aquí arriba, asegúrate de que esté como lo tienes:
class MainViewModel : androidx.lifecycle.ViewModel() {
    var partidos by mutableStateOf<List<MatchResponse>>(emptyList())
        private set
    var competiciones by mutableStateOf<List<LeagueData>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    suspend fun fetchLiveFixtures() {
        isLoading = true
        errorMessage = null
        try {
            val response = RetrofitClient.instance.getFixtures(live = "all")
            if (response.isSuccessful) {
                partidos = response.body()?.response ?: emptyList()
            } else {
                errorMessage = "Error al cargar partidos en vivo: ${response.code()}"
                Log.e("API_CALL", "Error en la respuesta en vivo: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            errorMessage = "Error de conexión: ${e.message}"
            Log.e("API_CALL", "Error al conectar con la API en vivo: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    suspend fun fetchUpcomingFixtures() {
        isLoading = true
        errorMessage = null
        val tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        try {
            val response = RetrofitClient.instance.getFixtures(date = tomorrow)
            if (response.isSuccessful) {
                partidos = response.body()?.response ?: emptyList()
            } else {
                errorMessage = "Error al cargar próximos partidos: ${response.code()}"
                Log.e("API_CALL", "Error en la respuesta próximos: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            errorMessage = "Error de conexión: ${e.message}"
            Log.e("API_CALL", "Error al conectar con la API próximos: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    suspend fun fetchLeagues() {
        isLoading = true
        errorMessage = null
        try {
            val response = RetrofitClient.instance.getLeagues()
            if (response.isSuccessful) {
                competiciones = response.body()?.response ?: emptyList()
            } else {
                errorMessage = "Error al cargar competiciones: ${response.code()}"
                Log.e("API_CALL", "Error en la respuesta ligas: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            errorMessage = "Error de conexión: ${e.message}"
            Log.e("API_CALL", "Error al conectar con la API ligas: ${e.message}")
        } finally {
            isLoading = false
        }
    }
}


@Composable
fun HomeScreen(
    onClickAccount: () -> Unit = {},
    navController: NavController,
    onClickBetHistory: () -> Unit // <-- ¡Añade este nuevo parámetro!
) {
    val viewModel: MainViewModel = viewModel()
    var selectedTab by remember { mutableStateOf("En Vivo") }

    // Observar el saldo del usuario del BalanceManager
    val userBalance by BalanceManager.userBalance.collectAsState() // <-- ¡Añade esto!

    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            "En Vivo" -> viewModel.fetchLiveFixtures()
            "Próximos" -> viewModel.fetchUpcomingFixtures()
            "Competiciones" -> viewModel.fetchLeagues()
        }
    }

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
                var expanded by remember { mutableStateOf(false) }

                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menú")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Cuenta") },
                        onClick = {
                            expanded = false
                            onClickAccount()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Historial de apuestas") },
                        onClick = {
                            expanded = false
                            onClickBetHistory() // <-- ¡LLAMA A LA FUNCIÓN PARA NAVEGAR!
                        }
                    )

                }

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

                Button(
                    onClick = {  }, // Puedes añadir una acción aquí si el botón de saldo debe hacer algo
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "$%.2f".format(userBalance), fontWeight = FontWeight.Bold) // <-- ¡Muestra el saldo dinámico!
                        Text(text = "Cajero", fontSize = 12.sp)
                    }
                }

            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF7A1E1E))
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TabButton(text = "En Vivo", isSelected = selectedTab == "En Vivo") { selectedTab = "En Vivo" }
                TabButton(text = "Próximos", isSelected = selectedTab == "Próximos") { selectedTab = "Próximos" }
                TabButton(text = "Competiciones", isSelected = selectedTab == "Competiciones") { selectedTab = "Competiciones" }
            }

            Image(
                painter = painterResource(id = R.drawable.banner),
                contentDescription = "Football Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF7A1E1E))
                }
            } else if (viewModel.errorMessage != null) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${viewModel.errorMessage}", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            } else {
                when (selectedTab) {
                    "En Vivo", "Próximos" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background(Color(0xFF7A1E1E), shape = RoundedCornerShape(20.dp))
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (selectedTab == "En Vivo") "Partidos en vivo" else "Próximos partidos",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            if (viewModel.partidos.isEmpty()) {
                                Text(
                                    text = "No hay partidos disponibles.",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 14.sp
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(viewModel.partidos) { match ->
                                        PartidoCard(match = match, navController = navController)
                                    }
                                }
                            }
                        }
                    }
                    "Competiciones" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background(Color(0xFF7A1E1E), shape = RoundedCornerShape(20.dp))
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Competiciones",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            if (viewModel.competiciones.isEmpty()) {
                                Text(
                                    text = "No hay competiciones disponibles.",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 14.sp
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(viewModel.competiciones) { league ->
                                        LeagueCard(league = league)
                                    }
                                }
                            }
                        }
                    }
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
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if (isSelected) Color.Yellow else Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PartidoCard(match: MatchResponse, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5EDDE), shape = RoundedCornerShape(6.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = match.teams.home.name,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = match.teams.away.name,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val displayStatus = when (match.fixture.status.short) {
                "FT", "AET", "PEN" -> "${match.goals.home ?: "-"} - ${match.goals.away ?: "-"}"
                "HT", "1H", "2H", "ET", "BT" -> "${match.fixture.status.elapsed?.toString() ?: ""}´"
                "NS", "PST", "CANC", "ABD" -> match.fixture.status.short
                else -> match.fixture.status.short
            }
            Text(
                text = displayStatus,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val matchJson = Gson().toJson(match)
                    val encodedMatchJson = URLEncoder.encode(matchJson, StandardCharsets.UTF_8.toString()) //
                    navController.navigate("bet/${encodedMatchJson}") //
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A1E1E)),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = "Apostar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Composable
fun LeagueCard(league: LeagueData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5EDDE), shape = RoundedCornerShape(6.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = league.league.name,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = league.country.name,
                color = Color.DarkGray,
                fontSize = 12.sp
            )
        }
    }
}

// Preview de HomeScreen (asegúrate de que está completo y sin errores)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Debes pasar todos los parámetros requeridos por HomeScreen
    HomeScreen(
        onClickAccount = {},
        navController = rememberNavController(),
        onClickBetHistory = {} // Lambda vacío para el preview
    )
}