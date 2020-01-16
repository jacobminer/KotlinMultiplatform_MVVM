//
//  ViewController.swift
//  iOSApp
//
//  Created by Javier Arroyo on 11/12/2019.
//  Copyright © 2019 Javier Arroyo. All rights reserved.
//

import UIKit
import SharedCode

class ViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    // View
    @IBOutlet weak var mButton: UIButton!
    @IBOutlet weak var mCounterLabel: UILabel!
    @IBOutlet weak var mTableView: UITableView!
    
    private var mCounterViewModel: CounterViewModel!
    private var mGitHubViewModel: GitHubViewModel!
    
    // Table View Data
    internal var mGitHubRepoList: [GitHubRepo] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configView()

        mCounterViewModel = CounterViewModel()
        mGitHubViewModel = GitHubViewModel()
    }
    
    func configView() {
        mButton.addTarget(self, action: #selector(didButtonClick), for: .touchUpInside)
        
        mTableView.dataSource = self
        mTableView.delegate = self

        mCounterViewModel.getCounterLiveData.addObserver { (mCurrentState) in
            if (mCurrentState is SuccessGetCounterState) {
                let successState = mCurrentState as! SuccessGetCounterState
                let response = (successState.response as! Response.Success)
                let value = response.data as! Int
                self.mCounterLabel.text = String(value)

            } else if (mCurrentState is LoadingGetCounterState) {
                self.mCounterLabel.text = "Loading"
            } else if (mCurrentState is ErrorGetCounterState) {
                self.mCounterLabel.text = "ERROR"
            }

        }

        mGitHubViewModel.getGitHubRepoListLiveData.addObserver { (mCurrentState) in
            if (mCurrentState is SuccessGetGitHubRepoListState) {
                let successState = mCurrentState as! SuccessGetGitHubRepoListState
                let response = (successState.response as! Response.Success)
                let value = response.data as! [GitHubRepo]

                self.update(list: value)

            } else if (mCurrentState is LoadingGetGitHubRepoListState) {
                self.mCounterLabel.text = "Loading"
            } else if (mCurrentState is ErrorGetGitHubRepoListState) {
                self.mCounterLabel.text = "ERROR"
            }

        }
    }
    
    /****************************************************************************
     * ON CLICKS
     ****************************************************************************/
    @objc func didButtonClick(_ sender: UIButton) {
        mCounterViewModel.getCounter()
        mGitHubViewModel.getGitHubRepoList(username: "jarroyoesp")
    }
    
    /*****************************************************************************
     TABLE VIEW
     ****************************************************************************/
    internal func update(list: [GitHubRepo]) {
        mGitHubRepoList.removeAll()
        mGitHubRepoList.append(contentsOf: list)
        mTableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return mGitHubRepoList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "locationListCell", for: indexPath)
        let entry = mGitHubRepoList[indexPath.row]
        
        cell.textLabel?.text = entry.name
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let entryNum = mGitHubRepoList[indexPath.row].name
    }
    
    deinit {
        mCounterViewModel.onCleared()
    }
    
}


