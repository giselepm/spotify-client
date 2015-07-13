package com.spotifyClient

class Artist {

    String name
    String idSpotify

    static hasMany = [songs:Song, albums:Album]

    static constraints = {
        name blank: false, nullable: false
        idSpotify blank: false, nullable: false, unique: true
        songs nullable: true
        albums nullable: true
    }
}
