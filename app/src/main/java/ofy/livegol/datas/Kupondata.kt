package ofy.livegol.datas

class Kupondata{
    var Kuponismi:String?=null
    var Kupondurumu:String?=null
    var maclar:ArrayList<macdata>?=null
    constructor(Kuponismi:String, Kupondurumu:String,maclar:ArrayList<macdata>){
        this.Kuponismi=Kuponismi
        this.Kupondurumu=Kupondurumu
        this.maclar=maclar
    }
    constructor(){}
}
class Canlidata{
    var Kuponismi:String?=null
    var Kupondurumu:String?=null
    var maclar:ArrayList<macdata>?=null
    var Kupondali:String?=null
    constructor(Kuponismi:String, Kupondurumu:String, maclar:ArrayList<macdata>, Kupondali:String){
        this.Kuponismi=Kuponismi
        this.Kupondurumu=Kupondurumu
        this.maclar=maclar
        this.Kupondali=Kupondali
    }
    constructor(){}
}