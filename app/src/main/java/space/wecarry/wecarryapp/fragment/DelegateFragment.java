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
public class DelegateFragment extends Fragment {

    public DelegateFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delegate, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_delegate));

        return rootView;
    }

}
