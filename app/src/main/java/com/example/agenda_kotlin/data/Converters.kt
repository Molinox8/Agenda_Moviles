package com.example.agenda_kotlin.data

import androidx.room.TypeConverter
import com.example.agenda_kotlin.model.Prioridad

class Converters {
    @TypeConverter
    fun fromPrioridad(prioridad: Prioridad): String {
        return prioridad.name
    }
    
    @TypeConverter
    fun toPrioridad(prioridad: String): Prioridad {
        return Prioridad.valueOf(prioridad)
    }
}
