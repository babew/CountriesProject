package lyubomir.babev.countries.project.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import lyubomir.babev.countries.project.databinding.ActivityInfoBinding

class InfoActivity: AppCompatActivity() {

    companion object {
        private const val ANIMATION_DURATION = 2000L
    }

    private lateinit var binding: ActivityInfoBinding

    private val handler     = Handler(Looper.getMainLooper())
    private val runnable    = Runnable {
        startActivity(Intent(this@InfoActivity, MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimation()
    }

    private fun startAnimation() {
        val alphaAnimation      = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = ANIMATION_DURATION

        binding.userImg.startAnimation(alphaAnimation)
        binding.projectTxt.startAnimation(alphaAnimation)
        alphaAnimation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                handler.postDelayed(runnable, ANIMATION_DURATION)
            }

            override fun onAnimationRepeat(animation: Animation?) {}

        })
        binding.nameTxt.startAnimation(alphaAnimation)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(runnable)
    }
}