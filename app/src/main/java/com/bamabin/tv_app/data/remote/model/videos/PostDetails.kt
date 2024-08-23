package com.bamabin.tv_app.data.remote.model.videos

import com.bamabin.tv_app.data.local.TempDB
import com.google.gson.annotations.SerializedName
import kotlin.math.floor

data class PostDetails(
    val id: Int,
    val title: String,
    @SerializedName("fa_title")
    val faTitle: String,
    @SerializedName("bg_thumbnail")
    val bgThumbnail: String,
    @SerializedName("imdb_rate")
    val imdbRate: String,
    @SerializedName("imdb_votes")
    val imdbVoteCount: Int,
    @SerializedName("has_dubbed")
    val hasDubbed: Boolean,
    @SerializedName("has_subtitle")
    val hasSubtitle: Boolean,
    val years: List<Int>,
    val summary: String,
    @SerializedName("post_type")
    val postType: String,
    @SerializedName("movies_dlbox")
    val movieDownloadBox: DownloadBox? = null,
    @SerializedName("series_dlbox")
    val seasons: List<Season>? = null,
    val status: String,
    @SerializedName("age_rate")
    val ageRate: String,
    @SerializedName("time")
    private val time: Int,
    @SerializedName("genres_id")
    private val genresId: List<Int>,
    private val type: String,
){
    val genres:List<Genre>
        get() {
            val g = mutableListOf<Genre>()
            TempDB.genres.forEach {
                if (genresId.contains(it.id))
                    g.add(it)
            }
            return g
        }

    val isSeries: Boolean
        get() = type == "series"

    fun getTime(): String {
        val s = time / 60
        if (s <= 60) return "$s دقیقه"

        val h = s / 60
        val m = s % 60
        return "$h ساعت و $m دقیقه"
    }

    fun getVoteCount(): String{
        if (imdbVoteCount < 1000) return imdbVoteCount.toString()

        return floor(imdbVoteCount.toDouble() / 1000).toString() + "k"
    }
}

data class Season(
    val name: String,
    val episodes: List<EpisodeInfo>
)

data class EpisodeInfo(
    val name: String,
    val items: DownloadBox
)

data class DownloadBox(
    val subtitle: List<ItemInfo> = emptyList(),
    val dubbed: List<ItemInfo> = emptyList(),
    val native: List<ItemInfo> = emptyList(),
    val screen: List<ItemInfo> = emptyList(),
) {
    fun getAllItemsName(): List<String> {
        val items = mutableListOf<String>()
        items.addAll(dubbed.map { "دوبله فارسی - کیفیت: " + it.quality + " - انکودر: " + it.encoder })
        items.addAll(subtitle.map { "زیرنویس فارسی - کیفیت: " + it.quality + " - انکودر: " + it.encoder })
        items.addAll(native.map { "نسخه اصلی - کیفیت: " + it.quality + " - انکودر: " + it.encoder })
        items.addAll(screen.map { "نسخه پرده - کیفیت: " + it.quality + " - انکودر: " + it.encoder })
        return items
    }

    fun getAllItemsLink(): List<String> {
        val items = mutableListOf<String>()
        items.addAll(dubbed.map { it.link })
        items.addAll(subtitle.map { it.link })
        items.addAll(native.map { it.link })
        items.addAll(screen.map { it.link })
        return items
    }

    fun getDefaultItemIndex(): Int {
        val items = mutableListOf<String>()
        items.addAll(dubbed.map { it.mainQuality })
        items.addAll(subtitle.map { it.mainQuality })
        items.addAll(native.map { it.mainQuality })
        items.addAll(screen.map { it.mainQuality })

        val qualities = listOf("720", "1080", "480")
        for (q in qualities){
            for (i in items.indices){
                if (items[i].contains(q) && items[i].contains("x264"))
                    return i
            }
        }

        return 0
    }

    fun getItemInfo(index: Int): List<String> {
        if (index < dubbed.size){
            return listOf("dubbed", dubbed[index].quality, dubbed[index].qualityCode, dubbed[index].encoder)
        }

        if (index < dubbed.size + subtitle.size) {
            val i = index - dubbed.size
            return listOf("subtitle", subtitle[i].quality, subtitle[i].qualityCode, subtitle[i].encoder)
        }

        if (index < dubbed.size + subtitle.size + native.size) {
            val i = index - dubbed.size - subtitle.size
            return listOf("native", native[i].quality, native[i].qualityCode, native[i].encoder)
        }

        val i = index - dubbed.size - subtitle.size - native.size
        return listOf("screen", screen[i].quality, screen[i].qualityCode, screen[i].encoder)
    }

    fun getSimilarIndex(data: List<String>): Int {
        val types = getSortedType(data[0])
        val items = mutableListOf<ItemInfo>()
        var type = ""
        var itemIndex = -1

        for (t in types){
            if (t == "dubbed" && dubbed.isNotEmpty()) {
                items.addAll(dubbed)
                type = "dubbed"
                break
            }
            if (t == "subtitle" && subtitle.isNotEmpty()) {
                items.addAll(subtitle)
                type = "subtitle"
                break
            }
            if (t == "native" && native.isNotEmpty()) {
                items.addAll(native)
                type = "native"
                break
            }
            if (t == "screen" && screen.isNotEmpty()) {
                items.addAll(screen)
                type = "screen"
                break
            }
        }

        if (items.isEmpty()) return itemIndex

        itemIndex = findSimilar(items, data[1], data[2], data[3])
        if (itemIndex == -1) itemIndex = findSimilar(items, data[1], data[2])
        if (itemIndex == -1) itemIndex = findSimilar(items, data[1])
        if (itemIndex == -1) itemIndex = 0

        if (type == "screen") return dubbed.size + subtitle.size + native.size + itemIndex
        if (type == "native") return dubbed.size + subtitle.size + itemIndex
        if (type == "subtitle") return dubbed.size + itemIndex
        return itemIndex
    }

    private fun getSortedType(type: String): List<String> {
        val availableTypes = mutableListOf("dubbed", "subtitle", "native", "screen")
        val types = mutableListOf(type)
        availableTypes.remove(type)
        types.addAll(availableTypes)
        return types
    }

    private fun findSimilar(items: List<ItemInfo>, quality: String, qualityCode: String? = null, encoder: String? = null): Int {
        for (i in items.indices) {
            if (!encoder.isNullOrEmpty()) {
                if (items[i].quality == quality && items[i].qualityCode == qualityCode && items[i].encoder == encoder) return i
            }
            if (!qualityCode.isNullOrEmpty()) {
                if (items[i].quality == quality && items[i].qualityCode == qualityCode) return i
            }
            if (items[i].quality == quality) return i
        }

        return -1
    }
}

data class ItemInfo(
    val link: String,
    val quality: String,
    @SerializedName("main_quality")
    val mainQuality: String,
    @SerializedName("quality_code")
    val qualityCode: String,
    val encoder: String,
)