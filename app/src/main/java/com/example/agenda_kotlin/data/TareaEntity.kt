package com.example.agenda_kotlin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.agenda_kotlin.model.Prioridad

@Entity(tableName = "tareas")
data class TareaEntity(
    @PrimaryKey
    val id: String,
    val titulo: String,
    val descripcion: String,
    val fecha: Long,
    val fechaProgramada: Long?,
    val prioridad: Prioridad,
    val completada: Boolean
)

