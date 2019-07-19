package com.example.sjsugo

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_submit_event.*
import java.util.jar.Manifest

//        TO DO:
//                1. When picture is taken it will store into the cloud firestore.
//                2. Submit button that will allow it to store
//                3. Should we display the image activity?

class SubmitEventActivity : AppCompatActivity() {


    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_event)

        // This will take it back to Dashboard
        val cancelBtn = findViewById<Button>(R.id.cancel_button)
        cancelBtn.setOnClickListener{startActivity(Intent(this, DashboardActivity::class.java))}

        // Button Click
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
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Called when image was capture from camera intent
        if(resultCode == Activity.RESULT_OK){
//            // Set image captured to image view
//            image_view.setImageURI(image_uri)
        }
    }
}
