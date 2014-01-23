
package com.teleca.jamendo.util.download;

import java.util.ArrayList;

import com.teleca.jamendo.model.Playlist;


/**
 * @author Lukasz Wisniewski
 */
public interface IDownload {
	
	/**
	 * Add an entry to the download queue
	 * 
	 * @param entry
	 */
	public void addToDownloadQueue(Playlist entry);
	
	/**
	 * Returns tracks path (if is available locally, on the SD Card)
	 * 
	 * @param entry
	 * @return
	 */
	public String getTrackPath(Playlist entry);
	
	/**
	 * Returns complete and queued downloads
	 * 
	 * @return
	 */
	public ArrayList<DownloadJob> getAllDownloads();
	
	/**
	 * Returns queued downloads
	 * 
	 * @return
	 */
	public ArrayList<DownloadJob> getQueuedDownloads();
	
	/**
	 * Returns completed downloads
	 * 
	 * @return
	 */
	public ArrayList<DownloadJob> getCompletedDownloads();

}
