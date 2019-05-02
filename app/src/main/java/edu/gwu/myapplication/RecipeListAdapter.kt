package edu.gwu.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import android.support.v7.widget.RecyclerView

/**recycler view for Recipe List**/
class RecipeListAdapter constructor(var mCtx: Context, var resource: Int, private val items: List<RecipeData>) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>()
{
    override fun getItemCount(): Int = resource


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(resource, null)
        return ViewHolder(view)

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe: RecipeData = items[position]
        holder.title.text = recipe.title
        holder.ingredients.text = recipe.ingredients

        holder.linkButton.setOnClickListener {

            var link = recipe.link.toString()
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            mCtx.startActivity(intent)
        }

        // imports image thumbnails for the images
        if (!TextUtils.isEmpty(recipe.thumbnail)) {
            Picasso.with(mCtx)
                .load(recipe.thumbnail)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.thumbnail)
        }

    }

    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        var title: TextView = view.findViewById(R.id.recipeTitle)
        var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        var ingredients: TextView = view.findViewById(R.id.recipeIngredients)
        var linkButton: Button = view.findViewById(R.id.linkButton)

    }
}