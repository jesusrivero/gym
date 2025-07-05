package com.jesus.gymcontrol.domain.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

object PdfReportGenerator {
	
	fun generateReportPdf(
		context: Context,
		reportTitle: String,
		headers: List<String>,
		rows: List<List<String>>,
	): File? {
		return try {
			val fileName = "${reportTitle}_${System.currentTimeMillis()}.pdf"
			val file = File(
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				fileName
			)
			
			val document = PdfDocument()
			val paint = Paint()
			val linePaint = Paint().apply {
				strokeWidth = 1f
				color = android.graphics.Color.BLACK
				style = Paint.Style.STROKE
			}
			
			val pageWidth = 595
			val pageHeight = 842
			val marginLeft = 40f
			val marginTop = 60f
			val marginBottom = 60f
			val columnWidth = 100f
			val rowHeight = 20f
			
			val dateText = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
			
			val availableHeight = pageHeight - marginTop - marginBottom - 90f
			val rowsPerPage = availableHeight.toInt() / rowHeight
			val totalPages = ceil(rows.size.toDouble() / rowsPerPage).toInt()
			
			var rowIndex = 0
			var pageNumber = 1
			
			while (rowIndex < rows.size) {
				val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
				val page = document.startPage(pageInfo)
				val canvas = page.canvas
				
				var y = marginTop
				
				paint.textSize = 16f
				paint.isFakeBoldText = true
				canvas.drawText("Reporte: $reportTitle", marginLeft, y, paint)
				y += 30f
				
				paint.textSize = 12f
				paint.isFakeBoldText = true
				headers.forEachIndexed { index, header ->
					val x = marginLeft + index * columnWidth
					canvas.drawText(header, x + 5, y + 15, paint)
					canvas.drawRect(x, y, x + columnWidth, y + rowHeight, linePaint)
				}
				y += rowHeight
				
				paint.isFakeBoldText = false
				for (i in 0 until rowsPerPage.toInt()) {
					if (rowIndex >= rows.size) break
					val row = rows[rowIndex]
					row.forEachIndexed { index, cell ->
						val x = marginLeft + index * columnWidth
						val formattedCell = formatIfDate(cell)
						canvas.drawText(formattedCell, x + 5, y + 15, paint)
						canvas.drawRect(x, y, x + columnWidth, y + rowHeight, linePaint)
					}
					y += rowHeight
					rowIndex++
				}
				
				drawFooter(canvas, paint, dateText, pageNumber, totalPages, pageWidth, pageHeight)
				
				document.finishPage(page)
				pageNumber++
			}
			
			document.writeTo(FileOutputStream(file))
			document.close()
			file
		} catch (e: Exception) {
			e.printStackTrace()
			null
		}
	}
	
	private fun formatIfDate(cell: String): String {
		return try {
			val longValue = cell.toLong()
			if (longValue in 1000000000000..9999999999999) {
				val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
				sdf.format(Date(longValue))
			} else {
				cell
			}
		} catch (e: Exception) {
			cell
		}
	}
	
	private fun drawFooter(
		canvas: Canvas,
		paint: Paint,
		dateText: String,
		pageNumber: Int,
		totalPages: Int,
		pageWidth: Int,
		pageHeight: Int,
	) {
		paint.textSize = 10f
		paint.isFakeBoldText = false
		val margin = 40f
		val bottomY = pageHeight - 20f
		
		canvas.drawText("Generado el: $dateText", margin, bottomY, paint)
		
		val pageLabel = "Página $pageNumber de $totalPages"
		val textWidth = paint.measureText(pageLabel)
		canvas.drawText(pageLabel, pageWidth - textWidth - margin, bottomY, paint)
	}
	
	fun getUriFromFile(context: Context, file: File): Uri {
		return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
	}
}


