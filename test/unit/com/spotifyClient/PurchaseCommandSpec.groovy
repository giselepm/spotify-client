package com.spotifyClient

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestMixin(ControllerUnitTestMixin)
class PurchaseCommandSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "user cannot be null"() {
        given:
        def purchase = new PurchaseCommand()

        when:
        purchase.validate()

        then:
        purchase.hasErrors()
        purchase.errors["user"].codes.contains("purchaseCommand.user.nullable")
    }

    void "user cannot be smaller than 6 chars"() {
        given:
        def purchase = new PurchaseCommand(user: "login", song: 1)

        when:
        purchase.validate()

        then:
        purchase.hasErrors()
        purchase.errors["user"].codes.contains("purchaseCommand.user.minSize.error")
    }

    void "song cannot be null"() {
        given:
        def purchase = new PurchaseCommand()

        when:
        purchase.validate()

        then:
        purchase.hasErrors()
        purchase.errors["song"].codes.contains("purchaseCommand.song.nullable")
    }

    def "Typical case"() {
        given:
        def purchase = new PurchaseCommand()
        purchase.user = "login1"
        purchase.song = 1

        when:
        purchase.validate()

        then:
        !purchase.hasErrors()
    }
}
