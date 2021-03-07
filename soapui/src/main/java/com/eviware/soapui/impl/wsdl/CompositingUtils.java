package com.eviware.soapui.impl.wsdl;

public class CompositingUtils {
    public static String pathProofName(String name){
        return name.replaceAll("[^a-zA-Z0-9-]","_");
    }
}
