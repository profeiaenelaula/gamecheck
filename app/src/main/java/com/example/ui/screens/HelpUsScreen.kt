package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.GameSubmissionEntity
import com.example.ui.theme.AppTheme
import com.example.viewmodel.GameCheckViewModel

@Composable
fun HelpUsScreen(viewModel: GameCheckViewModel) {
    val colors = AppTheme.colors
    val submissions by viewModel.gameSubmissions.collectAsState()

    var title by remember { mutableStateOf("") }
    var developer by remember { mutableStateOf("") }
    var genres by remember { mutableStateOf("") }
    var releaseYear by remember { mutableStateOf("2024") }
    var synopsis by remember { mutableStateOf("") }
    var minCpuText by remember { mutableStateOf("") }
    var recCpuText by remember { mutableStateOf("") }
    var minGpuText by remember { mutableStateOf("") }
    var recGpuText by remember { mutableStateOf("") }
    var minRamGb by remember { mutableStateOf(16f) }
    var recRamGb by remember { mutableStateOf(16f) }
    var storageGb by remember { mutableStateOf(80f) }
    var violenceScore by remember { mutableStateOf(3f) }
    var bloodScore by remember { mutableStateOf(3f) }
    var horrorScore by remember { mutableStateOf(2f) }
    var languageScore by remember { mutableStateOf(2f) }
    var ageRating by remember { mutableStateOf("PEGI 16") }
    var sensitiveThemes by remember { mutableStateOf("") }
    var basePriceUsd by remember { mutableStateOf("59.99") }
    var deluxePriceUsd by remember { mutableStateOf("79.99") }
    var durationHours by remember { mutableStateOf("30") }
    var showConfirmation by remember { mutableStateOf(false) }

    fun resetForm() {
        title = ""; developer = ""; genres = ""; releaseYear = "2024"; synopsis = ""
        minCpuText = ""; recCpuText = ""; minGpuText = ""; recGpuText = ""
        minRamGb = 16f; recRamGb = 16f; storageGb = 80f
        violenceScore = 3f; bloodScore = 3f; horrorScore = 2f; languageScore = 2f
        ageRating = "PEGI 16"; sensitiveThemes = ""
        basePriceUsd = "59.99"; deluxePriceUsd = "79.99"; durationHours = "30"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .testTag("help_us_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.surfaceBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🤝", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Propón un juego",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }
                    Text(
                        text = "Ayúdanos a crecer la base de datos. Completa los requisitos, precios y categorías del juego que quieras sumar; nuestro equipo lo revisará antes de publicarlo.",
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }

        item {
            HelpUsSectionCard(title = "Información General", icon = "🎮") {
                LabeledField("Título del juego", title, { title = it }, "help_us_title")
                LabeledField("Desarrollador / Estudio", developer, { developer = it }, "help_us_developer")
                LabeledField("Géneros (separados por coma)", genres, { genres = it }, "help_us_genres")
                LabeledField(
                    "Año de lanzamiento", releaseYear, { releaseYear = it }, "help_us_year",
                    keyboardType = KeyboardType.Number
                )
                LabeledField(
                    "Sinopsis breve", synopsis, { synopsis = it }, "help_us_synopsis",
                    singleLine = false
                )
            }
        }

        item {
            HelpUsSectionCard(title = "Requisitos de Hardware", icon = "⚡") {
                LabeledField("CPU Mínima", minCpuText, { minCpuText = it }, "help_us_min_cpu")
                LabeledField("CPU Recomendada", recCpuText, { recCpuText = it }, "help_us_rec_cpu")
                LabeledField("GPU Mínima", minGpuText, { minGpuText = it }, "help_us_min_gpu")
                LabeledField("GPU Recomendada", recGpuText, { recGpuText = it }, "help_us_rec_gpu")
                LabeledSlider("RAM Mínima", "${minRamGb.toInt()} GB", minRamGb, { minRamGb = it }, 2f..64f)
                LabeledSlider("RAM Recomendada", "${recRamGb.toInt()} GB", recRamGb, { recRamGb = it }, 2f..64f)
                LabeledSlider("Almacenamiento", "${storageGb.toInt()} GB", storageGb, { storageGb = it }, 5f..250f)
            }
        }

        item {
            HelpUsSectionCard(title = "Contenido y Categorías", icon = "🛡️") {
                LabeledSlider("Violencia", scoreLabel(violenceScore), violenceScore, { violenceScore = it }, 1f..5f, steps = 3)
                LabeledSlider("Sangre / Gore", scoreLabel(bloodScore), bloodScore, { bloodScore = it }, 1f..5f, steps = 3)
                LabeledSlider("Terror", scoreLabel(horrorScore), horrorScore, { horrorScore = it }, 1f..5f, steps = 3)
                LabeledSlider("Lenguaje soez", scoreLabel(languageScore), languageScore, { languageScore = it }, 1f..5f, steps = 3)
                LabeledField("Clasificación por edad (ej. PEGI 16)", ageRating, { ageRating = it }, "help_us_age_rating")
                LabeledField("Temas sensibles a destacar", sensitiveThemes, { sensitiveThemes = it }, "help_us_sensitive_themes")
            }
        }

        item {
            HelpUsSectionCard(title = "Precios y Duración", icon = "💰") {
                LabeledField(
                    "Precio Edición Base (USD)", basePriceUsd, { basePriceUsd = it }, "help_us_base_price",
                    keyboardType = KeyboardType.Decimal
                )
                LabeledField(
                    "Precio Edición Deluxe (USD)", deluxePriceUsd, { deluxePriceUsd = it }, "help_us_deluxe_price",
                    keyboardType = KeyboardType.Decimal
                )
                LabeledField(
                    "Duración historia principal (horas)", durationHours, { durationHours = it }, "help_us_duration",
                    keyboardType = KeyboardType.Number
                )
            }
        }

        item {
            if (showConfirmation) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colors.primary.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, colors.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✅ ¡Gracias! Tu propuesta fue enviada a la cola de revisión.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.primary,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    val submission = GameSubmissionEntity(
                        title = title.ifBlank { "Juego sin título" },
                        developer = developer,
                        genres = genres,
                        releaseYear = releaseYear.toIntOrNull() ?: 2024,
                        synopsis = synopsis,
                        minCpuText = minCpuText,
                        recCpuText = recCpuText,
                        minGpuText = minGpuText,
                        recGpuText = recGpuText,
                        minRamGb = minRamGb.toInt(),
                        recRamGb = recRamGb.toInt(),
                        storageGb = storageGb.toInt(),
                        violenceScore = violenceScore.toInt(),
                        bloodScore = bloodScore.toInt(),
                        horrorScore = horrorScore.toInt(),
                        languageScore = languageScore.toInt(),
                        ageRating = ageRating,
                        sensitiveThemes = sensitiveThemes,
                        basePriceUsd = basePriceUsd.toDoubleOrNull() ?: 59.99,
                        deluxePriceUsd = deluxePriceUsd.toDoubleOrNull() ?: 79.99,
                        durationHours = durationHours.toFloatOrNull() ?: 30.0f
                    )
                    viewModel.submitGameProposal(submission) {
                        showConfirmation = true
                    }
                    resetForm()
                },
                enabled = title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("help_us_submit_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = Color.Black,
                    disabledContainerColor = colors.surfaceVariant
                )
            ) {
                Text(text = "Enviar Propuesta", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }

        item {
            Text(
                text = "Cola de Revisión Comunitaria (${submissions.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (submissions.isEmpty()) {
            item {
                Text(
                    text = "Aún no hay propuestas en la cola. ¡Sé el primero en proponer un juego!",
                    fontSize = 12.sp,
                    color = colors.textSecondary
                )
            }
        } else {
            items(submissions, key = { it.id }) { submission ->
                SubmissionQueueCard(
                    submission = submission,
                    onDelete = { viewModel.deleteGameSubmission(submission.id) }
                )
            }
        }
    }
}

@Composable
private fun HelpUsSectionCard(title: String, icon: String, content: @Composable ColumnScope.() -> Unit) {
    val colors = AppTheme.colors
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.surfaceBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    testTag: String,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val colors = AppTheme.colors
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .testTag(testTag),
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

@Composable
private fun LabeledSlider(
    label: String,
    valueLabel: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
    steps: Int = 0
) {
    val colors = AppTheme.colors
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 12.sp, color = colors.textSecondary, fontWeight = FontWeight.Medium)
            Text(text = valueLabel, fontSize = 12.sp, color = colors.primary, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            steps = steps,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = colors.primary,
                activeTrackColor = colors.primary,
                inactiveTrackColor = colors.surfaceBorder
            )
        )
    }
}

private fun scoreLabel(score: Float): String = when (score.toInt()) {
    1 -> "1 - Nulo"
    2 -> "2 - Leve"
    3 -> "3 - Moderado"
    4 -> "4 - Alto"
    else -> "5 - Extremo"
}

@Composable
private fun SubmissionQueueCard(submission: GameSubmissionEntity, onDelete: () -> Unit) {
    val colors = AppTheme.colors
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("submission_card_${submission.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.surfaceBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = colors.primary.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = submission.emoji, fontSize = 22.sp)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = submission.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    maxLines = 1
                )
                Text(
                    text = submission.developer.ifBlank { "Desarrollador no especificado" },
                    fontSize = 11.sp,
                    color = colors.textSecondary,
                    maxLines = 1
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = colors.surfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = submission.status,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("submission_delete_${submission.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar propuesta",
                    tint = colors.textMuted
                )
            }
        }
    }
}
