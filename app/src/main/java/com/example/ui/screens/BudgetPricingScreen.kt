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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.GameCatalog
import com.example.data.model.Game
import com.example.ui.theme.*
import com.example.viewmodel.AppSection
import com.example.viewmodel.BudgetFilter
import com.example.viewmodel.BudgetSort
import com.example.viewmodel.GameCheckViewModel
import java.util.Locale

@Composable
fun BudgetPricingScreen(
    viewModel: GameCheckViewModel,
    onNavigateToGame: (String, AppSection) -> Unit
) {
    val userBudget by viewModel.userBudget.collectAsState()
    val budgetFilter by viewModel.budgetFilter.collectAsState()
    val budgetSort by viewModel.budgetSort.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Filter and sort games
    val displayedGames = remember(searchQuery, budgetFilter, budgetSort, userBudget) {
        var list = GameCatalog.games.filter { game ->
            if (searchQuery.isNotBlank()) {
                game.title.contains(searchQuery, ignoreCase = true) ||
                game.developer.contains(searchQuery, ignoreCase = true) ||
                game.genres.any { it.contains(searchQuery, ignoreCase = true) }
            } else true
        }

        // Apply Budget Filter
        list = when (budgetFilter) {
            BudgetFilter.ALL -> list
            BudgetFilter.BASE_FITS -> list.filter { it.pricing.basePriceUsd <= userBudget }
            BudgetFilter.DELUXE_FITS -> list.filter { it.pricing.deluxePriceUsd <= userBudget }
            BudgetFilter.ON_SALE -> list.filter { it.pricing.salePriceUsd != null }
            BudgetFilter.BEST_VALUE -> list.filter { it.costPerHourBase < 2.0 }
        }

        // Apply Sort
        when (budgetSort) {
            BudgetSort.PRICE_ASC -> list.sortedBy { it.pricing.basePriceUsd }
            BudgetSort.PRICE_DESC -> list.sortedByDescending { it.pricing.basePriceUsd }
            BudgetSort.VALUE_BEST -> list.sortedBy { it.costPerHourBase }
            BudgetSort.DELUXE_PRICE_ASC -> list.sortedBy { it.pricing.deluxePriceUsd }
        }
    }

    // Stats calculations
    val baseFitsCount = remember(userBudget) {
        GameCatalog.games.count { it.pricing.basePriceUsd <= userBudget }
    }
    val deluxeFitsCount = remember(userBudget) {
        GameCatalog.games.count { it.pricing.deluxePriceUsd <= userBudget }
    }

    // Cart calculations
    val cartTotal = remember(cartItems) {
        cartItems.sumOf { (gameId, isDeluxe) ->
            val g = GameCatalog.games.find { it.id == gameId }
            if (g != null) {
                if (isDeluxe) g.pricing.deluxePriceUsd else g.pricing.basePriceUsd
            } else 0.0
        }
    }
    val budgetRemaining = userBudget - cartTotal

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("budget_pricing_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Presupuesto Control Panel
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("budget_control_panel_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, DarkSurfaceBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "💰 Tu Presupuesto Gamer",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Define cuánto quieres gastar en videojuegos",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }

                        // Glowing budget display badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF0F2338),
                            border = BorderStroke(1.dp, AccentTeal)
                        ) {
                            Text(
                                text = "$%.2f USD".format(Locale.US, userBudget),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = AccentTeal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Slider for budget
                    Text(
                        text = "Ajustar presupuesto:",
                        fontSize = 11.sp,
                        color = TextMutedDark
                    )
                    Slider(
                        value = userBudget.toFloat(),
                        onValueChange = { viewModel.setUserBudget(it.toDouble()) },
                        valueRange = 5f..250f,
                        steps = 48,
                        colors = SliderDefaults.colors(
                            thumbColor = AccentTeal,
                            activeTrackColor = AccentTeal,
                            inactiveTrackColor = Color(0xFF1E293B)
                        ),
                        modifier = Modifier.testTag("budget_slider")
                    )

                    // Quick Preset Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(15.0, 30.0, 50.0, 70.0, 100.0, 150.0).forEach { preset ->
                            val isSelected = Math.abs(userBudget - preset) < 1.0
                            OutlinedButton(
                                onClick = { viewModel.setUserBudget(preset) },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, if (isSelected) AccentTeal else DarkSurfaceBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) AccentTeal.copy(alpha = 0.15f) else Color.Transparent
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 36.dp)
                                    .testTag("budget_preset_${preset.toInt()}")
                            ) {
                                Text(
                                    text = "$${preset.toInt()}",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) AccentTeal else TextSecondaryDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = DarkSurfaceBorder, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Summary Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BudgetSummaryBadge(
                            label = "Juegos Base al alcance",
                            value = "$baseFitsCount / ${GameCatalog.games.size}",
                            color = RunGreatGreen
                        )
                        BudgetSummaryBadge(
                            label = "Deluxe / Extras al alcance",
                            value = "$deluxeFitsCount / ${GameCatalog.games.size}",
                            color = AccentViolet
                        )
                    }
                }
            }
        }

        // Cart / Purchase Wishlist Simulator
        item {
            AnimatedVisibility(visible = cartItems.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("cart_simulator_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    border = BorderStroke(1.dp, if (budgetRemaining >= 0) RunGreatGreen else RunNoRed)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = AccentTeal,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Simulador de Cesta (${cartItems.size} juegos)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            TextButton(
                                onClick = { viewModel.clearCart() },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Vaciar", fontSize = 11.sp, color = RunNoRed)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Selected games chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            cartItems.forEach { (gameId, isDeluxe) ->
                                val g = GameCatalog.games.find { it.id == gameId }
                                if (g != null) {
                                    val price = if (isDeluxe) g.pricing.deluxePriceUsd else g.pricing.basePriceUsd
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = DarkSurface,
                                        border = BorderStroke(1.dp, DarkSurfaceBorder)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(text = g.coverEmoji, fontSize = 12.sp)
                                            Text(
                                                text = "${g.title} (${if (isDeluxe) "Deluxe" else "Base"})",
                                                fontSize = 11.sp,
                                                color = Color.White
                                            )
                                            Text(
                                                text = "$%.2f".format(Locale.US, price),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AccentTeal
                                            )
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Quitar",
                                                tint = TextMutedDark,
                                                modifier = Modifier
                                                    .size(14.dp)
                                                    .clickable { viewModel.toggleCartItem(gameId, isDeluxe) }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = DarkSurfaceBorder, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Total and balance
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total en Cesta:",
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                                Text(
                                    text = "$%.2f USD".format(Locale.US, cartTotal),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (budgetRemaining >= 0) RunGreatGreenContainer else RunNoRedContainer
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (budgetRemaining >= 0) Icons.Default.CheckCircle else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (budgetRemaining >= 0) RunGreatGreen else RunNoRed,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = if (budgetRemaining >= 0) {
                                            "Te sobran: $%.2f".format(Locale.US, budgetRemaining)
                                        } else {
                                            "Faltan: $%.2f".format(Locale.US, -budgetRemaining)
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (budgetRemaining >= 0) RunGreatGreen else RunNoRed
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Filters and Sort Controls
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                // Filter chips
                Text(
                    text = "Filtrar por alcance:",
                    fontSize = 11.sp,
                    color = TextSecondaryDark,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    BudgetFilter.values().forEach { filter ->
                        val isSelected = budgetFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setBudgetFilter(filter) },
                            label = { Text(filter.label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentTeal,
                                selectedLabelColor = Color(0xFF090D16),
                                containerColor = DarkSurface,
                                labelColor = TextSecondaryDark
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = DarkSurfaceBorder,
                                selectedBorderColor = AccentTeal
                            ),
                            modifier = Modifier.testTag("budget_filter_${filter.name.lowercase()}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Sort chips
                Text(
                    text = "Ordenar por:",
                    fontSize = 11.sp,
                    color = TextSecondaryDark,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    BudgetSort.values().forEach { sort ->
                        val isSelected = budgetSort == sort
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setBudgetSort(sort) },
                            label = { Text(sort.label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentViolet,
                                selectedLabelColor = Color.White,
                                containerColor = DarkSurface,
                                labelColor = TextSecondaryDark
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = DarkSurfaceBorder,
                                selectedBorderColor = AccentViolet
                            ),
                            modifier = Modifier.testTag("budget_sort_${sort.name.lowercase()}")
                        )
                    }
                }
            }
        }

        // Section header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Desglose de Precios y Ediciones (${displayedGames.size}):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentTeal
                )
                Text(
                    text = "Base vs. Base + Extras",
                    fontSize = 11.sp,
                    color = TextMutedDark
                )
            }
        }

        // Empty state
        if (displayedGames.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, DarkSurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "💸", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No se encontraron juegos con este filtro",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Prueba aumentando tu presupuesto o seleccionando 'Todos los juegos'.",
                            fontSize = 12.sp,
                            color = TextSecondaryDark
                        )
                    }
                }
            }
        }

        // Games List with Detailed Pricing Breakdown
        items(displayedGames, key = { it.id }) { game ->
            GamePricingCard(
                game = game,
                userBudget = userBudget,
                isBaseInCart = viewModel.isItemInCart(game.id, false),
                isDeluxeInCart = viewModel.isItemInCart(game.id, true),
                onToggleCartBase = { viewModel.toggleCartItem(game.id, false) },
                onToggleCartDeluxe = { viewModel.toggleCartItem(game.id, true) },
                onNavigateToDetails = { onNavigateToGame(game.id, AppSection.GAME_DETAILS) },
                onNavigateToSpecs = { onNavigateToGame(game.id, AppSection.CAN_I_RUN_IT) },
                onNavigateToDuration = { onNavigateToGame(game.id, AppSection.HOW_LONG) }
            )
        }
    }
}

