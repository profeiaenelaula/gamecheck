package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.GameCatalog
import com.example.data.model.Game
import com.example.data.model.HardwareSpecs
import com.example.ui.theme.AppTheme
import com.example.viewmodel.AppSection
import com.example.viewmodel.GameCheckViewModel

enum class TroubleshootingSymptom(
    val title: String,
    val shortLabel: String,
    val emoji: String,
    val category: String
) {
    GAME_SLOW(
        title = "El juego me va lento (Bajos FPS en general)",
        shortLabel = "Va lento",
        emoji = "🐌",
        category = "Rendimiento"
    ),
    FPS_STUTTER(
        title = "Me dan tirones de FPS / micro-congelamientos bruscos",
        shortLabel = "Tirones de FPS",
        emoji = "⚡",
        category = "Rendimiento"
    ),
    TEXTS_UNREADABLE(
        title = "No puedo leer bien los textos o subtítulos",
        shortLabel = "Textos difíciles",
        emoji = "🔍",
        category = "Visual y Accesibilidad"
    ),
    CONTROLS_DIFFICULT(
        title = "Los controles son difíciles o poco cómodos",
        shortLabel = "Controles difíciles",
        emoji = "🎮",
        category = "Jugabilidad"
    ),
    OVERHEATING_NOISE(
        title = "La máquina se calienta mucho o el ventilador suena al máximo",
        shortLabel = "Sobrecalentamiento",
        emoji = "🔥",
        category = "Hardware"
    ),
    LONG_LOADINGS(
        title = "Los tiempos de carga tardan demasiado en arrancar",
        shortLabel = "Cargas lentas",
        emoji = "⏳",
        category = "Almacenamiento"
    )
}

data class DiagnosticReport(
    val rootCauseTitle: String,
    val rootCauseSeverity: String, // "Crítica", "Moderada", "Configuración", "Informativa"
    val severityColor: Color,
    val detailedExplanation: String,
    val hardwareComparisonNote: String,
    val stepByStepSolutions: List<TroubleshootingStep>
)

data class TroubleshootingStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val tag: String
)

