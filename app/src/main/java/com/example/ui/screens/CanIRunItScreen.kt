package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.GameCatalog
import com.example.data.model.CompatibilityStatus
import com.example.data.model.Game
import com.example.ui.theme.*
import com.example.viewmodel.AppSection
import com.example.viewmodel.CompatibilityFilter
import com.example.viewmodel.GameCheckViewModel

@Composable
fun CanIRunItScreen(
    viewModel: GameCheckViewModel,
    onNavigateToGame: (String, AppSection) -> Unit
) {
    val userSpecs by viewModel.userSpecs.collectAsState()
    val profile by viewModel.playerProfile.collectAsState()
    val compatFilter by viewModel.compatFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val colors = AppTheme.colors

    var specsExpanded by remember { mutableStateOf(false) }

    val evaluatedGames = remember(userSpecs, searchQuery) {
        GameCatalog.games
            .filter {
                if (searchQuery.isBlank()) true
                else it.title.contains(searchQuery, ignoreCase = true) ||
                        it.genres.any { g -> g.contains(searchQuery, ignoreCase = true) }
            }
            .map { game ->
                val eval = GameCatalog.evaluateCompatibility(game, userSpecs)
                Pair(game, eval)
            }
    }

    val filteredList = remember(evaluatedGames, compatFilter) {
        when (compatFilter) {
            CompatibilityFilter.ALL -> evaluatedGames
            CompatibilityFilter.RUNS_GREAT -> evaluatedGames.filter { it.second.percentage >= 75 }
            CompatibilityFilter.RUNS_MEDIUM -> evaluatedGames.filter { it.second.percentage in 50..74 }
            CompatibilityFilter.RUNS_NO -> evaluatedGames.filter { it.second.percentage < 50 }
        }
    }

    val countGreat = evaluatedGames.count { it.second.percentage >= 75 }
    val countMedium = evaluatedGames.count { it.second.percentage in 50..74 }
    val countNo = evaluatedGames.count { it.second.percentage < 50 }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .testTag("can_i_run_it_list"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Player profile quick banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onNavigateToGame("", AppSection.PLAYER_PROFILE) }
                    .testTag("profile_quick_banner"),
                shape = RoundedCornerShape(12.dp),
                color = colors.surfaceVariant,
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = profile.avatarEmoji, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Perfil: ${profile.gamerTag}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Text(
                                text = "Tus especificaciones se guardan automáticamente en tu perfil",
                                fontSize = 10.sp,
                                color = colors.textSecondary
                            )
                        }
                    }
                    Text(
                        text = "Ajustar ›",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                }
            }
        }
        // Specs configuration card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("user_specs_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { specsExpanded = !specsExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "⚙️ Tus Especificaciones",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = colors.primary.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "5 Componentes",
                                        fontSize = 10.sp,
                                        color = colors.primary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${userSpecs.cpuName.substringBefore(" (")} • ${userSpecs.gpuName.substringBefore(" (")} • ${userSpecs.ramGb}GB",
                                fontSize = 12.sp,
                                color = colors.textSecondary,
                                maxLines = 1
                            )
                        }
                        IconButton(onClick = { specsExpanded = !specsExpanded }) {
                            Icon(
                                imageVector = if (specsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Alternar casillas de hardware",
                                tint = colors.primary
                            )
                        }
                    }

                    AnimatedVisibility(visible = specsExpanded) {
                        Column(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Quick presets
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Cargar perfil:", fontSize = 11.sp, color = TextMutedDark)
                                FilterChip(
                                    selected = false,
                                    onClick = { viewModel.applyPreset("entry") },
                                    label = { Text("Básico", fontSize = 11.sp) },
                                    modifier = Modifier.testTag("preset_entry_chip")
                                )
                                FilterChip(
                                    selected = false,
                                    onClick = { viewModel.applyPreset("medium") },
                                    label = { Text("Gama Media", fontSize = 11.sp) },
                                    modifier = Modifier.testTag("preset_medium_chip")
                                )
                                FilterChip(
                                    selected = false,
                                    onClick = { viewModel.applyPreset("high") },
                                    label = { Text("Gama Alta", fontSize = 11.sp) },
                                    modifier = Modifier.testTag("preset_high_chip")
                                )
                            }

                            // 1. CPU Casilla
                            SpecDropdownSelector(
                                label = "1. Procesador (CPU)",
                                selectedName = userSpecs.cpuName,
                                options = GameCatalog.cpuOptions.map { it.name to it.score },
                                onSelect = { name, score -> viewModel.updateCpu(name, score) },
                                testTag = "cpu_selector"
                            )

                            // 2. GPU Casilla
                            SpecDropdownSelector(
                                label = "2. Tarjeta gráfica (GPU)",
                                selectedName = userSpecs.gpuName,
                                options = GameCatalog.gpuOptions.map { it.name to it.score },
                                onSelect = { name, score -> viewModel.updateGpu(name, score) },
                                testTag = "gpu_selector"
                            )

                            // 3. RAM Casilla
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "3. Memoria RAM: ${userSpecs.ramGb} GB",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimaryDark
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    GameCatalog.ramOptions.forEach { ram ->
                                        val isSelected = userSpecs.ramGb == ram
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) AccentTeal else DarkSurfaceVariant,
                                            border = BorderStroke(
                                                1.dp,
                                                if (isSelected) AccentTeal else DarkSurfaceBorder
                                            ),
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { viewModel.updateRam(ram) }
                                                .testTag("ram_${ram}gb_button")
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            ) {
                                                Text(
                                                    text = "${ram}GB",
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) Color(0xFF090D16) else TextPrimaryDark
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 4. Sistema Operativo Casilla
                            SpecStringDropdownSelector(
                                label = "4. Sistema Operativo (OS)",
                                selectedValue = userSpecs.os,
                                options = GameCatalog.osOptions,
                                onSelect = { viewModel.updateOs(it) },
                                testTag = "os_selector"
                            )

                            // 5. Almacenamiento Disponible Casilla
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "5. Almacenamiento disponible",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimaryDark
                                    )
                                    Text(
                                        text = if (userSpecs.storageGb >= 1000) "${userSpecs.storageGb / 1000} TB libres" else "${userSpecs.storageGb} GB libres",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentTeal
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    GameCatalog.storageOptions.forEach { storage ->
                                        val isSelected = userSpecs.storageGb == storage
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) AccentViolet else DarkSurfaceVariant,
                                            border = BorderStroke(
                                                1.dp,
                                                if (isSelected) AccentViolet else DarkSurfaceBorder
                                            ),
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { viewModel.updateStorage(storage) }
                                                .testTag("storage_${storage}gb_button")
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            ) {
                                                Text(
                                                    text = if (storage >= 1000) "${storage / 1000}T" else "${storage}G",
                                                    fontSize = 11.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Summary Metric Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusCounterItem(
                        count = countGreat,
                        label = "75% - 100%",
                        sublabel = "Corre óptimo",
                        color = RunGreatGreen,
                        icon = "🟢"
                    )
                    Divider(
                        modifier = Modifier
                            .height(32.dp)
                            .width(1.dp),
                        color = colors.surfaceBorder
                    )
                    StatusCounterItem(
                        count = countMedium,
                        label = "50% - 74%",
                        sublabel = "Ajustado",
                        color = RunMediumYellow,
                        icon = "🟡"
                    )
                    Divider(
                        modifier = Modifier
                            .height(32.dp)
                            .width(1.dp),
                        color = colors.surfaceBorder
                    )
                    StatusCounterItem(
                        count = countNo,
                        label = "0% - 49%",
                        sublabel = "Insuficiente",
                        color = RunNoRed,
                        icon = "🔴"
                    )
                }
            }
        }

        // Filter Pills
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterTabChip(
                    text = "Todos (${evaluatedGames.size})",
                    selected = compatFilter == CompatibilityFilter.ALL,
                    onClick = { viewModel.setCompatFilter(CompatibilityFilter.ALL) },
                    testTag = "filter_all"
                )
                FilterTabChip(
                    text = "🟢 75%-100% ($countGreat)",
                    selected = compatFilter == CompatibilityFilter.RUNS_GREAT,
                    onClick = { viewModel.setCompatFilter(CompatibilityFilter.RUNS_GREAT) },
                    testTag = "filter_great"
                )
                FilterTabChip(
                    text = "🟡 50%-74% ($countMedium)",
                    selected = compatFilter == CompatibilityFilter.RUNS_MEDIUM,
                    onClick = { viewModel.setCompatFilter(CompatibilityFilter.RUNS_MEDIUM) },
                    testTag = "filter_medium"
                )
                FilterTabChip(
                    text = "🔴 0%-49% ($countNo)",
                    selected = compatFilter == CompatibilityFilter.RUNS_NO,
                    onClick = { viewModel.setCompatFilter(CompatibilityFilter.RUNS_NO) },
                    testTag = "filter_no"
                )
            }
        }

        // Game Compatibility Cards
        items(filteredList, key = { it.first.id }) { (game, eval) ->
            GameCompatibilityCard(
                game = game,
                eval = eval,
                onClick = { onNavigateToGame(game.id, AppSection.GAME_DETAILS) },
                onCheckDuration = { onNavigateToGame(game.id, AppSection.HOW_LONG) }
            )
        }
    }
}

