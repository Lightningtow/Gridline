package com.lightningtow.gridline.utils

//import androidx.compose.material.icons.filled.PointScan

import com.lightningtow.gridline.R


object Constants {
    val DEFAULT_MISSING: String = "https://picsum.photos/300/300"

    val TEST_11: String = "spotify:playlist:1KJW3gIz3EGTviDLDwb7xa"
    val TESTLIST: String = "spotify:playlist:5NKYetvb0UeaSDcnjs7SB7"
    val TESTLIST2: String = "spotify:playlist:55iEYHdJtZLoTErFPMSju8"
    val GIANT_TEST: String = "spotify:playlist:5HB0nTfYl0bC1yvpxvHJjh"
    val RK_TEST: String = "spotify:playlist:5PaNURPIq7G4kEpO6aEKp1"

    val PURGELIST: String = "spotify:playlist:3gWBGiJmlvbVJaS0CSY2Vg"
    val ROADPURGE: String = "spotify:playlist:1MYRwRHs71GWJNk1HunSAz"

    val OMNI: String = "spotify:playlist:3PXFZxy8QdBmvFHCYyErw3"

    val ROADKILL: String = "spotify:playlist:6o3HI8fSJrWEeZmhkCqSeZ"
    val RK_REPO: String = "spotify:playlist:5ts96D5tgtlxwXSWKwGvsm"
    val RK_ARCHIVE: String = "spotify:playlist:0HBq6MvLLiQBY9hTFw0JVE"

    val RK_BUNDLE: List<String> = listOf(ROADKILL, RK_REPO, RK_ARCHIVE)


    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Broadcasts",
            icon = R.drawable.api,
            route = "broadcasts"
        ),
//        BottomNavItem(
//            label = "Login",
//            icon = R.drawable.account_circle,
//            route = "login"
//        ),

        BottomNavItem(
            label = "Home",
            icon = R.drawable.home,
            route = "home"
        ),
        BottomNavItem(
            label = "Player",
            icon = R.drawable.baseline_play_circle_24,
            route = "player"
        ),

//        BottomNavItem(
//            label = "Purge",
//            icon = R.drawable.knife,
//            route = "purge"
//        ),
        BottomNavItem(
            label = "Idk",
            icon = R.drawable.settings_backup_restore,
            route = "idk"
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