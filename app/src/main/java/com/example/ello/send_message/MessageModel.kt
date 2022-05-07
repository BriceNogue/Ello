package com.example.ello.send_message

data class MessageModel(var _id: String, var _address: String, var _msg: String,
                     var _readState: String, var _time: String, var _folderName: String){

    //_readState "0" for have not read sms and "1" for have read sms
    constructor():this("","","","","",""){

    }

    fun getId(): String? {
        return _id
    }

    fun getAddress(): String? {
        return _address
    }

    fun getMsg(): String? {
        return _msg
    }

    fun getReadState(): String? {
        return _readState
    }

    fun getTime(): String? {
        return _time
    }

    fun getFolderName(): String? {
        return _folderName
    }

    @JvmName("setId1")
    fun setId(id: String) {
        _id = id
    }

    fun setId(): String {
        return _id
    }

    @JvmName("setAddress1")
    fun setAddress(address: String) {
        _address = address
    }

    fun setAddress(): String {
        return _address
    }

    @JvmName("setMsg1")
    fun setMsg(msg: String) {
        _msg = msg
    }

    fun setMsg(): String {
        return _msg
    }

    @JvmName("setReadState1")
    fun setReadState(readState: String) {
        _readState = readState
    }

    fun readState(): String {
        return _readState
    }

    @JvmName("setTime1")
    fun setTime(time: String) {
        _time = time
    }

    fun setTime(): String {
        return _time
    }

    @JvmName("setFolderName1")
    fun setFolderName(folderName: String) {
        _folderName = folderName
    }

    fun setFolderName(): String {
        return _folderName
    }
}
