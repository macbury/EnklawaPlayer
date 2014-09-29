package macbury.enklawa.managers.player.sources;

import android.net.Uri;

import macbury.enklawa.db.models.EnqueueEpisode;
import macbury.enklawa.managers.Enklawa;

/**
 * Created by macbury on 25.09.14.
 */
public class EpisodeMediaSource extends AbstractMediaSource {
  private final EnqueueEpisode enqueueEpisode;

  public EpisodeMediaSource(EnqueueEpisode enqueueEpisode) {
    this.enqueueEpisode = enqueueEpisode;
  }

  @Override
  public String getTitle() {
    return enqueueEpisode.episode.name;
  }

  @Override
  public String getSummary() {
    return enqueueEpisode.episode.description;
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
  public void onStart() {
    enqueueEpisode.status = EnqueueEpisode.Status.Playing;
    Enklawa.current().db.queue.update(enqueueEpisode);
  }

  @Override
  public void onPause() {
    enqueueEpisode.status = EnqueueEpisode.Status.Paused;
    Enklawa.current().db.queue.update(enqueueEpisode);
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
    enqueueEpisode.status = EnqueueEpisode.Status.Played;
    Enklawa.current().db.queue.destroy(enqueueEpisode);
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

  public EnqueueEpisode getEpisode() {
    return enqueueEpisode;
  }
}
