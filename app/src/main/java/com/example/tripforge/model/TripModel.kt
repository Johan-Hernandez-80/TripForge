package com.example.tripforge.model

import kotlinx.serialization.Serializable
import java.time.LocalDate

enum class TripTab(val label: String) {
    ALL("All"),
    UPCOMING("Upcoming"),
    ONGOING("Ongoing"),
    COMPLETE("Complete")
}

enum class TripStatus(val label: String) {
    UPCOMING("Upcoming"),
    ONGOING("Ongoing"),
    COMPLETE("Complete")
}

enum class BottomTab {
    HOME, TRIPS, MAP, PROFILE
}

enum class BudgetCategory(val label: String) {
    TRANSPORT("Transport"),
    ACCOMMODATION("Accommodation"),
    FOOD("Food"),
    ACTIVITIES("Activities"),
    OTHER("Other")
}

@Serializable
data class TripSummary(
    val id: String,
    val title: String,
    val location: String,
    val startDate: String,
    val endDate: String,
    val status: TripStatus = TripStatus.UPCOMING,
    val budgetTotal: Int = 0,
    val budgetSpent: Int = 0,
    val imageUrl: String = "",
    val expenses: List<ExpenseItem> = emptyList(),
    val itinerary: List<DayPlan> = emptyList(),
    val packingList: List<PackingItem> = emptyList(),
    val userId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class ActivityItem(
    val id: String,
    val title: String,
    val location: String,
    val time: String = "",
    val description: String = "",
    val cost: Int? = null,
    val completed: Boolean = false
)

@Serializable
data class DayPlan(
    val day: Int,
    val dateLabel: String,
    val activities: List<ActivityItem>
)

data class BudgetCategorySummary(
    val category: BudgetCategory,
    val amount: Int,
    val progress: Float
)

@Serializable
data class ExpenseItem(
    val id: String,
    val description: String,
    val dateLabel: String,
    val amount: Int,
    val category: BudgetCategory
)

@Serializable
data class PackingItem(
    val id: Int,
    val name: String,
    val category: String,
    val checked: Boolean
)
