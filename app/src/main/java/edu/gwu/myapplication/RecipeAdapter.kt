package edu.gwu.myapplication

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import edu.gwu.myapplication.R

class RecipeAdapter constructor(private val recipeData: List<RecipeData>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {
    //when alerts activity is launch, on create builds the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_recipes, parent, false)
        return ViewHolder(view)
    }

    //returns number of rows for the list
    override fun getItemCount(): Int = recipeData.size

    //content for each list input: username, content, and image icon
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAlert = recipeData[position]

        holder.usernameTextView.text = currentAlert.title
        holder.contentTextView.text = currentAlert.description
    }

    //references for each constructor in list
    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        val usernameTextView: TextView = view.findViewById(R.id.title)

        val contentTextView: TextView = view.findViewById(R.id.description)

        val iconImageView: ImageView = view.findViewById(R.id.icon)

    }
}