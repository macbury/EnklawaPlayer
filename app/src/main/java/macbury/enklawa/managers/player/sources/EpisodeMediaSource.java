package macbury.enklawa.managers.player.sources;

import android.net.Uri;

import macbury.enklawa.db.models.EnqueueEpisode;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.player.PlaybackStatus;

/**
 * Created by macbury on 25.09.14.
 */
public class EpisodeMediaSource extends AbstractMediaSource {
  private final EnqueueEpisode enqueueEpisode;

  public EpisodeMediaSource(EnqueueEpisode enqueueEpisode) {
    this.enqueueEpisode = enqueueEpisode;
    this.status         = enqueueEpisode.status;
  }

  @Override
  public String getTitle() {
    return enqueueEpisode.episode.name;
  }

  @Override
  public String getSummary() {
    return enqueueEpisode.episode.program.name;
  }

  @Override
  public Uri getMediaUri() {
    return Enklawa.current().storage.getEpisodeUri(enqueueEpisode.episode);
  }

  @Override
  public Uri getPreviewArtUri() {
    return Uri.parse(enqueueEpisode.episode.image);
  }

  @Override
  public boolean isLiveStream() {
    return false;
  }

  @Override
  public void onPlay() {
    setStatus(PlaybackStatus.Playing);
  }

  @Override
  public void onPause() {
    setStatus(PlaybackStatus.Paused);
  }

  @Override
  public void setPosition(int duration) {
    enqueueEpisode.time = duration;
    Enklawa.current().db.queue.update(enqueueEpisode);
  }

  @Override
  public int getDuration() {
    return enqueueEpisode.episode.getDuration();
  }

  @Override
  public void onFinishPlayback() {
    setStatus(PlaybackStatus.Finished);
  }

  @Override
  public int getPosition() {
    return enqueueEpisode.time;
  }

  public EnqueueEpisode getEnqueueEpisode() {
    return enqueueEpisode;
  }

  @Override
  public boolean equals(Object o) {
    if (EnqueueEpisode.class.isInstance(o)) {
      EnqueueEpisode ee = (EnqueueEpisode)o;
      return ee.id == enqueueEpisode.id;
    } else if (EpisodeMediaSource.class.isInstance(o)) {
      EpisodeMediaSource ems = (EpisodeMediaSource)o;
      return (ems.getEnqueueEpisode().id == enqueueEpisode.id);
    } else {
      return super.equals(o);
    }
  }

  public Episode getEpisode() {
    return enqueueEpisode.episode;
  }

  @Override
  public void setStatus(PlaybackStatus status) {
    super.setStatus(status);
    enqueueEpisode.status = status;
    Enklawa.current().db.queue.update(enqueueEpisode);
  }
}
