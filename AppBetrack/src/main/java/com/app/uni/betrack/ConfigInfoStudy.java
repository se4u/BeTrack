package com.app.uni.betrack;

import java.util.ArrayList;

/**
 * Created by cevincent on 4/24/16.
 */
public class ConfigInfoStudy {

    static public final String STUDY_STARTED = "study_started";
    static public final String APP_NAME_TO_WATCH = "AppNameToWatch";
    static public final String ID_USER = "IdUser";
    static public final String STUDY_DESCRIPTION = "StudyDescription";

    static public String IdUser; //Unique ID for this user
    static public String StudyDescription; //Description of the study going on

    public Boolean StudyStarted; //A study is started

    public String StudyId;
    public String StudyName;
    public String StudyVersionApp;
    public String StudyDuration;
    public String StudyPublicKey;
    public String StudyContactEmail;
    public String StudyLinkEndStudy;
    ArrayList<String> ApplicationsToWatch = new ArrayList<String>();
}
