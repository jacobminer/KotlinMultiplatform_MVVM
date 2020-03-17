//
//  SwiftUIView.swift
//  iOSApp
//
//  Created by Jake Miner on 2020-01-15.
//  Copyright © 2020 Javier Arroyo. All rights reserved.
//

import SwiftUI
import SharedCode
import Combine

struct SwiftUIView: View {
    @ObservedObject var listLiveData: MvvmMutableLiveData

    private var gitHubViewModel: GitHubViewModel

    init(gitHubViewModel: GitHubViewModel) {
        self.gitHubViewModel = gitHubViewModel
        self.listLiveData = gitHubViewModel.getGitHubRepoListLiveData
        self.listLiveData.makeObservableForSwiftUI()
    }

    func onDisappear(perform action: (() -> Void)? = nil) -> some View {
        self.gitHubViewModel.onCleared()
        return VStack() { Text("Removing") }
    }

    var body: some View {
        let itemsState = self.listLiveData.value as? GetGitHubRepoListState
        let itemsErrorResponse = itemsState?.response as? Response.Error
        let itemsResponse = (itemsState?.response as? Response.Success)?.data as? Array<GitHubRepo>

        return VStack {
            if itemsState == nil {
                initialState()
            } else if itemsState is LoadingGetGitHubRepoListState {
                loadingState()
            } else if itemsState is ErrorGetGitHubRepoListState {
                errorState(message: itemsErrorResponse?.exception.message)
            } else if itemsState is SuccessGetGitHubRepoListState {
                listState(list: itemsResponse ?? [])
            } else {
                errorState(message: "Help")
            }
        }
    }

    func initialState() -> some View {
        return Button(action: {
            self.gitHubViewModel.getGitHubRepoList(username: "jarroyoesp")
        }) {
            Text("Load Data")
        }
    }

    func loadingState() -> some View {
        return Text("Loading data...")
    }

    func listState(list: [GitHubRepo]) -> some View {
        return ScrollView(.vertical, showsIndicators: false) {
            VStack(alignment: .leading) {
                ForEach(list, id: \.self) { item in
                    self.itemView(for: item)
                }
            }
        }
    }

    func errorState(message: String?) -> some View {
        return Text(message ?? "Unknown Error")
    }

    func itemView(for item: GitHubRepo) -> some View {
        return Text(item.name).multilineTextAlignment(.leading).padding(EdgeInsets(top: 5, leading: 0, bottom: 5, trailing: 0))
    }
}

struct SwiftUIView_Previews: PreviewProvider {
    static var previews: some View {
        SwiftUIView(gitHubViewModel: GitHubViewModel())
    }
}

extension MvvmMutableLiveData: ObservableObject {
    func makeObservableForSwiftUI() {
        addObserver(observer: { thing in
            self.objectWillChange.send()
        })
    }
}
