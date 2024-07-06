package com.andmar.weather.ui.location

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.border 
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button 
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.TabRow
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.SheetState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Card
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.BadgedBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import com.andmar.weather.R
import com.andmar.weather.WeatherTopBar
import com.andmar.weather.ui.AppViewModelProvider
import com.andmar.weather.ui.navigate.WeatherDestination
import com.andmar.weather.data.LocationItem

object LocationDestination: WeatherDestination {
    override val route = "locationScreen"
    override val showActions = true
    const val iconActions = "add"
    override val iconNavigation = "back"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    viewModel: LocationViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavAdd: () -> Unit,
    onNavBack: () -> Unit
) {
val locationItemList = viewModel.locationItemListState.collectAsState()

    Scaffold(
        topBar = {
        
            WeatherTopBar(
                title = stringResource(R.string.location_screen_title),
                showActions = LocationDestination.showActions,
                iconActions = LocationDestination.iconActions,
                onClickActions = {
                    onNavAdd()
                },
                iconNavigation = LocationDestination.iconNavigation,
                onClickNavigation = {
                    onNavBack()
                }
            )
        }
    ) { scaffoldPadding ->
        WeatherItemSettingsBody(
            scaffoldPadding = scaffoldPadding,
            locationUiState = viewModel.locationUiState,
            locationItemList = locationItemList.value.locationItemList,
            onNavAdd = onNavAdd,
            updateLocationUiState = {
                viewModel.updateLocationUiState(it)
            },
            deleteLocationItem = {
                viewModel.deleteLocation(it)
            }
        )
    }
}

@Composable
fun WeatherItemSettingsBody(
    scaffoldPadding: PaddingValues,
    locationUiState: LocationUiState,
    locationItemList: List<LocationItem>,
    onNavAdd: () -> Unit,
    updateLocationUiState: (LocationUiState) -> Unit,
    deleteLocationItem: (LocationItem) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
    ) {
        item {
            Button(
                onClick = {
                    updateLocationUiState(locationUiState.copy(""))
                },
                enabled = locationUiState.isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                if(locationUiState.isEnabled) {
                    Text(stringResource(R.string.find_automatic))
                } else {
                    Text(stringResource(R.string.finding_automatic))
                }
            }
            if(locationItemList.isEmpty()) {
                Text(
                    text = stringResource(R.string.location_screen_info),
                    fontSize = 25.sp,
                    modifier = Modifier.padding(10.dp)
                )
            } else {
                locationItemList.forEach { item ->
                    val borderColor = if(locationUiState.location == "${item.lat}, ${item.lon}") MaterialTheme.colorScheme.primary else Color.Transparent
                    
                    WeatherItemCard(
                        item = item,
                        deleteLocationItem = {
                           deleteLocationItem(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .border(2.dp, borderColor, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                updateLocationUiState(locationUiState.copy("${item.lat}, ${item.lon}"))
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherItemCard(
    item: LocationItem,
    modifier: Modifier,
    deleteLocationItem: (LocationItem) -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
            Text(
                item.name
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.region,
                    modifier = Modifier.padding(top = 5.dp, end = 5.dp)
                
                )
                Text(
                    text = item.country,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
        }
        
        IconButton(
            onClick = {
            deleteLocationItem(item)
            },
            modifier = Modifier
                .padding(10.dp)
        ) {
            Icon(
                Icons.Filled.Close,
                null
            )
            
        }
        
        }
    }
}