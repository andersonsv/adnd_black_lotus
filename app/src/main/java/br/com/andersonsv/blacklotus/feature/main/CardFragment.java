package br.com.andersonsv.blacklotus.feature.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.stream.Collectors;

import br.com.andersonsv.blacklotus.BuildConfig;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.adapter.CardAdapter;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.firebase.CardModel;
import br.com.andersonsv.blacklotus.firebase.DeckModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.andersonsv.blacklotus.util.Constants.CARD_LAND;
import static br.com.andersonsv.blacklotus.util.Constants.CARD_LIST;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_ID;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;

public class CardFragment extends BaseFragment {

    private String deckId;
    private DeckModel deck;

    private FirebaseUser mUser;
    private FirebaseFirestore mDb;
    private String mUserUid;
    private FirestoreRecyclerAdapter mLandAdapter;
    private FirestoreRecyclerAdapter mCardAdapter;

    @BindView(R.id.deckName)
    TextView mDeckName;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.linearLayoutEmptyState)
    LinearLayout mEmptyState;

    @BindView(R.id.recyclerViewCardLand)
    RecyclerView mCardLandRecycler;

    @BindView(R.id.recyclerViewCard)
    RecyclerView mCardRecycler;

    public static CardFragment newInstance() {
        return new CardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card, container, false);

        ButterKnife.bind(this, rootView);

      //  ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            deckId = bundle.getString(DECK_ID, "");
            deck = bundle.getParcelable(DECK_PARCELABLE);

             mDeckName.setText(deck.getName());
        }

        mDb = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null){
            mUserUid = mUser.getUid();
        } else {

            //snack
            //TODO - usuario nao identificado, escrever mensagem
        }

        getLandList();
        getCardList();
        setHasOptionsMenu(true);

        return rootView;
    }

    private void getCardList() {
        setLinearLayoutVerticalWithDivider(mCardRecycler);

        Query query = mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection(CARD_LIST)
                .whereEqualTo(DECK_ID, deckId)
                .whereEqualTo(CARD_LAND, false);

        FirestoreRecyclerOptions<CardModel> response = new FirestoreRecyclerOptions.Builder<CardModel>()
                .setQuery(query, CardModel.class)
                .build();

        mCardAdapter = new CardAdapter(getContext(), response, mProgressBar, mEmptyState, this.getFragmentManager().beginTransaction());
        mCardAdapter.notifyDataSetChanged();
        mCardRecycler.setAdapter(mCardAdapter);
    }

    private void getLandList() {
        setLinearLayoutVerticalWithDivider(mCardLandRecycler);

        Query query = mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection(CARD_LIST)
                .whereEqualTo(DECK_ID, deckId)
                .whereEqualTo(CARD_LAND, true);

        FirestoreRecyclerOptions<CardModel> response = new FirestoreRecyclerOptions.Builder<CardModel>()
                .setQuery(query, CardModel.class)
                .build();

        mLandAdapter = new CardAdapter(getContext(), response, mProgressBar, mEmptyState, this.getFragmentManager().beginTransaction());
        mLandAdapter.notifyDataSetChanged();
        mCardLandRecycler.setAdapter(mLandAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLandAdapter.startListening();
        mCardAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLandAdapter.stopListening();
        mCardAdapter.stopListening();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.deck_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @OnClick(R.id.fabAddCard)
    public void addCard(View view){
        Fragment searchCardFragment = SearchCardFragment.newInstance();

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, searchCardFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.deck_share:


                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}