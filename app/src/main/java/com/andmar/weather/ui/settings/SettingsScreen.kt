package com.andmar.weather.ui.settings

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
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
import com.andmar.weather.TitleText
import com.andmar.weather.ParameterText
import com.andmar.weather.ModalBottomSheetItem
import com.andmar.weather.ui.AppViewModelProvider
import com.andmar.weather.ui.navigate.WeatherDestination
import com.andmar.weather.ui.theme.WeatherColors
import com.andmar.weather.rest.Weather
import com.andmar.weather.rest.Current
import com.andmar.weather.rest.Forecast
import com.andmar.weather.rest.ForecastDay
import com.andmar.weather.rest.AirQuality
import com.andmar.weather.rest.Alerts

object SettingsDestination: WeatherDestination {
    override val route = "settingsScreen"
    override val showActions = false
    override val iconNavigation = "back"
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavBack: () -> Unit
) {
val settings = viewModel.settingsUiState.settings

    Scaffold(
        topBar = {
            WeatherTopBar(
                title = stringResource(R.string.settings_screen_title),
                showActions = SettingsDestination.showActions,
                iconNavigation = SettingsDestination.iconNavigation,
                onClickNavigation = {
                    onNavBack()
                }
            )
        }
    ) { scaffoldPadding ->
        SettingsBody(
            scaffoldPadding = scaffoldPadding,
            settingsUiState = viewModel.settingsUiState,
            updateSettingsUiState = {
                viewModel.updateSettingsUiState(it)
            }
        )
        
        if(viewModel.settingsUiState.bottomSheetState.tempMenuSheet) {
            SettingsMenu(
                onContent = {
                    ModalBottomSheetItem(
                        itemText = stringResource(R.string.settings_text_temp_c),
                        parameterText = "°C",
                        onClick = {
                            viewModel.updateSettingsUiState(
                                viewModel.settingsUiState.copy(
                                    settings = settings.copy(tempMeasure = "°C"),
                                    bottomSheetState = BottomSheetState(tempMenuSheet = false)
                                )
                            )
                        }
                    )
                    ModalBottomSheetItem(
                        itemText = stringResource(R.string.settings_text_temp_f),
                        parameterText = "°F",
                        onClick = {
                            viewModel.updateSettingsUiState(
                                viewModel.settingsUiState.copy(
                                    settings = settings.copy(tempMeasure = "°F"),
                                    bottomSheetState = BottomSheetState(tempMenuSheet = false)
                                )
                            )
                        }
                    )
                },
                onDismiss = {
                    viewModel.updateSettingsUiState(
                        viewModel.settingsUiState.copy(
                            bottomSheetState = BottomSheetState(tempMenuSheet = false)
                        )
                    )
                }
            )
        }
        if(viewModel.settingsUiState.bottomSheetState.windMenuSheet) {
            SettingsMenu(
                onContent = {
                    ModalBottomSheetItem(
                        itemText = stringResource(R.string.settings_text_wind_mph),
                        parameterText = "m/h",
                        onClick = {
                            viewModel.updateSettingsUiState(
                                viewModel.settingsUiState.copy(
                                    settings = settings.copy(windMeasure = "m/h"),
                                    bottomSheetState = BottomSheetState(windMenuSheet = false)
                                )
                            )
                        }
                    )
                    ModalBottomSheetItem(
                        itemText = stringResource(R.string.settings_text_wind_kph),
                        parameterText = "km/h",
                        onClick = {
                            viewModel.updateSettingsUiState(
                                viewModel.settingsUiState.copy(
                                    settings = settings.copy(windMeasure = "km/h"),
                                    bottomSheetState = BottomSheetState(windMenuSheet = false)
                                )
                            )
                        }
                    )
                },
                onDismiss = {
                    viewModel.updateSettingsUiState(
                        viewModel.settingsUiState.copy(
                            bottomSheetState = BottomSheetState(windMenuSheet = false)
                        )
                    )
                }
            )
        }
        if(viewModel.settingsUiState.bottomSheetState.pressureMenuSheet) {
            SettingsMenu(
                onContent = {
                    ModalBottomSheetItem(
                        itemText = stringResource(R.string.settings_text_pressure_mb),
                        parameterText = "mb",
                        onClick = {
                            viewModel.updateSettingsUiState(
                                viewModel.settingsUiState.copy(
                                    settings = settings.copy(pressureMeasure = "mb"),
                                    bottomSheetState = BottomSheetState(pressureMenuSheet = false)
                                )
                            )
                        }
                    )
                },
                onDismiss = {
                    viewModel.updateSettingsUiState(
                        viewModel.settingsUiState.copy(
                            bottomSheetState = BottomSheetState(pressureMenuSheet = false)
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun SettingsBody(
    scaffoldPadding: PaddingValues,
    settingsUiState: SettingsUiState,
    updateSettingsUiState: (SettingsUiState) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
    ) {
        item {
            SettingsItemCard(
                parameterText = settingsUiState.settings.tempMeasure,
                itemText = stringResource(R.string.settings_text_temp),
                onClickSettingsItem = {
                    updateSettingsUiState(
                        settingsUiState.copy(
                            bottomSheetState = BottomSheetState(tempMenuSheet = true)
                        )
                    )
                }
            )
            SettingsItemCard(
                parameterText = settingsUiState.settings.windMeasure,
                itemText = stringResource(R.string.settings_text_wind),
                onClickSettingsItem = {
                    updateSettingsUiState(
                        settingsUiState.copy(
                            bottomSheetState = BottomSheetState(windMenuSheet = true)
                        )
                    )
                }
            )
            SettingsItemCard(
                parameterText = settingsUiState.settings.pressureMeasure,
                itemText = stringResource(R.string.settings_text_pressure),
                onClickSettingsItem = {
                    updateSettingsUiState(
                        settingsUiState.copy(
                            bottomSheetState = BottomSheetState(pressureMenuSheet = true)
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun SettingsItemCard(
    itemText: String,
    parameterText: String,
    onClickSettingsItem: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onClickSettingsItem()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleText(itemText)
            ParameterText(parameterText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsMenu(
    state: SheetState = rememberModalBottomSheetState(),
    onContent: @Composable () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = {
            onDismiss()
        }
    ) {
        onContent()
        Spacer(modifier = Modifier.height(30.dp))
    }
}