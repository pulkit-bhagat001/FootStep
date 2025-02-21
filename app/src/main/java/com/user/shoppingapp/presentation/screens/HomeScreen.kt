package com.user.shoppingapp.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.user.shoppingapp.R
import com.user.shoppingapp.common.Routes
import com.user.shoppingapp.domain.models.PartialInfo
import com.user.shoppingapp.presentation.Viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val catList= listOf(
    Category("Shoes", R.drawable.shoes1),
    Category("Boots", R.drawable.boot),
    Category("Flip Flops", R.drawable.slipper),
    Category("Sandal", R.drawable.sandal),
    Category("Heels", R.drawable.heels),
)

data class Category(
    val name:String,
    @DrawableRes val icon:Int
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(userViewModel: UserViewModel, navController: NavController) {

    val wishlist by userViewModel.wishlist.collectAsState()
    Log.e("WishList",wishlist.toString())
    val nameList by userViewModel.nameList.collectAsState()
    val bannerList by userViewModel.bannerList.collectAsState()

    //val count by userViewModel.count.collectAsState()
   // Log.d("count",count.toString())
    LaunchedEffect(key1 = Unit) {
            userViewModel.getListOfPartialInfo("Shoes")
        userViewModel.getBanner()
    }
    val list by userViewModel.homeScreenState.collectAsState()

    Column {

        SearchBar(query = userViewModel.searchBar, onQueryChange = {
            userViewModel.searchBar = it
            userViewModel.getNameList(it)
        }, onSearch = {}, active = userViewModel.active, onActiveChange = {
            userViewModel.active = it
            userViewModel.searchBarIsExpanded=!userViewModel.searchBarIsExpanded
            if (!it) {
                userViewModel.clearList()
            }
        },
            content = {
                if (nameList.isNotEmpty()&&userViewModel.searchBar.isNotEmpty()) {
                    nameList.fastForEach {
                        Divider()
                        Text(it)
                        Divider()
                    }
                } else {
                    Text(text = "")
                }
            },
            leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "") },
            placeholder = { Text(text = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (userViewModel.searchBarIsExpanded) 0.dp else 10.dp)

        )

        Spacer(Modifier.heightIn(25.dp))
        Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            catList.fastForEach {
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(shape = RoundedCornerShape(70.dp), modifier = Modifier.clip(RoundedCornerShape(70.dp)).clickable {  }) {
                        Image(
                            painter = painterResource(it.icon),
                            contentDescription = "",
                            modifier = Modifier.size(70.dp).padding(5.dp)
                        )

                    }
                    Text(it.name, fontSize = 14.sp)
                }
            }
        }
        Spacer(Modifier.heightIn(25.dp))

        if(bannerList.isNotEmpty()){
            SlidingPager(bannerList)
        }
        Spacer(Modifier.heightIn(17.dp))
        Text(text = "Flash Sale",Modifier.padding(10.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {


            item {
                if (list.success.isNotEmpty()) {
                    LazyRow {
                        items(list.success) {
                            CreateFlashSaleCard(
                                it,
                                userViewModel,
                                onNavigate = { navController.navigate(Routes.ProductDetailsScreen) })
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun SlidingPager(listImages:List<String>) {


    val pagerState = rememberPagerState(pageCount = { listImages.size })
    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(2000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }
    HorizontalPager(state = pagerState,modifier = Modifier.padding(horizontal = 10.dp)) {
        Column {
            Card(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    ,
                shape = RoundedCornerShape(15.dp)
            ) {
                AsyncImage(
                    model = listImages[it], contentDescription = "",
                    contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize()
                )
            }

        }
    }
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) {
            ShowDotIndicator(isSelected = it == pagerState.currentPage)

        }
    }


}

@Composable
fun ShowDotIndicator(isSelected: Boolean) {
    val animateSize by animateDpAsState(targetValue = if (isSelected) 7.dp else 5.dp, label = "")
    val animateBg by animateColorAsState(
        targetValue = if (isSelected) Color.Black else Color.Gray,
        label = ""
    )

    Box(modifier = Modifier
        .padding(2.dp)
        .size(animateSize)
        .clip(CircleShape)
        .background(animateBg))

}


@Composable
fun CreateFlashSaleCard(
    partialInfo: PartialInfo,
    userViewModel: UserViewModel,
    onNavigate: () -> Unit
) {
    val context=LocalContext.current
    val scope= rememberCoroutineScope()

    Log.d("Partial Info1",partialInfo.toString())



    Column(
        modifier = Modifier.padding(end = 15.dp)
            .width(170.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color(0xFFD4D4D4),
                    style = Stroke(width = 2.dp.toPx()),
                    cornerRadius = CornerRadius(30f, 30f)
                )
            }
            .padding(10.dp)
            .clickable {
                userViewModel.resetColorAndSizeAndProductInfo()
                userViewModel.getColorAndSizeList(partialInfo.id)
                userViewModel.updatePartialInfo(partialInfo)
                onNavigate()

            }
    ) {
        Card(

            modifier = Modifier
                .width(170.dp)
                .height(170.dp)

        ) {
            Box(Modifier.fillMaxSize()) {
                AsyncImage(
                    model = partialInfo.imageUrl, contentDescription = "", modifier = Modifier
                        .width(170.dp)
                        .height(170.dp)
                        .padding(top = 20.dp)
                )

                IconButton(
                    onClick = {
                        scope.launch {
                            val result=userViewModel.addToWishlistFromHomeScreen(partialInfo.id,false)
                            if(result.isNotEmpty()){
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()

                            }
                        }

                              },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (partialInfo.isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                }
            }


        }
        Text(text = partialInfo.name, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        Text(
            text = "Category: ${partialInfo.category}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = "From Rs.${partialInfo.basePrice}",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(
                0xFF535353
            )
        )

    }
}


