package io.gvox.phonecalltrap;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import android.content.Context;
import android.Manifest;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import static android.Manifest.permission.READ_CALL_LOG;

import org.json.JSONException;
import org.json.JSONArray;


public class PhoneCallTrap extends CordovaPlugin {

    CallStateListener listener;
    
    private static final int READ_CALL_LOG_REQ_CODE = 0;
    private static final int WRITE_CALL_LOG_REQ_CODE = 0;
    
    public static final String READ_CALL_LOG =
        android.Manifest.permission.READ_CALL_LOG;
    public static final String WRITE_CALL_LOG =
        android.Manifest.permission.WRITE_CALL_LOG;
    
    public static final int REAL_PHONE_CALL = 1;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        
        if (!cordova.hasPermission(READ_CALL_LOG)) {
            cordova.requestPermission(this, READ_CALL_LOG_REQ_CODE,
                    READ_CALL_LOG);
        }
        if (!cordova.hasPermission(WRITE_CALL_LOG)) {
            cordova.requestPermission(this, WRITE_CALL_LOG_REQ_CODE,
                    WRITE_CALL_LOG);   
        }
        
        cordova.requestPermission(this, REAL_PHONE_CALL, Manifest.permission.CALL_PHONE);
        
        prepareListener();

        listener.setCallbackContext(callbackContext);

        return true;
    }

    private void prepareListener() {
        if (listener == null) {
            listener = new CallStateListener();
            TelephonyManager TelephonyMgr = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            TelephonyMgr.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}

class CallStateListener extends PhoneStateListener {

    private CallbackContext callbackContext;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        if (callbackContext == null) return;

        String msg = "";

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
            msg = "IDLE";
            break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
            msg = "OFFHOOK";
            break;

            case TelephonyManager.CALL_STATE_RINGING:
            msg = "RINGING";
            break;
        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, incomingNumber);
        result.setKeepCallback(true);

        callbackContext.sendPluginResult(result);
    }
}
