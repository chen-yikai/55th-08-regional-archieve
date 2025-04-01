package com.example.prepare_room_db

import android.content.Context
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Entity(tableName = "todo")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val todo: String,
    val done: Boolean
)

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(user: Todo)

    @Query("SELECT * FROM todo")
    fun select(): Flow<List<Todo>>

    @Query("DELETE FROM todo WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("UPDATE todo SET todo = :newTodo WHERE id = :id")
    suspend fun updateName(newTodo: String, id: Int)

    @Query("UPDATE todo SET done = :done WHERE id = :id")
    suspend fun doneTodo(done: Boolean, id: Int)
}

@Database(entities = [Todo::class], version = 3, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}

fun getRoom(context: Context): AppDB {
    return Room.databaseBuilder(context.applicationContext, AppDB::class.java, "db")
        .fallbackToDestructiveMigration().build()
}

class TodoViewModel(private val db: AppDB) : ViewModel() {
    val allTodo: Flow<List<Todo>> = db.todoDao().select()

    fun insert(todo: Todo) = viewModelScope.launch {
        db.todoDao().insert(todo)
    }

    fun deleteTodo(id: Int) = viewModelScope.launch {
        db.todoDao().delete(id)
    }

    fun updateName(newName: String, id: Int) = viewModelScope.launch {
        db.todoDao().updateName(newName, id)
    }

    fun updateDone(done: Boolean, id: Int) = viewModelScope.launch {
        db.todoDao().doneTodo(done, id)
    }
}