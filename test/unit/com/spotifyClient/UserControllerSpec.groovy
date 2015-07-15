package com.spotifyClient

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import static org.springframework.http.HttpStatus.*

@TestFor(UserController)
@Mock(User)
class UserControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        params['login'] = 'newlogin'
    }

    void "Test the index action returns the correct model"() {
        when:
        controller.index()

        then:
        response.status == OK.value
        response.text == ([] as JSON).toString()

    }

    void "Test the show action without an user"() {
        when:
        controller.show(null)

        then:
        response.status == NOT_FOUND.value
        response.text == '{"error":"User not found","url":"http://minha.api/errors/user"}'

    }

    void "Test the show action returns the user received"() {
        given:
        def user = new User(login: "newlogin")

        when:
        controller.show(user)

        then:
        response.status == OK.value
        response.text == (user as JSON).toString()

    }

    void "Test the save action without an user as parameter"() {
        given:
        controller.request.method = "POST"

        when:
        controller.save(null)

        then:
        response.status == NOT_FOUND.value
        response.text == '{"error":"User not found","url":"http://minha.api/errors/user"}'
    }

    void "Test the save action with an invalid user as parameter"() {
        given:
        controller.request.method = "POST"
        def user = new User()

        when:
        controller.save(user)

        then:
        response.status == PRECONDITION_FAILED.value
        response.text == '{"error":"Validation failed","url":"http://minha.api/errors/user"}'
    }

    void "Test the save action correctly persists an instance"() {
        given:
        controller.request.method = "POST"
        response.reset()
        populateValidParams(params)
        def user = new User(params)

        when:
        controller.save(user)

        then:
        response.status == CREATED.value
        response.text == (user as JSON).toString()
    }

    void "Test the update action without an user as parameter"() {
        given:
        controller.request.method = "PUT"

        when:
        controller.update(null)

        then:
        response.status == NOT_FOUND.value
        response.text == '{"error":"User not found","url":"http://minha.api/errors/user"}'
    }

    void "Test the update action with an invalid user as parameter"() {
        given:
        controller.request.method = "PUT"
        def user = new User()

        when:
        controller.update(user)

        then:
        response.status == PRECONDITION_FAILED.value
        response.text == '{"error":"Validation failed","url":"http://minha.api/errors/user"}'
    }

    void "Test the update action correctly updates an instance"() {
        given:
        controller.request.method = "PUT"
        response.reset()
        populateValidParams(params)
        def user = new User(params).save(flush: true)

        when:
        controller.update(user)

        then:
        response.status == OK.value
        response.text == (user as JSON).toString()
    }

    void "Test the delete action without an user as parameter"() {
        given:
        controller.request.method = "DELETE"

        when:
        controller.delete(null)

        then:
        response.status == NOT_FOUND.value
        response.text == '{"error":"User not found","url":"http://minha.api/errors/user"}'
    }

    void "Test the delete action correctly deletes an instance"() {
        given:
        controller.request.method = "DELETE"
        response.reset()
        populateValidParams(params)
        def user = new User(params).save(flush: true)

        when:
        controller.delete(user)

        then:
        User.count() == 0
        response.status == NO_CONTENT.value
    }
}