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
    @ObservedObject var counterLiveData: MvvmMutableLiveData
    @ObservedObject var listLiveData: MvvmMutableLiveData

    private var gitHubViewModel: GitHubViewModel
    private var counterViewModel: CounterViewModel

    init(counterViewModel: CounterViewModel, gitHubViewModel: GitHubViewModel) {
        self.gitHubViewModel = gitHubViewModel
        self.counterViewModel = counterViewModel

        self.counterLiveData = counterViewModel.getCounterLiveData
        self.listLiveData = gitHubViewModel.getGitHubRepoListLiveData

        self.counterLiveData.makeObservableForSwiftUI()
        self.listLiveData.makeObservableForSwiftUI()
    }

    func onDisappear(perform action: (() -> Void)? = nil) -> some View {
        self.gitHubViewModel.onCleared()
        self.counterViewModel.onCleared()
        return VStack() { Text("Removing") }
    }

    var body: some View {
        let counterState = self.counterLiveData.value as? GetCounterState
        let itemsState = self.listLiveData.value as? GetGitHubRepoListState

        let counterErrorResponse = counterState?.response as? Response.Error
        let itemsErrorResponse = itemsState?.response as? Response.Error

        let counterResponse = (counterState?.response as? Response.Success)?.data as? Int
        let itemsResponse = (itemsState?.response as? Response.Success)?.data as? Array<GitHubRepo>

        return VStack {
            if counterState == nil || itemsState == nil {
                initialState()
            } else if counterState is LoadingGetCounterState || itemsState is LoadingGetGitHubRepoListState {
                loadingState()
            } else if counterState is ErrorGetCounterState || itemsState is ErrorGetGitHubRepoListState {
                errorState(message: counterErrorResponse?.message ?? itemsErrorResponse?.message)
            } else if counterState is SuccessGetCounterState && itemsState is SuccessGetGitHubRepoListState {
                listState(value: counterResponse ?? 0, list: itemsResponse ?? [])
            } else {
                errorState(message: "Help")
            }
        }
    }

    func initialState() -> some View {
        return Button(action: {
            self.counterViewModel.getCounter()
            self.gitHubViewModel.getGitHubRepoList(username: "jarroyoesp")
        }) {
            Text("Load Data")
        }
    }

    func loadingState() -> some View {
        return Text("Loading data...")
    }

    func listState(value: Int, list: [GitHubRepo]) -> some View {
        return ScrollView(.vertical, showsIndicators: false) {
            VStack(alignment: .leading) {
                Text("Loaded \(value) repos").padding(EdgeInsets(top: 5, leading: 0, bottom: 15, trailing: 0))
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
        SwiftUIView(counterViewModel: CounterViewModel(), gitHubViewModel: GitHubViewModel())
    }
}

extension MvvmMutableLiveData: ObservableObject {
    func makeObservableForSwiftUI() {
        addObserver(observer: { thing in
            self.objectWillChange.send()
        })
    }
}
