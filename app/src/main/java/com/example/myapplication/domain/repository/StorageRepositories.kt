package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.HomeData
import com.example.myapplication.domain.model.RegistrationDraft

/** Хранилище кэша главной страницы. */
interface HomeCache {
    fun save(data: HomeData)
    fun load(): HomeData?
}

/** Хранилище последнего экрана: вкладка, открытая надстройка (корзина, документ), экран входа/регистрации. */
interface LastScreenRepository {
    var route: String?
    var tab: Int
    var overlay: String
    fun clear()
}

/** Хранилище черновика регистрации. */
interface RegistrationDraftRepository {
    fun load(): RegistrationDraft
    fun save(draft: RegistrationDraft)
    fun clear()
}