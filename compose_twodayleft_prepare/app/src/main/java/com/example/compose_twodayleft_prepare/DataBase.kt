package com.example.compose_twodayleft_prepare

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

data class MusicApi(
    val id: Int,
    val name: String,
    val description: String,
    val tags: List<String>,
    val audio: String,
    val cover: String
)

@Entity(tableName = "music_list")
data class MusicSchema(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
)

@Dao
interface MusicDao {
    @Insert
    suspend fun add(data: MusicSchema)

    @Query("SELECT * FROM music_list")
    fun getAll(): Flow<List<MusicSchema>>
}

@Database(entities = [MusicSchema::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun MusicDao(): MusicDao
}

fun getDataBase(context: Context): DataBase {
    return Room.databaseBuilder(
        context.applicationContext, DataBase::class.java, "db"
    ).fallbackToDestructiveMigration().build()
}

class MusicListData(private val db: DataBase) : ViewModel() {
    val data: Flow<List<MusicSchema>> = db.MusicDao().getAll()
}
