package com.wizeline.academy.animations.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.wizeline.academy.animations.R
import com.wizeline.academy.animations.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get()  = _binding!!

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.rvImages.apply {
            adapter = HomeAdapter { goToDetailScreen(it) }
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        return binding.root
    }

    private fun goToDetailScreen(id: Int) {
        val directions = HomeFragmentDirections.toDetailFragment(id)
        findNavController().navigate(directions)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.images.observe(viewLifecycleOwner) { images -> loadImages(images) }
    }

    private fun loadImages(images: List<Int>) {
        binding.rvImages.apply {
            val adapter = this.adapter as HomeAdapter
            adapter.updateDateSet(images)
            adapter.notifyDataSetChanged()
        }
    }
}