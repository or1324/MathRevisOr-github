package or.nevet.math_revisor.helpers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.math.BigInteger;

import or.nevet.math_revisor.R;
import or.nevet.math_revisor.main.AboutActivity;
import or.nevet.math_revisor.main.CustomExerciseCreationActivity;
import or.nevet.math_revisor.main.LeaderboardActivity;
import or.nevet.math_revisor.main.MainActivity;
import or.nevet.math_revisor.main.ManageCustomExercisesActivity;
import or.nevet.math_revisor.main.MultiplayerActivity;
import or.nevet.math_revisor.main.SettingsActivity;
import or.nevet.math_revisor.main.SignIn;
import or.nevet.math_revisor.main.SplashScreen;
import or.nevet.math_revisor.main.StatisticsActivity;
import or.nevet.mathrevisorusermanager.SettingsManager;
import or.nevet.mathrevisorusermanager.SettingsObject;
import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager.LearnedExerciseManager;
import or.nevet.orexercises.main.ExerciseMainOperations;
import or.nevet.orgeneralhelpers.CommonArrayAlgorithms;
import or.nevet.orgeneralhelpers.Email;
import or.nevet.orgeneralhelpers.LoadingHelper;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;
import or.nevet.orgeneralhelpers.graphical.UserMessages;
import or.nevet.orgeneralhelpers.music_related.ActivityOpeningHelper;

