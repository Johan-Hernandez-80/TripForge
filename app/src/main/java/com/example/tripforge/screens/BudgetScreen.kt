package com.example.tripforge.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.tripforge.data.TripRepository
import com.example.tripforge.model.BudgetCategory
import com.example.tripforge.model.BudgetCategorySummary
import com.example.tripforge.model.ExpenseItem
import com.example.tripforge.model.TripSummary
import com.example.tripforge.ui.components.CategoryButton
import com.example.tripforge.ui.components.ExpenseSummaryRow
import com.example.tripforge.ui.components.MoneyField
import com.example.tripforge.ui.components.ProgressBar
import com.example.tripforge.ui.components.ScreenHeader
import com.example.tripforge.ui.components.SectionHeader
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun BudgetScreen(
    tripId: String,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { TripRepository(context) }

    val trips by repository.trips.collectAsState(initial = emptyList())
    val trip = trips.find { it.id == tripId }

    var showAddExpense by rememberSaveable { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf<BudgetCategory?>(null) }
    var expenseDescription by rememberSaveable { mutableStateOf("") }
    var expenseAmount by rememberSaveable { mutableStateOf("") }

    if (trip == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val totalSpent = trip.expenses.sumOf { it.amount }
    val remaining = (trip.budgetTotal - totalSpent).coerceAtLeast(0)
    val progress = if (trip.budgetTotal > 0) totalSpent.toFloat() / trip.budgetTotal else 0f

    val categorySummaries = BudgetCategory.entries.map { category ->
        val categoryAmount = trip.expenses.filter { it.category == category }.sumOf { it.amount }
        val categoryProgress = if (totalSpent > 0) categoryAmount.toFloat() / totalSpent else 0f
        BudgetCategorySummary(category, categoryAmount, categoryProgress)
    }.filter { it.amount > 0 }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    ScreenHeader(
                        title = "Budget",
                        subtitle = trip.title,
                        onBack = onBack
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Total Spent",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "$totalSpent",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "of ${trip.budgetTotal}",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }

                    ProgressBar(
                        progress = progress.coerceIn(0f, 1f),
                        barColor = MaterialTheme.colorScheme.onPrimary,
                        trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary.copy(
                                    alpha = 0.12f
                                )
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.TrendingUp,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Spent",
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    "$totalSpent",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary.copy(
                                    alpha = 0.12f
                                )
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.TrendingDown,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Remaining",
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    "$remaining",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (categorySummaries.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SectionHeader(title = "By Category")

                            categorySummaries.forEach { item ->
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .background(
                                                        MaterialTheme.colorScheme.primaryContainer,
                                                        RoundedCornerShape(12.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                val icon = when (item.category) {
                                                    BudgetCategory.TRANSPORT -> Icons.Default.DirectionsBus
                                                    BudgetCategory.ACCOMMODATION -> Icons.Default.Hotel
                                                    BudgetCategory.FOOD -> Icons.Default.Restaurant
                                                    BudgetCategory.ACTIVITIES -> Icons.Default.ConfirmationNumber
                                                    BudgetCategory.OTHER -> Icons.Default.Category
                                                }
                                                Icon(
                                                    icon,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                item.category.label,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        Text(
                                            "${item.amount}",
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    ProgressBar(
                                        progress = item.progress,
                                        barColor = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SectionHeader(title = "Recent Expenses")

                        if (trip.expenses.isEmpty()) {
                            Text(
                                "No expenses yet. Tap + to add one.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            trip.expenses.reversed().forEach { expense ->
                                ExpenseSummaryRow(
                                    title = expense.description,
                                    amount = "${expense.amount}",
                                    icon = when (expense.category) {
                                        BudgetCategory.TRANSPORT -> Icons.Default.DirectionsBus
                                        BudgetCategory.ACCOMMODATION -> Icons.Default.Hotel
                                        BudgetCategory.FOOD -> Icons.Default.Restaurant
                                        BudgetCategory.ACTIVITIES -> Icons.Default.ConfirmationNumber
                                        BudgetCategory.OTHER -> Icons.Default.Category
                                    },
                                    tint = MaterialTheme.colorScheme.primary,
                                    backgroundTint = MaterialTheme.colorScheme.primaryContainer,
                                    onDelete = {
                                        scope.launch {
                                            repository.deleteExpense(tripId, expense.id)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        FloatingActionButton(
            onClick = { showAddExpense = true },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add expense",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        if (showAddExpense) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.50f))
                    .clickable { showAddExpense = false }
            ) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .clickable(enabled = false) { },
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Add Expense",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            IconButton(onClick = { showAddExpense = false }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        OutlinedTextField(
                            value = expenseDescription,
                            onValueChange = { expenseDescription = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Description") },
                            placeholder = { Text("e.g., Lunch at restaurant") },
                            shape = RoundedCornerShape(14.dp)
                        )

                        MoneyField(
                            label = "Amount",
                            value = expenseAmount,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() }) expenseAmount = newValue
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                "Category",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    CategoryButton(
                                        label = "Transport",
                                        icon = Icons.Default.DirectionsBus,
                                        selected = selectedCategory == BudgetCategory.TRANSPORT,
                                        onClick = { selectedCategory = BudgetCategory.TRANSPORT }
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    CategoryButton(
                                        label = "Accommodation",
                                        icon = Icons.Default.Hotel,
                                        selected = selectedCategory == BudgetCategory.ACCOMMODATION,
                                        onClick = {
                                            selectedCategory = BudgetCategory.ACCOMMODATION
                                        }
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    CategoryButton(
                                        label = "Food",
                                        icon = Icons.Default.Restaurant,
                                        selected = selectedCategory == BudgetCategory.FOOD,
                                        onClick = { selectedCategory = BudgetCategory.FOOD }
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    CategoryButton(
                                        label = "Activities",
                                        icon = Icons.Default.ConfirmationNumber,
                                        selected = selectedCategory == BudgetCategory.ACTIVITIES,
                                        onClick = { selectedCategory = BudgetCategory.ACTIVITIES }
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    CategoryButton(
                                        label = "Other",
                                        icon = Icons.Default.Category,
                                        selected = selectedCategory == BudgetCategory.OTHER,
                                        onClick = { selectedCategory = BudgetCategory.OTHER }
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                        Button(
                            onClick = {
                                if (expenseDescription.isNotBlank() && expenseAmount.isNotBlank() && selectedCategory != null) {
                                    scope.launch {
                                        val newExpense = ExpenseItem(
                                            id = UUID.randomUUID().toString(),
                                            description = expenseDescription,
                                            amount = expenseAmount.toIntOrNull() ?: 0,
                                            category = selectedCategory!!,
                                            dateLabel = "Today"
                                        )
                                        repository.addExpense(tripId, newExpense)
                                        showAddExpense = false
                                        expenseDescription = ""
                                        expenseAmount = ""
                                        selectedCategory = null
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Add Expense", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}
