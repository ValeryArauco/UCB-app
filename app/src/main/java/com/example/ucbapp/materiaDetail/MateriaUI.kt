package com.example.ucbapp.materiaDetail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.Materia
import com.example.ucbapp.service.Util

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriaUI(
    materia: Materia,
    onBackPressed: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(materia.elemCompletados) {
        if (materia.elemCompletados < materia.elementosTotales) {
            Util.sendNotification(
                context = context,
            )
        }
    }
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F2)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            Text(
                text = materia.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            Text(
                text = "Resumen de progreso",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp),
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                CircularProgressChart(
                    progress = materia.elemCompletados.toFloat() / materia.elementosTotales,
                    text = "${materia.elemCompletados}/${materia.elementosTotales}",
                    label = "Elementos completados",
                    progressColor = Color(0xFFFFC903),
                )

                CircularProgressChart(
                    progress = materia.recTomados.toFloat() / materia.recTotales,
                    text = "${materia.recTomados}/${materia.recTotales}",
                    label = "Recuperatorios tomados",
                    progressColor = Color(0xFF003C71), // Azul
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Detalles adicionales",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp),
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                StatCard(
                    title = "Evaluados",
                    value = materia.elemEvaluados,
                    total = materia.elementosTotales,
                    color = Color(0xFF003C71),
                    modifier = Modifier.weight(1f),
                )

                val completionRate =
                    if (materia.elemEvaluados > 0) {
                        materia.elemCompletados.toFloat() / materia.elemEvaluados
                    } else {
                        0f
                    }

                StatCard(
                    title = "Tasa de aprobación",
                    percentage = completionRate,
                    color = Color(0xFF5A5C60),
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Progreso general del curso",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            LinearProgressIndicator(
                progress = materia.elemCompletados.toFloat() / materia.elementosTotales,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(8.dp)),
                color = Color(0xFF003C71),
                trackColor = Color(0xFFE0E0E0),
            )

            Text(
                text = "${(materia.elemCompletados.toFloat() / materia.elementosTotales * 100).toInt()}% completado",
                style = MaterialTheme.typography.bodyMedium,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                textAlign = TextAlign.End,
            )
        }
    }
}

@Composable
fun CircularProgressChart(
    progress: Float,
    text: String,
    label: String,
    progressColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(120.dp),
        ) {
            Canvas(modifier = Modifier.size(120.dp)) {
                val strokeWidth = 15.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val center = Offset(size.width / 2, size.height / 2)

                drawArc(
                    color = Color(0xFFE0E0E0),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft =
                        Offset(
                            center.x - radius,
                            center.y - radius,
                        ),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )

                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    topLeft =
                        Offset(
                            center.x - radius,
                            center.y - radius,
                        ),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )
            }

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color.DarkGray,
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: Int = 0,
    total: Int = 0,
    percentage: Float = 0f,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White,
            ),
        modifier =
            modifier
                .height(100.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (total > 0) {
                Text(
                    text = "$value de $total",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = color,
                )
            } else {
                Text(
                    text = "${(percentage * 100).toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = color,
                )
            }
        }
    }
}
