package com.codepath.pokemonpokedex

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var pokemonName: TextView
    private lateinit var pokemonNumber: TextView
    private lateinit var pokemonImage: ImageView
    private lateinit var newPokemonButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pokemonName = findViewById(R.id.pokemon_name)
        pokemonNumber = findViewById(R.id.pokemon_number)
        pokemonImage = findViewById(R.id.pokemon_image)
        newPokemonButton = findViewById(R.id.new_pokemon_button)

        getPokemon()
        newPokemonButton.setOnClickListener {
            getPokemon()
        }
    }
    private fun getPokemon() {
        val client = AsyncHttpClient()
        val pokemonId = Random.nextInt(1, 1026) // Get a random Pokémon ID
        val url = "https://pokeapi.co/api/v2/pokemon/$pokemonId/"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("Pokemon", "Response successful: $json")

                val name = json.jsonObject.getString("name").replaceFirstChar { it.uppercase() }
                val number = json.jsonObject.getInt("id")
                val imageUrl = json.jsonObject.getJSONObject("sprites").getString("front_default")

                // Update UI
                pokemonName.text = name
                pokemonNumber.text = "#${String.format("%03d", number)}"
                Glide.with(this@MainActivity).load(imageUrl).into(pokemonImage)
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.e("Pokemon", "Failed to get Pokémon: $errorResponse")
            }
        })
    }
}