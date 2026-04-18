package com.example.tripforge.data

import com.example.tripforge.model.*

val sampleTrips = listOf(
    TripSummary(
        id = 1,
        title = "Tokyo Adventure",
        location = "Tokyo, Japan",
        dateRange = "Apr 14 - Apr 24",
        status = TripStatus.UPCOMING,
        daysLeft = 30,
        budgetSpent = 1200,
        budgetTotal = 3500,
        budgetProgress = 0.34f
    ),
    TripSummary(
        id = 2,
        title = "Paris Romance",
        location = "Paris, France",
        dateRange = "May 9 - May 16",
        status = TripStatus.UPCOMING,
        daysLeft = 45,
        budgetSpent = 900,
        budgetTotal = 2800,
        budgetProgress = 0.32f
    )
)

val sampleTripDetails = sampleTrips.first()

val sampleDayPlans = listOf(
    DayPlan(
        day = 15,
        dateLabel = "Wednesday, April 15",
        activities = listOf(
            ActivityItem(
                id = 1,
                title = "Visit Senso-ji Temple",
                location = "Asakusa, Tokyo",
                time = "09:00",
                description = "Explore the oldest temple in Tokyo"
            ),
            ActivityItem(
                id = 2,
                title = "Shibuya Crossing Experience",
                location = "Shibuya, Tokyo",
                time = "14:00",
                description = "Walk across the famous crossing"
            )
        )
    ),
    DayPlan(
        day = 16,
        dateLabel = "Thursday, April 16",
        activities = listOf(
            ActivityItem(
                id = 3,
                title = "Sushi Dinner at Tsukiji",
                location = "Tsukiji Market, Tokyo",
                description = "Fresh sushi experience",
                cost = 150
            )
        )
    )
)

val sampleBudgetCategories = listOf(
    BudgetCategorySummary(BudgetCategory.TRANSPORT, 850, 0.70f),
    BudgetCategorySummary(BudgetCategory.ACCOMMODATION, 350, 0.35f)
)

val sampleExpenses = listOf(
    ExpenseItem(
        id = 1,
        description = "Flight Tickets",
        dateLabel = "Mar 14",
        amount = 800,
        category = BudgetCategory.TRANSPORT
    )
)

val samplePackingList = listOf(
    PackingItem(
        id = 1,
        name = "Passport",
        category = "Documents",
        checked = true
    ),
    PackingItem(
        id = 2,
        name = "Travel Insurance",
        category = "Documents",
        checked = true
    ),
    PackingItem(
        id = 3,
        name = "JR Rail Pass",
        category = "Documents",
        checked = false
    ),
    PackingItem(
        id = 4,
        name = "T-shirts (5)",
        category = "Clothes",
        checked = false
    ),
    PackingItem(
        id = 5,
        name = "Jeans (2)",
        category = "Clothes",
        checked = false
    ),

    PackingItem(
        id = 6,
        name = "Jacket",
        category = "Clothes",
        checked = true
    )
)
