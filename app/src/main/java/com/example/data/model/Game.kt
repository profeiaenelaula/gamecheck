package com.example.data.model

data class HardwareTier(
    val id: String,
    val name: String,
    val score: Int // 1 to 5
)

data class HardwareSpecs(
    val cpuName: String = "Intel Core i5-10400 / Ryzen 5 3600",
    val cpuScore: Int = 3, // 1: Básico, 2: Entrada, 3: Medio, 4: Alto, 5: Extremo
    val gpuName: String = "NVIDIA GeForce GTX 1660 Super / RX 5600 XT",
    val gpuScore: Int = 3, // 1: Integrada, 2: Entrada, 3: Media, 4: Alta, 5: Extrema
    val ramGb: Int = 16,
    val os: String = "Windows 11 (64-bit)",
    val storageGb: Int = 500
)

enum class CompatibilityStatus(val label: String, val description: String) {
    RUNS_GREAT("75% - 100% Compatible", "Cumples o superas los requisitos recomendados. Experiencia fluida a 60+ FPS en calidad Alta/Ultra."),
    RUNS_MEDIUM("50% - 74% Compatible", "Cumples requisitos mínimos esenciales. Jugable a 30-45 FPS con ajustes gráficos medios o bajos."),
    RUNS_NO("0% - 49% No Compatible", "Tu hardware no cumple con los requisitos mínimos esenciales para jugar con estabilidad.")
}

enum class CompatColorTier(val label: String, val colorHex: Long) {
    GREEN("75% - 100% (Verde)", 0xFF10B981),
    YELLOW("50% - 74% (Amarillo)", 0xFFF59E0B),
    RED("0% - 49% (Rojo)", 0xFFEF4444)
}

data class CompatibilityEvaluation(
    val status: CompatibilityStatus,
    val percentage: Int, // 0 to 100%
    val cpuPassMin: Boolean,
    val cpuPassRec: Boolean,
    val gpuPassMin: Boolean,
    val gpuPassRec: Boolean,
    val ramPassMin: Boolean,
    val ramPassRec: Boolean,
    val osPass: Boolean,
    val storagePass: Boolean,
    val bottlenecks: List<String>,
    val cpuScorePct: Int = 100,
    val gpuScorePct: Int = 100,
    val ramScorePct: Int = 100,
    val storageScorePct: Int = 100
) {
    val tier: CompatColorTier
        get() = when {
            percentage >= 75 -> CompatColorTier.GREEN
            percentage >= 50 -> CompatColorTier.YELLOW
            else -> CompatColorTier.RED
        }
}

data class AccessibilityFeatures(
    // Subtítulos
    val subtitleSizeAdjustable: Boolean = true,
    val subtitleBackgroundContrast: Boolean = true,
    val speakerIdentification: Boolean = true,
    val directionalSoundCues: Boolean = true,
    val fullSpanishAudioAndSub: Boolean = true,
    
    // Controles
    val fullButtonRemapping: Boolean = true,
    val toggleVsHoldOption: Boolean = true,
    val aimAssistAdjustable: Boolean = true,
    val simplifiedControlScheme: Boolean = true,
    val adaptiveControllerCompatible: Boolean = true,
    
    // Asistencia visual y auditiva
    val colorblindFilters: List<String> = listOf("Protanopia", "Deuteranopia", "Tritanopia"),
    val highContrastMode: Boolean = true,
    val hudScaling: Boolean = true,
    val textToSpeechScreenReader: Boolean = false,
    val reduceMotionAndFlashes: Boolean = true,

    val scoreLetter: String = "A" // A+, A, B, C
)

data class ContentWarnings(
    val violenceLevel: String, // "Baja", "Moderada", "Alta / Brutal"
    val violenceScore: Int, // 1 to 5
    val bloodGore: String, // "Sin sangre", "Sangre moderada", "Gore explícito y mutilación"
    val bloodScore: Int, // 1 to 5
    val horrorLevel: String, // "Ninguno", "Tensión atmosférica", "Terror psicológico y Jump Scares"
    val horrorScore: Int, // 1 to 5
    val strongLanguage: String, // "Limpio", "Ocasional", "Explícito y constante"
    val languageScore: Int, // 1 to 5
    val sensitiveThemes: List<String>, // Ej: "Depresión", "Consumo de sustancias", "Fobia a insectos/arañas", "Parpadeo de luces / Fotosensibilidad"
    val ageRating: String, // "PEGI 18 / ESRB M"
    val detailedSummary: String
)

data class GameDuration(
    val mainStoryHours: Float,
    val mainPlusExtraHours: Float,
    val completionistHours: Float,
    val speedrunHours: Float,
    val relaxedHours: Float
)

