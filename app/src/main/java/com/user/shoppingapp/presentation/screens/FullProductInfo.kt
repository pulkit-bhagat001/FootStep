package com.user.shoppingapp.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.user.shoppingapp.common.Routes
import com.user.shoppingapp.presentation.Viewmodel.UserViewModel


@Composable
fun FullProductInfo(
    userViewModel: UserViewModel,
    navController: NavHostController,
    onHeartChanged: (String) -> Unit

) {
    val context = LocalContext.current
    val colorAndSizeList by userViewModel.colorAndSizeList.collectAsState()
    val partialInfo by userViewModel.holdPartialInfo.collectAsState()
    val fullProductInfo by userViewModel.completeProductInfo.collectAsState()

    LaunchedEffect(key1 = userViewModel.colorIndex, key2 = userViewModel.sizeIndex) {

        if (userViewModel.colorIndex.isNotEmpty() && userViewModel.sizeIndex.isNotEmpty()) {
            userViewModel.getCompleteInfo(
                partialInfo.id,
                userViewModel.colorIndex,
                userViewModel.sizeIndex
            )
        }
    }

    if (fullProductInfo.error.isNotEmpty() && fullProductInfo.success == null) {
        Toast.makeText(context, fullProductInfo.error, Toast.LENGTH_SHORT).show()

    }
    if (fullProductInfo.success != null && fullProductInfo.error.isEmpty()) {

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(model = fullProductInfo.success!!.imageUrl, contentDescription = "")
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            ) {
                LazyColumn(
                    Modifier
                        .background(Color(0xFFEAEAEA))
                        .padding(15.dp)
                        .padding()
                ) {

                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = fullProductInfo.success!!.name,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                fontFamily = FontFamily.SansSerif
                            )
                            IconButton(onClick = {
                                onHeartChanged(partialInfo.id)

                            }) {
                                Icon(
                                    imageVector = if (partialInfo.isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "",
                                    tint = Color(0xFF1168B6)
                                )
                            }
                        }

                    }
                    item { Spacer(Modifier.height(8.dp)) }
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box() {
                                Row {

                                    repeat(5) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = "",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                                Row {

                                    repeat(4) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = "",
                                            tint = Color(
                                                0xFFFF9900
                                            )
                                        )
                                    }
                                }
                            }
                            Column {
                                Text(
                                    text = "Rs.${fullProductInfo.success!!.variant!!.finalPrice}",
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Rs.${fullProductInfo.success!!.variant!!.regularPrice}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Gray,
                                    textDecoration = TextDecoration.LineThrough,
                                    modifier = Modifier.padding(start = 20.dp)
                                )
                            }


                        }


                    }
                    item { Spacer(Modifier.height(12.dp)) }
                    item {
                        Text(
                            text = "Description",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E1F22)
                        )
                    }

                    item { Spacer(Modifier.height(5.dp)) }
                    item {
                        Text(
                            text = fullProductInfo.success!!.desc,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black, lineHeight = 15.sp
                        )
                    }
                    item { Spacer(Modifier.height(10.dp)) }

                    item {
                        Text(
                            text = "Colour",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E1F22)
                        )
                    }
                    item {
                        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                            if (colorAndSizeList.second.isNotEmpty()) {
                                colorAndSizeList.second.forEach { new ->
                                    TextButton(
                                        onClick = {
                                            userViewModel.colorIndex = new
                                            Log.d("Selected color", userViewModel.colorIndex)

                                        },
                                        Modifier.padding(horizontal = 5.dp),
                                        shape = RoundedCornerShape(9.dp),
                                        border = if (userViewModel.colorIndex == new) {
                                            BorderStroke(width = 2.dp, color = Color(0xFF1168B6))
                                        } else {
                                            BorderStroke(width = 2.dp, color = Color.Gray)
                                        }
                                    ) {
                                        Text(
                                            text = new,
                                            color = if (userViewModel.colorIndex == new) Color(
                                                0xFF1168B6
                                            ) else Color.Gray,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                        }
                    }
                    item { Spacer(Modifier.height(10.dp)) }

                    item {
                        Text(
                            text = "Size",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E1F22),

                            )
                    }
                    item {
                        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                            if (colorAndSizeList.first.isNotEmpty()) {
                                colorAndSizeList.first.forEach { new ->
                                    TextButton(
                                        onClick = {
                                            userViewModel.sizeIndex = new
                                            Log.d("Selected size", userViewModel.sizeIndex)
                                        },
                                        Modifier.padding(horizontal = 5.dp),
                                        shape = RoundedCornerShape(9.dp),
                                        border = if (userViewModel.sizeIndex == new) BorderStroke(
                                            width = 2.dp, color = Color(
                                                0xFF1168B6
                                            )
                                        ) else BorderStroke(width = 2.dp, color = Color.Gray),
                                        colors = if (userViewModel.sizeIndex == new) {
                                            ButtonDefaults.buttonColors(

                                                containerColor = Color(0xFF1168B6)


                                            )
                                        } else {
                                            ButtonDefaults.buttonColors(

                                                containerColor = Color.Transparent


                                            )
                                        }
                                    ) {
                                        Text(
                                            text = new,
                                            color = if (userViewModel.sizeIndex == new) Color.White else Color.Gray,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                        }
                    }
                    item { Spacer(Modifier.height(15.dp)) }
                    item {
                        OutlinedButton(
                            onClick = {
                                navController.navigate(
                                    Routes.BuyNowScreen(
                                        id = fullProductInfo.success!!.id,
                                        name = fullProductInfo.success!!.name,
                                        category = fullProductInfo.success!!.category,
                                        image = fullProductInfo.success!!.imageUrl,
                                        regPrice = fullProductInfo.success!!.variant!!.regularPrice,
                                        finalPrice = fullProductInfo.success!!.variant!!.finalPrice,
                                        noOfUnits = fullProductInfo.success!!.variant!!.availableUnits,
                                        color = userViewModel.colorIndex,
                                        size = userViewModel.sizeIndex,
                                        discountPercent = fullProductInfo.success!!.variant!!.discountPercentage
                                    )
                                )
                            },
                            Modifier
                                .fillMaxWidth()
                                .height(45.dp),
                            colors = ButtonDefaults.buttonColors(),
                            border = null,
                            shape = RoundedCornerShape(9.dp)
                        ) {
                            Text(text = "Buy Now")
                        }
                    }
                    item { Spacer(Modifier.height(15.dp)) }
                    item {
                        OutlinedButton(
                            onClick = {
                                userViewModel.addToCart(
                                    Cart(
                                        fullProductInfo.success!!.id,
                                        fullProductInfo.success!!.name,
                                        fullProductInfo.success!!.category,
                                        fullProductInfo.success!!.imageUrl,
                                        fullProductInfo.success!!.variant!!.regularPrice,
                                        fullProductInfo.success!!.variant!!.finalPrice,
                                        fullProductInfo.success!!.variant!!.availableUnits,
                                        fullProductInfo.success!!.variant!!.discountPercentage,
                                        userViewModel.colorIndex,
                                        userViewModel.sizeIndex,
                                        fullProductInfo.success!!.seller,
                                        partialInfo.isFavourite
                                    )
                                )
                            },
                            Modifier
                                .fillMaxWidth()
                                .height(45.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            border = BorderStroke(width = 2.dp, color = Color(0xFF1168B6)),
                            shape = RoundedCornerShape(9.dp)
                        ) {
                            Text(text = "Add to Cart", color = Color(0xFF1168B6))
                        }
                    }
                    item { Spacer(Modifier.height(100.dp)) }
                }
            }
        }
    } else {


        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(model = partialInfo.imageUrl, contentDescription = "")
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEAEAEA))
            ) {
                LazyColumn(
                    Modifier.background(Color(0xFFEAEAEA))
                        .padding(15.dp)
                        .padding()
                ) {

                    item {
                        Text(
                            text = partialInfo.name,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                    item { Spacer(Modifier.height(10.dp)) }
                    item {
                        Text(
                            text = "Category: ${partialInfo.category}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    }
                    item { Spacer(Modifier.height(5.dp)) }

                    item { Spacer(Modifier.height(10.dp)) }

                    item {
                        Text(
                            text = "Colour",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E1F22)
                        )
                    }
                    item {
                        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                            if (colorAndSizeList.second.isNotEmpty()) {
                                colorAndSizeList.second.forEach { new ->
                                    TextButton(
                                        onClick = {
                                            userViewModel.resetCompleteProductInfo()
                                            userViewModel.colorIndex = new
                                            Log.d("Selected color", userViewModel.colorIndex)

                                        },
                                        Modifier.padding(horizontal = 5.dp),
                                        shape = RoundedCornerShape(9.dp),
                                        border = if (userViewModel.colorIndex == new) {
                                            BorderStroke(width = 2.dp, color = Color(0xFFFF4356))
                                        } else {
                                            BorderStroke(width = 2.dp, color = Color.Gray)
                                        }
                                    ) {
                                        Text(
                                            text = new,
                                            color = if (userViewModel.colorIndex == new) Color(
                                                0xFFFF4356
                                            ) else Color.Gray,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                        }
                    }
                    item { Spacer(Modifier.height(10.dp)) }

                    item {
                        Text(
                            text = "Size",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E1F22),

                            )
                    }
                    item {
                        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                            if (colorAndSizeList.first.isNotEmpty()) {
                                colorAndSizeList.first.forEach { new ->
                                    TextButton(
                                        onClick = {
                                            userViewModel.resetCompleteProductInfo()
                                            userViewModel.sizeIndex = new
                                            Log.d("Selected size", userViewModel.sizeIndex)
                                        },
                                        Modifier.padding(horizontal = 5.dp),
                                        shape = RoundedCornerShape(9.dp),
                                        border = if (userViewModel.sizeIndex == new) BorderStroke(
                                            width = 2.dp, color = Color(
                                                0xFF402222
                                            )
                                        ) else BorderStroke(width = 2.dp, color = Color.Gray),
                                        colors = if (userViewModel.sizeIndex == new) {
                                            ButtonDefaults.buttonColors(

                                                containerColor = Color(0xFFFF4356)


                                            )
                                        } else {
                                            ButtonDefaults.buttonColors(

                                                containerColor = Color.Transparent


                                            )
                                        }
                                    ) {
                                        Text(
                                            text = new,
                                            color = if (userViewModel.sizeIndex == new) Color.White else Color.Gray,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                        }
                    }
                    item { Spacer(Modifier.height(15.dp)) }
                    item {
                        Text(
                            text = "From Rs.${partialInfo.basePrice}",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    item { Spacer(Modifier.height(15.dp)) }
                    item {
                        Text(
                            text = "Select a variant to see more details",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
