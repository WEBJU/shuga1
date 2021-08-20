package com.usalama.usalamatechnology.shuga.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*data class Users(var uid:String?=null,var firstName:String?=null,var lastName:String?=null,var gender:String?=null,var profileUrl:String?=null,var gender2:String?=null,var dob:String?=null,var age:String?=null, var sugar:String?=null){
    constructor() : this("", "","", "","", "","","","")
}*/
@Parcelize
data class Pics(var uid:String?=null,var img1:String?=null,var img2:String?=null,var Img3:String?=null):Parcelable{
    constructor() : this("", "","","")
}
data class Shugas(var uid:String?=null,var firstName:String?=null,var lastName:String?=null,var domain:String?=null,var gender:String?=null,var profileUrl:String?=null,var gender2:String?=null,var dob:String?=null,var age:String?=null, var sugar:String?=null){
    constructor() : this("", "","","", "","", "","","","")
}
data class Pic(var uid:String?=null,var img1:String?=null){
    constructor() : this("", "")
}
@Parcelize
data class Users(var uid:String?=null,var firstName:String?=null,var lastName:String?=null,var domain:String?=null,var gender:String?=null,var profileUrl:String?=null,var gender2:String?=null,var dob:String?=null,var age:String?=null, var sugar:String?=null):Parcelable{
    constructor() : this("", "","", "","", "","","","","")}
/*
class Users{
    var uid:String?=null
    var firstName:String?=null
    var lastName:String?=null
    var gender:String?=null
    var profileUrl:String?=null
    var gender2:String?=null
    var dob:String?=null
    var age:String?=null
    var sugar:String?=null}*/
