package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.NewProjectData
import com.example.myapplication.domain.model.PhotoData
import com.example.myapplication.domain.model.Project
import com.example.myapplication.domain.repository.ProjectRepository
import com.example.myapplication.domain.util.parseDate

/** Получить проекты текущего пользователя. */
class GetProjectsUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(): Resource<List<Project>> = repository.getProjects()
}

/** Создать проект: все поля и фото обязательны, даты проверяются. */
class CreateProjectUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(data: NewProjectData, photo: PhotoData?): Resource<Project> {
        val textFields = listOf(data.type, data.title, data.recipient, data.descriptionSource, data.categoryId)
        if (textFields.any { it.isBlank() } || photo == null) {
            return Resource.Failure("Заполните все поля и добавьте фото")
        }
        val start = parseDate(data.startDate) ?: return Resource.Failure("Дата начала: формат дд.мм.гггг")
        val end = parseDate(data.endDate) ?: return Resource.Failure("Дата окончания: формат дд.мм.гггг")
        if (end.isBefore(start)) return Resource.Failure("Дата окончания раньше даты начала")

        val source = data.descriptionSource.trim().let {
            if (it.startsWith("http://") || it.startsWith("https://")) it else "https://$it"
        }
        return repository.createProject(
            data.copy(startDate = start.toString(), endDate = end.toString(), descriptionSource = source),
            photo
        )
    }
}