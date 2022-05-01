package com.example.ello

class SmsMainModel (var _id: String? = null, var _address: String? = null, var _msg: String? = null, var _readState //"0" for have not read sms and "1" for have read sms
: String? = null, var _time: String? = null, var _folderName: String? = null){

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


    fun setId(id: String?) {
        _id = id
    }

    fun setAddress(address: String?) {
        _address = address
    }

    fun setMsg(msg: String?) {
        _msg = msg
    }

    fun setReadState(readState: String?) {
        _readState = readState
    }

    fun setTime(time: String?) {
        _time = time
    }

    fun setFolderName(folderName: String?) {
        _folderName = folderName
    }

}