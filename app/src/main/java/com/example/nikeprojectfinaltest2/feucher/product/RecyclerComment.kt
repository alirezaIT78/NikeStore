package com.example.nikeprojectfinaltest2.feucher.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.data.Comment

class RecyclerComment: RecyclerView.Adapter<RecyclerComment.RecyclerCommentVh>() {
    var commentList=ArrayList<Comment>()
    var booleanViewAll:Boolean=false
    var userComments=ArrayList<Comment>()
    inner class RecyclerCommentVh(itemView: View): RecyclerView.ViewHolder(itemView)
    {
       val txt_titleComment:TextView=itemView.findViewById(R.id.txt_titleComment)
       val txt_dateComment:TextView=itemView.findViewById(R.id.txt_dateComment)
       val txt_emailComment:TextView=itemView.findViewById(R.id.txt_emailComment)
       val txt_contentComment:TextView=itemView.findViewById(R.id.txt_contentComment)
        fun bindComment(comment: Comment)
        {
            txt_titleComment.text=comment.title
            txt_dateComment.text=comment.date
            txt_emailComment.text=comment.author.email
            txt_contentComment.text=comment.content
        }
    }
    fun changeComment(comments:ArrayList<Comment>)
    {
        userComments.removeAll(commentList)
        var i=comments.size-1
        while (i>=0)
        {
            userComments.add(comments.get(i))
            i--
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerCommentVh {
       return RecyclerCommentVh(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_comment,parent,false))
    }

    override fun getItemCount(): Int {
        if (commentList.size>3&&!booleanViewAll)return 3 else return commentList.size
    }

    override fun onBindViewHolder(holder: RecyclerCommentVh, position: Int) {
       holder.bindComment(userComments.get(position))
    }
}