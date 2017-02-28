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
    public static final String PARAM_VIDEO_URL = "urlVideo";
    private VideoView video;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragmento_actividad_home_video, container, false);

        this.video = (VideoView) rootView.findViewById(R.id.video_fragmento_home);
        String uriVideo = this.getArguments().getString(PARAM_VIDEO_URL);
        Uri uri = Uri.parse(uriVideo);

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

    public void reproducirVideo() {
        if (this.video != null) {
            this.video.start();
        }
    }
}
