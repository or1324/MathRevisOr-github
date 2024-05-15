package or.nevet.multiplayergame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;

import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExerciseData;
import or.nevet.multiplayergame.data_objects.GameUser;
import or.nevet.multiplayergame.data_objects.User;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.multiplayergame.game_listeners.OrReadyUsersListener;
import or.nevet.orcloud.CloudFieldNamingHelper;
import or.nevet.orcloud.RealtimeField;
import or.nevet.orcloud.RealtimeHelper;
import or.nevet.orcloud.cloud_listeners.OrChildAddedListener;
import or.nevet.orcloud.cloud_listeners.OrNChildrenAddedListener;
import or.nevet.orcloud.cloud_listeners.OrValueChangedListener;
import or.nevet.orcloud.listener_stoppers.ChildEventListenerStopper;
import or.nevet.orcloud.listener_stoppers.ValueEventListenerStopper;
import or.nevet.orgeneralhelpers.constants.CloudConstants;

class GameOperationLowLevelStages {
    static void signalToTheOtherUsersThatTheMatchingStageWasFinished(String gameRoom, String myIdentifier, Users usersWithMe) {
        HashSet<RealtimeField> userReadyStates = new HashSet<>();
        for (User user : usersWithMe.getUsers()) {
            String identifier = CloudFieldNamingHelper.replaceDotsWithSpaces(user.getIdentifier());
            if (!identifier.equals(myIdentifier))
                userReadyStates.add(new RealtimeField(gameRoom + CloudConstants.cloudLocationSeparator + CloudConstants.readyUsersInMatchingUserGameSubCollection + CloudConstants.cloudLocationSeparator + identifier, true));
        }
        RealtimeHelper.updateMultipleFieldsNoOrderSync(userReadyStates);
    }

