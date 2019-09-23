package ru.skillbranch.devintensive.ui.main


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.archive.ArchiveActivity
import ru.skillbranch.devintensive.ui.custom.SimpleItemDecorator
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.MainViewModel


class MainActivity : AppCompatActivity() {


    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initViews()
        initViewModel()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = (menu?.findItem(R.id.action_search))?.actionView as SearchView
        searchView.queryHint = "Введите имя пользователя"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearchQuery(newText)
                return true


            }

        })
        return super.onCreateOptionsMenu(menu)
    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initViews() {
        val simpleItemDecorator = SimpleItemDecorator(this@MainActivity)
        chatAdapter = ChatAdapter {
            if (it.chatType == ChatType.ARCHIVE) {
                val intent = Intent(this, ArchiveActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(rv_chat_list, "Click on ${it.title}", Snackbar.LENGTH_LONG).show()
            }
        }
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter, false)
        { chatItem: ChatItem?, swipingPosition: Int ->
            simpleItemDecorator.swipingItemNumber = swipingPosition
            if (chatItem != null) {
                val chatId = chatItem.id
                viewModel.addToArchive(chatId)
                val snackbar = Utils.createSnackbar(
                    rv_chat_list,
                    resources.getString(R.string.snackbar_text, chatItem.title),
                    this
                )
                snackbar.setAction("Отмена") {
                    viewModel.restoreFromArchive(chatId)
                }
                snackbar.show()
            }
        }
        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_chat_list)
        with(rv_chat_list) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(simpleItemDecorator)
        }
        fab.setOnClickListener {
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)

        }

    }


    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getChatData(isArchived = false)
            .observe(this, Observer { chatAdapter.updateData(it) })

    }


}