package com.hoicham.orc.pdf_export

import android.content.Context
import android.print.PrintManager
import com.hoicham.orc.R
import com.hoicham.orc.database.entity.Scan

class PdfExportServiceImpl : PdfExportService {
    override fun printDocument(
        context: Context, titleOfDocument: String, scans: List<Scan>, color: Int, fontSize: Int
    ) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

        val jobName = context.getString(R.string.app_name) + " Document"

        val textList = mutableListOf<String>()
        scans.forEach {
            textList.add(it.scanText)
        }

        printManager.print(
            jobName, MyPrintDocumentAdapter(
                context, titleOfDocument, textList, color, fontSize
            ), null
        )
    }
}