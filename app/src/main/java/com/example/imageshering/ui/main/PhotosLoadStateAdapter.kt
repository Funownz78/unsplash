package com.example.imageshering.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imageshering.databinding.PhotosFooterItemBinding

class PhotosLoadStateAdapter(
    private val retry: ()->Unit,
) : LoadStateAdapter<PhotosLoadStateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PhotosLoadStateViewHolder {
        val binding = PhotosFooterItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return PhotosLoadStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: PhotosLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}

class PhotosLoadStateViewHolder(
    private val binding: PhotosFooterItemBinding,
    private val retry: ()->Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.tryAgainButton.setOnClickListener { retry.invoke() }
    }
    fun bind(loadState: LoadState) {
        when (loadState) {
            is LoadState.NotLoading -> {
                binding.tryAgainButton.visibility = View.GONE
                binding.progressCircular.visibility = View.GONE
            }
            LoadState.Loading -> {
                binding.tryAgainButton.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE
            }
            is LoadState.Error -> {
                binding.tryAgainButton.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
            }
        }
    }
}
