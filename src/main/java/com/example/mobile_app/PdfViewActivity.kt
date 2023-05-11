package com.example.mobile_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.mobile_app.databinding.ActivityPdfViewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class PdfViewActivity : AppCompatActivity() {


    //view binding
    private lateinit var binding: ActivityPdfViewBinding

    //TAG
    private companion object{
        const val TAG = "PDF_VIEW_TAG"
    }

    //book id
    var bookId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get book id from intent , it will be used to load book from firebase
        bookId = intent.getStringExtra("bookId")!!
        loadBookDetails()


        //handle click go back
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadBookDetails() {
        Log.d(TAG, "loadBookDetails: Get Pdf URL from db")
        //db reference to get book details

        //step  1) Get book URL using Book ID

        val ref = FirebaseDatabase.getInstance().getReference("Events")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get book url
                    val pdfUrl = snapshot.child("url").value
                    Log.d(TAG, "onDataChange: PDF_URL: $pdfUrl")


                    //step  2)  load pdf using url from firebase storage
                   loadBookFromUrl("$pdfUrl")
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }


    @SuppressLint("SuspiciousIndentation")
    private fun loadBookFromUrl(pdfUrl: String) {
        Log.d(TAG, "loadBookFromUrl: Get Pdf from firebase storage using URL")


      val reference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
        reference.getBytes(Constants.MAX_BYTES_PDF)

            .addOnSuccessListener { bytes->
                Log.d(TAG,"loadBookFromUrl: pdf got from url")

                //load pdf
                binding.pdfView.fromBytes(bytes)
                    .swipeHorizontal(false)//set false to scroll vertical. set true to scroll horizontal
                    .onPageChange{page, pageCount->
                         //set current and total pages in toolbar subtitle
                        val currentPage = page+1 //page start from 0
                        binding.toolbarSubtitleTv.text = "$currentPage/$pageCount"
                        Log.d(TAG, "loadBookFromUrl: currentPage/$pageCount")
                    }
                    .onError { t->
                        Log.d(TAG, "loadBookFromUrl: ${t.message}")
                    }
                    .onPageError { page, t->
                        Log.d(TAG, "loadBookFromUrl: ${t.message}")
                    }
                    .load()
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { e->
                Log.d(TAG, "loadBookFromUrl: Failed to get pdf due to ${e.message}")

                binding.progressBar.visibility = View.GONE
            }
    }
}