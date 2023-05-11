package com.example.mobile_app


import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.mobile_app.databinding.ActivityBookTicketBinding
import com.example.mobile_app.databinding.ActivityPdfDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.FileOutputStream

class PdfDetailActivity : AppCompatActivity() {

    //view binding
    private lateinit var  binding: ActivityPdfDetailBinding


    private companion object{
        //TAG
        const val TAG = "BOOK_DETAILS_TAG"
    }

    //book id
    private var bookId = ""

    //get from firebase
    private var bookTitle = ""
    private var bookUrl = ""

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //get book id from intent , will load book info using this  bookId
        bookId = intent.getStringExtra("bookId")!!

        //init progress bar
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        //increment book view count , whenever this page starts
        MyApplication.incrementBookViewCount(bookId)

        loadBookDetails()

        //handle back button click go back
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        //handle click, open pdf view activity
            binding.readBookBtn.setOnClickListener{
                val intent = Intent(this, PdfViewActivity::class.java)
                intent.putExtra("bookId", bookId)
                startActivity(intent)
            }
            //handle click , download book/pdf
        binding.downloadBookBtn.setOnClickListener{
            //first check storage permission
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onCreate: STORAGE PERMISSION is already granted")
                downloadBook()
            }
            else{
                Log.d(TAG, "onCreate: STORAGE PERMISSION was not granted, LETS request it")
                requestStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        binding.payBtn.setOnClickListener{
            val intent = Intent(this, CalculatePaymentActivity::class.java)
            intent.putExtra("bookId", bookId)
            startActivity(intent)
        }

    }

    private val requestStoragePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->

        if (isGranted) {
            Log.d(TAG, "onCreate: STORAGE PERMISSION was not granted")
            downloadBook()
        }
        else{
            Log.d(TAG, "onCreate: STORAGE PERMISSION is denied")
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun downloadBook(){
        Log.d(TAG, "downloadBook: Downloading Book")

        progressDialog.setMessage("Downloading Book")
        progressDialog.show()


        //download book from firebase storage using url
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl)
        storageReference.getBytes(Constants.MAX_BYTES_PDF)

            .addOnSuccessListener { bytes->
                Log.d(TAG, "downloadBook : Book downloaded...")
                saveDownloadsFolder(bytes)
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Log.d(TAG, "downloadBook: Failed to download book due to ${e.message}")
                Toast.makeText(this, "Failed to download book due to", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDownloadsFolder(bytes: ByteArray) {
            Log.d(TAG, "saveToDownloadsFolder: saving downloaded book")

          val nameWithExtension =  "${System.currentTimeMillis()}.pdf"

        try {
            val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            downloadsFolder.mkdirs() //create folder if not exist

            val filePath = downloadsFolder.path + "/"+ nameWithExtension

            val out = FileOutputStream(filePath)
            out.write(bytes)
            out.close()


            Toast.makeText(this, "Saved to Downloads Folder", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
            incrementDownloadCount()
        }
        catch (e: Exception){
            progressDialog.dismiss()
            Log.d(TAG, "Saved to DownloadsFolder: failed to save due to ${e.message}")
            Toast.makeText(this, "failed to save due to", Toast.LENGTH_SHORT).show()
        }
    }

    private fun incrementDownloadCount() {
            //increment download count to firebase db
        Log.d(TAG, "incrementDownloadCount: ")

        //step 1  Get previous download count
        val ref = FirebaseDatabase.getInstance().getReference("Events")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get downloads count
                    var downloadsCount = "${snapshot.child("downloadsCount").value}"
                    Log.d(TAG, "onDataChange: Current Downloads Count: $downloadsCount")

                    if (downloadsCount == "" || downloadsCount == "null"){
                        downloadsCount = "0"
                    }

                    //convert to long and increment 1
                    val newDownloadCount : Long = downloadsCount.toLong() + 1
                    Log.d(TAG, "onDataChange: New Downloads Count: $newDownloadCount")

                    //setup data to update to db
                    val hashMap : HashMap<String, Any> = HashMap()
                    hashMap["downloadsCount"] = newDownloadCount


                //step 2  Update new incremented download count to db
                val dbRef = FirebaseDatabase.getInstance().getReference("Events")
                dbRef.child(bookId)
                    .updateChildren(hashMap)
                    .addOnSuccessListener {
                        Log.d(TAG, "onDataChange: Downloads count incremented")
                    }
                    .addOnFailureListener { e->
                        Log.d(TAG, "onDataChange: Failed to increment due to ${e.message}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }

    private fun loadBookDetails() {
        //books > bookId > Details
        val ref = FirebaseDatabase.getInstance().getReference("Events")

        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get data
                    val categoryId = "${snapshot.child("categoryId").value}"
                    val description = "${snapshot.child("description").value}"
                    val downloadsCount = "${snapshot.child("downloadsCount").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"
                    bookTitle = "${snapshot.child("title").value}"
                    val uid = "${snapshot.child("uid").value}"
                    bookUrl = "${snapshot.child("url").value}"
                    val viewsCount = "${snapshot.child("viewsCount").value}"

                    //format date
                    val date = MyApplication.formatTimeStamp(timestamp.toLong())

                    //load pdf category
                    MyApplication.loadCategory(categoryId, binding.categoryTv)

                    //load pdf thumbnail, pages count
                    MyApplication.loadPdfFromUrlSinglePage("$bookUrl", "$bookTitle", binding.pdfView, binding.progressBar, binding.pagesTv)

                        //load pdf size
                    MyApplication.loadPdfSize("$bookUrl", "$bookTitle", binding.sizeTv)


                    //set data
                    binding.titleTv.text = bookTitle
                    binding.descriptionTv.text = description
                    binding.viewsTv.text = viewsCount
                    binding.downloadsTv.text = downloadsCount
                    binding.dateTv.text = date
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}