package com.example.myapplication.domain.repository

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.NewProjectData
import com.example.myapplication.domain.model.PhotoData
import com.example.myapplication.domain.model.Project

/** Что приложению нужно от проектов. */
interface ProjectRepository {
    suspend fun getProjects(): Resource<List<Project>>
    suspend fun createProject(data: NewProjectData, photo: PhotoData): Resource<Project>
}