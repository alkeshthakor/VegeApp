package com.sabziatdoor.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpAddressValidator
{
    private Pattern mPattern;
    private Matcher mMatcher;

    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    /**
     * Constructor for this class
     */
    public IpAddressValidator()
    {
        mPattern = Pattern.compile(IPADDRESS_PATTERN);
    }

    /**
     * validate IP address
     * 
     * @param ipAddress
     *            ipaddress to be validate
     * @return true or false
     */
    public boolean validate(final String ipAddress)
    {
        mMatcher = mPattern.matcher(ipAddress);
        return mMatcher.matches();
    }
}
