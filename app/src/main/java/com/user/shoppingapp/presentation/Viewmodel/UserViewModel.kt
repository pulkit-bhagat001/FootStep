package com.user.shoppingapp.presentation.Viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.user.shoppingapp.common.Events
import com.user.shoppingapp.common.Events1
import com.user.shoppingapp.data.db.Favourite
import com.user.shoppingapp.data.db.FavouriteDao
import com.user.shoppingapp.domain.models.Category
import com.user.shoppingapp.domain.models.PartialInfo
import com.user.shoppingapp.domain.models.Product
import com.user.shoppingapp.domain.useCase.AllCategoriesUseCase
import com.user.shoppingapp.domain.useCase.AuthenticateUser
import com.user.shoppingapp.domain.useCase.GetBannerImages
import com.user.shoppingapp.domain.useCase.GetColorAndSizeList
import com.user.shoppingapp.domain.useCase.GetCompleteInfo
import com.user.shoppingapp.domain.useCase.GetListOfPartialInfo
import com.user.shoppingapp.domain.useCase.GetNamesOfProducts
import com.user.shoppingapp.domain.useCase.GetProfileDetails
import com.user.shoppingapp.domain.useCase.GetUserAddressAndPhoneNumber
import com.user.shoppingapp.domain.useCase.UpdateUserAddressAndPhoneNumber
import com.user.shoppingapp.presentation.screens.Cart
import com.user.shoppingapp.presentation.screens.QuantityAdded
import com.user.shoppingapp.domain.useCase.UpdateUserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val allCategoriesUseCase: AllCategoriesUseCase,
    private val getListOfPartialInfo: GetListOfPartialInfo,
    private val getColorAndSizeList: GetColorAndSizeList,
    private val getCompleteInfo: GetCompleteInfo,
    private val authenticateUser: AuthenticateUser,
    private val firebaseAuth: FirebaseAuth,
    private val getProfileDetails: GetProfileDetails,
    private val updateUserDetails: UpdateUserDetails,
    private val getUserAddressAndPhoneNumber: GetUserAddressAndPhoneNumber,
    private val updatePhoneNumberAndAddress: UpdateUserAddressAndPhoneNumber,
    private val getNamesOfProducts: GetNamesOfProducts,
    private val getBannerImages: GetBannerImages,
    private val dbDao: FavouriteDao

) : ViewModel() {
    var index by mutableStateOf(0)
    var colorIndex by mutableStateOf("")
    var sizeIndex by mutableStateOf("")
    var currentScreen by mutableStateOf("")

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    private val _syncLocalDatabase = MutableStateFlow(mutableListOf<String>())

    private val _completeProductInfo = MutableStateFlow(CompleteProductInfoClass())
    val completeProductInfo = _completeProductInfo.asStateFlow()

    private val _holdPartialInfo = MutableStateFlow<PartialInfo>(PartialInfo())
    val holdPartialInfo = _holdPartialInfo.asStateFlow()

    private val _firebaseUser = MutableStateFlow<FirebaseUser?>(null)
    val firebaseUser: MutableStateFlow<FirebaseUser?> get() = _firebaseUser


    fun updatePartialInfo(partial: PartialInfo) {
        _holdPartialInfo.value = partial
    }

    fun resetColorAndSizeAndProductInfo() {
        colorIndex = ""
        sizeIndex = ""
        _completeProductInfo.value = CompleteProductInfoClass()
    }

    private val _colorAndSizeList = MutableStateFlow(
        Pair<List<String>, List<String>>(
            emptyList(),
            emptyList()
        )
    )
    val colorAndSizeList = _colorAndSizeList.asStateFlow()


    fun getColorAndSizeList(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getColorAndSizeList.getColorAndSizeList(id).collectLatest {
                when (it) {
                    is Events.Error -> {

                    }

                    Events.Loading -> TODO()
                    is Events.Success -> {
                        _colorAndSizeList.value = it.success
                    }
                }
            }
        }

    }

    fun resetCompleteProductInfo() {
        _completeProductInfo.value = CompleteProductInfoClass()
    }

