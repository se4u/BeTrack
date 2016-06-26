package com.app.uni.betrack;

import java.util.ArrayList;

/**
 * Created by cevincent on 4/24/16.
 */
public class InfoStudy {

    static public final String STUDY_STARTED = "study_started";


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
