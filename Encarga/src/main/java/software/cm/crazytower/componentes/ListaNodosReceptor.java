package software.cm.crazytower.componentes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

@SuppressLint("ParcelCreator")
public class ListaNodosReceptor extends ResultReceiver {
    private Receptor receptor;

    public ListaNodosReceptor(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (this.receptor != null) {
            this.receptor.onReceiveResult(resultCode, resultData);
        }
    }

    public void setReceptor(Receptor receiver) {
        this.receptor = receiver;
    }

}
