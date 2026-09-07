package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.GameCatalog
import com.example.data.model.CompatibilityStatus
import com.example.data.model.Game
import com.example.ui.theme.*
import com.example.viewmodel.AppSection
import com.example.viewmodel.GameCheckViewModel

@Composable
fun GameComparisonScreen(
    viewModel: GameCheckViewModel,
    onNavigateToGame: (String, AppSection) -> Unit
) {
    val compareAId by viewModel.compareGameAId.collectAsState()
    val compareBId by viewModel.compareGameBId.collectAsState()
    val userSpecs by viewModel.userSpecs.collectAsState()
    val profile by viewModel.playerProfile.collectAsState()
    val colors = AppTheme.colors

    val gameA = remember(compareAId) {
        GameCatalog.games.find { it.id == compareAId } ?: GameCatalog.games[0]
    }
    val gameB = remember(compareBId) {
        GameCatalog.games.find { it.id == compareBId } ?: GameCatalog.games[1]
    }

    val evalA = remember(gameA, userSpecs) {
        GameCatalog.evaluateCompatibility(gameA, userSpecs)
    }
    val evalB = remember(gameB, userSpecs) {
        GameCatalog.evaluateCompatibility(gameB, userSpecs)
    }

    var showPickerA by remember { mutableStateOf(false) }
    var showPickerB by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .testTag("game_comparison_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Selector Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("vs_selector_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚖️ Comparador Frente a Frente",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        IconButton(
                            onClick = { viewModel.swapCompareGames() },
                            modifier = Modifier.testTag("swap_games_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Intercambiar juegos",
                                tint = colors.primary
                            )
                        }
                    }
                    Text(
                        text = "Compara especificaciones, accesibilidad, duración y precios para saber cuál comprar:",
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Game A Box
                        ComparisonGameBox(
                            game = gameA,
                            label = "Juego 1",
                            onClick = { showPickerA = true },
                            modifier = Modifier.weight(1f),
                            testTag = "game_a_picker_trigger"
                        )

                        // VS Badge
                        Surface(
                            shape = CircleShape,
                            color = colors.primary.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, colors.primary),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "VS",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = colors.primary
                                )
                            }
                        }

                        // Game B Box
                        ComparisonGameBox(
                            game = gameB,
                            label = "Juego 2",
                            onClick = { showPickerB = true },
                            modifier = Modifier.weight(1f),
                            testTag = "game_b_picker_trigger"
                        )
                    }
                }
            }
        }

        // Smart Buying Verdict Card
        item {
            val betterPerf = when {
                evalA.percentage > evalB.percentage -> gameA.title
                evalB.percentage > evalA.percentage -> gameB.title
                else -> "Empate técnico"
            }

            val betterValue = when {
                gameA.costPerHourBase < gameB.costPerHourBase -> "${gameA.title} ($${String.format("%.2f", gameA.costPerHourBase)}/h)"
                gameB.costPerHourBase < gameA.costPerHourBase -> "${gameB.title} ($${String.format("%.2f", gameB.costPerHourBase)}/h)"
                else -> "Similar relación valor/hora"
            }

            val lessStorage = when {
                gameA.storageRequiredGb < gameB.storageRequiredGb -> "${gameA.title} (${gameA.storageRequiredGb}GB vs ${gameB.storageRequiredGb}GB)"
                gameB.storageRequiredGb < gameA.storageRequiredGb -> "${gameB.title} (${gameB.storageRequiredGb}GB vs ${gameA.storageRequiredGb}GB)"
                else -> "Mismo espacio (${gameA.storageRequiredGb}GB)"
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buying_verdict_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.5.dp, colors.primary)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "¿Cuál deberías comprar?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        )
                    }
                    Text(
                        text = "Veredicto inteligente según tu perfil (${profile.gamerTag}):",
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    VerdictBulletRow(
                        icon = "⚡",
                        title = "Mejor rendimiento en tu PC",
                        detail = "$betterPerf (A: ${evalA.percentage}% vs B: ${evalB.percentage}%)"
                    )
                    VerdictBulletRow(
                        icon = "💰",
                        title = "Más horas por tu dinero",
                        detail = betterValue
                    )
                    VerdictBulletRow(
                        icon = "💾",
                        title = "Menor uso de almacenamiento",
                        detail = lessStorage
                    )
                    VerdictBulletRow(
                        icon = "♿",
                        title = "Accesibilidad global",
                        detail = "A: Grado ${gameA.accessibility.scoreLetter} vs B: Grado ${gameB.accessibility.scoreLetter}"
                    )
                }
            }
        }

        // Section 1: Requisitos & Porcentaje de Compatibilidad
        item {
            ComparisonSectionCard(
                title = "⚡ Requisitos & Compatibilidad",
                subtitle = "Porcentaje calculado según los 5 componentes de tu PC"
            ) {
                // Compatibility percentage progress row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        PercentageMeter(percentage = evalA.percentage)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = evalA.status.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = getPercentageColor(evalA.percentage),
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        PercentageMeter(percentage = evalB.percentage)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = evalB.status.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = getPercentageColor(evalB.percentage),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                ComparisonRow(
                    label = "Espacio en Disco",
                    valA = "${gameA.storageRequiredGb} GB",
                    valB = "${gameB.storageRequiredGb} GB",
                    isBetterA = gameA.storageRequiredGb < gameB.storageRequiredGb,
                    isBetterB = gameB.storageRequiredGb < gameA.storageRequiredGb
                )
                ComparisonRow(
                    label = "RAM Mínima",
                    valA = "${gameA.minRamGb} GB",
                    valB = "${gameB.minRamGb} GB",
                    isBetterA = gameA.minRamGb < gameB.minRamGb,
                    isBetterB = gameB.minRamGb < gameA.minRamGb
                )
                ComparisonRow(
                    label = "RAM Recomendada",
                    valA = "${gameA.recRamGb} GB",
                    valB = "${gameB.recRamGb} GB"
                )
                ComparisonRow(
                    label = "GPU Recomendada",
                    valA = gameA.recGpuText.substringBefore(" o "),
                    valB = gameB.recGpuText.substringBefore(" o ")
                )
                ComparisonRow(
                    label = "CPU Recomendada",
                    valA = gameA.recCpuText.substringBefore(" o "),
                    valB = gameB.recCpuText.substringBefore(" o ")
                )
            }
        }

        // Section 2: Accesibilidad
        item {
            ComparisonSectionCard(
                title = "♿ Accesibilidad Comparada",
                subtitle = "Opciones para personas con distintas necesidades"
            ) {
                ComparisonRow(
                    label = "Calificación Global",
                    valA = "Grado ${gameA.accessibility.scoreLetter}",
                    valB = "Grado ${gameB.accessibility.scoreLetter}",
                    isBetterA = gameA.accessibility.scoreLetter == "A+" || (gameA.accessibility.scoreLetter == "A" && gameB.accessibility.scoreLetter != "A+"),
                    isBetterB = gameB.accessibility.scoreLetter == "A+" || (gameB.accessibility.scoreLetter == "A" && gameA.accessibility.scoreLetter != "A+")
                )
                ComparisonRow(
                    label = "Subtítulos Ajustables",
                    valA = if (gameA.accessibility.subtitleSizeAdjustable) "✅ Sí" else "❌ No",
                    valB = if (gameB.accessibility.subtitleSizeAdjustable) "✅ Sí" else "❌ No"
                )
                ComparisonRow(
                    label = "Remapeo de Botones",
                    valA = if (gameA.accessibility.fullButtonRemapping) "✅ Completo" else "❌ No",
                    valB = if (gameB.accessibility.fullButtonRemapping) "✅ Completo" else "❌ No"
                )
                ComparisonRow(
                    label = "Asistencia de Puntería",
                    valA = if (gameA.accessibility.aimAssistAdjustable) "✅ Ajustable" else "❌ No",
                    valB = if (gameB.accessibility.aimAssistAdjustable) "✅ Ajustable" else "❌ No"
                )
                ComparisonRow(
                    label = "Filtros Daltónicos",
                    valA = if (gameA.accessibility.colorblindFilters.isNotEmpty()) "✅ ${gameA.accessibility.colorblindFilters.size} tipos" else "❌ No",
                    valB = if (gameB.accessibility.colorblindFilters.isNotEmpty()) "✅ ${gameB.accessibility.colorblindFilters.size} tipos" else "❌ No"
                )
                ComparisonRow(
                    label = "Audio & Voces en Español",
                    valA = if (gameA.accessibility.fullSpanishAudioAndSub) "✅ Sí" else "❌ No",
                    valB = if (gameB.accessibility.fullSpanishAudioAndSub) "✅ Sí" else "❌ No"
                )
                ComparisonRow(
                    label = "Modo Alto Contraste",
                    valA = if (gameA.accessibility.highContrastMode) "✅ Sí" else "❌ No",
                    valB = if (gameB.accessibility.highContrastMode) "✅ Sí" else "❌ No"
                )
            }
        }

        // Section 3: Duración & Ritmo
        item {
            val daysA = Math.ceil((gameA.duration.mainStoryHours / profile.dailyHoursAvailable).toDouble()).toInt()
            val daysB = Math.ceil((gameB.duration.mainStoryHours / profile.dailyHoursAvailable).toDouble()).toInt()

            ComparisonSectionCard(
                title = "⏱️ Duración & Horas",
                subtitle = "Estimación según tus ${profile.dailyHoursAvailable}h disponibles al día"
            ) {
                ComparisonRow(
                    label = "Historia Principal",
                    valA = "${gameA.duration.mainStoryHours.toInt()} horas",
                    valB = "${gameB.duration.mainStoryHours.toInt()} horas"
                )
                ComparisonRow(
                    label = "Historia + Extras",
                    valA = "${gameA.duration.mainPlusExtraHours.toInt()} horas",
                    valB = "${gameB.duration.mainPlusExtraHours.toInt()} horas"
                )
                ComparisonRow(
                    label = "100% Completista",
                    valA = "${gameA.duration.completionistHours.toInt()} horas",
                    valB = "${gameB.duration.completionistHours.toInt()} horas"
                )
                ComparisonRow(
                    label = "Días para terminarlo",
                    valA = "~$daysA días",
                    valB = "~$daysB días",
                    isBetterA = daysA < daysB,
                    isBetterB = daysB < daysA
                )
            }
        }

        // Section 4: Precios & Presupuesto
        item {
            val budget = profile.defaultBudgetUsd
            val fitsA = gameA.pricing.basePriceUsd <= budget
            val fitsB = gameB.pricing.basePriceUsd <= budget

            ComparisonSectionCard(
                title = "💰 Precios & Ediciones",
                subtitle = "Comparación de precio y relación valor/hora"
            ) {
                ComparisonRow(
                    label = "Precio Juego Base",
                    valA = "$${String.format("%.2f", gameA.pricing.basePriceUsd)}",
                    valB = "$${String.format("%.2f", gameB.pricing.basePriceUsd)}",
                    isBetterA = gameA.pricing.basePriceUsd < gameB.pricing.basePriceUsd,
                    isBetterB = gameB.pricing.basePriceUsd < gameA.pricing.basePriceUsd
                )
                ComparisonRow(
                    label = "Precio Base + Extras",
                    valA = "$${String.format("%.2f", gameA.pricing.deluxePriceUsd)}",
                    valB = "$${String.format("%.2f", gameB.pricing.deluxePriceUsd)}",
                    isBetterA = gameA.pricing.deluxePriceUsd < gameB.pricing.deluxePriceUsd,
                    isBetterB = gameB.pricing.deluxePriceUsd < gameA.pricing.deluxePriceUsd
                )
                ComparisonRow(
                    label = "Diferencia de Extras",
                    valA = "+$${String.format("%.2f", gameA.pricing.extrasDifferenceUsd)}",
                    valB = "+$${String.format("%.2f", gameB.pricing.extrasDifferenceUsd)}"
                )
                ComparisonRow(
                    label = "Costo por Hora (Base)",
                    valA = "$${String.format("%.2f", gameA.costPerHourBase)}/h",
                    valB = "$${String.format("%.2f", gameB.costPerHourBase)}/h",
                    isBetterA = gameA.costPerHourBase < gameB.costPerHourBase,
                    isBetterB = gameB.costPerHourBase < gameA.costPerHourBase
                )
                ComparisonRow(
                    label = "Te alcanza (Presupuesto $${budget.toInt()})",
                    valA = if (fitsA) "✅ Sí te alcanza" else "❌ Supera",
                    valB = if (fitsB) "✅ Sí te alcanza" else "❌ Supera",
                    isBetterA = fitsA && !fitsB,
                    isBetterB = fitsB && !fitsA
                )
            }
        }

        // Section 5: Contenido Sensible
        item {
            ComparisonSectionCard(
                title = "🛡️ Contenido & Sensibilidad",
                subtitle = "Revisa niveles de violencia, gore y temas maduros"
            ) {
                ComparisonRow(
                    label = "Nivel de Violencia",
                    valA = gameA.contentWarnings.violenceLevel,
                    valB = gameB.contentWarnings.violenceLevel
                )
                ComparisonRow(
                    label = "Sangre / Gore",
                    valA = gameA.contentWarnings.bloodGore,
                    valB = gameB.contentWarnings.bloodGore
                )
                ComparisonRow(
                    label = "Terror / Tensión",
                    valA = gameA.contentWarnings.horrorLevel,
                    valB = gameB.contentWarnings.horrorLevel
                )
                ComparisonRow(
                    label = "Lenguaje Fuerte",
                    valA = gameA.contentWarnings.strongLanguage,
                    valB = gameB.contentWarnings.strongLanguage
                )
            }
        }

        // Section 6: Calificaciones de la Comunidad
        item {
            ComparisonSectionCard(
                title = "⭐ Calificaciones de la Comunidad",
                subtitle = "Evaluación promedio sobre 5.0 en 6 categorías"
            ) {
                ComparisonRow(
                    label = "Historia / Narrativa",
                    valA = "★ ${gameA.initialRatings.historia}",
                    valB = "★ ${gameB.initialRatings.historia}",
                    isBetterA = gameA.initialRatings.historia > gameB.initialRatings.historia,
                    isBetterB = gameB.initialRatings.historia > gameA.initialRatings.historia
                )
                ComparisonRow(
                    label = "Jugabilidad",
                    valA = "★ ${gameA.initialRatings.jugabilidad}",
                    valB = "★ ${gameB.initialRatings.jugabilidad}",
                    isBetterA = gameA.initialRatings.jugabilidad > gameB.initialRatings.jugabilidad,
                    isBetterB = gameB.initialRatings.jugabilidad > gameA.initialRatings.jugabilidad
                )
                ComparisonRow(
                    label = "Gráficos / Visuales",
                    valA = "★ ${gameA.initialRatings.graficos}",
                    valB = "★ ${gameB.initialRatings.graficos}",
                    isBetterA = gameA.initialRatings.graficos > gameB.initialRatings.graficos,
                    isBetterB = gameB.initialRatings.graficos > gameA.initialRatings.graficos
                )
                ComparisonRow(
                    label = "Optimización en PC",
                    valA = "★ ${gameA.initialRatings.optimizacion}",
                    valB = "★ ${gameB.initialRatings.optimizacion}",
                    isBetterA = gameA.initialRatings.optimizacion > gameB.initialRatings.optimizacion,
                    isBetterB = gameB.initialRatings.optimizacion > gameA.initialRatings.optimizacion
                )
            }
        }

        // Action Buttons: Ir a Ficha
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onNavigateToGame(gameA.id, AppSection.GAME_DETAILS) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                ) {
                    Text("Ficha ${gameA.title.take(12)}…", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Button(
                    onClick = { onNavigateToGame(gameB.id, AppSection.GAME_DETAILS) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentViolet)
                ) {
                    Text("Ficha ${gameB.title.take(12)}…", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }

    // Modal Game Picker A
    if (showPickerA) {
        GameSelectionDialog(
            title = "Selecciona el Juego 1",
            games = GameCatalog.games,
            onDismiss = { showPickerA = false },
            onSelect = {
                viewModel.setCompareGameA(it.id)
                showPickerA = false
            }
        )
    }

    // Modal Game Picker B
    if (showPickerB) {
        GameSelectionDialog(
            title = "Selecciona el Juego 2",
            games = GameCatalog.games,
            onDismiss = { showPickerB = false },
            onSelect = {
                viewModel.setCompareGameB(it.id)
                showPickerB = false
            }
        )
    }
}

@Composable
fun ComparisonGameBox(
    game: Game,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    val colors = AppTheme.colors
    Surface(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        color = colors.surfaceVariant,
        border = BorderStroke(1.dp, colors.surfaceBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 10.sp, color = colors.primary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color(game.bannerColorHex).copy(alpha = 0.25f),
                border = BorderStroke(1.dp, Color(game.bannerColorHex).copy(alpha = 0.6f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = game.coverEmoji, fontSize = 24.sp)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = game.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Text(
                text = "$${String.format("%.2f", game.pricing.basePriceUsd)} USD",
                fontSize = 11.sp,
                color = colors.textSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = colors.primary.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "Cambiar ▾",
                    fontSize = 10.sp,
                    color = colors.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun ComparisonSectionCard(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = AppTheme.colors
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.surfaceBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = colors.textSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun ComparisonRow(
    label: String,
    valA: String,
    valB: String,
    isBetterA: Boolean = false,
    isBetterB: Boolean = false
) {
    val colors = AppTheme.colors
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        shape = RoundedCornerShape(8.dp),
        color = colors.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = colors.textMuted,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = valA,
                    fontSize = 12.sp,
                    fontWeight = if (isBetterA) FontWeight.Black else FontWeight.Medium,
                    color = if (isBetterA) RunGreatGreen else colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "vs",
                    fontSize = 10.sp,
                    color = colors.textMuted,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                Text(
                    text = valB,
                    fontSize = 12.sp,
                    fontWeight = if (isBetterB) FontWeight.Black else FontWeight.Medium,
                    color = if (isBetterB) RunGreatGreen else colors.textPrimary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PercentageMeter(percentage: Int) {
    val color = getPercentageColor(percentage)
    val colors = AppTheme.colors

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(68.dp)
        ) {
            CircularProgressIndicator(
                progress = { percentage / 100f },
                modifier = Modifier.fillMaxSize(),
                color = color,
                trackColor = colors.surfaceBorder,
                strokeWidth = 6.dp
            )
            Text(
                text = "$percentage%",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}

fun getPercentageColor(percentage: Int): Color {
    return when {
        percentage >= 75 -> RunGreatGreen
        percentage >= 50 -> RunMediumYellow
        else -> RunNoRed
    }
}

@Composable
fun VerdictBulletRow(icon: String, title: String, detail: String) {
    val colors = AppTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = icon, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            Text(text = detail, fontSize = 11.sp, color = colors.textSecondary)
        }
    }
}

@Composable
fun GameSelectionDialog(
    title: String,
    games: List<Game>,
    onDismiss: () -> Unit,
    onSelect: (Game) -> Unit
) {
    val colors = AppTheme.colors
    var query by remember { mutableStateOf("") }
    val filtered = remember(query) {
        if (query.isBlank()) games
        else games.filter { it.title.contains(query, ignoreCase = true) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = colors.textSecondary)
            }
        },
        title = {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Buscar título...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(filtered.size) { idx ->
                        val g = filtered[idx]
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable { onSelect(g) },
                            shape = RoundedCornerShape(10.dp),
                            color = colors.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = g.coverEmoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = g.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.textPrimary
                                    )
                                    Text(
                                        text = "${g.developer} • $${String.format("%.2f", g.pricing.basePriceUsd)} USD",
                                        fontSize = 11.sp,
                                        color = colors.textSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        containerColor = colors.surface
    )
}
