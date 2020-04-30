package com.easyfilepicker.filter.loader;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

public class VideoLoader extends CursorLoader {
    private static final String[] VIDEO_PROJECTION = {
            //Base File
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            //Video File
            MediaStore.Video.Media.DURATION
    };

    private VideoLoader(Context context, Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public VideoLoader(Context context) {
        super(context);
//        setProjection(VIDEO_PROJECTION);
//        setUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//        setSortOrder(MediaStore.Video.Media.DATE_ADDED + " DESC");
//
//        setSelection(MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=? or "+ MIME_TYPE + "=?");
//        String[] selectionArgs;
//        selectionArgs = new String[] { "video/mpeg","video/mp4" ,"video/mov" ,"video/avi" };
//        setSelectionArgs(selectionArgs);


        // Return only video and image metadata.
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri("external");

        setProjection(VIDEO_PROJECTION);
        setUri(queryUri);
        setSortOrder(MediaStore.Video.Media.DATE_ADDED + " DESC");

        setSelection(selection);
        setSelectionArgs(null);
    }
}
