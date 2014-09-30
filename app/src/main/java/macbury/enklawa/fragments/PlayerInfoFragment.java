package macbury.enklawa.fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import at.markushi.ui.CircleButton;
import macbury.enklawa.R;
import macbury.enklawa.db.DatabaseCRUDListener;
import macbury.enklawa.db.models.EnqueueEpisode;
import macbury.enklawa.extensions.Converter;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.player.PlayerManager;
import macbury.enklawa.managers.player.PlayerManagerListener;
import macbury.enklawa.managers.player.sources.AbstractMediaSource;
import macbury.enklawa.services.PlayerService;

public class PlayerInfoFragment extends Fragment implements PlayerManagerListener, DatabaseCRUDListener<EnqueueEpisode>, View.OnClickListener {
  private ImageView artworkImageView;
  private TextView titleTextView;
  private TextView timeTextView;
  private PlayerService.PlayerBinder playerBinder;
  private CircleButton playPauseButton;

  public PlayerInfoFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_player_info, container, false);

    this.playPauseButton        = (CircleButton) view.findViewById(R.id.button_play_pause);
    this.artworkImageView       = (ImageView)view.findViewById(R.id.player_artwork);
    this.titleTextView          = (TextView) view.findViewById(R.id.player_name);
    this.timeTextView           = (TextView) view.findViewById(R.id.player_time);

    playPauseButton.setOnClickListener(this);

    return view;
  }

  public boolean isPlaying() {
    return playerBinder != null && playerBinder.isPlaying();
  }

  @Override
  public void onResume() {
    super.onResume();
    Enklawa.current().db.queue.addListener(this);
    Enklawa.current().broadcasts.playerStatusChangedReceiver(this.getActivity(), playerStatusReceiver);
    bindIfRunning();
    updateUI();
  }

  public void updateUI() {
    if (playerBinder != null) {
      AbstractMediaSource ems = playerBinder.getPlayerManager().getCurrentMediaSource();
      titleTextView.setText(ems.getTitle());
      Ion.with(getActivity()).load(ems.getPreviewArtUri().toString()).intoImageView(artworkImageView);
      updateTime(ems);
      getView().setVisibility(View.VISIBLE);

      if (isPlaying()) {
        playPauseButton.setImageResource(R.drawable.ic_action_av_pause);
      } else {
        playPauseButton.setImageResource(R.drawable.ic_action_av_play);
      }
    } else {
      // check if any episodes are in queue!
      getView().setVisibility(View.GONE);
    }
  }

  public void updateTime(AbstractMediaSource ms) {
    timeTextView.setText(Converter.getDurationStringLong(ms.getPosition()) + " / " + Converter.getDurationStringLong(ms.getDuration()));
  }

  @Override
  public void onPause() {
    super.onPause();
    Enklawa.current().db.queue.removeListener(this);
    getActivity().unregisterReceiver(playerStatusReceiver);
    if (playerBinder != null)
      getActivity().unbindService(playerManagerServiceConnection);
  }

  private BroadcastReceiver playerStatusReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      bindIfRunning();
    }
  };

  private void bindIfRunning() {
    if (PlayerService.isRunning() && playerBinder == null) {
      getActivity().bindService(Enklawa.current().intents.player(), playerManagerServiceConnection, Context.BIND_AUTO_CREATE);
    }
  }

  private ServiceConnection playerManagerServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      PlayerInfoFragment.this.playerBinder = (PlayerService.PlayerBinder)service;
      playerBinder.addListener(PlayerInfoFragment.this);
      updateUI();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      playerBinder.removeListener(PlayerInfoFragment.this);
      playerBinder = null;
    }
  };

  @Override
  public void onInitialize(PlayerManager manager, AbstractMediaSource mediaSource) {
    updateUI();
  }

  @Override
  public void onFinishAll(PlayerManager manager) {
    updateUI();
  }

  @Override
  public void onPlay(PlayerManager manager, AbstractMediaSource mediaSource) {
    updateUI();
  }

  @Override
  public void onPause(PlayerManager manager, AbstractMediaSource mediaSource) {
    updateUI();
  }

  @Override
  public void onFinish(PlayerManager manager, AbstractMediaSource mediaSource) {

  }

  @Override
  public void onBufferMedia(PlayerManager manager, AbstractMediaSource mediaSource) {

  }

  @Override
  public void onMediaUpdate(PlayerManager playerManager, AbstractMediaSource currentMediaSource) {
    updateTime(currentMediaSource);
  }

  @Override
  public void afterCreate(EnqueueEpisode model) {

  }

  @Override
  public void afterDestroy(EnqueueEpisode object) {

  }

  @Override
  public void afterSave(EnqueueEpisode object) {

  }

  @Override
  public void onClick(View v) {
    if (v == playPauseButton) {
      if (isPlaying()) {
        Enklawa.current().services.pausePlayer();
      } else {
        Enklawa.current().services.playNextInQueue();
      }
    }
  }
}
