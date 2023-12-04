package com.lizongying.mytv

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {

    private val playbackFragment = PlaybackFragment()
    private val mainFragment = MainFragment()

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_browse_fragment, playbackFragment)
                .add(R.id.main_browse_fragment, mainFragment)
                .commit()
        }
    }

    fun play(tv: TV) {
        playbackFragment.play(tv)
    }

    fun prev() {
        mainFragment.prev()
    }

    fun next() {
        mainFragment.next()
    }

    fun switchMainFragment() {
        val transaction = supportFragmentManager.beginTransaction()

        if (mainFragment.isHidden) {
            focusMainFragment()
            transaction.show(mainFragment)
        } else {
            transaction.hide(mainFragment)
        }

        transaction.commit()
    }

    fun focusMainFragment() {
        mainFragment.focus()
    }

    fun fragmentIsHidden(): Boolean {
        return mainFragment.isHidden
    }

    fun hideMainFragment() {
        if (!mainFragment.isHidden) {
            supportFragmentManager.beginTransaction()
                .hide(mainFragment)
                .commit()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return true
                }

                hideMainFragment()
                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2000)
                return true
            }

            KeyEvent.KEYCODE_MENU -> {
                val imageView = ImageView(this)
                val drawable = ContextCompat.getDrawable(this, R.drawable.appreciate)
                imageView.setImageDrawable(drawable)

                val parent = imageView.parent as? ViewGroup
                parent?.removeView(imageView)

                val linearLayout = LinearLayout(this)
                linearLayout.addView(imageView)

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.gravity = Gravity.BOTTOM
                imageView.layoutParams = layoutParams

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder
                    .setTitle("https://github.com/lizongying/my-tv/")
                    .setView(linearLayout)

                val dialog: AlertDialog = builder.create()
                dialog.show()
                return true
            }

            KeyEvent.KEYCODE_DPAD_CENTER -> {
                switchMainFragment()
            }

            KeyEvent.KEYCODE_DPAD_UP -> {
                if (mainFragment.isHidden) {
                    prev()
                }
            }

            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (mainFragment.isHidden) {
                    next()
                }
            }

            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (mainFragment.isHidden) {
                    prev()
                }
            }

            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (mainFragment.isHidden) {
                    next()
                }
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}