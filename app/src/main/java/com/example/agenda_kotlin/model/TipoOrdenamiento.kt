package com.example.agenda_kotlin.model

enum class TipoOrdenamiento(val displayName: String) {
    PRIORIDAD_ALTA_PRIMERO("Prioridad (Alta primero)"),
    PRIORIDAD_BAJA_PRIMERO("Prioridad (Baja primero)"),
    FECHA_RECIENTE_PRIMERO("Fecha (Reciente primero)"),
    FECHA_ANTIGUA_PRIMERO("Fecha (Antigua primero)"),
    FECHA_PROGRAMADA_PRIMERO("Fecha programada"),
    TITULO_AZ("Título (A-Z)"),
    TITULO_ZA("Título (Z-A)"),
    COMPLETADAS_PRIMERO("Completadas primero"),
    PENDIENTES_PRIMERO("Pendientes primero")
}

