package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.data.local.PlayerProfileEntity
import com.example.ui.theme.*
import com.example.viewmodel.AppSection
import com.example.viewmodel.DurationGoal
import com.example.viewmodel.GameCheckViewModel

@Composable
fun PlayerProfileScreen(
    viewModel: GameCheckViewModel,
    onNavigateToSection: (AppSection) -> Unit
) {
    val profile by viewModel.playerProfile.collectAsState()
    val activeThemeMode by viewModel.themeMode.collectAsState()
    val colors = AppTheme.colors

    var gamerTagInput by remember(profile.gamerTag) { mutableStateOf(profile.gamerTag) }
    var selectedAvatar by remember(profile.avatarEmoji) { mutableStateOf(profile.avatarEmoji) }

    val avatarOptions = listOf("🎮", "👾", "🦊", "⚡", "🐉", "🕹️", "👑", "🎯", "🚀", "🤖", "🛡️", "🔥")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .testTag("player_profile_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_hero_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(68.dp),
                            shape = CircleShape,
                            color = colors.primary.copy(alpha = 0.2f),
                            border = BorderStroke(2.dp, colors.primary)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = selectedAvatar, fontSize = 36.sp)
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Perfil de Jugador",
                                fontSize = 12.sp,
                                color = colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                            OutlinedTextField(
                                value = gamerTagInput,
                                onValueChange = {
                                    gamerTagInput = it
                                    viewModel.saveFullProfile(profile.copy(gamerTag = it, avatarEmoji = selectedAvatar))
                                },
                                singleLine = true,
                                label = { Text("Tu Gamertag / Apodo") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("gamertag_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = colors.primary,
                                    unfocusedBorderColor = colors.surfaceBorder,
                                    focusedContainerColor = colors.surfaceVariant,
                                    unfocusedContainerColor = colors.surfaceVariant,
                                    focusedTextColor = colors.textPrimary,
                                    unfocusedTextColor = colors.textPrimary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Elige tu avatar:",
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        avatarOptions.forEach { emoji ->
                            val isSelected = selectedAvatar == emoji
                            Surface(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clickable {
                                        selectedAvatar = emoji
                                        viewModel.saveFullProfile(profile.copy(avatarEmoji = emoji, gamerTag = gamerTagInput))
                                    }
                                    .testTag("avatar_option_$emoji"),
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) colors.primary.copy(alpha = 0.25f) else colors.surfaceVariant,
                                border = BorderStroke(1.dp, if (isSelected) colors.primary else colors.surfaceBorder)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = emoji, fontSize = 22.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = RunGreatGreen.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, RunGreatGreen.copy(alpha = 0.4f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = RunGreatGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Preferencias guardadas automáticamente. No tienes que volver a rellenarlas.",
                                fontSize = 11.sp,
                                color = RunGreatGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Section: Personalización de Tema
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("theme_selector_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎨", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Personalización de Tema Visual",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }
                    Text(
                        text = "Elige la apariencia que mejor se adapte a tu entorno y accesibilidad:",
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    ThemeMode.values().forEach { mode ->
                        val isSelected = activeThemeMode == mode
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.setThemeMode(mode)
                                }
                                .testTag("theme_option_${mode.name.lowercase()}"),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) colors.primary.copy(alpha = 0.15f) else colors.surfaceVariant,
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) colors.primary else colors.surfaceBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = mode.icon, fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = mode.displayName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) colors.primary else colors.textPrimary
                                        )
                                        Text(
                                            text = mode.description,
                                            fontSize = 11.sp,
                                            color = colors.textSecondary
                                        )
                                    }
                                }
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { viewModel.setThemeMode(mode) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = colors.primary,
                                        unselectedColor = colors.textMuted
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Mi Hardware de PC
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_hardware_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⚡", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mi Hardware de PC",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }
                        TextButton(onClick = { onNavigateToSection(AppSection.CAN_I_RUN_IT) }) {
                            Text("Ver en ¿Me corre?", fontSize = 12.sp, color = colors.primary)
                        }
                    }
                    Text(
                        text = "Configura tu equipo una vez y se aplicará a todas las evaluaciones:",
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Hardware Presets
                    Text(
                        text = "Presets rápidos:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PresetChip(
                            title = "🥉 Entrada",
                            specs = "i5 / 1050Ti / 8GB",
                            selected = profile.cpuScore == 2 && profile.gpuScore == 2,
                            onClick = {
                                viewModel.saveFullProfile(
                                    profile.copy(
                                        cpuName = GameCatalog.cpuOptions[1].name,
                                        cpuScore = 2,
                                        gpuName = GameCatalog.gpuOptions[1].name,
                                        gpuScore = 2,
                                        ramGb = 8,
                                        storageGb = 250
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        PresetChip(
                            title = "🥈 Media",
                            specs = "Ryzen 5 / 1660S / 16GB",
                            selected = profile.cpuScore == 3 && profile.gpuScore == 3,
                            onClick = {
                                viewModel.saveFullProfile(
                                    profile.copy(
                                        cpuName = GameCatalog.cpuOptions[2].name,
                                        cpuScore = 3,
                                        gpuName = GameCatalog.gpuOptions[2].name,
                                        gpuScore = 3,
                                        ramGb = 16,
                                        storageGb = 500
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        PresetChip(
                            title = "🥇 Alta",
                            specs = "Ryzen 7 / 3070 / 32GB",
                            selected = profile.cpuScore == 4 && profile.gpuScore == 4,
                            onClick = {
                                viewModel.saveFullProfile(
                                    profile.copy(
                                        cpuName = GameCatalog.cpuOptions[3].name,
                                        cpuScore = 4,
                                        gpuName = GameCatalog.gpuOptions[3].name,
                                        gpuScore = 4,
                                        ramGb = 32,
                                        storageGb = 1000
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    ProfileDropdownSelector(
                        label = "Procesador (CPU)",
                        currentValue = profile.cpuName,
                        options = GameCatalog.cpuOptions.map { it.name },
                        onSelect = { selectedName ->
                            val score = GameCatalog.cpuOptions.find { it.name == selectedName }?.score ?: 3
                            viewModel.saveFullProfile(profile.copy(cpuName = selectedName, cpuScore = score))
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileDropdownSelector(
                        label = "Tarjeta Gráfica (GPU)",
                        currentValue = profile.gpuName,
                        options = GameCatalog.gpuOptions.map { it.name },
                        onSelect = { selectedName ->
                            val score = GameCatalog.gpuOptions.find { it.name == selectedName }?.score ?: 3
                            viewModel.saveFullProfile(profile.copy(gpuName = selectedName, gpuScore = score))
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            ProfileDropdownSelector(
                                label = "Memoria RAM",
                                currentValue = "${profile.ramGb} GB",
                                options = GameCatalog.ramOptions.map { "$it GB" },
                                onSelect = { sel ->
                                    val gb = sel.replace(" GB", "").toIntOrNull() ?: 16
                                    viewModel.saveFullProfile(profile.copy(ramGb = gb))
                                }
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            ProfileDropdownSelector(
                                label = "Almacenamiento Libre",
                                currentValue = "${profile.storageGb} GB",
                                options = GameCatalog.storageOptions.map { "$it GB" },
                                onSelect = { sel ->
                                    val gb = sel.replace(" GB", "").toIntOrNull() ?: 500
                                    viewModel.saveFullProfile(profile.copy(storageGb = gb))
                                }
                            )
                        }
                    }
                }
            }
        }

        // Section: Preferencias de Juego & Duración
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_duration_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⏱️", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mi Ritmo de Juego & Duración",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }
                        TextButton(onClick = { onNavigateToSection(AppSection.HOW_LONG) }) {
                            Text("Calculadora", fontSize = 12.sp, color = colors.primary)
                        }
                    }
                    Text(
                        text = "Tiempo promedio que le dedicas a jugar por día:",
                        fontSize = 12.sp,
                        color = colors.textSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${profile.dailyHoursAvailable} horas al día",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = colors.primary
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf(1.0f, 2.0f, 3.0f, 4.0f).forEach { h ->
                                val isSelected = profile.dailyHoursAvailable == h
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) colors.primary else colors.surfaceVariant,
                                    modifier = Modifier.clickable {
                                        viewModel.saveFullProfile(profile.copy(dailyHoursAvailable = h))
                                    }
                                ) {
                                    Text(
                                        text = "${h.toInt()}h",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else colors.textPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Slider(
                        value = profile.dailyHoursAvailable,
                        onValueChange = {
                            val rounded = (Math.round(it * 2) / 2.0f)
                            viewModel.saveFullProfile(profile.copy(dailyHoursAvailable = rounded))
                        },
                        valueRange = 0.5f..8.0f,
                        steps = 14,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = colors.primary,
                            activeTrackColor = colors.primary,
                            inactiveTrackColor = colors.surfaceBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Estilo de juego habitual:",
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DurationGoal.values().forEach { goal ->
                            val isSel = profile.preferredPlaystyle == goal.name
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) colors.primary.copy(alpha = 0.2f) else colors.surfaceVariant,
                                border = BorderStroke(1.dp, if (isSel) colors.primary else colors.surfaceBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        viewModel.saveFullProfile(profile.copy(preferredPlaystyle = goal.name))
                                    }
                            ) {
                                Text(
                                    text = goal.label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) colors.primary else colors.textPrimary,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Presupuesto Habitual
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_budget_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "💰", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mi Presupuesto Habitual",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }
                        TextButton(onClick = { onNavigateToSection(AppSection.BUDGET_PRICING) }) {
                            Text("Ver Precios", fontSize = 12.sp, color = colors.primary)
                        }
                    }
                    Text(
                        text = "Presupuesto de referencia para comprar videojuegos:",
                        fontSize = 12.sp,
                        color = colors.textSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$${String.format("%.2f", profile.defaultBudgetUsd)} USD",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = AccentTeal
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(30.0, 60.0, 70.0, 100.0).forEach { b ->
                                val isSelected = Math.abs(profile.defaultBudgetUsd - b) < 0.1
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) AccentTeal else colors.surfaceVariant,
                                    modifier = Modifier.clickable {
                                        viewModel.saveFullProfile(profile.copy(defaultBudgetUsd = b))
                                    }
                                ) {
                                    Text(
                                        text = "$${b.toInt()}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else colors.textPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Slider(
                        value = profile.defaultBudgetUsd.toFloat(),
                        onValueChange = {
                            val rounded = (Math.round(it)).toDouble()
                            viewModel.saveFullProfile(profile.copy(defaultBudgetUsd = rounded))
                        },
                        valueRange = 10f..200f,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = AccentTeal,
                            activeTrackColor = AccentTeal,
                            inactiveTrackColor = colors.surfaceBorder
                        )
                    )
                }
            }
        }

        // Section: Preferencias de Accesibilidad
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_accessibility_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "♿", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mis Requisitos de Accesibilidad",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }
                        TextButton(onClick = { onNavigateToSection(AppSection.ACCESSIBILITY) }) {
                            Text("Ver Catálogo", fontSize = 12.sp, color = colors.primary)
                        }
                    }
                    Text(
                        text = "Filtra automáticamente juegos según tus necesidades físicas o sensoriales:",
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ProfileSwitchRow(
                        title = "Subtítulos ajustables obligatorios",
                        subtitle = "Requiere fuentes grandes y contraste de fondo",
                        checked = profile.requireSubtitles,
                        onCheckedChange = { viewModel.saveFullProfile(profile.copy(requireSubtitles = it)) }
                    )
                    ProfileSwitchRow(
                        title = "Filtros para daltonismo",
                        subtitle = "Protanopia, Deuteranopia o Tritanopia",
                        checked = profile.requireColorblindFilter,
                        onCheckedChange = { viewModel.saveFullProfile(profile.copy(requireColorblindFilter = it)) }
                    )
                    ProfileSwitchRow(
                        title = "Remapeo completo de botones",
                        subtitle = "Poder asignar libremente botones de control o teclado",
                        checked = profile.requireRemapping,
                        onCheckedChange = { viewModel.saveFullProfile(profile.copy(requireRemapping = it)) }
                    )
                    ProfileSwitchRow(
                        title = "Asistencia de puntería / Dificultad accesible",
                        subtitle = "Para jugar con mayor comodidad o movilidad reducida",
                        checked = profile.requireAimAssist,
                        onCheckedChange = { viewModel.saveFullProfile(profile.copy(requireAimAssist = it)) }
                    )
                    ProfileSwitchRow(
                        title = "Audio y voces en Español",
                        subtitle = "Priorizar títulos con doblaje completo en castellano",
                        checked = profile.requireSpanishAudio,
                        onCheckedChange = { viewModel.saveFullProfile(profile.copy(requireSpanishAudio = it)) }
                    )
                }
            }
        }

        // Section: Filtros de Contenido Sensible
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_content_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🛡️", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mis Filtros de Contenido",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }
                        TextButton(onClick = { onNavigateToSection(AppSection.IS_IT_FOR_ME) }) {
                            Text("Ver Alertas", fontSize = 12.sp, color = colors.primary)
                        }
                    }
                    Text(
                        text = "Configura advertencias de seguridad para saber si un juego es para ti:",
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ProfileSwitchRow(
                        title = "Evitar violencia brutal / mutilaciones",
                        subtitle = "Alertar si el juego incluye gore excesivo",
                        checked = profile.avoidBrutalViolence,
                        onCheckedChange = { viewModel.saveFullProfile(profile.copy(avoidBrutalViolence = it)) }
                    )
                    ProfileSwitchRow(
                        title = "Evitar terror psicológico y jumpscares",
                        subtitle = "Para jugar con calma sin sobresaltos ni horror",
                        checked = profile.avoidPsychologicalHorror,
                        onCheckedChange = { viewModel.saveFullProfile(profile.copy(avoidPsychologicalHorror = it)) }
                    )
                    ProfileSwitchRow(
                        title = "Evitar lenguaje soez constante",
                        subtitle = "Alertar si contiene groserías recurrentes",
                        checked = profile.avoidStrongLanguage,
                        onCheckedChange = { viewModel.saveFullProfile(profile.copy(avoidStrongLanguage = it)) }
                    )
                    ProfileSwitchRow(
                        title = "Modo anti-aracnofobia",
                        subtitle = "Alertar si aparecen arañas o criaturas arácnidas",
                        checked = profile.avoidSpiders,
                        onCheckedChange = { viewModel.saveFullProfile(profile.copy(avoidSpiders = it)) }
                    )
                }
            }
        }
    }
}

@Composable
fun PresetChip(
    title: String,
    specs: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (selected) colors.primary.copy(alpha = 0.2f) else colors.surfaceVariant,
        border = BorderStroke(1.dp, if (selected) colors.primary else colors.surfaceBorder),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) colors.primary else colors.textPrimary
            )
            Text(
                text = specs,
                fontSize = 9.sp,
                color = colors.textSecondary,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDropdownSelector(
    label: String,
    currentValue: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val colors = AppTheme.colors

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = currentValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.surfaceBorder,
                focusedContainerColor = colors.surfaceVariant,
                unfocusedContainerColor = colors.surfaceVariant,
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textPrimary
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(colors.surface)
        ) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt, color = colors.textPrimary, fontSize = 13.sp) },
                    onClick = {
                        onSelect(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colors = AppTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = colors.textSecondary
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colors.primary,
                uncheckedThumbColor = colors.textSecondary,
                uncheckedTrackColor = colors.surfaceVariant
            )
        )
    }
}
