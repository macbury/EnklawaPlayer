package macbury.enklawa.fragments;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import macbury.enklawa.R;
import macbury.enklawa.services.PlayerService;

public class PlayerInfoFragment extends Fragment {
  public PlayerInfoFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_player_info, container, false);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    updateUI();
  }

  public void updateUI() {
    if (PlayerService.isRunning()) {
      getView().setVisibility(View.VISIBLE);
    } else {
      getView().setVisibility(View.GONE);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
  }
}
