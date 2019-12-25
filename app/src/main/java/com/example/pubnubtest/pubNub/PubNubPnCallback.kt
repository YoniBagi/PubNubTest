package com.example.pubnubtest.pubNub

import android.util.Log
import com.google.gson.JsonObject
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNMembershipResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSpaceResult
import com.pubnub.api.models.consumer.pubsub.objects.PNUserResult
import java.lang.Exception

class PubNubPnCallback(val mMessagePubNubCallback: MessagePubNubCallback) : SubscribeCallback() {

    override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
        try {
            val message = (pnMessageResult.message as JsonObject).asJsonObject["message"].asString
            Log.d("PubNubPnCallback", message)
            mMessagePubNubCallback.onMessageArrived(message)
        } catch (e: Exception) {

        }
    }

    override fun user(pubnub: PubNub, pnUserResult: PNUserResult) {
    }

    override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
    }

    override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
    }

    override fun membership(pubnub: PubNub, pnMembershipResult: PNMembershipResult) {
    }

    override fun space(pubnub: PubNub, pnSpaceResult: PNSpaceResult) {
    }

    override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
    }

    override fun status(pubnub: PubNub, pnStatus: PNStatus) {
        Log.d("status", "pubnub: $pubnub, pnStatus: $pnStatus")
    }

    interface MessagePubNubCallback {
        fun onMessageArrived(message: String)
    }
}