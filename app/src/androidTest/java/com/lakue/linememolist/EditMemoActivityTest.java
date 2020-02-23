package com.lakue.linememolist;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.lakue.linememolist.Activity.EditMemoActivity;
import com.lakue.linememolist.Module.Common;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditMemoActivityTest {
    @Rule
    public ActivityTestRule<EditMemoActivity> mActivityRule = new ActivityTestRule(EditMemoActivity.class);

    @Before
    public void setUp(){
        Intents.init();
    }

    @Test
    public void testGetExtra(){
        Intent intent = new Intent();
        intent.putExtra("type",Common.TYPE_INTENT_UPDATE);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void testEditTextInTextIsEmpty() {
        //button 을 클릭합니다.
        onView(withId(R.id.et_title)).perform(typeText("Title"));
        onView(withId(R.id.et_content)).perform(typeText("Content"), closeSoftKeyboard());

        onView(withId(R.id.et_title)).check(matches(withText("Title")));
        onView(withId(R.id.et_content)).check(matches(withText("Content")));

        onView(withId(R.id.btn_memo_insert))
                .perform(click());

        intended(allOf(
                hasExtra("type", Common.TYPE_INTENT_INSERT),
                toPackage("com.lakue.linememolist"),
                hasComponent("com.lakue.linememolist.Activity.MainActivity")));
    }

    @Test
    public void testCaseForRecyclerVlick() {
        onView(ViewMatchers.withId(R.id.rv_memo_item))
                .inRoot(RootMatchers.withDecorView(
                        is(mActivityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void onActivityForResultAlbum() {
        Intent intent = new Intent();
        intent.putExtra("result", Common.TYPE_ALBUM);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);

        intending(toPackage("com.lakue.linememolist"))
                .respondWith(result);

    }

    @Test
    public void onActivityForResultCamera() {
        Intent intent = new Intent();
        intent.putExtra("result", Common.TYPE_PHOTO);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);

        intending(toPackage("com.lakue.linememolist"))
                .respondWith(result);

    }

    @Test
    public void onActivityForResultUrl() {
        Intent intent = new Intent();
        intent.putExtra("result", Common.TYPE_URL);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);

        intending(toPackage("com.lakue.linememolist"))
                .respondWith(result);

    }

    @After
    public void cleanUp() {
        Intents.release();
    }
}
