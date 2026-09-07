package com.example.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HeaderBar
import com.example.ui.theme.AppTheme
import com.example.viewmodel.AppSection
import com.example.viewmodel.GameCheckViewModel

@Composable
fun MainScreen(viewModel: GameCheckViewModel) {
    val currentSection by viewModel.currentSection.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val profile by viewModel.playerProfile.collectAsState()
    val colors = AppTheme.colors

    val subtitle = when (currentSection) {
        AppSection.CAN_I_RUN_IT -> "1. ¿Me corre? — Chequeo de 5 Casillas con % de Compatibilidad"
        AppSection.COMPARE_GAMES -> "2. Comparador — Enfrenta dos juegos para decidir tu compra"
        AppSection.BUDGET_PRICING -> "3. Presupuesto — Juego Base, Ediciones y Calculadora"
        AppSection.HOW_LONG -> "4. ¿Cuánto dura? — Tiempos de Juego y Calculadora Diaria"
        AppSection.ACCESSIBILITY -> "5. Accesibilidad — Subtítulos, Controles y Asistencia Visual"
        AppSection.IS_IT_FOR_ME -> "6. ¿Es para mí? — Violencia, Sangre, Terror y Temas Sensibles"
        AppSection.PLAYER_PROFILE -> "7. Mi Perfil — Preferencias, Especificaciones y Tema"
        AppSection.GAME_DETAILS -> "8. Ficha del Juego — Calificaciones por Categorías"
        AppSection.HELP_DIAGNOSTICS -> "9. Centro de Ayuda — Diagnóstico de FPS, Lentitud, Controles y Textos"
        AppSection.HELP_US -> "10. Ayúdanos — Propón un juego para sumar a la base de datos"
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("game_check_main_scaffold"),
        containerColor = colors.background,
        topBar = {
            HeaderBar(
                currentTitle = currentSection.title,
                subtitle = subtitle,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                avatarEmoji = profile.avatarEmoji,
                currentSection = currentSection,
                onNavigateToSection = { viewModel.setSection(it) },
                onProfileClick = { viewModel.setSection(AppSection.PLAYER_PROFILE) }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = colors.surface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_navigation_bar")
            ) {
                val bottomBarSections = listOf(
                    AppSection.CAN_I_RUN_IT,
                    AppSection.BUDGET_PRICING,
                    AppSection.HOW_LONG,
                    AppSection.ACCESSIBILITY,
                    AppSection.IS_IT_FOR_ME
                )
                bottomBarSections.forEach { section ->
                    val isSelected = currentSection == section
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setSection(section) },
                        icon = {
                            Text(
                                text = section.iconLabel,
                                fontSize = if (isSelected) 20.sp else 17.sp
                            )
                        },
                        label = {
                            Text(
                                text = section.shortTitle,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colors.primary,
                            selectedTextColor = colors.primary,
                            unselectedIconColor = colors.textMuted,
                            unselectedTextColor = colors.textMuted,
                            indicatorColor = colors.primary.copy(alpha = 0.18f)
                        ),
                        modifier = Modifier.testTag("nav_item_${section.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentSection, label = "section_crossfade") { section ->
                when (section) {
                    AppSection.CAN_I_RUN_IT -> CanIRunItScreen(
                        viewModel = viewModel,
                        onNavigateToGame = { gameId, targetSection ->
                            viewModel.selectGame(gameId, targetSection)
                        }
                    )
                    AppSection.COMPARE_GAMES -> GameComparisonScreen(
                        viewModel = viewModel,
                        onNavigateToGame = { gameId, targetSection ->
                            viewModel.selectGame(gameId, targetSection)
                        }
                    )
                    AppSection.BUDGET_PRICING -> BudgetPricingScreen(
                        viewModel = viewModel,
                        onNavigateToGame = { gameId, targetSection ->
                            viewModel.selectGame(gameId, targetSection)
                        }
                    )
                    AppSection.HOW_LONG -> HowLongScreen(
                        viewModel = viewModel,
                        onNavigateToGame = { gameId, targetSection ->
                            viewModel.selectGame(gameId, targetSection)
                        }
                    )
                    AppSection.ACCESSIBILITY -> AccessibilityScreen(
                        viewModel = viewModel,
                        onNavigateToGame = { gameId, targetSection ->
                            viewModel.selectGame(gameId, targetSection)
                        }
                    )
                    AppSection.IS_IT_FOR_ME -> IsItForMeScreen(
                        viewModel = viewModel,
                        onNavigateToGame = { gameId, targetSection ->
                            viewModel.selectGame(gameId, targetSection)
                        }
                    )
                    AppSection.PLAYER_PROFILE -> PlayerProfileScreen(
                        viewModel = viewModel,
                        onNavigateToSection = { targetSection ->
                            viewModel.setSection(targetSection)
                        }
                    )
                    AppSection.GAME_DETAILS -> GameDetailScreen(
                        viewModel = viewModel,
                        onNavigateToSection = { targetSection ->
                            viewModel.setSection(targetSection)
                        }
                    )
                    AppSection.HELP_DIAGNOSTICS -> TroubleshootingScreen(
                        viewModel = viewModel,
                        onNavigateToSection = { targetSection ->
                            viewModel.setSection(targetSection)
                        }
                    )
                    AppSection.HELP_US -> HelpUsScreen(viewModel = viewModel)
                }
            }
        }
    }
}
