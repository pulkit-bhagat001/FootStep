package com.user.shoppingapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.user.shoppingapp.domain.models.PartialInfo
import com.user.shoppingapp.presentation.Viewmodel.UserViewModel

@Composable
fun WishListScreen(userViewModel: UserViewModel,onNavigate:()->Unit) {
    val wishList by userViewModel.wishlist.collectAsState()
LazyColumn {
    if(wishList.isNotEmpty()){
        items(wishList){
            CreateWishCard(it.id,it.name,it.category,it.imageUrl,it.basePrice,it.seller,userViewModel,onNavigate)
            Spacer(Modifier.height(25.dp))
        }
    }
}
}
@Composable
fun CreateWishCard(
    id: String,
    name: String,
    category: String,
    image: String,
    basePrice: String,
    seller: String,
    userViewModel: UserViewModel,
    onNavigate:()->Unit
) {
    val context = LocalContext.current

//--------------------------------------------

        Column(modifier = Modifier.background(Color.White)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 5.dp).clickable {
                        userViewModel.resetColorAndSizeAndProductInfo()
                        userViewModel.getColorAndSizeList(id)
                        userViewModel.updatePartialInfo(PartialInfo(id,name,category,image,seller,basePrice,true))
                        onNavigate()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier.weight(1.4f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(
                                0xFFE8E8E8
                            )
                        )
                    ) {
                        AsyncImage(
                            model = image,
                            contentDescription = "",
                            Modifier.padding(7.dp).fillMaxWidth().height(120.dp)
                        )


                    }

                }
                Column(
                    Modifier
                        .weight(2.2f)
                        .padding(start = 7.dp, top = 25.dp, bottom = 25.dp)
                        .fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Box() {
                        Row {

                            repeat(5) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(15.dp)
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
                                    ),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "Category: $category",
                        fontWeight = FontWeight.W500,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "From Rs.$basePrice",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif
                        )


                    }
                    Text(
                        text = "Sold By: $seller",
                        fontWeight = FontWeight.W500,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                }


            }


        }
    }