    static void initializeUserGameRoomForGame(String gameRoom, Users users) {
        CountDownLatch latch = new CountDownLatch(users.getUsers().length+1);
        //initialize scores
        HashSet<RealtimeField> scores = new HashSet<>();
        for (User user : users.getUsers())
            scores.add(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.scoresOfUsersUserGameSubCollection+CloudConstants.cloudLocationSeparator+ CloudFieldNamingHelper.replaceDotsWithSpaces(user.getIdentifier()), 0));
        for (RealtimeField field : scores)
            RealtimeHelper.RealtimeHelperAsync.updateRealtimeFieldAndDoSomethingWhenCompletedAsync(field, () -> latch.countDown());
        //remove matching ready collection
        RealtimeHelper.RealtimeHelperAsync.removeRealtimeFieldAndDoSomethingWhenCompletedAsync(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.readyUsersDuringGameUserGameSubCollection, () -> latch.countDown());
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static void initializeUsersScores(String gameRoom, String[] userIdentifiers) {
        HashSet<RealtimeField> fields = new HashSet<>();
        for (String user : userIdentifiers)
            fields.add(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.scoresOfUsersUserGameSubCollection+CloudConstants.cloudLocationSeparator+ CloudFieldNamingHelper.replaceDotsWithSpaces(user), 0));
        RealtimeHelper.updateMultipleFieldsNoOrderSync(fields);
    }

    static void initializeUsersReadyStatesDuringGame(String gameRoom) {
        RealtimeHelper.removeRealtimeFieldAtLocationSync(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.readyUsersDuringGameUserGameSubCollection);
    }

    static void initializeUsersReadyStatesDuringMatching(String gameRoom) {
        RealtimeHelper.removeRealtimeFieldAtLocationSync(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.readyUsersDuringGameUserGameSubCollection);
    }

    static void waitForAllOfTheUsersToJoinTheRoomAsOwnerAsync(String gameRoom, String myIdentifier, int numOfPeopleAllowedIncludingMe, OrReadyUsersListener onUsersJoined) {
        //including my Identifier, but my Identifier will not be shown
        RealtimeHelper.RealtimeHelperAsync.waitForNChildrenToBeInTheCollectionAndThenDoSomethingAsync(gameRoom + CloudConstants.cloudLocationSeparator + CloudConstants.readyUsersInMatchingUserGameSubCollection, new OrNChildrenAddedListener() {
            @Override
            public void onAllAdded(RealtimeField[] fields) {
                User[] userIdentifiers = new User[fields.length-1];
                int currentIndex = 0;
                for (int i = 0; i < fields.length; i++)
                    if (!fields[i].getLastLocationPathPart().equals(myIdentifier))
                        userIdentifiers[currentIndex++] = new User(CloudFieldNamingHelper.restoreDotsAfterReplacementWithSpaces(fields[i].getLastLocationPathPart()));
                onUsersJoined.onAllUsersReady(new Users(userIdentifiers));
            }

            //does not show the users before ending because they do not see the users who joined and it is not fair that the owner will see. Also, I am too lazy to make a difference between them.
            @Override
            public void onChildAdded(RealtimeField field) {
//                String Identifier = CloudFieldNamingHelper.restoreDotsAfterReplacementWithSpaces(field.getLastLocationPathPart());
//                if (!Identifier.equals(CloudFieldNamingHelper.restoreDotsAfterReplacementWithSpaces(myIdentifier)))
//                    onUsersJoined.onUserReady(Identifier);
            }
        }, numOfPeopleAllowedIncludingMe);
    }

    static ArrayList<String> getJoinedUsers(String gameRoom) {
        RealtimeField[] children = RealtimeHelper.getRealtimeFieldChildrenValuesAtLocationSync(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.readyUsersInMatchingUserGameSubCollection);
        ArrayList<String> joinedUsers = new ArrayList<>();
        for (RealtimeField field : children)
            joinedUsers.add(CloudFieldNamingHelper.restoreDotsAfterReplacementWithSpaces(field.getLastLocationPathPart()));
        return joinedUsers;
    }

    static void waitForAllUsersToJoinAsOtherUserAsync(String gameRoom, String myIdentifier, OrReadyUsersListener onUserHasJoined) {
        ArrayList<User> joinedUsers = new ArrayList<>();
        ChildEventListenerStopper[] newUsersListenerStopper = new ChildEventListenerStopper[1];
        //waits for my mail to be true. If all of the collection was deleted, we need to stop the process and notify the user.
        RealtimeHelper.RealtimeHelperAsync.waitForFieldToChangeAndThenDoSomethingAsync(gameRoom + CloudConstants.cloudLocationSeparator + CloudConstants.readyUsersInMatchingUserGameSubCollection + CloudConstants.cloudLocationSeparator + myIdentifier, new OrValueChangedListener() {
            @Override
            public void onChanged(RealtimeField field) {
                //checks if my field was removed. If the field is removed, it means that the owner has created a new room and he is playing with other people (so he deleted the collection in createRoom). So we need to stop the room joining process for this user.
                if (field.getFieldValue() == null) {
                    //returns null to the listener if the owner had created a new game with other people. This means that the current player should exit the game.
                    onUserHasJoined.onAllUsersReady(null);
                }
                if (newUsersListenerStopper[0] != null)
                    newUsersListenerStopper[0].removeListener();
                User[] users = new User[joinedUsers.size()];
                joinedUsers.toArray(users);
                onUserHasJoined.onAllUsersReady(new Users(users));
            }
        });
        //listen to new children who join until all of the users joined (includes users who have already joined) and add them to the list. Do not show to the user because those users might be from the previous game because the owner did not open a room.
        newUsersListenerStopper[0] = RealtimeHelper.RealtimeHelperAsync.listenToNewChildrenAsync(gameRoom + CloudConstants.cloudLocationSeparator + CloudConstants.readyUsersInMatchingUserGameSubCollection, new OrChildAddedListener() {
            @Override
            public void onChildAdded(RealtimeField field) {
                String userIdentifier = CloudFieldNamingHelper.restoreDotsAfterReplacementWithSpaces(field.getLastLocationPathPart());
                //this does not include my own Identifier.
                if (!userIdentifier.equals(CloudFieldNamingHelper.restoreDotsAfterReplacementWithSpaces(myIdentifier))) {
                    //onUserHasJoined.onUserReady(userIdentifier);
                    joinedUsers.add(new User(userIdentifier));
                }
            }
        });
    }

    static void initializeUserGameRoomForMatching(String gameRoom, char exercisesSign) {
        removeAllSubCollectionsFromUserMainCollection(gameRoom);
        initializeUserGameRoomFields(gameRoom, exercisesSign);
    }
    static void initializeUserGameRoomFields(String gameRoom, char exerciseSign) {
        HashSet<RealtimeField> fields = new HashSet<>();
        fields.add(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.isEndGameUserGameCollectionProperty, false));
        fields.add(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.exercisesSignGameCollectionProperty, exerciseSign+""));
        RealtimeHelper.updateMultipleFieldsNoOrderSync(fields);
    }

    static void removeAllSubCollectionsFromUserMainCollection(String gameRoom) {
        HashSet<String> collectionsToRemove = new HashSet<>();
        collectionsToRemove.add(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.readyUsersInMatchingUserGameSubCollection);
        collectionsToRemove.add(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.readyUsersDuringGameUserGameSubCollection);
        collectionsToRemove.add(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.scoresOfUsersUserGameSubCollection);
        RealtimeHelper.removeMultipleFieldsNoOrderSync(collectionsToRemove);
    }

    static LearnedExerciseData downloadExerciseFromRoom(String gameRoom) {
        return (LearnedExerciseData) RealtimeHelper.getRealtimeFieldValueAtLocationAsCustomObjectSync(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.currentExerciseUserGameCollectionProperty, LearnedExerciseData.class);
    }

    static void signalThatIHaveJoinedTheRoom(String myIdentifier, String gameRoom) {
        RealtimeHelper.updateRealtimeFieldSync(new RealtimeField(gameRoom + CloudConstants.cloudLocationSeparator + CloudConstants.readyUsersInMatchingUserGameSubCollection + CloudConstants.cloudLocationSeparator + myIdentifier, false));
    }

    static void initializeCurrentExercise(String gameRoom, LearnedExerciseData exercise) {
        HashSet<RealtimeField> fields = new HashSet<>();
        fields.add(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.currentExerciseUserGameCollectionProperty, exercise));
        fields.add(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.exerciseNumberUserGameCollectionProperty, 1));
        RealtimeHelper.updateMultipleFieldsNoOrderSync(fields);
    }

    static ValueEventListenerStopper listenToEndGameFieldChangeAsync(String gameRoom, OrValueChangedListener listener) {
        return RealtimeHelper.RealtimeHelperAsync.waitForFieldToChangeAndThenDoSomethingAsync(gameRoom + CloudConstants.cloudLocationSeparator + CloudConstants.isEndGameUserGameCollectionProperty, listener);
    }

    static void signalThatIAmReadyToStartPlayingAsync(String gameRoom, String myIdentifier) {
        RealtimeHelper.RealtimeHelperAsync.updateRealtimeFieldAndDoSomethingWhenCompletedAsync(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.readyUsersDuringGameUserGameSubCollection+CloudConstants.cloudLocationSeparator+myIdentifier, true), null);
    }

    static boolean doesTheRoomExist(String gameRoom) {
        return RealtimeHelper.getRealtimeFieldValueAtLocationSync(gameRoom) != null;
    }

    static Users getReadyUsersSync(String gameRoom) {
        RealtimeField[] children = RealtimeHelper.getRealtimeFieldChildrenValuesAtLocationSync(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.readyUsersDuringGameUserGameSubCollection);
        int numOfReadyUsers = 0;
        for (RealtimeField field : children) {
            if (((boolean) field.getFieldValue()))
                numOfReadyUsers++;
        }
        User[] readyUsers = new User[numOfReadyUsers];
        int currentUserIndex = 0;
        for (RealtimeField field : children) {
            if (((boolean) field.getFieldValue()))
                readyUsers[currentUserIndex++] = new User(CloudFieldNamingHelper.restoreDotsAfterReplacementWithSpaces(field.getLastLocationPathPart()));
        }
        return new Users(readyUsers);
    }

    //returns weather or not everyone are already ready.
    static void waitUntilEveryoneIsReadyAndNotifyWhenSomeoneIsReadyAndUploadMyScoreAsync(String myIdentifier, String gameRoom, Users allUsersWithMe, OrReadyUsersListener onUserIsReady) {
        User[] users = new User[allUsersWithMe.getUsers().length-1];
        int[] currentIndex = new int[1];
        RealtimeHelper.RealtimeHelperAsync.waitForNChildrenToBeInTheCollectionAndThenDoSomethingAsync(gameRoom + CloudConstants.cloudLocationSeparator + CloudConstants.readyUsersDuringGameUserGameSubCollection, new OrNChildrenAddedListener() {
            @Override
            public void onAllAdded(RealtimeField[] fields) {
                //each user removes his field and then finishes. I have to remove the field because after the exercise I use OnChildAdded.
                RealtimeHelper.RealtimeHelperAsync.removeRealtimeFieldAndDoSomethingWhenCompletedAsync(gameRoom + CloudConstants.cloudLocationSeparator + CloudConstants.readyUsersDuringGameUserGameSubCollection + CloudConstants.cloudLocationSeparator + myIdentifier, new Runnable() {
                    @Override
                    public void run() {
                        onUserIsReady.onAllUsersReady(new Users(users));
                    }
                });
            }

            @Override
            public void onChildAdded(RealtimeField field) {
                if (!field.getLastLocationPathPart().equals(myIdentifier)) {
                    onUserIsReady.onUserReady(new User(CloudFieldNamingHelper.restoreDotsAfterReplacementWithSpaces(field.getLastLocationPathPart())));
                    users[currentIndex[0]++] = new User(CloudFieldNamingHelper.restoreDotsAfterReplacementWithSpaces(field.getLastLocationPathPart()));
                }
            }
        }, allUsersWithMe.getUsers().length);
        //after the listener is ready, I upload my score and signal that I am ready.
        GameOperationLowLevelStages.signalThatIAmReadyToStartPlayingAsync(gameRoom, myIdentifier);
    }

    static long getNumOfGamesDone(String gameRoom) {
        return (long) RealtimeHelper.getRealtimeFieldValueAtLocationSync(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.exerciseNumberUserGameCollectionProperty);
    }

    static void initializeNextExerciseAndUploadMyScoreAndWaitForAllUsersToBeReady(String myIdentifier, String gameRoom, LearnedExerciseData newExercise, int exerciseNumber, long myScore, int numOfOtherPlayers) {
        HashSet<RealtimeField> fields = new HashSet<>();
        fields.add(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.currentExerciseUserGameCollectionProperty, newExercise));
        fields.add(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.exerciseNumberUserGameCollectionProperty, exerciseNumber+1));
        fields.add(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.scoresOfUsersUserGameSubCollection+CloudConstants.cloudLocationSeparator+myIdentifier, myScore));
        RealtimeHelper.updateMultipleFieldsNoOrderSync(fields);
        RealtimeHelper.waitForNChildrenToBeAddedBeforeContinuingSync(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.readyUsersDuringGameUserGameSubCollection, numOfOtherPlayers);
    }

    static GameUser[] getUsersWithScores(String gameRoom) {
        RealtimeField[] users = RealtimeHelper.getRealtimeFieldChildrenValuesAtLocationSync(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.scoresOfUsersUserGameSubCollection);
        GameUser[] usersWithScores = new GameUser[users.length];
        for (int i = 0; i < users.length; i++)
            usersWithScores[i] = new GameUser(CloudFieldNamingHelper.restoreDotsAfterReplacementWithSpaces(users[i].getLastLocationPathPart()), (long)users[i].getFieldValue());
        return usersWithScores;
    }

    static void endTheGame(String gameRoom) {
        RealtimeHelper.updateRealtimeFieldSync(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.isEndGameUserGameCollectionProperty, true));
    }

    static char getExercisesSign(String gameRoom) {
        return ((String) RealtimeHelper.getRealtimeFieldValueAtLocationSync(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.exercisesSignGameCollectionProperty)).charAt(0);
    }

    static boolean uploadMyScoreAndWaitForSignalFromOwnerAboutGameContinuingSync(String gameRoom, String ownerIdentifier, String myIdentifier, long myScore) {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] isEndGame = new boolean[1];
        ValueEventListenerStopper[] stopper1 = new ValueEventListenerStopper[1];
        ValueEventListenerStopper[] stopper2 = new ValueEventListenerStopper[1];
        stopper1[0] = GameOperationLowLevelStages.listenToEndGameFieldChangeAsync(GameOperationLowLevelStages.getGameRoomForOwnerIdentifier(ownerIdentifier), new OrValueChangedListener() {
            @Override
            public void onChanged(RealtimeField field) {
                isEndGame[0] = true;
                stopper2[0].removeListener();
                latch.countDown();
            }
        });
        //waits that the collection will be deleted by the owner (that means that I can go on and download the scores).
        stopper2[0] = RealtimeHelper.RealtimeHelperAsync.waitForAFieldToBeRemovedAndThenDoSomethingAsync(gameRoom + CloudConstants.cloudLocationSeparator + CloudConstants.readyUsersDuringGameUserGameSubCollection, new Runnable() {
            @Override
            public void run() {
                stopper1[0].removeListener();
                latch.countDown();
            }
        });
        //uploading the score and notifying owner after preparing the listener, because if I will do it before preparing the listeners, there might be a timing problem that will cause the collection to be deleted before I uploaded my score.
        GameOperationLowLevelStages.uploadMyScoreAndThenNotifyOwnerAsync(gameRoom, myIdentifier, myScore);
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return isEndGame[0];
    }

    static void uploadMyScoreAndThenNotifyOwnerAsync(String gameRoom, String myIdentifier, long myScore) {
        RealtimeHelper.RealtimeHelperAsync.updateRealtimeFieldAndDoSomethingWhenCompletedAsync(new RealtimeField(gameRoom + CloudConstants.cloudLocationSeparator + CloudConstants.scoresOfUsersUserGameSubCollection + CloudConstants.cloudLocationSeparator + myIdentifier, myScore), new Runnable() {
            @Override
            public void run() {
                RealtimeHelper.RealtimeHelperAsync.updateRealtimeFieldAndDoSomethingWhenCompletedAsync(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.readyUsersDuringGameUserGameSubCollection+CloudConstants.cloudLocationSeparator+myIdentifier, true), null);
            }
        });
    }

    static String getGameRoomForOwnerIdentifier(String userIdentifier) {
        return CloudConstants.gameCloudLocation + CloudConstants.cloudLocationSeparator + userIdentifier;
    }
}
