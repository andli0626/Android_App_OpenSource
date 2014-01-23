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

package com.teleca.jamendo.util.download;

import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.model.Playlist;

/**
 * Various helper functions
 * 
 * @author Lukasz Wisniewski
 */
public class DownloadHelper {
	
	public static String getFileName(Playlist playlistEntry, String downloadFormat){
		String pattern = "%02d - %s";
		
		if(downloadFormat.equals(IServerApi.ENCODING_MP3)){
			pattern += ".mp3";
		} else {
			pattern += ".ogg";
		}
		return String.format(pattern, playlistEntry.getTrack().getNumAlbum(), playlistEntry.getTrack().getName());
	}
	
	public static String getRelativePath(Playlist playlistEntry){
		return String.format("/%s/%s", 
				playlistEntry.getAlbum().getArtistName(),
				playlistEntry.getAlbum().getName());
	}
	
	public static String getAbsolutePath(Playlist playlistEntry, String destination){
		return destination+getRelativePath(playlistEntry);
	}
}
