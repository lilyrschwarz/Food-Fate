package edu.gwu.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import edu.gwu.myapplication.R
import android.widget.Toast

class IngredientsActivity : AppCompatActivity()  {

    private lateinit var recipeButton: Button
    private lateinit var enter1: EditText
    private lateinit var enter2: EditText
    private lateinit var enter3: EditText


    private val textWatcher: TextWatcher = object : TextWatcher
    {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        {
            val inputIngredient1: String = enter1.text.toString().trim()
            val inputIngredient2: String = enter2.text.toString().trim()
            val inputIngredient3: String = enter3.text.toString().trim()
            val enableButton: Boolean = inputIngredient1.isNotEmpty() && inputIngredient2.isNotEmpty() && inputIngredient3.isNotEmpty()

            recipeButton.isEnabled = enableButton
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ingredients)

        Log.d("IngredientsActivity", "onCreate called")

        recipeButton = findViewById(R.id.recipe_button)
        enter1 = findViewById(R.id.enter1)
        enter2 = findViewById(R.id.enter2)
        enter3 = findViewById(R.id.enter3)

        enter1.addTextChangedListener(textWatcher)
        enter2.addTextChangedListener(textWatcher)
        enter3.addTextChangedListener(textWatcher)

        recipeButton.setOnClickListener {
            Log.d("IngredientsActivity", "Recipe Clicked")
            val intent: Intent = Intent(this, RecipesActivity::class.java)
            startActivity(intent)
        }
    }
}