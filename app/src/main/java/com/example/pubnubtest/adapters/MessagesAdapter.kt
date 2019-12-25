package com.example.pubnubtest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pubnubtest.R
import com.example.pubnubtest.databinding.ItemMessageAdapterBinding
import java.util.logging.Handler
import kotlin.collections.ArrayList

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {
    private var mMessageList: ArrayList<String> = arrayListOf("one", "tow")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemMessageAdapterBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_message_adapter,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mMessageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(mMessageList[position])
    }

    fun addMessage(message: String){
        mMessageList.add(message)
        notifyDataSetChanged()
    }

    inner class ViewHolder (var mBinding: ItemMessageAdapterBinding) :
    RecyclerView.ViewHolder (mBinding.root){
        fun onBind(message: String){
            mBinding.message = message
            /*mBinding.timeStamp = SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().time)*/
            mBinding.executePendingBindings()
        }
    }
}