package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.GameCatalog
import com.example.data.local.ReviewEntity
import com.example.data.model.CategoryRatings
import com.example.data.model.Game
import com.example.ui.theme.*
import com.example.viewmodel.AppSection
import com.example.viewmodel.GameCheckViewModel

@Composable
fun GameDetailScreen(
    viewModel: GameCheckViewModel,
    onNavigateToSection: (AppSection) -> Unit
) {
    val selectedGameId by viewModel.selectedGameId.collectAsState()
    val userReviews by viewModel.userReviews.collectAsState()

    val game = remember(selectedGameId) {
        GameCatalog.games.find { it.id == selectedGameId } ?: GameCatalog.games.first()
    }

    var showReviewDialog by remember { mutableStateOf(false) }

    // Aggregate community ratings including user reviews
    val combinedRatings = remember(game, userReviews) {
        if (userReviews.isEmpty()) {
            game.initialRatings
        } else {
            val totalH = game.initialRatings.historia * 5 + userReviews.sumOf { it.historiaRating.toDouble() }
            val totalJ = game.initialRatings.jugabilidad * 5 + userReviews.sumOf { it.jugabilidadRating.toDouble() }
            val totalG = game.initialRatings.graficos * 5 + userReviews.sumOf { it.graficosRating.toDouble() }
            val totalE = game.initialRatings.estrategia * 5 + userReviews.sumOf { it.estrategiaRating.toDouble() }
            val totalS = game.initialRatings.sonido * 5 + userReviews.sumOf { it.sonidoRating.toDouble() }
            val totalO = game.initialRatings.optimizacion * 5 + userReviews.sumOf { it.optimizacionRating.toDouble() }
            val count = (5 + userReviews.size).toFloat()
            CategoryRatings(
                historia = (totalH / count).toFloat(),
                jugabilidad = (totalJ / count).toFloat(),
                graficos = (totalG / count).toFloat(),
                estrategia = (totalE / count).toFloat(),
                sonido = (totalS / count).toFloat(),
                optimizacion = (totalO / count).toFloat()
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("game_detail_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Game Selector Horizontal Carousel
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Seleccionar juego a inspeccionar:",
                    fontSize = 12.sp,
                    color = TextSecondaryDark,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(GameCatalog.games, key = { it.id }) { g ->
                        val isSelected = g.id == selectedGameId
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) AccentTeal else DarkSurface,
                            border = BorderStroke(1.dp, if (isSelected) AccentTeal else DarkSurfaceBorder),
                            modifier = Modifier
                                .clickable { viewModel.selectGame(g.id) }
                                .testTag("select_game_pill_${g.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(text = g.coverEmoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = g.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFF090D16) else TextPrimaryDark
                                )
                            }
                        }
                    }
                }
            }
        }

        // Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("game_hero_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, Color(game.bannerColorHex).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(72.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(game.bannerColorHex).copy(alpha = 0.25f),
                            border = BorderStroke(1.dp, Color(game.bannerColorHex).copy(alpha = 0.6f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = game.coverEmoji, fontSize = 38.sp)
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = game.title,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = game.tagline,
                                fontSize = 12.sp,
                                color = AccentTeal,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${game.developer} • ${game.releaseYear} • ${game.publisher}",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = game.synopsis,
                        fontSize = 12.sp,
                        color = Color(0xFFD1D5DB),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Genres
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        game.genres.forEach { g ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = DarkSurfaceVariant
                            ) {
                                Text(
                                    text = g,
                                    fontSize = 10.sp,
                                    color = TextSecondaryDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AccentViolet.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, AccentViolet.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = game.contentWarnings.ageRating,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentViolet,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Jump to other 5 sections for this game
                    Text(
                        text = "Ver análisis detallado de este juego en:",
                        fontSize = 11.sp,
                        color = TextMutedDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SectionJumpButton("Me corre", "⚡", { onNavigateToSection(AppSection.CAN_I_RUN_IT) }, Modifier.weight(1f))
                        SectionJumpButton("Accesible", "♿", { onNavigateToSection(AppSection.ACCESSIBILITY) }, Modifier.weight(1f))
                        SectionJumpButton("Para mí?", "🛡️", { onNavigateToSection(AppSection.IS_IT_FOR_ME) }, Modifier.weight(1f))
                        SectionJumpButton("Duración", "⏱️", { onNavigateToSection(AppSection.HOW_LONG) }, Modifier.weight(1f))
                        SectionJumpButton("Precios", "💰", { onNavigateToSection(AppSection.BUDGET_PRICING) }, Modifier.weight(1f))
                    }
                }
            }
        }

        // Apartado Extra: Presupuesto y Ediciones del Juego
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("pricing_detail_card"),
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
                                text = "💰 Precios y Ediciones",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Juego Base vs. Juego Base + Extras",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AccentTeal.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, AccentTeal.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "$%.2f/hora".format(java.util.Locale.US, game.costPerHourBase),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentTeal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Base vs Deluxe columns
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Base Edition
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = DarkSurfaceVariant,
                            border = BorderStroke(1.dp, DarkSurfaceBorder)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "JUEGO BASE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AccentTeal
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$%.2f USD".format(java.util.Locale.US, game.pricing.basePriceUsd),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = game.pricing.baseEditionName,
                                    fontSize = 10.sp,
                                    color = TextSecondaryDark
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                game.pricing.baseIncludes.forEach { inc ->
                                    Text(
                                        text = "• $inc",
                                        fontSize = 9.sp,
                                        color = TextSecondaryDark,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        // Deluxe Edition
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = DarkSurfaceVariant,
                            border = BorderStroke(1.dp, AccentViolet.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "BASE + EXTRAS",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AccentViolet
                                    )
                                    Text(
                                        text = "+$%.2f".format(java.util.Locale.US, game.pricing.extrasDifferenceUsd),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentPurpleLight
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$%.2f USD".format(java.util.Locale.US, game.pricing.deluxePriceUsd),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = game.pricing.deluxeEditionName,
                                    fontSize = 10.sp,
                                    color = AccentPurpleLight
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                game.pricing.deluxeIncludes.take(3).forEach { inc ->
                                    Text(
                                        text = "★ $inc",
                                        fontSize = 9.sp,
                                        color = TextSecondaryDark,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    if (game.pricing.dlcPriceUsd > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF131A29),
                            border = BorderStroke(1.dp, DarkSurfaceBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📦 ${game.pricing.dlcName}",
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                                Text(
                                    text = "$%.2f USD".format(java.util.Locale.US, game.pricing.dlcPriceUsd),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentTeal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { onNavigateToSection(AppSection.BUDGET_PRICING) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, AccentTeal.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = AccentTeal.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = AccentTeal,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Calcular con mi Presupuesto en el Comparador",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentTeal
                        )
                    }
                }
            }
        }

        // Nueva Sección: ¿En qué plataforma corre mejor? (PC vs Play 4 vs Play 5 - Dónde comprarlo)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("platform_comparison_card"),
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
                                text = "🎮 ¿Dónde corre mejor?",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "PC vs Play 4 vs Play 5 • Recomendación de compra",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AccentTeal.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, AccentTeal.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "COMPARATIVA",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentTeal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dónde comprarlo Banner
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF13222E),
                        border = BorderStroke(1.5.dp, AccentTeal.copy(alpha = 0.7f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🏆", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "MEJOR PLATAFORMA PARA COMPRARLO:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AccentTeal
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = game.platformComparison.recommendedPlatform,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = game.platformComparison.buyingRecommendation,
                                fontSize = 12.sp,
                                color = Color(0xFFD1D5DB),
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Platform Details
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // PC
                        PlatformDetailCard(game.platformComparison.pcPerformance)

                        // PlayStation 5
                        PlatformDetailCard(game.platformComparison.ps5Performance)

                        // PlayStation 4
                        if (game.platformComparison.ps4Performance != null) {
                            PlatformDetailCard(game.platformComparison.ps4Performance)
                        } else {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = DarkSurfaceVariant.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, DarkSurfaceBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("ℹ️", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "PlayStation 4: No disponible (Exclusivo de nueva generación PS5 / PC).",
                                        fontSize = 11.sp,
                                        color = TextMutedDark
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Nueva Sección: Novedades de la Última Actualización
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("latest_update_card"),
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
                                text = "🔄 Última Actualización",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Novedades, mejoras y cambios añadidos",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AccentViolet.copy(alpha = 0.18f),
                            border = BorderStroke(1.dp, AccentViolet.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "${game.latestUpdate.version} • ${game.latestUpdate.releaseDate}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentPurpleLight,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = game.latestUpdate.headline,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentTeal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Performance impact banner
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF1E2838),
                        border = BorderStroke(1.dp, DarkSurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⚡", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = game.latestUpdate.performanceImpact,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF38BDF8)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Lo que se agregó en esta versión:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondaryDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    game.latestUpdate.changesAdded.forEach { change ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "✨",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = change,
                                fontSize = 12.sp,
                                color = Color(0xFFE5E7EB),
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botón para ir al Centro de Ayuda si tiene problemas
                    OutlinedButton(
                        onClick = { onNavigateToSection(AppSection.HELP_DIAGNOSTICS) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, AccentViolet.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = AccentViolet.copy(alpha = 0.1f))
                    ) {
                        Text("🛠️", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "¿Te va mal este juego? Abrir Centro de Ayuda",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentPurpleLight
                        )
                    }
                }
            }
        }

        // Section 5: Calificaciones por Categoría
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("ratings_breakdown_card"),
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
                                text = "⭐ Calificaciones por Categorías",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Basado en ${game.totalCommunityReviews + userReviews.size} reseñas comunitarias",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }

                        // Overall score badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF1E293B),
                            border = BorderStroke(1.dp, AccentTeal)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "%.1f".format(combinedRatings.overallAverage),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AccentTeal
                                )
                                Text(
                                    text = "sobre 5.0",
                                    fontSize = 9.sp,
                                    color = TextMutedDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 6 Categories required by user prompt
                    CategoryRatingRow(name = "Estrategia y Dificultad", rating = combinedRatings.estrategia, icon = "🧠")
                    CategoryRatingRow(name = "Historia y Narrativa", rating = combinedRatings.historia, icon = "📜")
                    CategoryRatingRow(name = "Jugabilidad y Mecánicas", rating = combinedRatings.jugabilidad, icon = "⚔️")
                    CategoryRatingRow(name = "Gráficos y Arte Visual", rating = combinedRatings.graficos, icon = "🎨")
                    CategoryRatingRow(name = "Sonido y Banda Sonora", rating = combinedRatings.sonido, icon = "🎵")
                    CategoryRatingRow(name = "Optimización y Rendimiento", rating = combinedRatings.optimizacion, icon = "⚙️")

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to rate the game
                    Button(
                        onClick = { showReviewDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("rate_game_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentTeal)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddComment,
                            contentDescription = null,
                            tint = Color(0xFF090D16),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Calificar este juego en las 6 categorías",
                            color = Color(0xFF090D16),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Community Reviews List Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reseñas de la Comunidad (${userReviews.size + 2}):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentTeal
                )
            }
        }

        // Submitted User Reviews
        items(userReviews, key = { it.id }) { rev ->
            UserReviewCard(review = rev)
        }

        // Static Initial Reviews
        item {
            SampleReviewCard(
                author = "Carlos_Gamer99",
                date = "Hace 3 días",
                comment = "Una experiencia monumental. La historia y la banda sonora están a otro nivel. Recomiendo prestar mucha atención a los ajustes gráficos.",
                avgScore = 4.8f,
                categories = "Historia: 5★ • Jugabilidad: 5★ • Gráficos: 5★ • Optimización: 4★"
            )
        }
        item {
            SampleReviewCard(
                author = "Elena_Pixel",
                date = "Hace 1 semana",
                comment = "Excelente en todos los aspectos, especialmente el combate y el apartado artístico. Las opciones de accesibilidad son muy completas.",
                avgScore = 4.6f,
                categories = "Estrategia: 4★ • Historia: 5★ • Jugabilidad: 5★ • Sonido: 5★"
            )
        }
    }

    if (showReviewDialog) {
        RateGameDialog(
            gameTitle = game.title,
            onDismiss = { showReviewDialog = false },
            onSubmit = { author, comment, h, j, g, e, s, o ->
                viewModel.addReview(game.id, author, comment, h, j, g, e, s, o)
                showReviewDialog = false
            }
        )
    }
}

@Composable
fun SectionJumpButton(title: String, icon: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DarkSurfaceVariant,
        border = BorderStroke(1.dp, DarkSurfaceBorder),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)
        ) {
            Text(text = icon, fontSize = 12.sp)
            Text(
                text = title,
                fontSize = 9.sp,
                color = TextSecondaryDark,
                maxLines = 1
            )
        }
    }
}

