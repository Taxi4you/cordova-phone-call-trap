package io.gvox.phonecalltrap;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import android.content.Context;
import android.Manifest;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONArray;


public class PhoneCallTrap extends CordovaPlugin {

    CallStateListener listener;
    CallbackContext myCallbackContext;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        
        myCallbackContext = callbackContext;
        
        if (!cordova.hasPermission(Manifest.permission.READ_PHONE_STATE)) {
            cordova.requestPermission(this, 0, Manifest.permission.READ_PHONE_STATE);
        } else {
            prepareListener();
            listener.setCallbackContext(callbackContext);
        }
        
        return true;
        
    }
    
    public void onRequestPermissionResult(int requestCode) {
        prepareListener();
        listener.setCallbackContext(myCallbackContext);
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

        PluginResult result = new PluginResult(PluginResult.Status.OK, "msg" + msg + " incomingNumber" + incomingNumber);
        result.setKeepCallback(true);

        callbackContext.sendPluginResult(result);
    }
}
