package com.ghostflying.locationreportenabler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import java.io.DataOutputStream;
import java.io.IOException;

public class SimStateChangedReceiver extends BroadcastReceiver {
    private final String COMMAND_PREFIX = "setprop ";
    private final String[] PROPERTIES = {"gsm.sim.operator.numeric 310030"};

    public SimStateChangedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager.getSimState() == TelephonyManager.SIM_STATE_READY){
            enableLocationReport();
        }
    }

    public void enableLocationReport() {
        try{
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            for (String property : PROPERTIES) {
                os.writeBytes(COMMAND_PREFIX + property + "\n");
            }
            os.writeBytes("exit\n");
            os.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
