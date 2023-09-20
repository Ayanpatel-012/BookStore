package com.dailyrounds.bookstore.Adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dailyrounds.bookstore.Models.Book
import com.dailyrounds.bookstore.R


class Adaptor(private val bookList: ArrayList<Book>, private val listener: EventClickListener) :
    RecyclerView.Adapter<Adaptor.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val hitsCnt: TextView = itemView.findViewById(R.id.hitsCnt)
        val subTitle: TextView = itemView.findViewById(R.id.subTitle)
        val bookImg: ImageView = itemView.findViewById(R.id.bookImg)
        val favImg: ImageView = itemView.findViewById(R.id.favImg)
        val clMain: View = itemView.findViewById(R.id.clMain)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false)
        return BookViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.title.text = book.title
        holder.subTitle.text = book.alias
        holder.hitsCnt.text = book.hits.toString()
        Glide.with(holder.itemView.context)
            .load(book.image)
            .into(holder.bookImg)
        holder.favImg.setOnClickListener {
            listener.onFavClicked(book.id, !book.fav)
        }
        holder.clMain.setOnClickListener {
            listener.openBookDetails(book)
        }
        if (book.fav) {
            holder.favImg.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.favImg.context,
                    R.drawable.fav_check
                )
            )
        } else {
            holder.favImg.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.favImg.context,
                    R.drawable.fav_uncheck
                )
            )
        }

    }

}

interface EventClickListener {
    fun onFavClicked(id: String, newValue: Boolean)
    fun openBookDetails(book: Book)
}