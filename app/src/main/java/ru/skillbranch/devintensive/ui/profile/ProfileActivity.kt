package ru.skillbranch.devintensive.ui.profile


import android.content.res.Configuration
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.ui.custom.TextDrawable
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel


class ProfileActivity : AppCompatActivity() {
    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"

    }

    lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFilds: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { initUI(it) })
        viewModel.getAppTheme().observe(this, Observer { initTheme(it) })
        viewModel.getRepoStatus().observe(this, Observer { checkRepoStatus(it) })
    }

    private fun checkRepoStatus(isRepoValid: Boolean) {
        if (!isRepoValid) {
            wr_repository.error = resources.getString(R.string.error_message)
            wr_repository.isErrorEnabled = true
            sv_main.postDelayed({
               sv_main.scrollTo(0, sv_main.bottom)
            },
            100)

        } else {
            wr_repository.isErrorEnabled = false
            wr_repository.error = null
        }


    }

    private fun initTheme(mode: Int) {
        delegate.setLocalNightMode(mode)

    }

    private fun initUI(profile: Profile) {
        val initials = Utils.toInitials(profile.firstName, profile.lastName)
        if (initials != null) {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val color = if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                resources.getColor(R.color.color_accent_night, theme)
            } else {
                resources.getColor(R.color.color_accent, theme)
            }
            val drawable = TextDrawable.builder()
                .buildRound(initials, color)
            iv_avatar.setImageDrawable(drawable)

        }
        profile.toMap().also {
            for ((k, v) in viewFilds) {
                v.text = it[k].toString()
            }
        }
        addRepoListener()

    }

    private fun addRepoListener() {
        et_repository.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onRepoChanged(s.toString())
            }

        })

    }


    private fun saveProfileInfo() {

        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
            tv_nick_name.text = this.nickName
        }
    }

    private fun initViews(savedInstanceState: Bundle?) {
        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        viewFilds = mapOf(
            "nickname" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect
        )
        btn_edit.setOnClickListener {
            if (isEditMode) {
                saveProfileInfo()
            }
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)

        }
        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }
        showCurrentMode(isEditMode)
    }

    private fun showCurrentMode(isEditMode: Boolean) {
        val info = viewFilds.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEditMode
            v.isFocusableInTouchMode = isEditMode
            v.isEnabled = isEditMode
            v.background.alpha = if (isEditMode) 255 else 0
        }
        ic_eye.visibility = if (isEditMode) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEditMode
        with(btn_edit) {
            val filter: ColorFilter? = if (isEditMode) {
                PorterDuffColorFilter(resources.getColor(R.color.color_accent, theme), PorterDuff.Mode.SRC_IN)
            } else {
                null
            }
            val icon = if (isEditMode) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }
            background.colorFilter = filter
            setImageDrawable(icon)
        }


    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_EDIT_MODE, isEditMode)
    }
}
