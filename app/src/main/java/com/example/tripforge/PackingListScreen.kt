package com.example.tripforge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.tripforge.data.TripRepository
import com.example.tripforge.data.samplePackingList
import com.example.tripforge.model.BudgetCategory
import com.example.tripforge.model.PackingItem
import com.example.tripforge.ui.components.CategoryButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackingListScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { TripRepository(context) }
    val tripId = "sample_trip_id"

    var items by remember {
        mutableStateOf(
            samplePackingList.toMutableList()
        )
    }

    var selectedCategory by rememberSaveable { mutableStateOf<BudgetCategory?>(null) }
    var itemName by rememberSaveable { mutableStateOf("") }


    var showAddItem by remember { mutableStateOf(false) }

    fun toggle(id: Int) {
        items = items.map {
            if (it.id == id) it.copy(checked = !it.checked) else it
        }.toMutableList()
        
        scope.launch {
            repository.togglePackingItem(tripId, id)
        }
    }

    fun addItem() {
        if (itemName.isNotBlank()) {
            val newId = (items.maxOfOrNull { it.id } ?: 0) + 1
            val categoryLabel = selectedCategory?.label ?: "Other"
            val newItem = PackingItem(
                id = newId,
                name = itemName,
                category = categoryLabel,
                checked = false
            )
            items.add(newItem)

            scope.launch {
                repository.addPackingItem(tripId, newItem)
            }

            itemName = ""
            selectedCategory = null
            showAddItem = false
        }
    }

    val totalChecked = items.count { it.checked }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Packing List") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddItem = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add item",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {

            Spacer(Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { if (items.isEmpty()) 0f else totalChecked.toFloat() / items.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "$totalChecked/${items.size} packed",
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            val categories = listOf("Documents", "Clothes", "Electronics", "Personal", "Other")
            categories.forEach { category ->
                val categoryItems = items.filter { it.category == category }
                if (categoryItems.isNotEmpty()) {
                    CategorySection(
                        category,
                        categoryItems,
                        ::toggle,
                        onDelete = { itemId ->
                            items.removeAll { it.id == itemId }
                            scope.launch {
                                repository.deletePackingItem(tripId, itemId)
                            }
                        }
                    )
                }
            }
        }
    }

    if (showAddItem) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.50f))
                .clickable { showAddItem = false }
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
                        Text(
                            "Add Item",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { showAddItem = false }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Item Name") },
                        placeholder = { Text("e.g., Baggy Pants") },
                        shape = RoundedCornerShape(14.dp)
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
                                    label = "Clothes",
                                    icon = Icons.Default.DryCleaning,
                                    selected = selectedCategory == BudgetCategory.TRANSPORT, 
                                    onClick = { selectedCategory = BudgetCategory.TRANSPORT }
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                CategoryButton(
                                    label = "Documents",
                                    icon = Icons.Default.Folder,
                                    selected = selectedCategory == BudgetCategory.ACCOMMODATION,
                                    onClick = { selectedCategory = BudgetCategory.ACCOMMODATION }
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                CategoryButton(
                                    label = "Electronics",
                                    icon = Icons.Default.Smartphone,
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
                                    label = "Personal",
                                    icon = Icons.Default.Person,
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
                        onClick = { addItem() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Add Item", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySection(
    title: String,
    items: List<PackingItem>,
    toggle: (Int) -> Unit,
    onDelete: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
    ) {

        Text(
            title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { toggle(item.id) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            if (item.checked)
                                MaterialTheme.colorScheme.primary
                            else
                                Color.Transparent,
                            CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = if (item.checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    item.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Row {
                    IconButton(onClick = { /* TODO: Edit */ }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = { onDelete(item.id) }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
