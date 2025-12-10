package com.example.agenda_kotlin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.agenda_kotlin.model.Prioridad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Long?) -> Unit,
    datePickerState: DatePickerState
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun PrioridadChip(
    prioridad: Prioridad,
    seleccionada: Boolean,
    onClick: () -> Unit
) {
    val (texto, color) = when (prioridad) {
        Prioridad.BAJA -> "Baja" to Color(0xFF4CAF50) // Verde
        Prioridad.MEDIA -> "Media" to Color(0xFFFF9800) // Naranja
        Prioridad.ALTA -> "Alta" to Color(0xFFF44336) // Rojo
    }
    
    FilterChip(
        selected = seleccionada,
        onClick = onClick,
        label = { Text(texto) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color.copy(alpha = 0.2f),
            selectedLabelColor = color,
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

