package edu.gwu.testingapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import edu.gwu.project1.RecipeAdapter
import edu.gwu.project1.RecipeData

class RecipesActivity : AppCompatActivity()   {

    private lateinit var recyclerView: RecyclerView

    private lateinit var tryButton: Button

    private lateinit var pizzaButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipes)
        tryButton = findViewById(R.id.tryButton)
        pizzaButton = findViewById(R.id.pizzaButton)

        recyclerView = findViewById(R.id.recyclerView)

        // Set the direction of our list to be vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        val recipes = fakeRecipe()
        recyclerView.adapter = RecipeAdapter(recipes)

        title = getString(R.string.recipe_title)

        fakeRecipe()

        tryButton.setOnClickListener {
            Log.d("RecipesActivity", "TRY Clicked")

            val intent: Intent = Intent(this, IngredientsActivity::class.java)
            startActivity(intent)
        }

        pizzaButton.setOnClickListener {
            Log.d("RecipesActivity", "PIZZA Clicked")

            val intent: Intent = Intent(this, PizzaActivity::class.java)
            startActivity(intent)
        }
    }
    private fun fakeRecipe(): List<RecipeData> {
        return listOf(
            RecipeData(
                title = "Pasta",
                description = "Wow! This is pasta.",
                iconUrl = "https://...."
            ),
            RecipeData(
                title = "Steak",
                description = "OMG STEAK",
                iconUrl = "https://...."
            )
        )
    }
}