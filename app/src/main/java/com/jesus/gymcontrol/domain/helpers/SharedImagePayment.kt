package com.jesus.gymcontrol.domain.helpers

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


fun shareBitmap(context: Context, bitmap: Bitmap) {
	val cachePath = File(context.cacheDir, "images")
	cachePath.mkdirs()
	
	val file = File(cachePath, "payment_invoice.jpg")
	FileOutputStream(file).use { out ->
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
	}
	
	val uri = FileProvider.getUriForFile(
		context,
		"${context.packageName}.fileprovider",
		file
	)
	
	val intent = Intent(Intent.ACTION_SEND).apply {
		type = "image/jpeg"
		putExtra(Intent.EXTRA_STREAM, uri)
		addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
	}
	
	context.startActivity(Intent.createChooser(intent, "Compartir factura"))
}
