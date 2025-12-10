package com.example.agenda_kotlin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.agenda_kotlin.model.TipoOrdenamiento
import com.example.agenda_kotlin.ui.SearchBar
import com.example.agenda_kotlin.ui.ThemeOption
import com.example.agenda_kotlin.viewmodel.TareaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    viewModel: TareaViewModel,
    themeOption: ThemeOption,
    onThemeChange: (ThemeOption) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var mostrarMenuOrdenamiento by remember { mutableStateOf(false) }
    var mostrarMenuTema by remember { mutableStateOf(false) }
    var mostrarCalendario by remember { mutableStateOf(false) }

    // Si se muestra el calendario, mostrar esa pantalla
    if (mostrarCalendario) {
        CalendarioScreen(
            viewModel = viewModel,
            onBack = { mostrarCalendario = false }
        )
        return
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Mi Agenda",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    actions = {
                        // Botón para tema de colores
                        Box {
                            IconButton(onClick = {
                                mostrarMenuTema = true
                                mostrarMenuOrdenamiento = false
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Palette,
                                    contentDescription = "Cambiar tema"
                                )
                            }
                            DropdownMenu(
                                expanded = mostrarMenuTema,
                                onDismissRequest = { mostrarMenuTema = false }
                            ) {
                                ThemeOption.entries.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.displayName) },
                                        onClick = {
                                            onThemeChange(option)
                                            mostrarMenuTema = false
                                        },
                                        leadingIcon = {
                                            if (themeOption == option) {
                                                Icon(
                                                    imageVector = Icons.Default.Palette,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        // Botón para ver calendario
                        IconButton(onClick = { mostrarCalendario = true }) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Ver calendario"
                            )
                        }
                        
                        // Botón de ordenamiento
                        Box {
                            IconButton(onClick = { mostrarMenuOrdenamiento = true }) {
                                Icon(
                                    imageVector = Icons.Default.Sort,
                                    contentDescription = "Ordenar tareas"
                                )
                            }
                            DropdownMenu(
                                expanded = mostrarMenuOrdenamiento,
                                onDismissRequest = { mostrarMenuOrdenamiento = false }
                            ) {
                                TipoOrdenamiento.values().forEach { tipo ->
                                    DropdownMenuItem(
                                        text = { Text(tipo.displayName) },
                                        onClick = {
                                            viewModel.cambiarOrdenamiento(tipo)
                                            mostrarMenuOrdenamiento = false
                                        },
                                        leadingIcon = {
                                            if (uiState.tipoOrdenamiento == tipo) {
                                                Icon(
                                                    imageVector = Icons.Default.Sort,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.mostrarDialog() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, "Agregar tarea")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Campo de búsqueda
                SearchBar(
                    query = uiState.textoBusqueda,
                    onQueryChange = { viewModel.cambiarTextoBusqueda(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                
                if (uiState.tareas.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (uiState.textoBusqueda.isNotEmpty()) {
                                "No se encontraron tareas con \"${uiState.textoBusqueda}\""
                            } else {
                                "No hay tareas. ¡Agrega una nueva!"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Indicador de ordenamiento actual
                        if (uiState.tareas.isNotEmpty()) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    text = "Ordenado por: ${uiState.tipoOrdenamiento.displayName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.tareas, key = { it.id }) { tarea ->
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
                    }
                }
            }

            if (uiState.mostrarDialog) {
                DialogAgregarTarea(
                    onDismiss = { viewModel.ocultarDialog() },
                    onAgregar = { titulo, descripcion, fechaProgramada, prioridad ->
                        viewModel.agregarTarea(titulo, descripcion, fechaProgramada, prioridad)
                        viewModel.ocultarDialog()
                    }
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

