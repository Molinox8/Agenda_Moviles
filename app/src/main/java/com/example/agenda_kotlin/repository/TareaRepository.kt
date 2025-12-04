package com.example.agenda_kotlin.repository

import com.example.agenda_kotlin.data.TareaDao
import com.example.agenda_kotlin.data.toTarea
import com.example.agenda_kotlin.data.toTareaEntity
import com.example.agenda_kotlin.model.Tarea
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TareaRepository(private val tareaDao: TareaDao) {
    
    fun obtenerTodasLasTareas(): Flow<List<Tarea>> {
        return tareaDao.obtenerTodasLasTareas().map { entities ->
            entities.map { it.toTarea() }
        }
    }
    
    suspend fun agregarTarea(tarea: Tarea) {
        tareaDao.insertarTarea(tarea.toTareaEntity())
    }
    
    suspend fun actualizarTarea(tarea: Tarea) {
        tareaDao.actualizarTarea(tarea.toTareaEntity())
    }
    
    suspend fun eliminarTarea(id: String) {
        tareaDao.eliminarTareaPorId(id)
    }
    
    suspend fun toggleCompletada(id: String) {
        val tarea = tareaDao.obtenerTareaPorId(id)
        tarea?.let {
            tareaDao.actualizarCompletada(id, !it.completada)
        }
    }
}

