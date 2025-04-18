package com.example.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MateriaDto(
    @Json(name = "name")
    val id: String,
    @Json(name = "fields")
    val fields: Fields
) {

    @JsonClass(generateAdapter = true)
    data class Fields(
        @Json(name = "elementosCompletados")
        val elemComp: FirestoreInt,
        @Json(name = "elemEvaluados")
        val elemEvaluados: FirestoreInt,
        @Json(name = "image")
        val image: FirestoreString,
        @Json(name = "recTotales")
        val recTotales: FirestoreInt,
        @Json(name = "recTomados")
        val recTomados: FirestoreInt,
        @Json(name = "description")
        val description: FirestoreString,
        @Json(name = "elemTotal")
        val elemTotal: FirestoreInt,
        @Json(name = "teacher")
        val teacher: FirestoreReference
    )
}


data class FirestoreInt(@Json(name = "integerValue") val value: Int)
data class FirestoreString(@Json(name = "stringValue") val value: String)
data class FirestoreReference(@Json(name = "referenceValue") val value: String)