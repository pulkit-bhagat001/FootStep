package com.user.shoppingapp.presentation.screens

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.user.shoppingapp.R
import com.user.shoppingapp.domain.models.PartialInfo
import com.user.shoppingapp.presentation.Viewmodel.UserViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun newDatePicker(context: Context, userViewModel: UserViewModel,size: String,color: String,itemId:String) {

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDate ->
        val actual = "$selectedDate/${selectedMonth + 1}/$selectedYear"
        userViewModel.updateDateForParticularItem(size,color,actual,itemId)

    }, year, month, day).show()
}

@Composable
fun CartScreen(userViewModel: UserViewModel,onNavigate:()->Unit) {
    val completeList by userViewModel.cartlist.collectAsState()
    val priceDetails by userViewModel.priceDetails.collectAsState()
    if(completeList.isEmpty()){
        Text(text = "Cart List is Empty")
    }else{
        LazyColumn(Modifier.fillMaxSize()) {
            items(completeList){
                CreateCards(it.cart!!.id,it.cart.name,it.cart.category,it.cart.image,it.cart.regPrice,it.cart.finalPrice,it.cart.noOfUnits,it.cart.discountPercent,it.cart.color,it.cart.size,userViewModel,it.count,it.isChecked,it.date,it.cart.seller,it.cart.isFavourite,onNavigate)
                Spacer(Modifier.height(15.dp))
            }
            if(priceDetails.regPrice.toInt()!=0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 30.dp)) {
                            Text(
                                text = "Price Details",
                                color = Color.Black,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                            Spacer(Modifier.height(20.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Price")
                                Text(text = "Rs.${priceDetails.regPrice}")

                            }
                            Spacer(Modifier.height(5.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()

                            ) {
                                Text(text = "Discount")
                                Text(
                                    text = "-Rs.${priceDetails.discount}",
                                    color = Color(0xFF1D8D20)
                                )

                            }
                            Spacer(Modifier.height(5.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()

                            ) {
                                Text(text = "Delivery Charges")
                                if (priceDetails.finalPrice.toInt() < 600) {
                                    Row {
                                        Text(
                                            text = "Rs.200"
                                        )


                                    }
                                } else {
                                    Row {
                                        Text(
                                            text = "Free Delivery",
                                            color = Color(0xFF1D8D20)
                                        )
                                    }
                                }

                            }
                            Spacer(Modifier.height(20.dp))
                            Divider()
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()

                            ) {
                                Text(text = "Total Amount", color = Color.Black)
                                Text(text = if (priceDetails.finalPrice.toInt() < 600) "Rs.${priceDetails.finalPrice.toInt() + 200}" else "Rs.${priceDetails.finalPrice}")

                            }
                            Divider()


                        }
                    }
                }
            }
        }
    }

}

@Composable
fun CreateCards(
    id: String = "",
    name: String = "",
    category: String = "",
    image: String = "",
    regPrice: String = "",
    finalPrice: String = "",
    noOfUnits: Int = 0,
    discountPercent: String = "",
    color: String = "",
    size: String = "",
    userViewModel: UserViewModel,
    count: Int = 0,
    checked: Boolean,
    date: String,
    seller:String,
    isFavor:Boolean,
    onNavigate:()->Unit
) {
    val context = LocalContext.current

//--------------------------------------------
    if(count!=0) {
        Column(modifier = Modifier.background(Color.White)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 5.dp).clickable {
                        userViewModel.resetColorAndSizeAndProductInfo()
                        userViewModel.getColorAndSizeList(id)
                        userViewModel.updatePartialInfo(PartialInfo(id,name,category,image,seller,finalPrice,isFavor))
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
                    Spacer(Modifier.height(10.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFE8E8E8)), contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround

                        ) {
                            Card(colors = CardDefaults.cardColors(containerColor = Color.White),
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(0.dp)
                                    )
                                    .clickable {
                                        val message =
                                            userViewModel.increaseOrDecreaseQuantityForEachItem(
                                                noOfUnits,
                                                size,
                                                color,
                                                id,
                                                false
                                            )
                                        if (message.isNotEmpty()) {
                                            Toast
                                                .makeText(context, message, Toast.LENGTH_SHORT)
                                                .show()
                                        }

                                    }) {
                                Icon(
                                    painter = painterResource(R.drawable.minus),
                                    contentDescription = "",
                                    tint = Color(0xFF1168B6)
                                )
                            }
                            Text(
                                text = count.toString(),
                                modifier = Modifier.padding(horizontal = 7.dp),
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1168B6)),
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(0.dp)
                                    )
                                    .clickable {
                                        val message =
                                            userViewModel.increaseOrDecreaseQuantityForEachItem(
                                                noOfUnits,
                                                size,
                                                color,
                                                id,
                                                true
                                            )
                                        if (message.isNotEmpty()) {
                                            Toast
                                                .makeText(context, message, Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
                Column(
                    Modifier
                        .weight(2.2f)
                        .padding(start = 7.dp, top = 17.dp, bottom = 25.dp)
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

                    Text(
                        text = "$color | $size",
                        fontWeight = FontWeight.W500,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.ArrowDownward,
                                contentDescription = "",
                                tint = Color(0xFF1D8D20)
                            )
                            Text(
                                text = "$discountPercent%",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.SansSerif,
                                color = Color(0xFF1D8D20)
                            )
                        }
                        Text(
                            text = "Rs.${regPrice.toInt() * count}",

                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Rs.${finalPrice.toInt() * count}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif
                        )


                    }

                }


            }

            Column(verticalArrangement = Arrangement.spacedBy((-20).dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = checked, onCheckedChange = {
                        if (it) {
                            userViewModel.checkToggleForCart(size, color, id)
                            Log.d("Inside Tomorrow", it.toString())

                        }
                    })
                    Text(
                        text = "Get by Tomorrow ${
                            formatDateFor(
                                System.currentTimeMillis() + TimeUnit.DAYS.toMillis(
                                    1.toLong()
                                )
                            )
                        }", fontSize = 14.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = !checked, onCheckedChange = {
                        if (it) {
                            userViewModel.checkToggleForCart(size, color, id)
                            newDatePicker(context, userViewModel, size, color, id)
                            Log.d("Inside Particular", it.toString())
                        }
                    })
                    Text(
                        text = if (date.isEmpty()) "Select a particular Date" else "Get by ${date}",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

}