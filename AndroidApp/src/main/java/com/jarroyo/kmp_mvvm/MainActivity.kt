package com.jarroyo.kmp_mvvm

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProviders
import com.jarroyo.sharedcode.viewModel.github.GitHubViewModel
import kotlinx.android.synthetic.main.main.*

class MainActivity : AppCompatActivity() {
    private lateinit var gitHubViewModel: GitHubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gitHubViewModel = ViewModelProviders.of(this).get(GitHubViewModel::class.java)

        setContentView(R.layout.main)

        gitHubViewModel.issuesList.addObserver {
            it ?: return@addObserver
            Log.d("MainActivity", "$it")
        }

        button.setOnClickListener {
            gitHubViewModel.setAuthToken(editText.text.toString())
            editText.isGone = true
            gitHubViewModel.getIssuesList()
        }
    }
}
