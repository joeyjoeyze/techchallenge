# 500px Popular Photos
Retrofit to consume JSON API.

Jackson to handle JSON to POJO and reverse.

EventBus to send out API response to UI fragments for processing and general Activity-Fragment communication.

MainActivity holds RecyclerFragment. MainActivity sends out API request for popular photos. API response is handled in the RecyclerFragment. RecyclerFragment displays each response photo in a vertical scrolling gallery. MainActivity should support pull down to refresh for new popular photos.

Clicking any photo in the RecyclerFragment should invoke the FullscreenActivity. The FullscreenActivity requests and displays the fullscreen image. On the bottom of the screen there is a card displaying the title of the photo. Pulling up on the card reveals more photo information.

Clicking on TitleBar scrolls the gallery to the very top.

Glide by default caches images on disk.

Gallery displays thumbnails in their native aspect ratio.
