package com.example.mobile_app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.mobile_app.databinding.FragmentBookUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BookUserFragment : Fragment {

    //view binding fragment_book_user.xml => FragmentBookUserBinding
    private lateinit var binding:FragmentBookUserBinding
    public companion object {
        private const val TAG = "BOOKS_USER_TAG"

        public fun newInstance(categoryId: String, category:String, uid:String): BookUserFragment{
            val fragment = BookUserFragment()
            val args = Bundle()
            args.putString("categoryId",categoryId);
            args.putString("category",category);
            args.putString("uid",uid);
            fragment.arguments = args
            return fragment
        }

        //receive data from activity to load books
    }


    private var categoryId = ""
    private var category = ""
    private var uid = ""
    
    private lateinit var pdfArrayList: ArrayList<ModelPdf>
    private lateinit var adapterPdfUser: AdapterPdfUser
    
    constructor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        //get arguments that passed
        val args = arguments
        if (args != null){
            categoryId = args.getString("categoryId")!!
            category = args.getString("category")!!
            uid = args.getString("uid")!!
            
        }
        
    }
    
    override  fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       binding = FragmentBookUserBinding.inflate(LayoutInflater.from(context), container, false)
        
        //load pdf according to category
        Log.d(TAG, "onCreateView: Category: $category")
        if (category=="All"){
            //load all books
            loadAllBooks()
        }
        else if (category== "Most Viewed"){
            //load most viewed books
            loadMostViewedDownloadedBooks("viewsCount")
        }
        else if (category== "Most Downloaded"){
            //load most downloaded books
            loadMostViewedDownloadedBooks("downloadsCount")
        }
        else{
            //load selected category books
            loadCategorizedBooks()
        }
        //search
        binding.searchEt.addTextChangedListener{object :TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
               try {
                   adapterPdfUser.filter.filter(s)
            }
               catch (e:Exception){
                   Log.d(TAG, "onTextChanged: SEARCH EXCEPTION : ${e.message}")
               }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        }
        return binding.root
    }

    private fun loadAllBooks() {

        //init list
        pdfArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Events")

        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before starting adding data into it
                pdfArrayList.clear()
                for (ds in snapshot.children){
                    //get data
                    val model = ds.getValue(ModelPdf::class.java)

                    //add to list
                    pdfArrayList.add(model!!)
                }

                //setup adapter
                adapterPdfUser = AdapterPdfUser(context!!, pdfArrayList)
                //setup adapter
                binding.booksRv.adapter = adapterPdfUser
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    private fun loadMostViewedDownloadedBooks(orderBy: String) {
        //init list
        pdfArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Events")

        ref.orderByChild(orderBy).limitToLast(10) //load 10 most viewed or most downloaded
            .addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before starting adding data into it
                pdfArrayList.clear()
                for (ds in snapshot.children){
                    //get data
                    val model = ds.getValue(ModelPdf::class.java)

                    //add to list
                    pdfArrayList.add(model!!)
                }

                //setup adapter
                adapterPdfUser = AdapterPdfUser(context!!, pdfArrayList)
                //setup adapter
                binding.booksRv.adapter = adapterPdfUser
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun loadCategorizedBooks() {
        //init list
        pdfArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Events")

        ref.orderByChild("categoryId").equalTo(categoryId)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //clear list before starting adding data into it
                    pdfArrayList.clear()
                    for (ds in snapshot.children){
                        //get data
                        val model = ds.getValue(ModelPdf::class.java)

                        //add to list
                        pdfArrayList.add(model!!)
                    }

                    //setup adapter
                    adapterPdfUser = AdapterPdfUser(context!!, pdfArrayList)
                    //setup adapter
                    binding.booksRv.adapter = adapterPdfUser
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }


}