@Composable
fun TroubleshootingScreen(
    viewModel: GameCheckViewModel,
    onNavigateToSection: (AppSection) -> Unit
) {
    val colors = AppTheme.colors
    val selectedGameId by viewModel.selectedGameId.collectAsState()
    val userSpecs by viewModel.userSpecs.collectAsState()

    var activeSymptom by remember { mutableStateOf(TroubleshootingSymptom.GAME_SLOW) }

    val game = remember(selectedGameId) {
        GameCatalog.games.find { it.id == selectedGameId } ?: GameCatalog.games.first()
    }

    val report = remember(activeSymptom, game, userSpecs) {
        generateDiagnosticReport(activeSymptom, game, userSpecs)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("troubleshooting_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Header Info Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = colors.primary.copy(alpha = 0.15f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🛠️", fontSize = 20.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Centro de Ayuda y Diagnóstico",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Text(
                                text = "¿Problemas jugando? Te explicamos el porqué y cómo solucionarlo",
                                fontSize = 12.sp,
                                color = colors.textSecondary
                            )
                        }
                    }
                }
            }
        }

        // Game selector carousel
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "Selecciona el juego con el que tienes problemas:",
                    fontSize = 12.sp,
                    color = colors.textSecondary,
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
                            color = if (isSelected) colors.primary else colors.surface,
                            border = BorderStroke(1.dp, if (isSelected) colors.primary else colors.surfaceBorder),
                            modifier = Modifier
                                .clickable { viewModel.selectGame(g.id) }
                                .testTag("troubleshoot_game_${g.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(g.coverEmoji, fontSize = 15.sp)
                                Text(
                                    text = g.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.Black else colors.textPrimary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Symptom Selector Grid / Pills
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = "¿Qué problema estás experimentando?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TroubleshootingSymptom.values().forEach { symptom ->
                        val isSelected = symptom == activeSymptom
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) colors.primary.copy(alpha = 0.16f) else colors.surface,
                            border = BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) colors.primary else colors.surfaceBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { activeSymptom = symptom }
                                .testTag("symptom_option_${symptom.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(symptom.emoji, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = symptom.title,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isSelected) colors.primary else colors.textPrimary
                                    )
                                    Text(
                                        text = symptom.category,
                                        fontSize = 11.sp,
                                        color = colors.textMuted
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Seleccionado",
                                        tint = colors.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Hardware Context Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TUS ESPECIFICACIONES VS REQUISITOS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.secondary
                        )
                        Text(
                            text = "Juego: ${game.title}",
                            fontSize = 11.sp,
                            color = colors.textSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SpecComparisonPill(
                            label = "CPU",
                            userValue = "Nv. ${userSpecs.cpuScore}/5",
                            reqValue = "Mín. ${game.minCpuScore}/5",
                            isSufficient = userSpecs.cpuScore >= game.minCpuScore,
                            modifier = Modifier.weight(1f)
                        )
                        SpecComparisonPill(
                            label = "GPU",
                            userValue = "Nv. ${userSpecs.gpuScore}/5",
                            reqValue = "Mín. ${game.minGpuScore}/5",
                            isSufficient = userSpecs.gpuScore >= game.minGpuScore,
                            modifier = Modifier.weight(1f)
                        )
                        SpecComparisonPill(
                            label = "RAM",
                            userValue = "${userSpecs.ramGb} GB",
                            reqValue = "Mín. ${game.minRamGb} GB",
                            isSufficient = userSpecs.ramGb >= game.minRamGb,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Diagnosis Result Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("diagnostic_result_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.5.dp, report.severityColor.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header with root cause
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🔎", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DIAGNÓSTICO DEL SISTEMA",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = colors.textMuted
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = report.severityColor.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, report.severityColor)
                        ) {
                            Text(
                                text = report.rootCauseSeverity,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = report.severityColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = report.rootCauseTitle,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = report.detailedExplanation,
                        fontSize = 13.sp,
                        color = colors.textSecondary,
                        lineHeight = 18.sp
                    )

                    if (report.hardwareComparisonNote.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = colors.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("💡", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = report.hardwareComparisonNote,
                                    fontSize = 11.sp,
                                    color = colors.textPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = colors.surfaceBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Step by step recommendations
                    Text(
                        text = "SOLUCIONES Y PASOS A SEGUIR:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        report.stepByStepSolutions.forEach { step ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = colors.surfaceVariant,
                                border = BorderStroke(1.dp, colors.surfaceBorder),
                                modifier = Modifier.fillMaxWidth()
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
                                                color = colors.primary,
                                                modifier = Modifier.size(22.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = step.stepNumber.toString(),
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = Color.Black
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = step.title,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = colors.textPrimary
                                            )
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = colors.surface
                                        ) {
                                            Text(
                                                text = step.tag,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = colors.textMuted,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = step.description,
                                        fontSize = 12.sp,
                                        color = colors.textSecondary,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onNavigateToSection(AppSection.CAN_I_RUN_IT) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                        ) {
                            Text(
                                text = "Verificar Hardware",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        OutlinedButton(
                            onClick = { onNavigateToSection(AppSection.GAME_DETAILS) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, colors.surfaceBorder),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textPrimary)
                        ) {
                            Text(
                                text = "Ver Ficha del Juego",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecComparisonPill(
    label: String,
    userValue: String,
    reqValue: String,
    isSufficient: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val statusColor = if (isSufficient) Color(0xFF06D6A0) else Color(0xFFEF476F)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = colors.surface,
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textMuted
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = userValue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
            Text(
                text = reqValue,
                fontSize = 9.sp,
                color = colors.textSecondary
            )
        }
    }
}

private fun generateDiagnosticReport(
    symptom: TroubleshootingSymptom,
    game: Game,
    userSpecs: HardwareSpecs
): DiagnosticReport {
    val cpuWeak = userSpecs.cpuScore < game.minCpuScore
    val cpuMed = userSpecs.cpuScore < game.recCpuScore
    val gpuWeak = userSpecs.gpuScore < game.minGpuScore
    val gpuMed = userSpecs.gpuScore < game.recGpuScore
    val ramLow = userSpecs.ramGb < game.minRamGb
    val ramMed = userSpecs.ramGb < game.recRamGb

    return when (symptom) {
        TroubleshootingSymptom.GAME_SLOW -> {
            when {
                cpuWeak && gpuWeak -> DiagnosticReport(
                    rootCauseTitle = "CPU y Tarjeta Gráfica por debajo de lo mínimo requerido",
                    rootCauseSeverity = "Causa Crítica de Hardware",
                    severityColor = Color(0xFFEF476F),
                    detailedExplanation = "Tanto tu procesador (Nivel ${userSpecs.cpuScore}) como tu gráfica (Nivel ${userSpecs.gpuScore}) son inferiores a los requisitos mínimos de ${game.title} (mínimo CPU Nivel ${game.minCpuScore} y GPU Nivel ${game.minGpuScore}). El juego no puede mantener los fotogramas mínimos necesarios en combates y zonas abiertas.",
                    hardwareComparisonNote = "Requisito mínimo del juego: ${game.minCpuText} y ${game.minGpuText}.",
                    stepByStepSolutions = listOf(
                        TroubleshootingStep(
                            stepNumber = 1,
                            title = "Activar Escalador (FSR / DLSS / XeSS)",
                            description = "Activa FSR (AMD) o DLSS (NVIDIA) en modo 'Ultra Rendimiento' o 'Rendimiento'. Esto renderiza el juego a menor resolución y lo reescala con IA, ganando entre 40% y 80% más de FPS.",
                            tag = "Ajustes del Juego"
                        ),
                        TroubleshootingStep(
                            stepNumber = 2,
                            title = "Bajar Sombras, Reflejos y Ray Tracing",
                            description = "El trazado de rayos (Ray Tracing) y la calidad de sombras consumen más del 50% de la GPU. Desactiva Ray Tracing por completo y ajusta sombras en 'Bajo'.",
                            tag = "Gráficos"
                        ),
                        TroubleshootingStep(
                            stepNumber = 3,
                            title = "Reducir la Resolución de Pantalla",
                            description = "Si juegas a 1440p o 1080p, baja la resolución a 1600x900 o 720p en pantalla completa para aliviar de inmediato la carga de la GPU.",
                            tag = "Resolución"
                        )
                    )
                )
                cpuWeak -> DiagnosticReport(
                    rootCauseTitle = "Cuello de botella en el Procesador (CPU)",
                    rootCauseSeverity = "Límitación de CPU",
                    severityColor = Color(0xFFFF9F1C),
                    detailedExplanation = "Tu tarjeta gráfica es competente, pero tu procesador (Nivel ${userSpecs.cpuScore}) es insuficiente frente a lo que exige ${game.title} (Nivel ${game.minCpuScore}). La CPU no logra procesar la inteligencia artificial de los NPCs, las físicas y la simulación del mundo abierto a tiempo para alimentar la GPU.",
                    hardwareComparisonNote = "El juego requiere al menos: ${game.minCpuText}.",
                    stepByStepSolutions = listOf(
                        TroubleshootingStep(
                            stepNumber = 1,
                            title = "Reducir Densidad de Multitud y Tráfico",
                            description = "En Opciones Gráficas o de Juego, reduce la 'Densidad de Población' o 'Detalle del Mundo'. Esto libera enormemente los hilos del procesador.",
                            tag = "Ajustes de CPU"
                        ),
                        TroubleshootingStep(
                            stepNumber = 2,
                            title = "Limitar los FPS a 30 o 45",
                            description = "Establece un límite de fotogramas de 30 FPS en el menú de pantalla. Al no forzar a la CPU a buscar 60 FPS, evitarás picos de uso al 100% y la experiencia será constante y fluida.",
                            tag = "Estabilidad"
                        ),
                        TroubleshootingStep(
                            stepNumber = 3,
                            title = "Cerrar programas de fondo",
                            description = "Cierra navegadores (Google Chrome / Edge), Discord, grabadores y antivirus activos que consuman ciclos de CPU en segundo plano.",
                            tag = "Windows / Sistema"
                        )
                    )
                )
                gpuWeak -> DiagnosticReport(
                    rootCauseTitle = "Tarjeta Gráfica (GPU) insuficiente",
                    rootCauseSeverity = "Limitación de GPU",
                    severityColor = Color(0xFFFF9F1C),
                    detailedExplanation = "Tu procesador tiene potencia suficiente, pero la tarjeta gráfica (Nivel ${userSpecs.gpuScore}) se satura al renderizar texturas y efectos de iluminación de ${game.title}. Requiere al menos ${game.minGpuText}.",
                    hardwareComparisonNote = "Tu GPU está en Nivel ${userSpecs.gpuScore} y el juego pide mínimo Nivel ${game.minGpuScore}.",
                    stepByStepSolutions = listOf(
                        TroubleshootingStep(
                            stepNumber = 1,
                            title = "Activar FSR / DLSS / Reescalado Dinámico",
                            description = "Activa AMD FSR o NVIDIA DLSS en modo 'Rendimiento'. Esto reduce el esfuerzo directo del chip gráfico.",
                            tag = "Reescalado"
                        ),
                        TroubleshootingStep(
                            stepNumber = 2,
                            title = "Desactivar Oclusión Ambiental y Niebla Volumétrica",
                            description = "Efectos volumétricos como niebla densa, humo y nubes consumen gran cantidad de cálculos de sombreado. Colócalos en 'Bajo'.",
                            tag = "Gráficos"
                        ),
                        TroubleshootingStep(
                            stepNumber = 3,
                            title = "Actualizar Controladores de Gráfica",
                            description = "Instala los últimos drivers 'Game Ready' de NVIDIA, AMD o Intel, que suelen incluir parches de rendimiento específicos para ${game.title}.",
                            tag = "Drivers"
                        )
                    )
                )
                else -> DiagnosticReport(
                    rootCauseTitle = "Configuración gráfica por encima del rango óptimo",
                    rootCauseSeverity = "Ajuste de Configuración",
                    severityColor = Color(0xFF06D6A0),
                    detailedExplanation = "Tus especificaciones cumplen con los requisitos mínimos del juego, pero es muy probable que tengas activadas opciones exigentes como resolución 4K nativa, Ray Tracing completo, o sincronización vertical con doble búfer.",
                    hardwareComparisonNote = "Tu PC tiene hardware capaz de correr ${game.title} si ajustas los perfiles de calidad a nivel medio/alto.",
                    stepByStepSolutions = listOf(
                        TroubleshootingStep(
                            stepNumber = 1,
                            title = "Activar FSR 3 o DLSS Calidad",
                            description = "Obtendrás la misma nitidez visual con un 30% a 50% extra de fotogramas por segundo.",
                            tag = "Ajustes Recomendados"
                        ),
                        TroubleshootingStep(
                            stepNumber = 2,
                            title = "Utilizar Pantalla Completa Exclusiva",
                            description = "Evita el modo 'Ventana sin bordes', ya que el gestor de ventanas del sistema operativo añade latencia y reduce los FPS.",
                            tag = "Modo de Pantalla"
                        ),
                        TroubleshootingStep(
                            stepNumber = 3,
                            title = "Desactivar Sincronización Vertical tradicional",
                            description = "Si no tienes pantalla G-Sync/FreeSync, la sincronización vertical puede cortar tus FPS a la mitad si bajan de 60.",
                            tag = "V-Sync"
                        )
                    )
                )
            }
        }

        TroubleshootingSymptom.FPS_STUTTER -> {
            when {
                ramLow -> DiagnosticReport(
                    rootCauseTitle = "Memoria RAM insuficiente (Memoria Virtual en Disco)",
                    rootCauseSeverity = "Causa Crítica: Memoria RAM",
                    severityColor = Color(0xFFEF476F),
                    detailedExplanation = "Tu equipo cuenta con ${userSpecs.ramGb} GB de RAM, pero ${game.title} exige al menos ${game.minRamGb} GB (y recomienda ${game.recRamGb} GB). Al llenarse la memoria física, Windows traslada datos al disco duro (archivo de paginación), provocando congelamientos bruscos de 1 a 3 segundos.",
                    hardwareComparisonNote = "Tienes ${userSpecs.ramGb} GB de RAM. Requisito mínimo: ${game.minRamGb} GB.",
                    stepByStepSolutions = listOf(
                        TroubleshootingStep(
                            stepNumber = 1,
                            title = "Cerrar navegadores y apps en segundo plano",
                            description = "Navegadores como Chrome con varias pestañas pueden consumir 2 a 4 GB de RAM. Ciérralos antes de iniciar el juego.",
                            tag = "Memoria RAM"
                        ),
                        TroubleshootingStep(
                            stepNumber = 2,
                            title = "Bajar Calidad de Texturas a 'Bajo' o 'Medio'",
                            description = "Las texturas de alta resolución llenan rápidamente la RAM del sistema y la VRAM de la gráfica, desencadenando tirones.",
                            tag = "Texturas"
                        ),
                        TroubleshootingStep(
                            stepNumber = 3,
                            title = "Aumentar archivo de paginación en disco SSD",
                            description = "Configura el archivo de memoria virtual de Windows en una unidad SSD rápida con tamaño administrado por el sistema.",
                            tag = "Windows"
                        )
                    )
                )
                userSpecs.storageGb < game.storageRequiredGb -> DiagnosticReport(
                    rootCauseTitle = "Falta de espacio en disco o instalación en disco HDD",
                    rootCauseSeverity = "Cuello de botella de Almacenamiento",
                    severityColor = Color(0xFFFF9F1C),
                    detailedExplanation = "${game.title} requiere ${game.storageRequiredGb} GB de espacio y lectura continua de datos del mapa. Si el disco está casi lleno o es un disco mecánico tradicional (HDD), las texturas y modelos 3D no se cargan a tiempo al moverte rápido, causando tartamudeo.",
                    hardwareComparisonNote = "Espacio requerido por el juego: ${game.storageRequiredGb} GB.",
                    stepByStepSolutions = listOf(
                        TroubleshootingStep(
                            stepNumber = 1,
                            title = "Mover el juego a una unidad SSD / NVMe",
                            description = "Los discos SSD leen 5 a 30 veces más rápido que un HDD tradicional, eliminando por completo los tirones de streaming de texturas.",
                            tag = "SSD"
                        ),
                        TroubleshootingStep(
                            stepNumber = 2,
                            title = "Liberar al menos 15% de espacio en la unidad",
                            description = "Las unidades SSD necesitan espacio libre para gestionar la memoria caché y operaciones de escritura temporales.",
                            tag = "Espacio Libre"
                        )
                    )
                )
                else -> DiagnosticReport(
                    rootCauseTitle = "Compilación de Shaders en Segundo Plano o Latencia de V-Sync",
                    rootCauseSeverity = "Causa de Software / Motor",
                    severityColor = Color(0xFFFF9F1C),
                    detailedExplanation = "Los tirones esporádicos al entrar a nuevas salas o disparar por primera vez suelen deberse a la compilación de shaders de DirectX 12 / Vulkan, o a fluctuaciones entre 55 y 60 FPS sin tasa de refresco variable.",
                    hardwareComparisonNote = "Tu procesador y tarjeta gráfica son suficientes para el juego.",
                    stepByStepSolutions = listOf(
                        TroubleshootingStep(
                            stepNumber = 1,
                            title = "Dejar que compilen los Shaders al iniciar",
                            description = "Muchos juegos modernos compilan shaders en la pantalla de inicio. Espera a que la barra llegue al 100% antes de entrar a tu partida.",
                            tag = "Shaders"
                        ),
                        TroubleshootingStep(
                            stepNumber = 2,
                            title = "Bloquear FPS con Rivatuner o Panel de Control",
                            description = "Bloquear los fotogramas a una cifra exacta (por ejemplo 60 o 75 FPS) con un limitador externo produce tiempos de cuadro ('frame times') perfectamente planos sin micro-parones.",
                            tag = "Frame Pacing"
                        )
                    )
                )
            }
        }

        TroubleshootingSymptom.TEXTS_UNREADABLE -> {
            DiagnosticReport(
                rootCauseTitle = "Escalado de Interfaz y Contraste de Subtítulos",
                rootCauseSeverity = "Ajustes de Accesibilidad Disponibles",
                severityColor = Color(0xFF118AB2),
                detailedExplanation = "Los monitores modernos de alta resolución (1440p / 4K) o televisores a distancia hacen que los textos por defecto resulten diminutos. Afortunadamente, ${game.title} cuenta con opciones de accesibilidad integradas para solucionarlo.",
                hardwareComparisonNote = if (game.accessibility.subtitleSizeAdjustable) "✅ ${game.title} permite cambiar el tamaño de los subtítulos." else "⚠️ El juego tiene tamaño fijo de subtítulos.",
                stepByStepSolutions = listOf(
                    TroubleshootingStep(
                        stepNumber = 1,
                        title = "Ajustar Tamaño de Subtítulos en Opciones",
                        description = if (game.accessibility.subtitleSizeAdjustable) {
                            "Ve a Menú -> Ajustes -> Accesibilidad o Sonido -> Tamaño de Subtítulos y cámbialo a 'Grande' o 'Muy Grande'."
                        } else {
                            "Este juego no cuenta con selector de tamaño de subtítulos directo; prueba a reducir la resolución de renderizado o activar la lupa de Windows."
                        },
                        tag = "Accesibilidad"
                    ),
                    TroubleshootingStep(
                        stepNumber = 2,
                        title = "Activar Fondo Oscuro o Alto Contraste",
                        description = if (game.accessibility.subtitleBackgroundContrast) {
                            "Activa la opción 'Fondo de Subtítulos' o 'Contraste'. Colocará una barra oscura detrás del texto para que no se confunda con explosiones ni fondos claros."
                        } else {
                            "Activa subtítulos con identificación de hablante para facilitar el seguimiento del diálogo."
                        },
                        tag = "Visibilidad"
                    ),
                    TroubleshootingStep(
                        stepNumber = 3,
                        title = "Escalado del HUD e Interfaz",
                        description = if (game.accessibility.hudScaling) {
                            "Ve a Opciones de Pantalla / Interfaz y sube el 'Escalado del HUD' al 110% - 130% para agrandar menús, brújula y vida."
                        } else {
                            "Aumenta la escala de pantalla en Windows al 125% o 150% antes de abrir el juego."
                        },
                        tag = "Interfaz"
                    )
                )
            )
        }

        TroubleshootingSymptom.CONTROLS_DIFFICULT -> {
            DiagnosticReport(
                rootCauseTitle = "Curva de Sensibilidad, Ayuda de Apuntado o Mando",
                rootCauseSeverity = "Configuración de Controles",
                severityColor = Color(0xFF118AB2),
                detailedExplanation = "La dificultad con los controles suele originarse por una sensibilidad de cámara demasiado alta, una zona muerta mal calibrada en los sticks analógicos o por falta de ayuda de apuntado activa.",
                hardwareComparisonNote = if (game.accessibility.fullButtonRemapping) "✅ ${game.title} permite reasignación total de botones." else "Esquema de control predefinido.",
                stepByStepSolutions = listOf(
                    TroubleshootingStep(
                        stepNumber = 1,
                        title = "Activar o Aumentar Ayuda de Apuntado (Aim Assist)",
                        description = if (game.accessibility.aimAssistAdjustable) {
                            "En Ajustes de Mando / Accesibilidad, activa 'Asistencia de Apuntado' en nivel 'Alto' o 'Enfoque'. Te ayudará a fijar enemigos con facilidad."
                        } else {
                            "Reduce la sensibilidad de los ejes X e Y para lograr un rastreo más suave con la mira."
                        },
                        tag = "Asistencia"
                    ),
                    TroubleshootingStep(
                        stepNumber = 2,
                        title = "Reasignar Botones a Acciones Cómodas",
                        description = if (game.accessibility.fullButtonRemapping) {
                            "Reasigna esquivar, correr o saltar a los botones laterales (gatillos L1/R1 o L2/R2) para no tener que soltar el stick derecho de la cámara."
                        } else {
                            "Usa la reasignación de botones de Steam Input o de la consola PlayStation en Ajustes del Sistema."
                        },
                        tag = "Reasignación"
                    ),
                    TroubleshootingStep(
                        stepNumber = 3,
                        title = "Alternar entre 'Mantener' y 'Pulsar'",
                        description = if (game.accessibility.toggleVsHoldOption) {
                            "Cambia agacharse, apuntar o correr de 'Mantener pulsado' a 'Pulsar una vez (Toggle)'. Reduce drásticamente la fatiga en los dedos."
                        } else {
                            "Configura una zona muerta del 5% al 8% en el stick si notas que la cámara se mueve sola (drift)."
                        },
                        tag = "Ergonomía"
                    )
                )
            )
        }

        TroubleshootingSymptom.OVERHEATING_NOISE -> {
            DiagnosticReport(
                rootCauseTitle = "Uso de GPU/CPU al 100% sin límite de FPS",
                rootCauseSeverity = "Causa Térmica / Carga Máxima",
                severityColor = Color(0xFFEF476F),
                detailedExplanation = "Cuando los FPS están desbloqueados, la tarjeta gráfica trabaja a su máxima potencia posible para generar 100 o 200 fotogramas que tu monitor quizá ni siquiera puede mostrar a 60 Hz, elevando la temperatura a más de 80°C y acelerando los ventiladores al 100%.",
                hardwareComparisonNote = "Tu hardware está entregando el 100% de su capacidad para ${game.title}.",
                stepByStepSolutions = listOf(
                    TroubleshootingStep(
                        stepNumber = 1,
                        title = "Limitar los FPS a la frecuencia de tu pantalla",
                        description = "Si tu monitor es de 60 Hz o 75 Hz, fija los FPS exactamente a 60 en los ajustes del juego. La GPU descansará en cuanto alcance esa cifra y bajará entre 10°C y 20°C de temperatura.",
                        tag = "Límite Térmico"
                    ),
                    TroubleshootingStep(
                        stepNumber = 2,
                        title = "Limpiar polvo de ventiladores y disipador",
                        description = "El polvo acumulado en las rejillas de ventilación de tu PC o consola impide la evacuación del aire caliente.",
                        tag = "Mantenimiento"
                    ),
                    TroubleshootingStep(
                        stepNumber = 3,
                        title = "Activar sincronización vertical o modo ecológico",
                        description = "Evita que la tarjeta gráfica dibuje fotogramas innecesarios en menús y pausas.",
                        tag = "Consumo"
                    )
                )
            )
        }

        TroubleshootingSymptom.LONG_LOADINGS -> {
            DiagnosticReport(
                rootCauseTitle = "Velocidad de Lectura del Almacenamiento",
                rootCauseSeverity = "Almacenamiento y Lectura",
                severityColor = Color(0xFFFF9F1C),
                detailedExplanation = "${game.title} ocupa ${game.storageRequiredGb} GB. Los discos mecánicos HDD leen a unos 100 MB/s, lo que genera tiempos de carga de más de 40 a 75 segundos entre muertes y viajes rápidos.",
                hardwareComparisonNote = "Tiempos estimados: en SSD NVMe: < 5 segundos. En HDD: 45 - 75 segundos.",
                stepByStepSolutions = listOf(
                    TroubleshootingStep(
                        stepNumber = 1,
                        title = "Instalar en unidad SSD / NVMe",
                        description = "Mueve la carpeta del juego a un disco de estado sólido SSD. Los tiempos de carga pasarán de 60 segundos a menos de 6 segundos.",
                        tag = "Almacenamiento"
                    ),
                    TroubleshootingStep(
                        stepNumber = 2,
                        title = "Desactivar grabación continua en segundo plano",
                        description = "Herramientas de repetición instantánea que graban continuamente el disco duro saturan el ancho de banda de lectura del juego.",
                        tag = "Optimización"
                    )
                )
            )
        }
    }
}
