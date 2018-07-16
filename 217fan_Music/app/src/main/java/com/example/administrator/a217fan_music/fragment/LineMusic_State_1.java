package com.example.administrator.a217fan_music.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.administrator.a217fan_music.R;


public class LineMusic_State_1 extends Fragment {

    private final static String TAG="LinMusic_State_1";
    EditText searchEdit;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_linemusic_state_1, container, false);
       searchEdit=(EditText)view.findViewById(R.id.line_Search);
       fragmentManager=getFragmentManager();
       fragmentTransaction=fragmentManager.beginTransaction();

       return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG, "onResume: 执行了" );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(TAG, "onPause: 执行了" );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w(TAG, "onStop: 执行了" );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
