package vn.asiantech.way.data.model

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 04/12/2017.
 */
data class ResultDistance(val destination_addresses: List<String>,
                          val origin_addresses: List<String>,
                          val rows: List<Rows>)

/**
 *  List result ETA
 */
data class Rows(val elements: List<Elements>)

/**
 *  ETA distance and duration return from Google Api.
 */
data class Elements(val distance: ElementInfo, val duration: ElementInfo)

/**
 *  Element of class Elements.
 */
data class ElementInfo(val text: String, val value: Int)
