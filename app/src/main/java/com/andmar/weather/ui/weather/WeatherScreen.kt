package com.andmar.weather.ui.weather

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
import com.andmar.weather.ParameterTextWithDivide
import com.andmar.weather.ParameterSmallText
import com.andmar.weather.ApiInfoText
import com.andmar.weather.ui.AppViewModelProvider
import com.andmar.weather.ui.navigate.WeatherDestination
import com.andmar.weather.ui.theme.WeatherColors

object WeatherScreenDestination: WeatherDestination {
    override val route = "weatherScreen"
    override val showActions = true
    const val iconActions = "settings"
    override val iconNavigation = "menu"
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavSettings: () -> Unit,
    onNavLocation: () -> Unit,
    onNavDetails: () -> Unit
) {
viewModel.getWeather()
    
    Scaffold(
        topBar = {
            WeatherTopBar(
                title = viewModel.weatherUiState.weather.location.name,
                showActions = WeatherScreenDestination.showActions,
                iconActions = WeatherScreenDestination.iconActions,
                onClickActions = {
                    onNavSettings()
                },
                iconNavigation = WeatherScreenDestination.iconNavigation,
                onClickNavigation = {
                    onNavLocation()
                }
            )
        }
    ) { scaffoldPadding ->
        WeatherBody(
            scaffoldPadding = scaffoldPadding,
            weather = viewModel.weatherUiState.weather,
            onNavDetails = {
                onNavDetails()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WeatherBody(
    scaffoldPadding: PaddingValues,
    weather: WeatherUi,
    onNavDetails: () -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
    ) {
        item {
            CurrentWeather(
                current = weather.current
            )
            AlertsCard(
                alerts = weather.alerts
            )
            DaysWeatherCard(
                forecast = weather.forecast,
                onNavDetails = {
                    onNavDetails()
                }
            )
            HoursWeatherCard(
                forecast = weather.forecast.forecastday
            )
            CurrentDetailsWeatherCard(
                current = weather.current
            )
            AirQualityCard(
                airQuality = weather.current.air_quality
            )
            AstroCard(
                forecast = weather.forecast.forecastday
            )
            WeatherApiInfo()
        }
    }
}

@Composable
fun CurrentWeather(
    current: CurrentUi
) {
val color = when(current.condition.code) {
    1000, 1003, 1006 -> WeatherColors.LightBlue
    1009, 1030, 1063, 1066, 1069, 1072, 1135, 1147, 1150, 1180, 1183, 1186, 1189, 1198, 1204, 1210, 1213, 1249, 1279 -> WeatherColors.Gray
    else -> WeatherColors.DarkBlue
}
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(color),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = current.temp,
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = current.condition.text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )
            AsyncImage(
                model = "https:${current.condition.icon}",
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

@Composable
fun CurrentDetailsWeatherCard(
    current: CurrentUi
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        TitleText(stringResource(R.string.other_parameters))
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            ParameterTextWithDivide(stringResource(R.string.wind_dir) + ": ${current.wind_dir}")
            ParameterTextWithDivide(stringResource(R.string.wind_speed) + ": ${current.wind}")
            ParameterTextWithDivide(stringResource(R.string.wind_gust) + ": ${current.gust}")
            ParameterTextWithDivide(stringResource(R.string.pressure) + ": ${current.pressure}")
            ParameterTextWithDivide(stringResource(R.string.precipe) + ": ${current.precip}")
            ParameterTextWithDivide(stringResource(R.string.humidity) + ": ${current.humidity}")
            ParameterTextWithDivide(stringResource(R.string.feelslike) + ": ${current.feelslike}")
            ParameterTextWithDivide(stringResource(R.string.uv) + ": ${current.uv}")
        }
    } 
}

@Composable
fun DaysWeatherCard(
    forecast: ForecastUi,
    onNavDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        TitleText(stringResource(R.string.forecast_by_day))
        forecast.forecastday.forEachIndexed { index, item ->
        
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                ParameterText(
                    if(index == 0) {
                        stringResource(R.string.today)
                    } else if(index == 1) {
                        stringResource(R.string.tomorrow)
                    } else {
                        item.date
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                        .fillMaxWidth(0.7f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = "https:${item.day.condition.icon}",
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 5.dp)
                        )
                        ParameterText(item.day.condition.text)
                    }
                    ParameterText("${item.day.maxtemp} / ${item.day.mintemp}")
                }
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Gray)
                )
            }
        }
        Button(
            onClick = {
                onNavDetails()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(stringResource(R.string.details))
        }
    }
}

@Composable
fun HoursWeatherCard(
    forecast: List<ForecastDayUi>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        TitleText(
            stringResource(R.string.forecast_by_hour)
        )
        
        forecast.forEachIndexed { index, forecastItem ->
            if(index == 0) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) { 
                    items(forecastItem.hour) { item ->
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ParameterText(item.temp)
                            AsyncImage(
                                model = "https:${item.condition.icon}",
                                contentDescription = null
                            )
                            ParameterSmallText(item.wind)
                            ParameterText(item.time.removeRange(0, 11))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AirQualityCard(
    airQuality: AirQualityUi
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        TitleText(
            stringResource(R.string.air_quality)
        )
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            ParameterTextWithDivide(
                stringResource(R.string.co) + " - ${airQuality.co}"
            )
            ParameterTextWithDivide(
                stringResource(R.string.no2) + " - ${airQuality.no2}"
            )
            ParameterTextWithDivide(
                stringResource(R.string.o3) +" - ${airQuality.o3}"
            )
            ParameterTextWithDivide(
                stringResource(R.string.so2) + " - ${airQuality.so2}"
            )
            ParameterTextWithDivide(
                stringResource(R.string.pm2_5) + " - ${airQuality.pm2_5}"
            )
            ParameterTextWithDivide(
                stringResource(R.string.pm10) +" - ${airQuality.pm10}"
            )
            ParameterTextWithDivide(
                stringResource(R.string.index) + " - ${airQuality.us_epa_index}"
            )
        }
    }
}

@Composable
fun AstroCard(
    forecast: List<ForecastDayUi>
) {
    forecast.forEachIndexed { index, item ->
        if(index == 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                TitleText(
                    stringResource(R.string.sun_parameters)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    ParameterTextWithDivide(
                        stringResource(R.string.sunrise) + " - ${item.astro.sunrise}"
                    )
                    ParameterTextWithDivide(
                        stringResource(R.string.sunset) + " - ${item.astro.sunset}"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AlertsCard(alerts: AlertsUi) {
val pagerState = rememberPagerState(pageCount = { alerts.alert.size })

    HorizontalPager(state = pagerState) { page ->
        AlertCard(
            alerts.alert[page]
        )
    }
}

@Composable
fun AlertCard(alert: AlertUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Warning,
                null
            )
            Column(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                TitleText(
                    alert.event
                )
                ParameterText(
                    alert.desc
                )
            }
        }
    }
}

@Composable
fun WeatherApiInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ApiInfoText(
            stringResource(R.string.data_provided)
        )
        AsyncImage(
            model = "https://cdn.weatherapi.com/v4/images/weatherapi_logo.png",
            null
        )
    }
}