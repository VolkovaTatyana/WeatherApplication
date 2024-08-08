package com.mukas.weatherapp.presentation.screen.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mukas.weatherapp.R
import com.mukas.weatherapp.presentation.util.getGradientByIndex
import com.mukas.weatherapp.presentation.util.tempToFormattedString
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.absoluteValue

@Composable
fun DetailsScreen(
    citiesAmount: Int,
    cityPositionInList: Int,
    cityId: Int,
    cityName: String,
    country: String,
    viewModel: DetailsViewModel = koinViewModel(
        key = cityId.toString(),
        parameters = { parametersOf(citiesAmount, cityPositionInList, cityId, cityName, country) })
) {
    val state by viewModel.state.collectAsState()

    val pagerState = rememberPagerState(initialPage = state.cityPositionInList) {
        state.citiesAmount
    }

    // This condition means that we have list of favourite cities
    // and Pager will work
    // else we have only one city to show
    if (citiesAmount > 1) {
        LaunchedEffect(key1 = pagerState.currentPage) {
            viewModel.act(DetailsAction.PagerStateChanged(pagerState.currentPage))
        }
    }

    val gradient = remember {
        getGradientByIndex(cityId).primaryGradient
    }

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        beyondViewportPageCount = 2
    ) { page ->

        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopBar(
                    cityName = state.city.name,
                    isCityFavourite = state.isFavourite,
                    onClick = viewModel::act
                )
            },
            modifier = Modifier
                .pagerCubeInDepthTransition(page, pagerState)
                .fillMaxSize()
                .background(
                    if (citiesAmount > 1) {
                        getGradientByIndex(page).primaryGradient
                    } else gradient
                )
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                when (val forecastState = state.forecastState) {
                    DetailsState.ForecastState.Error -> {
                        ErrorState()
                    }

                    DetailsState.ForecastState.Initial -> {
                    }

                    is DetailsState.ForecastState.Loaded -> {
                        Forecast(forecast = forecastState.forecast)
                    }

                    DetailsState.ForecastState.Loading -> {
                        Loading()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    cityName: String,
    isCityFavourite: Boolean,
    onClick: (DetailsAction) -> Unit
) {

    CenterAlignedTopAppBar(
        title = { Text(text = cityName) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            IconButton(onClick = { onClick(DetailsAction.ClickBack) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        },
        actions = {
            IconButton(onClick = { onClick(DetailsAction.ClickChangeFavouriteState) }) {
                val icon = if (isCityFavourite) {
                    Icons.Default.Star
                } else {
                    Icons.Default.StarBorder
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    )
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
private fun Forecast(forecast: ForecastUi) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Text(
            text = forecast.currentWeather.conditionText,
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = forecast.currentWeather.tempC.tempToFormattedString(),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 70.sp)
            )
            AsyncImage(
                modifier = Modifier.size(70.dp),
                model = forecast.currentWeather.conditionUrl,
                contentDescription = null
            )
        }
        Text(
            text = forecast.currentWeather.formattedFullDate,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        AnimatedUpcomingWeather(upcoming = forecast.upcoming)
        Spacer(
            modifier = Modifier.weight(0.5f)
        )
    }
}

@Composable
private fun AnimatedUpcomingWeather(upcoming: ImmutableList<WeatherUi>) {

    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(500)) + slideIn(
            animationSpec = tween(500), initialOffset = { IntOffset(0, it.height) }
        )
    ) {
        UpcomingWeather(upcoming = upcoming)
    }
}

@Composable
private fun UpcomingWeather(upcoming: ImmutableList<WeatherUi>) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(
                alpha = 0.24f
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.upcoming),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                upcoming.forEach {
                    SmallWeatherCard(weather = it)
                }
            }
        }
    }
}

@Composable
private fun RowScope.SmallWeatherCard(weather: WeatherUi) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .height(128.dp)
            .weight(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = weather.tempC.tempToFormattedString())
            AsyncImage(
                modifier = Modifier.size(48.dp),
                model = weather.conditionUrl,
                contentDescription = null
            )
            Text(text = weather.formattedShortDayOfWeek)
        }
    }
}

@Composable
private fun ErrorState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(64.dp),
            imageVector = Icons.Filled.Android,
            contentDescription = null,
            tint = Color.White
        )
        Text(
            text = stringResource(R.string.failed_to_retrieve_data),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.background
        )
    }
}

private fun Modifier.pagerCubeInDepthTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    cameraDistance = 32f
    // Calculate the absolute offset for the current page from the
    // scroll position.
    val pageOffset = pagerState.getOffsetDistanceInPages(page)

    if (pageOffset < -1f) {
        // page is far off screen
        alpha = 0f
    } else if (pageOffset <= 0) {
        // page is to the right of the selected page or the selected page
        alpha = 1f
        transformOrigin = TransformOrigin(0f, 0.5f)
        rotationY = -90f * pageOffset.absoluteValue

    } else if (pageOffset <= 1) {
        // page is to the left of the selected page
        alpha = 1f
        transformOrigin = TransformOrigin(1f, 0.5f)
        rotationY = 90f * pageOffset.absoluteValue
    } else {
        alpha = 0f
    }

    if (pageOffset.absoluteValue <= 0.5) {
        scaleY = 0.4f.coerceAtLeast(1 - pageOffset.absoluteValue)
    } else if (pageOffset.absoluteValue <= 1) {
        scaleY = 0.4f.coerceAtLeast(1 - pageOffset.absoluteValue)
    }
}
