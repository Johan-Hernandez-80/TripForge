package com.example.tripforge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.tripforge.data.TripRepository
import com.example.tripforge.data.sampleBudgetCategories
import com.example.tripforge.data.sampleExpenses
import com.example.tripforge.model.BudgetCategory
import com.example.tripforge.model.ExpenseItem
import com.example.tripforge.ui.components.CategoryButton
import com.example.tripforge.ui.components.ExpenseSummaryRow
import com.example.tripforge.ui.components.ProgressBar
import com.example.tripforge.ui.components.ScreenHeader
import com.example.tripforge.ui.components.SectionHeader
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun BudgetScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { TripRepository(context) }
    val tripId = "sample_trip_id"
    
    var showAddExpense by rememberSaveable { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf<BudgetCategory?>(null) }
    var expenseDescription by rememberSaveable { mutableStateOf("") }
    var expenseAmount by rememberSaveable { mutableStateOf("") }

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
                        subtitle = "Tokyo Adventure",
                        onBack = onBack
                    )

                    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text("Total Spent", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("$1,200", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("of $3,500", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                    }

                    ProgressBar(
                        progress = 0.34f,
                        barColor = MaterialTheme.colorScheme.onPrimary,
                        trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Spent", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("$1,200", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineSmall)
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                    Icon(Icons.Default.TrendingDown, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Remaining", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("$2,300", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineSmall)
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SectionHeader(title = "By Category")

                        sampleBudgetCategories.forEach { item ->
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
                                            contentAlignment = androidx.compose.ui.Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Folder, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(item.category.label, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Text("$${item.amount}", fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                }

                                ProgressBar(progress = item.progress, barColor = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SectionHeader(title = "Recent Expenses")

                        sampleExpenses.forEach { expense ->
                            ExpenseSummaryRow(
                                title = expense.description,
                                amount = "$${expense.amount}",
                                icon = Icons.Default.AttachMoney,
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

            Spacer(modifier = Modifier.height(100.dp))
        }

        FloatingActionButton(
            onClick = { showAddExpense = true },
            containerColor = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add expense", tint = MaterialTheme.colorScheme.onTertiary)
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
                        .align(androidx.compose.ui.Alignment.BottomCenter)
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
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Text("Add Expense", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
                            IconButton(onClick = { showAddExpense = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
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

                        OutlinedTextField(
                            value = expenseAmount,
                            onValueChange = { expenseAmount = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Amount") },
                            placeholder = { Text("0.00") },
                            shape = RoundedCornerShape(14.dp)
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Category", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
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
                                        onClick = { selectedCategory = BudgetCategory.ACCOMMODATION }
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

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
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
