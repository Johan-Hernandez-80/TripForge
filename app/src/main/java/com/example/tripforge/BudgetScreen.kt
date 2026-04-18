package com.example.tripforge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tripforge.data.sampleBudgetCategories
import com.example.tripforge.data.sampleExpenses
import com.example.tripforge.data.sampleTripDetails
import com.example.tripforge.model.BudgetCategory
import com.example.tripforge.ui.components.BudgetCategoryButton
import com.example.tripforge.ui.components.ExpenseSummaryRow
import com.example.tripforge.ui.components.ProgressBar
import com.example.tripforge.ui.components.ScreenHeader
import com.example.tripforge.ui.components.SectionHeader

@Composable
fun BudgetScreen(
    onBack: () -> Unit = { /* TODO placeholder */ }
) {
    var showAddExpense by rememberSaveable { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf<BudgetCategory?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2563EB))
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    ScreenHeader(
                        title = "Budget",
                        subtitle = "Tokyo Adventure",
                        onBack = onBack
                    )

                    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text("Total Spent", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("$1,200", color = Color.White, style = MaterialTheme.typography.headlineLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("of $3,500", color = Color.White.copy(alpha = 0.8f))
                    }

                    ProgressBar(
                        progress = 0.34f,
                        barColor = Color.White,
                        trackColor = Color.White.copy(alpha = 0.25f)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Spent", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("$1,200", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                    Icon(Icons.Default.TrendingDown, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Remaining", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("$2,300", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(shape = RoundedCornerShape(20.dp)) {
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
                                                .background(Color(0xFFEFF6FF), RoundedCornerShape(12.dp)),
                                            contentAlignment = androidx.compose.ui.Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Folder, contentDescription = null, tint = Color(0xFF2563EB))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(item.category.label, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium, color = Color(0xFF111827))
                                    }
                                    Text("$${item.amount}", fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold, color = Color(0xFF111827))
                                }

                                ProgressBar(progress = item.progress, barColor = Color(0xFF2563EB))
                            }
                        }
                    }
                }

                Card(shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SectionHeader(title = "Recent Expenses")

                        sampleExpenses.forEach { expense ->
                            ExpenseSummaryRow(
                                title = expense.description,
                                amount = "$${expense.amount}",
                                icon = Icons.Default.AttachMoney,
                                tint = Color(0xFF2563EB),
                                backgroundTint = Color(0xFFEFF6FF)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        FloatingActionButton(
            onClick = { showAddExpense = true },
            containerColor = Color(0xFF2563EB),
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add expense", tint = Color.White)
        }

        if (showAddExpense) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.50f))
            ) {
                Card(
                    modifier = Modifier
                        .align(androidx.compose.ui.Alignment.BottomCenter)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
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
                            Text("Add Expense", style = MaterialTheme.typography.headlineSmall)
                            IconButton(onClick = { showAddExpense = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }

                        OutlinedTextField(
                            value = "",
                            onValueChange = { },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Description") },
                            placeholder = { Text("e.g., Lunch at restaurant") },
                            shape = RoundedCornerShape(14.dp)
                        )

                        OutlinedTextField(
                            value = "",
                            onValueChange = { },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Amount") },
                            placeholder = { Text("0.00") },
                            shape = RoundedCornerShape(14.dp)
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Category", style = MaterialTheme.typography.bodyMedium)
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                BudgetCategoryButton(
                                    label = "Transport",
                                    shortLabel = "TR",
                                    selected = selectedCategory == BudgetCategory.TRANSPORT,
                                    onClick = { selectedCategory = BudgetCategory.TRANSPORT }
                                )
                                BudgetCategoryButton(
                                    label = "Accommodation",
                                    shortLabel = "AC",
                                    selected = selectedCategory == BudgetCategory.ACCOMMODATION,
                                    onClick = { selectedCategory = BudgetCategory.ACCOMMODATION }
                                )
                                BudgetCategoryButton(
                                    label = "Food",
                                    shortLabel = "FD",
                                    selected = selectedCategory == BudgetCategory.FOOD,
                                    onClick = { selectedCategory = BudgetCategory.FOOD }
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                BudgetCategoryButton(
                                    label = "Activities",
                                    shortLabel = "AT",
                                    selected = selectedCategory == BudgetCategory.ACTIVITIES,
                                    onClick = { selectedCategory = BudgetCategory.ACTIVITIES }
                                )
                                BudgetCategoryButton(
                                    label = "Other",
                                    shortLabel = "OT",
                                    selected = selectedCategory == BudgetCategory.OTHER,
                                    onClick = { selectedCategory = BudgetCategory.OTHER }
                                )
                            }
                        }

                        Button(
                            onClick = { /* TODO placeholder */ },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                        ) {
                            Text("Add Expense", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}