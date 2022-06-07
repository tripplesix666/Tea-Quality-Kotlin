package com.example.teaqualitykotlin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

lateinit var firebaseAuth: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference

const val NODE_TEAS = "teas"

const val FOLDER_IMAGES = "teasImage"

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