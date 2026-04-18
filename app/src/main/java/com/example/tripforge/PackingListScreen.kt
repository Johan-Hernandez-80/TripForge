package com.example.tripforge

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import com.example.tripforge.ui.components.BottomNav

data class PackingItem(
    val id: Int,
    val name: String,
    val category: String,
    val checked: Boolean
)

@Composable
fun PackingListScreen() {

    var selectedTab by remember { mutableStateOf("trips") }

    var items by remember {
        mutableStateOf(
            listOf(
                PackingItem(1, "Passport", "Documents", true),
                PackingItem(2, "Travel Insurance", "Documents", true),
                PackingItem(3, "JR Rail Pass", "Documents", false),
                PackingItem(4, "T-shirts (5)", "Clothes", false),
                PackingItem(5, "Jeans (2)", "Clothes", false),
                PackingItem(6, "Jacket", "Clothes", true)
            )
        )
    }

    var showDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemCategory by remember { mutableStateOf("Documents") }

    fun toggle(id: Int) {
        items = items.map {
            if (it.id == id) it.copy(checked = !it.checked) else it
        }
    }

    fun addItem() {
        if (newItemName.isNotBlank()) {
            val newId = (items.maxOfOrNull { it.id } ?: 0) + 1
            items = items + PackingItem(
                id = newId,
                name = newItemName,
                category = newItemCategory,
                checked = false
            )
            newItemName = ""
            newItemCategory = "Documents"
            showDialog = false
        }
    }

    val totalChecked = items.count { it.checked }

    Scaffold(
        bottomBar = {
            BottomNav(selected = selectedTab, onSelect = { selectedTab = it })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add item", tint = Color.White)
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

            Text(
                "Packing List",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = totalChecked.toFloat() / items.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "$totalChecked/${items.size} packed",
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(16.dp))

            CategorySection(
                "Documents",
                items.filter { it.category == "Documents" },
                ::toggle
            )

            CategorySection(
                "Clothes",
                items.filter { it.category == "Clothes" },
                ::toggle
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = { Text("Item name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    Text("Category")

                    Spacer(Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Documents", "Clothes").forEach { category ->
                            FilterChip(
                                selected = newItemCategory == category,
                                onClick = { newItemCategory = category },
                                label = { Text(category) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { addItem() }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CategorySection(
    title: String,
    items: List<PackingItem>,
    toggle: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
    ) {

        Text(
            title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )

        items.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { toggle(it.id) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            if (it.checked)
                                MaterialTheme.colorScheme.primary
                            else
                                Color.Transparent,
                            CircleShape
                        )
                )

                Spacer(Modifier.width(12.dp))

                Text(it.name)
            }
        }
    }
}