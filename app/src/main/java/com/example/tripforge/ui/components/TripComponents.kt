package com.example.tripforge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import com.example.tripforge.model.*

@Composable
fun ScreenHeader(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(50))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF374151))
            }
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column {
            Text(
                title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )
            if (!subtitle.isNullOrBlank()) {
                Text(subtitle, fontSize = 13.sp, color = Color(0xFF6B7280))
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
        if (actionText != null && onAction != null) {
            TextButton(onClick = onAction, contentPadding = PaddingValues(0.dp)) {
                Text(actionText)
            }
        }
    }
}

@Composable
fun ProgressBar(
    progress: Float,
    barColor: Color,
    trackColor: Color = Color(0xFFE5E7EB),
    modifier: Modifier = Modifier
) {
    val safeProgress = progress.coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(trackColor, RoundedCornerShape(50))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(safeProgress)
                .background(barColor, RoundedCornerShape(50))
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector,
    iconTint: Color,
    backgroundTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(backgroundTint, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, fontSize = 13.sp, color = Color(0xFF6B7280))
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                value,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )
            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, fontSize = 12.sp, color = Color(0xFF6B7280))
            }
        }
    }
}

@Composable
fun TripTabChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background = if (selected) Color(0xFF2563EB) else Color.White
    val contentColor = if (selected) Color.White else Color(0xFF4B5563)

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = background),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(50),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(text, color = contentColor, fontSize = 13.sp)
    }
}

@Composable
fun TripPreviewCard(
    title: String,
    location: String,
    dateRange: String,
    statusText: String,
    badgeColor: Color,
    budgetText: String? = null,
    budgetSpentText: String? = null,
    progress: Float? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(22.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFF1D4ED8))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(badgeColor, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(statusText, color = Color.White, fontSize = 11.sp)
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(dateRange, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                    Text(
                        title,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(location, fontSize = 13.sp, color = Color(0xFF4B5563))
                    if (budgetText != null) {
                        Text(
                            budgetText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                    }
                }

                if (progress != null && budgetSpentText != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    ProgressBar(progress = progress, barColor = Color(0xFF14B8A6))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(budgetSpentText, fontSize = 12.sp, color = Color(0xFF6B7280))
                }
            }
        }
    }
}

@Composable
fun TripBulletItem(
    title: String,
    location: String,
    time: String,
    dateLabel: String? = null
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFFF3F4F6), RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color(0xFF9CA3AF), RoundedCornerShape(50))
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF111827))
            Text(location, fontSize = 13.sp, color = Color(0xFF6B7280))
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(time, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2563EB))
            if (!dateLabel.isNullOrBlank()) {
                Text(dateLabel, fontSize = 11.sp, color = Color(0xFF6B7280))
            }
        }
    }
}

@Composable
fun TripDaySection(
    day: Int,
    dateLabel: String,
    activities: List<ActivityItem>
) {
    Column {
        Row(
            modifier = Modifier.padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFF2563EB), RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Text(day.toString(), color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                dateLabel,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 21.dp)
                .border(2.dp, Color(0xFFE5E7EB), RoundedCornerShape(0.dp))
                .padding(start = 18.dp)
        ) {
            activities.forEachIndexed { index, activity ->
                TripActivityCard(activity = activity)
                if (index != activities.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun TripActivityCard(activity: ActivityItem) {
    Card(shape = RoundedCornerShape(18.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = activity.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF111827),
                    modifier = Modifier.weight(1f)
                )
                if (activity.time.isNotBlank()) {
                    Text(
                        activity.time,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2563EB)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text("Location: ${activity.location}", fontSize = 13.sp, color = Color(0xFF6B7280))

            if (activity.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(activity.description, fontSize = 13.sp, color = Color(0xFF6B7280))
            }

            if (activity.cost != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Cost: \$${activity.cost}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0F766E)
                )
            }
        }
    }
}

@Composable
fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (leadingIcon != null) {
                Icon(leadingIcon, contentDescription = null, tint = Color(0xFF111827))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF111827))
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            shape = RoundedCornerShape(14.dp)
        )
    }
}

@Composable
fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) Color(0xFF2563EB) else Color(0xFFE5E7EB)
    val backgroundColor = if (selected) Color(0xFFEFF6FF) else Color.White

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(14.dp))
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(vertical = 14.dp)
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(label, color = Color(0xFF374151))
        }
    }
}

@Composable
fun ExpenseSummaryRow(
    title: String,
    amount: String,
    icon: ImageVector,
    tint: Color,
    backgroundTint: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(backgroundTint, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF111827))
        }
        Text(amount, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
    }
}

@Composable
fun BudgetCategoryButton(
    label: String,
    shortLabel: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) Color(0xFF2563EB) else Color(0xFFE5E7EB)
    val background = if (selected) Color(0xFFEFF6FF) else Color.White

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = background),
        contentPadding = PaddingValues(12.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                shortLabel,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 11.sp, color = Color(0xFF374151))
        }
    }
}