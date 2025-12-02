package com.example.agenda_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agenda_kotlin.data.TareaDatabase
import com.example.agenda_kotlin.repository.TareaRepository
import com.example.agenda_kotlin.ui.AgendaScreen
import com.example.agenda_kotlin.viewmodel.TareaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar base de datos y repositorio
        val database = TareaDatabase.getDatabase(applicationContext)
        val repository = TareaRepository(database.tareaDao())
        
        setContent {
            val viewModel: TareaViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return TareaViewModel(repository) as T
                    }
                }
            )
            AgendaScreen(viewModel = viewModel)
        }
    }
}