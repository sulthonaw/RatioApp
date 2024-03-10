package com.ratioapp.viewModels

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.ratioapp.Constant
import com.ratioapp.api.RatioApi
import com.ratioapp.models.ChatMessage
import com.ratioapp.models.User
import com.ratioapp.models.ViewUiState
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class DetailChatViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val userId: String = checkNotNull(savedStateHandle["userId"])
    private val db = Firebase.firestore

    var chatMessage by mutableStateOf("")

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages

    private var _messagesReceiver = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messagesReceiver: LiveData<MutableList<Map<String, Any>>> = _messages

    private var _messagesSender = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messagesSender: LiveData<MutableList<Map<String, Any>>> = _messages

    var userUiState: ViewUiState<User> by mutableStateOf(ViewUiState.Loading(false))

    fun getUsers(token: String, userId: String) {
        viewModelScope.launch {
            userUiState = try {
                val request =
                    RatioApi().userService.getUser(token = "Bearer $token", userId = userId)
                ViewUiState.Success(request.data)
            } catch (e: HttpException) {
                ViewUiState.Error(e)
            } catch (e: IOException) {
                ViewUiState.Error(e)
            }
        }
    }

    fun addNewMessage(
        from: String,
        snackbarHostState: SnackbarHostState,
    ) {
        if (chatMessage.isNotEmpty())
            db.collection(Constant.COLLECTION_MESSAGES).document(from).collection(userId).add(
                hashMapOf(
                    Constant.FIELD_MESSAGE to chatMessage,
                    Constant.FIELD_TO_USERID to userId,
                    Constant.FIELD_FROM_USERID to from,
                    Constant.SEND_ON to System.currentTimeMillis()
                )
            ).addOnFailureListener {
                viewModelScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Gagal mengirim pesan",
                        withDismissAction = true
                    )
                }
            }.addOnSuccessListener {
                chatMessage = ""
            }
    }

    fun getMessagesReceiver(from: String) {
        db.collection(Constant.COLLECTION_MESSAGES).document(userId).collection(from)
            .orderBy("send_on", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                val TAG = "FIREBASE"
                val chats = mutableListOf<Map<String, Any>>().toMutableList()
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null)
                    for (item in snapshot) {
                        chats.add(item.data)
                    }

                updateMessagesReceive(chats)
                messagesSender.value?.let { chats.addAll(it) }
                updateMessages(chats.distinct().sortedByDescending { it["send_on"].toString().toLong() }.toMutableList())
            }
    }

    fun getMessagesSender(from: String) {
        db.collection(Constant.COLLECTION_MESSAGES).document(from).collection(userId)
            .orderBy("send_on", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                val TAG = "FIREBASE"
                val chats = mutableListOf<Map<String, Any>>().toMutableList()
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null)
                    for (item in snapshot) {
                        chats.add(item.data)
                    }

                updateMessagesSender(chats)
                messagesReceiver.value?.let { chats.addAll(it) }
                updateMessages(chats.distinct().sortedByDescending { it["send_on"].toString().toLong() }.toMutableList())
            }
    }


    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }

    private fun updateMessagesSender(list: MutableList<Map<String, Any>>) {
        _messagesSender.value = list.asReversed()
    }

    private fun updateMessagesReceive(list: MutableList<Map<String, Any>>) {
        _messagesReceiver.value = list.asReversed()
    }
}