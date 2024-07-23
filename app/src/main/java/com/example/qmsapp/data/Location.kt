package com.example.qmsapp.data

import java.sql.Time

data class Location(val locId:Int,
                    val country:String?="",
                    val city:String?="",
                    val postalCode:String?="",
                    val address:String?="",
                    val description:String?="",
                        val name:String?="",
                    val lat:Float?=0f,
                    val lng:Float?=0f,
                    val opensAt:String?,
                    val closesAt:String?)
