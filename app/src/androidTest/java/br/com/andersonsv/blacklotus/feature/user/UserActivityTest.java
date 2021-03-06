package br.com.andersonsv.blacklotus.feature.user;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.BaseActivityTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.andersonsv.blacklotus.util.ConstantsTest.TEXT_MSG_EMAIL;
import static br.com.andersonsv.blacklotus.util.ConstantsTest.TEXT_MSG_PASSWORD_CONFIRMATION;
import static br.com.andersonsv.blacklotus.util.ConstantsTest.TEXT_MSG_REQUIRED;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserActivityTest extends BaseActivityTest {

    @Rule
    public final ActivityTestRule<UserActivity> mActivityTestRule = new ActivityTestRule<>(UserActivity.class);

    @Test
    public void whenInputsAreEmpty_andClickOnSignUp_shouldDisplayErrors() {

        String msgEmail = TEXT_MSG_REQUIRED.concat("\n").concat(TEXT_MSG_EMAIL);
        String msgPassword = TEXT_MSG_REQUIRED.concat("\n").concat(mActivityTestRule.getActivity().getResources().getString(R.string.login_auth_password_error));

        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.textInputLayoutName)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_REQUIRED)));
        onView(withId(R.id.textInputLayoutEmail)).check(matches(hasTextInputLayoutHintText(msgEmail)));
        onView(withId(R.id.textInputLayoutPassword)).check(matches(hasTextInputLayoutHintText(msgPassword)));
        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_REQUIRED)));
    }

    @Test
    public void whenNameIsOkAndOthersInputsAreEmpty_andClickOnSignUp_shouldDisplayErrors() {

        String msgEmail = TEXT_MSG_REQUIRED.concat("\n").concat(TEXT_MSG_EMAIL);
        String msgPassword = TEXT_MSG_REQUIRED.concat("\n").concat(mActivityTestRule.getActivity().getResources().getString(R.string.login_auth_password_error));

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.textInputLayoutEmail)).check(matches(hasTextInputLayoutHintText(msgEmail)));
        onView(withId(R.id.textInputLayoutPassword)).check(matches(hasTextInputLayoutHintText(msgPassword)));
        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_REQUIRED)));
    }

    @Test
    public void whenNameIsOkAndEmailIsInvalidAndOthersInputsAreEmpty_shouldDisplayErrors() {

        String msgPassword = TEXT_MSG_REQUIRED.concat("\n").concat(mActivityTestRule.getActivity().getResources().getString(R.string.login_auth_password_error));

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("aaaaa"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.textInputLayoutEmail)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_EMAIL)));
        onView(withId(R.id.textInputLayoutPassword)).check(matches(hasTextInputLayoutHintText(msgPassword)));
        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_REQUIRED)));
    }

    @Test
    public void whenNameAndEmailAreOkAndOthersInputsAreEmpty_andClickOnSignUp_shouldDisplayErrors() {

        String msgPassword = mActivityTestRule.getActivity().getResources().getString(R.string.login_auth_password_error);
        String msgPasswordConfirmation = TEXT_MSG_REQUIRED.concat("\n").concat(TEXT_MSG_PASSWORD_CONFIRMATION);

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("aaaa@aaa.com"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPassword)).perform(typeText("12345"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.textInputLayoutPassword)).check(matches(hasTextInputLayoutHintText(msgPassword)));
        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(msgPasswordConfirmation)));
    }

    @Test
    public void whenNameAndEmailPsswordAreOkAndConfirmationPasswordAreEmpty_andClickOnSignUp_shouldDisplayErrors() {

        String msgPasswordConfirmation = TEXT_MSG_REQUIRED.concat("\n").concat(TEXT_MSG_PASSWORD_CONFIRMATION);

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("aaaa@aaa.com"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPassword)).perform(typeText("123456"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(msgPasswordConfirmation)));
    }

    @Test
    public void whenNameAndEmailPaswordAreOkAndConfirmationPasswordDifferentPassword_andClickOnSignUp_shouldDisplayErrors() {

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("aaaa@aaa.com"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPassword)).perform(typeText("123456"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPasswordConfirmation)).perform(typeText("12345899"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_PASSWORD_CONFIRMATION)));
    }

    @Test
    public void whenSignUpUserExists_andClickOnSignUp_shouldDisplayErrors() {

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("test@test.com"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPassword)).perform(typeText("123456"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPasswordConfirmation)).perform(typeText("123456"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());

        onView(withText(R.string.default_error_title)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

}
