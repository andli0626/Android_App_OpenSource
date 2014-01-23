/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teleca.jamendo.api;

import java.util.ArrayList;

import com.teleca.jamendo.activity.playview.PlayMethod;
import com.teleca.jamendo.model.Playlist;
import com.teleca.jamendo.model.Radio;

/**
 * Interface defining operations on internal application database 
 * 
 * @author Lukasz Wisniewski
 */
public interface IDatabase {
	
	/**
	 * Save a playlist to the database
	 * 
	 * @param playlist
	 * @param name
	 */
	public void savePlaylist(PlayMethod playlist, String name);
	
	/**
	 * Load playlist from database
	 * 
	 * @param playlistName
	 * @return <code>Playlist</code> instance or null if playlist does not exists
	 */
	public PlayMethod loadPlaylist(String playlistName);
	
	/**
	 * Remove a playlist from database
	 * 
	 * @param playlistName
	 */
	public void deletePlaylist(String playlistName);
	
	/**
	 * Checks if a given playlists exists in the database 
	 * 
	 * @param playlistName
	 * @return
	 */
	public boolean playlistExists(String playlistName);
	
	/**
	 * Get all available playlists from the database
	 * 
	 * @return
	 */
	public ArrayList<String> getAvailablePlaylists();
	
	/**
	 * Adds radio to list of recent radios
	 * 
	 * @param radio
	 */
	public void addRadioToRecent(Radio radio);
	
	/**
	 * Retrieves recent radios
	 * 
	 * @param limit
	 * @return
	 */
	public ArrayList<Radio> getRecentRadios(int limit);
	
	/**
	 * Adds entry to favorites
	 * 
	 * @param entry
	 */
	public void addToFavorites(Playlist entry);
	
	/**
	 * Remove entry to favorites
	 * 
	 * @param entry
	 */
	public void removeFromFavorites(Playlist entry);
	
	/**
	 * Gets favorite tracks
	 * 
	 * @return
	 */
	public PlayMethod getFavorites();

}
