package com.mukas.weatherapp.presentation.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mukas.weatherapp.R
import com.mukas.weatherapp.domain.entity.City
import com.theapache64.rebugger.Rebugger
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    isSearchToAddFavourite: Boolean,
    viewModel: SearchViewModel = koinViewModel(
        key = isSearchToAddFavourite.toString(),
        parameters = { parametersOf(isSearchToAddFavourite) })
) {
    val state by viewModel.state.collectAsState()

    val focusRequester = remember {
        FocusRequester()
    }

    Rebugger(
        trackMap = mapOf(
            "searchQuery" to state.searchQuery,
            "requestState" to state.requestState,
            "focusRequester" to focusRequester
        )
    )

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    SearchBar(
        modifier = Modifier.focusRequester(focusRequester),
        query = state.searchQuery,
        placeholder = { Text(text = stringResource(id = R.string.search)) },
        onQueryChange = { viewModel.act(SearchAction.ChangeSearchQuery(it)) },
        onSearch = { viewModel.act(SearchAction.ClickSearch) },
        active = true,
        onActiveChange = {},
        leadingIcon = {
            IconButton(onClick = { viewModel.act(SearchAction.ClickBack) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { viewModel.act(SearchAction.ClickSearch) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
        }
    ) {
        when (val searchState = state.requestState) {
            SearchState.RequestState.EmptyResult -> {
                Text(
                    text = stringResource(R.string.nothing_was_found_for_your_request),
                    modifier = Modifier.padding(8.dp)
                )
            }

            SearchState.RequestState.Error -> {
                Text(
                    text = stringResource(R.string.something_went_wrong),
                    modifier = Modifier.padding(8.dp)
                )
            }

            SearchState.RequestState.Initial -> {

            }

            SearchState.RequestState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            is SearchState.RequestState.SuccessLoaded -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        items = searchState.cities,
                        key = { it.id }
                    ) { city ->
                        CityCard(
                            city = city,
                            onCityClick = viewModel::act
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CityCard(
    city: City,
    onCityClick: (SearchAction) -> Unit
) {
    Rebugger(
        trackMap = mapOf(
            "city" to city,
            "onCityClick" to onCityClick
        )
    )

    Card(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCityClick(SearchAction.ClickCity(city)) }
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text(text = city.country)
        }
    }
}