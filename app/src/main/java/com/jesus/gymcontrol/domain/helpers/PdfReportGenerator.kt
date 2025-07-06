package com.jesus.gymcontrol.domain.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
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
		totalDolares: Double? = null,
		totalBolivares: Double? = null
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
			
			val totalBackgroundPaint = Paint().apply {
				color = Color.LTGRAY
				style = Paint.Style.FILL
			}
			
			val pageWidth = 595
			val pageHeight = 842
			val marginLeft = 40f
			val marginTop = 60f
			val marginBottom = 60f
			val rowHeight = 25f
			
			val dateText = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
			
			val availableHeight = pageHeight - marginTop - marginBottom - 90f
			val rowsPerPage = availableHeight.toInt() / rowHeight
			val totalPages = ceil(rows.size.toDouble() / rowsPerPage).toInt()
			
			val columnCount = headers.size
			val baseColumnWidth = (pageWidth - 2 * marginLeft) / columnCount
			
			val columnWidths = FloatArray(columnCount) { baseColumnWidth.toFloat() }
			if (headers.last().contains("Monto", ignoreCase = true)) {
				columnWidths[columnCount - 1] = baseColumnWidth * 1.5f
				val reduce = (baseColumnWidth * 0.5f) / (columnCount - 1)
				for (i in 0 until columnCount - 1) {
					columnWidths[i] -= reduce.toFloat()
				}
			}
			
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
					val x = marginLeft + columnWidths.take(index).sum()
					canvas.drawText(header, x + 5, y + 15, paint)
					canvas.drawRect(x, y, x + columnWidths[index], y + rowHeight, linePaint)
				}
				y += rowHeight
				
				paint.isFakeBoldText = false
				paint.textSize = 10f
				
				for (i in 0 until rowsPerPage.toInt()) {
					if (rowIndex >= rows.size) break
					val row = rows[rowIndex]
					row.forEachIndexed { index, cell ->
						val x = marginLeft + columnWidths.take(index).sum()
						val formattedCell = formatIfDate(cell)
						drawWrappedText(canvas, formattedCell, x + 5, y + 12, columnWidths[index] - 10, paint)
						canvas.drawRect(x, y, x + columnWidths[index], y + rowHeight, linePaint)
					}
					y += rowHeight
					rowIndex++
				}
				
				// Si ya es la última página y ya dibujamos todas las filas
				if (rowIndex >= rows.size && totalDolares != null && totalBolivares != null) {
					// dibujar fondo gris claro
					val totalText = "T.: ${formatDollars(totalDolares)} + ${formatBolivares(totalBolivares)}"
					val totalRow = List(columnCount - 1) { "" } + totalText
					
					totalRow.forEachIndexed { index, cell ->
						val x = marginLeft + columnWidths.take(index).sum()
						canvas.drawRect(
							x,
							y,
							x + columnWidths[index],
							y + rowHeight,
							totalBackgroundPaint
						)
						canvas.drawRect(
							x,
							y,
							x + columnWidths[index],
							y + rowHeight,
							linePaint
						)
						paint.isFakeBoldText = true
						drawWrappedText(canvas, cell, x + 5, y + 12, columnWidths[index] - 10, paint)
						paint.isFakeBoldText = false
					}
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
		pageHeight: Int
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
	
	private fun drawWrappedText(
		canvas: Canvas,
		text: String,
		x: Float,
		y: Float,
		maxWidth: Float,
		paint: Paint
	) {
		val words = text.split(" ")
		val lineSpacing = paint.textSize + 2f
		var currentLine = ""
		var currentY = y
		
		for (word in words) {
			val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
			if (paint.measureText(testLine) <= maxWidth) {
				currentLine = testLine
			} else {
				canvas.drawText(currentLine, x, currentY, paint)
				currentY += lineSpacing
				currentLine = word
			}
		}
		if (currentLine.isNotEmpty()) {
			canvas.drawText(currentLine, x, currentY, paint)
		}
	}
	
	fun getUriFromFile(context: Context, file: File): Uri {
		return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
	}
	
	private fun formatDollars(amount: Double?): String {
		if (amount == null) return "$0.00"
		val format = NumberFormat.getCurrencyInstance(Locale.US)
		return format.format(amount)
	}
	
	private fun formatBolivares(amount: Double?): String {
		if (amount == null) return "Bs 0,00"
		val format = NumberFormat.getCurrencyInstance(Locale("es", "VE"))
		format.maximumFractionDigits = 2
		format.minimumFractionDigits = 2
		return format.format(amount).replace("Bs.", "Bs")
	}
}