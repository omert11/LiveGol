package ofy.livegol.services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class fcmmodel {
    @Expose
    @SerializedName("data")
    var data: Data? = null
    @Expose
    @SerializedName("to")
    var to: String? = null
    constructor(data:Data,to:String){
        this.data=data
        this.to=to
    }
    class Data {
        @Expose
        @SerializedName("mesaj")
        var mesaj: String? = null

        @Expose
        @SerializedName("baslik")
        var baslik: String? = null
        constructor(mesaj:String,baslik:String){
            this.baslik=baslik
            this.mesaj=mesaj
        }
    }
}