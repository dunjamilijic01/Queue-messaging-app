package com.example.qmsapp.data

data class Ticket(val tid:Int,val dateTime:String,var label:String?,
                  val dateTimeForService:String?,val cardNo:String,
                  val email:String,val fkLocId:Int?,var locationName:String?,
                  var serviceName:String?,var serviceId:Int?)
