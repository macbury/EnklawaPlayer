package macbury.enklawa.managers.player;

import macbury.enklawa.managers.player.sources.AbstractMediaSource;

/**
 * Created by macbury on 25.09.14.
 */
public interface PlayerManagerListener {

  public void onBufferMedia(PlayerManager manager, AbstractMediaSource mediaSource);

}