@Composable
fun CategoryRatingRow(name: String, rating: Float, icon: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1.2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = icon, fontSize = 13.sp)
            Text(text = name, fontSize = 12.sp, color = TextPrimaryDark)
        }

        Row(
            modifier = Modifier.weight(1.4f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LinearProgressIndicator(
                progress = { (rating / 5.0f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = AccentTeal,
                trackColor = Color(0xFF1E293B)
            )

            Text(
                text = "%.1f ★".format(rating),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun UserReviewCard(review: ReviewEntity) {
    val avg = (review.historiaRating + review.jugabilidadRating + review.graficosRating +
            review.estrategiaRating + review.sonidoRating + review.optimizacionRating) / 6f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, AccentTeal.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = AccentTeal.copy(alpha = 0.2f),
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "👤", fontSize = 12.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = review.author,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = AccentTeal.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "%.1f ★".format(avg),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentTeal,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (review.comment.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = review.comment,
                    fontSize = 12.sp,
                    color = TextPrimaryDark
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "H: %.0f★ • J: %.0f★ • G: %.0f★ • E: %.0f★ • S: %.0f★ • O: %.0f★".format(
                    review.historiaRating,
                    review.jugabilidadRating,
                    review.graficosRating,
                    review.estrategiaRating,
                    review.sonidoRating,
                    review.optimizacionRating
                ),
                fontSize = 10.sp,
                color = TextMutedDark
            )
        }
    }
}

@Composable
fun SampleReviewCard(author: String, date: String, comment: String, avgScore: Float, categories: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "🎮", fontSize = 12.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = author, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "• $date", fontSize = 10.sp, color = TextMutedDark)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = AccentTeal.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "%.1f ★".format(avgScore),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentTeal,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = comment, fontSize = 12.sp, color = Color(0xFFE5E7EB))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = categories, fontSize = 10.sp, color = TextMutedDark)
        }
    }
}