//    fun getListOfPartialInfo(category: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _syncLocalDatabase.value=dbDao.getUserId().first().toMutableList()
//            getListOfPartialInfo.getListOfPartialInfo(category).collectLatest {
//                when (it) {
//                    is Events.Error -> {}
//                    Events.Loading -> {}
//                    is Events.Success -> {
//                        Log.d("Success Toggled","start")
//
//                        val newDbState=it.success.map {
//                            if(_syncLocalDatabase.value.contains(it.id)){
//                                it.copy(isFavourite = true)
//                            }else{
//                                it
//                            }
//                        }
//                        _homeScreenState.value = _homeScreenState.value.copy(
//                            success = newDbState.toMutableList()
//                        )
//                        Log.d("Success Toggled","end")
//
//                    }
//                }
//            }
//        }
//
//    }
fun getListOfPartialInfo(category: String) {
    viewModelScope.launch(Dispatchers.IO) {
       dbDao.getUserId().collect{doc->
        getListOfPartialInfo.getListOfPartialInfo(category).collectLatest {
            when (it) {
                is Events.Error -> {}
                Events.Loading -> {}
                is Events.Success -> {
                    Log.d("Success Toggled", "start")

                    val newDbState = it.success.map {
                        if (doc.contains(it.id)) {
                            it.copy(isFavourite = true)
                        } else {
                            it
                        }
                    }
                    _homeScreenState.value = _homeScreenState.value.copy(
                        success = newDbState.toMutableList()
                    )
                    Log.d("Success Toggled", "end")

                }
            }
        }
        }
    }

}


    fun getCompleteInfo(id: String, color: String, size: String) {

        viewModelScope.launch(Dispatchers.IO) {

            getCompleteInfo.getCompleteInfo(id, size, color).collectLatest {
                when (it) {
                    is Events.Error -> {
                        _completeProductInfo.value = _completeProductInfo.value.copy(
                            error = "The selected combination is not available",
                            isLoading = false,
                            success = null
                        )
                    }

                    Events.Loading -> {
                        _completeProductInfo.value = _completeProductInfo.value.copy(
                            isLoading = true,
                            error = "",
                            success = null
                        )
                    }

                    is Events.Success -> {
                        _completeProductInfo.value = _completeProductInfo.value.copy(
                            success = it.success,
                            isLoading = false,
                            error = ""
                        )
                    }
                }

            }
        }
    }

    private val _signUpScreenVariables = MutableStateFlow(SignUpScreenTextField())
    val signUpScreenVariables = _signUpScreenVariables.asStateFlow()

    private val _signUpScreen = MutableStateFlow(AuthenticateUserScreen())
    val signUpScreen = _signUpScreen.asStateFlow()

    private val _enabledOrNot = MutableStateFlow(false)
    val enabledOrNot = _enabledOrNot.asStateFlow()


    fun updateSignUpScreenTextField(field: String, value: String) {
        _signUpScreenVariables.value = when (field) {
            "firstName" -> _signUpScreenVariables.value.copy(firstname = value)
            "lastName" -> _signUpScreenVariables.value.copy(lastName = value)
            "email" -> _signUpScreenVariables.value.copy(email = value)
            else -> {
                _signUpScreenVariables.value.copy(password = value)
            }
        }
    }

    fun registerUser() {
        viewModelScope.launch(Dispatchers.IO) {
            authenticateUser.authenticateUser(_signUpScreenVariables.value).collectLatest {
                when (it) {
                    is Events1.Error -> {
                        _signUpScreen.value = AuthenticateUserScreen(error = it.error)

                        _enabledOrNot.value = false
                    }

                    Events1.Loading -> {
                        _signUpScreen.value = AuthenticateUserScreen(isLoading = true)

                        _enabledOrNot.value = true
                    }

                    is Events1.Success -> {
                        val (user, message) = it.success
                        _signUpScreen.value =
                            AuthenticateUserScreen(success = message)

                        _firebaseUser.value = user


                    }
                }
            }
        }
    }

    fun clearRegisterState() {
        _signUpScreen.value = AuthenticateUserScreen()
    }

    private val _profileScreen = MutableStateFlow(ProfileScreen())
    val profileScreen = _profileScreen.asStateFlow()

    private val _profileScreenTextField = MutableStateFlow(ProfileScreenTextField())
    val profileScreenTextField = _profileScreenTextField.asStateFlow()

    var toggle by mutableStateOf(true)

    fun toggleBool() {
        toggle = !toggle
    }


    fun loadProfileDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            getProfileDetails.getProfile(firebaseAuth.currentUser!!.uid).collectLatest {
                when (it) {
                    is Events.Error -> {
                        _profileScreen.value = ProfileScreen(error = it.error)
                    }

                    Events.Loading -> {
                        _profileScreen.value = ProfileScreen(isLoading = true)
                    }

                    is Events.Success -> {
                        _profileScreen.value = ProfileScreen(isLoading = false)

                        _profileScreenTextField.value = ProfileScreenTextField(
                            it.success.firstname,
                            it.success.lastName,
                            it.success.email,
                            it.success.phoneNumber,
                            it.success.address
                        )
                    }
                }
            }
        }
    }

    fun setUser(firebaseUser: FirebaseUser?) {
        _firebaseUser.value = firebaseUser

    }


    fun updateProfileScreenTextField(field: String, value: String) {
        _profileScreenTextField.value = when (field) {
            "firstName" -> _profileScreenTextField.value.copy(firstname = value)
            "lastName" -> _profileScreenTextField.value.copy(lastName = value)
            "email" -> _profileScreenTextField.value.copy(email = value)
            "phoneNumber" -> _profileScreenTextField.value.copy(phoneNumber = value)
            else -> {
                _profileScreenTextField.value.copy(address = value)
            }
        }
    }

    private val _saveDetailsState = MutableStateFlow(SaveState())
    val saveDetailsState = _saveDetailsState.asStateFlow()


    fun saveDetails() {
        viewModelScope.launch (Dispatchers.IO){
            updateUserDetails.updateUserDetails(
                firebaseAuth.currentUser!!.uid,
                _profileScreenTextField.value
            ).collectLatest {
                when (it) {
                    is Events.Error -> {
                        _saveDetailsState.value = SaveState(error = it.error)
                    }

                    Events.Loading -> {
                        _saveDetailsState.value = SaveState(isLoading = true)
                    }

                    is Events.Success -> {
                        _saveDetailsState.value = SaveState(success = it.success)
                    }
                }

            }
        }

    }

    fun reset() {
        _saveDetailsState.value = SaveState()
        _profileScreen.value = ProfileScreen()
    }

    fun logout() {
        firebaseAuth.signOut()
        _firebaseUser.value = null
        index = 0
        colorIndex = ""
        sizeIndex = ""
        currentScreen = ""
        _colorAndSizeList.value = Pair<List<String>, List<String>>(
            emptyList(),
            emptyList()
        )
        _enabledOrNot.value = false
        _homeScreenState.value = HomeScreenState()
        _signUpScreen.value = AuthenticateUserScreen()
        _holdPartialInfo.value = PartialInfo()
        _completeProductInfo.value = CompleteProductInfoClass()
        _signUpScreenVariables.value = SignUpScreenTextField()
        _profileScreenTextField.value = ProfileScreenTextField()
        _saveDetailsState.value = SaveState()
        _profileScreen.value = ProfileScreen()
        toggle = true
        Log.d("Executed", "Exe")
    }

    private val _quantityCounter = MutableStateFlow(1)
    val quantityCounter = _quantityCounter.asStateFlow()


    fun increaseQuantity(availUnits: Int): String {
        if (_quantityCounter.value >= 5) {
            return "Quantity greater than 5 is not available"
        } else if (_quantityCounter.value >= availUnits) {
            return "Quantity greater than $availUnits is not available"
        } else {
            _quantityCounter.value += 1
            return "Quantity Increased"
        }

    }

    fun decreaseQuantity():String {
        if (_quantityCounter.value != 0) {
            _quantityCounter.value -= 1
        }
        return "Quantity Decreased"

    }

    private val _phoneNumberAndAddress = MutableStateFlow(PhoneNumberAndAddress())
    val phoneNumberAndAddress = _phoneNumberAndAddress.asStateFlow()

    private val _phoneNumberAndAddressResult = MutableStateFlow(PhoneNumberAndAddressResult())
    val phoneNumberAndAddressResult = _phoneNumberAndAddressResult.asStateFlow()

    private val _enabledButtonOrNot = MutableStateFlow(true)
    val enabledButtonOrNot = _enabledButtonOrNot.asStateFlow()

    fun updatePhoneNumberAndAddress(field: String, value: String) {
        _phoneNumberAndAddress.value = when (field) {
            "address" -> _phoneNumberAndAddress.value.copy(address = value)
            else -> _phoneNumberAndAddress.value.copy(phoneNumber = value)
        }
    }

    fun toggleButton() {
        _enabledButtonOrNot.value = !_enabledButtonOrNot.value
    }


    fun getPhoneNumberAndAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserAddressAndPhoneNumber.getDetails().collectLatest {
                when (it) {
                    is Events.Error -> {
                        _phoneNumberAndAddress.value = PhoneNumberAndAddress(error = it.error)

                    }

                    Events.Loading -> {}
                    is Events.Success -> {
                        _phoneNumberAndAddress.value =
                            PhoneNumberAndAddress(
                                address = it.success.address,
                                phoneNumber = it.success.phoneNumber,
                                isSuccess = true
                            )


                    }
                }
            }
        }
    }

    fun update() {
        viewModelScope.launch(Dispatchers.IO) {
            updatePhoneNumberAndAddress.updatePhoneNumberAndAddress(
                _phoneNumberAndAddress.value.address,
                _phoneNumberAndAddress.value.phoneNumber
            ).collectLatest {
                when(it){
                    is Events.Error -> {_phoneNumberAndAddressResult.value=PhoneNumberAndAddressResult(error = it.error)}
                    Events.Loading -> {_phoneNumberAndAddressResult.value=PhoneNumberAndAddressResult(isLoading = true)}
                    is Events.Success -> {_phoneNumberAndAddressResult.value=PhoneNumberAndAddressResult(success = it.success)}
                }
            }
        }
    }
    fun clearPhoneNumberAndResultState(){
        _phoneNumberAndAddressResult.value=PhoneNumberAndAddressResult()
    }
    private val _checked=MutableStateFlow(true)
    val checked =_checked.asStateFlow()
    fun checkedToggle(){
        _checked.value=!_checked.value
    }
