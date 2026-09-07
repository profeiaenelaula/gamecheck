package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.GameCatalog
import com.example.data.local.FavoriteGameEntity
import com.example.data.local.GameCheckDatabase
import com.example.data.local.GameSubmissionEntity
import com.example.data.local.PlayerProfileEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.UserSpecsEntity
import com.example.data.model.*
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppSection(val title: String, val shortTitle: String, val iconLabel: String) {
    CAN_I_RUN_IT("¿Me corre?", "Me corre", "⚡"),
    COMPARE_GAMES("Comparador", "Comparar", "⚖️"),
    BUDGET_PRICING("Presupuesto", "Precios", "💰"),
    HOW_LONG("¿Cuánto dura?", "Duración", "⏱️"),
    ACCESSIBILITY("Accesibilidad", "Accesible", "♿"),
    IS_IT_FOR_ME("¿Es para mí?", "Para mí?", "🛡️"),
    PLAYER_PROFILE("Mi Perfil", "Perfil", "👤"),
    GAME_DETAILS("Ficha del juego", "Ficha", "🎮"),
    HELP_DIAGNOSTICS("Centro de Ayuda", "Ayuda", "🛠️"),
    HELP_US("Ayúdanos", "Ayúdanos", "🤝")
}

enum class CompatibilityFilter(val label: String) {
    ALL("Todos"),
    RUNS_GREAT("🟢 75% - 100% Óptimo"),
    RUNS_MEDIUM("🟡 50% - 74% Ajustado"),
    RUNS_NO("🔴 0% - 49% Insuficiente")
}

enum class DurationGoal(val label: String) {
    MAIN_STORY("Historia Principal"),
    MAIN_PLUS_EXTRAS("Historia + Extras"),
    COMPLETIONIST("100% Completista")
}

enum class BudgetFilter(val label: String) {
    ALL("Todos los juegos"),
    BASE_FITS("Base te alcanza (≤ Presupuesto)"),
    DELUXE_FITS("Deluxe te alcanza (≤ Presupuesto)"),
    ON_SALE("En oferta histórica"),
    BEST_VALUE("Mejor valor (< $2.0 / hora)")
}

enum class BudgetSort(val label: String) {
    PRICE_ASC("Precio Base: Menor a Mayor"),
    PRICE_DESC("Precio Base: Mayor a Menor"),
    VALUE_BEST("Mejor Relación ($/hora)"),
    DELUXE_PRICE_ASC("Precio Deluxe: Menor a Mayor")
}

data class DurationCalculationResult(
    val gameTitle: String,
    val targetHours: Float,
    val dailyHours: Float,
    val daysRequired: Int,
    val weeksRequired: Float,
    val completionDateFormatted: String
)

class GameCheckViewModel(application: Application) : AndroidViewModel(application) {

    private val db = GameCheckDatabase.getDatabase(application)
    private val dao = db.dao()

    private val _currentSection = MutableStateFlow(AppSection.CAN_I_RUN_IT)
    val currentSection: StateFlow<AppSection> = _currentSection.asStateFlow()

    private val _selectedGameId = MutableStateFlow("cyberpunk_2077")
    val selectedGameId: StateFlow<String> = _selectedGameId.asStateFlow()

    // "¿Me corre?" state
    private val _userSpecs = MutableStateFlow(
        HardwareSpecs(
            cpuName = "Intel Core i5-10400 / AMD Ryzen 5 3600 (Gama Media)",
            cpuScore = 3,
            gpuName = "NVIDIA GTX 1660 Super / AMD RX 5600 XT (Gama Media)",
            gpuScore = 3,
            ramGb = 16,
            os = "Windows 11 (64-bit)",
            storageGb = 500
        )
    )
    val userSpecs: StateFlow<HardwareSpecs> = _userSpecs.asStateFlow()

    private val _compatFilter = MutableStateFlow(CompatibilityFilter.ALL)
    val compatFilter: StateFlow<CompatibilityFilter> = _compatFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // "¿Cuánto dura?" state
    private val _dailyHours = MutableStateFlow(2.0f)
    val dailyHours: StateFlow<Float> = _dailyHours.asStateFlow()

    private val _durationGoal = MutableStateFlow(DurationGoal.MAIN_STORY)
    val durationGoal: StateFlow<DurationGoal> = _durationGoal.asStateFlow()

