package com.example.qmsapp.data

import java.sql.Time

data class ServiceLimitations(val limitationId:Int,val DateTime:String="",
                              val countLimit:Int,val timeLimit:String,
                              val appointmentsAllowed:Int,
                              val appointmentTimeslot:String,
                              val appointmentsLimitPerTimeslot:Int,
                              val appointmentsLimitTotal:Int,
                              val appointmentsAvailabeFrom:String
                              ,val appointmentsAvailabeUntil:String,
                              val appointmentPause1:String,
                              val appointmentPause2:String)
