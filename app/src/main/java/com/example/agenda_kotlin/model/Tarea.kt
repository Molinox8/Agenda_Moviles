package com.example.agenda_kotlin.model

import java.util.*

data class Tarea(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descripcion: String,
    val fecha: Long = System.currentTimeMillis(),
    val fechaProgramada: Long? = null, // Fecha programada para realizar la tarea
    var completada: Boolean = false
)

