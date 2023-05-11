package com.example.mobile_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SupEmpAdapter (private val empList: ArrayList<SupEmpModel>): RecyclerView.Adapter<SupEmpAdapter.ViewHolder>() {

    // Listener for item clicks
    private lateinit var mListener: onItemsClickListener

    // Interface for the item click listener
    interface onItemsClickListener{
        fun onItemsClick(position: Int)
    }

    // Method to set the item click listener
    fun setonItemsClickListener(clickListener: onItemsClickListener){
        mListener = clickListener
    }

    // Create a ViewHolder for the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for the list item view
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_list, parent, false)
        // Return a new ViewHolder for the list item view
        return ViewHolder(itemView, mListener)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder:SupEmpAdapter.ViewHolder, position: Int)
    {
        // Get the current employee from the employee list
        val currentEmp = empList[position]
        // Set the employee name in the ViewHolder's TextView
        holder.tvEmpName.text = currentEmp.empType

    }

    // Get the number of items in the employee list
    override fun getItemCount(): Int {
        return empList.size
    }

    // Define the ViewHolder for the RecyclerView
    class ViewHolder (itemView: View, ClickListener: onItemsClickListener) : RecyclerView.ViewHolder(itemView){
        // Initialize the TextView for the employee name
        val  tvEmpName: TextView = itemView.findViewById(R.id.tvEmpName)

        init {
            // Set a click listener for the list item view
            itemView.setOnClickListener{
                // Call the onItemClick method of the item click listener with the clicked item's position
                ClickListener.onItemsClick(adapterPosition)
            }
        }
    }

  }


