
package com.teleca.jamendo.api;

import org.json.JSONException;

import com.teleca.jamendo.activity.playview.PlayMethod;
import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.Artist;
import com.teleca.jamendo.model.License;
import com.teleca.jamendo.model.PlaylistRemote;
import com.teleca.jamendo.model.Radio;
import com.teleca.jamendo.model.Review;
import com.teleca.jamendo.model.Track;
import com.teleca.jamendo.model.ErrorMsg;


/**
 * 服务器接口
 * Java interface to the jamendo.get2 API ("The Free Music API")<br>
 * <br>
 * <a href="http://developer.jamendo.com/en/wiki/Musiclist2Api">Musiclist2Api</a><br>
 * <br>
 * <b>USAGE:</b><br>
 * Basically, since this service does not require any sort of authorization, just write:<br>
 * <br>
 * <code>
 * JamendoGet2Api service = new JamendoGet2ApiImpl(); 
 * </code><br>
 * <br>
 * <b>NOTE:</b><br> 
 * This library is meant to cover most of the api documented on the wiki. It is
 * not side-by-side copy though. These Java bindings where designed with an Android application
 * in mind, therefore they provide certain optimizations for this platform, e.g. Apache HTTP 
 * Client 4 backend<br>
 * 
 * 
 * @author Lukasz Wisniewski
 */
public interface IServerApi {
	
	public static final String ENCODING_MP3 = "mp31";
	public static final String ENCODING_OGG = "ogg2";

	/**
	 * Retrieve "this week's most popular albums" 
	 * <br><br>
	 * http://api.jamendo.com/get2/id+name+url+image+artist_name/album/jsonpretty/?n=5&order=ratingweek_desc
	 * @return an array of albums
	 * @throws JSONException 
	 * @throws ErrorMsg 
	 */
	Album[] getPopularAlbumsWeek() throws JSONException, ErrorMsg;
	
	/**
	 * Retrieve info on all track from the given album 
	 * <br><br>
	 * http://api.jamendo.com/get2/id+name+duration+url+stream/track/jsonpretty/?album_id=33
	 * @param album an <code>Album</code> instance
	 * @return a <code>Track</code> array from the given album
	 * @throws JSONException
	 * @throws ErrorMsg 
	 */
	Track[] getAlbumTracks(Album album, String encoding) throws JSONException, ErrorMsg;
	
	/**
	 * Search for albums with artist like name
	 * 
	 * @param artistName
	 * @return an array of albums
	 * @throws JSONException
	 * @throws ErrorMsg 
	 */
	Album[] searchForAlbumsByArtist(String artistName) throws JSONException, ErrorMsg;
	
	/**
	 * Search for given artist's albums
	 * 
	 * @param artistName
	 * @return an array of albums
	 * @throws JSONException
	 * @throws ErrorMsg 
	 */
	Album[] searchForAlbumsByArtistName(String artistName) throws JSONException, ErrorMsg;
	
	/**
	 * Search for albums matching the given tag<br>
	 * <br>
	 * http://api.jamendo.com/get2/id+name+url+image+artist_name/album/jsonpretty/?tag_idstr=rock&n=50&order=rating_desc
	 * 
	 * @param tag
	 * @return
	 * @throws JSONException
	 * @throws ErrorMsg 
	 */
	Album[] searchForAlbumsByTag(String tag) throws JSONException, ErrorMsg;
	
	/**
	 * Gets Artist info<br>
	 * <br>
	 * http://api.jamendo.com/get2/id+idstr+name+url+image+rating+mbgid+mbid+genre/artist/jsonpretty/?name=triface
	 * 
	 * @param name Artist name
	 * @return <code>Artist</code> instance
	 * @throws JSONException 
	 * @throws ErrorMsg 
	 */
	Artist getArtist(String name) throws JSONException, ErrorMsg;
	
	/**
	 * Returns tracks ids of top week tracks<br>
	 * <br>
	 * http://www.jamendo.com/pl/rss/top-track-week (via XML backend)
	 * 
	 * @return
	 * @throws ErrorMsg 
	 */
	int[] getTop100Listened() throws ErrorMsg;
	
	/**
	 * Returns an array of albums matching an array of track ids, can be combined with
	 * <code>getTop100Listened</code>
	 * 
	 * @param id
	 * @return
	 * @throws JSONException
	 * @throws ErrorMsg 
	 */
	Album[] getAlbumsByTracksId(int id[]) throws JSONException, ErrorMsg;
	
