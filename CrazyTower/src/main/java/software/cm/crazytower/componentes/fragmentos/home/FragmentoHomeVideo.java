package software.cm.crazytower.componentes.fragmentos.home;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import software.cm.crazytower.R;
import software.cm.crazytower.actividades.CrazyTowerHome;

public class FragmentoHomeVideo extends Fragment {
    private VideoView video;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragmento_actividad_home_video, container, false);

        this.video = (VideoView) rootView.findViewById(R.id.video_fragmento_home);
        //String path1 = "http://techslides.com/demos/sample-videos/small.3gp";
        //String path1 = "https://drive.google.com/file/d/0B9qY7Wb9busiQ190dWpOZWF3VFk/view";
        Uri uri = Uri.parse("android.resource://" + this.getActivity().getPackageName() + "/" + R.raw.videoclaro);
        //Uri uri = Uri.parse("android.resource://" + this.getActivity().getPackageName() + "/" + R.raw.small);
        //Uri uri = Uri.parse(path1);

        this.video.setVideoURI(uri);
        this.video.start();

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer vmp) {
                ((CrazyTowerHome)FragmentoHomeVideo.this.getActivity()).cambiarPagina();
            }
        });

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

       /* if (this.video != null) {
            if (this.isVisible()) {
                if (!isVisibleToUser) { // If we are becoming invisible, then...
                    //pause or stop video
                    //this.video.stopPlayback();
                } else {
                    //play your video
                    this.video.start();
                }
            }
        }*/
    }

    public void reproducirVideo() {
        if (this.video != null) {
            this.video.start();
        }
    }
}
