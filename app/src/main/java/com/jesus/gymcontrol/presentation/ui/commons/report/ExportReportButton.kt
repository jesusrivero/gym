package com.jesus.gymcontrol.presentation.ui.commons.report

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExportReportButton(
	modifier: Modifier = Modifier,
	reportTitle: String,
	headers: List<String>,
	rows: List<List<String>>,
	context: Context
) {
	Button(
		onClick = {
			val uri = PdfReportGenerator.generateReportPdf(context, reportTitle,
				headers as java.util.List<String>, rows as java.util.List<java.util.List<String>>
			)
			uri?.let {
				val intent = Intent(Intent.ACTION_SEND).apply {
					type = "application/pdf"
					putExtra(Intent.EXTRA_STREAM, it)
					addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
				}
				context.startActivity(Intent.createChooser(intent, "Compartir Reporte"))
			}
		},
		modifier = modifier
	) {
		Icon(Icons.Default.Share, contentDescription = "Compartir")
		Spacer(modifier = Modifier.width(8.dp))
		Text("Exportar")
	}
}
