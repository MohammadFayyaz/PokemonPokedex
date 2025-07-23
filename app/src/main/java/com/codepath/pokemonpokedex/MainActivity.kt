package com.codepath.pokemonpokedex

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private lateinit var pokemonList: MutableList<Pokemon>
    private lateinit var rvPokemon: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPokemon = findViewById(R.id.pokemon_list)
        pokemonList = mutableListOf()

        getPokemonList()
    }

    private fun getPokemonList() {
        val client = AsyncHttpClient()
        // This URL gets a list of the first 20 Pokémon
        val url = "https://pokeapi.co/api/v2/pokemon?limit=20&offset=0"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("MainActivity", "Response successful: ${json.jsonObject}")
                try {
                    val resultsArray = json.jsonObject.getJSONArray("results")

                    for (i in 0 until resultsArray.length()) {
                        val pokemonObject = resultsArray.getJSONObject(i)
                        val pokemonName = pokemonObject.getString("name").replaceFirstChar { it.uppercase() }
                        val pokemonUrl = pokemonObject.getString("url")

                        // For each Pokémon in the list, we need to make another call to get its details
                        getPokemonDetails(pokemonName, pokemonUrl)
                    }
                } catch (e: JSONException) {
                    Log.e("MainActivity", "JSON Exception: $e")
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.e("MainActivity", "Failed to get Pokémon list: $errorResponse")
            }
        })
    }

    private fun getPokemonDetails(name: String, url: String) {
        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val number = json.jsonObject.getInt("id")
                    val imageUrl = json.jsonObject.getJSONObject("sprites").getString("front_default")

                    // Add the new Pokémon to our list
                    pokemonList.add(Pokemon(name, number, imageUrl))

                    // Sort the list by Pokémon number to ensure they appear in order
                    pokemonList.sortBy { it.number }

                    // Create and set the adapter
                    val adapter = PokemonAdapter(pokemonList)
                    rvPokemon.adapter = adapter
                    rvPokemon.layoutManager = LinearLayoutManager(this@MainActivity)

                } catch (e: JSONException) {
                    Log.e("MainActivity", "Failed to parse details: $e")
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.e("MainActivity", "Failed to get Pokémon details: $errorResponse")
            }
        })
    }
}
