package com.spotifyClient

import grails.converters.JSON
import grails.transaction.Transactional
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.codehaus.groovy.grails.web.json.JSONObject

@Transactional
class SpotifyService {

    def JSONObject search(String name, String type) {
        String url = "https://api.spotify.com/v1/search?q=${type}:%22${name.replace(' ', '%20')}%22&type=${type}"

        return connectSpotify(url)

    }

    def JSONObject getItem(String idSpotify, String type) {
        String url = "https://api.spotify.com/v1/${type}s/${idSpotify}"

        return connectSpotify(url)

    }

    def JSONObject searchSong(String song, String album, String artist) {

        String songName = song? "%22${song.replace(' ', '%20')}%22" : "%22%22"
        String albumName = album? "%22${album.replace(' ', '%20')}%22" : "%22%22"
        String artistName = artist? "%22${artist.replace(' ', '%20')}%22" : "%22%22"

        String query = "track:${songName}+album:${albumName}+artist:${artistName}"

        String url = "https://api.spotify.com/v1/search?q=${query}&type=track"

        return connectSpotify(url)

    }


    def JSONObject connectSpotify(String url) {

        HttpClient client = HttpClientBuilder.create().build()
        HttpGet getRequest = new HttpGet(url)

        HttpResponse response = client.execute(getRequest)

        HttpEntity responseEntity = response.entity
        String jsonAsString = EntityUtils.toString(responseEntity)
        return JSON.parse(jsonAsString)

    }

    //TODO: Catch the exception: 2015-07-06 10:34:56,284 [http-bio-8080-exec-10] ERROR errors.GrailsExceptionResolver  - UnknownHostException occurred when processing request: [GET] /spotifyClient/artists/ - parameters:
//      name: Beth Carvalho
//      Maybe use: REQUEST_TIMEOUT

}
