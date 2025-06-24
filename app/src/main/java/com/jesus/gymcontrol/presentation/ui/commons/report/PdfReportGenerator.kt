package com.jesus.gymcontrol.presentation.ui.commons.report

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.List

object PdfReportGenerator {
	
	fun generateReportPdf(
		context: Context,
		reportTitle: String,
		headers: List<String>,
		rows: List<List<String>>
	): Uri? {
		return try {
			val fileName = "${reportTitle}_${System.currentTimeMillis()}.pdf"
			val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
			val file = File(downloadsDir, fileName)
			
			val document = PdfDocument()
			val paint = android.graphics.Paint()
			val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
			val page = document.startPage(pageInfo)
			val canvas = page.canvas
			
			val titleSize = 18f
			val textSize = 12f
			val startX = 40f
			var currentY = 60f
			
			// Title
			paint.textSize = titleSize
			paint.isFakeBoldText = true
			canvas.drawText("Reporte: $reportTitle", startX, currentY, paint)
			
			currentY += 30
			
			// Headers
			paint.textSize = textSize
			paint.isFakeBoldText = true
			headers.forEachIndexed { index, header ->
				canvas.drawText(header, startX + (index * 100), currentY, paint)
			}
			
			currentY += 20
			paint.isFakeBoldText = false
			
			// Rows
			rows.forEach { row ->
				row.forEachIndexed { index, cell ->
					canvas.drawText(cell, startX + (index * 100), currentY, paint)
				}
				currentY += 20
				if (currentY > 780) {
					document.finishPage(page)
					// Opcional: iniciar nueva página
					// Nota: Aquí deberías implementar lógica para múltiples páginas si es necesario
				}
			}
			
			// Footer
			currentY = 820f
			paint.textSize = 10f
			val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
			canvas.drawText("Generado: $dateStr", startX, currentY, paint)
			
			document.finishPage(page)
			document.writeTo(FileOutputStream(file))
			document.close()
			
			FileProvider.getUriForFile(
				context,
				"${context.packageName}.provider",
				file
			)
		} catch (e: Exception) {
			e.printStackTrace()
			null
		}
	}
}