	/**
	 * Returns an array of tracks matching an array of track ids, can be combined with
	 * <code>getTop100Listened</code>
	 * 
	 * @param id
	 * @return
	 * @throws JSONException
	 * @throws ErrorMsg 
	 */
	Track[] getTracksByTracksId(int id[], String encoding) throws JSONException, ErrorMsg;
	
	/**
	 * Gets album reviews<br>
	 * <br>
	 * http://api.jamendo.com/get2/id+name+text+rating+lang/review/jsonpretty/?album_id=7505
	 * 
	 * @param album
	 * @return
	 * @throws JSONException
	 * @throws ErrorMsg 
	 */
	Review[] getAlbumReviews(Album album)  throws JSONException, ErrorMsg;
	
	/**
	 * Gets playlist with album and track from the remote server
	 * 
	 * @param playlistRemote
	 * @return
	 * @throws JSONException 
	 * @throws ErrorMsg 
	 */
	PlayMethod getPlaylist(PlaylistRemote playlistRemote) throws JSONException, ErrorMsg;
	
	/**
	 * Gets radios by given ids<br>
	 * <br>
	 * http://api.jamendo.com/get2/id+idstr+name+image/radio/jsonpretty/?id=2+3
	 * @param id
	 * @return
	 * @throws JSONException 
	 * @throws ErrorMsg 
	 */
	Radio[] getRadiosByIds(int[] id) throws JSONException, ErrorMsg;
	
	/**
	 * Get radio by given idstr
	 * 
	 * @param idstr
	 * @return
	 * @throws JSONException
	 * @throws ErrorMsg 
	 */
	Radio[] getRadiosByIdstr(String idstr) throws JSONException, ErrorMsg;
	
	/**
	 * Retrieves tracks for the given radio<br>
	 * <br>
	 * http://www.jamendo.com/get2/stream+name+id+rating+album_id+album_name+album_image/track/jsonpretty/radio_track_inradioplaylist/?radio_id=4&nshuffle=1
	 * 
	 * @param radio
	 * @param n number of tracks to be retrieved at once
	 * @return
	 * @throws JSONException 
	 * @throws ErrorMsg 
	 */
	PlayMethod getRadioPlaylist(Radio radio, int n, String encoding) throws JSONException, ErrorMsg;
	
	/**
	 * Gets user defined playlists<br>
	 * <br>
	 * http://www.jamendo.com/get2/id+name+url/playlist/jsonpretty/playlist_user/?user_idstr=pierrotsmnrd&order=starred_desc
	 * 
	 * @param user
	 * @return
	 * @throws JSONException
	 * @throws ErrorMsg 
	 */
	PlaylistRemote[] getUserPlaylist(String user) throws JSONException, ErrorMsg;
	
	/**
	 * Gets user starred albums<br>
	 * <br>
	 * http://api.jamendo.com/get2/id+name+url+image+artist_name/album/jsonpretty/album_user_starred/?user_idstr=sylvinus&n=all
	 * 
	 * @param user
	 * @return
	 * @throws JSONException
	 * @throws ErrorMsg 
	 */
	Album[] getUserStarredAlbums(String user) throws JSONException, ErrorMsg;
	
	/**
	 * Get track lyrics<br>
	 * <br>
	 * http://api.jamendo.com/get2/text/track/jsonpretty/?id=241
	 * 
	 * @param track
	 * @return lyrics or null
	 * @throws ErrorMsg 
	 */
	String getTrackLyrics(Track track) throws ErrorMsg;
	
	/**
	 * Get album's license<br>
	 * <br>
	 * http://api.jamendo.com/get2/id+url+image/license/jsonpretty/?album_id=33
	 * 
	 * @param album
	 * @return
	 * @throws ErrorMsg 
	 */
	License getAlbumLicense(Album album) throws ErrorMsg;
	
	/**
	 * Get Album by its id<br>
	 * <br>
	 * http://api.jamendo.com/get2/id+name+rating+url+image+artist_name/album/jsonpretty/?id=7505 
	 * 
	 * @param id
	 * @return
	 * @throws JSONException 
	 * @throws ErrorMsg 
	 */
	Album getAlbumById(int id) throws JSONException, ErrorMsg;
}
