package com.jeko.mynote;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NoteClickListener {

    private NoteViewModel mNoteViewModel;

    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final NoteListAdapter adapter = new NoteListAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        mNoteViewModel.getAllNotes().observe(this, notes -> {
            // Update the cached copy of the notes in the adapter.
            adapter.setNotes(notes);
        });

        fab.setOnClickListener(v -> {
            startNewNote();
        });
    }

    @Override
    public void onNoteClick(int position) {
        Note note = mNoteViewModel.getAllNotes().getValue().get(position);
        NewNoteActivity.openNote(this, note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startNewNote();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(MainActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startNewNote() {
        Intent intent = new Intent(this, NewNoteActivity.class);
        startActivity(intent);
    }
 }
