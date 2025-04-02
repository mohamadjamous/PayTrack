package com.app.paytrack.model

import com.app.paytrack.R

val navigationItems = listOf(
    NavigationItem(
        title = "Home",
        icon = R.drawable.home,
        route = Screen.Home
    ),
    NavigationItem(
        title = "Profile",
        icon = R.drawable.person,
        route = Screen.Profile
    ),
    NavigationItem(
        title = "Charts",
        icon = R.drawable.charts,
        route = Screen.Charts
    )
)