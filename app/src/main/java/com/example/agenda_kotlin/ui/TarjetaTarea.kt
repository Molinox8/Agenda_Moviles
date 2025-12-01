package com.example.agenda_kotlin.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agenda_kotlin.model.Prioridad
import com.example.agenda_kotlin.model.Tarea
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TarjetaTarea(
    tarea: Tarea,
    onCompletarClick: () -> Unit,
    onEliminarClick: () -> Unit
) {
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    // Obtener color de borde según prioridad
    val colorPrioridad = when (tarea.prioridad) {
        Prioridad.BAJA -> Color(0xFF4CAF50) // Verde
        Prioridad.MEDIA -> Color(0xFFFF9800) // Naranja
        Prioridad.ALTA -> Color(0xFFF44336) // Rojo
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (tarea.completada)
                Color(0xFFE8F5E9)
            else
                MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = colorPrioridad.copy(alpha = if (tarea.completada) 0.3f else 0.8f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Título con badge de prioridad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = tarea.titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (tarea.completada)
                            TextDecoration.LineThrough
                        else
                            null,
                        color = if (tarea.completada) Color.Gray else Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    // Badge de prioridad
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = colorPrioridad.copy(alpha = 0.2f),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Text(
                            text = tarea.prioridad.name,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorPrioridad,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tarea.descripcion,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textDecoration = if (tarea.completada)
                        TextDecoration.LineThrough
                    else
                        null
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Creada: ${formatoFecha.format(Date(tarea.fecha))}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (tarea.fechaProgramada != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📅 Programada: ${formatoFecha.format(Date(tarea.fechaProgramada))}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onCompletarClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (tarea.completada)
                            Color(0xFF4CAF50)
                        else
                            Color(0xFFE0E0E0)
                    )
                ) {
                    Icon(
                        Icons.Default.Check,
                        "Completar",
                        tint = if (tarea.completada) Color.White else Color.Gray
                    )
                }

                IconButton(
                    onClick = onEliminarClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFFFFEBEE)
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        "Eliminar",
                        tint = Color(0xFFD32F2F)
                    )
                }
            }
        }
    }
}

