package com.wizeline.academy.animations.ui.more_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.view.animation.AnimationUtils
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.wizeline.academy.animations.R
import com.wizeline.academy.animations.databinding.MoreDetailsFragmentBinding
import com.wizeline.academy.animations.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MoreDetailsFragment : Fragment() {
    private var _binding: MoreDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: MoreDetailsViewModel
    lateinit var scaleXImageViewAnimation: SpringAnimation
    lateinit var scaleYImageViewAnimation: SpringAnimation
    lateinit var scaleGestureDetector: ScaleGestureDetector
    var scaleFactor = 1f

    private val args: MoreDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoreDetailsFragmentBinding.inflate(inflater, container, false)
        binding.ivImageDetailLarge.loadImage(args.imageId)

        binding.tvTitle.animate()
            .translationX(10f)
            .alpha(1f)

        binding.ivImageDetailLarge.animate()
            .translationX(10f)
            .alpha(1f)

        binding.tvFullTextContent.animate()
            .translationX(10f)
            .alpha(1f)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.title.observe(viewLifecycleOwner) { binding.tvTitle.text = it }
        viewModel.content.observe(viewLifecycleOwner) { binding.tvFullTextContent.text = it }
        viewModel.fetchData(args.contentIndex)

        //Creates animation
        scaleXImageViewAnimation = SpringAnimation(binding.ivImageDetailLarge, DynamicAnimation.SCALE_X)
        scaleYImageViewAnimation = SpringAnimation(binding.ivImageDetailLarge, DynamicAnimation.SCALE_Y)

        //Creates spring with a final position
        val spring = SpringForce(1f)
        spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY

        //Add the sprint to the animation
        scaleXImageViewAnimation.spring = spring
        scaleYImageViewAnimation.spring = spring

        scaleGestureDetector = ScaleGestureDetector(requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    scaleFactor *= detector.scaleFactor

                    binding.ivImageDetailLarge.scaleX *= scaleFactor
                    binding.ivImageDetailLarge.scaleY *= scaleFactor

                    return true
                }
            })

        initListeners()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners()
    {
        binding.ivImageDetailLarge.setOnTouchListener { view, motionEvent ->
            scaleGestureDetector.onTouchEvent(motionEvent)
            when(motionEvent?.action){
                MotionEvent.ACTION_UP ->{
                    scaleXImageViewAnimation.start()
                    scaleYImageViewAnimation.start()
                }
                else ->{
                    scaleXImageViewAnimation.cancel()
                    scaleYImageViewAnimation.cancel()
                }
            }
            true
        }
    }
}