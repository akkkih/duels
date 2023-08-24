package com.akkih.duels.data

import gg.flyte.twilight.data.MongoDB

object Database {
    val PROFILES = MongoDB.collection("profiles")
}