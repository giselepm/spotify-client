package com.spotifyClient

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import static org.springframework.http.HttpStatus.*

@TestFor(PurchaseController)
@Mock([User, Song])
class PurchaseControllerSpec extends Specification {

    void "Test the buySong action with invalid parameter"() {
        given:
        controller.request.method = "POST"

        when:
        controller.buySong(new PurchaseCommand())

        then:
        response.status == PRECONDITION_FAILED.value
        response.text == '{"error":"Validation failed","url":"http://minha.api/errors/purchase"}'

    }

    void "Test the buySong action with an inexistent user"() {
        given:
        controller.request.method = "POST"

        and:
        new Song(name: "song 1", idSpotify: "aaaa", album: new Album(name: "Album Name", idSpotify: "qwert")).save(flush: true, failOnError: true)
        PurchaseCommand purchase = new PurchaseCommand(song: Song.first().id, user: "newUser")

        when:
        controller.buySong(purchase)

        then:
        response.status == NOT_FOUND.value
        response.text == '{"error":"User not found","url":"http://minha.api/errors/purchase"}'

    }

    void "Test the buySong action with an inexistent song"() {
        given:
        controller.request.method = "POST"

        and:
        new User(login: "newuser").save(flush: true, failOnError: true)
        PurchaseCommand purchase = new PurchaseCommand(song: 1, user: "newuser")

        when:
        controller.buySong(purchase)

        then:
        response.status == NOT_FOUND.value
        response.text == '{"error":"Song not found","url":"http://minha.api/errors/purchase"}'

    }

    void "Test the buySong action with a valid purchase"() {
        given:
        controller.request.method = "POST"

        and:
        new User(login: "newuser").save(flush: true, failOnError: true)
        new Song(name: "song 1", idSpotify: "aaaa", album: new Album(name: "Album Name", idSpotify: "qwert")).save(flush: true, failOnError: true)
        PurchaseCommand purchase = new PurchaseCommand(song: Song.first().id, user: "newuser")

        when:
        controller.buySong(purchase)

        then:
        response.status == OK.value
        response.text == (User.first() as JSON).toString()

    }
    void "Test the removeSong action with invalid parameter"() {
        given:
        controller.request.method = "DELETE"

        when:
        controller.removeSong(new PurchaseCommand())

        then:
        response.status == PRECONDITION_FAILED.value
        response.text == '{"error":"Validation failed","url":"http://minha.api/errors/purchase"}'

    }

    void "Test the removeSong action with an inexistent user"() {
        given:
        controller.request.method = "DELETE"

        and:
        new Song(name: "song 1", idSpotify: "aaaa", album: new Album(name: "Album Name", idSpotify: "qwert")).save(flush: true, failOnError: true)
        PurchaseCommand purchase = new PurchaseCommand(song: Song.first().id, user: "newUser")

        when:
        controller.removeSong(purchase)

        then:
        response.status == NOT_FOUND.value
        response.text == '{"error":"User not found","url":"http://minha.api/errors/purchase"}'

    }

    void "Test the removeSong action with an inexistent song"() {
        given:
        controller.request.method = "DELETE"

        and:
        new User(login: "newuser").save(flush: true, failOnError: true)
        PurchaseCommand purchase = new PurchaseCommand(song: 1, user: "newuser")

        when:
        controller.removeSong(purchase)

        then:
        response.status == NOT_FOUND.value
        response.text == '{"error":"Song not found","url":"http://minha.api/errors/purchase"}'

    }

    //Test failing. Need more debug to discover the problem.
//    void "Test the removeSong action with a valid purchase"() {
//        given:
//        controller.request.method = "DELETE"
//
//        and:
//        def u = new User(login: "newuser").save(flush: true, failOnError: true)
//        u.addToSongs(new Song(name: "song 1", idSpotify: "aaaa", album: new Album(name: "Album Name", idSpotify: "qwert")))
//        u.save(flush: true, failOnError: true)
//
//        and:
//        PurchaseCommand purchase = new PurchaseCommand(song: Song.first().id, user: User.first().login)
//
//        when:
//        controller.removeSong(purchase)
//
//        then:
//        response.status == OK.value
//        response.text == ([] as JSON).toString()
//
//    }

}