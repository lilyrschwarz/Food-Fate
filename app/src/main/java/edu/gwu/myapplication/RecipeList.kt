package edu.gwu.myapplication.activity

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import edu.gwu.myapplication.R
import edu.gwu.myapplication.RecipeListAdapter
import edu.gwu.myapplication.RecipeData
import org.json.JSONException
import org.json.JSONObject
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.support.v7.widget.LinearLayoutManager
import com.android.volley.Response.Listener
import edu.gwu.myapplication.PizzaActivity

class RecipeList : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var tryButton: Button

    private lateinit var pizzaButton: Button

    var networkRequest: RequestQueue? = null
    var recipeList: ArrayList<RecipeData>? = null
    var recipeListAdapter: RecipeListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.recipe_list)
        tryButton = findViewById(R.id.tryButton)
        pizzaButton = findViewById(R.id.pizzaButton)

        tryButton.setOnClickListener {
            Log.d("RecipesList", "TRY Clicked")

            val intent: Intent = Intent(this, RecipeActivity::class.java)
            startActivity(intent)
        }

        pizzaButton.setOnClickListener {
            Log.d("RecipesList", "PIZZA Clicked")

            val intent: Intent = Intent(this, PizzaActivity::class.java)
            startActivity(intent)
        }

        var url: String?

        var extras = intent.extras
        var ingredient1 = extras.get("ingredient1")
        var ingredient2 = extras.get("ingredient2")
        var ingredient3 = extras.get("ingredient3")

        if (extras != null && !ingredient1.equals("")
            && !ingredient2.equals("") && !ingredient3.equals("")) {

            var tempUrl = "http://www.recipepuppy.com/api/?i=" + ingredient1 + "%2C" + ingredient2 + "%2C" + ingredient3 + "&q"
            url = tempUrl

        }else {
            AlertDialog.Builder(this)
                .setTitle("Oops!")
                .setMessage("Please Enter 3 Ingredients!")
                .setPositiveButton("Got It!") { dialog, which ->
                    // User pressed OK
                    val intent: Intent = Intent(this, RecipeActivity::class.java)
                    startActivity(intent)
                }
                .show()
             url = ""
        }

        recipeList = ArrayList<RecipeData>()

        networkRequest = Volley.newRequestQueue(this)

        getRecipe(url)

    }

    fun getRecipe(url: String) {
        val recipeRequest = JsonObjectRequest(Request.Method.GET,
            url, Listener {
                    response: JSONObject ->
                try {

                    val recipeArray = response.getJSONArray("results")

                    for (i in 0 until recipeArray.length()) {
                        var recipeObj = recipeArray.getJSONObject(i)

                        var title = recipeObj.getString("title")
                        var link = recipeObj.getString("href")
                        var image = recipeObj.getString("thumbnail")
                        var ingredients = recipeObj.getString("ingredients")

                        var recipe = RecipeData()
                        recipe.title = title
                        recipe.link = link
                        recipe.thumbnail = image
                        recipe.ingredients = "List of Ingredients:\n $ingredients"

                        recipeList!!.add(recipe)

                        recyclerView = findViewById(R.id.recyclerView)
                        recyclerView.layoutManager = LinearLayoutManager(this)
                        recipeListAdapter = RecipeListAdapter(this, R.layout.recipe_view, recipeList!!)
                        recyclerView!!.adapter = recipeListAdapter
                    }

                }catch (e: JSONException) { e.printStackTrace() }

            },
            Response.ErrorListener {
                    error: VolleyError? ->
                try {
                    Log.d("Error:", error.toString())

                } catch (e: JSONException) { e.printStackTrace() }
            })

        networkRequest!!.add(recipeRequest)
    }
}
