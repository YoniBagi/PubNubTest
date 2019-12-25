package com.example.pubnubtest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.pubnubtest.adapters.MessagesAdapter
import com.example.pubnubtest.databinding.ActivityMainBinding
import com.example.pubnubtest.pubNub.PubNubPnCallback
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PubNubPnCallback.MessagePubNubCallback {
    fun View.hideKeyboard(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
    private var mPubnub_DataStream: PubNub? = null
    lateinit var messagesAdapter: MessagesAdapter

    private lateinit var mPubSubPnCallback: PubNubPnCallback
    val PUBSUB_CHANNEL = listOf(Consts.CHANNEL_NAME)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.mainActivity = this
        initPubNub()
        iniChannels()
        initViews()
    }

    private fun initViews() {
        messagesAdapter = MessagesAdapter()
    }

    private fun initPubNub() {
        mPubSubPnCallback = PubNubPnCallback(this)
        val config = PNConfiguration()
            .setPublishKey(Consts.PUBNUB_PUBLISH_KEY)
            .setSubscribeKey(Consts.PUBNUB_SUBSCRIBE_KEY)
            .setUuid("yoni"/*userName.toString()*/)
            .setSecure(true)
        mPubnub_DataStream = PubNub(config)
    }

    private fun iniChannels() {
        mPubnub_DataStream?.addListener(mPubSubPnCallback)
        mPubnub_DataStream?.subscribe()?.channels(PUBSUB_CHANNEL)?.execute()
    }

    fun getAdapter(): MessagesAdapter {
        return messagesAdapter
    }

    override fun onMessageArrived(message: String) {
        messagesAdapter.addMessage(message)
        rvMessages.scrollToPosition(getAdapter().itemCount -1)
    }

    fun publish(messageToSend: String, userName: String) {
        val messageMap = mapOf(
            "sender" to userName,
            "message" to messageToSend
        )
        mPubnub_DataStream?.publish()?.channel(Consts.CHANNEL_NAME)?.message(messageMap)
            ?.async(mPNCallback)
        rvMessages.hideKeyboard()
    }

    private val mPNCallback = object : PNCallback<PNPublishResult>() {
        override fun onResponse(result: PNPublishResult?, status: PNStatus) {
            Log.d("PubNubPnCallback", "Status: $status Result: $result")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectAndCleanup()

    }

    private fun disconnectAndCleanup() {
        mPubnub_DataStream?.unsubscribe()?.channels(PUBSUB_CHANNEL)?.execute()
        mPubnub_DataStream?.removeListener(mPubSubPnCallback)
        //mPubnub_DataStream?.destroy()
        mPubnub_DataStream = null
    }
}
