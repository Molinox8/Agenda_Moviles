package com.example.agenda_kotlin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.agenda_kotlin.model.Prioridad
import com.example.agenda_kotlin.model.Tarea
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogEditarTarea(
    tarea: Tarea,
    onDismiss: () -> Unit,
    onGuardar: (String, String, Long?, Prioridad) -> Unit
) {
    var titulo by remember { mutableStateOf(tarea.titulo) }
    var descripcion by remember { mutableStateOf(tarea.descripcion) }
    var fechaSeleccionada by remember { mutableStateOf<Long?>(tarea.fechaProgramada) }
    var prioridadSeleccionada by remember { mutableStateOf(tarea.prioridad) }
    var mostrarDatePicker by remember { mutableStateOf(false) }
    
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    
    // Inicializar el DatePicker
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaSeleccionada ?: System.currentTimeMillis()
    )
    
    // Actualizar el estado del DatePicker cuando se muestra
    LaunchedEffect(mostrarDatePicker) {
        if (mostrarDatePicker) {
            datePickerState.selectedDateMillis = fechaSeleccionada ?: System.currentTimeMillis()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Tarea") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                
                // Selector de fecha
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { mostrarDatePicker = true },
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Fecha programada",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (fechaSeleccionada != null) {
                                    formatoFecha.format(Date(fechaSeleccionada!!))
                                } else {
                                    "Sin fecha programada"
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (fechaSeleccionada != null) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Seleccionar fecha",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                // Botón para limpiar fecha
                if (fechaSeleccionada != null) {
                    TextButton(
                        onClick = { fechaSeleccionada = null },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Quitar fecha")
                    }
                }
                
                // Selector de prioridad
                Column {
                    Text(
                        text = "Prioridad",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PrioridadChip(
                            prioridad = Prioridad.BAJA,
                            seleccionada = prioridadSeleccionada == Prioridad.BAJA,
                            onClick = { prioridadSeleccionada = Prioridad.BAJA }
                        )
                        PrioridadChip(
                            prioridad = Prioridad.MEDIA,
                            seleccionada = prioridadSeleccionada == Prioridad.MEDIA,
                            onClick = { prioridadSeleccionada = Prioridad.MEDIA }
                        )
                        PrioridadChip(
                            prioridad = Prioridad.ALTA,
                            seleccionada = prioridadSeleccionada == Prioridad.ALTA,
                            onClick = { prioridadSeleccionada = Prioridad.ALTA }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titulo.isNotBlank()) {
                        onGuardar(titulo, descripcion, fechaSeleccionada, prioridadSeleccionada)
                    }
                },
                enabled = titulo.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
    
    // Mostrar DatePicker fuera del AlertDialog para evitar conflictos
    if (mostrarDatePicker) {
        CustomDatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            onDateSelected = { fechaMillis ->
                fechaSeleccionada = fechaMillis
                mostrarDatePicker = false
            },
            datePickerState = datePickerState
        )
    }
}

