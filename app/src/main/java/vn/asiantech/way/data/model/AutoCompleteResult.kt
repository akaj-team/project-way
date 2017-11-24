package vn.asiantech.way.data.model

import com.google.gson.annotations.SerializedName

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 07/11/2017
 */
data class AutoCompleteResult(var predictions: List<AutoCompleteLocation>)

/**
 *  Location return from google map autocomplete api.
 */
data class AutoCompleteLocation(var description: String,
                                @SerializedName("structured_formatting") var structuredFormatting: StructuredFormatting,
                                var id: String, @SerializedName("place_id") var placeId: String)

/**
 *  Structured formatting from google map autocomplete api.
 */
data class StructuredFormatting(@SerializedName("main_text") var mainText: String)