var selectedDate by mutableStateOf("")
    private val _cartList =MutableStateFlow<MutableList<QuantityAdded>>(mutableListOf())
    val cartlist =_cartList.asStateFlow()

    private val oldList=MutableStateFlow(mutableListOf<Cart>())

    private val _price=MutableStateFlow(PriceDetails())
    val priceDetails=_price.asStateFlow()

    fun addToCart(cart:Cart){
           val count=oldList.value.count { it==cart }
        if(count<5) {
            oldList.value.add(cart)
            val newList = oldList.value.groupingBy { it }.eachCount()
            _cartList.value.clear()
            newList.map { (cartItem, count) ->
                if (count > 5) {
                    _cartList.value.add(QuantityAdded(5, cartItem))

                } else {
                    _cartList.value.add(QuantityAdded(count, cartItem))

                }

            }
            var reg = 0
            var fin = 0
            var dis = 0
            _cartList.value.fastForEach {
                reg += it.count * it.cart!!.regPrice.toInt()
                fin += it.count * it.cart.finalPrice.toInt()
                dis += (it.cart.regPrice.toInt() - it.cart.finalPrice.toInt()) * it.count
            }

            _price.value = _price.value.copy(
                reg.toString(), fin.toString(), dis.toString()
            )
        }


        }
    fun checkToggleForCart(size: String,color: String,itemId:String){
        _cartList.value=_cartList.value.map {
            if(size==it.cart!!.size&&color==it.cart.color&&itemId==it.cart.id){
                it.copy(isChecked = !it.isChecked)
            }else
                it
        }.toMutableList()



    }
    fun updateDateForParticularItem(size: String,color: String,date:String,itemId: String){
        _cartList.value=_cartList.value.map {
            if(size==it.cart!!.size&&color==it.cart.color&&itemId==it.cart.id){
                it.copy(date = date)
            }else
                it
        }.toMutableList()
    }

    fun increaseOrDecreaseQuantityForEachItem(availUnits: Int,size: String,color: String,itemId: String,bool:Boolean):String{
        var message=""
        if(bool){
            _cartList.value=_cartList.value.map {item->
                if(size==item.cart!!.size&&color==item.cart.color&&itemId==item.cart.id){
                    if(item.count>=availUnits){
                        message="Quantity greater than $availUnits is not available"
                        item
                    }else if(item.count>=5){
                        message= "Quantity greater than 5 is not available"
                        item
                    }else{
                        oldList.value.add(item.cart)
                        message="Quantity Increased"
                        item.copy(count = item.count+1)

                    }

                }else
                    item
            }.toMutableList()
        }else{
            _cartList.value = _cartList.value.map { item ->
                if (size == item.cart!!.size && color == item.cart.color && itemId == item.cart.id && item.count != 0) {
                    val index = oldList.value.indexOfFirst { it.size == item.cart.size && it.color == item.cart.color && it.id== item.cart.id }
                    if (index != -1) {
                        oldList.value.removeAt(index)
                    }
                    message = "Quantity decreased"
                    item.copy(count = item.count - 1) // Decrease the count
                } else {
                    item // No change
                }
            }.toMutableList()

        }
        var reg=0
        var fin=0
        var dis=0
        _cartList.value.fastForEach {
            reg+=it.count*it.cart!!.regPrice.toInt()
            fin+=it.count*it.cart.finalPrice.toInt()
            dis+=(it.cart.regPrice.toInt()-it.cart.finalPrice.toInt())*it.count
        }

        _price.value=_price.value.copy(
            reg.toString(),fin.toString(),dis.toString()
        )



        return message


    }
    private val _nameList=MutableStateFlow(emptyList<String>())
    val nameList=_nameList.asStateFlow()

    var searchBar by mutableStateOf("")

    var active by mutableStateOf(false)

    fun clearList(){
        _nameList.value= emptyList()
    }


    fun getNameList(query:String){
        viewModelScope.launch {
            getNamesOfProducts.getNamesOfProducts(query).collectLatest {
                when(it){
                    is Events.Error -> { }
                    Events.Loading -> { }
                    is Events.Success -> {
                        _nameList.value=it.success
                    }
                }
            }
        }
    }
    private val _bannerList=MutableStateFlow(emptyList<String>())
    val bannerList=_bannerList.asStateFlow()
    fun getBanner(){
        viewModelScope.launch {
            getBannerImages.getBannerImages().collectLatest {
                when(it){
                    is Events.Error -> {

                    }
                    Events.Loading -> {

                    }
                    is Events.Success ->{
                        _bannerList.value=it.success
                    }
                }
            }
        }
    }
   var searchBarIsExpanded by mutableStateOf(false)

    private val _wishlist=MutableStateFlow(listOf<Favourite>())
    val wishlist=_wishlist.asStateFlow()


    suspend fun addToWishlistFromHomeScreen(userId: String,bool:Boolean): String {

        var message = ""
        val updatedList = _homeScreenState.value.success.map {
            if (userId == it.id) {
                it.copy(isFavourite = !it.isFavourite)
            } else {
                it
            }
        }.toMutableList()

        val product: PartialInfo? = updatedList.find { userId == it.id }
        if (product != null) {
            if (product.isFavourite) {
                val count=dbDao.getCount().first()
                if (count <= 10) {
                    dbDao.insertFavouriteProduct(
                        Favourite(
                            product.id,
                            product.name,
                            product.category,
                            product.imageUrl,
                            product.seller,
                            product.basePrice
                        )
                    )
                    _homeScreenState.value = _homeScreenState.value.copy(
                        success = updatedList
                    )
                    message = "Added to Wishlist"

                } else {
                    message = "Cannot insert more than 10 items"
                }
            } else {
                dbDao.deleteFavouriteProduct(product.id)
                _homeScreenState.value = _homeScreenState.value.copy(
                    success = updatedList
                )
                message = "Deleted from Wishlist"

            }
        }
        collectWishList()
        if(bool){
            _holdPartialInfo.value=product!!
        }
        return message

    }


    suspend fun collectWishList(){
        _wishlist.value=dbDao.getAllFavouriteProducts().first()
    }

    init {
        viewModelScope.launch(Dispatchers.Default) {
//            dbDao.getAllFavouriteProducts().collect{
//                _wishlist.value=it
//            }
            collectWishList()



    }
    }



    }





data class SignUpScreenTextField(
    val firstname: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val address: String = ""
)

data class ProfileScreenTextField(
    val firstname: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val address: String = ""
)


data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    val success: MutableList<PartialInfo> = mutableListOf<PartialInfo>()
)


data class AddCategoryState(
    val isLoading: Boolean = false,
    val success: List<Category> = emptyList(),
    val error: String = ""
)

data class CompleteProductInfoClass(
    val isLoading: Boolean = false,
    val error: String = "",
    val success: Product? = null
)

data class AuthenticateUserScreen(
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String? = null
)

data class ProfileScreen(
    val isLoading: Boolean = false,
    val error: String = "",
    val success: SignUpScreenTextField? = null
)

data class SaveState(
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String? = null
)

data class PhoneNumberAndAddress(
    val error: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val isSuccess: Boolean = false,

)
data class PhoneNumberAndAddressResult(
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = ""
)
data class PriceDetails(
    val regPrice:String="",
    val finalPrice:String="",
    val discount:String=""
)