package pnj.exam.contactlistapp

import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.CursorAdapter
import android.widget.ListView

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.loader.app.LoaderManager

import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    private lateinit var cursorAdapter: CursorAdapter

    companion object {
        private const val AUTHORITY = "pnj.exam.mycontact"
        private const val BASE_PATH = "contacts"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$BASE_PATH")

        // Constants to identify the requested operation
        private const val CONTACTS = 1
        private const val CONTACT_ID = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, BASE_PATH, CONTACTS)
            addURI(AUTHORITY, "$BASE_PATH/#", CONTACT_ID)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cursorAdapter = ContactsCursorAdapter(this, null, 0)
        val list: ListView = findViewById(R.id.list)
        list.adapter = cursorAdapter

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportLoaderManager.initLoader(0, null, this)


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { restartLoader() }
    }

    private fun restartLoader() {
        supportLoaderManager.restartLoader(0, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(this, CONTENT_URI, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        cursorAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        cursorAdapter.swapCursor(null)
    }
}