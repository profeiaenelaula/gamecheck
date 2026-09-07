package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameCheckDao {
    @Query("SELECT * FROM user_specs WHERE id = 1 LIMIT 1")
    fun getUserSpecs(): Flow<UserSpecsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserSpecs(specs: UserSpecsEntity)

    @Query("SELECT * FROM game_reviews WHERE gameId = :gameId ORDER BY createdAt DESC")
    fun getReviewsForGame(gameId: String): Flow<List<ReviewEntity>>

    @Insert
    suspend fun insertReview(review: ReviewEntity)

    @Query("SELECT * FROM favorite_games")
    fun getFavorites(): Flow<List<FavoriteGameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(fav: FavoriteGameEntity)

    @Query("DELETE FROM favorite_games WHERE gameId = :gameId")
    suspend fun removeFavorite(gameId: String)

    @Query("SELECT * FROM player_profile WHERE id = 1 LIMIT 1")
    fun getPlayerProfile(): Flow<PlayerProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayerProfile(profile: PlayerProfileEntity)

    @Query("SELECT * FROM game_submissions ORDER BY submittedAt DESC")
    fun getSubmissions(): Flow<List<GameSubmissionEntity>>

    @Insert
    suspend fun insertSubmission(submission: GameSubmissionEntity)

    @Query("DELETE FROM game_submissions WHERE id = :id")
    suspend fun deleteSubmission(id: Long)
}
