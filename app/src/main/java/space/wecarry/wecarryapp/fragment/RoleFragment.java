package space.wecarry.wecarryapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.wecarry.wecarryapp.R;

/**
 * Created by Tunabutter on 8/2/2016.
 */
public class RoleFragment extends Fragment {

    public RoleFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_role, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_role));

        return rootView;
    }

}
