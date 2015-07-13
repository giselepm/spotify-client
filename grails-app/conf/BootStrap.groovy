import com.spotifyClient.Album
import com.spotifyClient.Artist
import com.spotifyClient.Song
import com.spotifyClient.User
import grails.converters.JSON

class BootStrap {

    def init = { servletContext ->
        if(!User.count()){
            new User(login: "giselepm").save(failOnError: true)
            new User(login: "felipecao").save(failOnError: true)
            new User(login: "mknust").save(failOnError: true)
            new User(login: "livtyler").save(failOnError: true)
        }

        JSON.registerObjectMarshaller(Artist) { Artist artist ->
            return [
                    id: artist.id,
                    name: artist.name,
                    idSpotify: artist.idSpotify //this is here just to help me to test =)
            ]
        }

        JSON.registerObjectMarshaller(Song) { Song song ->
            return [
                    id: song.id,
                    name: song.name,
                    idSpotify: song.idSpotify, //this is here just to help me to test =)
                    artists: song.artists,
                    album: [
                            id: song.album.id,
                            name: song.album.name,
                            idSpotify: song.album.idSpotify //this is here just to help me to test =)
                    ]
            ]
        }

        JSON.registerObjectMarshaller(User) { User user ->
            return [
                    id: user.id,  //this is here just to help me to test =)
                    login: user.login,
                    songs: user.songs
            ]
        }

        JSON.registerObjectMarshaller(Album) { Album album ->
            return [
                    id: album.id,
                    name: album.name,
                    idSpotify: album.idSpotify, //this is here just to help me to test =)
                    artists: album.artists.collect { [id: it.id, name: it.name, idSpotify: it.idSpotify] },
                    songs: album.songs.collect {
                        [id: it.id,
                         name: it.name,
                         idSpotify: it.idSpotify,
                         artists: it.artists.collect { art ->
                             [id: art.id, name: art.name]
                         }]
                    }
            ]
        }

    }
    def destroy = {
    }
}
