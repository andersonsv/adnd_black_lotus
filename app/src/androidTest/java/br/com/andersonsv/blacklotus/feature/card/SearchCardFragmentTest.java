package br.com.andersonsv.blacklotus.feature.card;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;

import br.com.andersonsv.blacklotus.feature.BaseActivityTest;
import br.com.andersonsv.blacklotus.feature.base.DebugActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class SearchCardFragmentTest extends BaseActivityTest {


    @Rule
    public FragmentTestRule<DebugActivity, CardEditorFragment> fragmentTestRule =
            new FragmentTestRule<>(DebugActivity.class, CardEditorFragment.class);


    @BeforeClass
    public static void login(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword("test@test.com", "123456").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                assertThat(authResult.getUser().getUid(), notNullValue());
            }
        });
    }


    @AfterClass
    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
    }
}
