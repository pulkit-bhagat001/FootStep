package com.user.shoppingapp.presentation.screens

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.user.shoppingapp.R
import com.user.shoppingapp.presentation.Viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

fun formatDateFor(currentTimeMillis: Long): String {
    val date = Date(currentTimeMillis)
    val format = SimpleDateFormat("dd/MM/yy")
    return format.format(date)

}


fun datePicker(context: Context, userViewModel: UserViewModel) {

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDate ->
        val actual = "$selectedDate/${selectedMonth + 1}/$selectedYear"
        userViewModel.selectedDate = actual

    }, year, month, day).show()
}


@Composable
fun BuyNowScreen(
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
    userViewModel: UserViewModel
) {
    val count by userViewModel.quantityCounter.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        userViewModel.getPhoneNumberAndAddress()
    }
    val phoneNumberAndAddress by userViewModel.phoneNumberAndAddress.collectAsState()
    val enabledButton by userViewModel.enabledButtonOrNot.collectAsState()
    val phoneNumberAndAddressResult by userViewModel.phoneNumberAndAddressResult.collectAsState()
    if (phoneNumberAndAddressResult.isLoading) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.5f)), contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.Black)
                Spacer(Modifier.height(10.dp))
                Text(
                    "Updating in Progress",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

        }

    } else if (phoneNumberAndAddressResult.error.isNotEmpty()) {
        Toast.makeText(context, phoneNumberAndAddressResult.error, Toast.LENGTH_SHORT).show()
        userViewModel.clearPhoneNumberAndResultState()

    } else if (phoneNumberAndAddressResult.success.isNotEmpty()) {
        Toast.makeText(context, phoneNumberAndAddressResult.success, Toast.LENGTH_SHORT).show()
        userViewModel.clearPhoneNumberAndResultState()
    }
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F1F1)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (phoneNumberAndAddress.isSuccess) {

            item {

                Column(
                    Modifier
                        .background(Color.White)
                        .padding(horizontal = 15.dp)
                ) {
                    Spacer(Modifier.height(30.dp))

                    Text(
                        text = "Saved Addresses",
                        Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline
                    )
                    Spacer(Modifier.height(15.dp))
                    BasicTextField(
                        textStyle = TextStyle(fontSize = 18.sp),
                        value = phoneNumberAndAddress.address,
                        onValueChange = {
                            userViewModel.updatePhoneNumberAndAddress(
                                "address",
                                it
                            )
                        },
                        readOnly = enabledButton,
                        modifier = Modifier.fillMaxWidth()
                    )
                    BasicTextField(
                        textStyle = TextStyle(fontSize = 18.sp),
                        value = phoneNumberAndAddress.phoneNumber,
                        onValueChange = {
                            userViewModel.updatePhoneNumberAndAddress(
                                "phoneNumber",
                                it
                            )
                        },
                        readOnly = enabledButton,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(15.dp))

                    if (enabledButton) {
                        Card(
                            onClick = { userViewModel.toggleButton() },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F61A7))
                           ,
                        ) {
                            Text(
                                text = "Edit Details",
                                Modifier
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                                    .fillMaxWidth(), textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }

                    } else {
                        Card(
                            onClick = {
                                userViewModel.update()
                                userViewModel.toggleButton()
                            },  colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            border = BorderStroke(width = 2.dp, color = Color(0xFF0F61A7))

                        ) {
                            Text(
                                text = "Save Changes",
                                Modifier
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                                    .fillMaxWidth(), textAlign = TextAlign.Center,
                                color = Color(0xFF0F61A7)
                            )
                        }

                    }
                }
                Spacer(Modifier.height(50.dp))
                CreateCard(
                    id,
                    name,
                    category,
                    image,
                    regPrice,
                    finalPrice,
                    noOfUnits,
                    discountPercent,
                    color,
                    size,
                    userViewModel
                )
            }

        }

        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
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
                        Text(text = "Rs.${regPrice.toInt() * count}")

                    }
                    Spacer(Modifier.height(5.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()

                    ) {
                        Text(text = "Discount")
                        Text(
                            text = "-Rs.${(regPrice.toInt() - finalPrice.toInt()) * count}",
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
                        if ((finalPrice.toInt() * count) < 600) {
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
                        Text(text = if ((finalPrice.toInt() * count) < 600) "Rs.${(finalPrice.toInt() * count) + 200}" else "Rs.${(finalPrice.toInt() * count)}")

                    }
                    Divider()


                }
            }
        }

    }
}

@Composable
fun CreateCard(
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
    userViewModel: UserViewModel
) {
    val count by userViewModel.quantityCounter.collectAsState()
    val context = LocalContext.current
    val checked by userViewModel.checked.collectAsState()

//--------------------------------------------
    Column(modifier = Modifier.background(Color.White)) {
        Row(
            Modifier
                .fillMaxWidth()

                .height(200.dp)
                .padding(horizontal = 5.dp),
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
                        Modifier.padding(7.dp)
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
                                    val message = userViewModel.decreaseQuantity()
                                    Toast
                                        .makeText(context, message, Toast.LENGTH_SHORT)
                                        .show()
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
                                    val result =
                                        userViewModel.increaseQuantity(availUnits = noOfUnits)
                                    Toast
                                        .makeText(context, result, Toast.LENGTH_SHORT)
                                        .show()
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

        Column(verticalArrangement = Arrangement.spacedBy((-10).dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checked, onCheckedChange = {
                    if (it) {
                        userViewModel.checkedToggle()
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
                    }"
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = !checked, onCheckedChange = {
                    if (it) {
                        userViewModel.checkedToggle()
                        datePicker(context, userViewModel)
                        Log.d("Inside Particular", it.toString())
                    }
                })
                Text(text = if (userViewModel.selectedDate.isEmpty()) "Select a particular Date" else "Get by ${userViewModel.selectedDate}")
            }
        }
    }


}


data class Cart(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val image: String = "",
    val regPrice: String = "",
    val finalPrice: String = "",
    val noOfUnits: Int = 0,
    val discountPercent: String = "",
    val color: String = "",
    val size: String = "",
    val seller:String="",
    var isFavourite:Boolean=false
)
data class QuantityAdded(
    val count:Int=0,
    val cart:Cart?=null,
    var isChecked:Boolean=true,
    var date:String=""
)