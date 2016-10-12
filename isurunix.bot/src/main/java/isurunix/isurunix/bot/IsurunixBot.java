package isurunix.isurunix.bot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.FilterQuery;
import twitter4j.ResponseList;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

/**
 * Hello world!
 *
 */
public class IsurunixBot 
{
	private final String CONSUMER_KEY = "f0MyqzTYfUuKw4k54yBDevAMj";
    private final String CONSUMER_KEY_SECRET = "V0C3C6bS7nzyUXAj04N9rq6uiXaoj3nmo2rtK7JBtNdOA9cTtO";

    private final String ACCESS_TOKEN = "162311701-a5PAcjH0YvA2QZUr3m1FkNKMPP1aurvwYkULXuvh";
    private final String ACCESS_TOKEN_SECRET = "OYmUzq9dKVfxPPHKXSYmKwAcTKARCKtjsToi2xlrbwk4D";

    private Twitter isurunix;
    private AccessToken accToken;

    private File logFile;
    private BufferedWriter logFileWriter;

    public IsurunixBot() {
    	
    	//get TwitterFactory() instance
        this.isurunix = new TwitterFactory().getInstance();
        
        //create log file
        logFile = new File("./log");
        try {
            logFileWriter = new BufferedWriter(new FileWriter(logFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IsurunixBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IsurunixBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //start bot
        try {
            this.start();
        } catch (TwitterException e) {
            try {
                logFileWriter.append(e.getMessage());
                logFileWriter.flush();
            } catch (IOException ex) {
                Logger.getLogger(IsurunixBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void start() throws TwitterException {
        this.isurunix.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        accToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
        this.isurunix.setOAuthAccessToken(accToken);
    }

    public void readTimeLine() throws TwitterException {
        //Reading timeline
        ResponseList<Status> homeList = this.isurunix.getHomeTimeline();

        for (Status status : homeList) {
            System.out.println("Sent by: @" + status.getUser().getScreenName()
                    + " - " + status.getUser().getName() + "\n" + status.getText()
                    + "\n");
        }
    }

    /*
     * @param twitterID Twitter ID of the account to retweet
     * Retweet tweets from the account with the given twitter ID
     */
    public void retweetByID(long twitterID) {
    	
    	//get twitter stream
        final TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        
        //authenticate
        twitterStream.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        twitterStream.setOAuthAccessToken(accToken);

        //listen to status
        StatusListener statListener = new StatusListener() {

            public void onException(Exception ex) {
                ex.printStackTrace();
            }

            public void onTrackLimitationNotice(int arg0) {
            }
            
            //retweet when a status update is made
            public void onStatus(Status status) {
                try {
                    //StatusUpdate statusUpdate = new StatusUpdate(status.getText());
                    ///isurunix.updateStatus(statusUpdate);
                    System.out.println(status.getText() + " : @" + status.getUser().getScreenName());
                    if (!status.isRetweet()) {
                        isurunix.retweetStatus(status.getId());
                    }
                } catch (TwitterException e) {
                    try {
                        logFileWriter.append(e.getMessage());
                        logFileWriter.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(IsurunixBot.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            public void onStallWarning(StallWarning arg0) {
            }

            public void onScrubGeo(long arg0, long arg1) {
            }

            public void onDeletionNotice(StatusDeletionNotice arg0) {
            }
        };

        twitterStream.addListener(statListener);
        twitterStream.filter(new FilterQuery().follow(new long[]{twitterID}));
    }

    /*
     * @param hashses String array of hashtags
     * Retweet every tweet containing the hashtags in the given array
     */
    public void retweetByHash(String[] hashes) {
    	
    	//connect to twitter stream
        final TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        
        //authenticate
        twitterStream.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        twitterStream.setOAuthAccessToken(accToken);

        //listen
        StatusListener statListener = new StatusListener() {

            public void onException(Exception ex) {
                ex.printStackTrace();
            }

            public void onTrackLimitationNotice(int arg0) {
            }
            
            //on new status
            public void onStatus(Status status) {
                try {
                    String stat = status.getText();
                    
                    //retweet is it's not already a retweet
                    //prevents retweeing the same tweet over and over
                    if (!(status.isRetweet())) {
                        if ("en".equals(status.getLang())) {
                            System.out.println(status.getText() + " : @" + status.getUser().getScreenName());
                            logFileWriter.append(stat);
                            logFileWriter.flush();
                            //isurunix.retweetStatus(status.getId());
                        }
                    }
                    
//                } catch (TwitterException e) {
//                    try {
//                        logFileWriter.append(e.getMessage());
//                    } catch (IOException ex) {
//                        Logger.getLogger(IsurunixBot.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                } catch (IOException ex) {
                    Logger.getLogger(IsurunixBot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            public void onStallWarning(StallWarning arg0) {
            }

            public void onScrubGeo(long arg0, long arg1) {
            }

            public void onDeletionNotice(StatusDeletionNotice arg0) {
            }
        };

        twitterStream.addListener(statListener);
        twitterStream.filter(new FilterQuery().track(hashes));
    }
}
