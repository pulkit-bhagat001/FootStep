package com.user.shoppingapp.data.RepImpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.user.shoppingapp.common.CATEGORY
import com.user.shoppingapp.common.Events
import com.user.shoppingapp.common.Events1

import com.user.shoppingapp.domain.Repository.Repository
import com.user.shoppingapp.domain.models.Category
import com.user.shoppingapp.domain.models.GetUserAddressAndPhoneNumber
import com.user.shoppingapp.domain.models.PartialInfo
import com.user.shoppingapp.domain.models.Product
import com.user.shoppingapp.domain.models.VariantsInfo
import com.user.shoppingapp.presentation.Viewmodel.ProfileScreenTextField
import com.user.shoppingapp.presentation.Viewmodel.SignUpScreenTextField
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImpl @Inject constructor(private val firestore: FirebaseFirestore,private val firebaseAuth: FirebaseAuth) : Repository {
    override suspend fun getAllCategories(): Flow<Events<List<Category>>> = callbackFlow {

        trySend(Events.Loading)
        firestore.collection(CATEGORY).get().addOnSuccessListener{
            val newList=it.documents.mapNotNull { element-> element.toObject(Category::class.java) }
            trySend(Events.Success(newList))
        }.addOnFailureListener { trySend(Events.Error(it.toString())) }

        awaitClose { close() }
    }

    override suspend fun getProductByCategory(category: String): Flow<Events<List<PartialInfo>>> = callbackFlow {
        firestore.collection("PRODUCT").whereEqualTo("category","Shoes").get().addOnSuccessListener {
            val list= mutableListOf<PartialInfo>()
            for (child in it){
                val id=child.getString("id")
                val name=child.getString("name")
                val categoryName=child.getString("category")
                val imageUrl=child.getString("image")
                val seller=child.getString("createdBy")
                val basePrice=child.getString("basePrice")
                list.add(PartialInfo(id!!,name!!,categoryName!!,imageUrl!!,seller!!,basePrice!!))
            }
            trySend(Events.Success(list))
        }
        awaitClose { close() }
    }

    override suspend fun getCompleteInfo(id: String,size:String,color:String): Flow<Events<Product>> = callbackFlow{

           val ref = firestore.collection("PRODUCT").document(id)
           ref.get().addOnSuccessListener {
               val name = it.getString("name")
               val desc = it.getString("desc")
               val productId = it.getString("id")
               val category = it.getString("category")
               val imageUrl = it.getString("image")
               val seller = it.getString("createdBy")
               val timestamp = it.getString("timestamp")
               val basePrice = it.getString("basePrice")
               ref.collection("variant").document("size_${size}_color_$color").get()
                   .addOnSuccessListener { doc ->
                           val regularPrice = doc.getString("regPrice")
                           val finalPrice = doc.getString("finalPrice")
                           val discountPercentage = doc.getString("discountPercent")
                           val availableUnits = doc.getString("availUnits")
                           val isAvailable = doc.getBoolean("isAvail")
                       try {
                           val finalProduct = Product(
                               productId!!,
                               name!!,
                               desc!!,
                               category!!,
                               imageUrl!!,
                               seller!!,
                               timestamp!!.toLong(),
                               basePrice!!,
                               VariantsInfo(
                                   regularPrice!!,
                                   finalPrice!!,
                                   discountPercentage!!,
                                   availableUnits!!.toInt(),
                                   isAvailable!!
                               )

                           )
                           trySend(Events.Success(finalProduct))
                       }catch (e:Exception){
                           trySend(Events.Error(e.toString()))
                       }

                   }


           }.addOnFailureListener { trySend(Events.Error(it.toString())) }

        awaitClose{close()}
        }

    override suspend fun getColorAndSizeList(id: String): Flow<Events<Pair<List<String>,List<String>>>>  =
        callbackFlow {
        firestore.collection("PRODUCT").document(id).collection("variant").get().addOnSuccessListener {
            val colorList= mutableListOf<String>()
            val sizeList= mutableListOf<String>()
            for(child in it){
                val color=child.getString("color")
                val size=child.getString("size")
                colorList.add(color.toString())
                sizeList.add(size.toString())

            }
            trySend(Events.Success(Pair(sizeList,colorList)))
        }
            awaitClose { close() }
    }

    override suspend fun authenticateUser(userInfo: SignUpScreenTextField): Flow<Events1<Pair<FirebaseUser, String>>> = callbackFlow {
        trySend(Events1.Loading)
        firebaseAuth.createUserWithEmailAndPassword(userInfo.email,userInfo.password).addOnSuccessListener {doc->

            firestore.collection("USERS").document(doc.user?.uid.toString()).set(userInfo).addOnSuccessListener {
                trySend(Events1.Success(Pair(doc.user!!,"User Registered Successfully")))
            }.addOnFailureListener { trySend(Events1.Error("Error Registering the user in db")) }
        }.addOnFailureListener {   trySend(Events1.Error(it.message.toString()))  }
            awaitClose{
                close()
            }
    }

    override suspend fun getProfileDetails(userId: String): Flow<Events<ProfileScreenTextField>> = callbackFlow{
        trySend(Events.Loading)
        firestore.collection("USERS").document(userId).get().addOnSuccessListener {
            val firstname=it.getString("firstname")
            val lastName=it.getString("lastName")
            val email=it.getString("email")
            val phoneNumber=it.getString("phoneNumber")
            val address=it.getString("address")
            try {
                trySend(Events.Success(ProfileScreenTextField(firstname!!,lastName!!,email!!,phoneNumber!!,address!!)))
            }catch (e:Exception){
                trySend(Events.Error(it.toString()))
            }

        }.addOnFailureListener { trySend(Events.Error(it.message.toString())) }
        awaitClose { close() }
    }

    override suspend fun updateUserDetails(
        userid: String,
        info: ProfileScreenTextField
    ): Flow<Events<String>> = callbackFlow {
        trySend(Events.Loading)
        firestore.collection("USERS").document(userid).set(info).addOnSuccessListener {
            trySend(Events.Success("Information Updated Successfully"))
        }.addOnFailureListener { trySend(Events.Error(it.message.toString())) }



        awaitClose {
            close()
        }
    }

    override suspend fun getUserAddressAndPhoneNumber(): Flow<Events<GetUserAddressAndPhoneNumber>> = callbackFlow {
        firestore.collection("USERS").document(firebaseAuth.currentUser!!.uid).get().addOnSuccessListener {
            val address=it.getString("address")
            val phoneNumber=it.getString("phoneNumber")
            trySend(Events.Success(GetUserAddressAndPhoneNumber(address!!,phoneNumber!!)))
        }.addOnFailureListener { trySend(Events.Error(it.message.toString())) }
        awaitClose { close() }
    }

    override suspend fun updateUserAddressAndPhoneNumber(address:String,phoneNumber:String): Flow<Events<String>> = callbackFlow{
        trySend(Events.Loading)
        firestore.collection("USERS").document(firebaseAuth.currentUser!!.uid).update("address",address,"phoneNumber",phoneNumber).addOnSuccessListener {
            trySend(Events.Success("Successfully Updated"))
        }.addOnFailureListener { Events.Error(it.message.toString()) }
        awaitClose { close() }
    }

    override suspend fun getNamesOfProducts(query:String): Flow<Events<List<String>>> = callbackFlow{
        val mutList= mutableListOf<String>()
        firestore.collection("PRODUCT").orderBy("name").startAt(query).endAt(query+"\uf8ff").get().addOnSuccessListener {
            for(doc in it){
                val name=doc.getString("name")
                mutList.add(name!!)
            }
            trySend(Events.Success(mutList))
        }.addOnFailureListener { trySend(Events.Error(it.message.toString())) }
        awaitClose{close()}
    }

    override suspend fun getBannerImages(): Flow<Events<List<String>>> = callbackFlow {
        val bannerList= mutableListOf<String>()
        firestore.collection("BANNER").get().addOnSuccessListener {
            for(doc in it){
                val image=doc.getString("url")
                bannerList.add(image!!)
            }
            trySend(Events.Success(bannerList))
        }.addOnFailureListener { trySend(Events.Error(it.message.toString())) }
        awaitClose{close()}
    }

}