@Composable
fun BudgetSummaryBadge(label: String, value: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
            Text(text = label, fontSize = 10.sp, color = TextSecondaryDark)
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun GamePricingCard(
    game: Game,
    userBudget: Double,
    isBaseInCart: Boolean,
    isDeluxeInCart: Boolean,
    onToggleCartBase: () -> Unit,
    onToggleCartDeluxe: () -> Unit,
    onNavigateToDetails: () -> Unit,
    onNavigateToSpecs: () -> Unit,
    onNavigateToDuration: () -> Unit
) {
    val basePrice = game.pricing.basePriceUsd
    val deluxePrice = game.pricing.deluxePriceUsd
    val baseFits = basePrice <= userBudget
    val deluxeFits = deluxePrice <= userBudget
    val extrasDiff = game.pricing.extrasDifferenceUsd

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("pricing_card_${game.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Emoji + Title + Developer + Value Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(game.bannerColorHex).copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color(game.bannerColorHex).copy(alpha = 0.5f)),
                        modifier = Modifier.size(44.dp)
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
                            text = "${game.developer} • ${game.releaseYear}",
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )
                    }
                }

                // Cost per hour pill
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (game.costPerHourBase < 1.5) RunGreatGreenContainer else Color(0xFF1E293B),
                    border = BorderStroke(1.dp, if (game.costPerHourBase < 1.5) RunGreatGreen else DarkSurfaceBorder)
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$%.2f/h".format(Locale.US, game.costPerHourBase),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (game.costPerHourBase < 1.5) RunGreatGreen else AccentTeal
                        )
                        Text(
                            text = "valor/hora",
                            fontSize = 8.sp,
                            color = TextMutedDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pricing Comparison Row: Base vs. Base + Extras
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. JUEGO BASE CARD
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = DarkSurfaceVariant,
                    border = BorderStroke(1.dp, if (baseFits) RunGreatGreen.copy(alpha = 0.5f) else DarkSurfaceBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "JUEGO BASE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = AccentTeal
                            )
                            if (game.pricing.salePriceUsd != null) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = RunGreatGreenContainer
                                ) {
                                    Text(
                                        text = "OFERTA",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RunGreatGreen,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Price
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$%.2f".format(Locale.US, basePrice),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = " USD",
                                fontSize = 10.sp,
                                color = TextMutedDark,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }

                        Text(
                            text = game.pricing.baseEditionName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondaryDark,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // What's included
                        game.pricing.baseIncludes.take(2).forEach { inc ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(vertical = 1.dp)
                            ) {
                                Text("•", fontSize = 10.sp, color = AccentTeal)
                                Text(
                                    text = inc,
                                    fontSize = 9.sp,
                                    color = TextSecondaryDark,
                                    maxLines = 1
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Budget compatibility badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (baseFits) RunGreatGreenContainer else RunNoRedContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (baseFits) {
                                    "✅ Te sobran $%.2f".format(Locale.US, userBudget - basePrice)
                                } else {
                                    "❌ Faltan $%.2f".format(Locale.US, basePrice - userBudget)
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (baseFits) RunGreatGreen else RunNoRed,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Add to cart button
                        Button(
                            onClick = onToggleCartBase,
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isBaseInCart) AccentTeal else Color(0xFF25334D)
                            ),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .testTag("cart_btn_base_${game.id}")
                        ) {
                            Text(
                                text = if (isBaseInCart) "✓ En Carrito" else "+ Base",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isBaseInCart) Color(0xFF090D16) else Color.White
                            )
                        }
                    }
                }

                // 2. JUEGO BASE + EXTRAS CARD
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = DarkSurfaceVariant,
                    border = BorderStroke(1.dp, if (deluxeFits) AccentViolet.copy(alpha = 0.7f) else DarkSurfaceBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "BASE + EXTRAS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = AccentViolet
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = AccentViolet.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "+$%.2f".format(Locale.US, extrasDiff),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentPurpleLight,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Deluxe Price
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$%.2f".format(Locale.US, deluxePrice),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = " USD",
                                fontSize = 10.sp,
                                color = TextMutedDark,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }

                        Text(
                            text = game.pricing.deluxeEditionName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentPurpleLight,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Included extras
                        game.pricing.deluxeIncludes.take(2).forEach { inc ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(vertical = 1.dp)
                            ) {
                                Text("★", fontSize = 9.sp, color = AccentViolet)
                                Text(
                                    text = inc,
                                    fontSize = 9.sp,
                                    color = TextSecondaryDark,
                                    maxLines = 1
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Budget compatibility badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (deluxeFits) RunGreatGreenContainer else RunNoRedContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (deluxeFits) {
                                    "✅ Te sobran $%.2f".format(Locale.US, userBudget - deluxePrice)
                                } else {
                                    "❌ Faltan $%.2f".format(Locale.US, deluxePrice - userBudget)
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (deluxeFits) RunGreatGreen else RunNoRed,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Add to cart button
                        Button(
                            onClick = onToggleCartDeluxe,
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDeluxeInCart) AccentViolet else Color(0xFF332352)
                            ),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .testTag("cart_btn_deluxe_${game.id}")
                        ) {
                            Text(
                                text = if (isDeluxeInCart) "✓ En Carrito" else "+ Deluxe",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // DLC note if any
            if (game.pricing.dlcPriceUsd > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF131A29),
                    border = BorderStroke(1.dp, DarkSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "📦 ${game.pricing.dlcName}",
                            fontSize = 10.sp,
                            color = TextSecondaryDark,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "$%.2f USD".format(Locale.US, game.pricing.dlcPriceUsd),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentTeal
                        )
                    }
                }
            }

            if (game.pricing.dealsNote.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "💡 ${game.pricing.dealsNote}",
                    fontSize = 10.sp,
                    color = TextMutedDark
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = DarkSurfaceBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(6.dp))

            // Navigation Jump Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateToDetails,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, DarkSurfaceBorder),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                ) {
                    Text("🎮 Ficha", fontSize = 10.sp, color = TextPrimaryDark)
                }

                OutlinedButton(
                    onClick = onNavigateToSpecs,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, DarkSurfaceBorder),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                ) {
                    Text("⚡ ¿Me corre?", fontSize = 10.sp, color = AccentTeal)
                }

                OutlinedButton(
                    onClick = onNavigateToDuration,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, DarkSurfaceBorder),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                ) {
                    Text("⏱️ Duración", fontSize = 10.sp, color = AccentViolet)
                }
            }
        }
    }
}
