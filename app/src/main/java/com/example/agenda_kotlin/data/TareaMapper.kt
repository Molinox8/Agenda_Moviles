package com.example.agenda_kotlin.data

import com.example.agenda_kotlin.model.Tarea

fun TareaEntity.toTarea(): Tarea {
    return Tarea(
        id = this.id,
        titulo = this.titulo,
        descripcion = this.descripcion,
        fecha = this.fecha,
        fechaProgramada = this.fechaProgramada,
        prioridad = this.prioridad,
        completada = this.completada
    )
}

fun Tarea.toTareaEntity(): TareaEntity {
    return TareaEntity(
        id = this.id,
        titulo = this.titulo,
        descripcion = this.descripcion,
        fecha = this.fecha,
        fechaProgramada = this.fechaProgramada,
        prioridad = this.prioridad,
        completada = this.completada
    )
}

