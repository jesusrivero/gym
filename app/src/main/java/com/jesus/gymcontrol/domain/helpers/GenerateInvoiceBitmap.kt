package com.jesus.gymcontrol.domain.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.presentation.ui.commons.formatMonto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun generateInvoiceBitmap(payment: Payment, context: Context): Bitmap {
	val width = 1080
	val height = 1600
	val margen = 50f
	val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
	val canvas = Canvas(bitmap)
	
	val headerColor = Color.parseColor("#003366")
	val textColor = Color.BLACK
	val grayColor = Color.LTGRAY
	
	val headerPaint = Paint().apply {
		color = headerColor
	}
	val titlePaint = Paint().apply {
		color = Color.WHITE
		textSize = 60f
		isFakeBoldText = true
		textAlign = Paint.Align.CENTER
	}
	val labelPaint = Paint().apply {
		color = textColor
		textSize = 38f
		isFakeBoldText = true
	}
	val valuePaint = Paint().apply {
		color = textColor
		textSize = 38f
	}
	val totalPaint = Paint().apply {
		color = textColor
		textSize = 50f
		isFakeBoldText = true
	}
	val dividerPaint = Paint().apply {
		color = grayColor
		strokeWidth = 2f
	}
	
	canvas.drawColor(Color.WHITE)
	
	// Header
	canvas.drawRect(0f, 0f, width.toFloat(), 150f, headerPaint)
	canvas.drawText("FACTURA", width / 2f, 100f, titlePaint)
	
	var y = 180f + 30f
	
	val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(payment.date))
	val venc = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(payment.fechaVencimiento))
	
	fun drawLabelValue(label: String, value: String) {
		canvas.drawText(label, margen, y, labelPaint)
		canvas.drawText(value, width / 2f, y, valuePaint)
		y += 50f
	}
	
	drawLabelValue("Cliente:", payment.name)
	drawLabelValue("Membresía:", payment.membershipName)
	drawLabelValue("Fecha:", date)
	drawLabelValue("Vence:", venc)
	
	y += 20f
	canvas.drawLine(margen, y, width - margen, y, dividerPaint)
	y += 60f
	
	canvas.drawText("Concepto", margen, y, labelPaint)
	canvas.drawText("Monto", width - margen - 200f, y, labelPaint)
	y += 40f
	canvas.drawLine(margen, y, width - margen, y, dividerPaint)
	y += 50f
	
	// 🔷 Concepto y monto total (precio de la membresía)
	canvas.drawText("Precio", margen, y, valuePaint)
	canvas.drawText(formatMembershipPrice(payment), width - margen - 200f, y, valuePaint)
	y += 50f
	
	// 🔷 Detalle del pago según monedas
	if (payment.amountDollar > 0) {
		canvas.drawText("Dólares", margen, y, valuePaint)
		canvas.drawText("$${"%.2f".format(payment.amountDollar)}", width - margen - 200f, y, valuePaint)
		y += 50f
	}
	
	if (payment.amountBs > 0) {
		canvas.drawText("Bolívares", margen, y, valuePaint)
		canvas.drawText("Bs. ${"%.2f".format(payment.amountBs)}", width - margen - 200f, y, valuePaint)
		y += 50f
	}
	
	payment.reference?.let {
		canvas.drawText("Referencia: $it", margen, y, valuePaint)
		y += 50f
	}
	
	if (!payment.promocionNombre.isNullOrBlank()) {
		canvas.drawText("Promoción: ${payment.promocionNombre}", margen, y, valuePaint)
		y += 50f
	}
	
	if (payment.promocionPorcentajeDescuento != null) {
		canvas.drawText("Descuento: ${payment.promocionPorcentajeDescuento}%", margen, y, valuePaint)
		y += 50f
	}
	
	y += 20f
	canvas.drawLine(margen, y, width - margen, y, dividerPaint)
	y += 60f
	
	canvas.drawText("TOTAL", margen, y, totalPaint)
	canvas.drawText(formatMembershipPrice(payment), width - margen - 200f, y, totalPaint)
	y += 100f
	
	canvas.drawText("Descripción:", margen, y, labelPaint)
	y += 50f
	
	val maxLineWidth = width - 2 * margen
	val descriptionLines = breakTextIntoLines(payment.description, valuePaint, maxLineWidth)
	
	for (line in descriptionLines) {
		canvas.drawText(line, margen, y, valuePaint)
		y += 45f
	}
	
	return bitmap
}

// 🔷 helper para partir texto en líneas
fun breakTextIntoLines(text: String, paint: Paint, maxWidth: Float): List<String> {
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

// 🔷 Esta función devuelve el precio original de la membresía:
fun formatMembershipPrice(payment: Payment): String {
	return "$${"%.2f".format(payment.amount)}"
}