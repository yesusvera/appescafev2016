package br.org.unesco.appesca.util;

import android.util.Log;

/**
 * Created by yesus on 05/03/16.
 */
public class AppescaUtil {

    public static void limparMemoria(){
        System.gc();
        Runtime.getRuntime().gc();

        Log.i("GC", "LIMPANDO MEMORIA ANDROID");
    }
}
