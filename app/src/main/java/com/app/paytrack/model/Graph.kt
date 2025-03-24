package com.app.paytrack.model

import kotlinx.serialization.Serializable

sealed class Graph {

    @Serializable
    object Root

    @Serializable
    object Auth

    @Serializable
    object Main

}