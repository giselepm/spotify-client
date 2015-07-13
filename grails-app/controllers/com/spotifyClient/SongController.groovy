package com.spotifyClient

import grails.converters.JSON

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SongController {

    SpotifyService spotifyService
    static responseFormats = ['json', 'xml']

    @Transactional
    def index(String songName, String albumName, String artistName) {

        if (!songName && !albumName && !artistName) {
            response.status = PRECONDITION_FAILED.value()
            render([error: "Validation failed", url: "http://minha.api/errors/song"] as JSON)

        }
        else {
            def songs = Song.createCriteria().list {
                createAlias("album", "alb")
                createAlias("artists", "art")
                if (songName){
                    ilike("name", "%${songName}%")
                }
                if (albumName) {
                    ilike("alb.name", "%${albumName}%")
                }
                if (artistName) {
                    ilike("art.name", "%${artistName}%")
                }
                //TODO: Spotify search is accent insensitive so make this too, for example, if I search for coracao search considering coração also.

            }

            if (!songs) {

                def json = spotifyService.searchSong(songName, albumName, artistName)
                def artists = []
                def artist
                def album
                def song

                json.tracks.items.each { songItem ->

                    album = Album.findWhere(idSpotify: songItem.album.id)
                    if (!album) {
                        album = new Album(name: songItem.album.name, idSpotify: songItem.album.id)
                        album.save flush:true
                    }

                    song = new Song(name: songItem.name, idSpotify: songItem.id, album: album)
                    songs.add(song)

                    songItem.artists.each { artistItem ->
                        artist = Artist.findWhere(idSpotify: artistItem.id)
                        if(!artist) {
                            artist = new Artist(name: artistItem.name, idSpotify: artistItem.id)
                            artist.save flush:true
                        }
                        artist.addToSongs(song)
                        artist.addToAlbums(album)
                        artists.add(artist)
                        artist = null

                    }

                    song = null
                    album = null
                }

                Song.saveAll(songs)
            }

            respond songs, [status: OK]
        }

        //TODO: Deal with Spotify pagination
        //TODO: Deal with the problem, when you first search for a song and then for the artist of one of the songs returned
    }

    def show(Integer id) {

        def songs = Song.get(id)

        if (!songs) {
            response.status = NOT_FOUND.value()
            render ([error: "song not found", url: "http://minha.api/errors/song"] as JSON)
            return
        }

        respond songs
    }



}
