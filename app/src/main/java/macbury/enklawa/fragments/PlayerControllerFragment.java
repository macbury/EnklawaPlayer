package macbury.enklawa.fragments;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import macbury.enklawa.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class PlayerControllerFragment extends Fragment {


    public PlayerControllerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_controller, container, false);
    }


}
