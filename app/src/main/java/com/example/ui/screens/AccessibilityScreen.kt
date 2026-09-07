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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import com.example.data.model.AccessibilityFeatures
import com.example.data.model.Game
import com.example.ui.theme.*
import com.example.viewmodel.AppSection
import com.example.viewmodel.GameCheckViewModel

@Composable
fun AccessibilityScreen(
    viewModel: GameCheckViewModel,
    onNavigateToGame: (String, AppSection) -> Unit
) {
    val filterSubtitles by viewModel.filterSubtitles.collectAsState()
    val filterControls by viewModel.filterControlsRemap.collectAsState()
    val filterVisual by viewModel.filterVisualAssist.collectAsState()
    val filterColorblind by viewModel.filterColorblind.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var expandedGameId by remember { mutableStateOf<String?>("cyberpunk_2077") }

    val filteredGames = remember(
        filterSubtitles,
        filterControls,
        filterVisual,
        filterColorblind,
        searchQuery
    ) {
        GameCatalog.games.filter { game ->
            val matchSearch = searchQuery.isBlank() ||
                    game.title.contains(searchQuery, ignoreCase = true) ||
                    game.genres.any { it.contains(searchQuery, ignoreCase = true) }

            val matchSub = !filterSubtitles || (game.accessibility.subtitleSizeAdjustable && game.accessibility.speakerIdentification)
            val matchCtrl = !filterControls || (game.accessibility.fullButtonRemapping && game.accessibility.toggleVsHoldOption)
            val matchVis = !filterVisual || (game.accessibility.highContrastMode || game.accessibility.hudScaling)
            val matchColor = !filterColorblind || game.accessibility.colorblindFilters.isNotEmpty()

            matchSearch && matchSub && matchCtrl && matchVis && matchColor
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("accessibility_screen_list"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Explanatory Banner
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
                        Text(text = "♿", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Opciones de Accesibilidad",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Comprueba qué facilidades ofrece cada juego antes de comprarlo",
                                fontSize = 12.sp,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Filtrar por requisitos indispensables:",
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
                            selected = filterSubtitles,
                            onClick = { viewModel.toggleFilterSubtitles() },
                            label = { Text("💬 Subtítulos avanzados") },
                            modifier = Modifier.testTag("filter_subtitles_chip")
                        )
                        FilterChip(
                            selected = filterControls,
                            onClick = { viewModel.toggleFilterControlsRemap() },
                            label = { Text("🎮 Remapeo de controles") },
                            modifier = Modifier.testTag("filter_controls_chip")
                        )
                        FilterChip(
                            selected = filterVisual,
                            onClick = { viewModel.toggleFilterVisualAssist() },
                            label = { Text("👁️ Asistencia visual / HUD") },
                            modifier = Modifier.testTag("filter_visual_chip")
                        )
                        FilterChip(
                            selected = filterColorblind,
                            onClick = { viewModel.toggleFilterColorblind() },
                            label = { Text("🎨 Filtros daltonismo") },
                            modifier = Modifier.testTag("filter_colorblind_chip")
                        )
                    }
                }
            }
        }

        // Result count header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mostrando ${filteredGames.size} juegos analizados",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentTeal
                )
                Text(
                    text = "Toca para ver el desglose completo",
                    fontSize = 11.sp,
                    color = TextMutedDark
                )
            }
        }

        items(filteredGames, key = { it.id }) { game ->
            val isExpanded = expandedGameId == game.id
            AccessibilityGameCard(
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
fun AccessibilityGameCard(
    game: Game,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onViewFullFile: () -> Unit
) {
    val acc = game.accessibility
    val gradeColor = when (acc.scoreLetter) {
        "A+", "A" -> RunGreatGreen
        "B+", "B" -> RunMediumYellow
        else -> RunNoRed
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("accessibility_card_${game.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, if (isExpanded) AccentTeal else DarkSurfaceBorder)
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
                            text = "${game.developer} • ${game.genres.firstOrNull() ?: "General"}",
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Grade Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = gradeColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, gradeColor)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Nivel ",
                                fontSize = 10.sp,
                                color = TextSecondaryDark
                            )
                            Text(
                                text = acc.scoreLetter,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = gradeColor
                            )
                        }
                    }

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expandir detalles de accesibilidad",
                        tint = AccentTeal
                    )
                }
            }

            // Quick summary tags (always visible)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                QuickTag(
                    icon = "💬",
                    label = "Subtítulos",
                    available = acc.subtitleSizeAdjustable,
                    modifier = Modifier.weight(1f)
                )
                QuickTag(
                    icon = "🎮",
                    label = "Controles",
                    available = acc.fullButtonRemapping,
                    modifier = Modifier.weight(1f)
                )
                QuickTag(
                    icon = "👁️",
                    label = "Asist. Visual",
                    available = acc.highContrastMode || acc.hudScaling,
                    modifier = Modifier.weight(1f)
                )
            }

            // Expanded Breakdown for the 3 key categories
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Divider(color = DarkSurfaceBorder)

                    // 1. Subtítulos
                    AccessibilityCategoryBlock(
                        title = "1. Opciones de Subtítulos",
                        icon = "💬",
                        items = listOf(
                            "Tamaño de texto y fuente ajustable" to acc.subtitleSizeAdjustable,
                            "Fondo con contraste / opacidad para lectura" to acc.subtitleBackgroundContrast,
                            "Identificación de quién habla (Speaker ID)" to acc.speakerIdentification,
                            "Indicadores de dirección de sonidos/efectos" to acc.directionalSoundCues,
                            "Subtítulos y audio completo en Español" to acc.fullSpanishAudioAndSub
                        )
                    )

                    // 2. Personalización de Controles
                    AccessibilityCategoryBlock(
                        title = "2. Personalización de Controles",
                        icon = "🎮",
                        items = listOf(
                            "Remapeo completo de botones y teclas" to acc.fullButtonRemapping,
                            "Alternar vs Mantener pulsado (Toggle/Hold)" to acc.toggleVsHoldOption,
                            "Asistencia de puntería / Auto-apuntado ajustable" to acc.aimAssistAdjustable,
                            "Esquema de controles simplificado" to acc.simplifiedControlScheme,
                            "Compatible con mandos adaptativos (Xbox/PlayStation)" to acc.adaptiveControllerCompatible
                        )
                    )

                    // 3. Modos de Asistencia Visual
                    AccessibilityCategoryBlock(
                        title = "3. Modos de Asistencia Visual",
                        icon = "👁️",
                        items = listOf(
                            "Filtros para Daltonismo (${if (acc.colorblindFilters.isNotEmpty()) acc.colorblindFilters.joinToString() else "No incluido"})" to acc.colorblindFilters.isNotEmpty(),
                            "Modo de Alto Contraste (Resaltado de siluetas y enemigos)" to acc.highContrastMode,
                            "Escalado de Interfaz de Usuario (HUD y menús)" to acc.hudScaling,
                            "Lector de pantalla / Narración de menús (TTS)" to acc.textToSpeechScreenReader,
                            "Reducción de mareo por movimiento y parpadeos" to acc.reduceMotionAndFlashes
                        )
                    )

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = onViewFullFile,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentTeal),
                            border = BorderStroke(1.dp, AccentTeal),
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
fun QuickTag(icon: String, label: String, available: Boolean, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (available) RunGreatGreen.copy(alpha = 0.12f) else DarkSurfaceVariant,
        border = BorderStroke(1.dp, if (available) RunGreatGreen.copy(alpha = 0.4f) else DarkSurfaceBorder),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
        ) {
            Text(text = icon, fontSize = 11.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (available) RunGreatGreen else TextMutedDark
            )
        }
    }
}

@Composable
fun AccessibilityCategoryBlock(
    title: String,
    icon: String,
    items: List<Pair<String, Boolean>>
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentTeal
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            items.forEach { (text, included) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (included) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (included) RunGreatGreen else Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = text,
                        fontSize = 12.sp,
                        color = if (included) TextPrimaryDark else TextMutedDark
                    )
                }
            }
        }
    }
}
