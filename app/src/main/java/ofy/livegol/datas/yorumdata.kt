package ofy.livegol.datas

class yorumdata{
    var res_yorumcu:String?=null
    var reslow_yorumcu:String?=null
    var nick_yorumcu:String?=null
    var yorum_yorumcu:String?=null
    constructor(res_yorumcu:String,reslow_yorumcu:String?,nick_yorumcu:String,yorum_yorumcu:String){
        this.res_yorumcu=res_yorumcu
        this.nick_yorumcu=nick_yorumcu
        this.yorum_yorumcu=yorum_yorumcu
        this.reslow_yorumcu=reslow_yorumcu
    }
    constructor(){}
}