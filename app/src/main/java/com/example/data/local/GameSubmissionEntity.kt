package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_submissions")
data class GameSubmissionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val emoji: String = "🎮",
    val developer: String = "",
    val genres: String = "",
    val releaseYear: Int = 2024,
    val synopsis: String = "",
    // Hardware Requirements
    val minCpuScore: Int = 3,
    val minCpuText: String = "",
    val recCpuScore: Int = 4,
    val recCpuText: String = "",
    val minGpuScore: Int = 3,
    val minGpuText: String = "",
    val recGpuScore: Int = 4,
    val recGpuText: String = "",
    val minRamGb: Int = 16,
    val recRamGb: Int = 16,
    val storageGb: Int = 80,
    val platformPerformanceNotes: String = "",
    // Content Warnings & Categories
    val violenceScore: Int = 3,
    val bloodScore: Int = 3,
    val horrorScore: Int = 2,
    val languageScore: Int = 2,
    val ageRating: String = "PEGI 16",
    val sensitiveThemes: String = "",
    // Pricing & Duration
    val basePriceUsd: Double = 59.99,
    val baseEditionName: String = "Edición Estándar",
    val deluxePriceUsd: Double = 79.99,
    val deluxeEditionName: String = "Edición Deluxe",
    val durationHours: Float = 30.0f,
    // Status in review queue
    val status: String = "En cola de revisión",
    val submittedAt: Long = System.currentTimeMillis()
)
