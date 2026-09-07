package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_specs")
data class UserSpecsEntity(
    @PrimaryKey
    val id: Int = 1,
    val cpuName: String,
    val cpuScore: Int,
    val gpuName: String,
    val gpuScore: Int,
    val ramGb: Int,
    val os: String,
    val storageGb: Int
)

@Entity(tableName = "game_reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameId: String,
    val author: String,
    val comment: String,
    val historiaRating: Float,
    val jugabilidadRating: Float,
    val graficosRating: Float,
    val estrategiaRating: Float,
    val sonidoRating: Float,
    val optimizacionRating: Float,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_games")
data class FavoriteGameEntity(
    @PrimaryKey
    val gameId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val gamerTag: String = "Jugador 1",
    val avatarEmoji: String = "🎮",
    // Hardware Specs
    val cpuName: String = "Intel Core i5-10400 / AMD Ryzen 5 3600 (Gama Media)",
    val cpuScore: Int = 3,
    val gpuName: String = "NVIDIA GTX 1660 Super / AMD RX 5600 XT (Gama Media)",
    val gpuScore: Int = 3,
    val ramGb: Int = 16,
    val os: String = "Windows 11 (64-bit)",
    val storageGb: Int = 500,
    // "¿Cuánto dura?" preferences
    val preferredPlaystyle: String = "MAIN_STORY", // MAIN_STORY, MAIN_PLUS_EXTRA, COMPLETIONIST
    val dailyHoursAvailable: Float = 2.0f,
    // "Presupuesto" preferences
    val defaultBudgetUsd: Double = 70.0,
    // "Accesibilidad" preferences
    val requireSubtitles: Boolean = false,
    val requireColorblindFilter: Boolean = false,
    val requireAimAssist: Boolean = false,
    val requireRemapping: Boolean = false,
    val requireSpanishAudio: Boolean = false,
    // "¿Es para mí?" (Content warning filters)
    val avoidBrutalViolence: Boolean = false,
    val avoidExplicitBloodGore: Boolean = false,
    val avoidPsychologicalHorror: Boolean = false,
    val avoidStrongLanguage: Boolean = false,
    val avoidSpiders: Boolean = false,
    // Theme personalization: "DARK", "LIGHT", "HIGH_CONTRAST"
    val themeMode: String = "DARK"
)
