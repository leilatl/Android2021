package com.example.androidproject.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.provider.SyncStateContract
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.androidproject.*
import com.example.androidproject.model.Product
import com.example.androidproject.model.User
import com.example.androidproject.ui.dashboard.DashboardFragment
import com.example.androidproject.ui.home.ProductsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {
    private val myFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        myFireStore.collection("users").document(userInfo.id).set(userInfo, SetOptions.merge()).addOnSuccessListener {
            activity.registrationSuccess()
        }.addOnFailureListener { e ->
            Log.e(activity.javaClass.simpleName, "error with registration", e)
        }

    }

    fun getUserID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser

        return currentUser?.uid ?: ""

    }

    fun getUserDetails(activity: Activity){

        myFireStore.collection("users").document(getUserID()).get().addOnSuccessListener {
            document ->
            Log.i(activity.javaClass.simpleName, document.toString())

            val user = document.toObject(User::class.java)!!

            val sharedPreferences = activity.getSharedPreferences(
                "Preferences", Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putString(
                "firstName", "${user.firstName}"
            )
            editor.putString(
                "lastName", "${user.lastName}"
            )
            editor.putString(
                "email", "${user.email}"
            )
            editor.apply()

            when(activity){
                is LoginActivity ->{
                    activity.userLoggedInSuccess(user)
                }
                is RegisterActivity ->{
                    activity.registrationSuccess()
                }
                is SettingsActivity -> {
                    activity.userDetailsSuccess(user)
                }
            }

        }

    }

    fun updateUserProfileData(activity: ProfileActivity, userHashMap: HashMap<String, Any>){
        myFireStore.collection("users").document(getUserID()).update(userHashMap).addOnSuccessListener {
            activity.userProfileUpdateSuccess()
        }
    }

    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product){
        myFireStore.collection("products").document().set(productInfo, SetOptions.merge()).addOnSuccessListener {
            activity.productUploadSuccess()
        }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?, imageType: String){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(activity, imageFileUri)
        )
        sRef.putFile(imageFileUri!!).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                uri ->
                Log.e("Downloadable Inage URL", uri.toString())
                when (activity){
                    is ProfileActivity -> {
                        activity.imageUploadSuccess(uri.toString())
                    }
                    is AddProductActivity -> {
                        activity.imageUploadSuccess(uri.toString())
                    }
                }
            }
        }


    }

    fun getProductsList(fragment: Fragment){
        myFireStore.collection("products").whereEqualTo("user_id", getUserID())
            .get().addOnSuccessListener { document ->
                val productList: ArrayList<Product> = ArrayList()
                for (i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.id = i.id
                    productList.add(product)
                }
                when(fragment){
                    is ProductsFragment -> {
                        fragment.successProductsList(productList)
                    }
                }

            }
    }
    fun getDashboardItemsList(fragment: DashboardFragment){
        myFireStore.collection("products").get().addOnSuccessListener {
            document ->
            val productList: ArrayList<Product> = ArrayList()
            for (i in document.documents){
                val product = i.toObject(Product::class.java)
                product!!.id = i.id
                productList.add(product)
            }
            fragment.successDashboardList(productList)
        }
    }
}