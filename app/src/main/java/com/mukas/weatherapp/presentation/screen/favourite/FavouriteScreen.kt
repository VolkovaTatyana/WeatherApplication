package com.mukas.weatherapp.presentation.screen.favourite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mukas.weatherapp.R
import com.mukas.weatherapp.presentation.theme.CardGradients
import com.mukas.weatherapp.presentation.theme.Gradient
import com.mukas.weatherapp.presentation.theme.Orange
import com.mukas.weatherapp.presentation.util.tempToFormattedString
import com.theapache64.rebugger.Rebugger
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavouriteScreen(viewModel: FavouriteViewModel = koinViewModel()) {

    val state by viewModel.state.collectAsState()

    val refreshingState by remember {
        derivedStateOf {
            state.cityItems.any { it.weatherState is FavouriteState.WeatherState.Loading }
        }
    }

    val refreshState = rememberPullRefreshState(
        refreshingState,
        { viewModel.act(FavouriteAction.Refresh) }
    )

    val onClickSearchCallback = remember {
        {
            viewModel.act(action = FavouriteAction.ClickSearch)
        }
    }

    val onClickAddFavouriteCallback = remember {
        {
            viewModel.act(action = FavouriteAction.ClickAddFavourite)
        }
    }

    Box(Modifier.pullRefresh(refreshState)) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "SearchCard", span = { GridItemSpan(2) }) {
                SearchCard(
                    onClickSearch = viewModel::act
                )
            }
            itemsIndexed(
                items = state.cityItems,
                key = { _, item -> item.city.id }
            ) { index, item ->
                CityCard(
                    cityItem = item,
                    index = index,
                    onClickCityItem = viewModel::act
                )
            }
            item(key = "AddFavouriteCityCard") {
                AddFavouriteCityCard(
                    onClickAddFavourite = viewModel::act
                )
            }
        }

        PullRefreshIndicator(
            refreshingState,
            refreshState,
            Modifier.align(Alignment.TopCenter),
            contentColor = Color.Magenta
        )
    }
}

@Composable
private fun CityCard(
    cityItem: FavouriteState.CityItem,
    index: Int,
    onClickCityItem: (FavouriteAction) -> Unit
) {
    val gradient = remember { getGradientByIndex(index) }

    Rebugger(
        trackMap = mapOf(
            "cityItem" to cityItem,
            "index" to index,
            "onClickCityItem" to onClickCityItem,
            "gradient" to gradient,
        ), composableName = "CityCard"
    )

    Card(
        modifier = Modifier
            .fillMaxSize()
            .shadow(
                elevation = 16.dp,
                spotColor = gradient.shadowColor,
                shape = MaterialTheme.shapes.extraLarge
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Blue),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Box(
            modifier = Modifier
                .background(gradient.primaryGradient)
                .fillMaxSize()
                .sizeIn(minHeight = 196.dp)
                .drawBehind {
                    drawCircle(
                        brush = gradient.secondaryGradient,
                        center = Offset(
                            x = center.x - size.width / 10,
                            y = center.y + size.height / 2
                        ),
                        radius = size.maxDimension / 2
                    )
                }
                .clickable { onClickCityItem(FavouriteAction.CityItemClick(cityItem.city)) }
                .padding(24.dp)
        ) {
            when (val weatherState = cityItem.weatherState) {

                FavouriteState.WeatherState.Initial -> {}

                is FavouriteState.WeatherState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.background
                    )
                }

                is FavouriteState.WeatherState.Loaded -> {
                    LoadedWeatherState(
                        iconUrl = weatherState.iconUrl,
                        tempC = weatherState.tempC.tempToFormattedString()
                    )
                }

                is FavouriteState.WeatherState.Error -> {}
            }

            Text(
                modifier = Modifier.align(Alignment.BottomStart),
                text = cityItem.city.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
private fun BoxScope.LoadedWeatherState(
    iconUrl: String,
    tempC: String
) {

    AsyncImage(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .size(56.dp),
        model = iconUrl,
        contentDescription = null
    )
    Text(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(bottom = 24.dp),
        text = tempC,
        color = MaterialTheme.colorScheme.background,
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp)
    )
}

@Composable
private fun AddFavouriteCityCard(
    onClickAddFavourite: () -> Unit
) {
    Rebugger(
        trackMap = mapOf(
            "onClickAddFavourite" to onClickAddFavourite,
        ), composableName = "AddFavouriteCityCard"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = Modifier
                .sizeIn(minHeight = 196.dp)
                .fillMaxWidth()
                .clickable { onClickAddFavourite() }
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp)
                    .size(64.dp),
                imageVector = Icons.Default.Edit,
                tint = Orange,
                contentDescription = null
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.button_add_favourite),
                color = Orange,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
private fun SearchCard(
    onClickSearch: () -> Unit
) {

    val gradient = remember {
        CardGradients.gradients[3]
    }

    Rebugger(
        trackMap = mapOf(
            "onClickSearch" to onClickSearch,
            "gradient" to gradient,
        ), composableName = "SearchCard"
    )

    Card(
        shape = CircleShape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClickSearch() }
                .fillMaxWidth()
                .background(gradient.primaryGradient)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.background,
                contentDescription = null,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
            Text(
                text = stringResource(R.string.search),
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

private fun getGradientByIndex(index: Int): Gradient {
    val gradients = CardGradients.gradients
    return gradients[index % gradients.size]
}