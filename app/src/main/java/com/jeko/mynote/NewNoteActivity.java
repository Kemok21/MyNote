package com.jeko.mynote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewNoteActivity extends AppCompatActivity {

    @BindView(R.id.title)
    EditText mEditTitleView;
    @BindView(R.id.content)
    EditText mEditContentView;

    private Note mNote;
    private NoteViewModel mNoteViewModel;

    public NewNoteActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ButterKnife.bind(this);

        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        Intent intent = getIntent();
        if (intent.hasExtra("note")) {
            mNote = intent.getParcelableExtra("note");
            setTitle(R.string.title_edit);
            mEditTitleView.setText(mNote.getTitle());
            mEditContentView.setText(mNote.getContent());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this ia a new Note, hide the "Delete" menu item.
        if (mNote == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveNote();
                return true;
            case R.id.action_delete:
                showDeleteNoteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (mNote != null) {
                    String title = mEditTitleView.getText().toString();
                    String content = mEditContentView.getText().toString();
                    if (!(title.equals(mNote.getTitle()) && content.equals(mNote.getContent()))) {
                        showUpdateNoteConfirmationDialog();
                    } else {
                        NavUtils.navigateUpFromSameTask(NewNoteActivity.this);
                    }
                } else {
                    saveNote();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteNoteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_note_dialog_massage)
                .setPositiveButton(R.string.delete_note, (dialog, which) -> {
                    // User clicked the "Delete" button, so delete the note.
                    deleteNote();
                })
                .show();
    }

    private void showUpdateNoteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.update_note_dialog_massage)
                .setPositiveButton(R.string.save_changes, (dialog, which) -> {
                    // User clicked the "Change" button, so change the note.
                    saveNote();
                    finish();
                })
                .setNegativeButton(R.string.cancel_changes, (dialog, which) -> finish())
                .show();
    }

    private void deleteNote() {
        mNoteViewModel.delete(mNote);
        Toast.makeText(
                getApplicationContext(),
                R.string.toast_delete_note,
                Toast.LENGTH_LONG).show();
        finish();
    }

    private void saveNote() {
        String title = mEditTitleView.getText().toString();
        String content = mEditContentView.getText().toString();
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.toast_empty_note,
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (title.isEmpty()) {
            title = makeTitleByContent(content);
            mEditTitleView.setText(title);
        }
        if (mNote != null) {
            if (!(title.equals(mNote.getTitle()) && content.equals(mNote.getContent()))) {
                mNote.setTitle(title);
                mNote.setContent(content);
                mNote.setDate(dateNow());
                mNoteViewModel.insert(mNote);
            } else return;
        } else {
            mNote = new Note(
                    title,
                    content,
                    dateNow()
            );
            mNoteViewModel.insert(mNote);
        }
        Toast.makeText(
                getApplicationContext(),
                R.string.toast_save_note,
                Toast.LENGTH_LONG).show();
        finish();
    }
    // If title is empty, make it from content
    private String makeTitleByContent(String content) {
        String res = "";
        String line = content.split("\n")[0].trim();
        if (line.length() < 18) return line;
        else {
            String[] words = line.split(" ");
            if (words[0].length() > 18) return words[0].substring(0, 16) + "...";
            for (String word : words) {
                if (word.length() + res.length() > 18) {
                    return res.trim();
                } else {
                    res += " " + word;
                }
            }
            return res.trim();
        }
    }

    private String dateNow() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    public static void openNote(MainActivity activity, Note note) {
        Intent intent = new Intent(activity, NewNoteActivity.class);
        intent.putExtra("note", note);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveNote();
    }
}
