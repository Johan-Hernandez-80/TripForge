package com.example.tripforge.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tripforge.R


    @Composable
    fun FeaturedTripCard() {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.tokyo),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                        color = Color(0xFFF97316),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("30 days left", color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), style = MaterialTheme.typography.labelMedium)
                    }
                    Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                        Text("Apr 14 - Apr 24", color = Color.White.copy(0.9f), style = MaterialTheme.typography.bodySmall)
                        Text("Tokyo Adventure", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tokyo, Japan", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                }
            }
        }
    }

    @Composable
    fun TripCardBig(title: String, location: String, imageRes: Int, modifier: Modifier = Modifier) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(location, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }

    @Composable
    fun TripCardLong(location: String, description: String, imageRes: Int) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(location, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(description, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1)
                }
            }
        }
    }

