package com.example.agenda_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agenda_kotlin.ui.AgendaScreen
import com.example.agenda_kotlin.viewmodel.TareaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: TareaViewModel = viewModel()
            AgendaScreen(viewModel = viewModel)
        }
    }
}