package com.lakue.linememolist;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.lakue.linememolist.Activity.MainActivity;
import com.lakue.linememolist.Module.Common;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule(MainActivity.class);

    @Test
    public void listGoesOverTheFold(){
        //글 작성 버튼 클릭
        //넘어가는 액티비티에 데이터를 뿌려줌

        onView(withId(R.id.btn_edit_move))
                .perform(click());

        intended(allOf(
                hasExtra("type", Common.TYPE_INTENT_INSERT),
                toPackage("com.lakue.linememolist"),
                hasComponent("com.lakue.linememolist.Activity.EditMemoActivity")));


    }

    @Test
    public void onActivityForResult() {
        Intent intent = new Intent();
        intent.putExtra("result", Common.REQUEST_IMAGE_TYPE);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);

        intending(toPackage("com.lakue.linememolist"))
                .respondWith(result);

    }

    @Test
    public void testCaseForRecyclerVlick(){
        // 메모리스트 recyclerview 아이템 클릭
        onView(ViewMatchers.withId(R.id.rv_memolist))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mActivityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
}
