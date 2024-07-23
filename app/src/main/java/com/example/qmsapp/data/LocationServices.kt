package com.example.qmsapp.data

data class LocationServices(val serviceId:Int,
                            val serviceStatus:String="",
                            val serviceName:String="",
                            val counterServices:ArrayList<Counter>?,
                            val limitations: ArrayList<ServiceLimitations>)
{
    //var limitations:ServiceLimitations?=null
}
