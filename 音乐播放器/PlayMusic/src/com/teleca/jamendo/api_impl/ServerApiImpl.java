package com.teleca.jamendo.api_impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;

import com.teleca.jamendo.activity.playview.PlayMethod;
import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.Artist;
import com.teleca.jamendo.model.ErrorMsg;
import com.teleca.jamendo.model.License;
import com.teleca.jamendo.model.Playlist;
import com.teleca.jamendo.model.PlaylistRemote;
import com.teleca.jamendo.model.Radio;
import com.teleca.jamendo.model.Review;
import com.teleca.jamendo.model.Track;
import com.teleca.jamendo.other.AlbumFunctions;
import com.teleca.jamendo.other.ArtistFunctions;
import com.teleca.jamendo.other.LicenseBuilder;
import com.teleca.jamendo.other.PlaylistFunctions;
import com.teleca.jamendo.other.RSSFunctions;
import com.teleca.jamendo.other.RadioFunctions;
import com.teleca.jamendo.other.ReviewFunctions;
import com.teleca.jamendo.other.TrackFunctions;
import com.teleca.jamendo.util.Caller;

/**
 * 实现服务器接口的方法 Jamendo Get2 API implementation, Apache HTTP Client used for web
 * requests
 * 
 * @author Lukasz Wisniewski
 */
public class ServerApiImpl implements IServerApi {

	private static String GET_API = "http://api.jamendo.com/get2/";

	private String doGet(String query) throws ErrorMsg {
		return Caller.doGet(GET_API + query);
	}

	@Override
	public Album[] getPopularAlbumsWeek() throws JSONException, ErrorMsg {

		String jsonString = doGet("id+name+url+image+rating+artist_name/album/json/?n=20&order=ratingweek_desc");
		if (jsonString == null)
			return null;
		JSONArray jsonArrayAlbums = new JSONArray(jsonString);
		return AlbumFunctions.getAlbums(jsonArrayAlbums);

	}

	@Override
	public Track[] getAlbumTracks(Album album, String encoding)
			throws JSONException, ErrorMsg {
		String jsonString = doGet("numalbum+id+name+duration+rating+url+stream/track/json/?album_id="
				+ album.getId() + "&streamencoding=" + encoding);
		JSONArray jsonArrayTracks = new JSONArray(jsonString);
		return TrackFunctions.getTracks(jsonArrayTracks, true);
	}

