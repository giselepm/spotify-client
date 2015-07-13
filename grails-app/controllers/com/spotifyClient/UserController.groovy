package com.spotifyClient

import grails.converters.JSON

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class UserController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", update: "PATCH", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), [status: OK]
    }

    def show(User userInstance) { // it is necessary to include this class in the generated restful controller
        if (userInstance == null) {
            response.status = NOT_FOUND.value()
            render([error: "User not found", url: "http://minha.api/errors/user"] as JSON)
            return
        }
        respond userInstance
    }

    @Transactional
    def save(User userInstance) {
        if (userInstance == null) {
            response.status = NOT_FOUND.value()
            render([error: "User not found", url: "http://minha.api/errors/user"] as JSON)
            return
        }

        userInstance.validate()
        if (userInstance.hasErrors()|| userInstance.songs) {
            response.status = PRECONDITION_FAILED.value()
            render([error: "Validation failed", url: "http://minha.api/errors/user"] as JSON)
            return
        }

        userInstance.save flush:true
        respond userInstance, [status: CREATED]
    }

    @Transactional
    def update(User userInstance) {
        if (userInstance == null) {
            response.status = NOT_FOUND.value()
            render([error: "User not found", url: "http://minha.api/errors/user"] as JSON)
            return
        }

        userInstance.validate()
        if (userInstance.hasErrors()) {
            response.status = PRECONDITION_FAILED.value()
            render([error: "Validation failed", url: "http://minha.api/errors/user"] as JSON)
            return
        }

        userInstance.save flush:true
        respond userInstance, [status: OK]

        //TODO: Don't allow user to change his songs
    }

    @Transactional
    def delete(User userInstance) {

        if (userInstance == null) {
            response.status = NOT_FOUND.value()
            render([error: "User not found", url: "http://minha.api/errors/user"] as JSON)
            return
        }

        userInstance.delete flush:true
        render status: NO_CONTENT
    }
}

