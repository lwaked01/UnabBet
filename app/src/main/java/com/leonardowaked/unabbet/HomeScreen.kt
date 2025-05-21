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
import androidx.lifecycle.viewmodel.compose.viewModel // Importar para usar ViewModel
import androidx.navigation.NavController
import com.google.gson.Gson

import com.leonardowaked.unabbet.data.models.LeagueData
import com.leonardowaked.unabbet.data.models.MatchResponse
import com.leonardowaked.unabbet.network.RetrofitClient // Importar tu cliente Retrofit
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate // Necesitarás Java 8+ API para esto
import java.time.format.DateTimeFormatter
import java.util.Locale

// ViewModel para manejar la lógica de la API y el estado
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
fun HomeScreen(navController: NavController) {
    // Instancia del ViewModel
    val viewModel: MainViewModel = viewModel()

    var selectedTab by remember { mutableStateOf("En Vivo") }

    // Usamos LaunchedEffect para disparar la carga de datos cuando cambia la pestaña
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

            // --- HEADER (Tu código original) ---
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


            // --- PESTAÑAS DE NAVEGACIÓN ---
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

            // --- BANNER (Tu código original) ---
            Image(
                painter = painterResource(id = R.drawable.banner),
                contentDescription = "Football Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp)) // Ajustado un poco para mejor espaciado

            // --- CONTENIDO PRINCIPAL (DINÁMICO SEGÚN LA PESTAÑA) ---
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
                        // Lista de partidos en vivo o próximos
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp) // Reducido el padding para que no se corte tanto
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
                                        // Pasa el navController a PartidoCard
                                        PartidoCard(match = match, navController = navController)
                                    }
                                }
                            }
                        }
                    }
                    "Competiciones" -> {
                        // Lista de competiciones
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

        // --- FOOTER (Tu código original) ---
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

// Composable para el botón de las pestañas (más limpio)
@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if (isSelected) Color.Yellow else Color.White // Color si está seleccionada
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
            // Lógica para mostrar el estado del partido (minuto, o goles finales)
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
                    // 1. Codificar el JSON para que sea seguro en la URL
                    val encodedMatchJson = URLEncoder.encode(matchJson, StandardCharsets.UTF_8.toString()) //
                    // 2. Navegar con el JSON codificado
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
        // Aquí podrías mostrar el logo de la liga si tienes una librería como Coil o Glide
        // AsyncImage(model = league.league.logo, contentDescription = league.league.name, modifier = Modifier.size(40.dp))
    }
}