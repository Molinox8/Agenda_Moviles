package com.example.agenda_kotlin.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    @Query("SELECT * FROM tareas ORDER BY fecha DESC")
    fun obtenerTodasLasTareas(): Flow<List<TareaEntity>>
    
    @Query("SELECT * FROM tareas WHERE id = :id")
    suspend fun obtenerTareaPorId(id: String): TareaEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTarea(tarea: TareaEntity)
    
    @Update
    suspend fun actualizarTarea(tarea: TareaEntity)
    
    @Delete
    suspend fun eliminarTarea(tarea: TareaEntity)
    
    @Query("DELETE FROM tareas WHERE id = :id")
    suspend fun eliminarTareaPorId(id: String)
    
    @Query("UPDATE tareas SET completada = :completada WHERE id = :id")
    suspend fun actualizarCompletada(id: String, completada: Boolean)
}