	@Override
	public Album[] searchForAlbumsByArtist(String artistName)
			throws JSONException, ErrorMsg {

		try {
			artistName = URLEncoder.encode(artistName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		String jsonString = doGet("id+name+url+image+rating+artist_name/album/json/?order=ratingweek_desc&n=50&searchquery="
				+ artistName);
		JSONArray jsonArrayAlbums = new JSONArray(jsonString);
		return AlbumFunctions.getAlbums(jsonArrayAlbums);
	}

	@Override
	public Album[] searchForAlbumsByTag(String tag) throws JSONException,
			ErrorMsg {
		try {
			tag = URLEncoder.encode(tag, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		String jsonString = doGet("id+name+url+image+rating+artist_name/album/json/?order=ratingweek_desc&tag_idstr="
				+ tag + "&n=50");
		JSONArray jsonArrayAlbums = new JSONArray(jsonString);
		return AlbumFunctions.getAlbums(jsonArrayAlbums);
	}

	@Override
	public Album[] searchForAlbumsByArtistName(String artistName)
			throws JSONException, ErrorMsg {
		try {
			artistName = URLEncoder.encode(artistName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		String jsonString = doGet("id+name+url+image+rating+artist_name/album/json/?order=ratingweek_desc&n=50&artist_name="
				+ artistName);
		JSONArray jsonArrayAlbums = new JSONArray(jsonString);
		return AlbumFunctions.getAlbums(jsonArrayAlbums);
	}

	@Override
	public Artist getArtist(String name) throws JSONException, ErrorMsg {
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		String jsonString = doGet("id+idstr+name+url+image+rating+mbgid+mbid+genre/artist/jsonpretty/?name="
				+ name);
		JSONArray jsonArrayAlbums = new JSONArray(jsonString);
		return ArtistFunctions.getArtist(jsonArrayAlbums)[0];
	}

	@Override
	public int[] getTop100Listened() throws ErrorMsg {
		String rssString = Caller
				.doGet("http://www.jamendo.com/en/rss/top-track-week");
		return RSSFunctions.getTracksIdFromRss(rssString);
	}

	@Override
	public Album[] getAlbumsByTracksId(int[] id) throws JSONException,
			ErrorMsg {
		if (id == null)
			return null;

		String id_query = Caller.createStringFromIds(id);

		String jsonString = doGet("id+name+url+image+rating+artist_name/album/json/?n="
				+ id.length + "&track_id=" + id_query);
		JSONArray jsonArrayAlbums = new JSONArray(jsonString);
		return AlbumFunctions.getAlbums(jsonArrayAlbums);
	}

	@Override
	public Track[] getTracksByTracksId(int[] id, String encoding)
			throws JSONException, ErrorMsg {
		if (id == null)
			return null;

		String id_query = Caller.createStringFromIds(id);

		String jsonString = doGet("id+numalbum+name+duration+rating+url+stream/track/json/?streamencoding="
				+ encoding + "&n=" + id.length + "&id=" + id_query);
		JSONArray jsonArrayTracks = new JSONArray(jsonString);
		return TrackFunctions.getTracks(jsonArrayTracks, false);
	}

	@Override
	public Review[] getAlbumReviews(Album album) throws JSONException,
			ErrorMsg {
		String jsonString = doGet("id+name+text+rating+lang+user_name+user_image/review/json/?album_id="
				+ album.getId());
		JSONArray jsonReviewAlbums = new JSONArray(jsonString);
		return ReviewFunctions.getReviews(jsonReviewAlbums);
	}

	@Override
	public PlayMethod getRadioPlaylist(Radio radio, int n, String encoding)
			throws JSONException, ErrorMsg {
		String jsonString = doGet("track_id/track/json/radio_track_inradioplaylist/?radio_id="
				+ radio.getId() + "&nshuffle=" + n * 10 + "&n=" + n);
		int[] tracks_id = TrackFunctions.getRadioPlaylist(new JSONArray(
				jsonString));

		Album[] albums = getAlbumsByTracksId(tracks_id);
		Track[] tracks = getTracksByTracksId(tracks_id, encoding);

		if (albums == null || tracks == null)
			return null;
		Hashtable<Integer, Playlist> hashtable = new Hashtable<Integer, Playlist>();
		for (int i = 0; i < tracks.length && i < albums.length; i++) {
			Playlist playlistEntry = new Playlist();
			playlistEntry.setAlbum(albums[i]);
			playlistEntry.setTrack(tracks[i]);
			hashtable.put(tracks[i].getId(), playlistEntry);
		}

		// creating playlist in the correct order
		PlayMethod playlist = new PlayMethod();
		for (int i = 0; i < tracks_id.length && i < albums.length; i++) {
			playlist.addPlaylistEntry(hashtable.get(tracks_id[i]));
		}
		return playlist;
	}

	@Override
	public Radio[] getRadiosByIds(int[] id) throws JSONException, ErrorMsg {
		String id_query = Caller.createStringFromIds(id);
		String jsonString = doGet("id+idstr+name+image/radio/json/?id="
				+ id_query);
		return RadioFunctions.getRadios(new JSONArray(jsonString));
	}

	@Override
	public Radio[] getRadiosByIdstr(String idstr) throws JSONException, ErrorMsg {
		String jsonString = doGet("id+idstr+name+image/radio/json/?idstr="
				+ idstr);
		return RadioFunctions.getRadios(new JSONArray(jsonString));
	}

	@Override
	public PlaylistRemote[] getUserPlaylist(String user) throws JSONException,
			ErrorMsg {

		try {
			user = URLEncoder.encode(user, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		String jsonString = doGet("id+name+url+duration/playlist/json/playlist_user/?order=starred_desc&user_idstr="
				+ user);
		return PlaylistFunctions.getPlaylists(new JSONArray(jsonString));
	}

	@Override
	public PlayMethod getPlaylist(PlaylistRemote playlistRemote)
			throws JSONException, ErrorMsg {
		String jsonString = doGet("stream+name+duration+url+id+rating/track/json/?playlist_id="
				+ playlistRemote.getId());
		return TrackFunctions.getPlaylist(new JSONArray(jsonString));
	}

	@Override
	public String getTrackLyrics(Track track) throws ErrorMsg {
		String jsonString = doGet("text/track/json/?id=" + track.getId());
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(jsonString);
			if (jsonArray.length() > 0)
				return jsonArray.getString(0).replace("\r", "");
			else
				return null;
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public License getAlbumLicense(Album album) throws ErrorMsg {
		String jsonString = doGet("id+url+image/license/json/?album_id="
				+ album.getId());
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(jsonString);
			if (jsonArray.length() > 0)
				return new LicenseBuilder().build(jsonArray.getJSONObject(0));
			else
				return null;
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public Album getAlbumById(int id) throws JSONException, ErrorMsg {

		String jsonString = doGet("id+name+url+image+rating+artist_name/album/json/?id="
				+ id);
		JSONArray jsonArrayAlbums = new JSONArray(jsonString);
		Album[] album = AlbumFunctions.getAlbums(jsonArrayAlbums);
		if (album != null && album.length > 0)
			return album[0];
		return null;
	}

	@Override
	public Album[] getUserStarredAlbums(String user) throws JSONException,
			ErrorMsg {

		try {
			user = URLEncoder.encode(user, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		String jsonString = doGet("id+name+url+image+rating+artist_name/album/json/album_user_starred/?user_idstr="
				+ user + "&n=all&order=rating_desc");
		JSONArray jsonArrayAlbums = new JSONArray(jsonString);
		return AlbumFunctions.getAlbums(jsonArrayAlbums);
	}

	// TODO private String nameToIdstr(String name);

}
