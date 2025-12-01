package com.example.agenda_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agenda_kotlin.model.Prioridad
import com.example.agenda_kotlin.model.Tarea
import com.example.agenda_kotlin.repository.TareaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TareaUiState(
    val tareas: List<Tarea> = emptyList(),
    val mostrarDialog: Boolean = false
)

class TareaViewModel(
    private val repository: TareaRepository = TareaRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TareaUiState())
    val uiState: StateFlow<TareaUiState> = _uiState.asStateFlow()
    
    init {
        cargarTareas()
    }
    
    private fun cargarTareas() {
        viewModelScope.launch {
            val tareas = repository.obtenerTodasLasTareas()
            _uiState.value = _uiState.value.copy(tareas = tareas)
        }
    }
    
    fun agregarTarea(titulo: String, descripcion: String, fechaProgramada: Long? = null, prioridad: Prioridad = Prioridad.MEDIA) {
        if (titulo.isNotBlank()) {
            viewModelScope.launch {
                val nuevaTarea = Tarea(
                    titulo = titulo,
                    descripcion = descripcion,
                    fechaProgramada = fechaProgramada,
                    prioridad = prioridad
                )
                repository.agregarTarea(nuevaTarea)
                cargarTareas()
            }
        }
    }
    
    fun toggleCompletada(id: String) {
        viewModelScope.launch {
            repository.toggleCompletada(id)
            cargarTareas()
        }
    }
    
    fun eliminarTarea(id: String) {
        viewModelScope.launch {
            repository.eliminarTarea(id)
            cargarTareas()
        }
    }
    
    fun mostrarDialog() {
        _uiState.value = _uiState.value.copy(mostrarDialog = true)
    }
    
    fun ocultarDialog() {
        _uiState.value = _uiState.value.copy(mostrarDialog = false)
    }
}

