package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.AppContainer

/** Класс приложения: держит контейнер зависимостей. */
class MatuleApp : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}