package com.example.mediaplay;

import android.annotation.TargetApi;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnTimedTextListener;
import android.media.MediaPlayer.TrackInfo;
import android.media.TimedText;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MediaActivity extends Activity implements SurfaceHolder.Callback,
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
		OnClickListener,OnTimedTextListener {
	ImageButton btnplay, btnstop, btnpause;
	SurfaceView surfaceView;
	MediaPlayer mediaPlayer;
	int position;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnplay = (ImageButton) this.findViewById(R.id.btnplay);
		btnstop = (ImageButton) this.findViewById(R.id.btnplay);
		btnpause = (ImageButton) this.findViewById(R.id.btnplay);
		tv = (TextView) this.findViewById(R.id.tv);

		btnstop.setOnClickListener(this);
		btnplay.setOnClickListener(this);
		btnpause.setOnClickListener(this);

		mediaPlayer = new MediaPlayer();
		surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);

		// 设置SurfaceView自己不管理的缓冲区
		surfaceView.getHolder()
				.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.getHolder().addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {

			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				if (position > 0) {
					try {
						// 开始播放
						play();
						// 并直接从指定位置开始播放
						mediaPlayer.seekTo(position);
						position = 0;
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {

			}
		});
		
//		try {
//			mediaPlayer.reset();
//			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
////			mediaPlayer.selectTrack(3);
//			// 设置需要播放的视频
//			mediaPlayer.setDataSource("/mnt/sdcard/a.mp4");
//			// 把视频画面输出到SurfaceView
//			mediaPlayer.setDisplay(surfaceView.getHolder());
//			mediaPlayer.prepare();
//			// 播放
//			mediaPlayer.start();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnplay:
			play();
			break;

		case R.id.btnpause:
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			} else {
				mediaPlayer.start();
			}
			break;

		case R.id.btnstop:
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}

			break;
		default:
			break;
		}

	}

	@Override
	protected void onPause() {
		// 先判断是否正在播放
		if (mediaPlayer.isPlaying()) {
			// 如果正在播放我们就先保存这个播放位置
			position = mediaPlayer.getCurrentPosition();
			mediaPlayer.stop();
		}
		super.onPause();
	}

	private void play() {
		try {
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			// 设置需要播放的视频
			mediaPlayer.setDataSource("/mnt/sdcard/a.mp4");
//			
			// 把视频画面输出到SurfaceView
			mediaPlayer.setDisplay(surfaceView.getHolder());
			mediaPlayer.prepare();
			// 播放
//			mediaPlayer.start();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					Log.e("log", "000");
					try { 
						mp.addTimedTextSource("file:///android_asset/b.srt",
								MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
						Log.e("log", "211");
						TrackInfo[] trackInfos = mediaPlayer.getTrackInfo();
						Log.e("log", "111");
						if (trackInfos != null && trackInfos.length > 0) {
							Log.e("log", "122");
							for (int i = 0; i < trackInfos.length; i++) {
								Log.e("log", "123");
								final TrackInfo info = trackInfos[i];
								info.toString(); 

								Log.e("tag",
										"TrackInfo: " + info.getTrackType() + " "
												+ info.getLanguage());

								if (info.getTrackType() == TrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
									mediaPlayer.selectTrack(i);
								} else if (info.getTrackType() == TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT) {
									mediaPlayer.selectTrack(i);
								}
							}
						}
					} catch (Exception e) {
						Log.e("log", "440");
						e.printStackTrace();
						
					}
					mediaPlayer.start();
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		
	}

	@Override
	public void onCompletion(MediaPlayer mp) {

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	@Override
	public void onTimedText(MediaPlayer mp, TimedText text) {
		tv.setText("");
	}

}
