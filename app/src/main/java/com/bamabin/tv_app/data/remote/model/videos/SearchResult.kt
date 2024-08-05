package com.bamabin.tv_app.data.remote.model.videos

data class SearchResult(
    val series: List<Post> = emptyList(),
    val movies: List<Post> = emptyList(),
    val animations: List<Post> = emptyList(),
    val anime: List<Post> = emptyList(),
)
