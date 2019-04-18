package com.buel.holyhelper.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.TutorialViewerUtil;
import com.buel.holyhelper.data.UserType;
import com.buel.holyhelper.model.UserModel;
import com.buel.holyhelper.view.DataTypeListener;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.logger.LoggerHelper;

import androidx.annotation.NonNull;
import io.fabric.sdk.android.Fabric;

/**
 * 로그인
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 10;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoggerHelper.i("start LoginActivity");

        Fabric.with(this,new Crashlytics());

        findViewById(R.id.helper_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TutorialViewerUtil.getCreateAccountTutorialModels(LoginActivity.this);
            }
        });

        //throw new RuntimeException("This is a crash");

        setUI();
    }

    private void setUI() {
        LoggerHelper.i("setUI");

        findViewById(R.id.loginActivity_button_signup).setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //  로그인 인터페이스 리스너
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            CommonData.setFirebaseUser(user);
            showProgressDialog(false);
            if (user != null) {
                //로그인 성공
                checkedUserData();
            } else {
                //로그아웃
                popToast("로그아웃 되었습니다.");
            }
        };

        showProgressDialog(true);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        CommonData.setGoogleSignInClient(mGoogleSignInClient);

        try {
            Intent intent = getIntent();
            String login = intent.getExtras().getString("login");
            if (login.equals("out")) {
                CommonData.setInitCommonSettings();
                revokeAccess();
            }
        } catch (Exception e) {
            LoggerHelper.d("앱스타트");
        }
    }

    private void checkedUserData() {
        LoggerHelper.i("checkedUserData");

        showProgressDialog(true);
        final String uid = FirebaseAuth.getInstance().getUid();

        FDDatabaseHelper.INSTANCE.getUserData(uid, new DataTypeListener.OnCompleteListener<UserModel>() {
                    @Override
                    public void onComplete(UserModel userModel) {
                        checkUserDataAndLogin(userModel,uid);
                    }
                });
    }

    public void checkUserDataAndLogin(UserModel userModel ,String uid) {
        LoggerHelper.d("onDataChange");

        //회원 정보가 없는경우 : 신규 회원
        if (userModel == null) {
            showProgressDialog(false);
            CommonData.setInitCommonSettings();
            //goJoin();
            goAgreement();
            return;
        }

        if (uid.equals(userModel.uid)) {
            CommonData.setUserModel(userModel);
            LoggerHelper.d(userModel.toString());

            //토큰을 저장한다.
            FDDatabaseHelper.INSTANCE.sendUserDataAddDataMap(userModel.uid, "pushToken" , FirebaseInstanceId.getInstance().getToken());

            try {
                if (userModel.userType.equals(UserType.SUPER_ADMIN.toString())) {
                    CommonData.setAdminUid(FirebaseAuth.getInstance().getUid());
                }else{
                    CommonData.setAdminUid(userModel.adminUID);
                }

                if (userModel.userType.equals("SUPER_ADMIN")) {
                    CommonData.setMemberShipType(UserType.SUPER_ADMIN);
                } else if (userModel.userType.equals("SUB_ADMIN")) {
                    CommonData.setMemberShipType(UserType.SUB_ADMIN);
                } else if (userModel.userType.equals("PERSONAL")) {
                    CommonData.setMemberShipType(UserType.PERSONAL);
                }
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                return;
            }
            showProgressDialog(false);
            goMain();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginActivity_button_signup:
                signIn();
                break;
            //로그인 버튼 클릭
            case R.id.top_bar_btn_back:
                MaterialDailogUtil.simpleYesNoDialog(v.getContext(), "종료하시겠습니까?",
                        new MaterialDailogUtil.OnDialogSelectListner() {
                            @Override
                            public void onSelect(String s) {
                                finish();
                            }
                        });
                break;
        }
    }

    /**
     * 구글 인증 관련
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        firebaseAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        firebaseAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //updateUI(null);
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        showProgressDialog(true);
        Log.d(Common.PACKAGE_NAME, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(Common.PACKAGE_NAME, "signInWithCredential:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(Common.PACKAGE_NAME, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(Common.PACKAGE_NAME, "Google sign in failed", e);
                //
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
