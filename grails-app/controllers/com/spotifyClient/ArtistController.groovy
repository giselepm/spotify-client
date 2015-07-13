package com.spotifyClient

import grails.converters.JSON
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class ArtistController {

    SpotifyService spotifyService
    static responseFormats = ['json', 'xml']
    def type = "artist"

    @Transactional
    def index(String name) {

        if(!name){
            response.status = PRECONDITION_FAILED.value()
            render([error: "Validation failed", url: "http://minha.api/errors/${type}"] as JSON)
            return
        }

        def artists = Artist.findAllByNameIlike("%${name}%")
        //TODO: Spotify search is accent insensitive so make this too, for example, if I search for coracao search considering coração also.

        if (!artists) {
            def json = spotifyService.search(name, type)

            json.artists.items.each { artistItem ->
                artists.add(new Artist(name: artistItem.name, idSpotify: artistItem.id))
            }

            Artist.saveAll(artists)
        }


        respond artists, [status: OK]
    }

    def show(Integer id) {
        def artist = Artist.get(id)

        if (!artist) {
            response.status = NOT_FOUND.value()
            render([error: "Artist not found", url: "http://minha.api/errors/${type}"] as JSON)
            return
        }

        respond artist
    }



}