data class CategoryRatings(
    val historia: Float, // 1.0 to 5.0
    val jugabilidad: Float,
    val graficos: Float,
    val estrategia: Float,
    val sonido: Float,
    val optimizacion: Float
) {
    val overallAverage: Float
        get() = (historia + jugabilidad + graficos + estrategia + sonido + optimizacion) / 6f
}

data class CommunityReview(
    val id: String,
    val gameId: String,
    val author: String,
    val comment: String,
    val ratings: CategoryRatings,
    val dateString: String
)

data class GamePricing(
    val basePriceUsd: Double,
    val baseEditionName: String = "Edición Estándar",
    val baseIncludes: List<String> = listOf("Juego base completo"),
    
    val deluxePriceUsd: Double,
    val deluxeEditionName: String = "Edición Deluxe / Completa",
    val deluxeIncludes: List<String>,
    
    val dlcPriceUsd: Double = 0.0,
    val dlcName: String = "",
    val dlcIncludes: List<String> = emptyList(),
    
    val salePriceUsd: Double? = null,
    val dealsNote: String = ""
) {
    val extrasDifferenceUsd: Double
        get() = (deluxePriceUsd - basePriceUsd).coerceAtLeast(0.0)
}

data class PlatformPerformance(
    val platformName: String, // "PC", "PlayStation 4", "PlayStation 5"
    val fpsTarget: String, // "60 - 120+ FPS", "30 FPS", "60 FPS Dinámicos"
    val resolution: String, // "Hasta 4K con DLSS/FSR", "1080p nativo", "1440p / 4K"
    val loadingTime: String, // "Ultrarrápido (< 5s NVMe)", "Lento (45s - 70s HDD)"
    val stabilityBadge: String, // "Rendimiento Óptimo", "Excelente", "Ajustado", "No Recomendado"
    val pros: List<String>,
    val cons: List<String>,
    val score: Int // 1 a 10
)

data class PlatformComparison(
    val recommendedPlatform: String, // ej. "PlayStation 5 o PC"
    val buyingRecommendation: String, // Consejo detallado de compra
    val pcPerformance: PlatformPerformance,
    val ps4Performance: PlatformPerformance?, // null si no está en PS4
    val ps5Performance: PlatformPerformance
)

data class LatestUpdateInfo(
    val version: String, // ej. "Parche 2.13", "Actualización 1.6"
    val releaseDate: String, // ej. "Septiembre 2024"
    val headline: String, // Titular de novedades
    val changesAdded: List<String>, // Lista de añadidos de la última actualización
    val performanceImpact: String // Impacto en FPS y estabilidad
)

data class Game(
    val id: String,
    val title: String,
    val tagline: String,
    val synopsis: String,
    val developer: String,
    val publisher: String,
    val releaseYear: Int,
    val genres: List<String>,
    val bannerColorHex: Long,
    val coverEmoji: String,
    
    // Hardware Requirements
    val minCpuScore: Int,
    val recCpuScore: Int,
    val minCpuText: String,
    val recCpuText: String,
    
    val minGpuScore: Int,
    val recGpuScore: Int,
    val minGpuText: String,
    val recGpuText: String,
    
    val minRamGb: Int,
    val recRamGb: Int,
    val supportedOs: List<String>,
    val storageRequiredGb: Int,
    
    // Apartados data
    val accessibility: AccessibilityFeatures,
    val contentWarnings: ContentWarnings,
    val duration: GameDuration,
    val initialRatings: CategoryRatings,
    val totalCommunityReviews: Int,
    
    // Apartado Extra: Presupuesto y Precios (Juego Base, Base + Extras, DLCs)
    val pricing: GamePricing = GamePricing(
        basePriceUsd = 59.99,
        baseEditionName = "Edición Estándar",
        baseIncludes = listOf("Juego base completo"),
        deluxePriceUsd = 79.99,
        deluxeEditionName = "Edición Deluxe",
        deluxeIncludes = listOf("Contenido digital adicional", "Pase de expansión")
    ),

    // Novedades de la última actualización
    val latestUpdate: LatestUpdateInfo = com.example.data.datasource.GamePlatformAndUpdatesData.getLatestUpdate(id),

    // Comparativa de plataformas (PC, PS4, PS5) y dónde conviene comprarlo
    val platformComparison: PlatformComparison = com.example.data.datasource.GamePlatformAndUpdatesData.getPlatformComparison(id)
) {
    val costPerHourBase: Double
        get() = if (duration.mainStoryHours > 0) pricing.basePriceUsd / duration.mainStoryHours else 0.0

    val costPerHourDeluxe: Double
        get() = if (duration.mainPlusExtraHours > 0) pricing.deluxePriceUsd / duration.mainPlusExtraHours else 0.0
}
