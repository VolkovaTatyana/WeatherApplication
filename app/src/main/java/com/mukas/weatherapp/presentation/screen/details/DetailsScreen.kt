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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mukas.weatherapp.R
import com.mukas.weatherapp.domain.entity.Forecast
import com.mukas.weatherapp.domain.entity.Weather
import com.mukas.weatherapp.presentation.theme.CardGradients
import com.mukas.weatherapp.presentation.util.formattedFullDate
import com.mukas.weatherapp.presentation.util.formattedShortDayOfWeek
import com.mukas.weatherapp.presentation.util.recomposeHighlighter
import com.mukas.weatherapp.presentation.util.tempToFormattedString
import com.theapache64.rebugger.Rebugger
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailsScreen(
    cityId: Int,
    cityName: String,
    country: String,
    viewModel: DetailsViewModel = koinViewModel(
        key = cityId.toString(),
        parameters = { parametersOf(cityId, cityName, country) })
) {
    val state by viewModel.state.collectAsState()

    Rebugger(
        trackMap = mapOf(
            "city" to state.city,
            "isFavourite" to state.isFavourite,
            "forecastState" to state.forecastState
        )
    )

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
            .recomposeHighlighter()
            .fillMaxSize()
            .background(CardGradients.gradients[1].primaryGradient)
    ) { paddingValues ->
        Box(modifier = Modifier
            .recomposeHighlighter()
            .padding(paddingValues)) {
            when (val forecastState = state.forecastState) {
                DetailsState.ForecastState.Error -> {
                    Error()
                }

                DetailsState.ForecastState.Initial -> {
                    Initial()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    cityName: String,
    isCityFavourite: Boolean,
    onClick: (DetailsAction) -> Unit
) {

    Rebugger(
        trackMap = mapOf(
            "cityName" to cityName,
            "isFavourite" to isCityFavourite,
            "onClick" to onClick
        )
    )

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
    Box(modifier = Modifier
        .recomposeHighlighter()
        .fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .recomposeHighlighter()
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.background
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Forecast(forecast: Forecast) {
    Rebugger(
        trackMap = mapOf(
            "forecast" to forecast
        )
    )

    Column(
        modifier = Modifier
            .recomposeHighlighter()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier
            .recomposeHighlighter()
            .weight(1f))
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
            GlideImage(
                modifier = Modifier
                    .recomposeHighlighter()
                    .size(70.dp),
                model = forecast.currentWeather.conditionUrl,
                contentDescription = null
            )
        }
        Text(
            text = forecast.currentWeather.date.formattedFullDate(),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier
            .recomposeHighlighter()
            .weight(1f))
        AnimatedUpcomingWeather(upcoming = forecast.upcoming)
        Spacer(modifier = Modifier
            .recomposeHighlighter()
            .weight(0.5f))
    }
}

@Composable
private fun AnimatedUpcomingWeather(upcoming: List<Weather>) {
    Rebugger(trackMap = mapOf("upcoming" to upcoming))

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
private fun UpcomingWeather(upcoming: List<Weather>) {
    Card(
        modifier = Modifier
            .recomposeHighlighter()
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
                .recomposeHighlighter()
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.upcoming),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .recomposeHighlighter()
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                modifier = Modifier
                    .recomposeHighlighter()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                upcoming.forEach {
                    SmallWeatherCard(weather = it)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RowScope.SmallWeatherCard(weather: Weather) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .recomposeHighlighter()
            .height(128.dp)
            .weight(1f)
    ) {
        Column(
            modifier = Modifier
                .recomposeHighlighter()
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = weather.tempC.tempToFormattedString())
            GlideImage(
                modifier = Modifier
                    .recomposeHighlighter()
                    .size(48.dp),
                model = weather.conditionUrl,
                contentDescription = null
            )
            Text(text = weather.date.formattedShortDayOfWeek())
        }
    }
}

@Composable
private fun Initial() {

}

@Composable
private fun Error() {

}
