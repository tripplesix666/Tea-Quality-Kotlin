package com.example.teaqualitykotlin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var firebaseAuth: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference

const val NODE_TEAS = "teas"
const val NODE_CART = "Cart"
const val NODE_FAVORITE = "Favorite"
const val NODE_USERS = "Users"
const val NODE_SALE = "Sale"

const val CHILD_PERCENT = "percent"
const val CHILD_SUM = "sum"

const val CHILD_ID = "id"
const val CHILD_IMAGE = "image"
const val CHILD_NAME = "name"
const val CHILD_PRICE = "price"
const val CHILD_DETAILS = "details"

fun initFirebase() {
    firebaseAuth = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}