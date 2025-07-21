package com.jesus.gymcontrol.domain.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.jesus.gymcontrol.domain.model.Payment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.graphics.toColorInt

fun generateInvoicePdf(
	context: Context,
	payment: Payment,
	gymName: String?,
	gymDirection: String,
	gymRif: String?,
	fileName: String = "Factura_${System.currentTimeMillis()}.pdf",
): File {
	val pdfDocument = PdfDocument()
	val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
	val page = pdfDocument.startPage(pageInfo)
	
	val canvas = page.canvas
	val margin = 40f
	val rightMargin = pageInfo.pageWidth - margin
	val centerX = pageInfo.pageWidth / 2f
	
	// PINTURAS
	val titlePaint = Paint().apply {
		color = Color.BLACK
		textSize = 20f
		typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
		textAlign = Paint.Align.CENTER
	}
	
	val labelPaint = Paint().apply {
		color = Color.DKGRAY
		textSize = 12f
		typeface = Typeface.DEFAULT_BOLD
	}
	
	val valuePaint = Paint().apply {
		color = Color.BLACK
		textSize = 12f
	}
	
	val gymNamePaint = Paint().apply {
		color = Color.BLACK
		textSize = 18f
		typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
		textAlign = Paint.Align.RIGHT
	}
	
	val gymInfoPaint = Paint().apply {
		color = Color.BLACK
		textSize = 12f
		textAlign = Paint.Align.RIGHT
	}
	
	val dividerPaint = Paint().apply {
		color = Color.LTGRAY
		strokeWidth = 1f
	}
	
	val totalPaint = Paint().apply {
		color = Color.BLACK
		textSize = 14f
		typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
	}
	
	val totalValuePaint = Paint().apply {
		color = "#388E3C".toColorInt()
		textSize = 14f
		typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
		textAlign = Paint.Align.RIGHT
	}
	
	// 📌 GYM INFO en la esquina superior derecha
	var gymY = 50f
	gymName?.let {
		canvas.drawText(it, rightMargin, gymY, gymNamePaint)
		gymY += 20f
	}
	gymRif?.let {
		canvas.drawText(it, rightMargin, gymY, gymInfoPaint)
		gymY += 15f
	}
	gymDirection.let {
		canvas.drawText(it, rightMargin, gymY, gymInfoPaint)
		gymY += 15f
	}
	
	// 📌 Título
	var y = gymY + 15f
	canvas.drawText("COMPROBANTE DE PAGO", centerX, y, titlePaint)
	y += 20f
	canvas.drawLine(margin, y, pageInfo.pageWidth - margin, y, dividerPaint)
	y += 20f
	
	val col1X = margin
	val col2X = pageInfo.pageWidth - margin
	
	drawRow(canvas, "Cédula:", payment.idCard, col1X, col2X, y, labelPaint, valuePaint)
	y += 20f
	drawRow(canvas, "Nombre:", payment.name, col1X, col2X, y, labelPaint, valuePaint)
	y += 20f
	drawRow(canvas, "Apellido:", payment.lastname, col1X, col2X, y, labelPaint, valuePaint)
	y += 20f
	drawRow(canvas, "Membresía:", payment.membershipName, col1X, col2X, y, labelPaint, valuePaint)
	y += 20f
	
	// Concepto multilinea
	val conceptLines = breakTextIntoLines(payment.description, valuePaint, pageInfo.pageWidth - col2X - margin)
	canvas.drawText("Concepto:", col1X, y, labelPaint)
	var conceptY = y
	for (line in conceptLines) {
		canvas.drawText(line, col2X, conceptY, valuePaint)
		conceptY += 15f
	}
	y = conceptY + 5f
	
	drawRow(canvas, "Tipo de Pago:", payment.paymentType, col1X, col2X, y, labelPaint, valuePaint)
	y += 20f
	
	when (payment.paymentType.lowercase()) {
		"dólares" -> {
			drawRow(canvas, "Monto $:", "$${"%.2f".format(payment.amountDollar)}", col1X, col2X, y, labelPaint, valuePaint)
			y += 20f
		}
		"bolívares" -> {
			drawRow(canvas, "Monto Bs:", "Bs. ${"%.2f".format(payment.amountBs)}", col1X, col2X, y, labelPaint, valuePaint)
			y += 20f
		}
		"mixto" -> {
			drawRow(canvas, "Monto $:", "$${"%.2f".format(payment.amountDollar)}", col1X, col2X, y, labelPaint, valuePaint)
			y += 20f
			drawRow(canvas, "Monto Bs:", "Bs. ${"%.2f".format(payment.amountBs)}", col1X, col2X, y, labelPaint, valuePaint)
			y += 20f
		}
	}
	
	if (!payment.promocionNombre.isNullOrBlank()) {
		canvas.drawLine(margin, y, pageInfo.pageWidth - margin, y, dividerPaint)
		y += 20f
		
		drawRow(canvas, "Promoción:", payment.promocionNombre, col1X, col2X, y, labelPaint, valuePaint)
		y += 20f
		
		payment.promocionPorcentajeDescuento?.let {
			drawRow(canvas, "Descuento aplicado:", "${"%.0f".format(it)}%", col1X, col2X, y, labelPaint, valuePaint)
			y += 20f
		}
	}
	
	val vencimientoFormatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(payment.fechaVencimiento))
	drawRow(canvas, "Fecha de vencimiento:", vencimientoFormatted, col1X, col2X, y, labelPaint, valuePaint)
	y += 20f
	
	val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(payment.date))
	drawRow(canvas, "Fecha del pago:", fechaActual, col1X, col2X, y, labelPaint, valuePaint)
	y += 20f
	
	canvas.drawLine(margin, y, pageInfo.pageWidth - margin, y, dividerPaint)
	y += 20f
	
	canvas.drawRect(margin, y, pageInfo.pageWidth - margin, y + 20f, dividerPaint)
	canvas.drawText("Monto Total:", margin + 5f, y + 15f, totalPaint)
	canvas.drawText(
		formatMembershipPrice(payment),
		pageInfo.pageWidth - margin - 5f,
		y + 15f,
		totalValuePaint
	)
	y += 40f
	
	canvas.drawLine(margin, pageInfo.pageHeight - 40f, pageInfo.pageWidth - margin, pageInfo.pageHeight - 40f, dividerPaint)
	canvas.drawText(
		"¡Gracias por su preferencia!",
		centerX,
		pageInfo.pageHeight - 25f,
		Paint().apply {
			color = Color.DKGRAY
			textSize = 10f
			textAlign = Paint.Align.CENTER
		}
	)
	
	pdfDocument.finishPage(page)
	
	val file = File(context.cacheDir, fileName)
	pdfDocument.writeTo(file.outputStream())
	pdfDocument.close()
	
	return file
}

fun drawRow(
	canvas: Canvas,
	label: String,
	value: String,
	col1X: Float,
	col2X: Float,
	y: Float,
	labelPaint: Paint,
	valuePaint: Paint,
) {
	canvas.drawText(label, col1X, y, labelPaint)
	canvas.drawText(value, col2X, y, valuePaint.apply { textAlign = Paint.Align.RIGHT })
}

fun formatMembershipPrice(payment: Payment): String {
	return "$${"%.2f".format(payment.amount)}"
}

fun breakTextIntoLines(
	text: String,
	paint: Paint,
	maxWidth: Float,
): List<String> {
	val words = text.split(" ")
	val lines = mutableListOf<String>()
	var currentLine = ""
	
	for (word in words) {
		val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
		if (paint.measureText(testLine) <= maxWidth) {
			currentLine = testLine
		} else {
			lines.add(currentLine)
			currentLine = word
		}
	}
	if (currentLine.isNotEmpty()) {
		lines.add(currentLine)
	}
	
	return lines
}
