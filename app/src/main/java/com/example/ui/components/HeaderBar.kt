package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppTheme
import com.example.viewmodel.AppSection

@Composable
fun HeaderBar(
    currentTitle: String,
    subtitle: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    avatarEmoji: String = "🎮",
    currentSection: AppSection = AppSection.CAN_I_RUN_IT,
    onNavigateToSection: (AppSection) -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    var searchExpanded by remember { mutableStateOf(false) }
    var cornerMenuExpanded by remember { mutableStateOf(false) }
    val colors = AppTheme.colors

    val isCornerActive = currentSection == AppSection.PLAYER_PROFILE || 
                         currentSection == AppSection.COMPARE_GAMES ||
                         currentSection == AppSection.HELP_DIAGNOSTICS ||
                         currentSection == AppSection.HELP_US

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colors.primary.copy(alpha = 0.15f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🎮", fontSize = 22.sp)
                    }
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Game",
                            color = colors.textPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Check",
                            color = colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text(
                        text = currentTitle,
                        color = colors.secondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(
                    onClick = { searchExpanded = !searchExpanded },
                    modifier = Modifier.testTag("toggle_search_button")
                ) {
                    Icon(
                        imageVector = if (searchExpanded) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = "Buscar juegos",
                        tint = if (searchExpanded) colors.primary else colors.textSecondary
                    )
                }

                // Corner deployable button (despliega Perfil y Comparador)
                Box {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isCornerActive) colors.primary.copy(alpha = 0.18f) else colors.surfaceVariant,
                        border = BorderStroke(
                            1.dp,
                            if (isCornerActive) colors.primary else colors.surfaceBorder
                        ),
                        modifier = Modifier
                            .clickable { cornerMenuExpanded = true }
                            .testTag("corner_menu_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = avatarEmoji, fontSize = 18.sp)
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Desplegar menú de perfil y comparador",
                                tint = if (isCornerActive) colors.primary else colors.textSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = cornerMenuExpanded,
                        onDismissRequest = { cornerMenuExpanded = false },
                        modifier = Modifier
                            .background(colors.surface)
                            .widthIn(min = 240.dp)
                            .testTag("corner_dropdown_menu")
                    ) {
                        Text(
                            text = "HERRAMIENTAS Y PERFIL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textMuted,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )

                        HorizontalDivider(color = colors.surfaceBorder.copy(alpha = 0.6f))

                        // Option 1: Mi Perfil
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Mi Perfil",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = if (currentSection == AppSection.PLAYER_PROFILE) colors.primary else colors.textPrimary
                                        )
                                        if (currentSection == AppSection.PLAYER_PROFILE) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "• Activo",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = colors.primary
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Hardware, especificaciones y tema",
                                        fontSize = 11.sp,
                                        color = colors.textSecondary
                                    )
                                }
                            },
                            leadingIcon = {
                                Text(text = avatarEmoji, fontSize = 20.sp)
                            },
                            onClick = {
                                cornerMenuExpanded = false
                                onNavigateToSection(AppSection.PLAYER_PROFILE)
                                onProfileClick()
                            },
                            modifier = Modifier.testTag("menu_item_profile")
                        )

                        HorizontalDivider(color = colors.surfaceBorder.copy(alpha = 0.6f))

                        // Option 2: Comparador de Juegos
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Comparador de Juegos",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = if (currentSection == AppSection.COMPARE_GAMES) colors.primary else colors.textPrimary
                                        )
                                        if (currentSection == AppSection.COMPARE_GAMES) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "• Activo",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = colors.primary
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Enfrenta 2 títulos cara a cara",
                                        fontSize = 11.sp,
                                        color = colors.textSecondary
                                    )
                                }
                            },
                            leadingIcon = {
                                Text(text = "⚖️", fontSize = 20.sp)
                            },
                            onClick = {
                                cornerMenuExpanded = false
                                onNavigateToSection(AppSection.COMPARE_GAMES)
                            },
                            modifier = Modifier.testTag("menu_item_compare")
                        )

                        HorizontalDivider(color = colors.surfaceBorder.copy(alpha = 0.6f))

                        // Option 3: Centro de Ayuda (Diagnóstico de problemas)
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Centro de Ayuda",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = if (currentSection == AppSection.HELP_DIAGNOSTICS) colors.primary else colors.textPrimary
                                        )
                                        if (currentSection == AppSection.HELP_DIAGNOSTICS) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "• Activo",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = colors.primary
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Diagnóstico: FPS, lentitud, textos y controles",
                                        fontSize = 11.sp,
                                        color = colors.textSecondary
                                    )
                                }
                            },
                            leadingIcon = {
                                Text(text = "🛠️", fontSize = 20.sp)
                            },
                            onClick = {
                                cornerMenuExpanded = false
                                onNavigateToSection(AppSection.HELP_DIAGNOSTICS)
                            },
                            modifier = Modifier.testTag("menu_item_help")
                        )

                        HorizontalDivider(color = colors.surfaceBorder.copy(alpha = 0.6f))

                        // Option 4: Ayúdanos (Enviar juego para revisión y añadir a la BD)
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Ayúdanos",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = if (currentSection == AppSection.HELP_US) colors.primary else colors.textPrimary
                                        )
                                        if (currentSection == AppSection.HELP_US) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "• Activo",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = colors.primary
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Propón un juego (requisitos, precios y categorías)",
                                        fontSize = 11.sp,
                                        color = colors.textSecondary
                                    )
                                }
                            },
                            leadingIcon = {
                                Text(text = "🤝", fontSize = 20.sp)
                            },
                            onClick = {
                                cornerMenuExpanded = false
                                onNavigateToSection(AppSection.HELP_US)
                            },
                            modifier = Modifier.testTag("menu_item_help_us")
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = searchExpanded) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Buscar juego por título o género...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .testTag("search_games_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.surfaceBorder,
                    focusedContainerColor = colors.surfaceVariant,
                    unfocusedContainerColor = colors.surfaceVariant,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary
                ),
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda",
                                tint = colors.textSecondary
                            )
                        }
                    }
                }
            )
        }

        if (subtitle.isNotEmpty() && !searchExpanded) {
            Text(
                text = subtitle,
                color = colors.textSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
