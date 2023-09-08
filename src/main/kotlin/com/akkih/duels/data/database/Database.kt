package com.akkih.duels.data.database

import com.akkih.duels.data.profile.Profile
import java.util.*

interface Database {
    fun create(connectionURI: String)

    fun save(profile: Profile)

    fun from(uuid: UUID): Profile
}