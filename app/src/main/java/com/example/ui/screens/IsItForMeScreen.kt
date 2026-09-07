package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Warning
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
import com.example.data.model.ContentWarnings
import com.example.data.model.Game
import com.example.ui.theme.*
import com.example.viewmodel.AppSection
import com.example.viewmodel.GameCheckViewModel

@Composable
fun IsItForMeScreen(
    viewModel: GameCheckViewModel,
    onNavigateToGame: (String, AppSection) -> Unit
) {
    val avoidExtremeGore by viewModel.avoidExtremeGore.collectAsState()
    val avoidHorror by viewModel.avoidHorror.collectAsState()
    val avoidStrongLanguage by viewModel.avoidStrongLanguage.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var expandedGameId by remember { mutableStateOf<String?>("silent_hill_2_remake") }

    val filteredGames = remember(avoidExtremeGore, avoidHorror, avoidStrongLanguage, searchQuery) {
        GameCatalog.games.filter { game ->
            val matchSearch = searchQuery.isBlank() ||
                    game.title.contains(searchQuery, ignoreCase = true) ||
                    game.genres.any { it.contains(searchQuery, ignoreCase = true) }

            val matchGore = !avoidExtremeGore || game.contentWarnings.bloodScore <= 3
            val matchHorror = !avoidHorror || game.contentWarnings.horrorScore <= 2
            val matchLang = !avoidStrongLanguage || game.contentWarnings.languageScore <= 3

            matchSearch && matchGore && matchHorror && matchLang
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("is_it_for_me_list"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Banner card with comfort filters
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, DarkSurfaceBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🛡️", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "¿Es para mí? (Guía de Contenido)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Descubre violencia, sangre, terror, lenguaje y temas sensibles",
                                fontSize = 12.sp,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Mis preferencias de contenido (Ocultar si contiene):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = avoidExtremeGore,
                            onClick = { viewModel.toggleAvoidExtremeGore() },
                            label = { Text("🚫 Sin gore extremo") },
                            modifier = Modifier.testTag("avoid_gore_chip")
                        )
                        FilterChip(
                            selected = avoidHorror,
                            onClick = { viewModel.toggleAvoidHorror() },
                            label = { Text("🚫 Sin terror ni sustos") },
                            modifier = Modifier.testTag("avoid_horror_chip")
                        )
                        FilterChip(
                            selected = avoidStrongLanguage,
                            onClick = { viewModel.toggleAvoidStrongLanguage() },
                            label = { Text("🚫 Sin lenguaje explícito") },
                            modifier = Modifier.testTag("avoid_language_chip")
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mostrando ${filteredGames.size} de ${GameCatalog.games.size} juegos",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentTeal
                )
                Text(
                    text = "Toca para ver el desglose",
                    fontSize = 11.sp,
                    color = TextMutedDark
                )
            }
        }

        items(filteredGames, key = { it.id }) { game ->
            val isExpanded = expandedGameId == game.id
            ContentWarningGameCard(
                game = game,
                isExpanded = isExpanded,
                onToggleExpand = {
                    expandedGameId = if (isExpanded) null else game.id
                },
                onViewFullFile = { onNavigateToGame(game.id, AppSection.GAME_DETAILS) }
            )
        }
    }
}

@Composable
fun ContentWarningGameCard(
    game: Game,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onViewFullFile: () -> Unit
) {
    val warn = game.contentWarnings
    val maxScore = maxOf(warn.violenceScore, warn.bloodScore, warn.horrorScore, warn.languageScore)

    val intensityBadgeColor = when {
        maxScore >= 4 -> RunNoRed
        maxScore == 3 -> RunMediumYellow
        else -> RunGreatGreen
    }

    val intensityBadgeText = when {
        maxScore >= 4 -> "Contenido Maduro"
        maxScore == 3 -> "Contenido Moderado"
        else -> "Apto y Tranquilo"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("content_card_${game.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, if (isExpanded) AccentViolet else DarkSurfaceBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleExpand),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(game.bannerColorHex).copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color(game.bannerColorHex).copy(alpha = 0.5f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = game.coverEmoji, fontSize = 22.sp)
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
                            text = "Clasificación: ${warn.ageRating}",
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = intensityBadgeColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, intensityBadgeColor)
                    ) {
                        Text(
                            text = intensityBadgeText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = intensityBadgeColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expandir advertencias",
                        tint = AccentViolet
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Mini Indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ContentScoreIndicator(label = "Violencia", score = warn.violenceScore, modifier = Modifier.weight(1f))
                ContentScoreIndicator(label = "Sangre", score = warn.bloodScore, modifier = Modifier.weight(1f))
                ContentScoreIndicator(label = "Terror", score = warn.horrorScore, modifier = Modifier.weight(1f))
                ContentScoreIndicator(label = "Lenguaje", score = warn.languageScore, modifier = Modifier.weight(1f))
            }

            // Sensitive Themes tags
            if (warn.sensitiveThemes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    warn.sensitiveThemes.forEach { theme ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF1E293B)
                        ) {
                            Text(
                                text = "⚠️ $theme",
                                fontSize = 10.sp,
                                color = Color(0xFFE2E8F0),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // Expanded detail section
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Divider(color = DarkSurfaceBorder)

                    Text(
                        text = "📋 Resumen Detallado de Contenido:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentViolet
                    )

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = DarkSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = warn.detailedSummary,
                            fontSize = 12.sp,
                            color = Color(0xFFE5E7EB),
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    // Breakdown cards for the 4 core topics
                    ContentTopicRow(icon = "⚔️", title = "Violencia y Combate", detail = warn.violenceLevel, score = warn.violenceScore)
                    ContentTopicRow(icon = "🩸", title = "Sangre y Gore", detail = warn.bloodGore, score = warn.bloodScore)
                    ContentTopicRow(icon = "👻", title = "Terror y Sustos", detail = warn.horrorLevel, score = warn.horrorScore)
                    ContentTopicRow(icon = "🤬", title = "Lenguaje Fuerte", detail = warn.strongLanguage, score = warn.languageScore)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = onViewFullFile,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentViolet),
                            border = BorderStroke(1.dp, AccentViolet),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Ver ficha del juego completa →", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContentScoreIndicator(label: String, score: Int, modifier: Modifier = Modifier) {
    val color = when {
        score >= 4 -> RunNoRed
        score == 3 -> RunMediumYellow
        else -> RunGreatGreen
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
        ) {
            Text(text = label, fontSize = 10.sp, color = TextSecondaryDark)
            Text(
                text = "$score/5",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun ContentTopicRow(icon: String, title: String, detail: String, score: Int) {
    val color = when {
        score >= 4 -> RunNoRed
        score == 3 -> RunMediumYellow
        else -> RunGreatGreen
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DarkSurfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = icon, fontSize = 16.sp)
                Column {
                    Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = detail, fontSize = 11.sp, color = color)
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = color.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "Nivel $score",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
