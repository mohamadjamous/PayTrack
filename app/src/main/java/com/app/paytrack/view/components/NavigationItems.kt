package com.app.paytrack.view.components

import com.app.paytrack.R
import com.app.paytrack.model.NavigationItem
import com.app.paytrack.model.Screen

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