package com.hoicham.orc.use_case.text_extract

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hoicham.orc.di.AppDispatchers
import com.hoicham.orc.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScanTextFromImageUseCase @Inject constructor(
    @Dispatcher(AppDispatchers.IO) private val dispatcher: CoroutineDispatcher,
) {
    private val recognizers = listOf(
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS),
        TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build()),
        TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build()),
        TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()),
        TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build()),
    )

    suspend operator fun invoke(image: InputImage) = withContext(dispatcher) {
        kotlin.runCatching {
            val detectedText = mutableSetOf<String>()
            recognizers.forEach { textRecognizer ->
                val result = textRecognizer.process(image).await()
                if (result.text.isNotBlank()) {
                    detectedText.add(result.text)
                }
            }

            val completeText = buildString {
                detectedText.forEach { text -> append(text) }
            }
            completeText
        }
    }
}