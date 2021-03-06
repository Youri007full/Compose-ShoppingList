package com.artf.shoppinglistcompose.ui.view.layout.currentList

import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.core.Modifier
import androidx.ui.core.TestTag
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.DrawerState
import androidx.ui.material.FloatingActionButton
import androidx.ui.material.IconButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Scaffold
import androidx.ui.material.ScaffoldState
import androidx.ui.material.TopAppBar
import androidx.ui.material.contentColorFor
import androidx.ui.res.stringResource
import androidx.ui.res.vectorResource
import androidx.ui.unit.dp
import com.artf.shoppinglistcompose.data.status.ResultStatus
import com.artf.shoppinglistcompose.R
import com.artf.shoppinglistcompose.ui.model.ScreenStateAmbient
import com.artf.shoppinglistcompose.ui.model.SharedViewModelAmbient
import com.artf.shoppinglistcompose.ui.model.model.ShoppingListUi
import com.artf.shoppinglistcompose.ui.model.model.compose.CurrentShoppingListModel
import com.artf.shoppinglistcompose.ui.model.model.compose.CurrentShoppingListModel.showDialogState
import com.artf.shoppinglistcompose.ui.view.layout.EmptyScreen
import com.artf.shoppinglistcompose.ui.view.value.TestTag.fab
import com.artf.shoppinglistcompose.ui.view.menu.AppDrawer
import com.artf.shoppinglistcompose.ui.view.menu.MainMenu

@Composable
fun ShoppingListCurrentScreen(
    scaffoldState: ScaffoldState = remember {
        ScaffoldState().apply { drawerState = CurrentShoppingListModel.drawerState }
    }
) {
    val screenState = ScreenStateAmbient.current.currentScreenState
    CurrentShoppingListModel.drawerState = scaffoldState.drawerState

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            AppDrawer(
                currentScreenState = screenState,
                closeDrawer = {
                    scaffoldState.drawerState = DrawerState.Closed
                    CurrentShoppingListModel.drawerState = scaffoldState.drawerState
                }
            )
        },
        topAppBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                backgroundColor = MaterialTheme.colors.primary,
                navigationIcon = {
                    IconButton(onClick = { scaffoldState.drawerState = DrawerState.Opened }) {
                        Icon(vectorResource(R.drawable.ic_baseline_menu_24))
                    }
                },
                actions = { MainMenu() }
            )
        },
        bodyContent = {
            ScreenBody()
            CreateShoppingListDialog()
        },
        floatingActionButton = { Fab() }
    )
}

@Composable
private fun ScreenBody() {
    when (val result = ScreenStateAmbient.current.shoppingListsUi) {
        is ResultStatus.Loading -> LoadingScreen()
        is ResultStatus.Success -> SuccessScreen(result.data)
        is ResultStatus.Error -> ErrorScreen()
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        gravity = ContentGravity.Center
    ) { CircularProgressIndicator() }
}

@Composable
private fun SuccessScreen(productList: List<ShoppingListUi>) {
    val sharedViewModel = SharedViewModelAmbient.current
    VerticalScroller {
        Column(Modifier.fillMaxWidth().padding(8.dp, 8.dp, 8.dp, 96.dp)) {
            productList.forEach { post -> ShoppingListCurrentItem(sharedViewModel, post) }
        }
    }
}

@Composable
private fun ErrorScreen() {
    EmptyScreen(
        R.string.empty_view_shopping_list_title,
        R.string.empty_view_shopping_list_subtitle
    )
}

@Composable
private fun Fab() {
    TestTag(tag = fab) {
        FloatingActionButton(
            onClick = { showDialogState = true },
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = contentColorFor(MaterialTheme.colors.onSecondary),
            elevation = 6.dp,
            icon = { Icon(vectorResource(R.drawable.ic_add_black_24dp)) }
        )
    }
}