@Composable
fun RateGameDialog(
    gameTitle: String,
    onDismiss: () -> Unit,
    onSubmit: (String, String, Float, Float, Float, Float, Float, Float) -> Unit
) {
    var author by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    var ratingHistoria by remember { mutableStateOf(5f) }
    var ratingJugabilidad by remember { mutableStateOf(5f) }
    var ratingGraficos by remember { mutableStateOf(5f) }
    var ratingEstrategia by remember { mutableStateOf(5f) }
    var ratingSonido by remember { mutableStateOf(5f) }
    var ratingOptimizacion by remember { mutableStateOf(5f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Calificar $gameTitle",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Valora cada categoría de 1 a 5 estrellas",
                    fontSize = 11.sp,
                    color = TextSecondaryDark
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Tu nombre o apodo") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("review_author_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentTeal,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                item {
                    StarRatingPicker("Estrategia / Dificultad", ratingEstrategia) { ratingEstrategia = it }
                }
                item {
                    StarRatingPicker("Historia / Narrativa", ratingHistoria) { ratingHistoria = it }
                }
                item {
                    StarRatingPicker("Jugabilidad / Mecánicas", ratingJugabilidad) { ratingJugabilidad = it }
                }
                item {
                    StarRatingPicker("Gráficos y Arte", ratingGraficos) { ratingGraficos = it }
                }
                item {
                    StarRatingPicker("Sonido y Música", ratingSonido) { ratingSonido = it }
                }
                item {
                    StarRatingPicker("Optimización / Rendimiento", ratingOptimizacion) { ratingOptimizacion = it }
                }

                item {
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Escribe tu opinión / reseña") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("review_comment_input"),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentTeal,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(
                        author,
                        comment,
                        ratingHistoria,
                        ratingJugabilidad,
                        ratingGraficos,
                        ratingEstrategia,
                        ratingSonido,
                        ratingOptimizacion
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentTeal),
                modifier = Modifier.testTag("submit_review_button")
            ) {
                Text("Publicar Reseña", color = Color(0xFF090D16), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondaryDark)
            }
        },
        containerColor = DarkSurface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun StarRatingPicker(label: String, rating: Float, onRatingChange: (Float) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.sp, color = TextPrimaryDark, modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            (1..5).forEach { star ->
                val isFilled = rating >= star
                IconButton(
                    onClick = { onRatingChange(star.toFloat()) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "$star estrellas",
                        tint = if (isFilled) AccentTeal else DarkSurfaceBorder,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PlatformDetailCard(info: com.example.data.model.PlatformPerformance) {
    val isGood = info.score >= 8
    val scoreColor = when {
        info.score >= 9 -> Color(0xFF06D6A0)
        info.score >= 7 -> Color(0xFF38BDF8)
        info.score >= 5 -> Color(0xFFFF9F1C)
        else -> Color(0xFFEF476F)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceVariant,
        border = BorderStroke(1.dp, if (isGood) scoreColor.copy(alpha = 0.4f) else DarkSurfaceBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when (info.platformName) {
                            "PC" -> "💻"
                            "PlayStation 5" -> "🎮"
                            else -> "🕹️"
                        },
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = info.platformName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = scoreColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, scoreColor)
                ) {
                    Text(
                        text = "${info.stabilityBadge} • ${info.score}/10",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tech specs row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = DarkSurface,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        Text("FPS Esperados", fontSize = 9.sp, color = TextMutedDark)
                        Text(info.fpsTarget, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = DarkSurface,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        Text("Resolución", fontSize = 9.sp, color = TextMutedDark)
                        Text(info.resolution, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = DarkSurface,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        Text("Cargas", fontSize = 9.sp, color = TextMutedDark)
                        Text(info.loadingTime, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Pros
            info.pros.forEach { pro ->
                Text(
                    text = "✓ $pro",
                    fontSize = 11.sp,
                    color = Color(0xFF86EFAC),
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }

            // Cons
            info.cons.forEach { con ->
                Text(
                    text = "✗ $con",
                    fontSize = 11.sp,
                    color = Color(0xFFFCA5A5),
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }
        }
    }
}
