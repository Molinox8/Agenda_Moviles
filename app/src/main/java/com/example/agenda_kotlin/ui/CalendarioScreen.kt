package com.example.agenda_kotlin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.agenda_kotlin.model.Tarea
import com.example.agenda_kotlin.viewmodel.TareaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarioScreen(
    viewModel: TareaViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var fechaSeleccionada by remember { mutableStateOf<Long?>(null) }
    var mostrarDatePicker by remember { mutableStateOf(false) }
    
    val formatoFecha = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val formatoFechaCompleto = remember { SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale.getDefault()) }
    
    // Agrupar tareas por fecha programada - se recalcula cuando cambian las tareas
    val tareasPorFecha = remember(uiState.tareas) {
        uiState.tareas
            .filter { it.fechaProgramada != null }
            .groupBy { tarea ->
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = tarea.fechaProgramada!!
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }
    }
    
    // Función para normalizar una fecha (poner horas, minutos, segundos y milisegundos a 0)
    fun normalizarFecha(fechaMillis: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = fechaMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    // Estado del DatePicker - se actualiza cuando se muestra o cambia la fecha seleccionada
    val fechaInicialParaPicker = remember(fechaSeleccionada) {
        fechaSeleccionada ?: System.currentTimeMillis()
    }
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaInicialParaPicker
    )
    
    // Actualizar el estado del DatePicker cuando se muestra
    LaunchedEffect(mostrarDatePicker, fechaSeleccionada) {
        if (mostrarDatePicker) {
            val fechaParaMostrar = fechaSeleccionada ?: System.currentTimeMillis()
            datePickerState.selectedDateMillis = fechaParaMostrar
        }
    }
    
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Calendario",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver a lista"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { mostrarDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Seleccionar fecha"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            // Mostrar fecha seleccionada o fecha actual
            val fechaAMostrar = fechaSeleccionada ?: System.currentTimeMillis()
            val fechaAMostrarCalendar = Calendar.getInstance().apply {
                timeInMillis = fechaAMostrar
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val fechaAMostrarMillis = fechaAMostrarCalendar.timeInMillis
            
            // Mostrar tareas de la fecha seleccionada
            val tareasDelDia = tareasPorFecha[fechaAMostrarMillis] ?: emptyList()
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Encabezado con fecha seleccionada
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = formatoFechaCompleto.format(Date(fechaAMostrarMillis)),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = formatoFecha.format(Date(fechaAMostrarMillis)),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
                
                // Tareas del día seleccionado
                if (tareasDelDia.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = Color.Gray
                                )
                                Text(
                                    "No hay tareas programadas para este día",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                } else {
                    items(tareasDelDia, key = { it.id }) { tarea ->
                        TarjetaTarea(
                            tarea = tarea,
                            onCompletarClick = {
                                viewModel.toggleCompletada(tarea.id)
                            },
                            onEditarClick = {
                                viewModel.mostrarDialogEdicion(tarea)
                            },
                            onEliminarClick = {
                                viewModel.eliminarTarea(tarea.id)
                            }
                        )
                    }
                }
                
                // Sección de todas las fechas con tareas
                if (tareasPorFecha.isNotEmpty()) {
                    item {
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                    }
                    
                    item {
                        Text(
                            text = "Todas las fechas con tareas:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    items(tareasPorFecha.keys.sorted(), key = { it }) { fechaMillis ->
                        val tareas = tareasPorFecha[fechaMillis] ?: emptyList()
                        val fechaFormateada = formatoFecha.format(Date(fechaMillis))
                        val cantidadTareas = tareas.size
                        
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { fechaSeleccionada = fechaMillis },
                            color = if (fechaMillis == fechaAMostrarMillis) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = fechaFormateada,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "$cantidadTareas tarea${if (cantidadTareas != 1) "s" else ""}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (fechaMillis == fechaAMostrarMillis) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = "Fecha seleccionada",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // DatePicker Dialog
            if (mostrarDatePicker) {
                CustomDatePickerDialog(
                    onDismissRequest = { mostrarDatePicker = false },
                    onDateSelected = { fechaMillis ->
                        // Normalizar la fecha seleccionada
                        fechaMillis?.let { fecha ->
                            fechaSeleccionada = normalizarFecha(fecha)
                        } ?: run {
                            fechaSeleccionada = null
                        }
                        mostrarDatePicker = false
                    },
                    datePickerState = datePickerState
                )
            }
            
            // Diálogo de edición
            uiState.tareaEditando?.let { tarea ->
                DialogEditarTarea(
                    tarea = tarea,
                    onDismiss = { viewModel.ocultarDialogEdicion() },
                    onGuardar = { titulo, descripcion, fechaProgramada, prioridad ->
                        viewModel.editarTarea(
                            id = tarea.id,
                            titulo = titulo,
                            descripcion = descripcion,
                            fechaProgramada = fechaProgramada,
                            prioridad = prioridad
                        )
                    }
                )
            }
        }
    }
}

