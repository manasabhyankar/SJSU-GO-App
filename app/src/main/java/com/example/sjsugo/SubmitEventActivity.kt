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
//                1. When picture is taken it will store into the cloud firestore.
//                2. Submit button that will allow it to store
//                3. Should we display the image activity?

class SubmitEventActivity : AppCompatActivity() {

    lateinit var storage: FirebaseStorage

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    private val SELECT_PICUTRE = 1002
    private val PERMISSION_GALLERY_CODE = 1003
    var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_event)
        //val storage = FirebaseStorage.getInstance()

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
//        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        startActivityForResult(galleryIntent, SELECT_PICUTRE)
        val gIntent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(gIntent, "Select Picture"),SELECT_PICUTRE)
    }
    private fun uploadImage(){
        val progress = ProgressDialog(this).apply {
            setTitle("Uploading...")
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
        val pData = FirebaseStorage.getInstance()
        var countdown = 0.0
        var storage = pData.getReference().child("myimage.jpg").putFile(image_uri!!)
            .addOnSuccessListener{ taskSnapshot -> progress.dismiss()
                //TODO: the comment one from this section, do not work, but this important to upload
                //val uri = taskSnapshot.downloadUrl
                Toast.makeText(this, "DONE!", Toast.LENGTH_SHORT).show()
                //Glide.with(this@StorageActivity).load(uri).into(image)
                startActivity(Intent(this, DashboardActivity::class.java))
            }
            .addOnProgressListener { takeSnapshot ->
                countdown = ((100.0 * takeSnapshot.bytesTransferred) / takeSnapshot.totalByteCount)
                Log.v("countdown", "countdown=="+countdown)
                progress.setMessage("Uploaded... " + countdown.toInt() + "%")
                //TODO: THIS COUNT DOWN DOESN'T WORK WHICH MEANS THAT IT IS NEVER UPLOADING
                //startActivity(Intent(this, DashboardActivity::class.java))
            }
            .addOnFailureListener{
                exception -> exception.printStackTrace()
            }
        //TODO: This works, don't delete
//        if(image_uri != null) {
//            val progressDialog: ProgressDialog = ProgressDialog(this)
//            progressDialog.setTitle("Uploading...")
//            progressDialog.show()
//            val storage = FirebaseStorage.getInstance()
//            var storageRef = storage.getReference()
//            val ref: StorageReference
//            ref = storageRef.child("camperaImage/" + UUID.randomUUID().toString())
//            ref.putFile(image_uri!!).addOnProgressListener {
//
//            }
//        }
        Toast.makeText(this, "Uploaded Done!", Toast.LENGTH_LONG).show()
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
//            try{
//                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, image_uri)
//                imageView.setImageBitmap(bitmap)
//            } catch (IOException e) {
//                e.printStackTrace()
//            }
            imageView.setImageURI(image_uri)
        }
    }
}

