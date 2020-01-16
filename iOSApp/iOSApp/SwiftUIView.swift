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

    init(counterViewModel: CounterViewModel, gitHubViewModel: GitHubViewModel) {
        self.gitHubViewModel = gitHubViewModel
        self.counterLiveData = counterViewModel.getCounterLiveData
        self.listLiveData = gitHubViewModel.getGitHubRepoListLiveData

        self.counterLiveData.makeObservable()
        self.listLiveData.makeObservable()
    }

    var body: some View {
        return VStack {
            Text("Hello, World! \(counterLiveData.value.debugDescription)")
            Button(action: {
                self.gitHubViewModel.getGitHubRepoList(username: "jarroyoesp")
                if let value = self.counterLiveData.value as? Int {
                    self.counterLiveData.value = value + 1
                } else {
                    self.counterLiveData.value = 0
                }
            }) {
                Text("Hello, World!")
            }
        }
    }
}

struct SwiftUIView_Previews: PreviewProvider {
    static var previews: some View {
        SwiftUIView(counterViewModel: CounterViewModel(), gitHubViewModel: GitHubViewModel())
    }
}

extension MvvmMutableLiveData: ObservableObject {
    func makeObservable() {
        _ = MyObserver(object: self)
    }
}

class MyObserver: NSObject {
    @objc var objectToObserve: MvvmMutableLiveData
    var observation: NSKeyValueObservation?

    init(object: MvvmMutableLiveData) {
        objectToObserve = object
        super.init()

        observation = observe(
            \.objectToObserve.value,
            options: [.old, .new]
        ) { object, change in
            self.objectToObserve.objectWillChange.send()
        }
    }
}
