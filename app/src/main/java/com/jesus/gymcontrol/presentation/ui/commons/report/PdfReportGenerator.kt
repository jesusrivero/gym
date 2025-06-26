package com.jesus.gymcontrol.presentation.ui.commons.report
import java.util.List

import android.content.Context
import android.graphics.Color
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
			val paint = Paint()
			val boldPaint = Paint().apply {
				isFakeBoldText = true
				textSize = 18f
			}
			val headerPaint = Paint().apply {
				isFakeBoldText = true
				textSize = 14f
			}
			val textPaint = Paint().apply {
				textSize = 12f
				color = Color.BLACK
			}
			val statusPaint = Paint().apply {
				textSize = 12f
			}
			val linePaint = Paint().apply {
				strokeWidth = 0.5f
				color = Color.DKGRAY
			}
			
			val pageWidth = 595
			val pageHeight = 842
			val marginLeft = 40f
			val marginRight = 40f
			val usableWidth = pageWidth - marginLeft - marginRight
			
			val columnCount = headers.size
			val columnWidth = usableWidth / columnCount
			
			var currentPageNumber = 1
			
			fun startNewPage(): PdfDocument.Page {
				val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
				val page = document.startPage(pageInfo)
				currentPageNumber++
				return page
			}
			
			var page = startNewPage()
			var canvas = page.canvas
			var currentY = 60f
			
			// Dibujar título centrado
			val titleText = "Reporte: $reportTitle"
			val titleWidth = boldPaint.measureText(titleText)
			val titleX = (pageWidth - titleWidth) / 2f
			canvas.drawText(titleText, titleX, currentY, boldPaint)
			
			currentY += 40f
			
			// Dibujar encabezados con negrita
			headers.forEachIndexed { index, header ->
				val x = marginLeft + index * columnWidth
				canvas.drawText(header, x, currentY, headerPaint)
			}
			
			currentY += 25f
			
			// Línea debajo del encabezado
			canvas.drawLine(marginLeft, currentY - 10f, pageWidth - marginRight, currentY - 10f, linePaint)
			
			// Función para dibujar una fila, devuelve la nueva Y
			fun drawRow(row: List<String>, yPos: Float): Float {
				var y = yPos
				row.forEachIndexed { index, cell ->
					val x = marginLeft + index * columnWidth
					val headerLower = if (index in headers.indices) headers[index].lowercase() else ""
					
					// Colorear según el estado si la columna es "estado" o similar
					if (headerLower.contains("estado") || headerLower.contains("activo")) {
						val cellLower = cell.trim().lowercase()
						when (cellLower) {
							"activo" -> {
								statusPaint.color = Color.GREEN
								canvas.drawText(cell, x, y, statusPaint)
							}
							"inactivo", "pendiente" -> {
								statusPaint.color = Color.RED
								canvas.drawText(cell, x, y, statusPaint)
							}
							else -> {
								textPaint.color = Color.BLACK
								canvas.drawText(cell, x, y, textPaint)
							}
						}
					} else {
						textPaint.color = Color.BLACK
						canvas.drawText(cell, x, y, textPaint)
					}
				}
				return y + 20f
			}
			
			// Dibujar filas con paginación
			for (row in rows) {
				if (currentY + 30f > pageHeight - 60f) {
					// Terminar página actual y crear una nueva
					document.finishPage(page)
					page = startNewPage()
					canvas = page.canvas
					currentY = 60f
					
					// Redibujar encabezados en nueva página
					val titleWidthNew = boldPaint.measureText(titleText)
					val titleXNew = (pageWidth - titleWidthNew) / 2f
					canvas.drawText(titleText, titleXNew, currentY, boldPaint)
					currentY += 40f
					headers.forEachIndexed { index, header ->
						val x = marginLeft + index * columnWidth
						canvas.drawText(header, x, currentY, headerPaint)
					}
					currentY += 25f
					canvas.drawLine(marginLeft, currentY - 10f, pageWidth - marginRight, currentY - 10f, linePaint)
				}
				
				currentY = drawRow(row, currentY)
			}
			
			// Footer
			val footerPaint = Paint().apply {
				textSize = 10f
				color = Color.DKGRAY
			}
			val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
			val footerText = "Generado: $dateStr"
			val footerWidth = footerPaint.measureText(footerText)
			val footerX = pageWidth - marginRight - footerWidth
			val footerY = pageHeight - 30f
			canvas.drawText(footerText, footerX, footerY, footerPaint)
			
			// Terminar última página
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