package com.spotifyClient

import grails.validation.Validateable

@Validateable
class PurchaseCommand {
    String user
    Long song

    static constraints = {
        user nullable: false, blank: false, minSize: 6
        song nullable: false
    }
}
