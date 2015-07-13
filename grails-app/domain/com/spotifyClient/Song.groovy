package com.spotifyClient

class Song {

    String name
    String idSpotify
    Album album

    static belongsTo = [Artist]

    static hasMany = [artists:Artist]

    static constraints = {
        name blank: false, nullable: false
        idSpotify blank: false, nullable: false, unique: true
//        artists nullable: false
//        album nullable: true
    }
}
