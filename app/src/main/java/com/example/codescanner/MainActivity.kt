package com.example.codescanner
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.codescanner.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.example.codescanner.Enter




class MainActivity : AppCompatActivity() {
    var urlString = "";




//    var userName= bundle.getString("Username")
//
//
//    var userEmail = bundle.getString("UserEmail")
//   var userPassword =bundle.getString("UserPassword")


    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .build()

    lateinit var binding: ActivityMainBinding

    private val REQUEST_IMAGE_CAPTURE = 1

    private var imageBitmap: Bitmap? = null


    fun Intent.getData(key: String): String {
        return extras?.getString(key) ?: "intent is null"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "hey"+ intent.extras?.getString("samplename").toString(),  Toast.LENGTH_SHORT,).show()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {

            captureImage.setOnClickListener {

                takeImage()

                textView.text = ""


            }

            detectTextImageBtn.setOnClickListener {

                processImage()

            }

            saveText.setOnClickListener { saveImg() }


        }


    }

    private fun takeImage() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {

            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

        } catch (e: Exception) {


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val extras: Bundle? = data?.extras

            imageBitmap = extras?.get("data") as Bitmap

            if (imageBitmap != null) {

                binding.imageView.setImageBitmap(imageBitmap)

            }


        }


    }

   val db = Firebase.firestore

    private fun processImage() {

        if (imageBitmap != null) {

            val image = InputImage.fromBitmap(imageBitmap!!, 0)

            val scanner = BarcodeScanning.getClient(options)


            scanner.process(image)
                .addOnSuccessListener { barcodes ->

                    if (barcodes.toString() == "[]") {

                        Toast.makeText(this, "Nothing to scan", Toast.LENGTH_SHORT).show()

                    }


                    for (barcode in barcodes) {
                        val valueType = barcode.valueType
                        // See API reference for complete list of supported types
                        when (valueType) {
                            Barcode.TYPE_WIFI -> {
                                val ssid = barcode.wifi!!.ssid
                                val password = barcode.wifi!!.password
                                val type = barcode.wifi!!.encryptionType

                                binding.textView.text = ssid + "\n" + password + "\n" + type


                            }
                            Barcode.TYPE_URL -> {
                                val title = barcode.url!!.title
                                val url = barcode.url!!.url
                                urlString = url.toString().trim();
                                binding.textView.text = title + "\n" + url

                            }

                        }
                    }


                }
                .addOnFailureListener {


                    Toast.makeText(this, "Please select photo", Toast.LENGTH_SHORT).show()
                }


        } else {

            Toast.makeText(this, "Please select photo", Toast.LENGTH_SHORT).show()

        }




    }


    private fun saveImg() {

        val userName =      intent.getStringExtra("username")
        var userEmail =    intent.getStringExtra("userEmail")
        var userPassword =  intent.getStringExtra("userPassword")
        val userMap = hashMapOf(
            "urlText" to urlString,
            "Name" to userName,
         "UserEmail" to userEmail,
        "UserPassword" to userPassword
        )
        if(!urlString.isEmpty()){
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("users").document(userId).set(userMap)

         //   db.collection("users").add(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully Added!", Toast.LENGTH_LONG).show()
                    binding.imageView.setImageResource(android.R.color.transparent)
                    binding.textView.setText("")


                }
                .addOnFailureListener{
                    Toast.makeText(this, "Failure !", Toast.LENGTH_LONG).show()
                }
        }
        else{
            Toast.makeText(this, "Empty Field!", Toast.LENGTH_LONG).show()
        }


    }
//
}