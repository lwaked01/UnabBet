package com.leonardowaked.unabbet

import android.util.Log // Asegúrate de importar Log si lo necesitas para depuración
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // Para el botón de regreso
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
import androidx.navigation.NavController // Importa NavController
import androidx.navigation.compose.rememberNavController // Necesario para el @Preview si lo usas
import com.leonardowaked.unabbet.data.models.MatchResponse // Importa tu modelo MatchResponse

// 1. Modificar el @Preview para que acepte los nuevos parámetros si quieres que funcione
@Preview
@Composable
fun BetScreenPreview() {
    // Proporciona un MatchResponse de ejemplo para el Preview
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
        goals = com.leonardowaked.unabbet.data.models.Goals(home = 2, away = 1), // Goles de ejemplo
        score = com.leonardowaked.unabbet.data.models.Score( // Datos de score completos, aunque no todos se usen
            halftime = com.leonardowaked.unabbet.data.models.Goals(0,0),
            fulltime = com.leonardowaked.unabbet.data.models.Goals(0,0),
            extratime = com.leonardowaked.unabbet.data.models.Goals(0,0),
            penalty = com.leonardowaked.unabbet.data.models.Goals(0,0)
        )
    )
    BetScreen(navController = rememberNavController(), match = sampleMatch)
}

// 2. Modificar la firma de BetScreen para recibir NavController y MatchResponse
@Composable
fun BetScreen(navController: NavController, match: MatchResponse) {
    // 3. Inicializar los estados de homeScore y awayScore con los goles del partido real
    var homeScore by remember { mutableStateOf(match.goals.home ?: 0) } // Usa 0 si es nulo
    var awayScore by remember { mutableStateOf(match.goals.away ?: 0) } // Usa 0 si es nulo

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7A1E1E))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 4. Modificar la TopBar para incluir un botón de regreso
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) { // Botón para regresar
                    Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                }

                // Botón para el LOGO
                Button(
                    onClick = { /* Acción para el logo si la hay */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White // Cambiado a blanco para contraste
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
                    onClick = { /* Acción para el cajero */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
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
                    onClick = { /* Acción para la cuenta */ },
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

            // 5. Usar los datos del objeto 'match' para mostrar la información del partido
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${match.teams.home.name} - ${match.teams.away.name}", // Equipos dinámicos
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${match.goals.home ?: "-"} - ${match.goals.away ?: "-"}", // Goles dinámicos (usar "-" si son nulos)
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    // Mostrar minutos transcurridos o "No iniciado"
                    text = "${match.fixture.status.elapsed?.toString() ?: "No iniciado"}´",
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
                    onClick = {
                        // Lógica para fijar apuesta
                        Log.d("Apuesta", "Apuesta para ${match.teams.home.name} vs ${match.teams.away.name}: $homeScore - $awayScore")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A1E1E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fijar Apuesta", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* Lógica para Cantidad A Apostar */ },
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

        // Footer (tu código original)
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

// Puedes eliminar la TopBar genérica si ya la has integrado directamente en BetScreen
// o si tienes una TopBar común para varias pantallas.
// Si la usas en otras pantallas, asegúrate de que sea compatible con los cambios.
/*
@Composable
fun TopBar() {
    // ... (Tu código original de TopBar si es que lo usas en HomeScreen y aquí)
}
*/