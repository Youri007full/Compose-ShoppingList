package com.artf.shopinglistcompose.ui.view.layout.archivedList

import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.*
import androidx.ui.res.stringResource
import androidx.ui.res.vectorResource
import androidx.ui.unit.dp
import com.artf.data.database.model.ShoppingList
import com.artf.shopinglistcompose.R
import com.artf.shopinglistcompose.ui.data.ScreenBackStackAmbient
import com.artf.shopinglistcompose.ui.data.SharedViewModelAmbient
import com.artf.shopinglistcompose.util.observer

@Composable
fun ArchivedProductListScreen(
    shoppingList: ShoppingList,
    scaffoldState: ScaffoldState = remember { ScaffoldState() }
) {
    val backStack = ScreenBackStackAmbient.current
    Scaffold(
        scaffoldState = scaffoldState,
        topAppBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                backgroundColor = MaterialTheme.colors.primary,
                navigationIcon = {
                    IconButton(onClick = { backStack.pop() }) {
                        Icon(vectorResource(R.drawable.ic_baseline_arrow_back_24))
                    }
                }
            )
        },
        bodyContent = { ScreenBody(shoppingList) }
    )
}

@Composable
private fun ScreenBody(shoppingList: ShoppingList) {
    val sharedViewModelAmbient = SharedViewModelAmbient.current
    sharedViewModelAmbient.onShoppingListClick(shoppingList)
    val productList = observer(sharedViewModelAmbient.productListUi)

    VerticalScroller {
        Column(Modifier.fillMaxWidth().padding(8.dp, 8.dp, 8.dp, 96.dp)) {
            productList?.forEach { post -> ArchivedProductItem(sharedViewModelAmbient, post) }
        }
    }
}