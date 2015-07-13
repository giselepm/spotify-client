class UrlMappings {

	static mappings = {
//        "/$controller/$action?/$id?(.$format)?"{
//            constraints {
//                // apply constraints here
//            }
//        }

        "/artists"(resources: "artist")
        "/songs"(resources: "song")
        "/albums"(resources: "album")
        "/users"(resources:"user")

        "/purchases"(controller: 'purchase', action: [POST: 'buySong', DELETE: 'removeSong'])

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
