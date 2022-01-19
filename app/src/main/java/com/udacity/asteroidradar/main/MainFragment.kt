package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel


        val adapter = AsteroidsAdapter(AsteroidsListener {
                asteroidId -> viewModel.onAsteroidClicked(asteroidId)
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner, Observer{
                asteroid -> asteroid?.let{
            this.findNavController().navigate(
                MainFragmentDirections.actionShowDetail(asteroid))
            viewModel.onDetailsFragmentNavigated()
        }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer{
            it ?.let{
                adapter.submitList(it)
                Log.i("Main Activity", "Status changed")
            }
        })

        setHasOptionsMenu(true)

        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when(item.itemId){
                R.id.show_onwards_menu -> AsteroidsFilter.SHOW_ONWARDS
                R.id.show_today_menu -> AsteroidsFilter.SHOW_TODAY
                else -> AsteroidsFilter.SHOW_SAVED
            }
        )
        return true
    }
}