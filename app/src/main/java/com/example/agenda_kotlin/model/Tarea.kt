package com.example.agenda_kotlin.model

import java.util.*

enum class Prioridad {
    BAJA,
    MEDIA,
    ALTA
}

data class Tarea(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descripcion: String,
    val fecha: Long = System.currentTimeMillis(),
    val fechaProgramada: Long? = null, // Fecha programada para realizar la tarea
    val prioridad: Prioridad = Prioridad.MEDIA,
    var completada: Boolean = false
)

