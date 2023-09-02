package com.example.imageshering.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.imageshering.R
import com.example.imageshering.databinding.PhotoItemBinding
import com.example.imageshering.domain.Photo

class FlatPhotosAdapter(
    private val isConnected: () -> Boolean,
    private val setLike: (String, () -> Unit) -> Unit,
    private val removeLike: (String, () -> Unit) -> Unit,
    private val transitionToDetails: (id: String) -> Unit,
) : ListAdapter<Photo, FlatPhotosAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class PhotoViewHolder(
        private val binding: PhotoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photoDao: Photo) {
            binding.apply {
                root.setOnClickListener {
                    transitionToDetails(photoDao.id)
                }
                Glide.with(photoView)
                    .load(photoDao.urlThumb)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.baseline_error_24_day)
                    .into(photoView)
                Glide.with(avatarView)
                    .load(photoDao.profileImage)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.baseline_error_24_day)
                    .into(avatarView)
                nameView.text = photoDao.name
                usernameView.text = photoDao.username
                likesView.text = photoDao.likes.toString()
                setLikeState(photoDao.likedByUser, photoDao, selfLikeView)
            }
        }

        private fun setLikeState(state: Boolean, photo: Photo, selfLikeView: ImageButton) {
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
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem.urlThumb == newItem.urlThumb

        }
    }
}