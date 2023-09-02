package com.example.imageshering.ui.main.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.imageshering.R
import com.example.imageshering.databinding.PhotoInCollectionItemBinding
import com.example.imageshering.domain.PhotoInCollection

class PhotoInCollectionAdapter(
    private val isConnected: () -> Boolean,
    private val setLike: (String, () -> Unit) -> Unit,
    private val removeLike: (String, () -> Unit) -> Unit,
    private val transitionToDetails: (id: String) -> Unit,
) : PagingDataAdapter<PhotoInCollection, PhotoInCollectionAdapter.CollectionViewHolder>(
    PHOTO_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding =
            PhotoInCollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CollectionViewHolder(
        private val binding: PhotoInCollectionItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photoCollection: PhotoInCollection) {
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
                nameView.text = photoCollection.name
                usernameView.text = photoCollection.username
                setLikeState(photoCollection.likedByUser, photoCollection, selfLikeView)
            }
        }

        private fun setLikeState(
            state: Boolean,
            photo: PhotoInCollection,
            selfLikeView: ImageButton
        ) {
            if (state) {
                selfLikeView.setImageResource(R.drawable.heart_filled)
                selfLikeView.setOnClickListener {
                    if (isConnected()) {
                        selfLikeView.setImageResource(R.drawable.heart_unfilled)
                        setLikeState(false, photo, selfLikeView)
                        removeLike(photo.id) {
                            selfLikeView.setImageResource(R.drawable.heart_filled)
                            setLikeState(true, photo, selfLikeView)
                        }
                    }
                }
            } else {
                selfLikeView.setImageResource(R.drawable.heart_unfilled)
                selfLikeView.setOnClickListener {
                    if (isConnected()) {
                        selfLikeView.setImageResource(R.drawable.heart_filled)
                        setLikeState(true, photo, selfLikeView)
                        setLike(photo.id) {
                            selfLikeView.setImageResource(R.drawable.heart_unfilled)
                            setLikeState(false, photo, selfLikeView)
                        }
                    }
                }
            }
        }

    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<PhotoInCollection>() {
            override fun areItemsTheSame(
                oldItem: PhotoInCollection,
                newItem: PhotoInCollection
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: PhotoInCollection,
                newItem: PhotoInCollection
            ): Boolean =
                oldItem.id == newItem.id

        }
    }
}