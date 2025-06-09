package com.example.ucbapp.dashboard

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ucbapp.R

data class CompetencyElement(
    val title: String,
    val code: String,
    val date: String,
    val isCompleted: Boolean = false,
)

data class TodayItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconColor: Color,
    val backgroundColor: Color,
)

@Composable
fun DashboardUI() {
    var showLogoutMenu by remember { mutableStateOf(false) }

    val competencyElements =
        listOf(
            CompetencyElement("Elemento de competencia 1", "SIS-125", "13/05/25"),
            CompetencyElement("Elemento de competencia 2", "SIS-122", "13/05/25"),
            CompetencyElement("Elemento de competencia 3", "SIS-122", "13/05/25"),
        )

    val todayItems =
        listOf(
            TodayItem(
                "Anatomía humana",
                "2 Elementos vencidos",
                Icons.Default.Star,
                Color(0xFF6366F1),
                Color(0xFFEEF2FF),
            ),
            TodayItem(
                "Anatomía humana",
                "Vence hoy",
                Icons.Default.Star,
                Color(0xFFEF4444),
                Color(0xFFFEF2F2),
            ),
            TodayItem(
                "Bandeja de entrada",
                "3 Mensajes no leídos",
                Icons.Default.Email,
                Color(0xFF10B981),
                Color(0xFFF0FDF4),
            ),
        )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                Color(0xFFF8FAFC),
                                Color(0xFFE2E8F0),
                            ),
                    ),
                ),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                HeaderSection(
                    userName = "Jhon Doe",
                    showLogoutMenu = showLogoutMenu,
                    onProfileClick = { showLogoutMenu = !showLogoutMenu },
                    onLogoutClick = {
                        showLogoutMenu = false
                    },
                )
            }

            item {
                PendingProgressSection(competencyElements)
            }

            // Today Section
            item {
                TodaySection(todayItems)
            }
        }
        BottomNavigationSection(
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
fun HeaderSection(
    userName: String,
    showLogoutMenu: Boolean,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF64748B),
                    fontSize = 16.sp,
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                )
            }

            Box {
                Box(
                    modifier =
                        Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors =
                                        listOf(
                                            Color(0xFF6366F1),
                                            Color(0xFF8B5CF6),
                                        ),
                                ),
                            ).clickable { onProfileClick() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil de usuario",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp),
                    )
                }

                this@Row.AnimatedVisibility(
                    visible = showLogoutMenu,
                    enter =
                        scaleIn(
                            animationSpec =
                                spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessHigh,
                                ),
                        ) + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                ) {
                    Card(
                        modifier =
                            Modifier
                                .offset(x = (-80).dp, y = 60.dp)
                                .shadow(8.dp, RoundedCornerShape(12.dp)),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = Color.White,
                            ),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Row(
                                modifier =
                                    Modifier
                                        .clickable { onLogoutClick() }
                                        .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = "Cerrar sesión",
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(20.dp),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Cerrar sesión",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFEF4444),
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingProgressSection(elements: List<CompetencyElement>) {
    Text(
        text = "Avance pendiente",
        style = MaterialTheme.typography.titleLarge,
        color = Color(0xFF1E293B),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )
    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { elements.count() },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 16.dp),
        preferredItemWidth = 186.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) { index ->
        val element = elements[index]
        CompetencyCard(
            element = element
        )
    }
}

@Composable
fun CompetencyCard(
    element: CompetencyElement,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.75f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = element.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = element.code,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = element.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "Ver",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
}}

@Composable
fun TodaySection(items: List<TodayItem>) {
    Column {
        Text(
            text = "Hoy",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF1E293B),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Timeline Layout
        Column {
            items.forEachIndexed { index, item ->
                TimelineItemCard(
                    item = item,
                    isLast = index == items.size - 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun TimelineItemCard(
    item: TodayItem,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(60.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(item.backgroundColor),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.iconColor,
                    modifier = Modifier.size(24.dp),
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(Color(0xFFE2E8F0))
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, bottom = if (isLast) 0.dp else 16.dp)
                .clickable { }
                .shadow(1.dp, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF8FAFC),
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B),
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Versión alternativa con estilo más similar a la imagen
@Composable
fun TimelineItemCardCompact(
    item: TodayItem,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(56.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(item.backgroundColor.copy(alpha = 0.2f))
                    .border(2.dp, item.backgroundColor, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.iconColor,
                    modifier = Modifier.size(20.dp),
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(50.dp)
                        .background(Color(0xFFE2E8F0))
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, bottom = if (isLast) 0.dp else 20.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF1E293B),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF64748B),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationSection(modifier: Modifier = Modifier) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        tonalElevation = 0.dp,
        modifier = modifier,
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Inicio",
                )
            },
            label = { Text("Inicio") },
            selected = true,
            onClick = {
            },
            colors =
                NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                ),
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Mis Materias",
                )
            },
            label = { Text("Mis materias") },
            selected = false,
            onClick = {
            },
            colors =
                NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                ),
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                )
            },
            label = { Text("Notificaciones") },
            selected = false,
            onClick = {
            },
            colors =
                NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                ),
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Mensajes",
                )
            },
            label = { Text("Mensajes") },
            selected = false,
            onClick = {
            },
            colors =
                NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                ),
        )
    }
}
@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    MaterialTheme {
        DashboardUI()
    }
}