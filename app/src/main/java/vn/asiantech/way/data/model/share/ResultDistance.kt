package vn.asiantech.way.data.model.share

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 15/11/2017.
 */
data class ResultDistance(val destination_addresses: List<String>,
                          val origin_addresses: List<String>,
                          val rows: List<Rows>, val status: String)

data class Rows(val elements: List<Elements>)

data class Elements(val distance: InfoDistance, val duration: InfoDistance, val status: String)

data class InfoDistance(val text: String, val value: Int)