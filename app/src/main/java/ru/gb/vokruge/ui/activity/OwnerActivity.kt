package ru.gb.vokruge.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.owner_activity.*
import ru.gb.vokruge.R


class OwnerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: OwnerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.ThemeNoActionBar)
        setContentView(R.layout.owner_activity)
        viewModel = ViewModelProviders.of(this).get(OwnerViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar?.hide()

        val toggle = ActionBarDrawerToggle(
            this, container, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        container.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        viewModel.onCreate()
    }

    override fun onBackPressed() {
        if (container.isDrawerOpen(GravityCompat.START)) {
            container.closeDrawer(GravityCompat.START)
        } else {
            if (viewModel.onBackSearchClick()) {
                return
            }
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //Видимость неиспользуемых иконок отключена
            R.id.nav_settings -> {
                //todo
            }
            R.id.nav_add_event -> {
                //todo
            }
            R.id.nav_add_place -> {
                //todo
            }
            R.id.nav_send_email -> {
                //Пользователь может отправить сообщение в техподдержку по email
                val i = Intent(Intent.ACTION_SENDTO)
                i.data = Uri.parse("mailto:" + getString(R.string.support_email))
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.message_subject))
                i.putExtra(Intent.EXTRA_TEXT, getString(R.string.problem_message))
                try {
                    startActivity(Intent.createChooser(i, getString(R.string.send_mail_title)))
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(
                        this@OwnerActivity,
                        "\n" + getString(R.string.application_absent),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            R.id.nav_about_the_app -> {
                showInfoDialogFragment()
            }
            R.id.nav_share_app -> {
                //Передаем ссылку на приложение в "Google Play"
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_in_google_play))
                intent.type = "text/plain"
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@OwnerActivity,
                        "\n" + getString(R.string.application_absent),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            R.id.nav_rate_app -> {
                //Открываем приложение в "Google Play"
                val intent =
                    Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.app_in_google_play)))

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@OwnerActivity,
                        "\n" + getString(R.string.application_absent),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        container.closeDrawer(GravityCompat.START)
        return true
    }

    //показываем окно с информацией о приложении
    private fun showInfoDialogFragment() {
        val infoByApp = AlertDialog.Builder(this)
        infoByApp.setIcon(R.mipmap.ic_launcher)
        infoByApp.setTitle(R.string.app_name)
        infoByApp.setMessage(
            getString(R.string.about_the_project_1) + "\n \n" + getString(R.string.about_the_project_2)
        )
        infoByApp.setNegativeButton(getString(R.string.text_to_close), null)
        infoByApp.show()
    }
}
