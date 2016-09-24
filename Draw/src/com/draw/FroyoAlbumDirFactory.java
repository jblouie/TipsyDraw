package com.draw;

import java.io.File;

import android.os.Environment;
import android.util.Log;

public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {

	@Override
	public File getAlbumStorageDir(String albumName) {
		// TODO Auto-generated method stub
		Log.i("FROYOALBUM", Environment.getExternalStoragePublicDirectory(
			    Environment.DIRECTORY_PICTURES
				  ).getAbsolutePath());
		return new File(
		  Environment.getExternalStoragePublicDirectory(
		    Environment.DIRECTORY_PICTURES
		  ), 
		  albumName
		);
	}
}