package com.ryeslim.darbuosna;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.ryeslim.darbuosna.controller.ActivitySwipeDetector;
import com.ryeslim.darbuosna.model.WorkWithProverbs;

import com.ryeslim.darbuosna.model.*;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    /**
     * This app shows a random proverb about work (in Lithuanian).
     * The proverb that appears on the screen of your device as soon as you open the app
     * can become your proverb of the day
     * or you can
     * 1) go forward to read more proverbs (by using an arrow or swipe gesture);
     * 2) go backwards if you want to read some proverbs again (by using an arrow or swipe gesture);
     * 3) bookmark the ones you like by clicking on the heart icon (they will be added to the favorites list saved in a file);
     * 4) remove from bookmarks in one of two ways: "unliking" using the heart icon or clicking the "delete" button on the list;
     * 5) see the list of bookmarks;
     * 6) go to the first proverb of this session;
     * 7) go to the last proverb of this session;
     * 5) share;
     * <p>
     * When the app is turned off, only the list of bookmarks is stored.
     * Currently, there are about 900 proverbs to endlessly pick the random ones from.
     */

    int yesBookmarked;//resource id for the filled heart icon
    int notBookmarked;//resource id for the heart contour icon
    Proverb thisProverb;//the proverb on the screen
    ImageView favorite; //global ImageView for the two-state favorite (heart) icon, which can be either
    //filled (for bookmarked proverbs) or contour (not bookmarked proverbs)

    private static MainActivity instance = null;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = this.getResources();
        yesBookmarked = res.getIdentifier("ic_favorite", "drawable", this.getPackageName());
        notBookmarked = res.getIdentifier("ic_favorite_border", "drawable", this.getPackageName());

        //final Bitmap HEART_CONTOUR_ICON = BitmapFactory.decodeResource(res, R.drawable.ic_favorite_border);

        WorkWithProverbs.getInstance().setContext(this);
        MainActivity.instance = this;
        AllProverbs.getInstance().setLoadingQue(Volley.newRequestQueue(this));//downloads the list of all proverbs

        // Set a "swipe" listener on the proverb and react when clicked
        ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
        TextView swipeableProverb = findViewById(R.id.the_proverb);
        swipeableProverb.setOnTouchListener(swipe);

        // Set a click listener on the proverb and react when clicked
        TextView clickableProverb = findViewById(R.id.the_proverb);
        clickableProverb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goForward();
            }
        });

        // Find the '>' ImageView, set a click listener on it and react when clicked
        ImageView nextPage = findViewById(R.id.next_page);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goForward();
            }
        });

        // Find the '<' ImageView, set a click listener on it and react when clicked
        ImageView previousPage = findViewById(R.id.previous_page);
        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackwards();
            }
        });

        // Find the "first page" ImageView, set a click listener on it and react when clicked
        ImageView firstPage = findViewById(R.id.first_page);
        firstPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstPage();
            }
        });

        // Find the "last page" ImageView, set a click listener on it and react when clicked
        ImageView lastPage = findViewById(R.id.last_page);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPage();
            }
        });

        // Find the "favorite" ImageView, set a click listener on it and react when clicked
        favorite = findViewById(R.id.heart);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WorkWithProverbs.getInstance().isBookmarked(thisProverb.getTheID())) {
                    unbookmark();
                    favorite.setImageResource(notBookmarked);//switch to the heart contour icon
                } else {
                    bookmark();
                    favorite.setImageResource(yesBookmarked);//switch to the filled heart icon
                }
            }
        });

        // Find the "show bookmarks" ImageView, set a click listener on it and react when clicked
        ImageView showBookmarks = findViewById(R.id.show_bookmarks);
        showBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBookmarks();
            }
        });

        // Find the "share" ImageView, set a click listener on it and react when clicked
        ImageView share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });
    }

    public void goForward() {
        thisProverb = WorkWithProverbs.getInstance().getTheNext();
        show(thisProverb);
    }

    public void goBackwards() {
        thisProverb = WorkWithProverbs.getInstance().getThePrevious();
        show(thisProverb);
    }

    public void firstPage() {
        thisProverb = WorkWithProverbs.getInstance().getTheFirst();
        show(thisProverb);
    }

    public void lastPage() {
        thisProverb = WorkWithProverbs.getInstance().getTheLast();
        show(thisProverb);
    }

    public void bookmark() {
        WorkWithProverbs.getInstance().addToTheFile();//add to the file
        WorkWithProverbs.getInstance().readBookmarks();//update the listOfBookmarks array from the file
    }

    public void unbookmark() {
        WorkWithProverbs.getInstance().removeFromArray();//remove from listOfBookmarks array
        WorkWithProverbs.getInstance().saveTheUpdatedList();//save to the file
    }

    public void showBookmarks() {
        Intent intent = new Intent(this, ListOfProverbsActivity.class);
        startActivity(intent);
    }

    public void share() {
        WorkWithProverbs.getInstance().shareSomehow();
    }

    public void show(Proverb thisProverb) {

        // Every time a proverb is shown, the app reads bookmarks from the file
        // to check if this proverb has been bookmarked
        // in order to set the right heart icon
        WorkWithProverbs.getInstance().readBookmarks();

        TextView theProverb = findViewById(R.id.the_proverb);
        favorite = findViewById(R.id.heart);//global ImageView

        theProverb.setText(thisProverb.getProverb());

        if (WorkWithProverbs.getInstance().isBookmarked(thisProverb.getTheID())) {
            favorite.setImageResource(yesBookmarked);//set the filled heart
        } else {
            favorite.setImageResource(notBookmarked);//set the heart contour
        }
    }
}
