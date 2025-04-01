package com.example.skills53dic.db

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.launch

@Entity(tableName = "tickets")
data class TicketsSchema(
    @PrimaryKey val id: String,
    val title: String = "2022第41屆新一代設計展",
    val type: String,
    val name: String,
    val email: String,
    val phone: String,
    val date: String,
    val price: String
)

@Entity(tableName = "users")
data class UsersSchema(
    @PrimaryKey val email: String, val password: String
)

@Dao
interface TicketsDao {
    @Insert
    suspend fun insert(tickets: TicketsSchema)

    @Query("SELECT * FROM tickets")
    suspend fun getAll(): List<TicketsSchema>

    @Query("SELECT COUNT(*) FROM tickets WHERE id = :id")
    suspend fun checkSameId(id: String): Int
}

@Dao
interface UsersDao {
    @Insert
    suspend fun insert(users: UsersSchema)

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UsersSchema>

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun checkSameEmail(email: String): Int

    @Query("SELECT COUNT(*) FROM users WHERE email = :email AND password = :password")
    suspend fun auth(email: String, password: String): Int
}

@Database(entities = [TicketsSchema::class, UsersSchema::class], version = 3)
abstract class DataBase : RoomDatabase() {
    abstract fun TicketsDao(): TicketsDao
    abstract fun UsersDao(): UsersDao
}

fun getDataBase(context: Context): DataBase {
    return Room.databaseBuilder(
        context.applicationContext, DataBase::class.java, "db"
    )
        .fallbackToDestructiveMigration()
        .build()
}

class UsersViewModel(private val db: DataBase) : ViewModel() {
    val users = mutableStateListOf<UsersSchema>()

    init {
        update()
    }

    fun update() {
        viewModelScope.launch {
            users.clear()
            users.addAll(db.UsersDao().getAll())
        }
    }

    fun add(data: UsersSchema) {
        viewModelScope.launch {
            db.UsersDao().insert(data)
        }
    }

    suspend fun checkSameEmail(email: String): Boolean {
        return db.UsersDao().checkSameEmail(email) > 0
    }

    suspend fun authSignIn(email: String, password: String): Boolean {
        return db.UsersDao().auth(email, password) > 0
    }

}

class TicketsViewModel(private val db: DataBase) : ViewModel() {
    val tickets = mutableStateListOf<TicketsSchema>()

    init {
        update()
    }

    fun add(data: TicketsSchema) {
        viewModelScope.launch {
            db.TicketsDao().insert(data)
            update()
        }
    }

    suspend fun checkSameId(id: String): Boolean {
        return db.TicketsDao().checkSameId(id) > 0
    }

    fun update() {
        viewModelScope.launch {
            tickets.clear()
            tickets.addAll(db.TicketsDao().getAll())
        }
    }
}