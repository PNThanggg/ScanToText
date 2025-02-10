package com.hoicham.orc.pdf_export

import android.content.Context
import com.hoicham.orc.database.entity.Scan

interface PdfExportService {
    fun printDocument(
        context: Context, titleOfDocument: String, scans: List<Scan>, color: Int, fontSize: Int
    )
}