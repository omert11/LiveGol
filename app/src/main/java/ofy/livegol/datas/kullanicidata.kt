package com.codof.matbacim.datas

import java.util.*

class kullanicidata{
    var Res:String?=null
    var Reslow:String?=null
    var Nick:String?=null
    var Coin:String?=null
    var Dogumtarihi:Date?=null
    var Degerlendirmedurum:Boolean?=null
    var Oydurum:Oydata?=null
    var Abonelik:Boolean?=null
    var Ayarlar:ayardata?=null
    var ReklamGun:String?=null
    var Reklamkalan:String?=null
    var token:String?=null
    var abonelikzaman:Date?=null
    var reklamkaldir:Boolean?=null
    var maclarim:String?=null
    var mesajengelim:Date?=null
    var songorulmem:Date?=null
    var adminlik:Int?=null
    constructor(Res:String,Reslow:String,Nick:String,Coin:String,Dogumtarihi:Date,Abonelik:Boolean,Ayarlar:ayardata,DegerlendirmeDurum:Boolean,Oydurum:Oydata,ReklamGun:String,ReklamKalan:String,token:String,abonelikzaman:Date,reklamkaldir:Boolean,maclarim:String,mesajengelim:Date,songorulmem:Date){
        this.Res=Res
        this.Nick=Nick
        this.Coin=Coin
        this.Dogumtarihi=Dogumtarihi
        this.Degerlendirmedurum=DegerlendirmeDurum
        this.Oydurum=Oydurum
        this.Abonelik=Abonelik
        this.Ayarlar=Ayarlar
        this.ReklamGun=ReklamGun
        this.Reklamkalan=ReklamKalan
        this.token=token
        this.abonelikzaman=abonelikzaman
        this.reklamkaldir=reklamkaldir
        this.maclarim=maclarim
        this.Reslow=Reslow
        this.mesajengelim=mesajengelim
        this.songorulmem=songorulmem

    }
    constructor(Res:String,Reslow:String,Nick:String,Coin:String,Dogumtarihi:Date,Abonelik:Boolean,Ayarlar:ayardata,DegerlendirmeDurum:Boolean,Oydurum:Oydata,ReklamGun:String,ReklamKalan:String,token:String,abonelikzaman:Date,reklamkaldir:Boolean,maclarim:String,mesajengelim:Date,songorulmem: Date,adminlik:Int){
        this.Res=Res
        this.Nick=Nick
        this.Coin=Coin
        this.Dogumtarihi=Dogumtarihi
        this.Degerlendirmedurum=DegerlendirmeDurum
        this.Oydurum=Oydurum
        this.Abonelik=Abonelik
        this.Ayarlar=Ayarlar
        this.ReklamGun=ReklamGun
        this.Reklamkalan=ReklamKalan
        this.token=token
        this.abonelikzaman=abonelikzaman
        this.reklamkaldir=reklamkaldir
        this.maclarim=maclarim
        this.Reslow=Reslow
        this.mesajengelim=mesajengelim
        this.adminlik=adminlik
        this.songorulmem=songorulmem
    }
    constructor(){}
}
class ayardata{
    var Stil:String?=null
    var Bildirim:Boolean?=null
    var Veritasarufu:Boolean?=null
    constructor(Stil:String,Bildirim:Boolean,Veritasarufu:Boolean){
        this.Stil=Stil
        this.Bildirim=Bildirim
        this.Veritasarufu=Veritasarufu
    }
    constructor(){}
}
class Oydata{
    var Gun:String?=null
    var Tahmin:String?=null
    var Durum:Boolean?=null
    constructor(Gun:String,Durum:Boolean){
        this.Gun=Gun
        this.Durum=Durum

    }
    constructor(Gun:String,Durum:Boolean,Tahmin:String){
        this.Gun=Gun
        this.Durum=Durum
        this.Tahmin=Tahmin

    }
    constructor(){}
}