@Composable
fun StatusCounterItem(count: Int, label: String, sublabel: String, color: Color, icon: String) {
    val colors = AppTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$count",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
        Text(text = sublabel, fontSize = 10.sp, color = colors.textSecondary)
    }
}

@Composable
fun FilterTabChip(text: String, selected: Boolean, onClick: () -> Unit, testTag: String) {
    val colors = AppTheme.colors
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (selected) colors.primary else colors.surfaceVariant,
        border = BorderStroke(1.dp, if (selected) colors.primary else colors.surfaceBorder),
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Color.Black else colors.textPrimary,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun GameCompatibilityCard(
    game: Game,
    eval: com.example.data.model.CompatibilityEvaluation,
    onClick: () -> Unit,
    onCheckDuration: () -> Unit
) {
    val colors = AppTheme.colors
    val pctColor = when {
        eval.percentage >= 75 -> RunGreatGreen
        eval.percentage >= 50 -> RunMediumYellow
        else -> RunNoRed
    }

    val pctBg = when {
        eval.percentage >= 75 -> if (colors.isDark) Color(0xFF063528) else Color(0xFFDCFCE7)
        eval.percentage >= 50 -> if (colors.isDark) Color(0xFF3F2305) else Color(0xFFFEF9C3)
        else -> if (colors.isDark) Color(0xFF3F0B0B) else Color(0xFFFEE2E2)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .testTag("game_card_${game.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, pctColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(game.bannerColorHex).copy(alpha = 0.25f),
                        border = BorderStroke(1.dp, Color(game.bannerColorHex).copy(alpha = 0.6f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = game.coverEmoji, fontSize = 28.sp)
                        }
                    }

                    Column {
                        Text(
                            text = game.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        Text(
                            text = "${game.developer} • ${game.releaseYear}",
                            fontSize = 11.sp,
                            color = colors.textSecondary
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            game.genres.take(2).forEach { g ->
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = colors.surfaceVariant
                                ) {
                                    Text(
                                        text = g,
                                        fontSize = 10.sp,
                                        color = colors.textSecondary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Prominent Percentage Pill (75-100% Green, 50-74% Yellow, 0-49% Red)
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = pctBg,
                    border = BorderStroke(1.5.dp, pctColor)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${eval.percentage}%",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = pctColor
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = when {
                                eval.percentage >= 75 -> "Óptimo"
                                eval.percentage >= 50 -> "Ajustado"
                                else -> "Insuf."
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = pctColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Percentage progress bar
            LinearProgressIndicator(
                progress = { eval.percentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = pctColor,
                trackColor = colors.surfaceBorder
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Diagnostic indicators for 5 casillas
            Text(
                text = eval.status.description,
                fontSize = 12.sp,
                color = colors.textSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Component status chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ComponentCheckBadge("CPU", eval.cpuScorePct, Modifier.weight(1f))
                ComponentCheckBadge("GPU", eval.gpuScorePct, Modifier.weight(1f))
                ComponentCheckBadge("RAM", eval.ramScorePct, Modifier.weight(1f))
                ComponentCheckBadge("Disco", eval.storageScorePct, Modifier.weight(1f))
                ComponentCheckBadge("SO", if (eval.osPass) 100 else 0, Modifier.weight(1f))
            }

            if (eval.bottlenecks.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = RunNoRed.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, RunNoRed.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "⚠️ Limitaciones detectadas:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RunNoRed
                        )
                        eval.bottlenecks.forEach { b ->
                            Text(
                                text = "• $b",
                                fontSize = 11.sp,
                                color = if (colors.isDark) Color(0xFFFCA5A5) else RunNoRed
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Requisitos: ${game.storageRequiredGb}GB | RAM min: ${game.minRamGb}GB",
                    fontSize = 11.sp,
                    color = colors.textMuted
                )

                TextButton(
                    onClick = onClick,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Ver ficha completa →", fontSize = 12.sp, color = colors.primary)
                }
            }
        }
    }
}

@Composable
fun ComponentCheckBadge(name: String, pct: Int, modifier: Modifier = Modifier) {
    val color = when {
        pct >= 75 -> RunGreatGreen
        pct >= 50 -> RunMediumYellow
        else -> RunNoRed
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.45f)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
        ) {
            Text(text = name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AppTheme.colors.textPrimary)
            Text(text = "$pct%", fontSize = 10.sp, color = color, fontWeight = FontWeight.Black)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecDropdownSelector(
    label: String,
    selectedName: String,
    options: List<Pair<String, Int>>,
    onSelect: (String, Int) -> Unit,
    testTag: String
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimaryDark)
        Spacer(modifier = Modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedName,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag(testTag),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentTeal,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedContainerColor = DarkSurfaceVariant,
                    unfocusedContainerColor = DarkSurfaceVariant,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(DarkSurfaceVariant)
            ) {
                options.forEach { (name, score) ->
                    DropdownMenuItem(
                        text = { Text(name, color = Color.White, fontSize = 12.sp) },
                        onClick = {
                            onSelect(name, score)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecStringDropdownSelector(
    label: String,
    selectedValue: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    testTag: String
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimaryDark)
        Spacer(modifier = Modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag(testTag),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentTeal,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedContainerColor = DarkSurfaceVariant,
                    unfocusedContainerColor = DarkSurfaceVariant,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(DarkSurfaceVariant)
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item, color = Color.White, fontSize = 12.sp) },
                        onClick = {
                            onSelect(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
