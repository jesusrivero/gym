package com.jesus.gymcontrol.domain.helpers

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReportGenerator {
	
	fun generateReportPdf(
		context: Context,
		reportTitle: String,
		headers: List<String>,
		rows: List<List<String>>
	): File? {
		return try {
			val fileName = "${reportTitle}_${System.currentTimeMillis()}.pdf"
			val file = File(
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				fileName
			)
			
			val document = PdfDocument()
			val paint = android.graphics.Paint()
			val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
			val page = document.startPage(pageInfo)
			val canvas = page.canvas
			
			var x = 40f
			var y = 60f
			paint.textSize = 16f
			paint.isFakeBoldText = true
			canvas.drawText("Reporte: $reportTitle", x, y, paint)
			
			y += 30f
			paint.textSize = 12f
			headers.forEachIndexed { index, header ->
				canvas.drawText(header, x + index * 100, y, paint)
			}
			
			y += 20f
			paint.isFakeBoldText = false
			rows.forEach { row ->
				row.forEachIndexed { index, cell ->
					canvas.drawText(cell, x + index * 100, y, paint)
				}
				y += 20f
			}
			
			// Footer
			y = 820f
			val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
			canvas.drawText("Generado el: $date", x, y, paint)
			
			document.finishPage(page)
			document.writeTo(FileOutputStream(file))
			document.close()
			
			file
		} catch (e: Exception) {
			e.printStackTrace()
			null
		}
	}
	
	fun getUriFromFile(context: Context, file: File): android.net.Uri {
		return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
	}
}
