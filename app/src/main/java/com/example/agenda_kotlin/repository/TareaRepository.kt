package com.example.agenda_kotlin.repository

import com.example.agenda_kotlin.model.Tarea

class TareaRepository {
    private val _tareas = mutableListOf<Tarea>()
    
    fun obtenerTodasLasTareas(): List<Tarea> {
        return _tareas.toList()
    }
    
    fun agregarTarea(tarea: Tarea) {
        _tareas.add(tarea)
    }
    
    fun actualizarTarea(tarea: Tarea) {
        val index = _tareas.indexOfFirst { it.id == tarea.id }
        if (index != -1) {
            _tareas[index] = tarea
        }
    }
    
    fun eliminarTarea(id: String) {
        _tareas.removeAll { it.id == id }
    }
    
    fun toggleCompletada(id: String) {
        val index = _tareas.indexOfFirst { it.id == id }
        if (index != -1) {
            _tareas[index] = _tareas[index].copy(completada = !_tareas[index].completada)
        }
    }
}

