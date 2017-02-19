package software.cm.crazytower.helpers;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import software.cm.crazytower.modelo.Nodo;

public class APManager {
    private static final String SSID = "wifiEncarga";
    private static String SSID_PASS = "12345678";

    public static void recrearContrasenia(Context context) {
        // Se modifica la contraseña
        SSID_PASS = UUID.randomUUID().toString().substring(0, 8);

        // Se inicia nuevamente el anclaje de red con la nueva contraseña
        setWifiApState(context);
    }

    public static String obtenerContrasenia() {
        return SSID_PASS;
    }

    public static boolean setWifiApState(Context context) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            if (mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(false);
            }

            WifiConfiguration conf = getWifiApConfiguration();
            mWifiManager.addNetwork(conf);

            return (Boolean) mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class).invoke(mWifiManager, conf, true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean estaAnclajeRedActivo(Context context) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            return (Boolean) mWifiManager.getClass().getMethod("isWifiApEnabled").invoke(mWifiManager);
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public static WifiConfiguration getWifiApConfiguration() {
        /*WifiConfiguration conf = new WifiConfiguration();
        conf.SSID =  SSID;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        return conf;*/

        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = SSID;
        wc.preSharedKey = SSID_PASS;
        wc.hiddenSSID = false;
        wc.status = WifiConfiguration.Status.ENABLED;
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

        return (wc);
    }

    public static List<Nodo> leerTablaARP() {
        BufferedReader bufferedReader = null;
        List<Nodo> dispositivosConectados = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"));

            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String[] lineaArray = linea.split(" +");

                if (lineaArray != null && lineaArray.length >= 4) {
                    String ip = lineaArray[0];
                    String mac = lineaArray[3];
                    if (mac.matches("..:..:..:..:..:..")) {
                        if (ping(ip)) {
                            Nodo nodo = new Nodo(ip, mac);
                            dispositivosConectados.add(nodo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dispositivosConectados;
    }

    private static boolean ping(String ip){
        Runtime runtime = Runtime.getRuntime();
        try {
            Process  mIpAddrProcess = runtime.exec(String.format("/system/bin/ping -c 1 %s", ip));
            int mExitValue = mIpAddrProcess.waitFor();

            return (mExitValue == 0);
        } catch (Exception ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:"+ignore);
        }

        return false;
    }
    /*//check whether wifi hotspot on or off
    public static boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        }
        catch (Throwable ignored) {
            int i = 1;
        }
        return false;
    }

    // toggle wifi hotspot on or off
    public static boolean configApState(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = crearConfiguracionAP();

        try {
            // if WiFi is on, turn it off
            if(isApOn(context)) {
                wifimanager.setWifiEnabled(false);
            }
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, !isApOn(context));
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static WifiConfiguration crearConfiguracionAP() {
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"SSIDName\"";
        wc.preSharedKey  = "\"password\"";
        wc.hiddenSSID = true;
        wc.status = WifiConfiguration.Status.ENABLED;
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

        return (wc);
    }*/
}
