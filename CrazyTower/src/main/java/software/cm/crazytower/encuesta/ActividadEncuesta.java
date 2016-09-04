package software.cm.crazytower.encuesta;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import software.cm.crazytower.R;
import software.cm.crazytower.componentes.fragmentos.FragmentoImagenSlider;

public class ActividadEncuesta extends FragmentActivity {
    private static final int CANT_PAGINAS = 5;
    private ViewPager mPaginador;
    private PagerAdapter mPaginadorAdapter;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_encuesta);

        this.mPaginador = (ViewPager) findViewById(R.id.pager);
        this.mPaginador.addOnPageChangeListener(new EncuestaOnPageChangeListener());
        this.mPaginadorAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        this.mPaginador.setAdapter(mPaginadorAdapter);

        this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.mProgressBar.setScaleY(3f);
        this.mProgressBar.getProgressDrawable().setColorFilter(
                Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        this.mProgressBar.setMax(CANT_PAGINAS);
    }

    @Override
    public void onBackPressed() {
        if (mPaginador.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPaginador.setCurrentItem(mPaginador.getCurrentItem() - 1);
        }
    }

    private class EncuestaOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            ActividadEncuesta.this.mProgressBar.setProgress(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FragmentoImagenSlider fragmento = new FragmentoImagenSlider();
            Bundle bundle = new Bundle();
            bundle.putInt("nroPagina", position);
            fragmento.setArguments(bundle);
            return fragmento;
        }

        @Override
        public int getCount() {
            return CANT_PAGINAS;
        }
    }
}
