package com.spotifyClient

import grails.converters.JSON
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class AlbumController {

    SpotifyService spotifyService
    static responseFormats = ['json', 'xml']
    def type = "album"

    @Transactional
    def index(String name) {

        if(!name){
            response.status = PRECONDITION_FAILED.value()
            render([error: "Validation failed", url: "http://minha.api/errors/${type}"] as JSON)
            return
        }

        def albums = Album.findAllByNameIlike("%${name}%")
        //TODO: Spotify search is accent insensitive so make this too, for example, if I search for coracao search considering coração also.

        if (!albums) {

            def jsonSearchAlbums = spotifyService.search(name, type)
            def jsonGetAlbum
            def album
            def artist
            def song
            def songArtist


            jsonSearchAlbums.albums.items.each { albumItem ->
                album = new Album(name: albumItem.name, idSpotify: albumItem.id)
                album.save()
                jsonGetAlbum = spotifyService.getItem(albumItem.id, type)

                //Artist owner of the Album
                jsonGetAlbum.artists.each { artistItem ->
                    artist = Artist.findWhere(idSpotify: artistItem.id)
                    if(!artist) {
                        artist = new Artist(name: artistItem.name, idSpotify: artistItem.id)
                        artist.save flush:true
                    }
                    artist.addToAlbums(album)
                    artist = null
                }

                //Songs of the album
                jsonGetAlbum.tracks.items.each { songItem ->
                    song = Song.findWhere(idSpotify: songItem.id)
                    if(!song) {
                        song = new Song(name: songItem.name, idSpotify: songItem.id)
                        song.save()

                        // Artists singing the song
                        songItem.artists.each { songArtistItem ->
                            songArtist = Artist.findWhere(idSpotify: songArtistItem.id)
                            if (!songArtist) {
                                songArtist = new Artist(name: songArtistItem.name, idSpotify: songArtistItem.id)
                                songArtist.save()
                            }
                            songArtist.addToSongs(song)

                        }
                    }
                    album.addToSongs(song)
                }

                albums.add(album)
            }

            Album.saveAll(albums)
        }
        respond albums, [status: OK]
    }

    def show(Integer id) {
        def album = Album.get(id)

        if (!album) {
            response.status = NOT_FOUND.value()
            render([error: "Album not found", url: "http://minha.api/errors/${type}"] as JSON)
            return
        }

        respond album
    }
}