public class AppGraphics {
    public static void initMain(MainActivity activity) {
        activity.multiply_button = activity.findViewById(R.id.multiply_button);
        activity.divide_button = activity.findViewById(R.id.divide_button);
        activity.settings = activity.findViewById(R.id.Settings);
        activity.stats = activity.findViewById(R.id.stats);
        activity.leaderboard = activity.findViewById(R.id.leaderboard);
        activity.music = activity.findViewById(R.id.music);
        activity.blessing = activity.findViewById(R.id.blessing);
        activity.navigationBar = activity.findViewById(R.id.navigation_bar);
        activity.info = activity.findViewById(R.id.info);
        activity.customExercises = activity.findViewById(R.id.custom_exercise_button);
        activity.customExercises.setOnClickListener(v -> ActivityOpeningHelper.openActivity(activity, ManageCustomExercisesActivity.class, null));
        activity.info.setOnClickListener(v -> ActivityOpeningHelper.openActivity(activity, AboutActivity.class, null));
        activity.stats.setOnClickListener(v -> ActivityOpeningHelper.openActivity(activity, StatisticsActivity.class, null));
        activity.multiplayer = activity.findViewById(R.id.multiplayer);
        activity.multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOpeningHelper.openActivity(activity, MultiplayerActivity.class, null);
            }
        });
        activity.multiply_button.setOnClickListener(v -> {
            if (activity.isTooFastDoubleClickThatWillCauseDoubleOpening())
                return;
            ExerciseMainOperations.openActivityAndSendExercises(activity, ExerciseMainOperations.ExerciseActivityOption.PracticeExerciseActivityWithScore, LearnedExerciseManager.getLearnedMulExercises(activity));
        });
        activity.divide_button.setOnClickListener(v -> {
            if (activity.isTooFastDoubleClickThatWillCauseDoubleOpening())
                return;
            ExerciseMainOperations.openActivityAndSendExercises(activity, ExerciseMainOperations.ExerciseActivityOption.PracticeExerciseActivityWithScore, LearnedExerciseManager.getLearnedDivExercises(activity));
        });

        activity.settings.setOnClickListener(v -> {
            if (activity.isTooFastDoubleClickThatWillCauseDoubleOpening())
                return;
            ActivityOpeningHelper.openActivity(activity, SettingsActivity.class, null);
        });
        activity.leaderboard.setOnClickListener(v -> ActivityOpeningHelper.openActivity(activity, LeaderboardActivity.class, null));
    }

    public static void initSplashScreen(SplashScreen splashScreen) {
        splashScreen.progressBar = splashScreen.findViewById(R.id.progressBar);
    }

    public static void initCustomExerciseCreation(CustomExerciseCreationActivity activity) {
        activity.back = activity.findViewById(R.id.back);
        activity.writeExerciseAnswers = activity.findViewById(R.id.write_exercise_answers);
        activity.instructions = activity.findViewById(R.id.instructions);
        activity.numOfLinesRight = activity.findViewById(R.id.num_of_rights);
        activity.numOfLinesLeft = activity.findViewById(R.id.num_of_lefts);
        activity.signEditText = activity.findViewById(R.id.sign);
        activity.back.setOnClickListener(activity);
        activity.writeExerciseAnswers.setOnClickListener(v -> {
            try {
                String leftLines = activity.numOfLinesLeft.getText().toString();
                String rightLines = activity.numOfLinesRight.getText().toString();
                String signText = activity.signEditText.getText().toString();
                if (signText.isEmpty() || leftLines.isEmpty() || rightLines.isEmpty()) {
                    UserMessages.showToastMessage(UserMessagesConstants.allFieldsMustBeFilled, activity);
                    return;
                }
                int numOfLeft = Integer.parseInt(activity.numOfLinesLeft.getText().toString());
                int numOfRight = Integer.parseInt(activity.numOfLinesRight.getText().toString());
                if (signText.length() > 1) {
                    UserMessages.showToastMessage(UserMessagesConstants.signNeedsToBeOneChar, activity);
                    return;
                }
                final char sign = activity.signEditText.getText().toString().charAt(0);
                try {
                    Integer.parseInt(signText);
                    UserMessages.showToastMessage(UserMessagesConstants.signCanNotBeNumber, activity);
                    return;
                } catch (Exception ignored) {

                }
                if (CommonArrayAlgorithms.arrayContainsElement(LearnedExerciseManager.getAllSavedSigns(activity), sign)) {
                    UserMessages.showToastMessage(UserMessagesConstants.thisSignAlreadyExists, activity);
                    return;
                }
                long a = (long)numOfLeft;
                long b = (long)numOfRight;
                if ((a*b) > 900L) {
                    UserMessages.showButtonDialogQuestion(UserMessagesConstants.areYouSureThatYouWantSoMuchExercises, activity, () -> activity.openExerciseCreationTable(sign, numOfLeft, numOfRight));
                    return;
                }
                activity.openExerciseCreationTable(sign, numOfLeft, numOfRight);

            } catch (Exception e) {
                e.printStackTrace();
                UserMessages.showToastMessage(UserMessagesConstants.thereWasAnError, activity);
            }
        });
    }

    public static void initManageCustomExercises(ManageCustomExercisesActivity activity) {
        activity.back = activity.findViewById(or.nevet.orexercises.R.id.back2);
        activity.back.setOnClickListener(activity);

    }

    public static void initLeaderboard(LeaderboardActivity activity) {
        activity.back = activity.findViewById(R.id.back2);
        activity.back.setOnClickListener(activity);
    }

    public static void initInfo(AboutActivity activity) {
        activity.sms = activity.findViewById(R.id.sms);
        activity.sms.setOnClickListener(v -> activity.tryToSendSMS());
        activity.back = activity.findViewById(R.id.back2);
        activity.back.setOnClickListener(activity);
    }

    public static void initSignIn(SignIn signIn) {
        if (UserManager.isTheCurrentUserSignedIn()) {
            try {
                UserManager.restoreUser(signIn);
                Intent intent = new Intent(signIn, SplashScreen.class);
                signIn.startActivity(intent);
            } catch (Exception exception) {
                UserManager.signOutAndRemoveUserFromStorage(signIn);
            }
        }
        signIn.email = signIn.findViewById(R.id.email);
        signIn.password = signIn.findViewById(R.id.password);
        signIn.userName = signIn.findViewById(R.id.userName);
        signIn.isSignInButton = signIn.findViewById(R.id.is_sign_in);
        signIn.signInButton = signIn.findViewById(R.id.sign_in_button);
        signIn.progressBar = signIn.findViewById(R.id.progressBar);
        signIn.userNameText = signIn.findViewById(R.id.username_text);
        signIn.resetPassword = signIn.findViewById(R.id.password_reset);
        signIn.isSignInButton.setOnClickListener(v -> {
            if (signIn.isSignIn) {
                signIn.isSignIn = false;
                signIn.userName.setVisibility(View.INVISIBLE);
                signIn.userName.setText("");
                signIn.resetPassword.setVisibility(View.VISIBLE);
                signIn.userNameText.setVisibility(View.INVISIBLE);
                signIn.isSignInButton.setImageDrawable(AppCompatResources.getDrawable(signIn, R.drawable.change_to_signup_button));
                signIn.signInButton.setImageDrawable(AppCompatResources.getDrawable(signIn, R.drawable.login_button));
            } else {
                signIn.isSignIn = true;
                signIn.userName.setVisibility(View.VISIBLE);
                signIn.userNameText.setVisibility(View.VISIBLE);
                signIn.resetPassword.setVisibility(View.GONE);
                signIn.isSignInButton.setImageDrawable(AppCompatResources.getDrawable(signIn, R.drawable.change_to_login_button));
                signIn.signInButton.setImageDrawable(AppCompatResources.getDrawable(signIn, R.drawable.signup_button));
            }
        });

        LoadingHelper loadingHelper = new LoadingHelper(signIn.progressBar);

        signIn.signInButton.setOnClickListener(v -> {
            String emailText = new Email(signIn.email.getText().toString().trim()).toString();
            String passwordText = signIn.password.getText().toString().trim();
            String userNameText = signIn.userName.getText().toString().trim();
            loadingHelper.loadAndRunIfAllowed(() -> {
                BackgroundRunningHelper.runCodeInBackgroundAsync(() -> {
                    try {
                        if (!CommonArrayAlgorithms.charArrayContainsAllStringCharacters(GeneralConstants.emailValidCharacters, emailText)) {
                            UserMessages.showEmptyDialogMessage(UserMessagesConstants.emailNotValidInSignIn, signIn);
                            return;
                        }
                        if (!CommonArrayAlgorithms.charArrayContainsAllStringCharacters(GeneralConstants.englishCharactersAndNumbers, userNameText)) {
                            UserMessages.showEmptyDialogMessage(UserMessagesConstants.usernameNotValid, signIn);
                            return;
                        }
                        SignInChecking.check(userNameText, emailText, passwordText, signIn.isSignIn, signIn);
                        Intent intent = new Intent(signIn, SplashScreen.class);
                        signIn.startActivity(intent);
                    } catch (FirebaseAuthUserCollisionException fe) {
                        fe.printStackTrace();
                        UserMessages.showEmptyDialogMessage(UserMessagesConstants.accountUsed, signIn);
                    } catch (FirebaseAuthWeakPasswordException pw) {
                        UserMessages.showEmptyDialogMessage(UserMessagesConstants.weakPassword, signIn);
                    } catch (FirebaseAuthInvalidCredentialsException fae) {
                        fae.printStackTrace();
                        UserMessages.showEmptyDialogMessage(UserMessagesConstants.wrongCredential, signIn);
                    } catch (FirebaseNetworkException e) {
                        e.printStackTrace();
                        UserMessages.showEmptyDialogMessage(UserMessagesConstants.youHaveNoInternet, signIn);
                    } catch (FirebaseAuthInvalidUserException fau) {
                        fau.printStackTrace();
                        UserMessages.showEmptyDialogMessage(UserMessagesConstants.wrongEmail, signIn);
                    } catch (RuntimeException r) {
                        r.printStackTrace();
                        if (UserMessagesConstants.notSignedUp.equals(r.getMessage()))
                            UserMessages.showEmptyDialogMessage(UserMessagesConstants.youAreNotSignedUp, signIn);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        loadingHelper.finishedRunning();
                    }
                });
            });
        });

        signIn.resetPassword.setOnClickListener(v -> {
                String email = new Email(signIn.email.getText().toString()).toString();
                loadingHelper.loadAndRunIfAllowed(() -> {
                    BackgroundRunningHelper.runCodeInBackgroundAsync(() -> {
                        try {
                            SignInChecking.checkEmailEmpty(email, signIn);
                            UserManager.sendPasswordResetEmail(email);
                            UserMessages.showEmptyDialogMessage(UserMessagesConstants.passwordResetSucceeded, signIn);
                        }catch (FirebaseNetworkException e) {
                            e.printStackTrace();
                            UserMessages.showEmptyDialogMessage(UserMessagesConstants.youHaveNoInternet, signIn);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        } finally {
                            loadingHelper.finishedRunning();
                        }
                    });
                });
        });
    }

    public static void initSettings(SettingsActivity settingsActivity) {
        settingsActivity.back = settingsActivity.findViewById(R.id.back2);
        settingsActivity.logout = settingsActivity.findViewById(R.id.logout);
        settingsActivity.practiceMode = settingsActivity.findViewById(R.id.practice_mode_button);
        settingsActivity.exercisesList = settingsActivity.findViewById(R.id.exercises_list);
        changePracticeModeButtonIconByPracticeModeState(settingsActivity.practiceMode, settingsActivity);
        settingsActivity.practiceMode.setOnClickListener(v -> {
            SettingsManager.switchIsLockScreenModeOnAndSave(settingsActivity);
            changePracticeModeButtonIconByPracticeModeState(settingsActivity.practiceMode, settingsActivity);
        });
        settingsActivity.logout.setOnClickListener(v -> {
            UserManager.signOutAndRemoveUserFromStorage(settingsActivity);
            ActivityOpeningHelper.openActivity(settingsActivity, SignIn.class, null);
        });
        Character[] signs = LearnedExerciseManager.getAllSavedSigpaString[] signsStringArray = new String[signs.length];
        signsStringArray[0] = String.valueOf(SettingsManager.getSettingsObject().getLockScreenExercisesSign());
        int currentIndex = 1;
        for (Character sign : signs)
            if (sign != SettingsManager.getSettingsObject().getLockScreenExercisesSign())
                signsStringArray[currentIndex++] = String.valueOf(sign);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(settingsActivity, R.layout.exercise_dropdown_item, signsStringArray);
        settingsActivity.exercisesList.setAdapter(arrayAdapter);
        settingsActivity.exercisesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //the updates the current exercise
                SettingsManager.saveLockScreenExerciseSign(parent.getItemAtPosition(position).toString().charAt(0), settingsActivity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        settingsActivity.back.setOnClickListener(settingsActivity);
    }

    public static void changePracticeModeButtonIconByPracticeModeState(AppCompatImageButton imageButton, Context context) {
        SettingsObject object = SettingsManager.getSettingsObject();
        if (object.isLockScreenModeOn()) {
            imageButton.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.lock_screen_mode_on));
        } else
            imageButton.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.lock_screen_mode_off));
    }

    public static void initStatistics(StatisticsActivity activity) {
        activity.openTableMul = activity.findViewById(R.id.multiply_stats);
        activity.openTableDiv = activity.findViewById(R.id.divide_stats);
        activity.back = activity.findViewById(R.id.back2);
        activity.percentsDiv = activity.findViewById(R.id.div_percents);
        activity.percentsMul = activity.findViewById(R.id.mul_percents);
        activity.openTableMul.setOnClickListener(view -> ExerciseMainOperations.openActivityAndSendExercises(activity, ExerciseMainOperations.ExerciseActivityOption.ExerciseStatsTableActivity, LearnedExerciseManager.getLearnedMulExercises(activity)));
        activity.openTableDiv.setOnClickListener(view -> ExerciseMainOperations.openActivityAndSendExercises(activity, ExerciseMainOperations.ExerciseActivityOption.ExerciseStatsTableActivity, LearnedExerciseManager.getLearnedDivExercises(activity)));
        activity.back.setOnClickListener(activity);
    }
}
