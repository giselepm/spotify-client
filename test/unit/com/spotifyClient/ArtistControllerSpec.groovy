package com.spotifyClient



import static org.springframework.http.HttpStatus.*
import grails.converters.JSON
import grails.test.mixin.*
import spock.lang.*

@TestFor(ArtistController)
@Mock(Artist)
class ArtistControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        params['name'] = 'someValidName'
        params['idSpotify'] = 'xxfdkjsdiaea'
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
        controller.index('Luiza')

        then: "The response is correct"
        response.status == OK.value
        response.text == ([] as JSON).toString()
    }

    void "Test the save action cannot be called"() {

        when: "The save action is executed"
        def artist = new Artist(name: 'Lua')
        controller.save(artist)

        then: "The response status is METHOD_NOT_ALLOWED"
        response.status == METHOD_NOT_ALLOWED.value

    }

    void "Test the update action cannot be called"() {

        when: "The update action is executed"
        def artist = new Artist(id: 1, name: 'Lua')
        controller.update(artist)

        then: "The response status is METHOD_NOT_ALLOWED"
        response.status == METHOD_NOT_ALLOWED.value

    }

    void "Test the delete action cannot be called"() {

        when: "The save action is executed"
        def artist = new Artist(id:1, name: 'Lua')
        controller.delete(artist)

        then: "The response status is METHOD_NOT_ALLOWED"
        response.status == METHOD_NOT_ALLOWED.value

    }

}