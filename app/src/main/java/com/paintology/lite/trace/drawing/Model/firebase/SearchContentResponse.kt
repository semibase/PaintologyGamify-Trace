package com.paintology.lite.trace.drawing.Model.firebase

import com.paintology.lite.trace.drawing.Model.CommunityPost


class SearchContentResponse (
    var community_posts: List<CommunityPost> = listOf(),
    var tutorials: List<Post> = listOf(),
    var posts: List<Post> = listOf()
)
