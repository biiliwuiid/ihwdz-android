package com.ihwdz.android.hwslimcore.ErrorHandler;

import android.content.Context;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwslimcore.API.BaseSlimAPI;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class SlimAppError {

    public int error;
    public String errorString;
    public void setError(int err){error = err;}
    public int getError(){return  error;}
    int innerErrorCode = -1;
    public int getInnerErrorCode(){
        return innerErrorCode;
    }
    public static SlimAppError create(int err)
    {
        SlimAppError appErr = new SlimAppError();
        appErr.error = err;
        return  appErr;
    }
    public static SlimAppError customError(String errorString)
    {
        SlimAppError appErr = new SlimAppError();
        appErr.error = CUSTOM_ERROR;
        appErr.errorString = errorString;
        return  appErr;
    }
    public static  SlimAppError createFromWebAPIError(BaseSlimAPI.SlimAPIError apiErr)
    {
        SlimAppError appErr = new SlimAppError();

        if(!apiErr.hasError()) {
            appErr.error = OK;
        }
        else {
            if(apiErr.getHttpError() != 200)
            {
                appErr.innerErrorCode = apiErr.getHttpError();
                // http error
                switch (apiErr.getHttpError()) {
                    case 400:appErr.error = BAD_REQUEST;break;
                    case 403:appErr.error = FORBIDDEN;break;
                    case 404:appErr.error = RESOURCE_NOT_FOUND;break;
                    case 405:appErr.error = METHOD_NOT_ALLOW;break;
                    case 416:appErr.error = INVALID_PARAMETERS;break;
                    case 420:appErr.error = METHOD_FAILURE;break;
                    case 423:appErr.error = LOCKED;break;
                    case 501:appErr.error = NOT_IMPLEMENTED;break;
                    case 522:appErr.error = TIMEOUT;break;
                    default:appErr.error = UNKNOWN_ERROR;break;
                }
            }
            else if(apiErr.getConnectionError() != 0)
            {
                switch (apiErr.getConnectionError())
                {
                    case 1:appErr.error = NO_INTERNET_CONNECTION;break;
                    case 2:appErr.error = TIMEOUT;break;
                }
            }
            else
            {
                // scanner error
                switch (apiErr.getLocalError())
                {
                    case 1:appErr.error = STORAGE_ERROR;break;
                    case 2:appErr.error = INVALID_RESPONSE;break;
                    case 3:appErr.error = DATABASE_ERROR;break;
                    default:appErr.error = UNKNOWN_ERROR;break;
                }
            }
        }
        return  appErr;
    }

    public Boolean isConnectionError()
    {
        if(error == TIMEOUT || error == NO_INTERNET_CONNECTION)
        {
            return true;
        }
        return false;
    }

    //region error codes

    public static final int OK = 0;

    // http error
    public static final int BAD_REQUEST = 100;
    public static final int FORBIDDEN = 101;
    public static final int METHOD_NOT_ALLOW = 102;
    public static final int INVALID_PARAMETERS = 103;
    public static final int METHOD_FAILURE = 104;
    public static final int LOCKED = 105;
    public static final int NOT_IMPLEMENTED = 106;
    public static final int TIMEOUT = 107;
    public static final int RESOURCE_NOT_FOUND = 108;

    // User Input error
    public static final int INVALID_URL = 200;
    public static final int INVALID_CREDENTIAL = 201;
    public static final int LOGIN_CANCELLED = 202;
    public static final int HTTP_URL_NOT_SUPPORTED = 203;

    //Local Error
    public static final int STORAGE_ERROR = 300;  // local error = 1, error occur in storage operation.
    public static final int INVALID_RESPONSE = 301;  // local error = 2, Invalid response from scanner.
    public static final int DATABASE_ERROR = 302;  // local error = 3, error occur in database operation.
    public static final int IMAGE_BORDER_CALCULATE_ERROR = 303;
    public static final int IMAGE_STRETCH_ERROR = 304;
    public static final int OPERATION_NOT_SUPPORTED = 305;
    public static final int OPERATION_CANCELLED = 306;
    //NO INTERNET Error

    public static final int NO_INTERNET_CONNECTION = 400;  // No network connection.

    //Slim Error
    public static final int NOT_SLIM_URL = 501;
    public static final int NO_LICENSE = 502;

    //Generic Error
    public static final int CUSTOM_ERROR = 800;  // http code = 520 and other errors

    //Generic Error
    public static final int UNKNOWN_ERROR = 1000;  // http code = 520 and other errors

    //endregion
    public String getLocalizedString(Context context){
        switch (error){
            case FORBIDDEN:
            case INVALID_URL:
            case INVALID_CREDENTIAL:
            case TIMEOUT:
            case RESOURCE_NOT_FOUND:
            case HTTP_URL_NOT_SUPPORTED:
            case NO_INTERNET_CONNECTION:
                return context.getString(R.string.error_connection);
            case NOT_SLIM_URL:
                return context.getString(R.string.error_invalid_site);
            case NO_LICENSE:
                return context.getString(R.string.error_no_license);
            case CUSTOM_ERROR:
                return errorString;
            case LOGIN_CANCELLED:
            default:
                return context.getString(R.string.error_generic_error, error);
        }
    }
}
