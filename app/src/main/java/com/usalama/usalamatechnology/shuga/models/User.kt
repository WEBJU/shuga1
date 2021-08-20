package com.usalama.usalamatechnology.shuga.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(var username:String?=null,var birthday:String?=null,var location:String?=null,var work:String?=null,var phone:String?=null,var status:String?=null):
    Parcelable {
    constructor() : this("", "","","", "","")}

@Parcelize
data class Security(var uid:String?=null,var username:String?=null,var password:String?=null):
    Parcelable {
    constructor() : this("", "","")}