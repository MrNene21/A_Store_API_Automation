package org.astore.Endpoints;

import org.astore.Utilities.ConfigReader;

public class EndPoints {
    public static final String BASE_URI = ConfigReader.getBaseURI();
    public static final String ADMIN_REGISTER = ConfigReader.getAdminRegisterEndPoint();
    public static final String ADMIN_LOGIN = ConfigReader.getAdminLoginEndPoint();
    public static final String ADMIN_USER_DETAILS = ConfigReader.getAdminUserDetails();
}