package com.tasheel.finance.data.db

import androidx.room.*

@Entity(tableName = "application_drafts")
data class ApplicationDraftEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val fullName: String = "",
    val nationalId: String = "",
    val employer: String = "",
    val monthlySalary: Double = 0.0,
    val requestedAmount: Double = 0.0,
    val requestedTenure: Int = 0,
    val currentStep: Int = 0,
    val updatedAt: Long = System.currentTimeMillis(),
)

@Dao
interface ApplicationDraftDao {
    @Query("SELECT * FROM application_drafts ORDER BY updatedAt DESC")
    suspend fun getAll(): List<ApplicationDraftEntity>

    @Query("SELECT * FROM application_drafts WHERE id = :id")
    suspend fun getById(id: Long): ApplicationDraftEntity?

    @Upsert
    suspend fun upsert(draft: ApplicationDraftEntity): Long

    @Query("DELETE FROM application_drafts WHERE id = :id")
    suspend fun delete(id: Long)
}

@Database(entities = [ApplicationDraftEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun draftDao(): ApplicationDraftDao
}
