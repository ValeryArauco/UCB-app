@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CompetencyElement(
    val id: Int,
    val title: String,
    val description: String,
    val saberesCompleted: Int,
    val saberesTotales: Int,
    val dueDate: String,
    val registeredDate: String,
    val registeredTime: String,
    val status: CompetencyStatus,
)

enum class CompetencyStatus {
    COMPLETED,
    IN_PROGRESS,
    PENDING,
}

@Composable
fun SIS321Screen() {
    val competencyElements =
        listOf(
            CompetencyElement(
                id = 1,
                title = "Elemento de competencia 1",
                description = "Marco técnico para la resolució",
                saberesCompleted = 4,
                saberesTotales = 4,
                dueDate = "30/10",
                registeredDate = "13/1",
                registeredTime = "2:48 PM",
                status = CompetencyStatus.COMPLETED,
            ),
            CompetencyElement(
                id = 2,
                title = "Elemento de competencia 1",
                description = "Elemento de competencia de ejemplo",
                saberesCompleted = 2,
                saberesTotales = 5,
                dueDate = "6/10",
                registeredDate = "13/2",
                registeredTime = "7:46 PM",
                status = CompetencyStatus.IN_PROGRESS,
            ),
            CompetencyElement(
                id = 3,
                title = "Elemento de competencia 2",
                description = "Otro elemento de competencia",
                saberesTotales = 3,
                saberesCompleted = 0,
                dueDate = "27/10",
                registeredDate = "",
                registeredTime = "",
                status = CompetencyStatus.PENDING,
            ),
        )

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
    ) {
        TopAppBarSection("ANALISIS DE ALGORITMOS")
        ProgressSection()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(competencyElements) { element ->
                CompetencyCard(element = element)
            }
        }
    }
}
@Composable
fun TopAppBarSection(
    title: String,
    onBackClick: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {

            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White,
                )
            }

        },
        colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
}

@Composable
fun ProgressSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            // Barra superior con flecha y título
            /*Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "[1-2024] SIS-321 SEGURIDAD DE SISTEMAS [Par. 1]",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))*/

            // Barra de progreso con colores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .weight(0.33f)
                            .height(6.dp)
                            .background(Color(0xFF4CAF50), RoundedCornerShape(3.dp)),
                )
                Box(
                    modifier =
                        Modifier
                            .weight(0.33f)
                            .height(6.dp)
                            .background(Color(0xFFFFC107), RoundedCornerShape(3.dp)),
                )
                Box(
                    modifier =
                        Modifier
                            .weight(0.33f)
                            .height(6.dp)
                            .background(Color(0xFF9E9E9E), RoundedCornerShape(3.dp)),
                )
            }
        }
    }
}

@Composable
fun CompetencyCard(element: CompetencyElement) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Círculo de progreso
            Box(
                modifier = Modifier.size(60.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    progress = { (element.saberesCompleted/element.saberesTotales).toFloat() },
                    modifier = Modifier.size(60.dp),
                    color =
                        when (element.status) {
                            CompetencyStatus.COMPLETED -> Color(0xFF4CAF50)
                            CompetencyStatus.IN_PROGRESS -> Color(0xFF2196F3)
                            CompetencyStatus.PENDING -> Color(0xFF9E9E9E)
                        },
                    strokeWidth = 4.dp,
                    trackColor = Color(0xFFE0E0E0),
                    strokeCap = StrokeCap.Round,
                )
                Text(
                    text = "${(element.saberesCompleted/element.saberesTotales * 100).toInt()}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                        when (element.status) {
                            CompetencyStatus.COMPLETED -> Color(0xFF4CAF50)
                            CompetencyStatus.IN_PROGRESS -> Color(0xFF2196F3)
                            CompetencyStatus.PENDING -> Color(0xFF9E9E9E)
                        },
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Contenido principal
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = element.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = element.description,
                    fontSize = 14.sp,
                    color = Color(0xFF757575),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Fechas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = "Vence ${element.dueDate}",
                            fontSize = 12.sp,
                            color = Color(0xFF757575),
                            fontWeight = FontWeight.Medium,
                        )
                    }

                    if (element.registeredDate.isNotEmpty()) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Registrado ${element.registeredDate} ${element.registeredTime}",
                                fontSize = 12.sp,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    } else {
                        Text(
                            text = "Pendiente",
                            fontSize = 12.sp,
                            color = Color(0xFFFF9800),
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }

            // Flecha indicadora
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Ver detalles",
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SIS321ScreenPreview() {
    MaterialTheme {
        SIS321Screen()
    }
}
