package software.cm.crazytower.encuesta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import software.cm.crazytower.R;
import software.cm.crazytower.componentes.fragmentos.FragmentoImagenSlider;

public class ActividadEncuesta extends FragmentActivity {
    private static final int CANT_PAGINAS = 5;
    private ViewPager mPaginador;
    private PagerAdapter mPaginadorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_encuesta);

        mPaginador = (ViewPager) findViewById(R.id.pager);
        mPaginadorAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPaginador.setAdapter(mPaginadorAdapter);
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new FragmentoImagenSlider();
        }

        @Override
        public int getCount() {
            return CANT_PAGINAS;
        }
    }
}
