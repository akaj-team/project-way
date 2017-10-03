package vn.asiantech.way.models

/**
* Created by at-hoavo on 29/09/2017.
*/
data class Arrived(var segments:MutableList<Segment>?=null,var time:Long=0,var distance:Double=0.0,var averageSpeed:Double=0.0)
