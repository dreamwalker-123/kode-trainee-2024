package com.example.kode.ui.screens.first

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.kode.R
import com.example.kode.data.remote.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File.separator
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel(), onSecondScreenClicked:(Item) -> Unit) {
    val inputText = viewModel.inputText.collectAsState()
    val state = viewModel.uiState.collectAsState()
    val error = viewModel.error.collectAsState()
    val selectedTab = viewModel.selectedTab.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var alphabetOrDay by remember { mutableIntStateOf(1) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading.value,
        onRefresh = viewModel::getItems
    )

    if (error.value) {
        Image(painter = painterResource(id = R.drawable.img_5),
            contentDescription = "ErrorImage",
            modifier = Modifier
                .padding(10.dp)
                .clickable { viewModel.getItems() }
        )
    } else {
        Scaffold(topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    TextField(
                        value = inputText.value,
                        onValueChange = { viewModel.changeString(it) },
                        label = { Text("Введи имя, тег, почту...") },
                        colors = textFieldColors(
                            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            errorContainerColor = MaterialTheme.colorScheme.primaryContainer)
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription ="Localized description",
                        modifier = Modifier.padding(10.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp,
                        )
                    )
            ) }
        )
        {   paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .pullRefresh(pullRefreshState),
            ) {
                TextTabs(selectedTab.value, onClick = { viewModel.setSelectedTab(it) })
                PullRefreshIndicator(
                    refreshing = isLoading.value,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    backgroundColor = if (isLoading.value) Color.LightGray else Color.Gray,
                )
                Column(modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()) {
                    Column(modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxSize()) {
                        Employees(
                            state = state.value,
                            selectedTab = selectedTab.value,
                            giveTheItemBack = { onSecondScreenClicked(it) },
                            alphabetOrDay = alphabetOrDay,
                            inputText = inputText.value
                        )
                    }
                }
            }
            if (showBottomSheet) {
                ModBotSheet(showBottomSheet = { showBottomSheet = it }, sheetState, scope, alphabetOrDay = { alphabetOrDay = it }, alphabetOrDay)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModBotSheet(
    showBottomSheet: (Boolean) -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
    alphabetOrDay: (Int) -> Unit,
    alphabetOrDay1: Int
) {
    ModalBottomSheet(
        onDismissRequest = {
            showBottomSheet(false)
        },
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Сортировка", modifier = Modifier.align(Alignment.CenterHorizontally))
            Column(
                Modifier
                    .selectableGroup()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 40.dp)) {
                Row {
                    RadioButton(
                        selected = alphabetOrDay1 == 1,
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                alphabetOrDay(1)
                                if (!sheetState.isVisible) {
                                    showBottomSheet(false)
                                }
                            } }
                    )
                    Text(text = "По алфавиту", modifier = Modifier.align(Alignment.CenterVertically))
                }
                Row {
                    RadioButton(
                        selected = alphabetOrDay1 == 2,
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                alphabetOrDay(2)
                                if (!sheetState.isVisible) {
                                    showBottomSheet(false)
                                }
                            } }
                    )
                    Text(text = "По дню рождения", modifier = Modifier.align(Alignment.CenterVertically))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Employees(
    state: MutableList<Pair<String, MutableList<Item>>>,
    selectedTab: Int,
    giveTheItemBack: (Item) -> Unit,
    alphabetOrDay: Int,
    inputText: String,

    ) {
    if (state[selectedTab].second.isEmpty()) {
        Text(text = "Designers", fontSize = TextUnit(25f, TextUnitType.Sp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(10) {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_2),
                        contentDescription = "ellipse",
                        modifier = Modifier
                            .height(80.dp)
                            .width(80.dp)
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_4),
                            contentDescription = "rectangle",
                            modifier = Modifier
                                .height(30.dp)
                                .width(150.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.img_3),
                            contentDescription = "rectangle2",
                            modifier = Modifier
                                .height(30.dp)
                                .width(100.dp)
                        )
                    }
                }
            }
        }
    } else {
        if (selectedTab != 0) {
            ListWithData(pair = state[selectedTab],
                giveTheItemBack = { giveTheItemBack(it) },
                alphabetOrDay = alphabetOrDay,
                inputText = inputText
            )
        } else {
            state.forEach{ ListWithData(
                pair = it,
                giveTheItemBack = {giveTheItemBack(it)},
                alphabetOrDay = alphabetOrDay,
                inputText = inputText
            )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListWithData(
    pair: Pair<String, MutableList<Item>>,
    giveTheItemBack: (Item) -> Unit,
    alphabetOrDay: Int,
    inputText: String
) {
    Text(text = pair.first, fontSize = TextUnit(25f, TextUnitType.Sp))

    // сортировка по алфавиту, дню рождения
    lateinit var list: List<Item>
    if (alphabetOrDay == 1) {
        list = pair.second.sortedBy { it.firstName }
    } else {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)
        val dateNow = current.split("-")
        val indexOfDate = (dateNow[1].toInt() * 100) + dateNow.last().toInt()

        val fromJanuaryToDecember = pair.second.sortedBy { (it.birthday.split("-")[1].toInt() * 100) + (it.birthday.split("-")[2].toInt()) }
        var index = 0
        for (i in fromJanuaryToDecember.indices) {
            if ((fromJanuaryToDecember[i].birthday.split("-")[1].toInt() * 100) + (fromJanuaryToDecember[i].birthday.split("-")[2].toInt()) > indexOfDate) {
                index = i
                break
            }
        }

        val mutList = fromJanuaryToDecember.subList(index, fromJanuaryToDecember.size).toMutableList()
        mutList.addAll(fromJanuaryToDecember.subList(0, index))
        list = mutList
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (inputText.isNotEmpty()) {
            list = list.filter { it.firstName.lowercase().contains(inputText.lowercase()) ||
                    it.lastName.contains(inputText.lowercase()) }
        }
        if (list.isEmpty()) {
            item {
                Image(painter = painterResource(id = R.drawable.img_6),
                    contentDescription = "if list is empty",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp))
            }
        } else {
            items(list) {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxSize()
                        .clickable { giveTheItemBack(it) }
                ) {
                    AsyncImage(
                        model = it.avatarUrl,
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(80.dp)
                            .width(80.dp)
                            .clip(CircleShape)
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 10.dp)
                    ) {
                        Row {
                            Text(text = "${it.firstName} ${it.lastName}", fontSize = 20.sp)
                            Text(
                                text = it.userTag,
                                fontSize = 11.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                        Text(text = it.position, fontSize = 15.sp)
                    }
                    if (alphabetOrDay == 2) {
                        Spacer(modifier = Modifier.weight(1f))
                        val date = it.birthday.split("-")
                        var month = "янв"
                        when (date[1]) {
                            "02" -> {
                                month = "фев"
                            }

                            "03" -> {
                                month = "мар"
                            }

                            "04" -> {
                                month = "апр"
                            }

                            "05" -> {
                                month = "май"
                            }

                            "06" -> {
                                month = "июн"
                            }

                            "07" -> {
                                month = "июл"
                            }

                            "08" -> {
                                month = "авг"
                            }

                            "09" -> {
                                month = "сен"
                            }

                            "10" -> {
                                month = "окт"
                            }

                            "11" -> {
                                month = "ноя"
                            }

                            "12" -> {
                                month = "дек"
                            }
                        }
                        Text(
                            text = "${date.last()} $month",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextTabs(selectedTab: Int, onClick: (Int) -> Unit) {
    val titles = listOf("Все", "Дизайн", "Аналитика", "Менеджмент", "iOS", "Android", "Бэк-офис",
        "Frontend", "Backend", "HR", "PR", "QA", "Техподдержка")
    Column {
        ScrollableTabRow(selectedTabIndex = selectedTab) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onClick(index) },
                    text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewMain() {
    MainScreen(onSecondScreenClicked = {} )
}