package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.lang.StringBuilder

data class Profile(
    val firstName:String,
    val lastName:String,
    val about:String,
    var repository:String,
    val rating:Int = 0,
    val respect:Int = 0
){
    var nickName:String = "John Doe"
        get() {
          return Utils.transliteration("$firstName $lastName", "_").trimStart('_').trimEnd('_')
        }

    val rank:String = "Junior Android Developer"
    fun toMap():Map<String,Any> = mapOf(
        "nickname" to nickName,
        "rank" to rank,
        "firstName" to firstName,
        "lastName" to lastName,
        "about" to about,
        "repository" to repository,
        "rating" to rating,
        "respect" to respect
    )

}