package com.example.network.impl

import com.example.network.common.ApiResult
import com.example.network.common.SessionStorage
import com.example.network.common.escapeFilter
import com.example.network.common.safeCall
import com.example.network.model.NewProject
import com.example.network.model.ProjectDto
import com.example.network.service.ProjectService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/** Реализация [ProjectService] поверх Pocketbase (создание проекта идёт как multipart из-за фото). */
internal class ProjectServiceImpl(
    private val api: PocketBaseApi,
    private val session: SessionStorage
) : ProjectService {

    override suspend fun getProjects(): ApiResult<List<ProjectDto>> = safeCall {
        val userId = session.userId ?: error("Пользователь не авторизован")
        api.getProjects("user='${escapeFilter(userId)}'").items
    }

    override suspend fun createProject(project: NewProject): ApiResult<ProjectDto> = safeCall {
        val userId = session.userId ?: error("Пользователь не авторизован")

        fun text(value: String): RequestBody = value.toRequestBody("text/plain".toMediaType())

        val fields = mapOf(
            "user" to text(userId),
            "type" to text(project.type),
            "title" to text(project.title),
            "start_date" to text(project.startDate),
            "end_date" to text(project.endDate),
            "recipient" to text(project.recipient),
            "description_source" to text(project.descriptionSource),
            "category" to text(project.categoryId)
        )
        val photo = MultipartBody.Part.createFormData(
            "photo",
            project.photo.fileName,
            project.photo.bytes.toRequestBody(project.photo.mimeType.toMediaType())
        )
        api.createProject(fields, photo)
    }
}