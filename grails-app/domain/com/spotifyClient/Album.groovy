package com.spotifyClient

class Album {

    String name
    String idSpotify

    static belongsTo = [Artist]

    static hasMany = [songs:Song, artists:Artist]

    static constraints = {
        name blank: false, nullable: false
        idSpotify blank: false, nullable: false, unique: true
       // artists nullable: false
    }

}
