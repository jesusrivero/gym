package com.jesus.gymcontrol.presentation.ui.commons.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.presentation.ui.settings.details.manage.formatAmount

//@Composable
//fun PaymentReceiptCard(pago: Pago) {
//	Card(
//		modifier = Modifier
//			.fillMaxWidth()
//			.padding(16.dp)
//			.background(Color.White),
//		shape = RoundedCornerShape(12.dp),
//		elevation = CardDefaults.cardElevation(8.dp)
//	) {
//		Column(modifier = Modifier.padding(16.dp)) {
//			Text("RECIBO DE PAGO", fontWeight = FontWeight.Bold, fontSize = 20.sp)
//			Spacer(Modifier.height(8.dp))
//			Text("Cliente: ${pago.name}")
//			Text("Cédula: ${pago.idcard}")
//			Text("Membresía: ${pago.membershipName}")
//			Text("Monto: ${formatAmount(pago)}")
//			Text("Tipo de pago: ${pago.tipepayment}")
//			Text("Referencia: ${pago.reference ?: "-"}")
//			Text("Fecha: ${formatDate(pago.date)}")
//			if (pago.description.isNotBlank()) Text("Notas: ${pago.description}")
//			if (!pago.promocionNombre.isNullOrBlank()) {
//				Text("Promoción: ${pago.promocionNombre} (${pago.promocionDescuento}%)")
//			}
//		}
//	}
//}
