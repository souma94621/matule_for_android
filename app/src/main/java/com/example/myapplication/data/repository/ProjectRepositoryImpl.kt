package com.example.myapplication.data.repository

import com.example.myapplication.data.mapper.toDomain
import com.example.myapplication.data.mapper.toResource
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.NewProjectData
import com.example.myapplication.domain.model.PhotoData
import com.example.myapplication.domain.model.Project
import com.example.myapplication.domain.repository.ProjectRepository
import com.example.network.model.NewProject
import com.example.network.model.PhotoFile
import com.example.network.model.ProjectDto
import com.example.network.service.ProjectService

/** Реализация [ProjectRepository] поверх сетевого интерфейса [ProjectService]. */
class ProjectRepositoryImpl(
    private val service: ProjectService,
    private val imageUrl: (String, String, String) -> String
) : ProjectRepository {

    private fun ProjectDto.photoUrl(): String? =
        if (photo.isBlank()) null else imageUrl(collectionId, id, photo)

    override suspend fun getProjects(): Resource<List<Project>> =
        service.getProjects().toResource { list -> list.map { it.toDomain(it.photoUrl()) } }

    override suspend fun createProject(data: NewProjectData, photo: PhotoData): Resource<Project> =
        service.createProject(
            NewProject(
                type = data.type,
                title = data.title,
                startDate = data.startDate,
                endDate = data.endDate,
                recipient = data.recipient,
                descriptionSource = data.descriptionSource,
                categoryId = data.categoryId,
                photo = PhotoFile(photo.bytes, photo.fileName, photo.mimeType)
            )
        ).toResource { it.toDomain(it.photoUrl()) }
}