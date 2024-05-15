package or.nevet.math_revisor.main;

import androidx.appcompat.widget.AppCompatImageButton;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import or.nevet.math_revisor.R;
import or.nevet.orexercises.main.CreateMultiplayerRoomActivity;
import or.nevet.orexercises.main.JoinMultiplayerRoomActivity;
import or.nevet.orgeneralhelpers.music_related.ActivityOpeningHelper;
import or.nevet.orgeneralhelpers.music_related.MusicActivity;
import or.nevet.orgeneralhelpers.music_related.MusicSubActivity;

public class MultiplayerActivity extends MusicSubActivity {

    public AppCompatImageButton back;
    public Button joinRoom;
    public Button createRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        back = findViewById(R.id.back2);
        back.setOnClickListener(this);
        joinRoom = findViewById(R.id.join_room);
        createRoom = findViewById(R.id.create_room);
        joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOpeningHelper.openActivity(MultiplayerActivity.this, JoinMultiplayerRoomActivity.class, null);
            }
        });
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOpeningHelper.openActivity(MultiplayerActivity.this, CreateMultiplayerRoomActivity.class, null);
            }
        });
    }
}