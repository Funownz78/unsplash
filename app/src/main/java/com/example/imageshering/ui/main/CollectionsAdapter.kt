package com.example.imageshering.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.imageshering.R
import com.example.imageshering.databinding.PhotoCollectionItemBinding
import com.example.imageshering.domain.PhotoCollection

class CollectionsAdapter(
    private val transitionToDetails: (id: String) -> Unit,
) : PagingDataAdapter<PhotoCollection, CollectionsAdapter.CollectionViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding =
            PhotoCollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CollectionViewHolder(
        private val binding: PhotoCollectionItemBinding,
        private val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photoCollection: PhotoCollection) {
            binding.apply {
                root.setOnClickListener {
                    transitionToDetails(photoCollection.id)
                }
                Glide.with(photoView)
                    .load(photoCollection.imageUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.baseline_error_24_day)
                    .into(photoView)
                Glide.with(avatarView)
                    .load(photoCollection.avatarUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.baseline_error_24_day)
                    .into(avatarView)
                sizeText.text =
                    context.getString(R.string.collection_size, photoCollection.totalPhotos)
                collectionTitle.text = photoCollection.title
                nameView.text = photoCollection.name
                usernameView.text = photoCollection.username
            }
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<PhotoCollection>() {
            override fun areItemsTheSame(
                oldItem: PhotoCollection,
                newItem: PhotoCollection
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: PhotoCollection,
                newItem: PhotoCollection
            ): Boolean =
                oldItem.id == newItem.id

        }
    }
}