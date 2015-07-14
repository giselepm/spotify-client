package com.spotifyClient

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Artist)
class ArtistSpec extends Specification {

    def setup() {
        mockForConstraintsTests(Artist)
    }

    def cleanup() {
    }

    def "name cannot be null"() {
        given:
        def artist = new Artist()

        when:
        artist.save()

        then:
        artist.hasErrors()
        "nullable" == artist.errors["name"]
    }

    def "idSpotify cannot be null"() {
        given:
        def artist = new Artist()

        when:
        artist.save()

        then:
        artist.hasErrors()
        "nullable" == artist.errors["idSpotify"]
    }

    def "idSpotify must be unique"() {
        given:
        def artist1 = new Artist(name: "Artist 1", idSpotify: "asdfg").save(flush: true)
        def artist2 = new Artist(name: "Artist 2", idSpotify: "asdfg")

        when:
        artist2.save()

        then:
        artist2.hasErrors()
        "unique" == artist2.errors["idSpotify"]
    }

    def "songs can be null"() {
        given:
        def artist = new Artist()

        when:
        artist.save()

        then:
        !artist.errors["songs"]
    }

    def "albums can be null"() {
        given:
        def artist = new Artist()

        when:
        artist.save()

        then:
        !artist.errors["albums"]
    }

    def "Typical case"() {
        given:
        def artist = new Artist(name: "Artist name", idSpotify: "asdfg")

        when:
        artist.save()

        then:
        !artist.hasErrors()
        Artist.count == 1
        Artist.list().last().name == "Artist name"
        Artist.list().last().idSpotify == "asdfg"
    }
}
