package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.data.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "last_viewed")
data class LastViewedEntity(
    @PrimaryKey val productId: String,
    val title: String,
    val price: Double,
    val imageUrl: String,
    val viewedAt: Long = System.currentTimeMillis()
)

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE productId = :productId LIMIT 1)")
    fun isFavorite(productId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE productId = :productId")
    suspend fun deleteFavoriteById(productId: String)
}

@Dao
interface LastViewedDao {
    @Query("SELECT * FROM last_viewed ORDER BY viewedAt DESC LIMIT 10")
    fun getLastViewedProducts(): Flow<List<LastViewedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLastViewed(product: LastViewedEntity)

    @Query("DELETE FROM last_viewed WHERE productId = :productId")
    suspend fun deleteLastViewed(productId: String)
}

@Database(entities = [FavoriteEntity::class, LastViewedEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun lastViewedDao(): LastViewedDao
}
