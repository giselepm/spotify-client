package com.spotifyClient

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {

    def setup() {
        mockForConstraintsTests(User)
    }

    def cleanup() {
    }

    void "login cannot be null"() {
        given:
        def user = new User()

        when:
        user.save()

        then:
        user.hasErrors()
        "nullable" == user.errors["login"]
    }

    void "login cannot be smaller than 6 chars"() {
        given:
        def user = new User(login: "login")

        when:
        user.save()

        then:
        user.hasErrors()
        "minSize" == user.errors["login"]
    }

    void "login must be unique"() {
        given:
        def user1 = new User(login: "login1").save(flush: true)
        def user2 = new User(login: "login1")

        when:
        user2.save()

        then:
        user2.hasErrors()
        "unique" == user2.errors["login"]
    }

    def "Typical case"() {
        given:
        def user = new User()
        user.login = "login1"

        when:
        user.save()

        then:
        !user.hasErrors()
        User.count == 1
        User.list().last().login == "login1"
    }
}
