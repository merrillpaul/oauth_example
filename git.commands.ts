/* jslint node: true */
"use strict";

let child_process = require("child_process");
let readline = require("readline");
let OK = "\x1b[32m";
let NOK = "\x1b[31m";
let WARN ="\x1b[43m";
let RESET = "\x1b[0m";


class RunCmd {
    static run(cmd) {
        return new Promise((resolve, reject) => {
            let opts = {
                maxBuffer: 1024 * 1024 // increase if necessary
            };
            child_process.exec(cmd, opts, (err, stdout, stderr) => {
                if (err) {
                    reject("cmd: " + cmd + " : " + err + " : " + stderr);
                } else {
                    resolve(stdout);
                }
            });
        });
    }
}



class GitPusher {

    _gitStatus() {
        console.log('Validating that the repository is in a clean state');
        return RunCmd.run("git status --porcelain");       
    }

    _countChanges(data) {
        let changes =  data.split('\n').length;
        return new Promise((resolve, reject) => {
            if (changes - 1 === 0 ) {
                resolve(true);
            } else {
               reject("Git repo is not clean! Did you forget to commit changes?");               
            }
        });
    }

    _hasBranches(branchName) {
        return RunCmd.run('git branch -r')
        .then((originBranches) => {
            let branches = originBranches.split('\n').filter((it) => {
                return it.length > 0;
            }).map((it) => {
                return it.split('/')[1];
            });
            let found = branches.indexOf(branchName) !== -1;
            let res = { found: found, name: branchName};           
            return res;
        });
    }

    _showPending(branchInfo) {
        let branchName = branchInfo.name;
        let p = new Promise((resolve, reject) => {
            if (branchInfo.found) {
                console.log("\n\nShowing you what would be pushed from branch '" + branchName + "'");
                RunCmd.run("git log origin/" + branchName + "..HEAD --no-merges")
                .then((result) => { 
                    console.log(result);
                    resolve(branchName);
                });
            } else {
                resolve(branchName);
            }
        });
        return p;
    }

    _confirmToPush(branchName) {
       console.log("Are you sure you want to push these changes to '" + branchName + "'? Type '" + branchName + "' to push or enter to cancel.");
       let p = new Promise((resolve, reject) => {
       let rl = readline.createInterface({
                input: process.stdin,
                output: process.stdout,
                terminal: false
        });

        rl.on('line', function(line){
            if (line.trim().length === 0 ) {
                reject("Not pushing changes now");
            } else {
                resolve(branchName);
            }
        });
       });

       return p;
    }

    _getBranchName() {
        return RunCmd.run("git symbolic-ref --short HEAD").then((res) => {
            return res.split('\n')[0];
        });    
    }

    _pushNow(branchName) {
        return RunCmd.run('git push origin HEAD');
    }

    run() {
        Promise.resolve(true)
        .then(this._gitStatus)   
        .then(this._countChanges)   
        .then(this._getBranchName)  
        .then(this._hasBranches)
        .then(this._showPending)
        .then(this._confirmToPush)
        .then(this._pushNow)
        .then((res) => {
            console.log(OK,"All pushed", res);
            process.exit(0);
        })
        .catch(errMsg => {
			console.error(NOK, "@@@@ Not Pushing !! ->", errMsg);
			process.exit(1);
		});
        
    }
}

class GitMerge {

    _getDestBranch() {
        let p = new Promise((resolve, reject) => {
            RunCmd.run("git branch").then((res) => {
                let branchName =  res.split('\n').find (it => it.startsWith('*'))
                .split('* ')[1];
                resolve(branchName);
            }).catch(errorMsg => reject (errorMsg));
        });
        return p;
    }

    _getChanges(destBranch) {
        console.log("This merge will add the following commits to " + this.destBranch);        
        return RunCmd.run('git log "HEAD..' + this.sourceBranch + '"').then((changes) => {
            return {
                changes: changes,
                destBranch: destBranch,
                sourceBranch: this.sourceBranch
            }
        });
    }

     _confirmToMerge(changes) {
      
       console.log("Are you sure you want to merge from", NOK, "[" + changes.sourceBranch + "]", RESET, "to", NOK , "[" + changes.destBranch + "]", RESET, "(yes/no)?");
       let p = new Promise((resolve, reject) => {
       let rl = readline.createInterface({
                input: process.stdin,
                output: process.stdout,
                terminal: false
        });

        rl.on('line', function(line){
            if (line === 'yes') {
                resolve(changes.sourceBranch);
            } else {
                reject("Not merging because you didn't answer yes.");  
            }          
        });
       });

       return p;
    }

    _mergeNow(branchName) {
        return RunCmd.run('git merge ' + branchName);
    }

    run() {
        let commands = process.argv.splice(2);
        if (commands.length !== 2) {
            console.log(NOK, "Usage: ./gitmerge.sh <branchname>");
            process.exit(-1);
        }

        this.sourceBranch = commands[1];
        Promise.resolve(true)
        .then(this._getDestBranch)
        .then((destBranch) => this._getChanges(destBranch))
        .then(this._confirmToMerge)
        .then(this._mergeNow)
        .then(res => { 
            console.log(OK, "Merged"); 
            process.exit(0);
        })
         .catch(errMsg => {
			console.error(NOK, "@@@@ Not Merging !! ->", errMsg);
			process.exit(1);
		});
    }
}


class Tools {
    static run() {
        if (process.argv. length <= 2) {
            console.log("Usage: node git.commands.ts push|merge");
            process.exit(-1);
        }

        let command = process.argv[2];
        let runner;
        switch (command) {
            case  'push': runner = new GitPusher(); break;
            case 'merge': runner = new GitMerge(); break;
            default: console.error("Invalid command"); process.exit();
        }
        runner.run();
    }
}

Tools.run();
