package com.lightningtow.gridline.utils

//import androidx.compose.material.icons.filled.PointScan

import android.graphics.drawable.VectorDrawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.material.icons.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.lightningtow.gridline.R
import com.lightningtow.gridline.ui.components.BottomNavItem
import com.lightningtow.gridline.R.drawable



object Constants {
    val TEST_11: String = "spotify:playlist:1KJW3gIz3EGTviDLDwb7xa"
    val TESTLIST: String = "spotify:playlist:5NKYetvb0UeaSDcnjs7SB7"
    val PURGELIST: String = "spotify:playlist:3gWBGiJmlvbVJaS0CSY2Vg"
    val GIANT_TEST: String = "spotify:playlist:5HB0nTfYl0bC1yvpxvHJjh"
    val RK_TEST: String = "spotify:playlist:5PaNURPIq7G4kEpO6aEKp1"

//    val imageVector = ImageVector.vectorResource(id = R.drawable.baseline_shopping_cart_24)



    val BottomNavItems = listOf(
//        BottomNavItem(
//            label = "Home",
//            icon = Icons.Filled.Home,
//            route = "home"
//        ),
        BottomNavItem(
            label = "Login",
            icon = R.drawable.merge,
            route = "login"
        ),

        BottomNavItem(
            label = "Search",
            icon = R.drawable.merge,
            route = "search"
        ),

        BottomNavItem(
            label = "Purge",
            icon = R.drawable.cut,
            route = "purge"
        ),

        BottomNavItem(
            label = "Shuffle",
            icon = R.drawable.shuffle,
            route = "playlists" // todo rename this
        ),


    )
}