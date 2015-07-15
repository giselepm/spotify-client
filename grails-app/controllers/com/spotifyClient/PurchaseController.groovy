package com.spotifyClient

import grails.converters.JSON
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED


class PurchaseController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [buySong: "POST", removeSong: "DELETE"]

    @Transactional
    def buySong(PurchaseCommand purchase) {

        if(!purchase.validate()){
            response.status = PRECONDITION_FAILED.value()
            render ([error: "Validation failed", url: "http://minha.api/errors/purchase"] as JSON)
            return
        }

        def user = User.findByLogin(purchase.user)

        if (!user) {
            response.status = NOT_FOUND.value()
            render ([error: "User not found", url: "http://minha.api/errors/purchase"] as JSON)
            return
        }

        def song = Song.get(purchase.song)

        if (!song) {
            response.status = NOT_FOUND.value()
            render ([error: "Song not found", url: "http://minha.api/errors/purchase"] as JSON)
            return
        }

        user.addToSongs(song)
        user.save flush:true
        respond user, [status: OK]
    }

    @Transactional
    def removeSong(PurchaseCommand purchase) {

        if(!purchase.validate()){
            response.status = PRECONDITION_FAILED.value()
            render ([error: "Validation failed", url: "http://minha.api/errors/purchase"] as JSON)
            return
        }

        User user = User.findByLogin(purchase.user)

        if (!user) {
            response.status = NOT_FOUND.value()
            render ([error: "User not found", url: "http://minha.api/errors/purchase"] as JSON)
            return
        }

        Song song = Song.get(purchase.song)

        if (!song || !user.songs.contains(song)) {
            response.status = NOT_FOUND.value()
            render ([error: "Song not found", url: "http://minha.api/errors/purchase"] as JSON)
            return
        }

        user.removeFromSongs(song)
        user.save flush:true
        respond user, [status: OK]

    }
}
