package com.spotifyClient

import com.google.gson.JsonObject
import org.codehaus.groovy.grails.web.json.JSONObject

import static org.springframework.http.HttpStatus.*
import grails.converters.JSON
import grails.test.mixin.*
import spock.lang.*

@TestFor(ArtistController)
@Mock(Artist)
class ArtistControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        params['name'] = 'Artist Name'
        params['idSpotify'] = 'xxfdkjsdiaea'
    }

    void "Test the index action without the name parameter"() {

        when:
        controller.index()

        then:
        response.status == PRECONDITION_FAILED.value
        response.text == '{"error":"Validation failed","url":"http://minha.api/errors/artist"}'
    }

    void "Test the index action with an artist existent in the application"() {
        given:
        populateValidParams(params)
        def artist = new Artist(params).save(flush: true)

        when:
        controller.index('Artist Name')

        then: "The response is correct"
        response.status == OK.value
        response.text == (([artist]) as JSON).toString()
    }

    void "Test the index action with an artist inexistent in the application"() {

        given:
        SpotifyService spotifyServiceMock = Mock()
        def json = '{artists:[{"items":[{"name":"Artist Name","id":"xxfdkjsdiaea"}]}]}'

        and:
        controller.spotifyService = spotifyServiceMock

        when:
        controller.index('Artist Name')

        then: "The response is correct"
        response.status == OK.value
        response.text == '[{"class":"com.spotifyClient.Artist","id":' + Artist.first().id + ',"albums":null,"idSpotify":"xxfdkjsdiaea","name":"Artist Name","songs":null}]'
        1 * spotifyServiceMock.search("Artist Name", "artist") >> JSON.parse(json)
    }

    void "Test the show action without an artist"() {
        when:
        controller.show(null)

        then:
        response.status == NOT_FOUND.value
        response.text == '{"error":"Artist not found","url":"http://minha.api/errors/artist"}'
    }

    void "Test the show action returns the artist received"() {
        given:
        populateValidParams(params)
        def artist = new Artist(params).save(flush: true)

        when:
        controller.show(artist.getId() as Integer)

        then:
        response.status == OK.value
        response.text == (artist as JSON).toString()

    }

}