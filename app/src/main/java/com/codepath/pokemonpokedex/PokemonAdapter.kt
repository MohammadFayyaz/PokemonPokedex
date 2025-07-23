package com.codepath.pokemonpokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// Data class to hold the information for a single Pokémon
data class Pokemon(
    val name: String,
    val number: Int,
    val imageUrl: String
)

class PokemonAdapter(private val pokemonList: List<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    // ViewHolder holds the views for each item in the RecyclerView
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonImage: ImageView = view.findViewById(R.id.pokemon_image)
        val pokemonName: TextView = view.findViewById(R.id.pokemon_name)
        val pokemonNumber: TextView = view.findViewById(R.id.pokemon_number)
    }

    // Creates a new ViewHolder when the RecyclerView needs one
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_item, parent, false)
        return ViewHolder(view)
    }

    // Binds the data to the views in a specific ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonList[position]

        holder.pokemonName.text = pokemon.name
        holder.pokemonNumber.text = "#${String.format("%03d", pokemon.number)}"

        Glide.with(holder.itemView.context)
            .load(pokemon.imageUrl)
            .into(holder.pokemonImage)
    }

    // Returns the total number of items in the list
    override fun getItemCount(): Int {
        return pokemonList.size
    }
}
