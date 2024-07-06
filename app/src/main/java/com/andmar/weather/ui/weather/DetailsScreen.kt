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
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.andmar.weather.TitleText
import com.andmar.weather.ParameterText
import com.andmar.weather.ParameterTextWithDivide
import com.andmar.weather.ParameterSmallText
import com.andmar.weather.ui.AppViewModelProvider
import com.andmar.weather.ui.navigate.WeatherDestination
import com.andmar.weather.ui.theme.WeatherColors
import com.andmar.weather.rest.Forecast
import com.andmar.weather.rest.ForecastDay
import com.andmar.weather.rest.Hour

object DetailsDestination: WeatherDestination {
    override val route = "detailsScreen"
    override val showActions = false
    override val iconNavigation = "back"
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            WeatherTopBar(
                title = stringResource(R.string.details_screen_title),
                showActions = DetailsDestination.showActions,
                iconNavigation = DetailsDestination.iconNavigation,
                onClickNavigation = {
                    onNavBack()
                }
            )
        }
    ) { scaffoldPadding ->
        DetailsDayBody(
            scaffoldPadding = scaffoldPadding,
            forecast = viewModel.detailsUiState.weather.forecast
        )
    }
}

@Composable
fun DetailsDayBody(
    scaffoldPadding: PaddingValues,
    forecast: ForecastUi
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
    ) {
        itemsIndexed(forecast.forecastday) { index, item ->
            DetailsDayCard(
                item = item,
                dateText = 
                    if(index == 0) {
                        stringResource(R.string.today) 
                    } else if(index == 1) {
                        stringResource(R.string.tomorrow) 
                    } else {
                        item.date
                    }
            )
        }
    }
}

@Composable
fun DetailsDayCard(
    item: ForecastDayUi,
    dateText: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
    TitleText(dateText)
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
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
            ParameterTextWithDivide(stringResource(R.string.wind_speed) + ": ${item.day.maxwind}")
            ParameterTextWithDivide(stringResource(R.string.avg_temp) + " : ${item.day.avgtemp}")
            ParameterTextWithDivide(stringResource(R.string.precipe) + ": ${item.day.totalprecip}")
            ParameterTextWithDivide(stringResource(R.string.humidity) + ": ${item.day.avghumidity}")
            ParameterTextWithDivide(stringResource(R.string.chance_of_rain) + " : ${item.day.daily_chance_of_rain}")
            ParameterTextWithDivide(stringResource(R.string.uv) + ": ${item.day.uv}")
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) { 
            items(item.hour, key = { it.time}) { item ->
                DetailsHourCard(
                    item = item
                )
            }
        }
    }
}

@Composable
fun DetailsHourCard(item: HourUi) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
    ParameterText("${item.temp}")
        
        AsyncImage(
            model = "https:${item.condition.icon}",
            contentDescription = null
         )
         ParameterSmallText(item.wind)
       ParameterText(item.time.removeRange(0, 11))
        
    }
}