package com.hoicham.orc.use_case.entity_extraction

import com.hoicham.orc.database.entity.ExtractionModelType

data class ExtractionResultModel(
    val type: ExtractionModelType,
    val content: String
)
