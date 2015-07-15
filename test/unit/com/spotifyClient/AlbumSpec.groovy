package com.spotifyClient

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Album)
class AlbumSpec extends Specification {

    def setup() {
        mockForConstraintsTests(Album)
    }

    def cleanup() {
    }

    def "Name cannot be null"() {
        given:
        def album = new Album()

        when:
        album.save()

        then:
        album.hasErrors()
        "nullable" == album.errors["name"]

    }

    def "idSpotify cannot be null"() {
        given:
        def album = new Album()

        when:
        album.save()

        then:
        album.hasErrors()
        "nullable" == album.errors["idSpotify"]

    }

    def "idSpotify must be unique"() {
        given:
        def album1 = new Album(name: "test1", idSpotify: "abcdef").save(flush: true)
        def album2 = new Album(name: "test2", idSpotify: "abcdef")

        when:
        album2.save()

        then:
        album2.hasErrors()
        "unique" == album2.errors["idSpotify"]

    }

    def "typical case"(){

        given:
        def album = new Album()

        when:
        album.name = "Album Name"
        album.idSpotify = "abcdef"
        album.save()

        then:
        !album.hasErrors()
        Album.count() == 1
        Album.list().last().name == "Album Name"
        Album.list().last().idSpotify == "abcdef"
    }
}
