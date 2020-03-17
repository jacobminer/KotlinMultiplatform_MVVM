package com.jarroyo.kmp_mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.lifecycle.ViewModelProviders
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.ExpandedWidth
import androidx.ui.layout.Row
import androidx.ui.layout.Spacing
import androidx.ui.material.Button
import androidx.ui.material.surface.Surface
import androidx.ui.text.ParagraphStyle
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.domain.model.github.GitHubRepo
import com.jarroyo.sharedcode.viewModel.github.*

class MainActivity : AppCompatActivity() {
    private lateinit var gitHubViewModel: GitHubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gitHubViewModel = ViewModelProviders.of(this).get(GitHubViewModel::class.java)

        setContent {
            Theme.withTheme {
                Items()
            }
        }
    }

    @Composable
    fun Items() {
        val itemsState = +observe<GetGitHubRepoListState?>(gitHubViewModel.getGitHubRepoListLiveData)

        Surface {
            when (itemsState) {
                null -> {
                    InitialState()
                }
                is LoadingGetGitHubRepoListState -> {
                    LoadingState()
                }
                is ErrorGetGitHubRepoListState -> {
                    val itemsResponse = itemsState.response as? Response.Error
                    ErrorState(itemsResponse?.exception?.message)
                }
                is SuccessGetGitHubRepoListState -> {
                    val itemsResponse = itemsState.response as Response.Success<List<GitHubRepo>>
                    ListState(itemsResponse.data)
                }
                else -> {
                    Column {
                        Text(text = "Help")
                    }
                }
            }
        }
    }

    @Composable
    fun InitialState() {
        Column(modifier = Spacing(8.dp)) {
            Button(text = "Load Data", onClick = {
                gitHubViewModel.getGitHubRepoList("jarroyoesp")
            }, modifier = Spacing(bottom = 16.dp).wraps(ExpandedWidth))
        }
    }

    @Composable
    fun LoadingState() {
        Column {
            Text(text = "Loading data...",
                modifier = Spacing(15.dp).wraps(ExpandedWidth),
                paragraphStyle = ParagraphStyle(textAlign = TextAlign.Center))
        }
    }

    @Composable
    fun ListState(list: List<GitHubRepo>) {
        VerticalScroller(modifier = Spacing(8.dp)) {
            Column {
                list.forEach {
                    Item(it)
                }
            }
        }
    }

    @Composable
    fun ErrorState(message: String?) {
        Column {
            Text(
                text = message ?: "Unknown Error",
                modifier = Spacing(5.dp).wraps(ExpandedWidth)
            )
        }
    }

    @Composable
    fun Item(item: GitHubRepo) {
        Row {
            Text(text = item.name, modifier = Spacing(10.dp).wraps(ExpandedWidth))
        }
    }

    @Preview
    @Composable
    fun LightPreview() {
        Theme.withLightTheme {
            gitHubViewModel = GitHubViewModel()
            Items()
        }
    }

    @Preview
    @Composable
    fun DarkPreview() {
        Theme.withDarkTheme {
            gitHubViewModel = GitHubViewModel()
            Items()
        }
    }
}
