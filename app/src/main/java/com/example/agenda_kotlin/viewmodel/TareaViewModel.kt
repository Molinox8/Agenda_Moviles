package com.example.agenda_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agenda_kotlin.model.Prioridad
import com.example.agenda_kotlin.model.Tarea
import com.example.agenda_kotlin.model.TipoOrdenamiento
import com.example.agenda_kotlin.repository.TareaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TareaUiState(
    val tareas: List<Tarea> = emptyList(),
    val mostrarDialog: Boolean = false,
    val tareaEditando: Tarea? = null, // Tarea que se está editando
    val tipoOrdenamiento: TipoOrdenamiento = TipoOrdenamiento.PRIORIDAD_ALTA_PRIMERO
)

class TareaViewModel(
    private val repository: TareaRepository
) : ViewModel() {
    
    private val _tipoOrdenamiento = MutableStateFlow(TipoOrdenamiento.PRIORIDAD_ALTA_PRIMERO)
    private val _mostrarDialog = MutableStateFlow(false)
    private val _tareaEditando = MutableStateFlow<Tarea?>(null)
    
    val tareasFlow = combine(
        repository.obtenerTodasLasTareas(),
        _tipoOrdenamiento
    ) { tareas, ordenamiento ->
        ordenarTareas(tareas, ordenamiento)
    }
    
    val uiState: StateFlow<TareaUiState> = combine(
        tareasFlow,
        _mostrarDialog,
        _tareaEditando,
        _tipoOrdenamiento
    ) { tareas, mostrarDialog, tareaEditando, tipoOrdenamiento ->
        TareaUiState(
            tareas = tareas,
            mostrarDialog = mostrarDialog,
            tareaEditando = tareaEditando,
            tipoOrdenamiento = tipoOrdenamiento
        )
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = TareaUiState()
    )
    
    fun cambiarOrdenamiento(tipoOrdenamiento: TipoOrdenamiento) {
        _tipoOrdenamiento.value = tipoOrdenamiento
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
            }
        }
    }
    
    fun toggleCompletada(id: String) {
        viewModelScope.launch {
            repository.toggleCompletada(id)
        }
    }
    
    fun eliminarTarea(id: String) {
        viewModelScope.launch {
            repository.eliminarTarea(id)
        }
    }
    
    fun mostrarDialog() {
        _mostrarDialog.value = true
    }
    
    fun ocultarDialog() {
        _mostrarDialog.value = false
    }
    
    fun mostrarDialogEdicion(tarea: Tarea) {
        _tareaEditando.value = tarea
    }
    
    fun ocultarDialogEdicion() {
        _tareaEditando.value = null
    }
    
    fun editarTarea(
        id: String,
        titulo: String,
        descripcion: String,
        fechaProgramada: Long?,
        prioridad: Prioridad
    ) {
        if (titulo.isNotBlank()) {
            viewModelScope.launch {
                val tareaActualizada = Tarea(
                    id = id,
                    titulo = titulo,
                    descripcion = descripcion,
                    fechaProgramada = fechaProgramada,
                    prioridad = prioridad,
                    completada = _tareaEditando.value?.completada ?: false,
                    fecha = _tareaEditando.value?.fecha ?: System.currentTimeMillis()
                )
                repository.actualizarTarea(tareaActualizada)
                ocultarDialogEdicion()
            }
        }
    }
}

