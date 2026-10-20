package com.example.network.service

import com.example.network.common.ApiResult
import com.example.network.model.NewProject
import com.example.network.model.ProjectDto

/** Проекты пользователя. */
interface ProjectService {
    /** Список проектов текущего пользователя, новые первыми. */
    suspend fun getProjects(): ApiResult<List<ProjectDto>>

    /** Создаёт проект вместе с фото. */
    suspend fun createProject(project: NewProject): ApiResult<ProjectDto>
}