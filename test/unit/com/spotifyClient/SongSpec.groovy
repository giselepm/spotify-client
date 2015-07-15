package com.spotifyClient

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Song)
@Mock(Album)
class SongSpec extends Specification {

    def setup() {
        mockForConstraintsTests(Song)
//        mockFor(Album)
    }

    def cleanup() {
    }

    def "name cannot be null"() {
        given:
        def song = new Song()

        when:
        song.save()

        then:
        song.hasErrors()
        "nullable" == song.errors["name"]
    }

    def "idSpotify cannot be null"() {
        given:
        def song = new Song()

        when:
        song.save()

        then:
        song.hasErrors()
        "nullable" == song.errors["idSpotify"]
    }

    def "idSpotify must be unique"() {
        given:
        def song1 = new Song(name: "Song 1", idSpotify: "asdfg", album: new Album(name: "Album Name", idSpotify: "qwert")).save(flush: true)
        def song2 = new Song(name: "Song 1", idSpotify: "asdfg")

        when:
        song2.save()

        then:
        song2.hasErrors()
        "unique" == song2.errors["idSpotify"]
    }

    def "Typical case"() {
        given:
        def song = new Song()
        song.name = "Song name"
        song.idSpotify = "asdfg"
        song.album = new Album(name: "Album Name", idSpotify: "qwert")

        when:
        song.save()

        then:
        !song.hasErrors()
        Song.count == 1
        Song.list().last().name == "Song name"
        Song.list().last().idSpotify == "asdfg"
    }
}
