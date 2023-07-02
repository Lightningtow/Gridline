package com.lightningtow.gridline.ui.components

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lightningtow.gridline.R
import com.lightningtow.gridline.ui.home.TrackViewMaster
import com.lightningtow.gridline.ui.home.shouldLoadTracks
import com.lightningtow.gridline.data.TrackHolder1
import com.lightningtow.gridline.player.Player.PlayerPage
import com.lightningtow.gridline.ui.home.AuthPage
import com.lightningtow.gridline.ui.home.Broadcasts
import com.lightningtow.gridline.ui.home.HomePage

import com.lightningtow.gridline.ui.home.AllPlaylistsViewEntry
//import com.lightningtow.gridline.activities.PlaylistViewActivity
//import com.lightningtow.gridline.activities.PlaylistViewPage
//import com.lightningtow.gridline.ui.home.AuthPage
import com.lightningtow.gridline.ui.home.LandingScreen
//import com.lightningtow.gridline.ui.home.MainActivity
import com.lightningtow.gridline.ui.home.PurgeViewMaster
import com.lightningtow.gridline.ui.home.listPicking
import com.lightningtow.gridline.ui.theme.GridlineTheme
import com.lightningtow.gridline.utils.Constants


//@Preview
@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,

        // set the start destination as home
        startDestination = "idk",

        // Set the padding provided by scaffold
        modifier = Modifier.padding(paddingValues = padding),

        builder = {
            /**  the order of these do not matter!  */


            composable("idk") {
//                HelloWorld()
                LandingScreen()
                showTracksNow = false
            }
            composable("home") {
                HomePage.HomePageEntry()
                showTracksNow = false
            }
            composable("player") {
                PlayerPage()
                showTracksNow = false
            }
            composable("queue") {
                PlayerPage()
                showTracksNow = false
            }
            composable("auth") {
                AuthPage()
                showTracksNow = false
            }

//            // todo what is showTracksNow
//            composable("purge") {
//                PurgeViewMaster()
//                BackHandler(
//                    enabled = listPicking, // runs this only while trackPicking
//                    onBack = {
//                        listPicking = false
////                    PurgeData.choosingPurgelist = false
//
//                })
//            }
//            composable("login") {
////                context.startActivity(Intent(context, Player::class.java))
//                AuthPage()
//                showTracksNow = false
//            }
            composable("library") {

                BackHandler(enabled = showTracksNow, onBack = {
                    shouldLoadTracks = false
                    showTracksNow = false

                })
                if(!showTracksNow) {
                    AllPlaylistsViewEntry("Playlists", onPlaylistClick = {
//                    loadingTracks = true

                        shouldLoadTracks = true
                        TrackHolder1.playlistName = it.name
                        TrackHolder1.TrackHolder1Uri = it.uri.uri
                        showTracksNow = true

                    })
                } else if (showTracksNow) {
                    TrackViewMaster(uri=TrackHolder1.TrackHolder1Uri)
                }

            }


        })
}
data class BottomNavItem(
    val label: String,
    val icon: Int,
    val route: String,
)
private val BottomNavItems = listOf(
    /**  order of these determine order and which ones are shown  */
    BottomNavItem(
        label = "Idk",
        icon = R.drawable.round_start_24,
        route = "idk"
    ),




    BottomNavItem(
        label = "Auth",
        icon = R.drawable.account_circle,
        route = "auth"
    ),

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

    BottomNavItem(
        label = "Queue",
        icon = R.drawable.round_queue_music_24,
        route = "queue"
    ),
//        BottomNavItem(
//            label = "Purge",
//            icon = R.drawable.knife,
//            route = "purge"
//        ),

    BottomNavItem(
        label = "Library",
        icon = R.drawable.round_inventory_2_24,
        route = "library"
    ),
)
private var showTracksNow by mutableStateOf(false)


@Composable
fun BottomNavigationBar(navController: NavHostController) {
//    Divider(color = GridlineTheme.colors.brand, thickness = 3.dp)
// todo dont pass nav controller
// todo https://developer.android.com/jetpack/compose/navigation#nav-from-composable
/*
It is strongly recommended that you decouple the Navigation code from your composable destinations to enable
testing each composable in isolation, separate from the NavHost composable. This means that you shouldn't
pass the navController directly into any composable and instead pass navigation callbacks as parameters.
This allows all your composables to be individually testable, as they don't require an instance of navController in tests.
 */
    BottomNavigation(

        // set background color
//        backgroundColor = Color(0xFF0F9D58)
        backgroundColor = GridlineTheme.colors.uiFloated, // why doesnt uiFloated work
        contentColor = GridlineTheme.colors.brand,

        ) {

        // observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // observe current route to change the icon
        // color,label color when navigated
        val currentRoute = navBackStackEntry?.destination?.route


        // Bottom nav items we declared
        BottomNavItems.forEach { navItem ->

            // Place the bottom nav items
            BottomNavigationItem(
                // it currentRoute is equal then its selected route
                selected = currentRoute == navItem.route,

                // navigate on click
                onClick = {
                    navController.navigate(navItem.route) },

                // Icon of navItem
                icon = {
                    Icon(
                        ImageVector.vectorResource(id = navItem.icon),
                        contentDescription = navItem.label,
                        modifier = Modifier
                            .height(28.dp)
                    )
//                    navItem.icon
//                    Icon(Ima)
//                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                },

                // label
                label = {
                    Text(text = navItem.label,
//                        modifier = Modifier.size(10.dp)
                    )
                },
                alwaysShowLabel = false,
//                modifier = Modifier.
            )
        }
    }
}