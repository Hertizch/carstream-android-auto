package com.thekirankumar.youtubeauto;

import android.content.Context;
import android.content.Intent;
import android.media.session.PlaybackState;

/**
 * Created by kiran.kumar on 11/01/18.
 */

class BroadcastFromWebview {
    public static Intent lastIntent = null;
    private static boolean broadcastEnabled = true;

    public static void setEnableBroadcast(Context context, boolean enable) {
        if (enable && !broadcastEnabled) {
            if (lastIntent != null) {
                context.sendBroadcast(lastIntent);
                lastIntent = null;
            }
        }

        broadcastEnabled = enable;
    }

    public static void broadcastTitle(Context context, String title) {
        Intent intent = new Intent(YoutubeMediaBrowserService.WEBVIEW_EVENT);
        intent.putExtra(YoutubeMediaBrowserService.MEDIA_TITLE, title);
        sendBroadcast(context, intent);
    }

    private static void sendBroadcast(Context context, Intent intent) {
        if (broadcastEnabled) {
            context.sendBroadcast(intent);
        } else {
            lastIntent = intent;
        }
    }

    public static void broadCastPlaying(Context context, String title) {
        Intent intent = new Intent(YoutubeMediaBrowserService.WEBVIEW_EVENT);
        intent.putExtra(YoutubeMediaBrowserService.MEDIA_TITLE, title);
        intent.putExtra(YoutubeMediaBrowserService.PLAYBACK_STATE, PlaybackState.STATE_PLAYING);
        sendBroadcast(context, intent);
    }

    public static void broadCastPaused(Context context, String title) {
        Intent intent = new Intent(YoutubeMediaBrowserService.WEBVIEW_EVENT);
        intent.putExtra(YoutubeMediaBrowserService.MEDIA_TITLE, title);
        intent.putExtra(YoutubeMediaBrowserService.PLAYBACK_STATE, PlaybackState.STATE_PAUSED);
        context.sendBroadcast(intent);
    }

    public static void broadCastLoading(Context context, String title) {
        Intent intent = new Intent(YoutubeMediaBrowserService.WEBVIEW_EVENT);
        intent.putExtra(YoutubeMediaBrowserService.MEDIA_TITLE, title);
        intent.putExtra(YoutubeMediaBrowserService.PLAYBACK_STATE, PlaybackState.STATE_BUFFERING);
        sendBroadcast(context, intent);
    }

    public static void broadCastError(Context context, String error) {
        Intent intent = new Intent(YoutubeMediaBrowserService.WEBVIEW_EVENT);
        intent.putExtra(YoutubeMediaBrowserService.MEDIA_TITLE, error);
        intent.putExtra(YoutubeMediaBrowserService.PLAYBACK_STATE, PlaybackState.STATE_ERROR);
        sendBroadcast(context, intent);
    }

}
