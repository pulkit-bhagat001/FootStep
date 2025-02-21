package com.user.shoppingapp.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.user.shoppingapp.common.Routes
import com.user.shoppingapp.common.SubNavigation
import com.user.shoppingapp.presentation.Viewmodel.UserViewModel
import kotlinx.coroutines.launch

val list = listOf(
    BottomItems(Icons.Filled.Home, Icons.Outlined.Home),
    BottomItems(Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    BottomItems(Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
    BottomItems(Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle),
)


@Composable
fun UserApp(
    firebaseAuth: FirebaseAuth,
    userViewModel: UserViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()

) { userViewModel.setUser(firebaseAuth.currentUser)
    val user by userViewModel.firebaseUser.collectAsState()

    var startScreen =
        if (user == null) SubNavigation.LoginAndSignupScreen else SubNavigation.MainHomeScreen
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen= backStackEntry?.destination?.route


    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (userViewModel.currentScreen != "ProductDetailsScreen") {
                BottomBar(
                    userViewModel,
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
                    ), onNavigate = {
                        when (it) {
                            0 -> {if(currentScreen!=Routes.HomeScreen::class.qualifiedName){
                                navController.navigate(Routes.HomeScreen)

                            }
                            }

                            1 -> {if(currentScreen!=Routes.WishListScreen::class.qualifiedName){
                                    navController.navigate(Routes.WishListScreen)
                            }}
                            2 -> {if(currentScreen!=Routes.CartScreen::class.qualifiedName){
                                navController.navigate(Routes.CartScreen)
                            }}
                            3 -> {
                                if(currentScreen!=Routes.ProfileScreen::class.qualifiedName){
                                    navController.navigate(Routes.ProfileScreen)

                                }
                            }
                        }
                    }
                )
            }
        }
    ) { padding ->
        val context= LocalContext.current
val scope= rememberCoroutineScope()


        NavHost(navController = navController, startDestination = startScreen, modifier = Modifier.padding(padding)) {
            navigation<SubNavigation.LoginAndSignupScreen>(Routes.SignUpScreen) {
                composable<Routes.LoginScreen> {
                    userViewModel.currentScreen = "ProductDetailsScreen"
                }
                composable<Routes.SignUpScreen> {
                    userViewModel.currentScreen = "ProductDetailsScreen"
                    SignUpScreen(
                        userViewModel,
                        onNavigate = { navController.navigate(Routes.HomeScreen) })
                }
            }
            navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen) {
                composable<Routes.HomeScreen> {
                    userViewModel.currentScreen = ""
                    HomeScreen(userViewModel, navController)
                }
                composable<Routes.ProductDetailsScreen> {
                    userViewModel.currentScreen = "ProductDetailsScreen"
                    FullProductInfo(userViewModel,navController, onHeartChanged = {
                      scope.launch {
                            val res=userViewModel.addToWishlistFromHomeScreen(it,true)
                            if(res.isNotEmpty()){
                                Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                            }
                        }


                    })
                }

            }
            composable<Routes.ProfileScreen> {
                userViewModel.currentScreen = ""
                ProfileScreen(userViewModel)
            }
            composable<Routes.BuyNowScreen> {
                userViewModel.currentScreen = "ProductDetailsScreen"
                val data=it.toRoute<Routes.BuyNowScreen>(
                )
                BuyNowScreen(id = data.id, name = data.name, category = data.category, image = data.image, regPrice = data.regPrice, finalPrice = data.finalPrice, noOfUnits = data.noOfUnits, discountPercent = data.discountPercent, color = data.color, size = data.size,userViewModel)
            }
            composable<Routes.CartScreen> {
                userViewModel.currentScreen = ""
                CartScreen(userViewModel, onNavigate = {
                    navController.navigate(Routes.ProductDetailsScreen)
                })
            }
            composable<Routes.WishListScreen> {
                userViewModel.currentScreen = ""
                WishListScreen(userViewModel, onNavigate = {
                    navController.navigate(Routes.ProductDetailsScreen)
                })
            }


        }


    }


}


@Composable
fun BottomBar(userViewModel: UserViewModel, modifier: Modifier, onNavigate: (Int) -> Unit) {

    Row(
        modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(15.dp)
            .background(Color(0xFF0A0A0A), shape = RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        list.forEachIndexed { index, bottomItems ->

            IconButton(
                onClick = {
                    userViewModel.index = index
                    onNavigate(index)
                },
                modifier = Modifier
                    .weight(1f)
                    .drawBehind {
                        if (userViewModel.index == index) {
                            drawCircle(color = Color(0xFFFDFFFF), radius = 80f)
                        }

                    }) {
                Icon(
                    imageVector = if (index == userViewModel.index) bottomItems.selectedIcon else bottomItems.unselectedIcon,
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp)
                        .drawBehind {
                            if (userViewModel.index == index) {
                                drawCircle(color = Color(0xFFFDFFFF), radius = 70f)
                            }

                        },
                    tint = if (userViewModel.index == index) Color(0xFF0A0A0A) else Color.White
                )
            }
        }


    }
}

data class BottomItems(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
