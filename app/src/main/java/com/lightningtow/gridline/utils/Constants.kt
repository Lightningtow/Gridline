package com.lightningtow.gridline.utils

//import androidx.compose.material.icons.filled.PointScan

import com.lightningtow.gridline.R


object Constants {
    val TEST_11: String = "spotify:playlist:1KJW3gIz3EGTviDLDwb7xa"
    val TESTLIST: String = "spotify:playlist:5NKYetvb0UeaSDcnjs7SB7"
    val TESTLIST2: String = "spotify:playlist:55iEYHdJtZLoTErFPMSju8"
    val PURGELIST: String = "spotify:playlist:3gWBGiJmlvbVJaS0CSY2Vg"
    val ROADPURGE: String = "spotify:playlist:1MYRwRHs71GWJNk1HunSAz"
    val GIANT_TEST: String = "spotify:playlist:5HB0nTfYl0bC1yvpxvHJjh"
    val RK_TEST: String = "spotify:playlist:5PaNURPIq7G4kEpO6aEKp1"

    val BottomNavItems = listOf(
//        BottomNavItem(
//            label = "Home",
//            icon = Icons.Filled.Home,
//            route = "home"
//        ),
        BottomNavItem(
            label = "Login",
            icon = R.drawable.account_circle,
            route = "login"
        ),
        BottomNavItem(
            label = "Idk",
            icon = R.drawable.settings_backup_restore,
            route = "idk"
        ),
        BottomNavItem(
            label = "Player",
            icon = R.drawable.home,
            route = "Player"
        ),

        BottomNavItem(
            label = "Purge",
            icon = R.drawable.knife,
            route = "purge"
        ),

        BottomNavItem(
            label = "Shuffle",
            icon = R.drawable.shuffle,
            route = "shuffle"
        ),
    )
}
// icon credits:
// shuffle https://iconscout.com/contributors/benjamin-j-sperry
// knife https://iconscout.com/contributors/phosphoricons
// solid knife https://iconscout.com/contributors/icon-click

// the rest are from https://fonts.google.com/icons
data class BottomNavItem(
    val label: String,
    val icon: Int,
    val route: String,
)