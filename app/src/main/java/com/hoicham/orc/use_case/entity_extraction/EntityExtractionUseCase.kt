package com.hoicham.orc.use_case.entity_extraction

import com.google.mlkit.nl.entityextraction.Entity
import com.google.mlkit.nl.entityextraction.EntityAnnotation
import com.google.mlkit.nl.entityextraction.EntityExtraction
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions
import com.hoicham.orc.database.entity.ExtractionModelType
import com.hoicham.orc.di.AppDispatchers
import com.hoicham.orc.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EntityExtractionUseCase @Inject constructor(
    @Dispatcher(AppDispatchers.IO) private val dispatcher: CoroutineDispatcher,
) {

    private val entityExtractor = EntityExtraction.getClient(
        EntityExtractorOptions.Builder(EntityExtractorOptions.ENGLISH).build()
    )

    suspend operator fun invoke(input: String) = withContext(dispatcher) {
        kotlin.runCatching {
            val resultModels: MutableList<ExtractionResultModel> = mutableListOf()
            val annotations: List<EntityAnnotation> = entityExtractor.annotate(input).await()
            annotations.forEach {
                resultModels.addAll(it.entities.map { entity ->
                    val type = when (entity.type) {
                        Entity.TYPE_EMAIL -> ExtractionModelType.EMAIL
                        Entity.TYPE_PHONE -> ExtractionModelType.PHONE
                        Entity.TYPE_URL -> ExtractionModelType.URL
                        else -> ExtractionModelType.OTHER
                    }
                    ExtractionResultModel(
                        type = type, content = it.annotatedText
                    )
                })
            }
            resultModels.toList()
        }
    }
}