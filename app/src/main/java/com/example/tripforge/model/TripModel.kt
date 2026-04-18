package com.example.tripforge.model

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

data class TripSummary(
    val id: Int,
    val title: String,
    val location: String,
    val dateRange: String,
    val status: TripStatus,
    val daysLeft: Int? = null,
    val budgetSpent: Int = 0,
    val budgetTotal: Int = 0,
    val budgetProgress: Float = 0f
)

data class ActivityItem(
    val id: Int,
    val title: String,
    val location: String,
    val time: String = "",
    val description: String = "",
    val cost: Int? = null,
    val completed: Boolean = false
)

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

data class ExpenseItem(
    val id: Int,
    val description: String,
    val dateLabel: String,
    val amount: Int,
    val category: BudgetCategory
)