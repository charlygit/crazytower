package software.cm.crazytower.componentes.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.widget.Toast;

import software.cm.crazytower.actividades.CrazyTowerSplash;
import software.cm.crazytower.helpers.AccionSistemaSMS;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                SmsMessage smsMessage = msgs[0];

                String sender = msgs[0].getOriginatingAddress();
                String mensaje = smsMessage.getMessageBody();
                boolean codigoValido = this.esCodigoValido(mensaje);

                if (codigoValido && PhoneNumberUtils.compare("098539084", sender)) {
                    Intent i = new Intent(context, CrazyTowerSplash.class);
                    i.putExtra(CrazyTowerSplash.PARAM_ACCION_SMS, mensaje.toUpperCase());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    //Toast.makeText(context, "Iguales, mensaje: " + smsMessage.getMessageBody(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Distintos, mensaje: " + smsMessage.getMessageBody(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean esCodigoValido(String mensaje) {
        return AccionSistemaSMS.BLOQUEAR.name().equalsIgnoreCase(mensaje) ||
                AccionSistemaSMS.DESBLOQUEAR.name().equalsIgnoreCase(mensaje);
    }
}
