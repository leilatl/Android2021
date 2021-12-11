package com.example.androidproject

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.androidproject.firestore.FirestoreClass
import com.example.androidproject.model.Product
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.IOException

class AddProductActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var selectedImageFileUri: Uri
    private var mProductImageURL: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setUpActionBar()
        iv_add_product_photo.setOnClickListener(this@AddProductActivity)
        btn_add_product.setOnClickListener {
            uploadProductImage()
        }
    }
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_add_product)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbar_add_product.setNavigationOnClickListener{onBackPressed()}
    }
    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.iv_add_product_photo -> {
                    if(ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ){
                        Constants.showImageChooser(this)
                    }else{
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            2
                        )
                    }


                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 2){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{

            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 1){
                if(data != null){
                    selectedImageFileUri = data.data!!
                    try{
                        GlideLoader(this).loadUserPicture(selectedImageFileUri, iv_add_product_photo)
                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                }
            }
        }
    }
    private fun uploadProductImage(){
        FirestoreClass().uploadImageToCloudStorage(this, selectedImageFileUri, "product_image")
    }
    fun productUploadSuccess(){
        finish()
    }
    fun imageUploadSuccess(imageURL: String){
        mProductImageURL = imageURL
        uploadProductDetails()
    }
    private fun uploadProductDetails(){
        val username = this.getSharedPreferences(
            "Preferences", Context.MODE_PRIVATE).getString("firstName", "")!!
        val product = Product(
            FirestoreClass().getUserID(),
            username,
            et_product_name.text.toString(),
            et_product_price.text.toString(),
            et_product_description.text.toString(),
            et_product_quantity.text.toString(),
            mProductImageURL
        )
        FirestoreClass().uploadProductDetails(this, product)
    }
}