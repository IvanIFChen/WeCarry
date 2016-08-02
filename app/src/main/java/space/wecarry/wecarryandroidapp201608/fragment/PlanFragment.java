package space.wecarry.wecarryandroidapp201608.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.wecarry.wecarryandroidapp201608.R;

/**
 * Created by Tunabutter on 8/2/2016.
 */
public class PlanFragment extends Fragment {

    public PlanFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plan, container, false);
//        int i = getArguments().getInt(ARG_PLANET_NUMBER);
//        String planet = getResources().getStringArray(R.array.planets_array)[i];
//
//        int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
//                "drawable", getActivity().getPackageName());
//        ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
//        getActivity().setTitle(planet);
        return rootView;
    }

}
