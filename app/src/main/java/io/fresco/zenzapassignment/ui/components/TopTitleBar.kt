package io.fresco.zenzapassignment.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.fresco.zenzapassignment.R
import io.fresco.zenzapassignment.ui.screens.mainscreen.MainViewModel
import io.fresco.zenzapassignment.ui.theme.DeepBlue

@Composable
fun TopTitleBar(title: String) {
    val viewModel: MainViewModel = hiltViewModel()
    TopTitleBarContent(
        viewModel,
        title = title,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopTitleBarContent(
    viewModel: MainViewModel,
    title: String,
) {
    val inProgress by viewModel.inProgress.collectAsState()
    TopAppBar(
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_stocks),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(start = 15.dp)
                    .size(30.dp),
                colorFilter = ColorFilter.tint(DeepBlue),
            )
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.padding(start = 6.dp),
            )
        },
        actions = {
            if (inProgress) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .size(24.dp),
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTopTitleBar() {
    TopTitleBarContent(
        hiltViewModel(),
        title = stringResource(id = R.string.title_text),
    )
}
