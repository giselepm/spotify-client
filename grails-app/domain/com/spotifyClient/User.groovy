package com.spotifyClient

class User {

    String login

    static hasMany = [songs:Song]

    static constraints = {
        login blank: false, minSize: 6, unique: true
    }
}
