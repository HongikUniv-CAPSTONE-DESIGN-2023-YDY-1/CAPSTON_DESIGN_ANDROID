package com.example.capstonedesign.response



import com.google.gson.annotations.SerializedName

data class PlaceInfo(
    @SerializedName("documents")
    val documents: List<Place>,
    @SerializedName("meta")
    val meta: Meta
)
data class SameName(
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("region")
    val region: List<Any>,
    @SerializedName("selected_region")
    val selectedRegion: String
)
data class Meta(
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("same_name")
    val sameName: SameName,
    @SerializedName("total_count")
    val totalCount: Int
)
data class Place(
    val address_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val category_name: String,
    val distance: String,
    val id: String,
    val phone: String,
    val place_name: String,
    val place_url: String,
    val road_address_name: String,
    val x: String,
    val y: String
)