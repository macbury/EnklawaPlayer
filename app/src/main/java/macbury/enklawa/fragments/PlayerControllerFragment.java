package macbury.enklawa.fragments;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import at.markushi.ui.CircleButton;
import macbury.enklawa.R;
import macbury.enklawa.extensions.Converter;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.player.PlayerManager;
import macbury.enklawa.managers.player.PlayerManagerListener;
import macbury.enklawa.managers.player.sources.AbstractMediaSource;
import macbury.enklawa.services.PlayerService;

public class PlayerControllerFragment extends Fragment implements PlayerManagerListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
  private static final String TAG = "PlayerControllerFragment";
  private PlayerService.PlayerBinder playerBinder;
  private SeekBar timeSeekBar;
  private TextView runningTextView;
  private TextView totalRunnigTextView;
  private CircleButton playPauseButton;
  private ProgressBar loadingProgress;
  private boolean seekingStarted;

  public PlayerControllerFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view           = inflater.inflate(R.layout.fragment_player_controller, container, false);
    timeSeekBar         = (SeekBar)view.findViewById(R.id.seek_bar_player);
    runningTextView     = (TextView)view.findViewById(R.id.text_running_time);
    totalRunnigTextView = (TextView)view.findViewById(R.id.text_running_total_time);
    playPauseButton     = (CircleButton)view.findViewById(R.id.button_play_pause);
    loadingProgress     = (ProgressBar)view.findViewById(R.id.progress_loading);

    playPauseButton.setOnClickListener(this);
    timeSeekBar.setOnSeekBarChangeListener(this);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    getActivity().bindService(Enklawa.current().intents.player(), playerManagerServiceConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  public void onPause() {
    super.onPause();
    getActivity().unbindService(playerManagerServiceConnection);
    if (playerBinder != null) {
      Log.i(TAG, "Removing binder");
      playerBinder.removeListener(this);
      playerBinder = null;
    }
  }

  private ServiceConnection playerManagerServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      PlayerControllerFragment.this.playerBinder = (PlayerService.PlayerBinder)service;
      playerBinder.addListener(PlayerControllerFragment.this);
      updateUIForCurrentMediaSource();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      Log.i(TAG, "Player service disconnected!");
      playerBinder.removeListener(PlayerControllerFragment.this);
      playerBinder = null;
    }
  };

  private void updateUIForCurrentMediaSource() {
    if (playerBinder != null) {
      PlayerManager pm = playerBinder.getPlayerManager();
      if (pm.isRunning())
        updateUIInfoForMediaSource(pm, pm.getCurrentMediaSource());
    }
  }

  private void updateUIInfoForMediaSource(PlayerManager playerManager, AbstractMediaSource mediaSource) {
    timeSeekBar.setMax(mediaSource.getDuration());
    if (!seekingStarted) {
      timeSeekBar.setProgress(mediaSource.getPosition());
      runningTextView.setText(Converter.getDurationStringLong(mediaSource.getPosition()));
    }

    totalRunnigTextView.setText(Converter.getDurationStringLong(mediaSource.getDuration()));

    if (playerManager.isPreparing()) {
      loadingProgress.setVisibility(View.VISIBLE);
      playPauseButton.setVisibility(View.GONE);
    } else {
      loadingProgress.setVisibility(View.GONE);
      playPauseButton.setVisibility(View.VISIBLE);
    }

    if (playerManager.isPlaying()) {
      playPauseButton.setImageResource(R.drawable.ic_action_av_pause);
    } else {
      playPauseButton.setImageResource(R.drawable.ic_action_av_play);
    }
  }

  @Override
  public void onInitialize(PlayerManager manager, AbstractMediaSource mediaSource) {
    updateUIInfoForMediaSource(manager, mediaSource);
  }

  @Override
  public void onPlay(PlayerManager manager, AbstractMediaSource mediaSource) {
    Log.d(TAG, "onPlay:" + mediaSource.getTitle());
    updateUIInfoForMediaSource(manager, mediaSource);
  }

  @Override
  public void onBufferMedia(PlayerManager manager, AbstractMediaSource mediaSource) {

  }

  @Override
  public void onMediaUpdate(PlayerManager playerManager, AbstractMediaSource mediaSource) {
    //Log.d(TAG, "onMediaUpdate:" + mediaSource.getDuration());
    updateUIInfoForMediaSource(playerManager, mediaSource);
  }

  @Override
  public void onPause(PlayerManager manager, AbstractMediaSource mediaSource) {
    Log.d(TAG, "onPause:" + mediaSource.getTitle());
    updateUIInfoForMediaSource(manager, mediaSource);
  }

  @Override
  public void onFinish(PlayerManager manager, AbstractMediaSource mediaSource) {
    Log.d(TAG, "onFinish:" + mediaSource.getTitle());
    updateUIInfoForMediaSource(manager, mediaSource);
  }

  @Override
  public void onFinishAll(PlayerManager manager) {
    Log.d(TAG, "onFinishAll:");
  }

  @Override
  public void onClick(View v) {
    if (v == playPauseButton && playerBinder != null) {
      PlayerManager pm = playerBinder.getPlayerManager();
      if (pm.isPlaying()) {
        pm.pause();
      } else {
        pm.play();
      }
    }
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    if (seekingStarted) {
      runningTextView.setText(Converter.getDurationStringLong(progress));
    }
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
    seekingStarted = true;
    playerBinder.getPlayerManager().pause();
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
    PlayerManager pm = playerBinder.getPlayerManager();
    pm.seekTo(seekBar.getProgress());
    pm.play();
    seekingStarted = false;
  }

  public void stopPlayerIfPaused() {
    if (playerBinder != null && playerBinder.getPlayerManager().isPaused()) {
      getActivity().startService(Enklawa.current().intents.stopPlayer());
    }
  }
}
