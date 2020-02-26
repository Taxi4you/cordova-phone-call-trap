var PhoneCallTrap = {
    
    requestPermission: function(successCallback, errorCallback) {
        errorCallback = errorCallback || this.errorCallback;
        cordova.requestPermission(successCallback, errorCallback, 'PhoneCallTrap', 'requestPermission', []);
    },
    
    onCall: function(successCallback, errorCallback) {
        errorCallback = errorCallback || this.errorCallback;
        cordova.exec(successCallback, errorCallback, 'PhoneCallTrap', 'onCall', []);
    },

    errorCallback: function() {
        console.log("WARNING: PhoneCallTrap errorCallback not implemented");
    }
};

module.exports = PhoneCallTrap;
