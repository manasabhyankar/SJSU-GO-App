package com.example.sjsugo

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.firebase.FirebaseApp
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import kotlinx.android.synthetic.main.activity_submit_event.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import java.util.jar.Manifest

//        TODO:
//              1. STORE WITH THE EVENT OF THE IMAGE

/*
TODO: IT IS IMPORTANT TO HAVE THE RULES IN THE STORAGE LIKE THIS TO BE ABLE TO STORE
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
    	allow read, write: if true;
      // allow read, write: if request.auth.token.premiumAccount == true;
    }
  }
}
 */

class SubmitEventActivity : AppCompatActivity() {

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    private val SELECT_PICUTRE = 1002
    private val PERMISSION_GALLERY_CODE = 1003
    private var image_uri: Uri? = null

    internal var storage: FirebaseStorage ?= null
    internal var storageRef: StorageReference ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_event)
        //Init Firebase
        storage = FirebaseStorage.getInstance()
        storageRef = storage!!.reference

        // Obtain file from gallery
        val galleryBtn = findViewById<ImageButton>(R.id.gallery_button)
        galleryBtn.setOnClickListener{
            // If system OS is Marshmallow or above, we need to request runtime permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //permission was not enabled
                    val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show pop up to request permission
                    requestPermissions(permission, PERMISSION_GALLERY_CODE)
                } else {
                    openGallery()
                }
            } else {
                openGallery()
            }
        }

        // This will take it back to Dashboard
        val cancelBtn = findViewById<Button>(R.id.cancel_button)
        cancelBtn.setOnClickListener{startActivity(Intent(this, DashboardActivity::class.java))}

        // Camera Button; Will open the camera.
        val cameraBtn = findViewById<ImageButton>(R.id.camera_button)
        cameraBtn.setOnClickListener{
            // If system OS is Marshmallow or Above, we need to request runtime permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show pop up to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else {
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system OS is Marshmallow
                openCamera()
            }
        }

        val submitBtn = findViewById<Button>(R.id.submit_button)
        submitBtn.setOnClickListener{
            if(image_uri == null){
                //this image view is empty
                Toast.makeText(this, "No image!", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
            }
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "camperaImage")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    private fun openGallery(){
        //gallery intent
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, SELECT_PICUTRE)
//        TODO: THIS WILL SHOW GOOGLE GALLERY STYLE
//        val gIntent = Intent().apply {
//            type = "image/*"
//            action = Intent.ACTION_GET_CONTENT
//        }
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"),SELECT_PICUTRE)
    }
    private fun uploadImage(){
        val progress = ProgressDialog(this).apply {
            setTitle("Uploading...")
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
        val imageRef = storageRef?.child("images/"+ UUID.randomUUID().toString())
        imageRef!!.putFile(image_uri!!)
            .addOnProgressListener { taskSnapShot ->
                val countdown = ((100.0 * taskSnapShot.bytesTransferred) / taskSnapShot.totalByteCount)
                progress.setMessage("Uploaded... " + countdown.toInt() + "%")
            }
            .addOnSuccessListener {
                progress.dismiss()
                Toast.makeText(this, "File Uploaded", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
            }
            .addOnFailureListener {
                progress.dismiss()
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
            }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // Called when user pressed ALLOW OR DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission from popup was granted
                    openCamera()
                }
                else{
                    // Permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            PERMISSION_GALLERY_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission from popup was granted
                    openGallery()
                }
                else{
                    // Permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Called when image was capture from camera intent
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            // Set image captured to image view
            imageView.setImageURI(image_uri)
        }
        // Result code is RESULT_OK only if the user selects an Image
        else if(resultCode == Activity.RESULT_OK && requestCode == SELECT_PICUTRE){
            image_uri = data?.getData()
            try{
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, image_uri)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException ) {
                e.printStackTrace()
            }
            //imageView.setImageURI(image_uri)
        }
    }
}