    // Accesibilidad filter state
    private val _filterSubtitles = MutableStateFlow(false)
    val filterSubtitles: StateFlow<Boolean> = _filterSubtitles.asStateFlow()

    private val _filterControlsRemap = MutableStateFlow(false)
    val filterControlsRemap: StateFlow<Boolean> = _filterControlsRemap.asStateFlow()

    private val _filterVisualAssist = MutableStateFlow(false)
    val filterVisualAssist: StateFlow<Boolean> = _filterVisualAssist.asStateFlow()

    private val _filterColorblind = MutableStateFlow(false)
    val filterColorblind: StateFlow<Boolean> = _filterColorblind.asStateFlow()

    // "¿Es para mí?" filter state
    private val _avoidExtremeGore = MutableStateFlow(false)
    val avoidExtremeGore: StateFlow<Boolean> = _avoidExtremeGore.asStateFlow()

    private val _avoidHorror = MutableStateFlow(false)
    val avoidHorror: StateFlow<Boolean> = _avoidHorror.asStateFlow()

    private val _avoidStrongLanguage = MutableStateFlow(false)
    val avoidStrongLanguage: StateFlow<Boolean> = _avoidStrongLanguage.asStateFlow()

    // Apartado Extra: Presupuesto y Precios state
    private val _userBudget = MutableStateFlow(70.0)
    val userBudget: StateFlow<Double> = _userBudget.asStateFlow()

    private val _budgetFilter = MutableStateFlow(BudgetFilter.ALL)
    val budgetFilter: StateFlow<BudgetFilter> = _budgetFilter.asStateFlow()

    private val _budgetSort = MutableStateFlow(BudgetSort.PRICE_ASC)
    val budgetSort: StateFlow<BudgetSort> = _budgetSort.asStateFlow()

    // Cart items: pair of gameId to isDeluxe
    private val _cartItems = MutableStateFlow<Set<Pair<String, Boolean>>>(emptySet())
    val cartItems: StateFlow<Set<Pair<String, Boolean>>> = _cartItems.asStateFlow()

    // Player Profile & Persistent Preferences
    private val _playerProfile = MutableStateFlow(PlayerProfileEntity())
    val playerProfile: StateFlow<PlayerProfileEntity> = _playerProfile.asStateFlow()

    // Theme personalization (Dark, Light, High Contrast)
    private val _themeMode = MutableStateFlow(ThemeMode.DARK)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    // Game Comparison (Juego A vs Juego B)
    private val _compareGameAId = MutableStateFlow("cyberpunk_2077")
    val compareGameAId: StateFlow<String> = _compareGameAId.asStateFlow()

    private val _compareGameBId = MutableStateFlow("witcher_3")
    val compareGameBId: StateFlow<String> = _compareGameBId.asStateFlow()

