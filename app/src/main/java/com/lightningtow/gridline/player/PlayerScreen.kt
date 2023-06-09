//package com.lightningtow.gridline.player
//
//import LoadingScreen
//import PlayerUiState
//import PlayerViewModel
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.sizeIn
//import androidx.compose.material.ContentAlpha
//import androidx.compose.material.LocalContentAlpha
//import androidx.compose.material.LocalContentColor
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Slider
//import androidx.compose.material.Surface
//import androidx.compose.material.Text
//import androidx.compose.material.TopAppBar
//import androidx.compose.material.icons.Icons
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.semantics.Role
//import androidx.compose.ui.semantics.role
//import androidx.compose.ui.semantics.semantics
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.window.layout.DisplayFeature
//import androidx.window.layout.FoldingFeature
//import java.time.Duration
//
//
//
///**
// * Stateful version of the Podcast player
// */
//@Composable
//fun PlayerScreen(
//    viewModel: PlayerViewModel,
//    onBackPress: () -> Unit
//) {
//    val uiState = viewModel.uiState
//    PlayerScreenPriv(uiState, onBackPress)
//}
//
///**
// * Stateless version of the Player screen
// */
//@Composable
//private fun PlayerScreenPriv(
//    uiState: PlayerUiState,
//    onBackPress: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Surface(modifier) {
//        if (uiState.podcastName.isNotEmpty()) {
//            PlayerContent(uiState, onBackPress)
//        } else {
////            FullScreenLoading()
//            LoadingScreen(message = "loading...")
//        }
//    }
//}
//
//@Composable
//fun PlayerContent(
//    uiState: PlayerUiState,
//
//    onBackPress: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//
//            PlayerContentRegular(uiState, onBackPress, modifier)
//
//
//}
//
//@Composable
//private fun PlayerImage(
//    podcastImageUrl: String,
//    modifier: Modifier = Modifier
//) {
////    AsyncImage(
////        model = ImageRequest.Builder(LocalContext.current)
////            .data(podcastImageUrl)
////            .crossfade(true)
////            .build(),
////        contentDescription = null,
////        contentScale = ContentScale.Crop,
////        modifier = modifier
////            .sizeIn(maxWidth = 500.dp, maxHeight = 500.dp)
////            .aspectRatio(1f)
////            .clip(MaterialTheme.shapes.medium)
////    )
//}
//
///**
// * The UI for the top pane of a tabletop layout.
// */
//@Composable
//private fun PlayerContentRegular(uiState: PlayerUiState, onBackPress: () -> Unit, modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier
//            .fillMaxSize()
////            .verticalGradientScrim(
////                color = MaterialTheme.colors.primary.copy(alpha = 0.50f),
////                startYPercentage = 1f,
////                endYPercentage = 0f
////            )
////            .systemBarsPadding()
//            .padding(horizontal = 8.dp)
//    ) {
////        TopAppBar(onBackPress = onBackPress)
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.padding(horizontal = 8.dp)
//        ) {
//            Spacer(modifier = Modifier.weight(1f))
//            PlayerImage(
//                podcastImageUrl = uiState.podcastImageUrl,
//                modifier = Modifier.weight(10f)
//            )
//            Spacer(modifier = Modifier.height(32.dp))
//            PodcastDescription(uiState.title, uiState.podcastName)
//            Spacer(modifier = Modifier.height(32.dp))
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.weight(10f)
//            ) {
//                PlayerSlider(uiState.duration)
//                PlayerButtons(Modifier.padding(vertical = 8.dp))
//            }
//            Spacer(modifier = Modifier.weight(1f))
//        }
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//private fun PodcastDescription(
//    title: String,
//    podcastName: String,
//    titleTextStyle: TextStyle = MaterialTheme.typography.h5
//) {
//    Text(
//        text = title,
//        style = titleTextStyle,
//        maxLines = 1,
////        modifier = Modifier.basicMarquee()
//    )
//    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
//        Text(
//            text = podcastName,
//            style = MaterialTheme.typography.body2,
//            maxLines = 1
//        )
//    }
//}
//
//@Composable
//private fun PodcastInformation(
//    title: String,
//    name: String,
//    summary: String,
//    titleTextStyle: TextStyle = MaterialTheme.typography.h5,
//    nameTextStyle: TextStyle = MaterialTheme.typography.h3,
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.padding(horizontal = 8.dp)
//    ) {
//        Text(
//            text = name,
//            style = nameTextStyle,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//        Spacer(modifier = Modifier.height(32.dp))
//        Text(
//            text = title,
//            style = titleTextStyle,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//        Spacer(modifier = Modifier.height(32.dp))
//        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
//            Text(
//                text = summary,
//                style = MaterialTheme.typography.body2,
//            )
//        }
//        Spacer(modifier = Modifier.weight(1f))
//    }
//}
//
//@Composable
//private fun PlayerSlider(episodeDuration: Duration?) {
//    if (episodeDuration != null) {
//        Column(Modifier.fillMaxWidth()) {
//            Slider(value = 0f, onValueChange = { })
//            Row(Modifier.fillMaxWidth()) {
//                Text(text = "0s")
//                Spacer(modifier = Modifier.weight(1f))
//                Text("42 FILLME")
//
////                Text("${episodeDuration.seconds}s")
//            }
//        }
//    }
//}
//
//@Composable
//private fun PlayerButtons(
//    modifier: Modifier = Modifier,
//    playerButtonSize: Dp = 72.dp,
//    sideButtonSize: Dp = 48.dp
//) {
//    Row(
//        modifier = modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceEvenly
//    ) {
//        val buttonsModifier = Modifier
//            .size(sideButtonSize)
//            .semantics { role = Role.Button }
//
////        Image(
////            imageVector = Icons.Filled.SkipPrevious,
//////            contentDescription = stringResource(R.string.cd_skip_previous),
////            contentScale = ContentScale.Fit,
////            colorFilter = ColorFilter.tint(LocalContentColor.current),
////            modifier = buttonsModifier
////        )
////        Image(
////            imageVector = Icons.Filled.Replay10,
//////            contentDescription = stringResource(R.string.cd_reply10),
////            contentScale = ContentScale.Fit,
////            colorFilter = ColorFilter.tint(LocalContentColor.current),
////            modifier = buttonsModifier
////        )
////        Image(
////            imageVector = Icons.Rounded.PlayCircleFilled,
//////            contentDescription = stringResource(R.string.cd_play),
////            contentScale = ContentScale.Fit,
////            colorFilter = ColorFilter.tint(LocalContentColor.current),
////            modifier = Modifier
////                .size(playerButtonSize)
////                .semantics { role = Role.Button }
////        )
////        Image(
////            imageVector = Icons.Filled.Forward30,
//////            contentDescription = stringResource(R.string.cd_forward30),
////            contentScale = ContentScale.Fit,
////            colorFilter = ColorFilter.tint(LocalContentColor.current),
////            modifier = buttonsModifier
////        )
////        Image(
////            imageVector = Icons.Filled.SkipNext,
//////            contentDescription = stringResource(R.string.cd_skip_next),
////            contentScale = ContentScale.Fit,
////            colorFilter = ColorFilter.tint(LocalContentColor.current),
////            modifier = buttonsModifier
////        )
//    }
//}