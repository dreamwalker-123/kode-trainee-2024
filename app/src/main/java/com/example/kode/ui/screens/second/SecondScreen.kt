package com.example.kode.ui.screens.second

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.sharp.Call
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kode.data.remote.Item
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SecondScreen(backClick:() -> Unit, item: Item) {
    val mContext = LocalContext.current
    Column {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .weight(4f))
        {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(20.dp))
                AsyncImage(model = item.avatarUrl,
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(150.dp)
                        .width(150.dp)
                        .clip(CircleShape))
                Text(text = "${item.firstName} ${item.lastName}",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp),
                    fontSize = 25.sp)
                Text(text = item.position,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    fontSize = 15.sp)

            }
            IconButton(onClick = { backClick() }, modifier = Modifier) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Localized description"
                )
            }
        }
        Column(modifier = Modifier.weight(7f)) {
            Row(modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Sharp.Star,
                    contentDescription = "Localized description"
                )
                val date = item.birthday.split("-")
                var month = "января"
                when(date[1]) {
                    "01" -> { month = "января"}
                    "02" -> { month = "февраля"}
                    "03" -> { month = "марта"}
                    "04" -> { month = "апреля"}
                    "05" -> { month = "мая"}
                    "06" -> { month = "июня"}
                    "07" -> { month = "июля"}
                    "08" -> { month = "августа"}
                    "09" -> { month = "сентября"}
                    "10" -> { month = "октября"}
                    "11" -> { month = "ноября"}
                    "12" -> { month = "декабря"}
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "${date.last()} $month ${date.first()}",
                    modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.weight(1f))

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val current = LocalDateTime.now().format(formatter)
                val dateNow = current.split("-")

                val age = (((dateNow.first()
                    .toInt() * 10000) + (dateNow[1].toInt() * 100) + dateNow.last()
                    .toInt()) - ((date.first()
                    .toInt() * 10000) + (date[1].toInt() * 100) + date.last().toInt())) / 10000
                var str = "лет"
                if (age.toString().last() == '1') {
                    str = "год"
                } else if (age.toString().last() == '1' || age.toString().last() == '2' ||
                    age.toString().last() == '3' || age.toString().last() == '4') {
                    str = "года"
                }
                Text(text = "$age $str", modifier = Modifier.align(Alignment.CenterVertically))
            }
            Row(modifier = Modifier
                .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .clickable {
                    val phoneNumber = item.phone.filter { it != '-' }
                    val callIntent: Intent = Uri.parse("tel:${phoneNumber}").let { number ->
                            Intent(Intent.ACTION_DIAL, number) }

                    mContext.startActivity(callIntent)
                }) {
                Icon(
                    imageVector = Icons.Sharp.Call,
                    contentDescription = "Localized description"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = item.phone,
                    modifier = Modifier.align(Alignment.CenterVertically))
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewSecondScreen() {
    SecondScreen(backClick = {},
        item = Item(
            id = "e0fceffa-cef3-45f7-97c6-6be2e3705927",
            phone ="802-623-1785",
            userTag = "LK",
            birthday = "2004-08-02",
            lastName = "Reichert",
            position = "Technician",
            avatarUrl = "https://i.pravatar.cc/150?img=1",
            firstName = "Dee",
            department = "back_office")
    )
}