    // Reviews from Room
    val userReviews: StateFlow<List<ReviewEntity>> = _selectedGameId.flatMapLatest { gameId ->
        dao.getReviewsForGame(gameId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Community Submissions for moderation ("Ayúdanos")
    val gameSubmissions: StateFlow<List<GameSubmissionEntity>> = dao.getSubmissions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Seed initial submissions if empty so moderation queue has community proposals
        viewModelScope.launch {
            dao.getSubmissions().first().let { list ->
                if (list.isEmpty()) {
                    dao.insertSubmission(
                        GameSubmissionEntity(
                            title = "Black Myth: Wukong",
                            emoji = "🐒",
                            developer = "Game Science",
                            genres = "Action RPG, Souls-like, Mitología",
                            releaseYear = 2024,
                            synopsis = "Basado en Peregrinación al Oeste, controla al Elegido dominando el bastón y transformaciones místicas.",
                            minCpuScore = 3,
                            minCpuText = "Intel Core i5-8400 / AMD Ryzen 5 1600",
                            recCpuScore = 4,
                            recCpuText = "Intel Core i7-9700 / AMD Ryzen 5 5500",
                            minGpuScore = 3,
                            minGpuText = "GTX 1060 (6 GB) / RX 580 (8 GB)",
                            recGpuScore = 4,
                            recGpuText = "RTX 2060 / RX 5700 XT",
                            minRamGb = 16,
                            recRamGb = 16,
                            storageGb = 130,
                            platformPerformanceNotes = "PS5 a 60 FPS estables en Rendimiento; en PC requiere DLSS/FSR en UE5.",
                            violenceScore = 4,
                            bloodScore = 3,
                            horrorScore = 2,
                            languageScore = 1,
                            ageRating = "PEGI 16",
                            sensitiveThemes = "Criaturas míticas, combates marciales intensos",
                            basePriceUsd = 59.99,
                            baseEditionName = "Edición Estándar",
                            deluxePriceUsd = 69.99,
                            deluxeEditionName = "Edición Digital Deluxe",
                            durationHours = 35.0f,
                            status = "En cola de revisión"
                        )
                    )
                    dao.insertSubmission(
                        GameSubmissionEntity(
                            title = "Helldivers 2",
                            emoji = "🪖",
                            developer = "Arrowhead Game Studios",
                            genres = "Shooter Cooperativo, Acción Táctica",
                            releaseYear = 2024,
                            synopsis = "Lucha por la Supertierra y la Democracia Gestionada en un shooter cooperativo galáctico.",
                            minCpuScore = 3,
                            minCpuText = "Intel Core i7-4790K / AMD Ryzen 5 1500X",
                            recCpuScore = 4,
                            recCpuText = "Intel Core i7-9700K / AMD Ryzen 7 3700X",
                            minGpuScore = 3,
                            minGpuText = "GTX 1050 Ti / RX 470",
                            recGpuScore = 4,
                            recGpuText = "RTX 2060 / RX 6600 XT",
                            minRamGb = 8,
                            recRamGb = 16,
                            storageGb = 100,
                            platformPerformanceNotes = "60 FPS en PS5; en PC demanda bastante CPU en dificultades 7 a 9.",
                            violenceScore = 4,
                            bloodScore = 4,
                            horrorScore = 2,
                            languageScore = 2,
                            ageRating = "PEGI 18",
                            sensitiveThemes = "Fuego amigo cómico, explosiones militares y bichos alienígenas",
                            basePriceUsd = 39.99,
                            baseEditionName = "Edición Estándar",
                            deluxePriceUsd = 59.99,
                            deluxeEditionName = "Edición Super Ciudadano",
                            durationHours = 45.0f,
                            status = "Verificando especificaciones técnicas"
                        )
                    )
                }
            }
        }

        // Load persisted player profile and preferences
        viewModelScope.launch {
            dao.getPlayerProfile().collect { profile ->
                if (profile != null) {
                    _playerProfile.value = profile
                    _userSpecs.value = HardwareSpecs(
                        cpuName = profile.cpuName,
                        cpuScore = profile.cpuScore,
                        gpuName = profile.gpuName,
                        gpuScore = profile.gpuScore,
                        ramGb = profile.ramGb,
                        os = profile.os,
                        storageGb = profile.storageGb
                    )
                    _dailyHours.value = profile.dailyHoursAvailable
                    _userBudget.value = profile.defaultBudgetUsd
                    _filterSubtitles.value = profile.requireSubtitles
                    _filterColorblind.value = profile.requireColorblindFilter
                    _filterControlsRemap.value = profile.requireRemapping
                    _avoidExtremeGore.value = profile.avoidExplicitBloodGore
                    _avoidHorror.value = profile.avoidPsychologicalHorror
                    _avoidStrongLanguage.value = profile.avoidStrongLanguage
                    val loadedTheme = try {
                        ThemeMode.valueOf(profile.themeMode)
                    } catch (e: Exception) {
                        ThemeMode.DARK
                    }
                    _themeMode.value = loadedTheme
                } else {
                    // Seed initial default profile in Room
                    val defaultProfile = PlayerProfileEntity()
                    dao.savePlayerProfile(defaultProfile)
                }
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        val updated = _playerProfile.value.copy(themeMode = mode.name)
        _playerProfile.value = updated
        viewModelScope.launch {
            dao.savePlayerProfile(updated)
        }
    }

    fun setCompareGameA(gameId: String) {
        _compareGameAId.value = gameId
    }

    fun setCompareGameB(gameId: String) {
        _compareGameBId.value = gameId
    }

    fun swapCompareGames() {
        val a = _compareGameAId.value
        val b = _compareGameBId.value
        _compareGameAId.value = b
        _compareGameBId.value = a
    }

    fun saveFullProfile(updatedProfile: PlayerProfileEntity) {
        _playerProfile.value = updatedProfile
        _userSpecs.value = HardwareSpecs(
            cpuName = updatedProfile.cpuName,
            cpuScore = updatedProfile.cpuScore,
            gpuName = updatedProfile.gpuName,
            gpuScore = updatedProfile.gpuScore,
            ramGb = updatedProfile.ramGb,
            os = updatedProfile.os,
            storageGb = updatedProfile.storageGb
        )
        _dailyHours.value = updatedProfile.dailyHoursAvailable
        _userBudget.value = updatedProfile.defaultBudgetUsd
        _filterSubtitles.value = updatedProfile.requireSubtitles
        _filterColorblind.value = updatedProfile.requireColorblindFilter
        _filterControlsRemap.value = updatedProfile.requireRemapping
        _avoidExtremeGore.value = updatedProfile.avoidExplicitBloodGore
        _avoidHorror.value = updatedProfile.avoidPsychologicalHorror
        _avoidStrongLanguage.value = updatedProfile.avoidStrongLanguage
        val parsedTheme = try {
            ThemeMode.valueOf(updatedProfile.themeMode)
        } catch (e: Exception) {
            ThemeMode.DARK
        }
        _themeMode.value = parsedTheme

        viewModelScope.launch {
            dao.savePlayerProfile(updatedProfile)
            dao.saveUserSpecs(
                UserSpecsEntity(
                    id = 1,
                    cpuName = updatedProfile.cpuName,
                    cpuScore = updatedProfile.cpuScore,
                    gpuName = updatedProfile.gpuName,
                    gpuScore = updatedProfile.gpuScore,
                    ramGb = updatedProfile.ramGb,
                    os = updatedProfile.os,
                    storageGb = updatedProfile.storageGb
                )
            )
        }
    }

    fun setSection(section: AppSection) {
        _currentSection.value = section
    }

    fun selectGame(gameId: String, targetSection: AppSection? = null) {
        _selectedGameId.value = gameId
        if (targetSection != null) {
            _currentSection.value = targetSection
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCompatFilter(filter: CompatibilityFilter) {
        _compatFilter.value = filter
    }

    fun updateCpu(name: String, score: Int) {
        _userSpecs.value = _userSpecs.value.copy(cpuName = name, cpuScore = score)
        persistUserSpecs()
    }

    fun updateGpu(name: String, score: Int) {
        _userSpecs.value = _userSpecs.value.copy(gpuName = name, gpuScore = score)
        persistUserSpecs()
    }

    fun updateRam(ram: Int) {
        _userSpecs.value = _userSpecs.value.copy(ramGb = ram)
        persistUserSpecs()
    }

    fun updateOs(os: String) {
        _userSpecs.value = _userSpecs.value.copy(os = os)
        persistUserSpecs()
    }

    fun updateStorage(storageGb: Int) {
        _userSpecs.value = _userSpecs.value.copy(storageGb = storageGb)
        persistUserSpecs()
    }

    fun applyPreset(presetType: String) {
        when (presetType) {
            "entry" -> {
                _userSpecs.value = HardwareSpecs(
                    cpuName = GameCatalog.cpuOptions[1].name,
                    cpuScore = 2,
                    gpuName = GameCatalog.gpuOptions[1].name,
                    gpuScore = 2,
                    ramGb = 8,
                    os = "Windows 10 (64-bit)",
                    storageGb = 250
                )
            }
            "medium" -> {
                _userSpecs.value = HardwareSpecs(
                    cpuName = GameCatalog.cpuOptions[2].name,
                    cpuScore = 3,
                    gpuName = GameCatalog.gpuOptions[2].name,
                    gpuScore = 3,
                    ramGb = 16,
                    os = "Windows 11 (64-bit)",
                    storageGb = 500
                )
            }
            "high" -> {
                _userSpecs.value = HardwareSpecs(
                    cpuName = GameCatalog.cpuOptions[3].name,
                    cpuScore = 4,
                    gpuName = GameCatalog.gpuOptions[3].name,
                    gpuScore = 4,
                    ramGb = 32,
                    os = "Windows 11 (64-bit)",
                    storageGb = 1000
                )
            }
        }
        persistUserSpecs()
    }

    private fun persistUserSpecs() {
        viewModelScope.launch {
            val s = _userSpecs.value
            dao.saveUserSpecs(
                UserSpecsEntity(
                    id = 1,
                    cpuName = s.cpuName,
                    cpuScore = s.cpuScore,
                    gpuName = s.gpuName,
                    gpuScore = s.gpuScore,
                    ramGb = s.ramGb,
                    os = s.os,
                    storageGb = s.storageGb
                )
            )
        }
    }

    // Accessibility filter toggles
    fun toggleFilterSubtitles() { _filterSubtitles.value = !_filterSubtitles.value }
    fun toggleFilterControlsRemap() { _filterControlsRemap.value = !_filterControlsRemap.value }
    fun toggleFilterVisualAssist() { _filterVisualAssist.value = !_filterVisualAssist.value }
    fun toggleFilterColorblind() { _filterColorblind.value = !_filterColorblind.value }

    // Content warning toggles
    fun toggleAvoidExtremeGore() { _avoidExtremeGore.value = !_avoidExtremeGore.value }
    fun toggleAvoidHorror() { _avoidHorror.value = !_avoidHorror.value }
    fun toggleAvoidStrongLanguage() { _avoidStrongLanguage.value = !_avoidStrongLanguage.value }

    // Duration calculator
    fun setDailyHours(hours: Float) {
        _dailyHours.value = hours.coerceIn(0.5f, 16.0f)
    }

    fun setDurationGoal(goal: DurationGoal) {
        _durationGoal.value = goal
    }

    fun calculateDurationForGame(game: Game, dailyHours: Float, goal: DurationGoal): DurationCalculationResult {
        val targetHours = when (goal) {
            DurationGoal.MAIN_STORY -> game.duration.mainStoryHours
            DurationGoal.MAIN_PLUS_EXTRAS -> game.duration.mainPlusExtraHours
            DurationGoal.COMPLETIONIST -> game.duration.completionistHours
        }
        val days = Math.ceil((targetHours / dailyHours).toDouble()).toInt().coerceAtLeast(1)
        val weeks = days / 7.0f

        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.DAY_OF_YEAR, days)
        val sdf = SimpleDateFormat("d 'de' MMMM, yyyy", Locale("es", "ES"))
        val finishDate = sdf.format(calendar.time)

        return DurationCalculationResult(
            gameTitle = game.title,
            targetHours = targetHours,
            dailyHours = dailyHours,
            daysRequired = days,
            weeksRequired = weeks,
            completionDateFormatted = finishDate
        )
    }

    fun addReview(
        gameId: String,
        author: String,
        comment: String,
        historia: Float,
        jugabilidad: Float,
        graficos: Float,
        estrategia: Float,
        sonido: Float,
        optimizacion: Float
    ) {
        viewModelScope.launch {
            dao.insertReview(
                ReviewEntity(
                    gameId = gameId,
                    author = author.ifBlank { "Gamer Anónimo" },
                    comment = comment,
                    historiaRating = historia,
                    jugabilidadRating = jugabilidad,
                    graficosRating = graficos,
                    estrategiaRating = estrategia,
                    sonidoRating = sonido,
                    optimizacionRating = optimizacion
                )
            )
        }
    }

    // Budget & Pricing actions
    fun setUserBudget(budget: Double) {
        _userBudget.value = budget.coerceAtLeast(0.0)
    }

    fun setBudgetFilter(filter: BudgetFilter) {
        _budgetFilter.value = filter
    }

    fun setBudgetSort(sort: BudgetSort) {
        _budgetSort.value = sort
    }

    fun toggleCartItem(gameId: String, isDeluxe: Boolean) {
        val current = _cartItems.value.toMutableSet()
        val key = Pair(gameId, isDeluxe)
        val oppositeKey = Pair(gameId, !isDeluxe)
        if (current.contains(key)) {
            current.remove(key)
        } else {
            current.remove(oppositeKey)
            current.add(key)
        }
        _cartItems.value = current
    }

    fun clearCart() {
        _cartItems.value = emptySet()
    }

    fun isItemInCart(gameId: String, isDeluxe: Boolean): Boolean {
        return _cartItems.value.contains(Pair(gameId, isDeluxe))
    }

    fun getSelectedGame(): Game {
        return GameCatalog.games.find { it.id == _selectedGameId.value } ?: GameCatalog.games.first()
    }

    fun submitGameProposal(submission: GameSubmissionEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            dao.insertSubmission(submission)
            onComplete()
        }
    }

    fun deleteGameSubmission(id: Long) {
        viewModelScope.launch {
            dao.deleteSubmission(id)
        }
    }
}
