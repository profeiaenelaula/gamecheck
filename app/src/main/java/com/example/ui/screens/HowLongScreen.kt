package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.GameCatalog
import com.example.data.model.Game
import com.example.ui.theme.*
import com.example.viewmodel.AppSection
import com.example.viewmodel.DurationGoal
import com.example.viewmodel.GameCheckViewModel

@Composable
fun HowLongScreen(
    viewModel: GameCheckViewModel,
    onNavigateToGame: (String, AppSection) -> Unit
) {
    val dailyHours by viewModel.dailyHours.collectAsState()
    val durationGoal by viewModel.durationGoal.collectAsState()
    val selectedGameId by viewModel.selectedGameId.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val selectedGame = remember(selectedGameId) {
        GameCatalog.games.find { it.id == selectedGameId } ?: GameCatalog.games.first()
    }

    val calculationResult = remember(selectedGame, dailyHours, durationGoal) {
        viewModel.calculateDurationForGame(selectedGame, dailyHours, durationGoal)
    }

    val filteredGames = remember(searchQuery) {
        if (searchQuery.isBlank()) GameCatalog.games
        else GameCatalog.games.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.genres.any { g -> g.contains(searchQuery, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("how_long_screen_list"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Interactive Calculator Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("duration_calculator_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, AccentTeal.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⏱️", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "¿Cuánto dura? - Calculadora",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Calcula cuánto tardarás según tu tiempo libre diario",
                                    fontSize = 12.sp,
                                    color = TextSecondaryDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Selected Game for calculation
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = DarkSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = selectedGame.coverEmoji, fontSize = 20.sp)
                                Column {
                                    Text(
                                        text = "Juego seleccionado:",
                                        fontSize = 10.sp,
                                        color = TextMutedDark
                                    )
                                    Text(
                                        text = selectedGame.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            Text(
                                text = "Elige otro abajo ↓",
                                fontSize = 11.sp,
                                color = AccentTeal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Objective Selector
                    Text(
                        text = "¿Qué objetivo deseas completar?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GoalButton(
                            title = "Solo Historia",
                            hours = selectedGame.duration.mainStoryHours,
                            selected = durationGoal == DurationGoal.MAIN_STORY,
                            onClick = { viewModel.setDurationGoal(DurationGoal.MAIN_STORY) },
                            modifier = Modifier.weight(1f),
                            testTag = "goal_story_button"
                        )
                        GoalButton(
                            title = "Historia + Extras",
                            hours = selectedGame.duration.mainPlusExtraHours,
                            selected = durationGoal == DurationGoal.MAIN_PLUS_EXTRAS,
                            onClick = { viewModel.setDurationGoal(DurationGoal.MAIN_PLUS_EXTRAS) },
                            modifier = Modifier.weight(1f),
                            testTag = "goal_extras_button"
                        )
                        GoalButton(
                            title = "100% Completista",
                            hours = selectedGame.duration.completionistHours,
                            selected = durationGoal == DurationGoal.COMPLETIONIST,
                            onClick = { viewModel.setDurationGoal(DurationGoal.COMPLETIONIST) },
                            modifier = Modifier.weight(1f),
                            testTag = "goal_completionist_button"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Daily hours slider & quick chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿Cuántas horas juegas al día?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimaryDark
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AccentTeal.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, AccentTeal)
                        ) {
                            Text(
                                text = if (dailyHours == dailyHours.toInt().toFloat()) "${dailyHours.toInt()} h/día" else "%.1f h/día".format(dailyHours),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentTeal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Slider(
                        value = dailyHours,
                        onValueChange = { viewModel.setDailyHours(it) },
                        valueRange = 0.5f..8.0f,
                        steps = 14,
                        colors = SliderDefaults.colors(
                            thumbColor = AccentTeal,
                            activeTrackColor = AccentTeal,
                            inactiveTrackColor = DarkSurfaceBorder
                        ),
                        modifier = Modifier.testTag("daily_hours_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(0.5f, 1.0f, 1.5f, 2.0f, 3.0f, 4.0f).forEach { hours ->
                            val isSel = dailyHours == hours
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSel) AccentTeal else DarkSurfaceVariant,
                                border = BorderStroke(1.dp, if (isSel) AccentTeal else DarkSurfaceBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setDailyHours(hours) }
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text(
                                        text = if (hours == hours.toInt().toFloat()) "${hours.toInt()}h" else "${hours}h",
                                        fontSize = 11.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSel) Color(0xFF090D16) else TextPrimaryDark
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Calculation Result Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF07271F),
                        border = BorderStroke(1.dp, RunGreatGreen.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("duration_result_box")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = RunGreatGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Resultado de tu estimación:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RunGreatGreen
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Te demorarás aproximadamente ${calculationResult.daysRequired} días (${"%.1f".format(calculationResult.weeksRequired)} semanas) para completar este objetivo (${calculationResult.targetHours.toInt()}h totales).",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = AccentTeal,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Terminarías el: ${calculationResult.completionDateFormatted}",
                                    fontSize = 12.sp,
                                    color = AccentTeal,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "Catálogo de Juegos y Tiempos Promedio:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AccentTeal,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
            )
        }

        // List of all games with duration times
        items(filteredGames, key = { it.id }) { game ->
            val isCurrentCalcTarget = game.id == selectedGameId
            GameDurationCard(
                game = game,
                isSelected = isCurrentCalcTarget,
                dailyHours = dailyHours,
                onSelectForCalc = { viewModel.selectGame(game.id) },
                onViewFullFile = { onNavigateToGame(game.id, AppSection.GAME_DETAILS) }
            )
        }
    }
}

@Composable
fun GoalButton(
    title: String,
    hours: Float,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (selected) AccentTeal else DarkSurfaceVariant,
        border = BorderStroke(1.dp, if (selected) AccentTeal else DarkSurfaceBorder),
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Color(0xFF090D16) else TextSecondaryDark,
                maxLines = 1
            )
            Text(
                text = "${hours.toInt()} h",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = if (selected) Color(0xFF090D16) else Color.White
            )
        }
    }
}

@Composable
fun GameDurationCard(
    game: Game,
    isSelected: Boolean,
    dailyHours: Float,
    onSelectForCalc: () -> Unit,
    onViewFullFile: () -> Unit
) {
    val daysStory = Math.ceil((game.duration.mainStoryHours / dailyHours).toDouble()).toInt()
    val days100 = Math.ceil((game.duration.completionistHours / dailyHours).toDouble()).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("game_duration_card_${game.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, if (isSelected) AccentTeal else DarkSurfaceBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(game.bannerColorHex).copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color(game.bannerColorHex).copy(alpha = 0.5f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = game.coverEmoji, fontSize = 20.sp)
                        }
                    }

                    Column {
                        Text(
                            text = game.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${game.developer} • ${game.genres.firstOrNull() ?: "General"}",
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )
                    }
                }

                if (isSelected) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = AccentTeal.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, AccentTeal)
                    ) {
                        Text(
                            text = "En cálculo activo",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentTeal,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = onSelectForCalc,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, DarkSurfaceBorder)
                    ) {
                        Text("Calcular este", fontSize = 11.sp, color = TextPrimaryDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3 Standard HowLongToBeat metric columns
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DurationMetricItem(
                    label = "Historia Principal",
                    hours = game.duration.mainStoryHours,
                    daysEst = daysStory,
                    color = AccentTeal,
                    modifier = Modifier.weight(1f)
                )
                DurationMetricItem(
                    label = "Historia + Extras",
                    hours = game.duration.mainPlusExtraHours,
                    daysEst = Math.ceil((game.duration.mainPlusExtraHours / dailyHours).toDouble()).toInt(),
                    color = AccentViolet,
                    modifier = Modifier.weight(1f)
                )
                DurationMetricItem(
                    label = "100% Completista",
                    hours = game.duration.completionistHours,
                    daysEst = days100,
                    color = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ritmo rápido: ~${game.duration.speedrunHours.toInt()}h | Calma: ~${game.duration.relaxedHours.toInt()}h",
                    fontSize = 10.sp,
                    color = TextMutedDark
                )

                TextButton(
                    onClick = onViewFullFile,
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text("Ver ficha completa →", fontSize = 11.sp, color = AccentTeal)
                }
            }
        }
    }
}

@Composable
fun DurationMetricItem(
    label: String,
    hours: Float,
    daysEst: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = DarkSurfaceVariant,
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = TextSecondaryDark,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${hours.toInt()}h",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                text = "~$daysEst días",
                fontSize = 10.sp,
                color = TextMutedDark
            )
        }
    }
}
