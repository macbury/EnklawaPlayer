package macbury.enklawa.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import macbury.enklawa.R;
import macbury.enklawa.extensions.Converter;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.player.PlayerManager;
import macbury.enklawa.managers.player.PlayerManagerListener;
import macbury.enklawa.managers.player.sources.AbstractMediaSource;
import macbury.enklawa.services.PlayerService;

public class PlayerControllerFragment extends Fragment implements PlayerManagerListener {
  private static final String TAG = "PlayerControllerFragment";
  private PlayerService.PlayerBinder playerBinder;
  private SeekBar timeSeekBar;
  private TextView runningTextView;
  private TextView totalRunnigTextView;

  public PlayerControllerFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view           = inflater.inflate(R.layout.fragment_player_controller, container, false);
    timeSeekBar         = (SeekBar)view.findViewById(R.id.seek_bar_player);
    runningTextView     = (TextView)view.findViewById(R.id.text_running_time);
    totalRunnigTextView = (TextView)view.findViewById(R.id.text_running_total_time);
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
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      Log.i(TAG, "Player service disconnected!");
      playerBinder.removeListener(PlayerControllerFragment.this);
      playerBinder = null;
    }
  };

  @Override
  public void onPlay(PlayerManager manager, AbstractMediaSource mediaSource) {
    Log.d(TAG, "onPlay:" + mediaSource.getTitle());
    timeSeekBar.setMax(mediaSource.getDuration());
  }

  @Override
  public void onBufferMedia(PlayerManager manager, AbstractMediaSource mediaSource) {

  }

  @Override
  public void onMediaUpdate(PlayerManager playerManager, AbstractMediaSource mediaSource) {
    Log.d(TAG, "onMediaUpdate:" + mediaSource.getDuration());
    timeSeekBar.setProgress(mediaSource.getPosition()/mediaSource.getDuration());
    timeSeekBar.setMax(1);
    runningTextView.setText(Converter.getDurationStringLong(mediaSource.getPosition()));
    totalRunnigTextView.setText(Converter.getDurationStringLong(mediaSource.getDuration()));
  }

  @Override
  public void onPause(PlayerManager manager, AbstractMediaSource mediaSource) {
    Log.d(TAG, "onPause:" + mediaSource.getTitle());
  }

  @Override
  public void onFinish(PlayerManager manager, AbstractMediaSource mediaSource) {
    Log.d(TAG, "onFinish:" + mediaSource.getTitle());
  }

  @Override
  public void onInitialize(PlayerManager manager, AbstractMediaSource mediaSource) {

  }

  @Override
  public void onFinishAll(PlayerManager manager, AbstractMediaSource mediaSource) {
    Log.d(TAG, "onFinishAll:" + mediaSource.getTitle());
  }

}
