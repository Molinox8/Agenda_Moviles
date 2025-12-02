package com.example.agenda_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agenda_kotlin.model.Prioridad
import com.example.agenda_kotlin.model.Tarea
import com.example.agenda_kotlin.model.TipoOrdenamiento
import com.example.agenda_kotlin.repository.TareaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TareaUiState(
    val tareas: List<Tarea> = emptyList(),
    val mostrarDialog: Boolean = false,
    val tipoOrdenamiento: TipoOrdenamiento = TipoOrdenamiento.PRIORIDAD_ALTA_PRIMERO
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
            val tareasOrdenadas = ordenarTareas(tareas, _uiState.value.tipoOrdenamiento)
            _uiState.value = _uiState.value.copy(tareas = tareasOrdenadas)
        }
    }
    
    fun cambiarOrdenamiento(tipoOrdenamiento: TipoOrdenamiento) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(tipoOrdenamiento = tipoOrdenamiento)
            val tareas = repository.obtenerTodasLasTareas()
            val tareasOrdenadas = ordenarTareas(tareas, tipoOrdenamiento)
            _uiState.value = _uiState.value.copy(tareas = tareasOrdenadas)
        }
    }
    
    private fun ordenarTareas(tareas: List<Tarea>, tipoOrdenamiento: TipoOrdenamiento): List<Tarea> {
        return when (tipoOrdenamiento) {
            TipoOrdenamiento.PRIORIDAD_ALTA_PRIMERO -> {
                tareas.sortedWith(
                    compareByDescending<Tarea> { it.prioridad.ordinal }
                        .thenByDescending { it.fechaProgramada ?: Long.MAX_VALUE }
                )
            }
            TipoOrdenamiento.PRIORIDAD_BAJA_PRIMERO -> {
                tareas.sortedWith(
                    compareBy<Tarea> { it.prioridad.ordinal }
                        .thenBy { it.fechaProgramada ?: Long.MAX_VALUE }
                )
            }
            TipoOrdenamiento.FECHA_RECIENTE_PRIMERO -> {
                tareas.sortedByDescending { it.fecha }
            }
            TipoOrdenamiento.FECHA_ANTIGUA_PRIMERO -> {
                tareas.sortedBy { it.fecha }
            }
            TipoOrdenamiento.FECHA_PROGRAMADA_PRIMERO -> {
                tareas.sortedWith(
                    compareBy<Tarea> { it.fechaProgramada ?: Long.MAX_VALUE }
                        .thenByDescending { it.prioridad.ordinal }
                )
            }
            TipoOrdenamiento.TITULO_AZ -> {
                tareas.sortedBy { it.titulo.lowercase() }
            }
            TipoOrdenamiento.TITULO_ZA -> {
                tareas.sortedByDescending { it.titulo.lowercase() }
            }
            TipoOrdenamiento.COMPLETADAS_PRIMERO -> {
                tareas.sortedByDescending { it.completada }
            }
            TipoOrdenamiento.PENDIENTES_PRIMERO -> {
                tareas.sortedBy { it.completada }
            }
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

